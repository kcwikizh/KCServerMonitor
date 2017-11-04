/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.database;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author VEP
 */
public class DBCenter {
    public static HashMap<String, Ship> ShipDB = new LinkedHashMap<>();  
    public static HashMap<String, SlotItem> SlotitemDB = new LinkedHashMap<>();  
    public static HashMap<String, Furniture> FurnitureDB = new LinkedHashMap<>();  
    public static HashMap<String, UseItem> UseitemDB = new LinkedHashMap<>();  
    public static HashMap<String, PayItem> PayitemDB = new LinkedHashMap<>();  
    public static HashMap<String, MapInfo> MapinfoDB = new LinkedHashMap<>();  
    public static HashMap<String, Bgm> BgmDB = new LinkedHashMap<>(); 
    public static HashMap<String, Mission> MissionDB = new LinkedHashMap<>();
    
        public static HashMap<String, Ship> NewShipDB = new LinkedHashMap<>();  
    public static HashMap<String, SlotItem> NewSlotitemDB = new LinkedHashMap<>();  
    public static HashMap<String, Furniture> NewFurnitureDB = new LinkedHashMap<>();  
    public static HashMap<String, UseItem> NewUseitemDB = new LinkedHashMap<>();  
    public static HashMap<String, PayItem> NewPayitemDB = new LinkedHashMap<>();  
    public static HashMap<String, MapInfo> NewMapinfoDB = new LinkedHashMap<>();  
    public static HashMap<String, Bgm> NewBgmDB = new LinkedHashMap<>(); 
    public static HashMap<String, Mission> NewMissionDB = new LinkedHashMap<>();

    public static HashMap<String, Ship> OldShipDB = new LinkedHashMap<>();  
    public static HashMap<String, SlotItem> OldSlotitemDB = new LinkedHashMap<>();  
    public static HashMap<String, Furniture> OldFurnitureDB = new LinkedHashMap<>();  
    public static HashMap<String, UseItem> OldUseitemDB = new LinkedHashMap<>();  
    public static HashMap<String, PayItem> OldPayitemDB = new LinkedHashMap<>();  
    public static HashMap<String, MapInfo> OldMapinfoDB = new LinkedHashMap<>();  
    public static HashMap<String, Bgm> OldBgmDB = new LinkedHashMap<>(); 
    public static HashMap<String, Mission> OldMissionDB = new LinkedHashMap<>();
    
    public static HashMap<String, String> SuspectData = new LinkedHashMap<>();
    public static HashMap<String, String> SuspectShipData = new LinkedHashMap<>();
    public static HashMap<String, String> SuspectSoundData = new LinkedHashMap<>();
    public static HashMap<String, String> SuspectBaseSoundData = new LinkedHashMap<>();
    public static HashMap<String, String> ShipVersionList = new LinkedHashMap<>(); 
    public static HashMap<String, String> AddressList = new LinkedHashMap<>(); 
    public static HashMap<String, Integer> NewMapBgm = new LinkedHashMap<>(); 
    
    public static HashMap<String, Ship> TempShipDB1 = new LinkedHashMap<>();  
    public static HashMap<String, Ship> TempShipDB2 = new LinkedHashMap<>();  
    public static HashMap<String, Ship> TempShipDB3 = new LinkedHashMap<>();  
    
    public static HashMap<String, String> beforeShipSortNO = new LinkedHashMap<>();  
    public static HashMap<String, JSONObject> JsonPatch = new LinkedHashMap<>();  
    public static HashMap<String, Object> swfSrcPatch = new LinkedHashMap<>(); 
    public static JSONObject dutyData = null;
    

    public static boolean reset(){
        ShipDB.clear();
        SlotitemDB.clear();
        FurnitureDB.clear();
        UseitemDB.clear();
        PayitemDB.clear();
        MapinfoDB.clear();
        BgmDB.clear();
        MissionDB.clear();
        
        NewShipDB.clear();
        NewSlotitemDB.clear();
        NewFurnitureDB.clear();
        NewUseitemDB.clear();
        NewPayitemDB.clear();
        NewMapinfoDB.clear();
        NewBgmDB.clear();
        NewMissionDB.clear();

        OldShipDB.clear();
        OldSlotitemDB.clear();
        OldFurnitureDB.clear();
        OldUseitemDB.clear();
        OldPayitemDB.clear();
        OldMapinfoDB.clear();
        OldBgmDB.clear();   
        OldMissionDB.clear();
        
        SuspectData.clear();
        SuspectShipData.clear();
        SuspectSoundData.clear();
        SuspectBaseSoundData.clear();
        ShipVersionList.clear();
        AddressList.clear();
        NewMapBgm.clear();
        
        TempShipDB1.clear();
        TempShipDB2.clear();
        TempShipDB3.clear();
        
        beforeShipSortNO.clear();
        JsonPatch.clear();
        swfSrcPatch.clear();
        dutyData = null;
        return true;
    }
}
