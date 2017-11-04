
package moe.kcwiki.monitor.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.UnknownHostException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import moe.kcwiki.handler.massage.msgPublish;
        
public class DlCore {
            
    
    public synchronized boolean download(String furl,String fPath,String proxyhost,int proxyport) {
        
        try{
            final RandomAccessFile file = new RandomAccessFile(fPath, "rw");
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            
            HttpGet get = new HttpGet(furl);
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
            //client.getCredentialsProvider().setCredentials(new AuthScope("127.0.0.1",8888), new UsernamePasswordCredentials("", ""));
            
            /*
            if(!MainServer.isDebugMode()){
                HttpHost proxy=new HttpHost(MainServer.getProxyhost(), MainServer.getProxyhost());
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy); 
            }	
            */
            client.getParams().setParameter("http.socket.timeout", 30*1000); 

            HttpResponse response = client.execute(get);
        
            if (300 >= response.getStatusLine().getStatusCode()) {
                HttpEntity en = response.getEntity();
                try (InputStream in = en.getContent()) {
                    byte[] by = new byte[512];
                    int len = -1;

                    // 开始下载
                    while (-1 != (len = in.read(by))) {
                        file.write(by, 0, len);
                    }
                    file.close();
                }
                client.getConnectionManager().shutdown();
            }
            return true;
        }catch(UnknownHostException e){
            msgPublish.msgPublisher("getmodifieddata"+"下载端口出错,请检测网络连接。",0,-1);
            return false;
        }catch(ConnectException e){
            msgPublish.msgPublisher("getmodifieddata"+"下载文件连接超时。",0,-1);
            return false;
        }catch(IOException e){
            msgPublish.msgPublisher("getmodifieddata"+"下载文件时发生IOException错误。",0,-1);  
            return false;
        }  
    }
    
}
