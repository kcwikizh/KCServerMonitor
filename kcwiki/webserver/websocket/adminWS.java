/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.websocket;

/**
 *
 * https://www.oschina.net/translate/java-ee-html5-websocket-example
 * @author iTeam_VEP
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import static moe.kcwiki.handler.massage.msgBroadcast.guestmsgbcer;
import moe.kcwiki.tools.daemon.CatchError;

@ServerEndpoint("/adminWS")
public class adminWS {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private  static final AtomicInteger onlineCount = new AtomicInteger(1);
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<adminWS> webSocketSet = new CopyOnWriteArraySet<adminWS>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    //private int reConnectCount = 0;
    private Session session;
    
  @OnMessage
  public void onMessage(String message, Session session) 
  {
    //System.out.println("Received: " + message);
    //System.out.println(this.session==session);
    guestmsgbcer.broadcast(message, 100);
  }
  
  @OnOpen
  public void onOpen(Session session) throws IOException {
      //session.setMaxIdleTimeout(60000);
      if(webSocketSet.size() > 10) {
          for(adminWS user:webSocketSet) {
              user.session.close();
          }
          webSocketSet.clear();
      }
      this.session = session;
      webSocketSet.add(this);
      //remoteAddress = String.valueOf(session.getUserProperties().get("javax.websocket.endpoint.remoteAddress"));
      //连通性测试
    //testmsg();
    /*
    HashMap<String, Object> data = new  HashMap<>();
              data.put("type", 2);
              data.put("message", session.getId());
        session.getBasicRemote().sendText(JSON.toJSONString(data));
        data.clear();
    */
  }

  @OnClose
  public void onClose(Session session) {
        webSocketSet.remove(this);
        try {
            session.close();
        } catch (IOException ex) {
            CatchError.WriteError("adminWS-onClose-IOException");
            Logger.getLogger(adminWS.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  @OnError
  public void onError(Session session, Throwable thr) {
        webSocketSet.remove(this);
        try {
            session.close();
        } catch (IOException ex) {
            CatchError.WriteError("adminWS-onClose-IOException");
            Logger.getLogger(adminWS.class.getName()).log(Level.SEVERE, null, ex);
              try {
                this.session.close();
              } catch (IOException e1) {
                CatchError.WriteError("adminWS-onError-IOException");
                Logger.getLogger(adminWS.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
  }
  
  
  public void broadcast(String info,int type){
      HashMap<String, Object> data = new  HashMap<>();
      webSocketSet.forEach((w) -> {
          try {
              /*
              if(admin.sessionSet.equals(w.session.getId())){
                
              }
                */
              data.put("type", type);
                data.put("message", info);
                synchronized (adminWS.class) {
                    w.session.getBasicRemote().sendText(JSON.toJSONString(data));
                    data.clear();
                }
          } catch (IOException e) {
              webSocketSet.remove(w);
              try {
                  w.session.close();
              } catch (IOException e1) {
                  
              }
          }
        });
    }
  
    private void testmsg(){
        for(int i=0;i<5;i++){
            try {
                broadcast("测试次数： "+i,10);
                sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(guestWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static String filter(String message){
        if(message==null){
            return null;
        }
        return message;
    }
}
