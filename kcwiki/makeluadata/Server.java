/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.makeluadata;


/**
 *
 * @author x5171
 */
public class Server {
        
    private static String WikiIdJson;
    private static String ShipData;
    private static String SlotitemData;
    
    
    /**
     * @param aWikiIdJson the WikiIdJson to set
     */
    public static void setWikiIdJson(String aWikiIdJson) {
        WikiIdJson = aWikiIdJson;
    }

    /**
     * @param aShipData the ShipData to set
     */
    public static void setShipData(String aShipData) {
        ShipData = aShipData;
    }

    /**
     * @param aSlotitemData the SlotitemData to set
     */
    public static void setSlotitemData(String aSlotitemData) {
        SlotitemData = aSlotitemData;
    }

    /**
     * @return the WikiIdJson
     */
    public static String getWikiIdJson() {
        return WikiIdJson;
    }

    /**
     * @return the ShipData
     */
    public static String getShipData() {
        return ShipData;
    }

    /**
     * @return the SlotitemData
     */
    public static String getSlotitemData() {
        return SlotitemData;
    }
}
