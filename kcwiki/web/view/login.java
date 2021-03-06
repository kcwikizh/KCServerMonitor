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

public class login extends HttpServlet{
    
    /**
     * @return the userList
     */
    public static HashMap<String, Object> getUserList() {
        return userList;
    }
    
    private static HashMap<String, Object> userList;
    
  protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
    
    String username=request.getParameter("username");
    String password=request.getParameter("password");
    
    HashMap<String, Object> data = new  HashMap<>();
    HttpSession session = request.getSession(true); 
    
    if( getUserList().containsKey(username)){
        if( getUserList().get(username).equals(password)){
            session.setAttribute("hsaLogin", "true");
            data.put("status", "success");
            String host = request.getServerName();
            if(host.equals("127.0.0.1")){
                data.put("info", request.getScheme()+"://"+"x.kcwiki.org/admin.html");
            } else if (host.contains("kcwiki")){
                data.put("info", request.getScheme()+"://"+request.getServerName()+"/admin.html"); 
            } else if (host.contains("45.")) {
                data.put("info", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/KcWikiOnline/admin.html"); 
            }
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
