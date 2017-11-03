/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.unpackswf;

import moe.kcwiki.init.MainServer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;
import static moe.kcwiki.tools.constant.FILESEPARATOR;

/**
 *
 * @author x5171
 */
public class UnpackSwf {
    
    private final String ffdecpath = MainServer.getFfdecFolder();
    private boolean isInit = false;
    
    public synchronized boolean callShell(String shellString){
        try {
            //msgPublish.msgPublisher(shellString+"准备执行完毕",0,0);
            if(shellString == null || shellString.length() == 0) {return false;}
            
            String[] cmd = { "sh", "-c", shellString };
            try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getWorksFolder()+FILESEPARATOR+"ShellLogs.txt"),true), "UTF-8"))) {
                Process pcs=Runtime.getRuntime().exec(cmd);
                String line;  
                try (BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()))) {
                    eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);
                    eBfw.write("shellString: \t "+shellString+constant.LINESEPARATOR);
                    while ((line = br.readLine()) != null){
                        eBfw.write(line+constant.LINESEPARATOR);
                    }
                    eBfw.write("</OUTPUT>"+constant.LINESEPARATOR);
                    if(pcs.waitFor(180, TimeUnit.SECONDS)){
                        return true;
                    } else {
                        pcs.destroyForcibly();
                        msgPublish.msgPublisher("UnpackSwf-callShell模块没有在规定时间内执行完毕，已强行终止。脚本为： " + shellString,0,-1);
                    }
                }
            }
            //msgPublish.msgPublisher(shellString+"执行完毕",0,0);
        } catch (IOException | InterruptedException ex) {
            msgPublish.msgPublisher("UnpackSwf-callShell模块发生IOException | InterruptedException 异常。",0,-1);
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean createDiffFile(String folder,String file) {
        if(isInit){
            return false;
        }
        File[] fileList = new File(file).listFiles();
        for(File swf:fileList){
            if(swf.isFile() && swf.getName().contains(".swf")) {
                if(swf.getName().toLowerCase().contains("core.swf")){
                    continue;
                }
                callShell("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+"\" \""+file+"\"");
            }
        }
        isInit = true;
        return true;
    }
    
    //解压各种游戏swf和批量解压文件夹
    public boolean ffdec(String folder,String file){
        //msgPublish.msgPublisher("ffdec 追踪： folder-"+folder+"\tfile: "+file,0,0);
        return callShell("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+"\" \""+file+"\"");
        
        /* 
        try { 
                try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+FILESEPARATOR+"ffdeclog.txt"),true), "UTF-8"))) {
                    //http://www.cnblogs.com/nkxyf/archive/2012/12/13/2815978.html
                    Process pcs=Runtime.getRuntime().exec("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+"\" \""+file+"\""+" && exit");
                    BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));  
                    String line;  
                    eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);  
                    while ((line = br.readLine()) != null)  
                        eBfw.write(line+constant.LINESEPARATOR);  
                    eBfw.write("</OUTPUT>"+constant.LINESEPARATOR); 

                    int exitVal = pcs.waitFor();  
                    eBfw.write("Process exitValue: " + exitVal+constant.LINESEPARATOR+constant.LINESEPARATOR);  
                } catch (InterruptedException ex) {
                    Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        } catch (IOException ex) {
            msgPublish.msgPublisher("IOException，FFdec模块解包失败。",0,-1);
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }*/
    }
    
    //一次性解压所有下载完的地图
    public boolean maps(String folder,String file){
        return callShell("java -jar \""+ffdecpath+"\" -export image,frame \""+folder+"\" \""+file+"\"");
        /*            
        try {
                msgPublish.msgPublisher("开始进行maps解包···",0,0);
                //http://www.cnblogs.com/nkxyf/archive/2012/12/13/2815978.html
                try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+FILESEPARATOR+"ffdeclog.txt"),true), "UTF-8"))) {
                    //http://www.cnblogs.com/nkxyf/archive/2012/12/13/2815978.html
                    Process pcs=Runtime.getRuntime().exec("java -jar \""+ffdecpath+"\" -export image,frame \""+folder+"\" \""+file+"\""+" && exit");
                    BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));  
                    String line;  
                    eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);  
                    while ((line = br.readLine()) != null)  
                        eBfw.write(line+constant.LINESEPARATOR);  
                    eBfw.write("</OUTPUT>"+constant.LINESEPARATOR); 

                    int exitVal = pcs.waitFor();  
                    eBfw.write("Process exitValue: " + exitVal+constant.LINESEPARATOR+constant.LINESEPARATOR);  
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
                    msgPublish.msgPublisher("InterruptedException，FFdec-Maps模块解包失败。",0,-1);
                }
                msgPublish.mapurlListPublisher(folder, "map");
                return true;
        } catch (IOException ex) {
            msgPublish.msgPublisher("IOException，FFdec-Maps模块解包失败。",0,-1);
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        */
    }
    
    //Start2DataThread
    public boolean unpackStart2(String folder,String file){
        msgPublish.msgPublisher("开始进行Start2Data解包···",0,0);
        /*
        fout = new FileWriter(temppath+FILESEPARATOR+"ffdec.bat");
        fout.write("@echo off"+constant.LINESEPARATOR);
        */
        if(!DBCenter.NewShipDB.isEmpty()){
            callShell("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\"");
            /*
            //fout.write("@java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\""+constant.LINESEPARATOR);
            try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+FILESEPARATOR+"ffdeclog.txt"),true), "UTF-8"))) {
            
            Process pcs=Runtime.getRuntime().exec("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"ships\""+" && exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
            String line;
            eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);
            while ((line = br.readLine()) != null)
            eBfw.write(line+constant.LINESEPARATOR);
            eBfw.write("</OUTPUT>"+constant.LINESEPARATOR);
            
            int exitVal = pcs.waitFor();
            eBfw.write("Process exitValue: " + exitVal+constant.LINESEPARATOR+constant.LINESEPARATOR);
            
            } catch (InterruptedException ex) {
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
            msgPublish.msgPublisher("InterruptedException，unpackStart2-NewShip模块解包失败。",0,-1);
            }
            */
        }
        if(!DBCenter.NewMapinfoDB.isEmpty()){
            //fout.write("@java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"map\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"map\""+constant.LINESEPARATOR);
            callShell("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"map\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"map\"");
            /*
            try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+FILESEPARATOR+"ffdeclog.txt"),true), "UTF-8"))) {
            
            Process pcs=Runtime.getRuntime().exec("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf"+FILESEPARATOR+"map\" \""+file+FILESEPARATOR+"swf"+FILESEPARATOR+"map\""+" && exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
            String line;
            eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);
            while ((line = br.readLine()) != null)
            eBfw.write(line+constant.LINESEPARATOR);
            eBfw.write("</OUTPUT>"+constant.LINESEPARATOR);
            
            int exitVal = pcs.waitFor();
            eBfw.write("Process exitValue: " + exitVal+constant.LINESEPARATOR+constant.LINESEPARATOR);
            
            } catch (InterruptedException ex) {
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
            msgPublish.msgPublisher("InterruptedException，unpackStart2-NewMapinfo模块解包失败。",0,-1);
            }
            */
        }
        if(!DBCenter.NewMapBgm.isEmpty()){
            //fout.write("@java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf\" \""+file+FILESEPARATOR+"swf\""+constant.LINESEPARATOR);
            callShell("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf\" \""+file+FILESEPARATOR+"swf\"");
            /*
            try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+FILESEPARATOR+"ffdeclog.txt"),true), "UTF-8"))) {
            
            Process pcs=Runtime.getRuntime().exec("java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+FILESEPARATOR+"swf\" \""+file+FILESEPARATOR+"swf\""+" && exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
            String line;
            eBfw.write("<OUTPUT>"+constant.LINESEPARATOR);
            while ((line = br.readLine()) != null)
            eBfw.write(line+constant.LINESEPARATOR);
            eBfw.write("</OUTPUT>"+constant.LINESEPARATOR);
            
            int exitVal = pcs.waitFor();
            eBfw.write("Process exitValue: " + exitVal+constant.LINESEPARATOR+constant.LINESEPARATOR);
            
            } catch (InterruptedException ex) {
            Logger.getLogger(UnpackSwf.class.getName()).log(Level.SEVERE, null, ex);
            msgPublish.msgPublisher("InterruptedException，unpackStart2-NewMapBgm模块解包失败。",0,-1);
            }
            */
        }
        /*
        if(!DBCenter.FurnitureDB.isEmpty()){
        fout.write("@java -jar \""+ffdecpath+"\" -export script,image,sound \""+folder+"\" \""+file+"\""+" && del %0 >nul && exit"+constant.LINESEPARATOR+constant.LINESEPARATOR);
        }
        
        fout.write("@del %0 >nul && exit"+constant.LINESEPARATOR);
        fout.close();
        Runtime.getRuntime().exec("cmd.exe /c start \"SWF解包模块运行中\" \""+temppath+FILESEPARATOR+"ffdec.bat\"");
        */
        return true;
    }
}
