/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.initializer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

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
        if(new File(MainServer.getTempFolder()).exists()){try {
            FileUtils.deleteDirectory(new File(MainServer.getTempFolder()));
            } catch (IOException ex) {
                Logger.getLogger(MainCore.class.getName()).log(Level.SEVERE, null, ex);
            }
}
        if(new File(MainServer.getDownloadFolder()).exists()){try {
            FileUtils.deleteDirectory(new File(MainServer.getDownloadFolder()));
            } catch (IOException ex) {
                Logger.getLogger(MainCore.class.getName()).log(Level.SEVERE, null, ex);
            }
}
        new File(MainServer.getTempFolder()).mkdirs();
        new File(MainServer.getDownloadFolder()).mkdirs();
    }

}
