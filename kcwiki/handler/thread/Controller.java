/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.handler.thread;

import java.util.HashMap;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.initializer.Start2DataThread;
import moe.kcwiki.handler.massage.msgPublish;

/**
 *
 * @author iTeam_VEP
 */
public class Controller {

    /**
     * @return the isPoolInit
     */
    public static boolean isIsPoolInit() {
        return isPoolInit;
    }
    
    private static boolean isPoolInit = false;
    
    private static final Controller poolController = new Controller();  
    private Controller() {}  

    public static Controller getInstance() {  
        /*
        if(!isPoolInit){
            isPoolInit = true;
            ininPool();
        }
        */
        return poolController;  
    } 
    
    public static boolean ininPool(){
        if(isPoolInit){
            return false;
        }
        MainServer.setStopScanner(false);
        modifieddataPool.setIsOnline(false);
        start2dataPool.setIsOnline(false);
        Start2DataThread.setHasStart2(false);
        corePool.ininPool();
        modifieddataPool.initPool();
        start2dataPool.ininPool();
        msgPublish.cleanArrayList();
        DBCenter.reset();
        isPoolInit = true;
        return true;
    }
    
    public boolean shutdownNow(){
        if(!isIsPoolInit()){
            return false;
        }
        isPoolInit = false;
        try{
            corePool.shutdownNow();
            modifieddataPool.shutdownNow();
            start2dataPool.shutdownNow();
            //verifyPool.shutdownNow();
        }catch(Exception e){
            msgPublish.msgPublisher("moe.kcwiki.threadpool-Controller-shutdownNow 出现Exception错误",0,0);
            return false;
        }
        return true;
    }
    
    public boolean shutdown(){
        if(!isIsPoolInit()){
            return false;
        }
        isPoolInit = false;
        try{
            corePool.shutdown();
            modifieddataPool.shutdown();
            start2dataPool.shutdown();
            //verifyPool.shutdown();
        }catch(Exception e){
            msgPublish.msgPublisher("moe.kcwiki.threadpool-Controller-shutdown 出现Exception错误",0,0);
            return false;
        }
        return true;
    }
    
    public boolean takeTask(){
        
        return true;
    }
    
    public HashMap isTerminated(){
        HashMap<String,String> responseList = new HashMap<>();
        responseList.put("corePool",String.valueOf(corePool.isTerminated()));
        responseList.put("modifieddataPool",String.valueOf(modifieddataPool.isTerminated()));
        responseList.put("start2dataPool",String.valueOf(start2dataPool.isTerminated()));
        responseList.put("getUnkownShipPool",String.valueOf(getUnkownShipPool.isTerminated()));
        responseList.put("getUnkownSlotitemPool",String.valueOf(getUnkownSlotitemPool.isTerminated()));
        //responseList.put("verifyPool",String.valueOf(verifyPool.isTerminated()));
        corePool.takeTask();
        return (HashMap) responseList.clone();
    }
    
    
}
