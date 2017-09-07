/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.unpackswf;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.database.Ship;
import moe.kcwiki.init.GetModifiedDataThread;
import moe.kcwiki.init.MainServer;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.massagehandler.msgPublish.msgPublisher;
import moe.kcwiki.threadpool.start2dataPool;
import moe.kcwiki.tools.constant;
import static moe.kcwiki.tools.constant.FILESEPARATOR;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author user13
 */
public class RenameShipSwf {
    private static final HashMap<String, Ship> ShipList = new LinkedHashMap<>(); 
    
    //在下载完start2数据后统一重命名
    public boolean shipSwf(String shipfolder){
        //GetModifiedDataThread.addJob();
        final int taskID = start2dataPool.getTaskNum();
        //msgPublish.msgPublisher(String.valueOf(start2dataPool.isIsInit()),0,0);
            start2dataPool.addTask(() -> {
                String shipFolder;
                String filename;
                String newfilepath;
                String outputPath=MainServer.getTempFolder()+File.separator+"ffoutput"+File.separator+"ships";
                if(!DBCenter.NewShipDB.isEmpty()){
                    //for( Map.Entry<String,Ship> map : DBCenter.NewShipDB.entrySet()){
                    DBCenter.NewShipDB.entrySet().forEach((map) -> {
                        ShipList.put(map.getKey(), map.getValue());
                    });
                }
                if(!DBCenter.SuspectShipData.isEmpty()){
                    //for( Map.Entry<String,String> map : DBCenter.SuspectShipData.entrySet()){
                    DBCenter.SuspectShipData.entrySet().forEach((map) -> {
                        ShipList.put(map.getKey(), DBCenter.ShipDB.get(map.getKey()));
                    });
                }
                
                if(!ShipList.isEmpty()){  //Ship
                    new moe.kcwiki.unpackswf.UnpackSwf().ffdec(outputPath,shipfolder);
                    msgPublish.msgPublisher("ShipSwf已全部解压完毕，开始进行重命名操作。",0,0);
                    //JOptionPane.showMessageDialog(null,"请在立绘文件解压完毕再点击按钮。" , "等待操作", JOptionPane.ERROR_MESSAGE);
                    for( Map.Entry<String,Ship> map : ShipList.entrySet()){
                        if(map.getKey()==null){continue;}
                        shipFolder=outputPath+File.separator+map.getValue().getApi_filename()+".swf";
                        String newname=outputPath+File.separator+map.getValue().getApi_filename()+"-"+map.getValue().getApi_name();
                        
                        if(new File(shipFolder).exists()){
                            new File(shipFolder).renameTo(new File(newname));
                            File[] fileList = new File(newname+File.separator+"images").listFiles();
                            for (File file : fileList) {
                                
                                if(fileList.length==15){    //普通立绘
                                    filename=file.getName();
                                    filename=filename.substring(0, filename.length()-4);
                                    if(Server.shiprule.get(filename)!=null){
                                        switch (filename) {
                                            case "5":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+".jpg");
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+".png");
                                                }
                                                break;
                                            case "1":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,160,40);
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,160,40);
                                                }
                                                break;
                                            case "7":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".jpg");
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".png");
                                                }
                                                break;
                                            case "3":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,160,40);
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,160,40);
                                                }
                                                break;
                                            default:
                                                file.renameTo(new File(newname+File.separator+"images"+File.separator+"KanMusu"+map.getValue().getApi_id()+Server.shiprule.get(filename)+".png"));
                                                if(filename.contains("17") || filename.contains("19")){
                                                    copyToPublish(newname+File.separator+"images"+File.separator+"KanMusu"+map.getValue().getApi_id()+Server.shiprule.get(filename)+".png","KanMusu"+map.getKey()+Server.shiprule.get(filename)+".png");
                                                }
                                                break;
                                        }
                                    }
                                }
                                if(fileList.length==7){     //限定立绘
                                    String season=null;
                                    for( Map.Entry<String,String> item : Server.seasonrule.entrySet()){
                                        if(map.getValue().getApi_name().contains(item.getKey())){
                                            season=item.getValue();
                                        }
                                    }
                                    if(season==null){
                                        msgPublish.msgPublisher("无法找到"+map.getValue().getApi_name()+"的对应季节名称。",0,0);
                                        break;
                                    }
                                    int year=Calendar.getInstance().get(Calendar.YEAR);
                                    filename=file.getName();
                                    filename=filename.substring(0, filename.length()-4);
                                    if(Server.shiprule.get(filename)!=null){
                                        switch (filename) {
                                            case "5":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+season+year+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+".jpg");
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+".png");
                                                }
                                                break;
                                            case "1":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+season+year+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,160,40);
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,160,40);
                                                }
                                                break;
                                            case "7":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+season+year+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".jpg");
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,218,300);
                                                    copyToPublish(newfilepath,"KanMusu"+map.getKey()+Server.shiprule.get(filename)+".png");
                                                }
                                                break;
                                            case "3":
                                                newfilepath=newname+File.separator+"images"+File.separator+"KanMusu"+map.getKey()+Server.shiprule.get(filename)+season+year+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".jpg",newfilepath,160,40);
                                                }else{
                                                    ImgPacker.compressImage(newname+File.separator+"images"+File.separator+filename+".png",newfilepath,160,40);
                                                }
                                                break;
                                            default:
                                                file.renameTo(new File(newname+File.separator+"images"+File.separator+"KanMusu"+map.getValue().getApi_id()+Server.shiprule.get(filename)+season+year+".png"));
                                                copyToPublish(newname+File.separator+"images"+File.separator+"KanMusu"+map.getValue().getApi_id()+Server.shiprule.get(filename)+".png",filename+".png");
                                                break;
                                        }
                                    }
                                    file.renameTo(new File(newname+File.separator+"images"+File.separator+filename+season+year+".png"));
                                }
                                if(fileList.length==2){     //敌舰立绘
                                    filename=file.getName();
                                    filename=filename.substring(0, filename.length()-4);
                                    if(Server.shiprule.get(filename)!=null){
                                        String filepath;
                                        switch (filename) {
                                            case "3":
                                                filepath = newname+File.separator+"images"+File.separator+filename+".jpg";
                                                if(new File(newname+File.separator+"images"+File.separator+filename+".jpg").exists()){
                                                    copyToPublish(filepath,"KanMusu"+map.getKey()+"Illust.jpg");
                                                }else{
                                                    filepath = newname+File.separator+"images"+File.separator+filename+".png";
                                                    copyToPublish(filepath,"KanMusu"+map.getKey()+"Illust.png");
                                                }
                                                break;
                                            default:
                                                file.renameTo(new File(newname+File.separator+"images"+File.separator+"KanMusu"+map.getValue().getApi_id()+Server.shiprule.get(filename)+".png"));
                                                break;
                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                }
                //msgPublish.urlListPublisher(MainServer.getPublishPath()+FILESEPARATOR+"ship", "newShip");
                msgPublisher("ShipList: "+ShipList.size(),0,0);
                msgPublish.shipPrePublisher(MainServer.getPublishPath()+FILESEPARATOR+"ship");
                //msgPublish.msgPublisher(JSON.toJSONString(msgPublish.getUrlprePublishList()),0,0);
                msgPublisher("舰娘重命名子线程运行结束",0, 1);
                start2dataPool.shutdown();
                return taskID;
        },taskID,"moe.kcwiki.unpackswf-RenameShipSwf-shipSwf"); 
        return true;
    }
    
    private boolean copyToPublish(String filepath,String filename){
        try {
            //msgPublish.msgPublisher("copyToPublish:"+filepath+"\t"+MainServer.getPublishPath()+FILESEPARATOR+"ship"+FILESEPARATOR+filename,0, -1);
            FileUtils.copyFile(new File(filepath),new File(MainServer.getPublishPath()+FILESEPARATOR+"ship"+FILESEPARATOR+filename));
            
            return true;
        } catch (IOException ex) {
            msgPublish.msgPublisher("FileUtils.copyFile模块发生IOException异常",0, -1);
            Logger.getLogger(RenameShipSwf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}