/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import moe.kcwiki.init.MainServer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author iTeam_VEP
 */
public class download extends HttpServlet {
    private StringBuilder sb = null;
    public final static String LINESEPARATOR = System.getProperty("line.separator", "\n");
    
    protected void processRequest(HttpServletRequest request,HttpServletResponse response,String method) throws ServletException,IOException
  {
    response.setContentType("text/xml");
    response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8"); 
            
    HashMap<String, Object> data = new  HashMap<>();

        String parameter=request.getParameter("download");
        //MainServer.setZipFolder(Long.parseLong("123411144"));
        Long date = MainServer.getZipFolder() ;
        if(parameter == null || !parameter.equals("true")){
            data.put("status", "error");
            data.put("data", "请附带请求参数。");
        } else {
            sb = new StringBuilder();
            if(date == null ) {
                this.addString("<!DOCTYPE html><html><body>");
                this.addString("文件队列仍未下载完毕，请等待后台下载进程执行。");
                this.addString("</body></html>");
            }else{
                File[] fl = new File(MainServer.getPublishFolder()+File.separator+MainServer.getZipFolder()).listFiles();
                String Publishing = "/KcWikiOnline/custom/Publishing/"+ date +"/";
                this.addString("<!DOCTYPE html><html><body>");
                for(File zip:fl) {
                    String filename = zip.getName();
                    String url = null;
                    
                    if(filename.contains("sourcefile"))
                        url = "<a href=\""+Publishing+filename+"\" >" +"完整的拆包文件（包括差分音频），请配合kcre进行拆包工作。" +"</a>";
                    if(filename.contains("editorialfile"))
                        url = "<a href=\""+Publishing+filename+"\" >" +"内含lua、拆好的地图、舰娘&装备立绘。（娇喘更新了的话）"+"</a>";
                    if(filename.contains("logfile"))
                        url = "<a href=\""+Publishing+filename+"\">" +"日志文件，仅供技术组分析用。"+"</a>";
                    url += LINESEPARATOR;
                    this.addString(url);
                }
                this.addString("</body></html>");
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
        Logger.getLogger(download.class.getName()).log(Level.SEVERE, null, e);
        Logger.getLogger(download.class.getName()).log(Level.WARNING, "客户端异常关闭" , e);
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
