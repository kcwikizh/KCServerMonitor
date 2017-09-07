/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.unpackswf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.init.GetModifiedDataThread;
import moe.kcwiki.picscanner.GetHash;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;

/**
 *
 * @author iTeam_VEP
 */
public class VerifyScr {
    
    public boolean verifyscr(String newFileFolder,String oldFileFolder) {
        GetModifiedDataThread.addJob();
        new Thread() {  //创建新线程用于下载
            @Override
            public void run() {
                HashMap<String, String> scrDelList = new LinkedHashMap<>();
                int newScr=0;
                int oldScr;
                scrDelList.clear();
                try {
                    
                    if(new File(oldFileFolder).exists()){
                        scrDelList=readOldScr(oldFileFolder,scrDelList);
                        oldScr=scrDelList.size();
                        newScr=readNewScr(newFileFolder,scrDelList,newScr);
                        
                        String filename=oldFileFolder.substring(0, oldFileFolder.lastIndexOf(File.separator));
                        msgPublish.msgPublisher(filename.substring(filename.lastIndexOf(File.separator)+1, filename.length())+"\t scr MD5互查对比完成，剩余文件："+(oldScr-newScr),0,0);
                        sleep(1*1000);
                        scrDelList.clear();
                    }
                    GetModifiedDataThread.finishJob();
                } catch (InterruptedException ex) {
                    Logger.getLogger(moe.kcwiki.picscanner.Controller.class.getName()).log(Level.SEVERE, null, ex);
                    msgPublish.msgPublisher("对比脚本失败。",0,-1);
                    return ;
                }    
            }
        }.start();
        return true;
    }
    
    public static HashMap readOldScr(String filepath,HashMap scrDelList)  {
        File file = new File(filepath);
        if (!file.isDirectory()) {
            scrDelList.put(new GetHash().getNewHash(file.getAbsolutePath()),file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1, file.getAbsolutePath().length()));
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + File.separator + filelist[i]);
                if (!readfile.isDirectory()) {
                    scrDelList.put(new GetHash().getNewHash(readfile.getAbsolutePath()),readfile.getAbsolutePath().substring(readfile.getAbsolutePath().lastIndexOf(File.separator)+1, readfile.getAbsolutePath().length()));
                } else if (readfile.isDirectory()) {
                    scrDelList.putAll(readOldScr(filepath + File.separator + filelist[i],scrDelList));
                }
            }
        }
        //System.out.println(scrDelList);
        return scrDelList;
    }
    
    public static int readNewScr(String filepath,HashMap scrDelList,int count) {
        File file = new File(filepath);
        if (!file.isDirectory()) {
            if(scrDelList.get(new GetHash().getNewHash(file.getAbsolutePath()))!=null){
                new File(file.getAbsolutePath()).delete();
                count++;
            }
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + File.separator + filelist[i]);
                if (!readfile.isDirectory()) {
                    String tempfolder=readfile.getAbsolutePath().substring(0,readfile.getAbsolutePath().lastIndexOf(File.separator) );
                    count=count+delFiles(tempfolder,scrDelList);
                } else if (readfile.isDirectory()) {
                    count=readNewScr(filepath + File.separator + filelist[i],scrDelList,count);
                }
                
                if(file.exists()){
                    File[] fileList = file.listFiles();
                    if(fileList.length==0){
                        readfile.delete();
                    }
                }
            }
        }
        return count;
    }
    
    public static int delFiles(String folder,HashMap scrDelList){
        int count=0;
        if(!new File(folder).exists()){return 0;}
        File[] fileList = new File(folder).listFiles();
        for (File fileList1 : fileList) {
            if(scrDelList.get(new GetHash().getNewHash(folder+File.separator + fileList1.getName()))!=null){
                fileList1.delete();
                count++;
            }
        }
        fileList = new File(folder).listFiles();
        if(fileList.length==0){
            new File(folder).delete();
        }
        return count;
    }
}
