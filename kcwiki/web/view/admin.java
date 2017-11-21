/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.*;
import java.util.HashMap;
import javax.servlet.http.*;
import moe.kcwiki.monitor.start2.GetStart2;
import moe.kcwiki.initializer.GetModifiedDataThread;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.initializer.Start2DataThread;
import moe.kcwiki.handler.thread.Controller;
import moe.kcwiki.handler.thread.modifieddataPool;
import moe.kcwiki.handler.thread.start2dataPool;

public class admin extends HttpServlet{

  protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
            
    HashMap<String, Object> data = new  HashMap<>();
    HttpSession session = request.getSession(false); 
    if(session == null || session.getAttribute("hsaLogin") != "true"){
        data.put("status", "error");
        String host = request.getServerName();
        if(host.equals("127.0.0.1")){
            data.put("info", request.getScheme()+"://"+"x.kcwiki.org/login.html");
        } else if (host.contains("kcwiki")){
            data.put("info", request.getScheme()+"://"+request.getServerName()+"/login.html"); 
        } else if (host.contains("45.")) {
            data.put("info", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/KcWikiOnline/login.html"); 
        }
        //data.put("info", request.getScheme()+"://"+"x.kcwiki.moe/login.html");
        /*
        if(session==null){
            data.put("info", "session==null");
        }
        if(session.getAttribute("hsaLogin")!="true"){
            data.put("info", "session!=true");
        }
*/
    }else{
        String parameter=request.getParameter("parameter");
        data.put("status", "success");
        if(parameter != null){
            switch(parameter){
                default:
                    data.put("info", "指令有误。");
                    break;
                case "fileScanner":
                    if(modifieddataPool.isIsOnline()){
                        data.put("info", "GetModifiedData子线程已启动，请勿重复提交。");
                        break;
                    }
                    if(Controller.isIsPoolInit()){
                        MainServer.setStopScanner(false);
                        new GetModifiedDataThread().StartThread();
                        data.put("info", "null");
                    }else{
                        data.put("info", "进程池已关闭，请重新启动进程池再进行操作。");
                    }
                    break;
                case "dmmLogin":
                    if(start2dataPool.isIsOnline()){
                        data.put("info", "Start2Data子线程已启动，请勿重复提交。");
                        break;
                    }
                    if(Controller.isIsPoolInit()){
                        new Start2DataThread().StartThread();
                        data.put("info", "null");
                    }else{
                        data.put("info", "进程池已关闭，请重新启动进程池再进行操作。");
                    }
                    break;
                case "stopScanner":
                    boolean isStopScanner = !MainServer.isStopScanner();
                    MainServer.setStopScanner(isStopScanner);
                    data.put("info", "盲扫线程目前状态为： " + !isStopScanner);
                    break;
                case "stop":
                    if(Controller.isIsPoolInit()){
                        MainServer.setStopScanner(true);
                        GetStart2.setIsStop(true);
                        Controller.getInstance().shutdownNow();
                        data.put("info", "进程池已进入紧急关闭模式,盲扫线程目前状态为： " + !MainServer.isStopScanner());
                    }else{
                        data.put("info", "进程池已关闭，无需再次关闭。");
                    }
                    break;
                case "ininPool":
                    start2dataPool.setIsOnline(false);
                    if(Controller.isIsPoolInit()){
                        data.put("info", "进程池已初始化成功，无需再次初始化。");
                    }else{
                        Controller.getInstance().ininPool();
                        data.put("info", "null");
                    }
                    //new moe.kcwiki.unpackswf.UnpackSwf().callShell("service tomcat8 restart");
                    break;
                case "shutdown":
                    if(request.getParameter("Authorizations-key") != null && request.getParameter("Authorizations-key").equals(MainServer.getAuthorization_superuser())){
                        System.exit(0);
                    }
                    break;
                case "updateID":
                    //sessionSet.add(request.getParameter("ID"));
                    break;     
                case "takeTask":
                    data.put("info", Controller.getInstance().isTerminated().clone());
                    break;   
            }
        }
    }
    
    try (PrintWriter out = response.getWriter()) {
        out.println(JSON.toJSONString(data));
    }
    data.clear();
    
  }

  @Override
  protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
  {
    processRequest(request,response,"GET");
  }

  @Override
  protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
  {
    processRequest(request,response,"POST");
  }

}
