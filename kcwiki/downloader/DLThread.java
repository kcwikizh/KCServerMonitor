/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.downloader;

import com.alibaba.fastjson.JSON;
import moe.kcwiki.init.Start2DataThread;
import moe.kcwiki.init.MainServer;

import moe.kcwiki.init.GetModifiedDataThread;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Callable;
import moe.kcwiki.database.*;
import moe.kcwiki.decryptcore.*;
import static moe.kcwiki.decryptcore.CoreDecrypt.shipDataList;
import moe.kcwiki.unpackswf.Server;
import moe.kcwiki.database.DBCenter.*;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.massagehandler.msgPublish.mapurlListPublisher;
import moe.kcwiki.threadpool.Controller;
import moe.kcwiki.threadpool.corePool;
import moe.kcwiki.threadpool.start2dataPool;
import static moe.kcwiki.threadpool.start2dataPool.addTask;
import static moe.kcwiki.threadpool.start2dataPool.getTaskNum;
import moe.kcwiki.tools.constant;
import static moe.kcwiki.tools.constant.FILESEPARATOR;
import org.apache.commons.io.FileUtils;


/**
 *
 * @author VEP
 */
public class DLThread {
    private static boolean isDownloadFinish=true;
    private static String proxyhost = MainServer.getProxyhost();
    private static int proxyport = MainServer.getProxyport();
    private static String downloadpath = MainServer.getDownloadFolder();
        
    //Start2DataThread 核心下载线程
    public boolean start2() throws Exception{
        final String rootFolder=MainServer.getDownloadFolder();
        Start2DataThread.addJob();
        
        final int taskID = start2dataPool.getTaskNum();
            start2dataPool.addTask(new Callable<Integer>() {
                @Override
                public Integer call() {
            String opPath=null;
            String fileName = null;
            String filePath = null;
            //MainGui.jProgressBar1.setMaximum(DBCenter.AddressList.size());
            int value=0;
            int fFSum=0;
            try{
                for (Map.Entry<String,String> nextAddress : moe.kcwiki.database.DBCenter.AddressList.entrySet()) {
                    if(nextAddress==null){continue;}
                    if("".equals(nextAddress.getKey())){continue;}

                    if(nextAddress.getKey().contains("/resources/swf/ships")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"swf"+File.separator+"ships";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/slotitem/card")){
                        fileName=nextAddress.getValue();
                        fileName=fileName.substring(0,fileName.length()-4);
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"slotitem"+File.separator+fileName+"-"+moe.kcwiki.database.DBCenter.NewSlotitemDB.get(fileName).getApi_name();
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+"Soubi"+fileName+Server.slotitemrule.get("card")+".png";
                    }
                                   
                    if(nextAddress.getKey().contains("/resources/image/slotitem/item_character")){
                        fileName=nextAddress.getValue();
                        fileName=fileName.substring(0,fileName.length()-4);
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"slotitem"+File.separator+fileName+"-"+moe.kcwiki.database.DBCenter.NewSlotitemDB.get(fileName).getApi_name();
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+"Soubi"+fileName+Server.slotitemrule.get("item_character")+".png";
                    }
                                        
                    if(nextAddress.getKey().contains("/resources/image/slotitem/item_on")){
                        fileName=nextAddress.getValue();
                        fileName=fileName.substring(0,fileName.length()-4);
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"slotitem"+File.separator+fileName+"-"+moe.kcwiki.database.DBCenter.NewSlotitemDB.get(fileName).getApi_name();
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+"Soubi"+fileName+Server.slotitemrule.get("item_on")+".png";
                    }
                                                            
                    if(nextAddress.getKey().contains("/resources/image/slotitem/item_up")){
                        fileName=nextAddress.getValue();
                        fileName=fileName.substring(0,fileName.length()-4);
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"slotitem"+File.separator+fileName+"-"+moe.kcwiki.database.DBCenter.NewSlotitemDB.get(fileName).getApi_name();
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+"Soubi"+fileName+Server.slotitemrule.get("item_up")+".png";
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/floor")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"floor";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/wall")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"wall";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/window")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"window";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/object")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"object";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/chest")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"chest";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/furniture/desk")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"furniture"+File.separator+"desk";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/image/useitem/card")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"image"+File.separator+"useitem"+File.separator+"card";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/swf/map")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"swf"+File.separator+"map";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(nextAddress.getKey().contains("/resources/swf/sound_b_bgm_")){
                        fileName=nextAddress.getValue();
                        filePath=rootFolder+File.separator+"resources"+File.separator+"swf";
                        //if  (!((new File(filePath).exists())&&( new File(filePath).isDirectory()))) { new File(filePath) .mkdirs();}
                        opPath=filePath+File.separator+fileName;
                    }
                    
                    if(!Controller.isIsPoolInit()){
                        msgPublish.msgPublisher("进程池被关闭,Start2文件下载线程已强制退出。",0,0);
                        return taskID;
                    }
                    
                    int reCode=new DlCore().download(nextAddress.getKey(),opPath,filePath,proxyhost,proxyport);
                    
                    if(reCode==1){ 
                        value++;  
                        if(!opPath.contains(".swf")){
                            //msgPublish.msgPublisher("!swf",0,0);
                            if(opPath.contains("slotitem") &&  opPath.contains("card")){
                                //msgPublish.msgPublisher("slotitem:"+opPath,0,0);
                                msgPublish.urlPrePublisher(opPath, fileName);
                            }
                            if(opPath.contains("furniture") ){
                                //msgPublish.msgPublisher("furniture:"+opPath,0,0);
                                msgPublish.urlPrePublisher(opPath, fileName);
                            }
                            if(opPath.contains("furniture") ){
                                //msgPublish.msgPublisher("useitem:"+opPath,0,0);
                                msgPublish.urlPrePublisher(opPath, fileName);
                            }
                            String relativePath = filePath.substring(rootFolder.length()+1, filePath.length());
                            if(! new File(MainServer.getWorksFolder()+File.separator+relativePath).exists()){
                                new File(MainServer.getWorksFolder()+File.separator+relativePath).mkdirs();
                            }
                            FileUtils.copyFileToDirectory(new File(opPath), new File(MainServer.getWorksFolder()+File.separator+relativePath));
                        }
                        //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value); 
                    }
                    if(reCode==0){ 
                        msgPublish.msgPublisher(constant.LINESEPARATOR+"无法连接服务器。",0,-1);
                        isDownloadFinish=false;
                        break;
                    }
                    if(reCode==2){ 
                        fFSum++;
                        value++;  
                            //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value);
                    } 
                    
                }
                
                if(isDownloadFinish==true){
                    msgPublish.msgPublisher("文件全部下载完成！下载失败："+fFSum+"个文件。"+constant.LINESEPARATOR+"下载路径为："+rootFolder+File.separator+"resources",0,0);
                }else{
                    msgPublish.msgPublisher("文件下载失败。",0,0);
                    //JOptionPane.showMessageDialog(null, "文件下载失败。");
                }
            }catch (Exception e) {
                msgPublish.msgPublisher(constant.LINESEPARATOR+"Start2下载线程创建失败。",0,-1);
                return taskID;
            }
            //new moe.kcwiki.unpackswf.RenameShipSwf().shipSwf(npath+File.separator+"resources"+File.separator+"swf"+File.separator+"ships");
            msgPublish.msgPublisher("Start2新数据下载完成",0,0);
            new moe.kcwiki.unpackswf.RenameShipSwf().shipSwf(rootFolder+FILESEPARATOR+"resources"+FILESEPARATOR+"swf"+FILESEPARATOR+"ships");
            return taskID;
        }   
        },taskID,"moe.kcwiki.downloader-DLThread-start2");  
        return isDownloadFinish==true;
    }
    
    //CoreDecrypt
    public boolean modifieddata(String rootFolder,final String ffdecpath,int mode) {
        //final String opPath=rootFolder;
        GetModifiedDataThread.addJob();
        final int taskID = getTaskNum();
            corePool.addTask(new Callable<Integer>() {
                @Override
                public Integer call() {
                
                if(mode==1){

                    int value=0;

                    for (Map.Entry<String,String> nextAddress : CoreDecrypt.mapAddressList.entrySet()) {
                        if(nextAddress==null){continue;}
                        if(!nextAddress.getKey().contains("/resources/swf/map")){continue;}
                        String opPath=rootFolder+File.separator+nextAddress.getValue();
                        try{
                            int reCode=new DlCore().download(nextAddress.getKey(),opPath,rootFolder,proxyhost,proxyport);

                            if(reCode==1){ 
                                value++;  
                                //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value); 
                            }
                            if(reCode==0){ 
                                msgPublish.msgPublisher(constant.LINESEPARATOR+"无法连接服务器。",0,-1);
                                break;
                            }
                            if(reCode==2){ 
                                value++;  
                                //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value);
                            } 
                        }catch (Exception e) {
                            msgPublish.msgPublisher(constant.LINESEPARATOR+"地图数据下载线程创建失败。",0,-1);
                            //return taskID;
                        }
                    }
                    String decompressionFolder = MainServer.getWorksFolder()+File.separator+"maps";
                    new moe.kcwiki.unpackswf.UnpackSwf().maps(decompressionFolder, rootFolder);
                    mapurlListPublisher(decompressionFolder);
                    if(CoreDecrypt.mapAddressList.isEmpty()){
                        msgPublish.msgPublisher("数据库中没有新地图信息。",0,-1);
                    }else{
                        msgPublish.msgPublisher("地图数据下载完成",0,1);
                        GetModifiedDataThread.finishJob();
                    }
                    return taskID;
                }

                if(mode==2){
                    //final String opPath=rootFolder;
                    int value=0;
                   //MainGui.jProgressBar1.setMaximum(CoreDecrypt.shipAddressList.size());
                    for (Map.Entry<String,String> nextAddress : CoreDecrypt.shipAddressList.entrySet()) {
                        //msgPublish.msgPublisher("shipAddressList"+nextAddress.getKey(),0,0);
                        if(nextAddress==null){continue;}
                        if(!nextAddress.getKey().contains("/sound/kc")){continue;}
                        //String shipdata=shipDataList.get(nextAddress.getValue());
                        Ship ship=moe.kcwiki.database.DBCenter.NewShipDB.get(shipDataList.get(nextAddress.getKey()));
                        String filepath=ship.getApi_filename()+"-"+ship.getApi_name();
                        String filename=nextAddress.getValue();
                        filename=Server.shipvoicerule.get(filename);
                        String folder = rootFolder+File.separator+filepath;
                        String opPath=rootFolder+File.separator+filepath+File.separator+shipDataList.get(nextAddress.getKey())+"-"+filename+".mp3";

                        try{
                            int reCode=new DlCore().download(nextAddress.getKey(),opPath,folder,proxyhost,proxyport);

                            if(reCode==1){ 
                                value++;  
                                //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value); 
                            }
                            if(reCode==0){ 
                                msgPublish.msgPublisher(constant.LINESEPARATOR+"无法连接服务器。",0,-1);
                                break;
                            }
                            if(reCode==2){ 
                                value++;  
                                //moe.kcwiki.massagehandler.MainGui.jProgressBar1.setValue(value);
                            } 
                        }catch (Exception e) {
                            msgPublish.msgPublisher(constant.LINESEPARATOR+"语音数据下载线程创建失败。",0,-1);
                            //return taskID;
                        }
                    }
                    if(!CoreDecrypt.shipAddressList.isEmpty()){
                        msgPublish.msgPublisher("语音数据下载完成",0,1);
                        GetModifiedDataThread.finishJob();
                    }
                    return taskID;
                } 
                return taskID;
            }
        },taskID,"moe.kcwiki.downloader-DLThread-Map&ShipVoice");   
        return true;
    }
      
}
