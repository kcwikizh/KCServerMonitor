/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.api;

import com.alibaba.fastjson.JSON;
import static moe.kcwiki.database.DBCenter.swfSrcPatch;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;

/**
 *
 * @author VEP
 */
public class srcdiff {
    private StringBuilder sb = null;
    
    public String getData() {
        sb = new StringBuilder();
        if(swfSrcPatch.isEmpty()){
            this.addString("<!DOCTYPE html><html><body>");
            this.addString("没有发现存在差异的文件或者抓包程序未运行，请等待后台抓包进程执行。");
            this.addString("</body></html>");
        } else {
            this.addString(JSON.toJSONString(swfSrcPatch));
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
