/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.monitor.file;

import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.tools.daemon.CatchError;

import moe.kcwiki.initializer.GetModifiedDataThread;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static moe.kcwiki.initializer.MainServer.isStopScanner;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.handler.thread.modifieddataPool;
import moe.kcwiki.tools.constant.constant;
import static moe.kcwiki.tools.constant.constant.FILESEPARATOR;

/**
 *
 * @author VEP
 */
public class GetLastModifiedData {
    //private final java.util.ArrayList<String>  addressList;
    //private final java.util.ArrayList<String>  dataList;
    private final String proxyhost;
    private final int proxyport;
    private static JSONArray modifidedData;
    private final String rootFolder = MainServer.getTempFolder() + File.separator+"newSlotItem";
    //String[] serverlistaddress= new String[]{"203.104.209.71", "203.104.209.87", "125.6.184.16", "125.6.187.205", "125.6.187.229","125.6.187.253", "125.6.188.25", "203.104.248.135", "125.6.189.7", "125.6.189.39","125.6.189.71", "125.6.189.103", "125.6.189.135", "125.6.189.167", "125.6.189.215","125.6.189.247", "203.104.209.23", "203.104.209.39", "203.104.209.55", "203.104.209.102"};
    private static List<String> serverlistaddress = MainServer.getWorldList();
    private static String[] serverlistname = new String[]{"横须贺镇守府","呉镇守府","佐世保镇守府","舞鹤镇守府","大凑警备府","トラック泊地","リンガ泊地","ラバウル基地","ショートランド泊地","ブイン基地","タウイタウイ泊地","パラオ泊地","ブルネイ泊地","単冠湾泊地","幌筵泊地","宿毛湾泊地","鹿屋基地","岩川基地","佐伯湾泊地","柱岛泊地"};
    private static String serveraddress;
    private static String servername = "203.104.209.102";
    private static int servernum=0;
    

    public GetLastModifiedData() throws UnsupportedEncodingException, IOException {
        this.proxyhost=moe.kcwiki.initializer.MainServer.getProxyhost();
        this.proxyport=moe.kcwiki.initializer.MainServer.getProxyport();
        serverAddress();
    }
    
    public boolean doMonitor() {
        GetModifiedDataThread.addJob();
        String line;
        String str = "";
        String ipFile=MainServer.getDataFolder()+File.separator+"filedata-lite.json";
        if(!new File(ipFile).exists()){
            msgPublish.msgPublisher("找不到"+serveraddress+"的扫描文件，请向技术组索取。",0,-1);
            return false;
        }
        try (BufferedReader Ibfr = new BufferedReader(new InputStreamReader(new FileInputStream(ipFile), "UTF-8"))) {
            while ((line=Ibfr.readLine())!=null) {
                str=str+line;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        modifidedData=JSON.parseArray(str);
        
        final int taskID = modifieddataPool.getTaskNum();
            modifieddataPool.addTask(() -> {
            int countno=1;
            while(!isStopScanner()){
                msgPublish.msgPublisher("开始第"+countno+"轮文件扫描。",0,0);
                if(getNewData(modifidedData)) {
                    break;
                }
                countno++;
                try {
                    if(isStopScanner()){
                        break;
                    }
                    msgPublish.msgPublisher("文件扫描线程进入等待阶段",0,0);
                    sleep(3*60*1000);
                } catch (InterruptedException ex) {
                    //msgPublish.msgPublisher("moe.kcwiki.getmodifieddata.GetLastModifiedData-doMonitor-InterruptedException",0,0);
                    Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
                    msgPublish.msgPublisher("文件扫描线程已强制退出。",0,0);
                    return taskID;
                }
            }
            msgPublish.msgPublisher("文件扫描线程已停止。",0,0);
            return taskID;
        },taskID,"moe.kcwiki.getmodifieddata-GetLastModifiedData-doMonitor");
        return true;
    }
    
    /*
    public boolean getterSplitter(String serveraddress){
        java.util.ArrayList<Object> blocklist=new java.util.ArrayList();

        JSONArray Data=modifidedData;
        int blockSize= (int) Math.ceil(Data.size()/(double) blockCount);

        int countno=0;
        int sum=0;
        int blockno=1;
        for(Object shipid:Data){
            blocklist.add(shipid);
            countno++;
            sum++;
            if(countno==blockSize){
                getNewData(blocklist.clone(),serveraddress);
                blocklist.clear();
                countno=0;
                maxCount = blockno;
                blockno++;
                continue;
            }
            if(sum==Data.size()){
                getNewData(blocklist.clone(),serveraddress);
                blocklist.clear();
                maxCount=blockno;
                break;
            }
        }
        return true;
    }
    */

    public boolean getNewData(JSONArray shipdata) {
         //msgPublish.msgPublisher("shipdata: "+JSON.toJSONString(shipdata),0,0); 
         boolean isFinish = false;
                Iterator iterator=shipdata.iterator();
                while(iterator.hasNext()){
                    if(isStopScanner()){
                        break;
                    }
                    JSONObject object=(JSONObject)iterator.next();
                        int count=0;
                        while(count<3){
                            String url;
                            /*if(!MainServer.isDebugMode()){
                                url="http://"+serveraddress+"/kcs/";
                            }else{
                                url=MainServer.getKcwikiServerAddress();
                            }*/
                            url=MainServer.getKcwikiServerAddress();
                            //url=MainServer.getKcwikiServerAddress();
                            String path = object.getString("path");
                            if (getMDD(url+path,Long.parseLong(object.getString("timestamp")),-1,serveraddress)) {
                                if(path.toLowerCase().contains("core")){
                                   isFinish = true; 
                                }
                                break;
                            }
                            count++;
                        }
                    
                    //msgPublish.msgPublisher("ID:"+shipid+"扫描完毕。",0);
                }

        return isFinish;
    }

    public boolean getMDD(String URL,Long flie,int nameNo,String serveraddress){
        int i=URL.lastIndexOf("/"); //取得子串的初始位置
        String filename;
        if(nameNo==-1){
            filename=URL.substring(i+1,URL.length());
        }else{
            filename=moe.kcwiki.swfunpacker.Server.shipvoicerule.get(String.valueOf(nameNo));
        }
        String sourcepath=URL.substring(0, i);
        for(int count=0;count<3;count++){
            i=sourcepath.indexOf("/");
            sourcepath=sourcepath.substring(i+1, sourcepath.length());  
        }
        String filepath=MainServer.getDownloadFolder()+File.separator+sourcepath;
        
        Long one=flie;
        URL serverUrl;
        HttpURLConnection urlcon ;
        
        try {
            serverUrl = new URL(URL);
            /*if(!MainServer.isDebugMode()){
                Proxy httpproxy = MainServer.getHttpproxy();
                urlcon = (HttpURLConnection) serverUrl.openConnection(httpproxy);
            }else{
                urlcon = (HttpURLConnection) serverUrl.openConnection();
            }*/
            urlcon = (HttpURLConnection) serverUrl.openConnection();
        } catch (IOException ex) {
            msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetLastModifiedData\t出现IOException错误。",0,-1); 
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "15000");
               
        urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
        try {
            urlcon.setRequestMethod("HEAD");
        } catch (ProtocolException ex) {
            msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetLastModifiedData\t出现ProtocolException错误。",0,-1); 
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        urlcon.setIfModifiedSince(one);
        try{
            String message = urlcon.getHeaderField(0);
            if(message==null){
                msgPublish.msgPublisher("getMDD无法获取服务器文件信息，请检查网络。",0,-1);  
                urlcon.disconnect();
                //serverAddress();
                return false;
            }
            if(message.contains("HTTP/1.1 404 Not Found")){
                urlcon.disconnect();
                return false;
            }
            if(urlcon.getLastModified()==0){
                urlcon.disconnect();
                sleep(5000);
                //serverAddress();
                return false;
            }
            if(message.contains("HTTP/1.1 403")){
                urlcon.disconnect();
                //serverAddress();
                sleep(5000);
            }
            if(message.contains("HTTP/1.1 500")){
                msgPublish.msgPublisher(serveraddress+"\t出现500错误，请降低连接数。",0,-1); 
                urlcon.disconnect();
            }
            if(message.contains("HTTP/1.1 503 Too many open connections")){
                msgPublish.msgPublisher(serveraddress+"\t出现503错误，请降低连接数。",0,-1); 
                urlcon.disconnect();
            }
            if(message.contains("HTTP/1.1 304 Not Modified")){
                urlcon.disconnect();
            }
            if(message.contains("HTTP/1.1 200 OK")){
                urlcon.disconnect();
                if  (!(new File(filepath).exists())||!(new File(filepath).isDirectory())) {
                    new File(filepath) .mkdirs();  
                }
                if(new DlCore().download(URL, filepath+FILESEPARATOR+filename,proxyhost,proxyport)){
                    msgPublish.msgPublisher(filename+"\t下载完成",0,0);
                    if(URL.contains(".swf")){
                        new moe.kcwiki.swfunpacker.Controller().Analysis(filename,filepath,sourcepath);   
                    }
                    return true;
                }
            }
            return false;
        }catch(UnsupportedOperationException | NullPointerException e){
            CatchError.WriteError("modifiedCheck-getMDD模块读写时发生IOException错误"+constant.LINESEPARATOR+Arrays.toString(e.getStackTrace()));
            msgPublish.msgPublisher("modifiedCheck-getMDD模块读写时发生IOException错误。",0,-1);  
            return false;
        }catch(SocketTimeoutException e){
            CatchError.WriteError("modifiedCheck-getMDD模块读写时发生SocketTimeoutException错误"+constant.LINESEPARATOR+Arrays.toString(e.getStackTrace()));
            msgPublish.msgPublisher("modifiedCheck-getMDD模块读写时发生SocketTimeoutException错误。",0,-1);  
            return false;
        } catch (InterruptedException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            CatchError.WriteError("modifiedCheck-getMDD模块读写时发生InterruptedException错误"+constant.LINESEPARATOR+Arrays.toString(ex.getStackTrace()));
            msgPublish.msgPublisher("modifiedCheck-getMDD模块读写时发生InterruptedException错误。",0,-1); 
            return false;
        } catch (IOException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            CatchError.WriteError("modifiedCheck-getMDD模块读写时发生IOException错误"+constant.LINESEPARATOR+Arrays.toString(ex.getStackTrace()));
            msgPublish.msgPublisher("modifiedCheck-getMDD模块读写时发生IOException错误。",0,-1); 
            return false;
        } catch (Exception ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            CatchError.WriteError("modifiedCheck-getMDD模块读写时发生Exception错误"+constant.LINESEPARATOR+Arrays.toString(ex.getStackTrace()));
            msgPublish.msgPublisher("modifiedCheck-getMDD模块读写时发生Exception错误。",0,-1); 
            return false;
        }
    }
    
    
    
    private void serverAddress(){
        serveraddress=serverlistaddress.get(servernum);
        servername=serverlistname[servernum]+"-"+serverlistaddress.get(servernum);
        servernum++;
        if(servernum==serverlistaddress.size()){
            servernum=0;
        }
    }
    
}
