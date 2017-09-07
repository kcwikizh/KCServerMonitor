/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getstart2data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
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
import javax.swing.JOptionPane;
import moe.kcwiki.init.MainServer;
import static moe.kcwiki.tools.constant.LINESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class Start2List {
    private static String urlStr;   
    private static HttpURLConnection conn = null; 
    private static String []  Start2Arr;  
    
    public static String[] getStart2List() throws InterruptedException, IOException, Exception {  
        HttpURLConnection conn1;  
        InputStream in;  
        urlStr="http://api.kcwiki.moe/start2/archives";
        
        byte[] buf = new byte[1024];  
        
        if((conn1 = GetHttpURLConnection())!=null){
            in = conn1.getInputStream();
            String str = "";  
            while (in.read(buf) != -1) {
                str = new String(buf);
                //System.out.print(str); 
            }
            try{
                sleep(3*1000);
            } catch (InterruptedException ex) {
                return Start2Arr;
            }
            conn1.disconnect();
            conn.disconnect();
            return Start2Arr;
        }
        return null;
    }  
  
    private static HttpURLConnection GetHttpURLConnection() throws IOException, InterruptedException, Exception {  
 
        try {  
            
            String formatCode;
            while(true){
                URL url = new URL(urlStr);  
                /*
                if(MainServer.getProxyhost().equals("")){
                    conn = (HttpURLConnection) url.openConnection();  
                }else{
                    Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(MainServer.getProxyhost(), MainServer.getProxyport()));
                    conn = (HttpURLConnection) url.openConnection(proxy);  
                }
                */
                conn = (HttpURLConnection) url.openConnection();  
                conn.setDoOutput(true); 
                conn.setRequestMethod("GET");  
                conn.setRequestProperty("Host", "api.kcwiki.moe");  
                conn.setRequestProperty("connection", "keep-alive");  
                conn.setRequestProperty("Upgrade-Insecure-Requests", "1"); 
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
                conn.setRequestProperty("DNT", "1");       
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");  
                conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");      

                try{
                    conn.connect();
                }catch(UnknownHostException e){
                    return null;
                }catch(IOException e){
                    return null;
                }
                
                
                int responseCode = conn.getResponseCode();  
                if (responseCode <= 300) {  
                    formatCode=conn.getContentEncoding();
                    break;
                }else{
                    return null;
                }
                
            }  
            
            
            BufferedReader reader;
            if(formatCode!=null){
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
            }else{
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            }
            
            String line = "";
            String str;
            while ((str = reader.readLine()) != null){
                line=line+str;       
            }
            line = line.substring(1, line.length()-1);
            Start2Arr= line.split(",");
           
        } catch (MalformedURLException e) {  
            return null;
        } catch (ProtocolException e) {  
            return null;
        } catch (IOException e) {  
            return null;
        }  
        return conn;     
    }
    
    
    public static void main(String[] args) throws IOException, Exception{
        new Runnable() {  
            @Override
            public void run() {  
                
                try {
                    String[] arr= getStart2List();
                    for(String obj:arr){
                        System.out.print(obj);
                    }
                    int len=arr.length;
                    System.out.print(LINESEPARATOR);
                    System.out.println(arr[len-2].substring(1, arr[len-2].length()-1));
                    System.out.println(arr[len-1].substring(1, arr[len-1].length()-1));
                    new Start2Api().GetStart2Api("http://api.kcwiki.moe/start2/"+arr[len-2].substring(1, arr[len-2].length()-1));
                    new Start2Api().GetStart2Api("http://api.kcwiki.moe/start2/"+arr[len-1].substring(1, arr[len-1].length()-1));
                } catch (IOException ex) {
                    Logger.getLogger(Start2List.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                } catch (Exception ex) {
                    Logger.getLogger(Start2List.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                } 
            }  
        }.run();
    }
}
