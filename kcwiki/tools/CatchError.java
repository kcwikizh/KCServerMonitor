/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;
import moe.kcwiki.init.MainServer;
import moe.kcwiki.massagehandler.msgPublish;
import static moe.kcwiki.tools.constant.FILESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class CatchError {
    private static PrintWriter pw;
    private static Date time;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");//可以方便地修改日期格式;
    private static boolean init = false;
    private static final AtomicInteger count = new AtomicInteger(1);
    
    public boolean init(){
        if(init){return false;}
        /*
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) ;
        String realPath = path.substring(firstIndex, lastIndex);
        String localpath = java.net.URLDecoder.decode(realPath, "utf-8");
        */
        
        String logpath = MainServer.getLogPath();
        try {
            time = new Date();
            new File(logpath).mkdirs();
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(logpath+File.separator+dateFormat.format( time )+".txt")), "UTF-8"), true) ;
            time = new Date();
            pw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+dateFormat.format( time )+constant.LINESEPARATOR+"开始运行"+constant.LINESEPARATOR);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(CatchError.class.getName()).log(Level.SEVERE, null, ex);
        }
        init = true;
        return true;
    }
    
    public static void WriteError(String error){
        time = new Date(); 

        pw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+dateFormat.format( time ));
        pw.write(constant.LINESEPARATOR+error+constant.LINESEPARATOR);
    }
    
    public static void SaveLog(String TaskName){
        time = new Date();
        pw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+dateFormat.format( time )+constant.LINESEPARATOR+
                "当前保存次数为： "+count.getAndIncrement()+constant.LINESEPARATOR+
                "主线程： "+TaskName+"结束运行。"+constant.LINESEPARATOR);
        pw.close();
    }
    
}
