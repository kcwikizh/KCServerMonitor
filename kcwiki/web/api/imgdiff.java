/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.api;

import com.alibaba.fastjson.JSON;
import java.io.File;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.initializer.MainServer;
import static moe.kcwiki.tools.constant.constant.FILESEPARATOR;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class imgdiff {
    private StringBuilder sb = null;
    private static final String urlprefix = "/custom/Publishing";
    
    public String getData() {
        sb = new StringBuilder();
        if(DBCenter.imgdiff.isEmpty()){
            this.addString("<!DOCTYPE html><html><body>");
            this.addString("没有发现差分图片或者抓包程序未运行，请等待后台抓包进程执行。");
            this.addString("</body></html>");
        } else {
            this.addString("<!DOCTYPE html><html><body>");
            this.addString(LINESEPARATOR);
            for (String folder:DBCenter.imgdiff) {
                File imgfolder = new File(folder);
                if(imgfolder.exists()){
                    this.addString("<h3>"+ imgfolder.getAbsolutePath().replace(MainServer.getPublishFolder(), "").replace("/currentswf/kcs/", "").replace("/images", "") +"</h3>");
                    File[] imgs = imgfolder.listFiles();
                    for(File img:imgs) {
                        this.addString("<img src=\""+urlprefix+img.getAbsolutePath().replace(MainServer.getPublishFolder(), "")+"\" />");
                    }
                    this.addString("-------------------------------------");
                }
                this.addString(LINESEPARATOR);
            }
            this.addString("</body></html>");
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
        sb.append(str).append(LINESEPARATOR).append("</br>").append(LINESEPARATOR);
    }
}
