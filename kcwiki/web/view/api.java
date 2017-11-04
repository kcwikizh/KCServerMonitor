/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.view;

import moe.kcwiki.web.api.monitor;
import moe.kcwiki.web.api.download;
import moe.kcwiki.web.api.unpackswf;
import moe.kcwiki.web.api.whatsnew;
import moe.kcwiki.web.api.jsonpatch;
import moe.kcwiki.web.api.srcdiff;
import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author iTeam_VEP
 */
public class api extends HttpServlet {
    private StringBuilder sb = null;
    public final static String LINESEPARATOR = System.getProperty("line.separator", "\n");
    
    protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
            
    HashMap<String, Object> data = new  HashMap<>();
    HttpSession session = request.getSession(false); 
        String parameter = request.getParameter("query");
        String user = request.getParameter("user");
        String token = request.getParameter("token");
        //MainServer.setZipFolder(Long.parseLong("123411144"));
        //Long date = MainServer.getZipFolder() ;
        if(parameter == null ){
            data.put("status", "error");
            data.put("data", "请附带请求参数。");
        } else {
            sb = new StringBuilder();
            switch(parameter){
                default:
                    data.put("status", "failure");
                    data.put("data", "请求参数有误。");
                    break;
                case "download":
                    sb.append(new download().getData());
                    break;
                case "monitor":
                    if(session==null || session.getAttribute("hsaLogin")!="true"){
                        data.put("status", "failure");
                        data.put("data", "请登录后再进行此操作。");
                        break;
                    }
                    sb.append(new monitor().getData());
                    break; 
                case "whatsnew":
                    sb.append(new whatsnew().getData());
                    break; 
                case "jsonpatch":
                    sb.append(new jsonpatch().getData());
                    break; 
                case "unpackswf":
                    if(session==null || session.getAttribute("hsaLogin")!="true"){
                        data.put("status", "failure");
                        data.put("data", "请登录后再进行此操作。");
                        break;
                    }
                    String src = request.getParameter("src");
                    String dest = request.getParameter("dest");
                    String clr = request.getParameter("clearbeforeunpack");
                    if(clr != null)
                        new unpackswf().clear(clr);
                    sb.append(new unpackswf().getData(src,dest));
                    break;
                case "srcdiff":
                    sb.append(new srcdiff().getData());
                    break;
                case "admin":
                    break; 
                case "null":
                    sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort());
            }

        }
        
    try (PrintWriter out = response.getWriter()) {
        if(data.isEmpty()){
            out.println(sb.toString());
        } else {
            out.println(JSON.toJSONString(data));
            data.clear();
        }
    } catch (Exception e) {
        e.printStackTrace();
        Logger.getLogger(api.class.getName()).log(Level.SEVERE, null, e);
        Logger.getLogger(api.class.getName()).log(Level.WARNING, "客户端异常关闭" , e);
    }
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
  
  private void addString(String str) {
      sb.append(str + LINESEPARATOR + "</br>");
  }
}
