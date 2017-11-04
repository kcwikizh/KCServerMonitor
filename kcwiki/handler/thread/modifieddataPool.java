/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.handler.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.daemon.CatchError;

/**
 *
 * @author iTeam_VEP
 */
public class modifieddataPool {

    /**
     * @return the isInit
     */
    public static boolean isIsInit() {
        return isInit;
    }
    private static  ExecutorService cachedThreadPool;  
    private static CompletionService<Integer> cs ;
    private static HashMap<Integer,String> taskList;
    private static AtomicInteger taskNum ;
    private static boolean isInit = false;
    private static boolean isOnline = false;
    
    /**
     * @return the cs
     */
    public static CompletionService<Integer> getCs() {
        return cs;
    }

    /**
     * @param task
     * @param taskID
     * @param taskName
     * @param cs the cs to set
     */
    public static void addTask(Callable<Integer> task,int taskID,String taskName) {
        if(!isIsInit()){
            return;
        }
        cs.submit(task);
        taskList.put(taskID, taskName);
        taskNum.getAndIncrement();
    }
    
    public static void shutdown(){
        if(!isIsInit()){
            return;
        }
        isInit = false;
        cachedThreadPool.shutdown();
        getUnkownShipPool.shutdown();
        getUnkownSlotitemPool.shutdown();
        CatchError.SaveLog("getmodifieddata");
    }
    
    public static void shutdownNow(){
        if(!isIsInit()){
            return;
        }
        isInit = false;
        cachedThreadPool.shutdownNow();
        getUnkownShipPool.shutdownNow();
        getUnkownSlotitemPool.shutdownNow();
        CatchError.SaveLog("getmodifieddata");
    }
    
    public static boolean isTerminated(){
        return cachedThreadPool.isTerminated();
    }
    
    /**
     * @return the taskList
     */
    public static HashMap<Integer,String> getTaskList() {
        return taskList;
    }

    /**
     * @return the taskNum
     */
    public static int getTaskNum() {
        return taskNum.get();
    }
    
    public static boolean takeTask(){
                getUnkownShipPool.takeTask();
                getUnkownSlotitemPool.takeTask();
                for(int taskid=0;taskid<taskNum.get();){
                    try {
                        Future<Integer> task=cs.take();
                        if(task.get() != null){
                            msgPublish.msgPublisher("modifieddataPool-takeTask反馈消息： "+taskList.get(task.get())+"\t运行结束",0,1);
                            taskid++;
                        }
                    } catch (InterruptedException ex) {
                        msgPublish.msgPublisher("modifieddataPool-takeTask 发生InterruptedException错误",0,-1);
                        Logger.getLogger(modifieddataPool.class.getName()).log(Level.SEVERE, null, ex);
                        taskid++;
                    } catch (ExecutionException ex) {
                        msgPublish.msgPublisher("modifieddataPool-takeTask 发生ExecutionException错误",0,-1);
                        Logger.getLogger(modifieddataPool.class.getName()).log(Level.SEVERE, null, ex);
                        taskid++;
                    }
                }
                
        return true;
    }
    
    public static boolean initPool(){
        if(isIsInit()){
            return false;
        }
        cachedThreadPool = Executors.newCachedThreadPool();  
        cs = new ExecutorCompletionService<>(cachedThreadPool);
        isInit = true;
        taskList = new HashMap<>();
        taskNum = new AtomicInteger(0);
        getUnkownShipPool.ininPool();
        getUnkownSlotitemPool.ininPool();
        msgPublish.msgPublisher("modifieddataPool-初始化成功",0,0); 
        isInit = true;
        return true;
    }

    /**
     * @return the isOnline
     */
    public static boolean isIsOnline() {
        return isOnline;
    }

    /**
     * @param aIsOnline the isOnline to set
     */
    public static void setIsOnline(boolean aIsOnline) {
        isOnline = aIsOnline;
    }
}
