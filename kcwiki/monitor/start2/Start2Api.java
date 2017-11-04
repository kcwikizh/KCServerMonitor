/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.monitor.start2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author x5171
 */
public class Start2Api {
    private String urlStr;  
    private String cookie;  
    private HttpURLConnection conn = null; 
    private String Start2Json;  
    
    public String GetStart2Api(String url)  {  
        try {
            HttpURLConnection conn1;
            InputStream in;
            this.urlStr=url;
            
            byte[] buf = new byte[1024];
            
            if((conn1 = this.GetHttpURLConnection())!=null){
                in = conn1.getInputStream();
                String str = "";
                while (in.read(buf) != -1) {
                    str = new String(buf);
                    //System.out.print(str);
                }
                conn1.disconnect();
                conn.disconnect();
                return Start2Json;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(Start2Api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Start2Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }  
  
    private HttpURLConnection GetHttpURLConnection() throws IOException, InterruptedException, Exception {  
 
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
                conn.setRequestProperty("Host", url.getHost()); 
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
                
                // normally, 3xx is redirect
                boolean redirect = false;
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                            || responseCode == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
                }


                if (redirect) {
                    // get redirect url from "location" header field
                    String newUrl = conn.getHeaderField("Location");
                    // get the cookie if need, for login
                    String cookies = conn.getHeaderField("Set-Cookie");
                    // open the new connnection again
                    conn = (HttpURLConnection) new URL(newUrl).openConnection();
                    conn.setRequestProperty("Cookie", cookies);
                    conn.setRequestProperty("connection", "keep-alive");  
                    conn.setRequestProperty("Upgrade-Insecure-Requests", "1"); 
                    conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
                    conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
                    conn.setRequestProperty("DNT", "1");       
                    conn.setRequestProperty("Accept-Encoding", "gzip, deflate");  
                    conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8"); 
                }
                conn.connect();
                
                responseCode = conn.getResponseCode();  
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
            Start2Json=line;
           
        } catch (MalformedURLException e) {  
            return null;
        } catch (ProtocolException e) {  
            return null;
        } catch (IOException e) {  
            return null;
        }  
        return conn;     
    }
}
