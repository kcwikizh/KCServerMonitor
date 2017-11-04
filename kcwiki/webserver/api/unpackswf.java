/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;
import moe.kcwiki.swfunpacker.UnpackSwf;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author iTeam_VEP
 */
public class unpackswf {
    private StringBuilder sb = null;
    
    public String getData(String src,String dest) {
        sb = new StringBuilder();
        new UnpackSwf().createDiffFile(dest,src);
        this.addString("");
        
        return sb.toString();
    }
    
    public boolean clear(String src) {
        File folder = new File(src);
        if(folder.exists()){
            try {
                FileUtils.deleteDirectory(folder);
                if(!folder.exists())
                    folder.mkdirs();
                return true;
            } catch (IOException ex) {
                Logger.getLogger(unpackswf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public void addData(String str) {
        if(sb == null){
            sb = new StringBuilder();
        }
        this.addString(str);
    }

    
    private void addString(String str) {
        sb.append(str).append(LINESEPARATOR).append("</br>").append(LINESEPARATOR);
    }
}
