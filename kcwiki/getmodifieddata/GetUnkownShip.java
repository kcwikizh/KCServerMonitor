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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import moe.kcwiki.getstart2data.Encoder;

import moe.kcwiki.init.MainServer;
import static moe.kcwiki.init.MainServer.isStopScanner;
import moe.kcwiki.unpackswf.ImgPacker;
import moe.kcwiki.unpackswf.Server;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.threadpool.getUnkownShipPool.*;
import moe.kcwiki.tools.GetHash;
import moe.kcwiki.tools.constant;

/**
 *
 * @author Administrator
 */
public class GetUnkownShip {
    private final String unknowShipFile;
    private static String intputFile;
    private final String localpath;
    private final String localoldstart2;
    private final String proxyhost;
    private final int proxyport;
    private static String serveraddress;
    private static String servername;
    private static int servernum=0;
    public static LinkedHashMap<String, String> unknowShipList = new LinkedHashMap<>(); 
    private final static Set<String> md5DataSet = new HashSet<>();
    private final static List<JSONObject> md5DataList = new ArrayList<>();
    //private final static LinkedHashMap<String,JSONObject> md5DataMap = new LinkedHashMap<>();
    private GetHash Hash = new GetHash();
    //private final java.util.ArrayList<String>  dataList;
    
    
    public GetUnkownShip(){
        this.localpath = moe.kcwiki.init.MainServer.getLocalpath();
        //this.localpath = "L:\\NetBeans\\NetBeansProjects\\moe kcwiki stdunpacktools\\build";
        this.localoldstart2 = moe.kcwiki.init.MainServer.getLocaloldstart2data();
        //this.localoldstart2 = MainServer.getDataPath()+File.separator+"oldstart2.json";
        this.proxyhost = moe.kcwiki.init.MainServer.getProxyhost();
        this.proxyport = moe.kcwiki.init.MainServer.getProxyport();
        unknowShipFile = MainServer.getDataPath()+File.separator+"unknowShip";
        //dataList = new  java.util.ArrayList<>();
        serverAddress();
    }
        
    public boolean loadKnewFile(){
        String line;
        String str = "";
        String ipFile=MainServer.getDataPath()+File.separator+"shipgraphdata.json";
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

    public void getUnknowData(){
        
        unknowShipList.clear();
        LinkedHashMap<String, String> alldataList = new LinkedHashMap<>(); 
        LinkedHashMap<String, String> olddataFileList = new LinkedHashMap<>();
        LinkedHashMap<String, String> olddataIdList = new LinkedHashMap<>();
        StringBuilder buffer;
        String line;
        /*
        try (BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(unknowShipFile+".txt"), Encoder.codeString(unknowShipFile+".txt")))) {
            while ((line=nBfr.readLine())!=null) {
                if(line.contains("\t")){
                    String[] data=line.split("\t");
                    alldataList.put(data[0], data[1]);
                }
            }
            
            try (BufferedReader Bfr = new BufferedReader(new InputStreamReader(new FileInputStream(localoldstart2), Encoder.codeString(localoldstart2)))) {
                buffer = new StringBuilder();
                while ((line=Bfr.readLine())!=null) {
                    buffer.append(line);
                }
            }
            JSONObject Data=JSON.parseObject(buffer.toString());
            JSONArray newData=Data.getJSONArray("api_mst_shipgraph");
            Iterator iterator=newData.iterator();
            while(iterator.hasNext()){
                JSONObject newObject=(JSONObject)iterator.next();
                olddataFileList.put(newObject.getString("api_id"), newObject.getString("api_filename"));
            }
            
            newData=Data.getJSONArray("api_mst_ship");
            iterator=newData.iterator();
            while(iterator.hasNext()){
                JSONObject newObject=(JSONObject)iterator.next();
                olddataIdList.put(newObject.getString("api_id"), newObject.getString("api_name"));
            }
            
            alldataList.entrySet().stream().filter((ship) -> (!olddataFileList.containsValue(ship.getValue()))).forEachOrdered((ship) -> {
                if(ship.getKey().contains("-")){
                    if(!dataList.contains(ship.getValue())){
                        dataList.add(ship.getValue());
                    }
                }else if(Integer.parseInt(ship.getKey()) <= 2000){
                    if(!dataList.contains(ship.getValue())){
                        dataList.add(ship.getValue());
                    }
                }
            });

            olddataFileList.entrySet().stream().filter((ship) -> (!olddataIdList.containsKey(ship.getKey()))).forEachOrdered((ship) -> {
                if(ship.getKey().contains("-")){
                    if(!dataList.contains(ship.getValue())){
                        dataList.add(ship.getValue());
                    }
                }else if(Integer.parseInt(ship.getKey())<=2000){
                    if(!dataList.contains(ship.getValue())){
                        dataList.add(ship.getValue());
                    }
                }
            });
            
        } catch (FileNotFoundException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:FileNotFoundException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:IOException",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetUnkownShip-getAllData:Exception",0,-1);
                Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        final int taskID = getTaskNum();
        addTask(new Callable<Integer>() {
                @Override
                public Integer call() {
                    
                        GetUnkownShip rename=new GetUnkownShip();
                        while(md5DataList!=null){
                            Iterator<JSONObject> it = md5DataList.iterator();     
                            while(it.hasNext()) {  
                                if(isStopScanner()){
                                    msgPublish.msgPublisher("新立绘扫描线程已停止",0,0);
                                    return taskID;
                                }
                                JSONObject data = it.next(); 
                                try {
                                    if (getMDD("http://"+serveraddress+"/kcs/"+data.getString("path")+".swf",data.getLong("timestamp"))) {
                                        it.remove();
                                        int i=data.getString("path").lastIndexOf("/"); //取得子串的初始位置
                                        String filename=data.getString("path").substring(i+1,data.getString("path").length());
                                        if(md5DataSet.contains(Hash.getNewHash(localpath+File.separator+"download"+File.separator+"newShip"+File.separator+filename+".swf"))){
                                            continue;
                                        }
                                        rename.renameShipSwf(filename);
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);  
                                } catch (Exception ex) {
                                    Logger.getLogger(GetUnkownShip.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                            if(md5DataList.isEmpty()){
                                msgPublish.msgPublisher("新立绘扫描线程结束",0,0);
                                return taskID;
                            }
                            try{
                                sleep(90*1000);
                            } catch (InterruptedException ex){
                                Logger.getLogger(GetUnknowSlotitem.class.getName()).log(Level.SEVERE, null, ex);
                                msgPublish.msgPublisher("新立绘扫描线程已强制退出。",0,0);
                                return taskID;
                            }
                            if(isStopScanner()){
                                msgPublish.msgPublisher("新立绘扫描线程已停止",0,0);
                                return taskID;
                            }
                            msgPublish.msgPublisher("开始下一轮新立绘扫描",0,0);
                        }
                    
                    return taskID;
                }
            },taskID,"GetUnkownShip-getUnknowData");
        
        /*
        new Thread() {
        @Override
            public void run() {
                GetUnkownShip rename=new GetUnkownShip();
                try {
                    while(dataList!=null){
                        Iterator<String> it = dataList.iterator();     
                        while(it.hasNext()) {  
                            String data = it.next(); 
                            if (getMDD("http://"+serveraddress+"/kcs/resources/swf/ships/"+data+".swf")) {
                                it.remove();
                                if(md5DataSet.equals(Hash.getNewHash(localpath+File.separator+"download"+File.separator+"newShip"+File.separator+data+".swf"))){
                                    continue;
                                }
                                rename.renameShipSwf(data);
                            }  
                            if(isStopScanner()){
                                return;
                            }
                        } 
                        if(dataList.isEmpty()){
                            msgPublish.msgPublisher("新立绘扫描线程结束",0,0);
                            break;
                        }
                        sleep(90*1000);
                        msgPublish.msgPublisher("开始下一轮新立绘扫描",0,0);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GetLastModifiedData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        }.start();  
        */
    }
    
    public synchronized boolean getMDD(String URL,Long timestamp) throws MalformedURLException, IOException, InterruptedException, Exception{
        int i=URL.lastIndexOf("/"); //取得子串的初始位置
        String filename=URL.substring(i+1,URL.length());
        String filepath=localpath+File.separator+"download"+File.separator+"newShip";
        
        URL serverUrl = new URL(URL);
        HttpURLConnection urlcon;
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "15000");
        if(!MainServer.isDebugMode()){
            Proxy tempproxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyhost, proxyport));
            //Proxy tempproxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(proxyhost, proxyport));
            urlcon = (HttpURLConnection) serverUrl.openConnection(tempproxy);
        }else{
            urlcon = (HttpURLConnection) serverUrl.openConnection();
        }
        
        urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
        urlcon.setRequestMethod("HEAD");
        urlcon.setIfModifiedSince(timestamp);
        String message = urlcon.getHeaderField(0);
        if(message==null){
            //msgPublish.msgPublisher("getUnknowShip模块无法获取服务器文件信息，请检查网络。",0,-1);  
            serverAddress();
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
                //msgPublish.msgPublisher(filename+"\t下载完成",1,0);    
            } 
            return true;
        }
        serverAddress();
        return false;  
    }
    
    private static void serverAddress(){
        String[] serverlistaddress= new String[]{"203.104.209.71", "203.104.209.87", "125.6.184.16", "125.6.187.205", "125.6.187.229","125.6.187.253", "125.6.188.25", "203.104.248.135", "125.6.189.7", "125.6.189.39","125.6.189.71", "125.6.189.103", "125.6.189.135", "125.6.189.167", "125.6.189.215","125.6.189.247", "203.104.209.23", "203.104.209.39", "203.104.209.55", "203.104.209.102"};
        String[] serverlistname=new String[]{"横须贺镇守府","呉镇守府","佐世保镇守府","舞鹤镇守府","大凑警备府","トラック泊地","リンガ泊地","ラバウル基地","ショートランド泊地","ブイン基地","タウイタウイ泊地","パラオ泊地","ブルネイ泊地","単冠湾泊地","幌筵泊地","宿毛湾泊地","鹿屋基地","岩川基地","佐伯湾泊地","柱岛泊地"};
        GetUnkownShip.serveraddress=serverlistaddress[servernum];
        GetUnkownShip.servername=serverlistname[servernum]+"-"+serverlistaddress[servernum];
        servernum++;
        if(servernum==serverlistaddress.length){
            servernum=0;
        }
    }
    
    public boolean renameShipSwf(String shipname){
        
        String filename;
        String newfilepath;
        String shipFolder=localpath+File.separator+"temp"+File.separator+"newShip"+File.separator+shipname;
        String shipFile=localpath+File.separator+"download"+File.separator+"newShip"+File.separator+shipname+".swf";
        
        new moe.kcwiki.unpackswf.UnpackSwf().ffdec(shipFolder, shipFile);
        
            File[] fileList = new File(shipFolder+File.separator+"images").listFiles();
            if(fileList!=null){
                for (File file : fileList) {
                    if(fileList.length==15){
                        
                    }
                }  
                
            msgPublish.unkonwedShipListPublisher(shipFolder+File.separator+"images");
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
