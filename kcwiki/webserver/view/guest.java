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
import javax.servlet.*;
import javax.servlet.http.*;

public class guest extends HttpServlet{
    
  protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
    
    String test=request.getParameter("test");

    HashMap<String, Object> data = new  HashMap<>();
    data.put("status", "success");
    data.put("method", method);
    data.put("test", test);
    data.put("session", request.getSession().getId());
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
