/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getstart2data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import moe.kcwiki.tools.RWFile;
import static moe.kcwiki.tools.constant.LINESEPARATOR;
import com.flipkart.zjsonpatch.*;
import com.fasterxml.jackson.databind.JsonNode;
/*import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonToken;*/
import com.fasterxml.jackson.databind.ObjectMapper;


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
                HashMap<String,String> sameMap = new LinkedHashMap<>();
                HashMap<String,String> diffMap = new LinkedHashMap<>();
                try {
                    String[] arr= getStart2List();
                    for(String obj:arr){
                        System.out.println(obj);
                    }
                    int len=arr.length;
                    System.out.print(LINESEPARATOR);
                    System.out.println(arr[len-2].substring(1, arr[len-2].length()-1));
                    System.out.println(arr[len-1].substring(1, arr[len-1].length()-1));
                    System.out.print(LINESEPARATOR);
                    //String oldStart2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[len-2].substring(1, arr[len-2].length()-1));
                    //String newStart2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+arr[len-1].substring(1, arr[len-1].length()-1));
                    ObjectMapper jackson = new ObjectMapper(); 
                    for(int id=120; id+1<arr.length; id++) {
                        String src1 = arr[id];
                        String src2 = arr[id+1];
                        src1 = src1.substring(1, src1.length()-1);
                        src2 = src2.substring(1, src2.length()-1);
                        String str1 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+src1);
                        String str2 = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"+src2);
                        JSONObject jobj1 = JSON.parseObject(str1);
                        JSONObject jobj2 = JSON.parseObject(str2);
                        /*String jstr1 = JSON.toJSONString(jobj1);
                        String jstr2 = JSON.toJSONString(jobj2);*/
                        /*RWFile.writeLog(str1, "E:\\kc\\str1.json");
                        RWFile.writeLog(str2, "E:\\kc\\str2.json");*/
                        HashMap<String,Object> data1 = JSON.parseObject(str1,new TypeReference<LinkedHashMap<String, Object>>() {},Feature.SortFeidFastMatch);
                        HashMap<String,Object> data2 = JSON.parseObject(str2,new TypeReference<LinkedHashMap<String, Object>>() {},Feature.SortFeidFastMatch);
                        String jstr1 = JSON.toJSONString(data1);
                        String jstr2 = JSON.toJSONString(data2);
                        JSONObject jobj3 = JSON.parseObject(jstr1);
                        JSONObject jobj4 = JSON.parseObject(jstr2);
                        jstr1 = jobj3.toJSONString();
                        jstr1 = jobj4.toJSONString();
                        /*RWFile.writeLog(jstr1.toString(), "E:\\kc\\jstr1.json");
                        RWFile.writeLog(jstr2.toString(), "E:\\kc\\jstr2.json");*/
                        /*if(jstr3.equals(jstr4)){
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } 
                        if(data1.equals(data2)) {
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        }
                        if(jstr1.equals(jstr2)) {
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } */
                         /*JsonParser parser = new JsonParser();  
                        JsonObject obj = (JsonObject) parser.parse(jstr3);  
                        JsonParser parser1 = new JsonParser();  
                        JsonObject obj1 = (JsonObject) parser1.parse(jstr4);  
                        if(obj.equals(obj1)) {
                            //Equals
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } 
                        Gson gson1 = new GsonBuilder().create();//or new Gson()   
                        JsonElement e1 = gson1.toJsonTree(jstr3);//or new Gson()   
                        Gson gson2 = new GsonBuilder().create();  
                        JsonElement e2 = gson2.toJsonTree(jstr4); 
                        if(e1.equals(e2)) {
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        }  
                        JsonElement e3 = new JsonPrimitive(jstr3);  
                        JsonElement e4 = new JsonPrimitive(jstr4);  
                        if(e3.equals(e4)) {
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } */
                        if(jobj1.equals(jobj2)){
                            System.out.println(src1+"\t equals: "+src2);
                            //System.out.println(jobj3.toString()+"\t equals: "+jobj4.toString());
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        }  
                        
                        JsonNode beforeNode = jackson.readTree(str1); 
                        JsonNode afterNode = jackson.readTree(str2); 
                        JsonNode patchNode = JsonDiff.asJson(beforeNode, afterNode); 
                        String diff = patchNode.toString();
                        JSONArray jarr = JSON.parseArray(diff);
                        
                        /*if(diff.equals("[]")){
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } 
                        EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy().clone();
                        JsonNode patch = JsonDiff.asJson(afterNode, beforeNode, flags);
                        patch.toString();*/
                        RWFile.writeLog(diff, "E:\\kc\\patch.json");
                        if(patchNode.size() == 0){
                            System.out.println(src1+"\t equals: "+src2);
                            sameMap.put(src1, src2);
                        } else {
                            for(Object key:jarr) {
                                JSONObject obj = (JSONObject) key;
                                System.out.println(src1+"\t not equals: "+src2);
                            }
                            System.out.println(src1+"\t not equals: "+src2);
                            diffMap.put(src1, src2);
                        } 
                        /*
                        JsonNode beforeNode1 = jackson.readTree(jstr3); 
                        JsonNode afterNode1 = jackson.readTree(jstr4); 
                        JsonNode patchNode1 = JsonDiff.asJson(beforeNode1, afterNode1); 
                        String diff1 = patchNode1.toString();
                        if(patchNode1.size() == 0){
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        } 
                        
                        flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy().clone();
                        patch = JsonDiff.asJson(afterNode1, beforeNode1, flags);
                        patch.toString();
                        if(patch.size() == 0){
                            System.out.println(src1+"\t equals: "+src2);
                        } else {
                            System.out.println(src1+"\t not equals: "+src2);
                        }*/
                        System.out.println();
                    }
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    
                    System.out.println("总数据量为："+arr.length);
                    System.out.println("开始输出相同的项目");
                        for(String str : sameMap.keySet()){
                            System.out.println(str + " : " + sameMap.get(str));
                        }
                        
                    System.out.println("开始输出不同的项目");
                        for(String str : diffMap.keySet()){
                            System.out.println(str + " : " + diffMap.get(str));
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
