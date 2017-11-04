/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.downloader;

import moe.kcwiki.tools.swfunpacker.coredecryptor.CoreDecrypt;
import moe.kcwiki.initializer.Start2DataThread;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.initializer.GetModifiedDataThread;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import moe.kcwiki.database.*;
import static moe.kcwiki.tools.swfunpacker.coredecryptor.CoreDecrypt.shipDataList;
import moe.kcwiki.tools.swfunpacker.Server;
import moe.kcwiki.handler.massage.msgPublish;
import static moe.kcwiki.handler.massage.msgPublish.mapurlListPublisher;
import moe.kcwiki.handler.thread.Controller;
import moe.kcwiki.handler.thread.corePool;
import moe.kcwiki.handler.thread.start2dataPool;
import static moe.kcwiki.handler.thread.start2dataPool.getTaskNum;
import moe.kcwiki.tools.constant.constant;
import static moe.kcwiki.tools.constant.constant.FILESEPARATOR;
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
                            String relativePath = filePath.substring(rootFolder.length()+1, filePath.length());
                            File srcFile = new File(opPath);
                            //File pubFolder = new File(MainServer.getPublishFolder()+File.separator+relativePath);
                            //FileUtils.copyFileToDirectory(srcFile, pubFolder);
                            File workFolder = new File(MainServer.getWorksFolder()+File.separator+relativePath);
                            FileUtils.copyFileToDirectory(srcFile, workFolder);
                            //msgPublish.msgPublisher("!swf",0,0);
                            if(opPath.contains("Soubi"+fileName+Server.slotitemrule.get("card")+".png")) {
                                //msgPublish.msgPublisher("slotitem:"+opPath,0,0);
                                msgPublish.urlPrePublisher(workFolder +File.separator+ srcFile.getName() , fileName);
                            }
                            if(opPath.contains("furniture") ){
                                //msgPublish.msgPublisher("furniture:"+opPath,0,0);
                                msgPublish.urlPrePublisher(workFolder +File.separator+ srcFile.getName() , fileName);
                            }
                            if(opPath.contains("useitem") ){
                                //msgPublish.msgPublisher("useitem:"+opPath,0,0);
                                msgPublish.urlPrePublisher(workFolder +File.separator+ srcFile.getName() , fileName);
                            }
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
            new moe.kcwiki.tools.standardization.RenameShipSwf().shipSwf(rootFolder+FILESEPARATOR+"resources"+FILESEPARATOR+"swf"+FILESEPARATOR+"ships");
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
                    new moe.kcwiki.tools.swfunpacker.UnpackSwf().maps(decompressionFolder, rootFolder);
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
      
    public String getFileName(String path) {
        return new File(path).getName().replaceAll("[.][^.]+$", "");
    }
}
