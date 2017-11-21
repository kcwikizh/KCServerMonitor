/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.initializer;


import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.tools.swfunpacker.coredecryptor.CoreDecrypt;
import moe.kcwiki.monitor.file.GetUnkownShip;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.handler.thread.Controller;
import moe.kcwiki.handler.thread.corePool;
import moe.kcwiki.handler.thread.modifieddataPool;
import moe.kcwiki.tools.constant.constant;

/**
 *
 * @author x5171
 */
public class GetModifiedDataThread {
    private Thread GetModifiedDataThread;
    private static boolean isFinish=false;
    private static String timeStamp = "0";
    private static int counter=0;
    private static int Threadcounter=0;
    
    public synchronized boolean StartThread(){
        
        GetModifiedDataThread = new Thread() {
            @Override
            @SuppressWarnings("SleepWhileInLoop")
            public void run() {
                modifieddataPool.setIsOnline(true);
                msgPublish.msgPublisher(new Date() +constant.LINESEPARATOR+"扫描文件\t主线程开始运行",0,1);
                //MainGui.btnctl(1,false);
                try {
                    //new moe.kcwiki.getstart2data.Start2Analyzer().ReadNewFile(null);
                    timeStamp = String.valueOf(System.currentTimeMillis());
                    CoreDecrypt.setTimeStamp(timeStamp);
                    new moe.kcwiki.monitor.file.GetUnkownShip().setterSplitter();
                    new moe.kcwiki.monitor.file.GetUnknowSlotitem().getUnknowData();
                    try {
                        new moe.kcwiki.monitor.file.GetLastModifiedData().doMonitor();
                    } catch (IOException ex) {
                        Logger.getLogger(GetModifiedDataThread.class.getName()).log(Level.SEVERE, null, ex);
                        msgPublish.msgPublisher("moe.kcwiki.getmodifieddata-GetLastModifiedData-doMonitor 发生IOException错误。",0,-1);
                    }
                    
                    /*
                    int i=0;
                    while(counter<Threadcounter){   //6
                        i++;
                        if(i==18){
                            msgPublish.msgPublisher("GetModifiedData\t待结束子线程还有："+Integer.valueOf(Threadcounter-counter),0,0);
                            i=0;
                        }
                        sleep(10*1000);
                    }
                    */
                    sleep(30*1000);
                    
                    if(MainServer.isStopScanner()){
                        modifieddataPool.shutdownNow();
                    }else{
                        modifieddataPool.shutdown();
                        modifieddataPool.takeTask();
                    }
                    while(!modifieddataPool.isTerminated()){
                        sleep(5*1000);
                    }
                    msgPublish.msgPublisher("等待语音下载线程结束。",0,1);
                    corePool.takeTask();
                    msgPublish.msgPublisher(new Date() +constant.LINESEPARATOR+"扫描文件\t主线程运行结束",0,1);
                    //MainGui.btnctl(1,true);
                    if(Controller.isIsPoolInit()){
                        modifieddataPool.initPool();
                    }
                    modifieddataPool.setIsOnline(false);
                    isFinish=true;
                } catch (InterruptedException ex) {
                    Logger.getLogger(GetModifiedDataThread.class.getName()).log(Level.SEVERE, null, ex);
                    msgPublish.msgPublisher("GetModifiedDataThread 发生InterruptedException错误，已强制退出。",0,-1);
                    return ;
                }
            }   
        };  
        GetModifiedDataThread.start();
        return true;
    }
    
    public static void addJob(){
        Threadcounter++;
    }
    
    public synchronized static void finishJob(){
        counter++;
    }
    
    public synchronized boolean StopThread(){
        GetModifiedDataThread.stop();
        return true;
    }

    /**
     * @return the isFinish
     */
    public static boolean isIsFinish() {
        return isFinish;
    }

    /**
     * @return the timeStamp
     */
    public static String getTimeStamp() {
        return timeStamp;
    }
}
