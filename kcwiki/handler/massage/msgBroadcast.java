/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.handler.massage;

import moe.kcwiki.webserver.websocket.guestWS;
import moe.kcwiki.webserver.websocket.adminWS;


/**
 *
 * @author iTeam_VEP
 */
public class msgBroadcast {
    public static guestWS guestmsgbcer = new guestWS();
    public static adminWS adminmsgbcer = new adminWS(); 
    
    private msgBroadcast(){}
    
    private static class msgBroadcastHolder {  
        private static final msgBroadcast INSTANCE = new msgBroadcast();  
    }  
     
    public boolean guestMsgBC(String msg,int type){
        guestmsgbcer.broadcast(msg, type);
        return false;
    }
    
    public boolean adminMsgBC(String msg,int type){
        adminmsgbcer.broadcast(msg, type);
        return false;
    }
    
    public static msgBroadcast getInstance() {  
        return msgBroadcastHolder.INSTANCE; 
    }
}
