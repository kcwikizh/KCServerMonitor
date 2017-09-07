/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import static java.lang.Thread.sleep;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import moe.kcwiki.getmodifieddata.DlCore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;

/**
 *
 * @author iTeam_VEP
 */
public class GetPresetData {
    HttpURLConnection conn=null;
    
    public boolean download(String urlStr,String fpath,Long file,String proxyhost,int proxyport) throws IOException, InterruptedException, Exception {  
        String formatCode; 
        StringBuilder buffer;
        try {  

            URL url = new URL(urlStr);  
            
            conn = (HttpURLConnection) url.openConnection();  
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");  
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
            //conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");       
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");  
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");       
            conn.setIfModifiedSince(file);
            
            int responseCode = conn.getResponseCode();
            if(responseCode==500){
                conn.disconnect();
                return false;
            }
            if(responseCode==304){
                conn.disconnect();
                return false;
            }
            if(responseCode==200){
                formatCode=conn.getContentEncoding();
                BufferedReader reader;
                if(formatCode!=null){
                    reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
                }else{
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                }
                String line=null;
                String str;
                buffer=new StringBuilder();

                try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fpath)), "UTF-8"))) {
                    while ((str = reader.readLine()) != null){
                        line=line+str+constant.LINESEPARATOR;
                        buffer.append(str).append(constant.LINESEPARATOR);
                        eBfw.write(str+constant.LINESEPARATOR);
                    }
                }
                //System.out.print(line);
                //System.out.print(buffer.toString());    
            }
            conn.disconnect();
            return true;
        } catch (MalformedURLException e) {  
            conn.disconnect();
            msgPublish.msgPublisher("预设文件下载地址无法访问。",0,-1);
            return false;
        } catch (ProtocolException e) {  
            conn.disconnect();
            msgPublish.msgPublisher("接收到的数据长度超过最大长度（65536）。",0,-1);
            return false;
        } catch (ConnectException e) {  
            conn.disconnect();
            msgPublish.msgPublisher("下载文件超时。",0,-1);
            return false;
        } catch (IOException e) {  
            conn.disconnect();
            msgPublish.msgPublisher("文件下载被意外终止。",0,-1);
            return false;
        }      
    }
    
    /*
    public static void main(String args[]){
        try {
            String furl="http://media.kcwiki.moe/kctoolssyn/161019.txt";
            String fPath="E:\\flasm 16win\\161019.txt";
            String proxyhost="127.0.0.1";
            int proxyport=8888;
            new GetPresetData().download(furl,fPath,(long)0,proxyhost,proxyport);
        } catch (Exception ex) {
            Logger.getLogger(GetStart2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
}
