/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getmodifieddata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.getstart2data.Encoder;

import moe.kcwiki.init.MainServer;
import static moe.kcwiki.init.MainServer.isStopScanner;
import moe.kcwiki.unpackswf.ImgPacker;
import moe.kcwiki.unpackswf.Server;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.threadpool.getUnkownSlotitemPool.*;
import moe.kcwiki.tools.constant;

/**
 *
 * @author iTeam_VEP
 */
public class GetUnknowSlotitem {
    private final String slotitemNo;
    private final String localpath;
    private final String localoldstart2;
    private final String proxyhost;
    private final int proxyport;
    private static String serveraddress;
    private static String servername;
    private static int servernum=0;
    public static LinkedHashMap<String, String> unknowShipList = new LinkedHashMap<>(); 
    private final java.util.ArrayList<String>  dataList;
    String[] serverlistaddress= new String[]{"203.104.209.71", "203.104.209.87", "125.6.184.16", "125.6.187.205", "125.6.187.229","125.6.187.253", "125.6.188.25", "203.104.248.135", "125.6.189.7", "125.6.189.39","125.6.189.71", "125.6.189.103", "125.6.189.135", "125.6.189.167", "125.6.189.215","125.6.189.247", "203.104.209.23", "203.104.209.39", "203.104.209.55", "203.104.209.102"};
    String[] serverlistname=new String[]{"横须贺镇守府","呉镇守府","佐世保镇守府","舞鹤镇守府","大凑警备府","トラック泊地","リンガ泊地","ラバウル基地","ショートランド泊地","ブイン基地","タウイタウイ泊地","パラオ泊地","ブルネイ泊地","単冠湾泊地","幌筵泊地","宿毛湾泊地","鹿屋基地","岩川基地","佐伯湾泊地","柱岛泊地"};
        
    public GetUnknowSlotitem(){
        this.localpath = moe.kcwiki.init.MainServer.getLocalpath();
        this.localoldstart2 = moe.kcwiki.init.MainServer.getLocaloldstart2data();
        this.proxyhost = moe.kcwiki.init.MainServer.getProxyhost();
        this.proxyport = moe.kcwiki.init.MainServer.getProxyport();
        slotitemNo = moe.kcwiki.init.MainServer.getSlotitemno();
        dataList = new  java.util.ArrayList<>();
        serverAddress();
    }
    
    
    /*
    public GetUnknowSlotitem(){
        this.localpath = "L:\\NetBeans\\NetBeansProjects\\moe kcwiki stdunpacktools\\build";
        this.localoldstart2 = MainServer.getDataPath()+File.separator+"oldstart2.json";
        this.proxyhost = "127.0.0.1";
        this.proxyhost = 1080;
        slotitemNo = "206";
        dataList = new  java.util.ArrayList<>();
        serverAddress();
    }
    */
    
    public void getUnknowData(){
        
        final int taskID = getTaskNum();
            addTask(() -> {
                int no=Integer.valueOf(slotitemNo);
                
                    while(!isStopScanner()){  
                        if (getMDD("http://"+serveraddress+"/kcs/resources/image/slotitem/card/"+String.valueOf(no)+".png")) {
                            getMDD("http://"+serveraddress+"/kcs/resources/image/slotitem/item_character/"+String.valueOf(no)+".png");
                            getMDD("http://"+serveraddress+"/kcs/resources/image/slotitem/item_on/"+String.valueOf(no)+".png");
                            getMDD("http://"+serveraddress+"/kcs/resources/image/slotitem/item_up/"+String.valueOf(no)+".png");
                            no++;
                            
                        }else{
                            serverAddress();
                             //msgPublish.msgPublisher("slotitemNo: "+slotitemNo,0,1);
                            if(no != Integer.valueOf(slotitemNo)&& no > 300){
                                msgPublish.msgPublisher("扫描到新装备，已下载完毕",0,1);
                                msgPublish.urlListPublisher(localpath+File.separator+"download"+File.separator+"newSlotItem"+File.separator+"card","newSlotitem");
                                return taskID;
                            }
                            if(no>300){
                                no=Integer.valueOf(slotitemNo);
                                try{
                                    sleep(60*1000);
                                } catch (InterruptedException ex){
                                    Logger.getLogger(GetUnknowSlotitem.class.getName()).log(Level.SEVERE, null, ex);
                                    msgPublish.msgPublisher("新装备扫描线程已强制退出。",0,0);
                                    return taskID;
                                }
                                msgPublish.msgPublisher("开始下一轮新装备扫描",0,0);
                            }
                            no++;
                        } 
                        if(isStopScanner()){
                            return taskID;
                        }
                    }
                    msgPublish.msgPublisher("新装备扫描线程已停止",0,0);
                
                return taskID;
        },taskID,"GetUnknowSlotitem-getUnknowData"); 
    }
    
    public synchronized boolean getMDD(String URL) throws MalformedURLException, IOException, InterruptedException, Exception{
        int i=URL.lastIndexOf("/"); //取得子串的初始位置
        String filename=URL.substring(i+1,URL.length());
        String filepath = null;
        
        if(URL.contains("card")){
            filepath=localpath+File.separator+"download"+File.separator+"newSlotItem"+File.separator+"card";
        }
        if(URL.contains("item_character")){
            filepath=localpath+File.separator+"download"+File.separator+"newSlotItem"+File.separator+"item_character";
        }
        if(URL.contains("item_on")){
            filepath=localpath+File.separator+"download"+File.separator+"newSlotItem"+File.separator+"item_on";
        }
        if(URL.contains("item_up")){
            filepath=localpath+File.separator+"download"+File.separator+"newSlotItem"+File.separator+"item_up";
        }
        
        URL serverUrl = new URL(URL);
        HttpURLConnection urlcon;
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "15000");
        if(!(proxyhost.equals(""))){
            Proxy tempproxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyhost, proxyport));
            //Proxy tempproxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(proxyhost, proxyport));
            urlcon = (HttpURLConnection) serverUrl.openConnection(tempproxy);
        }else{
            urlcon = (HttpURLConnection) serverUrl.openConnection();
        }
        
               
        urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
        urlcon.setRequestMethod("HEAD");
        String message = urlcon.getHeaderField(0);
        if(message==null){
            //msgPublish.msgPublisher("getUnknowShip模块无法获取服务器文件信息，请检查网络。",0,-1);  
            serverAddress();
            urlcon.disconnect();
            return false;
        }
        if(message.contains("HTTP/1.1 403")){
            urlcon.disconnect();
        }
        if(message.contains("HTTP/1.1 404 Not Found")){
            urlcon.disconnect();
            //msgPublish.msgPublisher(filename+"\t找不到文件",0,-1);
        }
        if(urlcon.getLastModified()==0){
            urlcon.disconnect();
            serverAddress();
            return false;
        }
        if(message.contains("HTTP/1.1 304 Not Modified")){
            urlcon.disconnect();
            msgPublish.msgPublisher(filename+"\t文件相同",0,0);
        }
        if(message.contains("HTTP/1.1 200 OK")){
            urlcon.disconnect();
            if  (!(new File(filepath).exists())||!(new File(filepath).isDirectory())) {
                new File(filepath) .mkdirs();  
            }
            if(new DlCore().download(URL, filepath+File.separator+filename,proxyhost,proxyport)){
                //msgPublish.msgPublisher(servername+"--"+filename+"\t下载完成",0);    
            } 
            return true;
        }
        serverAddress();
        return false;  
    }
    
    private void serverAddress(){
        serveraddress=serverlistaddress[servernum];
        servername=serverlistname[servernum]+"-"+serverlistaddress[servernum];
        servernum++;
        if(servernum==serverlistaddress.length){
            servernum=0;
        }
    }
    
    public static void main(String[] args){
        new GetUnknowSlotitem().getUnknowData();
    }
    
}
