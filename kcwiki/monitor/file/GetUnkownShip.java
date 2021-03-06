/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.monitor.file;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.initializer.MainServer;
import static moe.kcwiki.initializer.MainServer.isStopScanner;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.handler.thread.getUnkownShipPool;
import static moe.kcwiki.handler.thread.getUnkownShipPool.*;
import moe.kcwiki.tools.GetHash;
import moe.kcwiki.tools.constant.constant;
import moe.kcwiki.tools.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Administrator
 */
public class GetUnkownShip {
    private final String unknowShipFile;
    private static String intputFile;
    private final String localoldstart2;
    private final String proxyhost;
    private final int proxyport;
    private static String serveraddress = "203.104.209.102";
    private static String servername;
    private static int servernum=0;
    public static LinkedHashMap<String, String> unknowShipList = new LinkedHashMap<>(); 
    private final static Set<String> md5DataSet = new HashSet<>();
    private final static List<JSONObject> md5DataList = new ArrayList<>();
    private static int blockCount = 30;
    //private final static LinkedHashMap<String,JSONObject> md5DataMap = new LinkedHashMap<>();
    private static ArrayList<String> serverlistaddress = MainServer.getWorldList();
    private static String[] serverlistname = new String[]{"横须贺镇守府","呉镇守府","佐世保镇守府","舞鹤镇守府","大凑警备府","トラック泊地","リンガ泊地","ラバウル基地","ショートランド泊地","ブイン基地","タウイタウイ泊地","パラオ泊地","ブルネイ泊地","単冠湾泊地","幌筵泊地","宿毛湾泊地","鹿屋基地","岩川基地","佐伯湾泊地","柱岛泊地"};
    private GetHash Hash = new GetHash();
    private final String rootFolder = MainServer.getTempFolder()+File.separator+"newShip";
    //private final java.util.ArrayList<String>  dataList;
    
    
    public GetUnkownShip(){
        //this.localpath = "L:\\NetBeans\\NetBeansProjects\\moe kcwiki stdunpacktools\\build";
        this.localoldstart2 = moe.kcwiki.initializer.MainServer.getLocaloldstart2data();
        //this.localoldstart2 = MainServer.getDataPath()+File.separator+"oldstart2.json";
        this.proxyhost = moe.kcwiki.initializer.MainServer.getProxyhost();
        this.proxyport = moe.kcwiki.initializer.MainServer.getProxyport();
        unknowShipFile = MainServer.getDataFolder()+File.separator+"unknowShip";
        //dataList = new  java.util.ArrayList<>();
        //serverAddress();
    }
        
    public boolean loadKnewFile(){
        String line;
        String str = "";
        String ipFile=MainServer.getDataFolder()+File.separator+"shipgraphdata.json";
        if(!new File(ipFile).exists()){
            msgPublish.msgPublisher("找不到Md5的扫描文件，请向技术组索取。",0,-1);
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
        JSONArray md5Data=JSON.parseArray(str);
        Iterator iterator=md5Data.iterator();
        md5DataSet.add(null);
                while(iterator.hasNext()){
                    JSONObject object=(JSONObject)iterator.next();
                    md5DataList.add(object);
                    md5DataSet.add(object.getString("hash"));
                }
        return true;
    }
    
    public boolean getAllData(){
        StringBuilder buffer = null;

        File file=new File(unknowShipFile+".txt");
        if(file.exists()){
            try (BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(unknowShipFile+".txt"), Encoder.codeString(unknowShipFile+".txt")))) {
                String line;
                while ((line=nBfr.readLine())!=null) {
                    if(line.contains("\t")){
                        String[] data=line.split("\t");
                        unknowShipList.put(data[0], data[1]);
                    }
                }
            } catch (FileNotFoundException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:FileNotFoundException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:IOException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (Exception ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:Exception",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            //file.renameTo(new File(unknowShipFile+" backup.txt"));
        }
        
        try (BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(intputFile), Encoder.codeString(intputFile)))) {
            String line;
            buffer = new StringBuilder();
            while ((line=nBfr.readLine())!=null) {
                buffer.append(line);
            }
        }catch (FileNotFoundException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:FileNotFoundException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:IOException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (Exception ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:Exception",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
        }
        
        /*
        JSONObject Data=JSON.parseObject(buffer.toString());
        JSONArray newData=Data.getJSONArray("api_mst_ship");
        Iterator iterator=newData.iterator();
        while(iterator.hasNext()){
            JSONObject newObject=(JSONObject)iterator.next();
            if(unknowShipList.containsKey(newObject.getString("api_id"))){
                if(!unknowShipList.get(newObject.getString("api_id")).equals(newObject.getString("api_name"))){
                    int num=1;
                    String newkey=newObject.getString("api_id")+"-"+String.valueOf(num);
                    while(true){
                        if(!unknowShipList.containsKey(newkey)){
                            unknowShipList.put(newkey, newObject.getString("api_name"));
                            break;
                        }
                        num++;
                        newkey=newObject.getString("api_id")+"-"+String.valueOf(num);
                    }
                }
            }else{
                unknowShipList.put(newObject.getString("api_id"), newObject.getString("api_name"));
            }
            
        }
        */
        
        JSONObject Data=JSON.parseObject(buffer.toString());
        JSONArray newData=Data.getJSONArray("api_mst_shipgraph");
        Iterator iterator=newData.iterator();
        while(iterator.hasNext()){
            JSONObject newObject=(JSONObject)iterator.next();
            if(unknowShipList.containsKey(newObject.getString("api_id"))){
                if(!unknowShipList.get(newObject.getString("api_id")).equals(newObject.getString("api_filename"))){
                    int num=1;
                    String newkey=newObject.getString("api_id")+"-"+String.valueOf(num);
                    while(true){
                        if(!unknowShipList.containsKey(newkey)){
                            unknowShipList.put(newkey, newObject.getString("api_filename"));
                            break;
                        }
                        num++;
                        newkey=newObject.getString("api_id")+"-"+String.valueOf(num);
                    }
                }
            }else{
                unknowShipList.put(newObject.getString("api_id"), newObject.getString("api_filename"));
            }
            
        }
        
        try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(unknowShipFile+".txt")), "UTF-8"))) {
            for (Map.Entry<String,String> ship : unknowShipList.entrySet()) {
                eBfw.write(ship.getKey()+"\t"+ship.getValue()+constant.LINESEPARATOR);
            }
        } catch (FileNotFoundException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:FileNotFoundException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:IOException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (Exception ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:Exception",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                return false;
        }
        return true;
    }

    public void getUnknowData(List<JSONObject> md5DataList,int blockno){
        /*if(MainServer.isDebugMode()){
            return;
        }*/
        
        final int taskID = getUnkownShipPool.getTaskNum();
        getUnkownShipPool.addTask(new Callable<Integer>() {
                @Override
                public Integer call() {
                        GetUnkownShip rename=new GetUnkownShip();
                        while(md5DataList!=null){
                            Iterator<JSONObject> it = md5DataList.iterator();     
                            while(it.hasNext()) {  
                                if(isStopScanner() && taskID == blockCount - 2){
                                    msgPublish.msgPublisher("新立绘扫描线程已停止",0,0);
                                    return taskID;
                                }
                                JSONObject data = it.next(); 
                                try {
                                    if (getMDD("http://"+serveraddress+"/kcs/"+data.getString("path"),data.getLong("timestamp"),data.getString("hash"))) {
                                        it.remove();
                                        String filename=data.getString("filename");
                                        if(!new File(rootFolder+File.separator+filename+".swf").exists()){
                                            continue;
                                        }
                                        if(md5DataSet.contains(Hash.getNewHash(rootFolder+File.separator+filename+".swf"))){
                                            continue;
                                        }
                                        msgPublish.msgPublisher("立绘文件： "+data.getString("id")+"\t"+"http://"+serveraddress+"/kcs/"+data.getString("path"),0,0);
                                        msgPublish.msgPublisher("立绘HASH： "+Hash.getNewHash(rootFolder+File.separator+filename+".swf"),0,0);
                                        rename.renameShipSwf(filename);
                                    } else {
                                        serverAddress();
                                    }
                                } catch (Exception ex) {
                                    Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            if(md5DataList.isEmpty()){
                                msgPublish.msgPublisher("新立绘扫描线程:Thread-"+taskID+"结束",0,0);
                                return taskID;
                            }
                            try{
                                sleep(90*1000);
                            } catch (InterruptedException ex){
                                Logger.getLogger(GetUnknowSlotitem.class.getName()).log(Level.SEVERE, null, ex);
                                msgPublish.msgPublisher("新立绘扫描线程:Thread-"+taskID+"已强制退出。",0,0);
                                return taskID;
                            }
                            if(isStopScanner() && taskID == blockCount - 2){
                                msgPublish.msgPublisher("新立绘扫描线程已停止",0,0);
                                return taskID;
                            }
                            if(taskID == blockCount - 2){
                                msgPublish.msgPublisher("开始下一轮新立绘扫描",0,0);
                            }
                            
                        }
                    
                    return taskID;
                }
            },taskID,"GetUnkownShip-getUnknowData Thread-"+taskID);
    }
    
        
    public boolean setterSplitter(){
        unknowShipList.clear();
        
        if(!loadKnewFile()){
            return false;
        }
            ArrayList<JSONObject> tempList = new ArrayList();
            
            int blockSize= (int) Math.ceil(md5DataList.size()/(double) blockCount);
            int countno=0;
            int sum=0;
            int blockno=1;
            for(JSONObject ship:md5DataList){
                tempList.add(ship);
                countno++;
                sum++;
                if(countno==blockSize){
                    getUnknowData((List<JSONObject>) tempList.clone(),blockno);
                    tempList.clear();
                    countno=0;
                    blockno++;
                    continue;
                }
                if(sum==md5DataList.size()){
                    getUnknowData((List<JSONObject>) tempList.clone(),blockno);
                    tempList.clear();
                    break;
                }
            }
            msgPublish.msgPublisher("开始新舰娘扫描。",0,0);
            return true;
    }
    
    //synchronized
    public boolean getMDD(String URL,Long timestamp,String HashHex) {
        try {
            int i=URL.lastIndexOf("/"); //取得子串的初始位置
            String filename=URL.substring(i+1,URL.length());
            
            URL serverUrl = new URL(URL);
            HttpURLConnection urlcon;
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
            System.setProperty("sun.net.client.defaultReadTimeout", "15000");
            /*if(!MainServer.isDebugMode()){
                Proxy tempproxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyhost, proxyport));
                //Proxy tempproxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(proxyhost, proxyport));
                urlcon = (HttpURLConnection) serverUrl.openConnection(tempproxy);
            }else{
                urlcon = (HttpURLConnection) serverUrl.openConnection();
            }*/
            urlcon = (HttpURLConnection) serverUrl.openConnection();
            urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
            urlcon.setRequestMethod("HEAD");
            urlcon.setIfModifiedSince(timestamp);
            urlcon.setUseCaches(false);
            String message = urlcon.getHeaderField(0);
            if(message==null){
                //msgPublish.msgPublisher("getUnknowShip模块无法获取服务器文件信息，请检查网络。",0,-1);
                serverAddress();
                return false;
            }
            if(message.contains("HTTP/1.1 403")){
                urlcon.disconnect();
                serverAddress();
                return false;
            }
            if(message.contains("HTTP/1.1 404 Not Found")){
                urlcon.disconnect();
                return true;
            }
            if(urlcon.getLastModified()==0){
                urlcon.disconnect();
                serverAddress();
                return false;
            }
            if(message.contains("HTTP/1.1 304 Not Modified")){
                urlcon.disconnect();
                return true;
            }
            if(message.contains("HTTP/1.1 200 OK")){
                urlcon.disconnect();
                if(timestamp >= urlcon.getLastModified()){
                    urlcon.disconnect();
                    //msgPublish.msgPublisher("移除无法识别If-Modified-Since的服务器：\t"+serveraddress,0,0); 
                    serverlistaddress.remove(serveraddress);
                    //serverlistaddress = MainServer.getWorldList();
                    return false;
                }
                if  (!(new File(rootFolder).exists()) || !(new File(rootFolder).isDirectory())) {
                    new File(rootFolder) .mkdirs();
                }
                if(new DlCore().download(URL, rootFolder+File.separator+filename,proxyhost,proxyport)){
                    /*String hashhex = getMD5Checksum(rootFolder+File.separator+filename);
                    msgPublish.msgPublisher(filename+"\t"+urlcon.getLastModified(),0,0);
                    msgPublish.msgPublisher(filename+"\t"+timestamp,0,0);
                    msgPublish.msgPublisher(filename+"\t"+hashhex,0,0);
                    if(hashhex.equals(HashHex)){
                        return false;
                    }*/
                }
                return true;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
        }
        //serverAddress();
        return false;
    }
    
    private static void serverAddress(){
        if(servernum >= serverlistaddress.size()-1){
            servernum=0;
        }
        if(serverlistaddress.isEmpty()){
            serveraddress = "203.104.209.102";
        }else{
            serveraddress=serverlistaddress.get(servernum);
            servername=serverlistname[servernum]+"-"+serverlistaddress.get(servernum);
            servernum++;
        }
        if(servernum >= serverlistaddress.size()){
            servernum=0;
        }
    }
    
    public static String getMD5Checksum(String filename) {
        String md5 = "";
        try (FileInputStream fis = new FileInputStream(filename)) {
            md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
            IOUtils.closeQuietly(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            msgPublish.msgPublisher("getUnkownShipPool-getMD5Checksum模块读写时发生FileNotFoundException错误。",0,-1); 
        } catch (IOException ex) {
            Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
            msgPublish.msgPublisher("getUnkownShipPool-getMD5Checksum模块读写时发生IOException错误。",0,-1); 
        }
        return md5;
    }
    
    public boolean renameShipSwf(String shipname){
        
        String publishFolder=MainServer.getPublishFolder()+File.separator+"newShip"+File.separator+shipname;
        String shipFile=rootFolder+File.separator+shipname+".swf";
        
        new moe.kcwiki.tools.swfunpacker.UnpackSwf().ffdec(publishFolder, shipFile);
        
        File[] fileList = new File(publishFolder+File.separator+"images").listFiles();
        if(fileList!=null){
            /*for (File file : fileList) {
                if(fileList.length==15){
                        
                }
            }*/
                
            msgPublish.unkonwedShipListPublisher(publishFolder+File.separator+"images");
            return true;
        }
        return false;
    }
    
    /*
    public static void main(String[] args){
        intputFile="G:\\KcWiki\\Start2 历代文件\\start2\\20170429.json";
        new GetUnkownShip().getAllData();
        //new GetUnkownShip().getUnknowData();
        JOptionPane.showMessageDialog(null, "文件生成完毕。");
    }
    */
    
}
