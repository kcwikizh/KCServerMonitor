/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.swfunpacker;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author x5171
 */
public class Server {

    private static String Coremap;
    private static String Coresound;
    public static HashMap<String, String> shiprule = new LinkedHashMap<>(); 
    public static HashMap<String, String> shipvoicerule = new LinkedHashMap<>(); 
    public static HashMap<String, String> slotitemrule = new LinkedHashMap<>(); 
    public static HashMap<String, String> seasonrule = new LinkedHashMap<>(); 
    
    
    /**
     * @return the Coremap
     */
    public static String getCoremap() {
        return Coremap;
    }

    /**
     * @param aCoremap the Coremap to set
     */
    public static void setCoremap(String aCoremap) {
        Coremap = aCoremap;
    }

    /**
     * @return the Coresound
     */
    public static String getCoresound() {
        return Coresound;
    }

    /**
     * @param aCoresound the Coresound to set
     */
    public static void setCoresound(String aCoresound) {
        Coresound = aCoresound;
    }

    /**
     * @return the shiprule
     */
    public static HashMap<String, String> getShiprule() {
        return shiprule;
    }

    /**
     * @param aShiprule the shiprule to set
     */
    public static void setShiprule(HashMap<String, String> aShiprule) {
        shiprule = aShiprule;
    }

    /**
     * @return the shipsoundrule
     */
    public static HashMap<String, String> getShipsoundrule() {
        return shipvoicerule;
    }

    /**
     * @param aShipsoundrule the shipsoundrule to set
     */
    public static void setShipsoundrule(HashMap<String, String> aShipsoundrule) {
        shipvoicerule = aShipsoundrule;
    }

    /**
     * @return the slotitemrule
     */
    public static HashMap<String, String> getSlotitemrule() {
        return slotitemrule;
    }

    /**
     * @param aSlotitemrule the slotitemrule to set
     */
    public static void setSlotitemrule(HashMap<String, String> aSlotitemrule) {
        slotitemrule = aSlotitemrule;
    }
}

