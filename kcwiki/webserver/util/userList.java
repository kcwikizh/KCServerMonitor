/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.webserver.util;

import java.util.HashMap;

/**
 *
 * @author iTeam_VEP
 */
public class userList {
    public static HashMap<String, Object> userList = new  HashMap<>();

    /**
     * @return the userList
     */
    public static HashMap<String, Object> getUserList() {
        return userList;
    }

    /**
     * @param aUserList the userList to set
     */
    public static void setUserList(HashMap<String, Object> aUserList) {
        userList = aUserList;
    }
    
}
