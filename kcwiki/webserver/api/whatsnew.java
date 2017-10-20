/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.init.MainServer;
import static moe.kcwiki.tools.constant.LINESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class whatsnew {
    private StringBuilder sb = null;
    private static String file = MainServer.getWorksFolder()+File.separator+"Start2_Export.txt";
    
    public String getData() {
        sb = new StringBuilder();
        if(!new File(file).exists()){
            this.addString("<!DOCTYPE html><html><body>");
            this.addString("没有发现新文件或者抓包程序未运行，请等待后台抓包进程执行。");
            this.addString("</body></html>");
        } else {
            try {
                BufferedReader br=new BufferedReader( new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line="";
                this.addString(LINESEPARATOR);
                while ((line=br.readLine())!=null) {
                    this.addString(line);
                }       
                br.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(whatsnew.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(whatsnew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return sb.toString();
    }
    
    public void addData(String str) {
        if(sb == null){
            sb = new StringBuilder();
        }
        this.addString(str);
    }

    
    private void addString(String str) {
        sb.append(str + LINESEPARATOR + "</br>"+ LINESEPARATOR);
    }
}
