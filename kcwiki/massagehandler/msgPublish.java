/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.massagehandler;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import moe.kcwiki.init.MainServer;
import static moe.kcwiki.tools.constant.FILESEPARATOR;
import moe.kcwiki.unpackswf.Server;

/**
 *
 * @author iTeam_VEP
 */
public class msgPublish {
    private static final msgBroadcast msgpublisher = msgBroadcast.getInstance();
    private static final List urlList = Collections.synchronizedList(new ArrayList());
    private static final List urlprePublishList = Collections.synchronizedList(new ArrayList());
    private static boolean allowPublish = true;
    //private static boolean init = false;
    /**
    *
    * type: 0-admin; 1-system; 2-user
    * status: -1-error; 0-normal; 1-success
     * @param msg
     * @param type
     * @param status
     * @return 
    */
    
    /*
    public static void init() {
        if(!init){
            init = true;
            LinkedHashMap objMap = new LinkedHashMap<>();
            objMap.put("name", "init");
            objMap.put("src", "static/bg/img1.jpg");
            getUrlList().add(objMap.clone());
            getUrlList().add(objMap.clone());
            objMap.clear();
        }
    }
    */
    
    public static int getUrlListSize() {
        return getUrlList().size();
    }
    
    public static int getUrlprePublishListSize() {
        return getUrlprePublishList().size();
    }
    
    public static boolean msgPublisher(String msg,int type,int status){
        if(type == 0){
            switch(status){
                default:
                    msgpublisher.adminMsgBC(msg, 10);
                    break;
                case 1:
                    msgpublisher.adminMsgBC(msg, 11);
                    break;
                case 0:
                    msgpublisher.adminMsgBC(msg, 12);
                    break;
                case -1:
                    msgpublisher.adminMsgBC(msg, 13);
                    break;              
            }
           
        }
        if(type == 1){
            msgpublisher.guestMsgBC(msg, status);
        }
        return true;
    }
    
    public static boolean urlPublisher(String path,String name){
        LinkedHashMap objMap = new LinkedHashMap<>();
        String url = path.substring(MainServer.getWebrootPath().length()+1, path.length());
        objMap.put("name", name);
        objMap.put("src", url);
        getUrlList().add(objMap.clone());
        objMap.clear();
        Collections.reverse(getUrlList());
        msgPublisher("urlPublisher: "+getUrlList().size(),0,0);
        msgpublisher.guestMsgBC(JSON.toJSONString(getUrlList()), 101);
        Collections.reverse(getUrlList());
        return true;
    }
    
    public static boolean urlListPublisher(String folder,String name){
        LinkedHashMap objMap = new LinkedHashMap<>();
        String url;
        File[] fileList = new File(folder).listFiles();
        //msgPublish.msgPublisher("fileList.length"+fileList.length,0,1);
        if(fileList!=null){
            for (File file : fileList) {
                url = file.getAbsolutePath().substring(MainServer.getWebrootPath().length()+1, file.getAbsolutePath().length());
                objMap.put("name", name);
                objMap.put("src", url);
                getUrlList().add(objMap.clone());
                objMap.clear();
            }
        }         
        //msgPublish.msgPublisher("urlList.size()"+getUrlList().size(),0,1);
        Collections.reverse(getUrlList());
        msgPublisher("urlListPublisher: "+getUrlList().size(),0,0);
        msgpublisher.guestMsgBC(JSON.toJSONString(getUrlList()), 101);
        Collections.reverse(getUrlList());
        return true;
    }
    
    public static boolean unkonwedShipListPublisher(String folder){
        LinkedHashMap objMap = new LinkedHashMap<>();
        String url;
        File[] fileList = new File(folder).listFiles();
        String files[] = {"5.jpg","7.jpg","17.png","19.png"};
        //msgPublish.msgPublisher("fileList.length"+fileList.length,0,1);
        if(fileList!=null){
            String folderName = new File(folder).getName();
            for(String file:files){
                objMap.put("name", folderName);
                objMap.put("src", folder+FILESEPARATOR+file);
                getUrlList().add(objMap.clone());
                objMap.clear();
            }
        }         
        //msgPublish.msgPublisher("urlList.size()"+getUrlList().size(),0,1);
        Collections.reverse(getUrlList());
        msgPublisher("unkonwedShipListPublisher: "+getUrlList().size(),0,0);
        msgpublisher.guestMsgBC(JSON.toJSONString(getUrlList()), 101);
        Collections.reverse(getUrlList());
        return true;
    }
    
    
    //folder=localpath+File.separator+"temp"+File.separator+"ffoutput"+File.separator+"maps";
    public static boolean mapurlListPublisher(String folder){
        if(!isAllowPublish()){
            return false;
        }
        LinkedHashMap objMap = new LinkedHashMap<>();
        String url;
        File[] fileList = new File(folder).listFiles(); 
        if(fileList!=null){
            for (File mapfolder : fileList) { //mapfolder=map_x.swf
                if(mapfolder.isDirectory()){
                    File frames = new File(mapfolder.getAbsolutePath()+FILESEPARATOR+"frames");
                    if(frames.exists()){
                            String mapPath = frames.getAbsolutePath()+FILESEPARATOR+"2.png";
                            if(new File(mapPath).exists()){
                                url = mapPath.substring(MainServer.getWebrootPath().length()+1,mapPath.length());
                                objMap.put("name", mapfolder.getName());
                                objMap.put("src", url);
                                getUrlList().add(objMap.clone());
                                getUrlprePublishList().add(objMap.clone());
                                objMap.clear();
                            }
                    }
                    //msgPublisher("mapurlListPublisher当前定位到的frames文件夹真实地址为: "+frames.getAbsolutePath(),0,0);
                }
            }
        }         
        Collections.reverse(getUrlList());
        msgPublisher("mapurlListPublisher: "+getUrlList().size(),0,0);
        msgpublisher.guestMsgBC(JSON.toJSONString(getUrlList()), 101);
        Collections.reverse(getUrlList());
        return true;
    }
    
    public static boolean urlPrePublisher(String path,String name){
        LinkedHashMap objMap = new LinkedHashMap<>();
        String url = path.substring(MainServer.getWebrootPath().length()+1, path.length());
        objMap.put("name", name);
        objMap.put("src", url);
        getUrlprePublishList().add(objMap.clone());
        objMap.clear();
        return true;
    }
    
    //folder=localpath+File.separator+"temp"+File.separator+"ffoutput"+File.separator+"ships";
    public static boolean shipPrePublisher(String folder){
        
        String url;
        //String imagepath;
        LinkedHashMap objMap = new LinkedHashMap<>();
        File[] fileList = new File(folder).listFiles(); 
        if(fileList!=null){
            for (File ship : fileList) { //mapfolder=mapx.swf
                /*
                msgPublisher("shipPrePublisher当前定位到的shipfolder文件夹真实地址为: "+shipfolder.getAbsolutePath(),0,0);
                File imagesfolder = new File(shipfolder.getAbsolutePath() + FILESEPARATOR + "images");
                if(imagesfolder.exists() && imagesfolder.isDirectory()){
                    File[] imageList = imagesfolder.listFiles(); 
                    for (File image : imageList) {
                        for(String rule:shiprule){
                            
                        }
                        imagepath = image.getAbsolutePath();
                        url = imagepath.substring(MainServer.getWebrootPath().length()+1,imagepath.length());
                        objMap.put("name", shipfolder.getName());
                        objMap.put("src", url);
                        getUrlprePublishList().add(objMap.clone());
                        objMap.clear();
                    }
                }
                */
                url = ship.getAbsolutePath().substring(MainServer.getWebrootPath().length()+1, ship.getAbsolutePath().length());
                objMap.put("name", ship.getName());
                objMap.put("src", url);
                getUrlprePublishList().add(objMap.clone());
                objMap.clear();
            }
        }         
        return true;
    }
    
    public static boolean urlOnPublisher(){
        allowPublish = false;
        Collections.reverse(getUrlprePublishList());
        msgPublisher("urlOnPublisher: "+getUrlprePublishList().size(),0,0);
        msgpublisher.guestMsgBC(JSON.toJSONString(getUrlprePublishList()), 101);
        Collections.reverse(getUrlprePublishList());
        return true;
    }
    
    public static boolean cleanArrayList(){
        getUrlList().clear();
        getUrlprePublishList().clear();
        //init = false;
        //init();
        return true;
    } 

    /**
     * @return the allowPublish
     */
    public static boolean isAllowPublish() {
        return allowPublish;
    }

    /**
     * @return the urlprePublishList
     */
    public static List getUrlprePublishList() {
        return urlprePublishList;
    }

    /**
     * @return the urlList
     */
    public static List getUrlList() {
        return urlList;
    }
}
