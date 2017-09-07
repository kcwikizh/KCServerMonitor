/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.init;

import java.io.File;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet; 

/**
 *
 * @author user12
 */
public class MainCore {
    
    /*
    public void init(){
        MainServer.init();
    }
    */
    
    public void cleanup(){
        if(new File(MainServer.getTempFolder()).exists()){FileExerciser.delFolder(MainServer.getTempFolder());}
        if(new File(MainServer.getDownloadFolder()).exists()){FileExerciser.delFolder(MainServer.getDownloadFolder());}
        new File(MainServer.getTempFolder()).mkdirs();
        new File(MainServer.getDownloadFolder()).mkdirs();
    }

}
