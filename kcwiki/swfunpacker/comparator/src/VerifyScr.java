/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.swfunpacker.comparator.src;

import java.io.File;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static moe.kcwiki.database.DBCenter.swfSrcPatch;
import moe.kcwiki.initializer.GetModifiedDataThread;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.swfunpacker.comparator.image.GetHash;
import moe.kcwiki.handler.massage.msgPublish;

/**
 *
 * @author iTeam_VEP
 */
public class VerifyScr {
    private static String rootFolder = MainServer.getTempFolder()+File.separator+"currentswf";
    private static HashMap<String,Object> fileData = new LinkedHashMap<>();
    private static String newFile;
    private static String oldFile;
    
    public boolean verifyscr(String newFileFolder,String oldFileFolder) {
        new Thread() {  //创建新线程用于下载
            @Override
            public void run() {
                HashMap<String, String> scrDelList = new LinkedHashMap<>();
                newFile = newFileFolder;
                oldFile = oldFileFolder;
                int newScr=0;
                int oldScr;
                scrDelList.clear();
                
                if(new File(oldFileFolder).exists()){
                    scrDelList=readOldScr(oldFileFolder,scrDelList);
                    oldScr=scrDelList.size();
                    String filename=newFileFolder.substring(0, newFileFolder.lastIndexOf(File.separator));
                    newScr=readNewScr(newFileFolder,scrDelList,newScr);
                    
                    swfSrcPatch.put(newFileFolder.substring(rootFolder.length()+1, newFileFolder.length()), fileData.clone());
                    fileData.clear();
                    
                    msgPublish.msgPublisher(filename.substring(filename.lastIndexOf(File.separator)+1, filename.length())+"\t scr MD5互查对比完成，剩余文件："+(oldScr-newScr),0,0);
                    scrDelList.clear();
                    //String data = JSON.toJSONString(swfSrcPatch,SerializerFeature.BrowserCompatible);
                }    
            }
        }.start();
        return true;
    }
    
    public static HashMap readOldScr(String filepath,HashMap scrDelList)  {
        File file = new File(filepath);
        GetHash getHash = new GetHash();
        if (!file.isDirectory()) {
            scrDelList.put(getHash.getNewHash(file.getAbsolutePath()),file.getAbsolutePath());
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + File.separator + filelist[i]);
                if (!readfile.isDirectory()) {
                    scrDelList.put(new GetHash().getNewHash(readfile.getAbsolutePath()),readfile.getAbsolutePath());
                } else if (readfile.isDirectory()) {
                    scrDelList.putAll(readOldScr(filepath + File.separator + filelist[i],scrDelList));
                }
            }
        }
        //System.out.println(scrDelList);
        return scrDelList;
    }
    
    public static int readNewScr(String filepath,HashMap<String,String> scrDelList,int count) {
        scrdiff srcdiffer = new scrdiff();
        File file = new File(filepath);
        GetHash getHash = new GetHash();
        String newFilePath = file.getAbsolutePath();
        if (!file.isDirectory()) {
            if(scrDelList.get(getHash.getNewHash(newFilePath)) != null){
                new File(newFilePath).delete();
                count++;
            }else{
                String oldFilePath = oldFile + File.separator + newFilePath.substring(newFile.length()+1, newFilePath.length());
                HashMap tmp = srcdiffer.differ(oldFilePath,newFilePath);
                if(!tmp.isEmpty())
                    fileData.put(newFilePath.substring(rootFolder.length()+1, newFilePath.length()), tmp.clone());
            }
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + File.separator + filelist[i]);
                if (!readfile.isDirectory()) {
                    newFilePath = readfile.getAbsolutePath();
                    if(scrDelList.get(getHash.getNewHash(newFilePath)) != null){
                        new File(newFilePath).delete();
                        count++;
                    }else{
                        String oldFilePath = oldFile + File.separator + newFilePath.substring(newFile.length()+1, newFilePath.length());
                        HashMap tmp = srcdiffer.differ(oldFilePath,newFilePath);
                        if(!tmp.isEmpty())
                            fileData.put(newFilePath.substring(rootFolder.length()+1, newFilePath.length()), tmp.clone());
                    }
                    //String tempfolder=readfile.getAbsolutePath().substring(0,readfile.getAbsolutePath().lastIndexOf(File.separator) );
                    //count=count+delFiles(tempfolder,scrDelList);
                } else if (readfile.isDirectory()) {
                    count=readNewScr(filepath + File.separator + filelist[i],scrDelList,count);
                }
            }
            if(file.exists()){
                File[] fileList = file.listFiles();
                if(fileList.length==0){
                    file.delete();
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
    
    /*public static void main(String[] args) {
        MainServer.setTempFolder("C:\\Users\\VEP\\Desktop\\test\\test");
        rootFolder = MainServer.getTempFolder()+File.separator+"currentswf";
        try {
            //new Controller().Analysis("C:\\Users\\VEP\\Desktop\\test\\test\\Core.swf", "C:\\Users\\VEP\\Desktop\\test\\test", "null");
            new VerifyScr().verifyscr("C:\\Users\\VEP\\Desktop\\test\\test\\currentswf\\new\\scripts", "C:\\Users\\VEP\\Desktop\\test\\test\\currentswf\\old\\scripts");
        } catch (Exception ex) {
            Logger.getLogger(VerifyScr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
}
