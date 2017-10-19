/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getstart2data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.JOptionPane;
import moe.kcwiki.init.MainServer;
import moe.kcwiki.tools.RWFile;
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
        urlStr="https://acc.kcwiki.org/start2/archives";
        
        byte[] buf = new byte[1024];  
        
        if((conn1 = GetHttpURLConnection())!=null){
            in = conn1.getInputStream();
            String str = "";  
            while (in.read(buf) != -1) {
                str += new String(buf);
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
                conn.setRequestProperty("Host", "acc.kcwiki.org");  
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
                        System.out.println(obj);
                    }
                    int len=arr.length;
                    System.out.print(LINESEPARATOR);
                    System.out.println(arr[len-2].substring(1, arr[len-2].length()-1));
                    System.out.println(arr[len-1].substring(1, arr[len-1].length()-1));
                    //String oldStart2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[len-2].substring(1, arr[len-2].length()-1));
                    //String newStart2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[len-1].substring(1, arr[len-1].length()-1));
                    
                    for(int id=122; id+1<arr.length; id++) {
                        String str1 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[id].substring(1, arr[len-2].length()-1));
                        String str2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[id+1].substring(1, arr[len-2].length()-1));
                        JSONObject jobj1 = JSON.parseObject(str1);
                        JSONObject jobj2 = JSON.parseObject(str2);
                        String jstr1 = JSON.toJSONString(jobj1);
                        String jstr2 = JSON.toJSONString(jobj2);
                        /*RWFile.writeLog(str1, "E:\\kc\\kc1.json");
                        RWFile.writeLog(str2, "E:\\kc\\kc2.json");*/
                        HashMap<String,Object> data1 = JSON.parseObject(str1,new TypeReference<LinkedHashMap<String, Object>>() {},Feature.SortFeidFastMatch);
                        HashMap<String,Object> data2 = JSON.parseObject(str2,new TypeReference<LinkedHashMap<String, Object>>() {},Feature.SortFeidFastMatch);
                        String jstr3 = JSON.toJSONString(data1);
                        String jstr4 = JSON.toJSONString(data2);
                        JSONObject jobj3 = JSON.parseObject(jstr3);
                        JSONObject jobj4 = JSON.parseObject(jstr4);
                        jstr3 = jobj3.toJSONString();
                        jstr4 = jobj4.toJSONString();
                        RWFile.writeLog(jobj3.toString(), "E:\\kc\\kc1.json");
                        RWFile.writeLog(jobj4.toString(), "E:\\kc\\kc2.json");
                        if(jstr3.equals(jstr4)){
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        }  
                        if(jobj3.equals(jobj4)){
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                            //System.out.println(jobj3.toString()+"\t equals: "+jobj4.toString());
                        }
                        /*if(jsonEquals.jsonEquals(jstr3, jstr4)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        }*/
                        if(jstr1.equals(jstr2)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        } 
                        if(jobj1.equals(jobj2)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        } 
                        if(data1.equals(data2)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        } 
                        JsonParser parser = new JsonParser();  
                        JsonObject obj = (JsonObject) parser.parse(str1);  
                        JsonParser parser1 = new JsonParser();  
                        JsonObject obj1 = (JsonObject) parser1.parse(str2);  
                        if(obj.equals(obj1)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        } 
                        Gson gson1 = new GsonBuilder().create();//or new Gson()   
                        JsonElement e1 = gson1.toJsonTree(str1);//or new Gson()   
                        Gson gson2 = new GsonBuilder().create();  
                        JsonElement e2 = gson2.toJsonTree(str2); 
                        if(e1.equals(e2)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        }  
                        JsonElement e3 = new JsonPrimitive(str1);  
                        JsonElement e4 = new JsonPrimitive(str2);  
                        if(e3.equals(e4)) {
                            System.out.println(arr[id]+"\t equals: "+arr[id+1]);
                        } else {
                            System.out.println(arr[id]+"\t not equals: "+arr[id+1]);
                        } 
                        
                        //System.out.println(new moe.kcwiki.json.JSONObject(str1).similar(str2));
                        
                        System.out.println();
                    }
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
