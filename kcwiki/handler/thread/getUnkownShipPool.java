/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.handler.thread;

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

/**
 *
 * @author iTeam_VEP
 */
public class getUnkownShipPool  {
    private static  ExecutorService cachedThreadPool;  
    private static CompletionService<Integer> cs ;
    private static HashMap<Integer,String> taskList;
    private static AtomicInteger taskNum ;
    private static boolean isInit = false;
    
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
        if(!isInit){
            return;
        }
        cs.submit(task);
        taskList.put(taskID, taskName);
        taskNum.getAndIncrement();
    }
    
    public static void shutdown(){
        if(!isInit){
            return;
        }
        isInit = false;
        cachedThreadPool.shutdown();
    }
    
    public static void shutdownNow(){
        if(!isInit){
            return;
        }
        isInit = false;
        cachedThreadPool.shutdownNow();
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
        final int taskID = corePool.getTaskNum();
        modifieddataPool.addTask(new Callable<Integer>() {
            @Override
            public Integer call() {
                for(int taskid=0;taskid<taskNum.get();){
                    try {
                        Future<Integer> task=cs.take();
                        if(task.get() != null){
                            msgPublish.msgPublisher("getUnkownShipPool-takeTask反馈消息： "+taskList.get(task.get())+"\t运行结束",0,1);
                            taskid++;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(getUnkownSlotitemPool.class.getName()).log(Level.SEVERE, null, ex);
                        msgPublish.msgPublisher("getUnkownShipPool-takeTask 已强制退出。",0,0);
                        shutdownNow();
                        return taskID;
                    } catch (ExecutionException ex) {
                        Logger.getLogger(getUnkownSlotitemPool.class.getName()).log(Level.SEVERE, null, ex);
                        msgPublish.msgPublisher("getUnkownShipPool-takeTask 发生ExecutionException错误。",0,0);
                        shutdownNow();
                        return taskID;
                    }
                }
                return taskID;
            }
        },taskID,"getUnkownShipPool-Finished");
        return true;
    }
    
    public static boolean ininPool(){
        if(isInit){
            return false;
        }
        cachedThreadPool = Executors.newCachedThreadPool();  
        cs = new ExecutorCompletionService<>(cachedThreadPool);
        isInit = true;
        taskList = new HashMap<>();
        taskNum = new AtomicInteger(0);
        msgPublish.msgPublisher("getUnkownShipPool-初始化成功",0,0); 
        return true;
    }
}
