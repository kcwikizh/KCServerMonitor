/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.api;

import static moe.kcwiki.tools.constant.LINESEPARATOR;
import moe.kcwiki.unpackswf.UnpackSwf;

/**
 *
 * @author iTeam_VEP
 */
public class unpackswf {
    private StringBuilder sb = null;
    
    public String getData(String src,String dest) {
        sb = new StringBuilder();
        new UnpackSwf().ffdec(dest,src);
        this.addString("");
        
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
