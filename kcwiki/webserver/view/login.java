/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.*;




import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.*;
import moe.kcwiki.init.MainCore;
import moe.kcwiki.init.MainServer;
import moe.kcwiki.threadpool.Controller;

public class login extends HttpServlet{
    
    /**
     * @return the userList
     */
    public static HashMap<String, Object> getUserList() {
        return userList;
    }
    
    private static HashMap<String, Object> userList;
    private static boolean isActivative = false;
    
  protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
    
    /*if(!MainServer.isInit()){
        MainServer.init(false); 
        Controller.ininPool();
    }
    if(!isActivative){
            setUserList(moe.kcwiki.webserver.util.userList.getUserList());
        isActivative = true ;
    }*/
    
    String username=request.getParameter("username");
    String password=request.getParameter("password");
    
    HashMap<String, Object> data = new  HashMap<>();
    HttpSession session = request.getSession(true); 
    
    if( getUserList().containsKey(username)){
        if( getUserList().get(username).equals(password)){
            session.setAttribute("hsaLogin", "true");
            data.put("status", "success");
            data.put("info", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/KcWikiOnline/admin.html");
            //data.put("info", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/admin.html");
            //data.put("info", request.getScheme()+"://"+"x.kcwiki.org/admin.html");
            
        }else{
            data.put("status", "error");
            data.put("info", "账号或密码错误");
        }
    }else{
        data.put("status", "error");
        data.put("info", "账号或密码错误");
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

    /**
     * @param aUserList the userList to set
     */
    public static void setUserList(HashMap<String, Object> aUserList) {
        userList = aUserList;
    }

}
