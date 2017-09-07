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
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import static moe.kcwiki.massagehandler.msgBroadcast.adminmsgbcer;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.massagehandler.msgPublish.isAllowPublish;
import moe.kcwiki.tools.CatchError;

@ServerEndpoint("/guestWS")
public class guestWS {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private  static final AtomicInteger onlineCount = new AtomicInteger(0);
    private  static final AtomicInteger sumCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<guestWS> webSocketSet = new CopyOnWriteArraySet<guestWS>();
    private final String nickname;
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    public  guestWS() {
        nickname = "游客"+sumCount.incrementAndGet(); 
    }
    
  @OnMessage
  public void onMessage(String message, Session session) 
    throws IOException, InterruptedException {
      
    //System.out.println("Received: " + message);
    //System.out.println(this.session==session);
    broadcast(String.format("%s:%s",nickname,filter(message)),100);
  }
  
  @OnOpen
  public void onOpen(Session session){
        try {
            session.setMaxIdleTimeout(30000);
            this.session = session;
            webSocketSet.add(this);
            int count = onlineCount.incrementAndGet();
            delayMsg(session,this);
            sleep(1000);
            broadcast(String.valueOf(count),1);
            System.out.println("Client"+ nickname +"connected");
        } catch (InterruptedException ex) {
            webSocketSet.remove(this);
            try {
                session.close();
            } catch (IOException e) {
                CatchError.WriteError("adminWS-onOpen-IOException");
            }
            CatchError.WriteError("adminWS-onOpen-InterruptedException");
            Logger.getLogger(guestWS.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

  @OnClose
  public void onClose() {
      webSocketSet.remove(this);
      broadcast(String.valueOf(onlineCount.decrementAndGet()),1);
      System.out.println("Connection"+ nickname +"closed");
  }
  
  @OnError
  public void onError(Session session, Throwable thr) {
        webSocketSet.remove(this);
        try {
            session.close();
        } catch (IOException ex) {
            CatchError.WriteError("adminWS-onClose-IOException");
            Logger.getLogger(adminWS.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  public void broadcast(String info,int type){
      adminmsgbcer.broadcast(info, type);
      webSocketSet.forEach((w) -> {
          try {
              HashMap<String, Object> data = new  HashMap<>();
              data.put("type", type);
              data.put("message", info);
              synchronized (guestWS.class) {
                  w.session.getBasicRemote().sendText(JSON.toJSONString(data));
                  data.clear();
              }
          } catch (IOException e) {
              CatchError.WriteError("向客户端"+w.nickname+"发送消息失败");
              webSocketSet.remove(w);
              try {
                  w.session.close();
              } catch (IOException e1) {}
          }
        });
    }
  
    private void delayMsg(Session session,guestWS guest){
        new Thread() { 
            @Override
            public void run() {
                try {
                    sleep(5000);
                    HashMap<String, Object> data = new  HashMap<>();
                    if(!isAllowPublish()){
                        data.put("type", "103");
                        data.put("message", JSON.toJSONString(msgPublish.getUrlprePublishList()));
                        session.getBasicRemote().sendText( JSON.toJSONString(data));
                        data.clear();
                    }else if(msgPublish.getUrlListSize()>1){
                        sleep(5000);
                        data.put("type", "102");
                        data.put("message", JSON.toJSONString(msgPublish.getUrlList()));
                        session.getBasicRemote().sendText( JSON.toJSONString(data));
                        data.clear(); 
                    }
                } catch (InterruptedException | IOException ex) {
                    webSocketSet.remove(guest);
                    try {
                        session.close();
                    } catch (IOException e1) {}
                    broadcast(String.valueOf(onlineCount.decrementAndGet()),1);
                    CatchError.WriteError("moe.kcwiki.webserver.websocket-guestWS-delayMsg: 发生InterruptedException | IOException 错误。");
                    Logger.getLogger(guestWS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }
    
    /**
     * @return the onlineCount
     */
    public static int getOnlineCount() {
        return onlineCount.get();
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
