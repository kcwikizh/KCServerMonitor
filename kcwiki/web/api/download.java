/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.api;

import java.io.File;
import moe.kcwiki.initializer.MainServer;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class download {
    private StringBuilder sb = null;
    private static final String urlprefix = "/custom/Publishing/";
    
    public String getData() {
        sb = new StringBuilder();
        Long date = MainServer.getDate();
        File ZipFolder = new File(MainServer.getPublishFolder()+File.separator+date);
        if(!ZipFolder.exists() || ZipFolder.list().length == 0) {
                this.addString("<!DOCTYPE html><html><body>");
                this.addString("文件队列仍未下载完毕，请等待后台下载进程执行。");
                this.addString("</body></html>");
        }else{
            File[] fl = ZipFolder.listFiles();
                //String Publishing = "/KcWikiOnline/custom/Publishing/"+ date +"/";
                String Publishing = urlprefix + date +"/";
                this.addString("<!DOCTYPE html><html><body>");
                for(File zip:fl) {
                    String filename = zip.getName();
                    String url = null;
                    if(filename.contains("gamefile"))
                        url = "<a href=\""+Publishing+filename+"\" >" +"游戏核心差分文件" +"</a>";
                    if(filename.contains("sourcefile"))
                        url = "<a href=\""+Publishing+filename+"\" >" +"完整的拆包文件（包括差分音频），请配合kcre进行拆包工作。" +"</a>";
                    if(filename.contains("editorialfile"))
                        url = "<a href=\""+Publishing+filename+"\" >" +"内含lua、拆好的地图、舰娘&装备立绘。（娇喘更新了的话）"+"</a>";
                    if(filename.contains("logfile"))
                        url = "<a href=\""+Publishing+filename+"\">" +"日志文件，仅供技术组分析用。"+"</a>";
                    url += LINESEPARATOR;
                    this.addString(url);
                }
                this.addString("</body></html>");
        }
        
        return sb.toString();
    }
    
    private void addString(String str) {
        sb.append(str).append(LINESEPARATOR).append("</br>").append(LINESEPARATOR);
    }
}
