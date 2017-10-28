/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.api;

import com.alibaba.fastjson.JSON;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.init.MainServer;
import static moe.kcwiki.tools.constant.LINESEPARATOR;

/**
 *
 * @author iTeam_VEP
 */
public class jsonpatch {
    private StringBuilder sb = null;
    
    public String getData() {
        sb = new StringBuilder();
        if(DBCenter.JsonPatch.isEmpty()){
            this.addString("<!DOCTYPE html><html><body>");
            this.addString("没有发现差分JSON内容或者抓包程序未运行，请等待后台抓包进程执行。");
            this.addString("</body></html>");
        } else {
            this.addString(LINESEPARATOR);
            for (String key:DBCenter.JsonPatch.keySet()) {
                this.addString(JSON.toJSONString(DBCenter.JsonPatch.get(key)));
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
