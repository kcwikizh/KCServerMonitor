/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.initializer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.tools.compressor.ZipCompressor;
import moe.kcwiki.handler.massage.msgPublish;
import static moe.kcwiki.handler.massage.msgPublish.getUrlprePublishList;
import moe.kcwiki.handler.thread.Controller;
import moe.kcwiki.handler.thread.start2dataPool;
import moe.kcwiki.tools.constant.constant;
import static moe.kcwiki.tools.constant.constant.FILESEPARATOR;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;

/**
 *
 * @author x5171
 */
public class Start2DataThread {
    private Thread Start2DataThread;
    //private static boolean isFinish=false;
    private static boolean hasStart2=false;
    private static int Threadcounter=0;
    private static int counter=0;
    
    public synchronized boolean StartThread(){
        
        Start2DataThread = new Thread() { 
            @Override
            public void run() {
                //MainGui.btnctl(2,false);
                start2dataPool.setIsOnline(true);
                msgPublish.msgPublisher(new Date() +constant.LINESEPARATOR+"获取新数据\t主线程开始运行",0,1);
                try {
                    if(MainServer.isDebugMode()){
                        new moe.kcwiki.monitor.start2.Start2Analyzer().ReadNewFile(null);
                    }else if(!new moe.kcwiki.monitor.start2.GetStart2().netStart()){
                        msgPublish.msgPublisher("netStart运行出错，已停止GetStart2\t子线程运行。",0,1);
                        return;
                    }
                        msgPublish.msgPublisher("hasStart2:"+hasStart2,0,0);
                        msgPublish.msgPublisher("GetStart2\t子线程运行结束",0,1);
                        MainServer.setStopScanner(true);
                        msgPublish.msgPublisher("盲扫线程已自动停止。",0,0);
                        new moe.kcwiki.generator.wikilua.MakeNewShipData().WriteFile();
                        new moe.kcwiki.generator.wikilua.MakeNewSlotitemData().WriteFile();
                        new moe.kcwiki.downloader.DLThread().start2();
                        //new moe.kcwiki.unpackswf.RenameShipSwf().shipSwf(MainServer.getDownloadFolder()+FILESEPARATOR+"resources"+FILESEPARATOR+"swf"+FILESEPARATOR+"ships");
                        
                        sleep(30*1000);
                        start2dataPool.takeTask();
                        msgPublish.msgPublisher("等待start2dataPool关闭中",0,0);
                        while(!start2dataPool.isTerminated()){
                            sleep(10*1000);
                        }
                        msgPublish.msgPublisher("准备执行-关闭所有线程池-操作。",0,0);
                        Controller.getInstance().shutdown();
                        //new moe.kcwiki.unpackswf.UnpackSwf().unpackStart2(MainServer.getTempFolder()+FILESEPARATOR+"unpackswf"+FILESEPARATOR+"kcs",MainServer.getDownloadFolder()+FILESEPARATOR+"resources");
                        if(!getUrlprePublishList().isEmpty()){
                            msgPublish.urlOnPublisher();
                        }
                        long date = new Date().getTime();
                        String tempZipFolder = MainServer.getPublishFolder()+FILESEPARATOR+date;
                        ZipCompressor.createZip(MainServer.getDownloadFolder(), tempZipFolder, "sourcefile.zip");
                        ZipCompressor.createZip(MainServer.getWorksFolder(), tempZipFolder, "editorialfile.zip");
                        ZipCompressor.createZip(MainServer.getLogFolder(), tempZipFolder, "logfile.zip");
                        if(MainServer.isDebugMode()){
                            ZipCompressor.createZip(tempZipFolder, MainServer.getMuseumFolder(), date+"-debug.zip");
                        } else {
                            ZipCompressor.createZip(tempZipFolder, MainServer.getMuseumFolder(), date+".zip");
                        }
                        MainServer.setZipFolder(date);
                        msgPublish.msgPublisher(new Date() +LINESEPARATOR+"获取新数据\t主线程运行结束",0,1);
                        //MainGui.btnctl(2,true);
                        start2dataPool.setIsOnline(false);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Start2DataThread.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                } catch (IOException ex) {
                    Logger.getLogger(Start2DataThread.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                } catch (Exception ex) {
                    Logger.getLogger(Start2DataThread.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
                
            }   
        };
        Start2DataThread.start();
        return true;
    }
      
    public static void addJob(){
        Threadcounter++;
    }
    
    public synchronized static void finishJob(){
        counter++;
    }
    
    public synchronized boolean StopThread(){
        Start2DataThread.stop();
        return true;
    }

    /**
     * @return the hasStart2
     */
    public static boolean isHasStart2() {
        return hasStart2;
    }

    /**
     * @param aHasStart2 the hasStart2 to set
     */
    public static void setHasStart2(boolean aHasStart2) {
        hasStart2 = aHasStart2;
    }
}
