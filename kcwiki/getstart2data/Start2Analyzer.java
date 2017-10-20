/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getstart2data;

        
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import moe.kcwiki.database.*;

import moe.kcwiki.init.MainServer;
import moe.kcwiki.init.Start2DataThread;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.Encoder;
import moe.kcwiki.tools.constant;

/**
 *
 * @author VEP
 */
public class Start2Analyzer {
    private boolean wfFlag; 
    private final String downloadserver;
    private final String[] typelist;            

    public Start2Analyzer() {
        this.typelist = new String[]{"ship","shipgraph","slotitem_equiptype","equip_exslot","stype","slotitem","furniture","furnituregraph","useitem","payitem","maparea","mapinfo","mapbgm","mapcell","mission","shipupgrade","bgm"};    //"item_shop"、"const" 字段无法被转换成JSONArray
        this.wfFlag=false;
        this.downloadserver=MainServer.getKcwikiServerAddress();
    }
        
    public boolean ReadNewFile(StringBuilder buffer) throws IOException, Exception{
        String startdata;
        if(buffer == null){
            startdata=new Start2Api().GetStart2Api(MainServer.getNewstart2());
            //startdata=new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2"); 
        }else{
            startdata=buffer.toString();
            if(startdata.contains("svdata")){
                startdata=buffer.substring(buffer.indexOf("svdata=")+66, buffer.length()-1);
            }
        }
        
        
        String  prefix="api_mst_";
        try{
            JSONObject Data=JSON.parseObject(startdata);
            
            try (BufferedWriter Obfr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getTempFolder()+File.separator+"Start2_UTF8.json")), "UTF-8"))) {
                Obfr.write(Data.toJSONString());
            }
            
            Map<String,JSONObject> tempMap=new LinkedHashMap<>();

            for (String type:typelist){
                JSONArray newData=Data.getJSONArray(prefix+type);

                if(type.equals("ship")){            //Ship
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Ship newone=new Ship();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_yomi(newObject.getString("api_yomi"));
                        newone.setApi_stype(newObject.getString("api_stype"));
                        newone.setApi_souk(newObject.getString("api_souk"));
                        newone.setApi_slot_num(newObject.getString("api_slot_num"));
                        if(newObject.containsKey("api_sortno")){          //http://ask.csdn.net/questions/239114
                            newone.setApi_sortno(newObject.getString("api_sortno"));
                            newone.setApi_afterlv(newObject.getString("api_afterlv"));
                            newone.setApi_aftershipid(newObject.getString("api_aftershipid"));
                            newone.setApi_taik(newObject.getString("api_taik"));
                            newone.setApi_houg(newObject.getString("api_houg"));
                            newone.setApi_raig(newObject.getString("api_raig"));
                            newone.setApi_tyku(newObject.getString("api_tyku"));
                            newone.setApi_luck(newObject.getString("api_luck"));
                            newone.setApi_soku(newObject.getString("api_soku"));
                            newone.setApi_leng(newObject.getString("api_leng"));
                            newone.setApi_maxeq(newObject.getString("api_maxeq"));
                            newone.setApi_buildtime(newObject.getString("api_buildtime"));
                            newone.setApi_broken(newObject.getString("api_broken"));
                            newone.setApi_powup(newObject.getString("api_powup"));
                            newone.setApi_backs(newObject.getString("api_backs"));
                            newone.setApi_getmes(newObject.getString("api_getmes"));
                            newone.setApi_afterfuel(newObject.getString("api_afterfuel"));
                            newone.setApi_afterbull(newObject.getString("api_afterbull"));
                            newone.setApi_fuel_max(newObject.getString("api_fuel_max"));
                            newone.setApi_bull_max(newObject.getString("api_bull_max"));
                            newone.setApi_voicef(newObject.getString("api_voicef"));
                        }
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.ShipDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("shipgraph")){           //Shipgraph
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).filter((newObject) -> (DBCenter.ShipDB.get(newObject.getString("api_id"))!=null)).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_filename(newObject.getString("api_filename"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_version(newObject.getString("api_version"));
                        moe.kcwiki.database.DBCenter.ShipVersionList.put(newObject.getString("api_id"),newObject.getString("api_version").split(",")[0].substring(2,newObject.getString("api_version").split(",")[0].length()-1));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_boko_n(newObject.getString("api_boko_n"));
                        return newObject;
                    }).map((newObject) -> { 
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_battle_n(newObject.getString("api_battle_n"));
                        return newObject;
                    }).forEachOrdered((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_weda(newObject.getString("api_weda"));
                    });
                    continue;
                } 

                if(type.equals("slotitem")){            //SlotItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        SlotItem newone=new SlotItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_sortno(newObject.getString("api_sortno"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_taik(newObject.getString("api_taik"));
                        newone.setApi_souk(newObject.getString("api_souk"));
                        newone.setApi_houg(newObject.getString("api_houg"));
                        newone.setApi_raig(newObject.getString("api_raig"));
                        newone.setApi_soku(newObject.getString("api_soku"));
                        newone.setApi_baku(newObject.getString("api_baku"));
                        newone.setApi_tyku(newObject.getString("api_tyku"));
                        newone.setApi_tais(newObject.getString("api_tais"));
                        newone.setApi_atap(newObject.getString("api_atap"));
                        newone.setApi_houm(newObject.getString("api_houm"));
                        newone.setApi_raim(newObject.getString("api_raim"));
                        newone.setApi_houk(newObject.getString("api_houk"));
                        newone.setApi_raik(newObject.getString("api_raik"));
                        newone.setApi_bakk(newObject.getString("api_bakk"));
                        newone.setApi_saku(newObject.getString("api_saku"));
                        newone.setApi_sakb(newObject.getString("api_sakb"));
                        newone.setApi_luck(newObject.getString("api_luck"));
                        newone.setApi_leng(newObject.getString("api_leng"));
                        newone.setApi_rare(newObject.getString("api_rare"));
                        newone.setApi_broken(newObject.getString("api_broken"));
                        newone.setApi_info(newObject.getString("api_info"));
                        newone.setApi_usebull(newObject.getString("api_usebull"));
                        if(newObject.containsKey("api_cost")){
                            newone.setApi_cost(newObject.getString("api_cost"));
                        }
                        if(newObject.containsKey("api_distance")){
                            newone.setApi_distance(newObject.getString("api_distance"));
                        }                     
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.SlotitemDB.put(newone.getApi_id(),newone);
                    });   
                    continue;
                }            

                if(type.equals("furniture")){           //Furniture
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Furniture newone=new Furniture();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_no(newObject.getString("api_no"));
                        newone.setApi_title(newObject.getString("api_title"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_rarity(newObject.getString("api_rarity"));
                        newone.setApi_price(newObject.getString("api_price"));
                        newone.setApi_saleflg(newObject.getString("api_saleflg"));
                        newone.setApi_season(newObject.getString("api_season"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.FurnitureDB.put(newone.getApi_id(),newone);
                    });     
                    continue;
                } 

                if(type.equals("furnituregraph")){          //Furnituregraph
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        if(newObject.containsKey("api_filename")){
                            DBCenter.FurnitureDB.get(newObject.getString("api_id")).setApi_filename(newObject.getString("api_filename"));
                        }
                        return newObject;
                    }).filter((newObject) -> (newObject.containsKey("api_version"))).forEachOrdered((newObject) -> {
                        DBCenter.FurnitureDB.get(newObject.getString("api_id")).setApi_version(newObject.getString("api_version"));
                    });     
                    continue;
                } 

                if(type.equals("useitem")){            //UseItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        UseItem newone=new UseItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_usetype(newObject.getString("api_usetype"));
                        newone.setApi_category(newObject.getString("api_category"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_price(newObject.getString("api_price"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.UseitemDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("payitem")){         //PayItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        PayItem newone=new PayItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_item(newObject.getString("api_item"));
                        newone.setApi_price(newObject.getString("api_price"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.PayitemDB.put(newone.getApi_id(),newone);
                    });
                    continue;
                }

                if(type.equals("mapinfo")){         //MapInfo
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        MapInfo newone=new MapInfo();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_maparea_id(newObject.getString("api_maparea_id"));
                        newone.setApi_no(newObject.getString("api_no"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_level(newObject.getString("api_level"));
                        newone.setApi_opetext(newObject.getString("api_opetext"));
                        newone.setApi_infotext(newObject.getString("api_infotext"));
                        newone.setApi_item(newObject.getString("api_item"));
                        newone.setApi_max_maphp(newObject.getString("api_max_maphp"));
                        newone.setApi_required_defeat_count(newObject.getString("api_required_defeat_count"));
                        newone.setApi_sally_flag(newObject.getString("api_sally_flag"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.MapinfoDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("shipupgrade")){         //Shipupgrade
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_current_ship_id(newObject.getString("api_current_ship_id"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_original_ship_id(newObject.getString("api_original_ship_id"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_upgrade_type(newObject.getString("api_upgrade_type"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_upgrade_level(newObject.getString("api_upgrade_level"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_drawing_count(newObject.getString("api_drawing_count"));
                        return newObject;
                    }).forEachOrdered((JSONObject newObject) -> {
                        DBCenter.ShipDB.get(newObject.getString("api_id")).setApi_catapult_count(newObject.getString("api_catapult_count"));
                    });     
                    continue;
                }

                if(type.equals("bgm")){            //Bgm
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Bgm newone=new Bgm();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_name(newObject.getString("api_name"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.BgmDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("mission")){            //Mission
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Mission newone=new Mission();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_maparea_id(newObject.getString("api_maparea_id"));
                        newone.setApi_details(newObject.getString("api_details"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_time(newObject.getString("api_time"));
                        newone.setApi_difficulty(newObject.getString("api_difficulty"));
                        newone.setApi_use_fuel(newObject.getString("api_use_fuel"));
                        newone.setApi_use_bull(newObject.getString("api_use_bull"));
                        newone.setApi_win_item1(newObject.getString("api_win_item1"));
                        newone.setApi_win_item2(newObject.getString("api_win_item2"));
                        newone.setApi_return_flag(newObject.getString("api_return_flag"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.MissionDB.put(newone.getApi_id(),newone);
                    });     
                    continue;
                }   

                if(type.equals("mapbgm")){          //mapBgm
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        DBCenter.MapinfoDB.get(newObject.getString("api_id")).setApi_map_bgm(newObject.getString("api_map_bgm"));
                        return newObject;
                    }).forEachOrdered((newObject) -> {
                        DBCenter.MapinfoDB.get(newObject.getString("api_id")).setApi_boss_bgm(newObject.getString("api_boss_bgm"));
                    });  
                    continue;
                }        

                tempMap.clear();
            }
        }catch(JSONException e){
            msgPublish.msgPublisher("解析JSON时发生错误。",0,-1);
            return false;
        }       

        ReadOldFile();
        //JOptionPane.showMessageDialog(null,"运行完成" , "错误", JOptionPane.ERROR_MESSAGE);
        return true;
    }

    private boolean ReadOldFile() throws IOException, Exception{
        
        StringBuilder buffer;
        String startdata;
        if(MainServer.isDebugMode()){
            //startdata = new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/prev");
            startdata=new Start2Api().GetStart2Api(MainServer.getOldstart2());
        }else{
            try (BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(moe.kcwiki.init.MainServer.getLocaloldstart2data()), Encoder.codeString(moe.kcwiki.init.MainServer.getLocaloldstart2data())))) {
                String line;
                buffer = new StringBuilder();
                while ((line=nBfr.readLine())!=null) {
                    buffer.append(line);
                }
                startdata = buffer.toString();
            }
        }
        
        String  prefix="api_mst_";
        try{
            JSONObject Data=JSON.parseObject(startdata);
            Map<String,JSONObject> tempMap=new LinkedHashMap<>();

            for (String type:typelist){
                JSONArray newData=Data.getJSONArray(prefix+type);

                if(type.equals("ship")){            //Ship
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Ship newone=new Ship();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_yomi(newObject.getString("api_yomi"));
                        newone.setApi_stype(newObject.getString("api_stype"));
                        newone.setApi_souk(newObject.getString("api_souk"));
                        newone.setApi_slot_num(newObject.getString("api_slot_num"));
                        if(newObject.containsKey("api_sortno")){          //http://ask.csdn.net/questions/239114
                            newone.setApi_sortno(newObject.getString("api_sortno"));
                            newone.setApi_afterlv(newObject.getString("api_afterlv"));
                            newone.setApi_aftershipid(newObject.getString("api_aftershipid"));
                            newone.setApi_taik(newObject.getString("api_taik"));
                            newone.setApi_houg(newObject.getString("api_houg"));
                            newone.setApi_raig(newObject.getString("api_raig"));
                            newone.setApi_tyku(newObject.getString("api_tyku"));
                            newone.setApi_luck(newObject.getString("api_luck"));
                            newone.setApi_soku(newObject.getString("api_soku"));
                            newone.setApi_leng(newObject.getString("api_leng"));
                            newone.setApi_maxeq(newObject.getString("api_maxeq"));
                            newone.setApi_buildtime(newObject.getString("api_buildtime"));
                            newone.setApi_broken(newObject.getString("api_broken"));
                            newone.setApi_powup(newObject.getString("api_powup"));
                            newone.setApi_backs(newObject.getString("api_backs"));
                            newone.setApi_getmes(newObject.getString("api_getmes"));
                            newone.setApi_afterfuel(newObject.getString("api_afterfuel"));
                            newone.setApi_afterbull(newObject.getString("api_afterbull"));
                            newone.setApi_fuel_max(newObject.getString("api_fuel_max"));
                            newone.setApi_bull_max(newObject.getString("api_bull_max"));
                            newone.setApi_voicef(newObject.getString("api_voicef"));
                        }
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldShipDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("shipgraph")){           //Shipgraph
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).filter((newObject) -> (DBCenter.OldShipDB.get(newObject.getString("api_id"))!=null)).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_filename(newObject.getString("api_filename"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_version(newObject.getString("api_version"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_boko_n(newObject.getString("api_boko_n"));
                        return newObject;
                    }).map((newObject) -> { 
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_battle_n(newObject.getString("api_battle_n"));
                        return newObject;
                    }).forEachOrdered((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_weda(newObject.getString("api_weda"));
                    });
                    continue;
                } 

                if(type.equals("slotitem")){            //SlotItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        SlotItem newone=new SlotItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_sortno(newObject.getString("api_sortno"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_taik(newObject.getString("api_taik"));
                        newone.setApi_souk(newObject.getString("api_souk"));
                        newone.setApi_houg(newObject.getString("api_houg"));
                        newone.setApi_raig(newObject.getString("api_raig"));
                        newone.setApi_soku(newObject.getString("api_soku"));
                        newone.setApi_baku(newObject.getString("api_baku"));
                        newone.setApi_tyku(newObject.getString("api_tyku"));
                        newone.setApi_tais(newObject.getString("api_tais"));
                        newone.setApi_atap(newObject.getString("api_atap"));
                        newone.setApi_houm(newObject.getString("api_houm"));
                        newone.setApi_raim(newObject.getString("api_raim"));
                        newone.setApi_houk(newObject.getString("api_houk"));
                        newone.setApi_raik(newObject.getString("api_raik"));
                        newone.setApi_bakk(newObject.getString("api_bakk"));
                        newone.setApi_saku(newObject.getString("api_saku"));
                        newone.setApi_sakb(newObject.getString("api_sakb"));
                        newone.setApi_luck(newObject.getString("api_luck"));
                        newone.setApi_leng(newObject.getString("api_leng"));
                        newone.setApi_rare(newObject.getString("api_rare"));
                        newone.setApi_broken(newObject.getString("api_broken"));
                        newone.setApi_info(newObject.getString("api_info"));
                        newone.setApi_usebull(newObject.getString("api_usebull"));
                        if(newObject.containsKey("api_cost")){
                            newone.setApi_cost(newObject.getString("api_cost"));
                        }
                        if(newObject.containsKey("api_distance")){
                            newone.setApi_distance(newObject.getString("api_distance"));
                        }                     
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldSlotitemDB.put(newone.getApi_id(),newone);
                    });   
                    continue;
                }            

                if(type.equals("furniture")){           //Furniture
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Furniture newone=new Furniture();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_no(newObject.getString("api_no"));
                        newone.setApi_title(newObject.getString("api_title"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_rarity(newObject.getString("api_rarity"));
                        newone.setApi_price(newObject.getString("api_price"));
                        newone.setApi_saleflg(newObject.getString("api_saleflg"));
                        newone.setApi_season(newObject.getString("api_season"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldFurnitureDB.put(newone.getApi_id(),newone);
                    });     
                    continue;
                } 

                if(type.equals("furnituregraph")){          //Furnituregraph
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        if(newObject.containsKey("api_filename")){
                            DBCenter.OldFurnitureDB.get(newObject.getString("api_id")).setApi_filename(newObject.getString("api_filename"));
                        }
                        return newObject;
                    }).filter((newObject) -> (newObject.containsKey("api_version"))).forEachOrdered((newObject) -> {
                        DBCenter.OldFurnitureDB.get(newObject.getString("api_id")).setApi_version(newObject.getString("api_version"));
                    });     
                    continue;
                } 

                if(type.equals("useitem")){            //UseItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        UseItem newone=new UseItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_usetype(newObject.getString("api_usetype"));
                        newone.setApi_category(newObject.getString("api_category"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_price(newObject.getString("api_price"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldUseitemDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("payitem")){         //PayItem
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        PayItem newone=new PayItem();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_type(newObject.getString("api_type"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_description(newObject.getString("api_description"));
                        newone.setApi_item(newObject.getString("api_item"));
                        newone.setApi_price(newObject.getString("api_price"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldPayitemDB.put(newone.getApi_id(),newone);
                    });
                    continue;
                }

                if(type.equals("mapinfo")){         //MapInfo
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        MapInfo newone=new MapInfo();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_maparea_id(newObject.getString("api_maparea_id"));
                        newone.setApi_no(newObject.getString("api_no"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_level(newObject.getString("api_level"));
                        newone.setApi_opetext(newObject.getString("api_opetext"));
                        newone.setApi_infotext(newObject.getString("api_infotext"));
                        newone.setApi_item(newObject.getString("api_item"));
                        newone.setApi_max_maphp(newObject.getString("api_max_maphp"));
                        newone.setApi_required_defeat_count(newObject.getString("api_required_defeat_count"));
                        newone.setApi_sally_flag(newObject.getString("api_sally_flag"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldMapinfoDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("shipupgrade")){         //Shipupgrade
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_current_ship_id(newObject.getString("api_current_ship_id"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_original_ship_id(newObject.getString("api_original_ship_id"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_upgrade_type(newObject.getString("api_upgrade_type"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_upgrade_level(newObject.getString("api_upgrade_level"));
                        return newObject;
                    }).map((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_drawing_count(newObject.getString("api_drawing_count"));
                        return newObject;
                    }).forEachOrdered((newObject) -> {
                        DBCenter.OldShipDB.get(newObject.getString("api_id")).setApi_catapult_count(newObject.getString("api_catapult_count"));
                    });     
                    continue;
                }

                if(type.equals("bgm")){            //Bgm
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Bgm newone=new Bgm();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_name(newObject.getString("api_name"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldBgmDB.put(newone.getApi_id(),newone);
                    });    
                    continue;
                }

                if(type.equals("mission")){            //Mission
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> {
                        Mission newone=new Mission();
                        JSONObject newObject=map.getValue();  
                        newone.setApi_id(newObject.getString("api_id"));
                        newone.setApi_maparea_id(newObject.getString("api_maparea_id"));
                        newone.setApi_details(newObject.getString("api_details"));
                        newone.setApi_name(newObject.getString("api_name"));
                        newone.setApi_time(newObject.getString("api_time"));
                        newone.setApi_difficulty(newObject.getString("api_difficulty"));
                        newone.setApi_use_fuel(newObject.getString("api_use_fuel"));
                        newone.setApi_use_bull(newObject.getString("api_use_bull"));
                        newone.setApi_win_item1(newObject.getString("api_win_item1"));
                        newone.setApi_win_item2(newObject.getString("api_win_item2"));
                        newone.setApi_return_flag(newObject.getString("api_return_flag"));
                        return newone;
                    }).forEachOrdered((newone) -> {
                        DBCenter.OldMissionDB.put(newone.getApi_id(),newone);
                    });     
                    continue;
                }   

                if(type.equals("mapbgm")){          //mapBgm
                    tempMap.clear();
                    Iterator iterator=newData.iterator();
                    while(iterator.hasNext()){
                        JSONObject newObject=(JSONObject)iterator.next();
                        tempMap.put(newObject.getString("api_id"), newObject);
                    }

                    tempMap.entrySet().stream().map((map) -> map.getValue()).map((newObject) -> {  
                        DBCenter.OldMapinfoDB.get(newObject.getString("api_id")).setApi_map_bgm(newObject.getString("api_map_bgm"));
                        return newObject;
                    }).forEachOrdered((newObject) -> {
                        DBCenter.OldMapinfoDB.get(newObject.getString("api_id")).setApi_boss_bgm(newObject.getString("api_boss_bgm"));
                    });  
                    continue;
                }        

                tempMap.clear();
            }
        }catch(JSONException e){
            msgPublish.msgPublisher("解析JSON时发生错误。",0,-1);
            return false;
        }
        
        
        //msgPublish.msgPublisher("Start2Analyzer-准备进行分析数据。isHasStart2: "+Start2DataThread.isHasStart2(),0,0);
        StartContrast();
        //JOptionPane.showMessageDialog(null,"运行完成" , "错误", JOptionPane.ERROR_MESSAGE);
        return true;
    }
    
    private boolean StartContrast() throws FileNotFoundException, UnsupportedEncodingException, IOException, Exception{    
        
        HashMap<String, Ship> ShipgraphDB = new LinkedHashMap<>();
        boolean ShipgraphDBFlag=false;
        String ShipgraphstdDBFile=MainServer.getDataFolder()+File.separator+"ShipgraphstdDB.txt";
        
        
        if(new File(ShipgraphstdDBFile).exists()){
            BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(ShipgraphstdDBFile), Encoder.codeString(ShipgraphstdDBFile)));
            String s;
            while((s=nBfr.readLine())!=null){
                Ship newShip=new Ship();
                String[] str=s.split("\t");
                if(str.length==7){
                    newShip.setApi_id(str[0]);
                    newShip.setApi_name(str[1]);
                    newShip.setApi_filename(str[2]);
                    newShip.setApi_version(str[3]);
                    newShip.setApi_boko_n(str[4]);
                    newShip.setApi_battle_n(str[5]);
                    newShip.setApi_weda(str[6]);
                    ShipgraphDB.put(str[0], newShip);
                }
            }
            ShipgraphDBFlag=true;
        }
        
        for(String key : DBCenter.ShipDB.keySet()){      //Ship  
            if(DBCenter.OldShipDB.get(key)!=null){
                if(key==null){continue;}
                String newstr[]=DBCenter.ShipDB.get(key).getApi_version().substring(2, DBCenter.ShipDB.get(key).getApi_version().length()-2).split("\",\"");
                String oldstr[]=DBCenter.OldShipDB.get(key).getApi_version().substring(2, DBCenter.OldShipDB.get(key).getApi_version().length()-2).split("\",\"");
                if(ShipgraphDB.get(key)!=null){
                    String dbstr[]=ShipgraphDB.get(key).getApi_version().substring(2, ShipgraphDB.get(key).getApi_version().length()-2).split("\",\"");
                    if((!(ShipgraphDB.get(key).getApi_boko_n().equals(DBCenter.ShipDB.get(key).getApi_boko_n())))&&(!(DBCenter.OldShipDB.get(key).getApi_boko_n().equals(DBCenter.ShipDB.get(key).getApi_boko_n())))){
                        //if(new File(MainServer.getDataPath()+File.separator+"lastShip.txt").exists()){}
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                    }else if((!(ShipgraphDB.get(key).getApi_battle_n().equals(DBCenter.ShipDB.get(key).getApi_battle_n())))&&(!(DBCenter.OldShipDB.get(key).getApi_battle_n().equals(DBCenter.ShipDB.get(key).getApi_battle_n())))){
                        //if(new File(MainServer.getDataPath()+File.separator+"lastShip.txt").exists()){}
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                    }else if((!(ShipgraphDB.get(key).getApi_weda().equals(DBCenter.ShipDB.get(key).getApi_weda())))&&(!(DBCenter.OldShipDB.get(key).getApi_weda().equals(DBCenter.ShipDB.get(key).getApi_weda())))){
                        //if(new File(MainServer.getDataPath()+File.separator+"lastShip.txt").exists()){}
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                    }
                    if((!newstr[1].equals(oldstr[1]))&&(!newstr[1].equals(dbstr[1]))){ 
                        DBCenter.SuspectSoundData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }
                    if((!newstr[2].equals(oldstr[2]))&&(!newstr[2].equals(dbstr[2]))){ 
                        DBCenter.SuspectBaseSoundData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }
                }
                
                if(ShipgraphDBFlag==false){
                    if(!(DBCenter.OldShipDB.get(key).getApi_boko_n().equals(DBCenter.ShipDB.get(key).getApi_boko_n()))){ 
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }else if(!(DBCenter.OldShipDB.get(key).getApi_battle_n().equals(DBCenter.ShipDB.get(key).getApi_battle_n()))){ 
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }else if(!(DBCenter.OldShipDB.get(key).getApi_weda().equals(DBCenter.ShipDB.get(key).getApi_weda()))){ 
                        DBCenter.SuspectShipData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }
                    if(!(newstr[1].equals(oldstr[1]))){ 
                        DBCenter.SuspectBaseSoundData.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key).getApi_filename());
                        //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                    }
                }
                
                
                /*
                if(!(newstr[2].equals(oldstr[2]))){ 
                    DBCenter.SuspectBaseSoundData.put(DBCenter.ShipDB.get(key).getApi_name(), DBCenter.ShipDB.get(key).getApi_id());
                    //JOptionPane.showConfirmDialog(null, "是否开始解压swf文件?", "询问", JOptionPane.YES_NO_OPTION);
                }
                */
            }else{
                DBCenter.NewShipDB.put(DBCenter.ShipDB.get(key).getApi_id(), DBCenter.ShipDB.get(key));
            } 
        }                                                                                   
        
        
        DBCenter.SlotitemDB.keySet().forEach((key) -> {
            //SlotItem
            if(DBCenter.OldSlotitemDB.get(key)!=null){
                if(DBCenter.OldSlotitemDB.get(key).getApi_name().equals(DBCenter.SlotitemDB.get(key).getApi_name())){
                }else{
                    DBCenter.SuspectData.put(DBCenter.SlotitemDB.get(key).getApi_name(), DBCenter.SlotitemDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewSlotitemDB.put(DBCenter.SlotitemDB.get(key).getApi_id(), DBCenter.SlotitemDB.get(key));
            }
        });   

        
        DBCenter.FurnitureDB.keySet().forEach((key) -> {
            //Furniture
            if(DBCenter.OldFurnitureDB.get(key)!=null){
                if(DBCenter.OldFurnitureDB.get(key).getApi_title().equals(DBCenter.FurnitureDB.get(key).getApi_title())){
                }else{
                    DBCenter.SuspectData.put(DBCenter.FurnitureDB.get(key).getApi_title(), DBCenter.FurnitureDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewFurnitureDB.put(DBCenter.FurnitureDB.get(key).getApi_id(), DBCenter.FurnitureDB.get(key));
            }
        });  


        DBCenter.UseitemDB.keySet().forEach((key) -> {
            //UseItem
            if(DBCenter.OldUseitemDB.get(key)!=null){
                if(DBCenter.OldUseitemDB.get(key).getApi_name().equals(DBCenter.UseitemDB.get(key).getApi_name())){
                }else{
                    DBCenter.SuspectData.put(DBCenter.UseitemDB.get(key).getApi_name(), DBCenter.UseitemDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewUseitemDB.put(DBCenter.UseitemDB.get(key).getApi_id(), DBCenter.UseitemDB.get(key));
            }
        });  

        
        DBCenter.PayitemDB.keySet().forEach((key) -> {
            //PayItem
            if(DBCenter.OldPayitemDB.get(key)!=null){
                if(DBCenter.OldPayitemDB.get(key).getApi_name().equals(DBCenter.PayitemDB.get(key).getApi_name())){
                }else{
                    DBCenter.SuspectData.put(DBCenter.PayitemDB.get(key).getApi_name(), DBCenter.PayitemDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewPayitemDB.put(DBCenter.PayitemDB.get(key).getApi_id(), DBCenter.PayitemDB.get(key));
            }
        });  


        DBCenter.MapinfoDB.keySet().forEach((key) -> {
            //MapInfo
            if(DBCenter.OldMapinfoDB.get(key)!=null){
                if(DBCenter.OldMapinfoDB.get(key).getApi_name().equals(DBCenter.MapinfoDB.get(key).getApi_name())){
                    int count=0;
                    if(!DBCenter.OldMapinfoDB.get(key).getApi_map_bgm().equals(DBCenter.MapinfoDB.get(key).getApi_map_bgm())){
                        count=count+1;
                    }
                    if(!DBCenter.OldMapinfoDB.get(key).getApi_boss_bgm().equals(DBCenter.MapinfoDB.get(key).getApi_boss_bgm())){
                        count=count+2;
                    }
                    if(count!=0){
                        DBCenter.NewMapBgm.put(key, count);
                    }
                }else{
                    DBCenter.SuspectData.put(DBCenter.MapinfoDB.get(key).getApi_name(), DBCenter.MapinfoDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewMapinfoDB.put(DBCenter.MapinfoDB.get(key).getApi_id(), DBCenter.MapinfoDB.get(key));
            }
        });  

        
        DBCenter.BgmDB.keySet().forEach((key) -> {
            //Bgm
            if(DBCenter.OldBgmDB.get(key)!=null){
                if(DBCenter.OldBgmDB.get(key).getApi_name().equals(DBCenter.BgmDB.get(key).getApi_name())){
                }else{
                    DBCenter.SuspectData.put(DBCenter.BgmDB.get(key).getApi_name(), DBCenter.BgmDB.get(key).getApi_id());
                }
            }else{
                DBCenter.NewBgmDB.put(DBCenter.BgmDB.get(key).getApi_id(), DBCenter.BgmDB.get(key));
            }
        });  
        
        Start2DataThread.setHasStart2(true);
        Export();
        return true;
    }
    
    public boolean Export() throws FileNotFoundException, UnsupportedEncodingException, IOException{
    
        String folder=MainServer.getWorksFolder()+File.separator+"Start2_Export.txt";
        wfFlag=false;
        String Data,nData;
        
        try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(folder)), "UTF-8"))) {
            eBfw.write(constant.LINESEPARATOR);
            eBfw.write(new Date()+constant.LINESEPARATOR+constant.LINESEPARATOR);
            eBfw.write(constant.LINESEPARATOR);
            eBfw.write("目前统计如下："+constant.LINESEPARATOR);
            
            if (!DBCenter.SuspectShipData.isEmpty()){   //suspectData
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+"发现新立绘："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                wfFlag=true;
                eBfw.write("种类：\t\t\t 文件名："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,String> map : DBCenter.SuspectShipData.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=downloadserver+"resources/swf/ships/"+DBCenter.ShipDB.get(map.getKey()).getApi_filename()+".swf?VERSION="+DBCenter.ShipVersionList.get(map.getKey());
                    eBfw.write(DBCenter.ShipDB.get(map.getKey()).getApi_name()+"："+constant.LINESEPARATOR);
                    eBfw.write(Data+constant.LINESEPARATOR);
                    DBCenter.AddressList.put(Data,map.getValue()+".swf");
                }
            }
            
            if (!DBCenter.SuspectSoundData.isEmpty()){   //suspectData
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+"发现新语音："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                wfFlag=true;
                eBfw.write("id：\t\t\t 名字："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,String> map : DBCenter.SuspectSoundData.entrySet()){
                    if(map.getKey()==null){continue;}
                    eBfw.write(map.getKey()+"\t\t\t"+map.getValue()+constant.LINESEPARATOR);
                }
            }
            
            if (!DBCenter.SuspectBaseSoundData.isEmpty()){   //suspectData
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+"发现新母港语音："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                wfFlag=true;
                eBfw.write("id：\t\t\t 名字："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,String> map : DBCenter.SuspectBaseSoundData.entrySet()){
                    if(map.getKey()==null){continue;}
                    eBfw.write(map.getKey()+"\t\t\t"+map.getValue()+constant.LINESEPARATOR);
                }
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR);
            }
            
            if (!DBCenter.SuspectData.isEmpty()){   //suspectData
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+"警告：疑似发现娇喘API改动，本次分析结果可能不准（也有可能是仅更新了文件名）。"+constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("发现新杂项："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                wfFlag=true;
                eBfw.write("种类：\t\t\t ID："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,String> map : DBCenter.SuspectData.entrySet()){
                    if(map.getKey()==null){continue;}
                    eBfw.write(map.getKey()+"\t\t\t"+map.getValue()+constant.LINESEPARATOR);
                }
            }
            
            if(!DBCenter.NewShipDB.isEmpty()){  //Ship
                wfFlag=true;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+"发现新舰娘，下载链接如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,Ship> map : DBCenter.NewShipDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    
                    Data=downloadserver+"resources/swf/ships/"+map.getValue().getApi_filename()+".swf?VERSION="+DBCenter.ShipVersionList.get(map.getKey());
                    DBCenter.AddressList.put(Data,map.getValue().getApi_filename()+".swf");
                    eBfw.write(map.getValue().getApi_id()+"-"+map.getValue().getApi_name()+constant.LINESEPARATOR);
                    eBfw.write(Data+constant.LINESEPARATOR);
                }
            }
            
             
            if(!DBCenter.NewSlotitemDB.isEmpty()){  //SlotItem
                wfFlag=true;    
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("发现新装备，名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,SlotItem> map : DBCenter.NewSlotitemDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_name();
                    eBfw.write(Data+constant.LINESEPARATOR);
                }
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("下载链接如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);                
                for( Map.Entry<String,SlotItem> map : DBCenter.NewSlotitemDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    nData=map.getValue().getApi_id();
                    if(Integer.parseInt(map.getValue().getApi_id())>499){break;}
                    if(Integer.parseInt(map.getValue().getApi_id())<100){nData="0"+map.getValue().getApi_id();}
                    if(Integer.parseInt(map.getValue().getApi_id())<10){nData="00"+map.getValue().getApi_id();}
                    for(int count=0;count<4;count++){
                        Data="";
                        if(count==0){Data=downloadserver+"resources/image/slotitem/card/"+nData+".png";}
                        if(count==1){Data=downloadserver+"resources/image/slotitem/item_character/"+nData+".png";}
                        if(count==2){Data=downloadserver+"resources/image/slotitem/item_on/"+nData+".png";}
                        if(count==3){Data=downloadserver+"resources/image/slotitem/item_up/"+nData+".png";}
                        eBfw.write(Data+constant.LINESEPARATOR);
                        DBCenter.AddressList.put(Data,nData+".png");
                    } 
                }
            }
            
             
            if(!DBCenter.NewFurnitureDB.isEmpty()){  //Furniture
                wfFlag=true;
                String type;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("发现新家具，ID为："+constant.LINESEPARATOR+constant.LINESEPARATOR);                
                for( Map.Entry<String,Furniture> map : DBCenter.NewFurnitureDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_title();
                    //Data="http://voice.kcwiki.moe/resources/image/slotItem/card/"+String.valueOf(Integer.parseInt(newData.nFurniture[i][0])-1)+".png";
                    if(Integer.parseInt(map.getValue().getApi_season())!=0){eBfw.write(Data+"\t季节限定！"+constant.LINESEPARATOR);}else{eBfw.write(Data+constant.LINESEPARATOR);}
                }
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("下载链接如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);                
                for( Map.Entry<String,Furniture> map : DBCenter.NewFurnitureDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    nData=String.valueOf(Integer.parseInt(map.getValue().getApi_no())+1);
                    if(nData.length()>3){
                        nData=nData.substring(0,3);
                        if(Integer.parseInt(nData)<10){nData="00"+nData;}
                        if(Integer.parseInt(nData)>9&&Integer.parseInt(nData)<100){nData="0"+nData;}
                        nData=nData+String.valueOf(Integer.parseInt(map.getValue().getApi_no())+1).substring(3,String.valueOf(Integer.parseInt(map.getValue().getApi_no())+1).length());
                    }else{
                        if(Integer.parseInt(nData)>9&&Integer.parseInt(nData)<100){nData="0"+nData;}
                        if(Integer.parseInt(nData)<10){nData="00"+nData;}                       
                    }
                    type="";
                    if((Integer.parseInt(map.getValue().getApi_type()))==0){
                        type=downloadserver+"resources/image/furniture/floor/";
                    }
                    if((Integer.parseInt(map.getValue().getApi_type()))==1){
                        type=downloadserver+"resources/image/furniture/wall/";
                    }
                    if((Integer.parseInt(map.getValue().getApi_type()))==2){
                        type=downloadserver+"resources/image/furniture/window/";
                    }
                    if((Integer.parseInt(map.getValue().getApi_type()))==3){
                        type=downloadserver+"resources/image/furniture/object/";
                    }
                    if((Integer.parseInt(map.getValue().getApi_type()))==4){
                        type=downloadserver+"resources/image/furniture/chest/";
                    }
                    if((Integer.parseInt(map.getValue().getApi_type()))==5){
                        type=downloadserver+"resources/image/furniture/desk/";
                    }    
                    if((map.getValue().getApi_filename())!=null){
                        Data=type+map.getValue().getApi_filename()+".swf";
                        DBCenter.AddressList.put(Data,nData+".swf");
                    } else{
                        Data=type+nData+".png";
                        DBCenter.AddressList.put(Data,nData+".png");
                    }                     
                    eBfw.write(Data+constant.LINESEPARATOR);
                }
            }        
            
            
            if(!DBCenter.NewUseitemDB.isEmpty()){  //UseItem
                wfFlag=true;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR); 
                eBfw.write("发现新用品，名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,UseItem> map : DBCenter.NewUseitemDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_name();
                    eBfw.write(Data+constant.LINESEPARATOR);
                }
                eBfw.write(constant.LINESEPARATOR+"下载链接如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);   
                for( Map.Entry<String,UseItem> map : DBCenter.NewUseitemDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    nData=map.getValue().getApi_id();
                    if(Integer.parseInt(map.getValue().getApi_id())<100){nData="0"+map.getValue().getApi_id();}
                    if(Integer.parseInt(map.getValue().getApi_id())<10){nData="00"+map.getValue().getApi_id();}
                    Data=downloadserver+"resources/image/useitem/card/"+nData+".png";
                    eBfw.write(map.getValue().getApi_name()+constant.LINESEPARATOR+Data+constant.LINESEPARATOR);
                    DBCenter.AddressList.put(Data,nData+".png");
                }
            }
            
            if(!DBCenter.NewPayitemDB.isEmpty()){  //PayItem
                wfFlag=true;    
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR);
                eBfw.write("发现新商品，名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,PayItem> map : DBCenter.NewPayitemDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_name();
                    eBfw.write(Data+constant.LINESEPARATOR);
                }
            }  
            
            if(!DBCenter.NewMapinfoDB.isEmpty()){  //MapInfo
                wfFlag=true;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR); 
                eBfw.write("发现新地图，名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,MapInfo> map : DBCenter.NewMapinfoDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_name()+"——"+map.getValue().getApi_opetext();
                    eBfw.write(Data+"\t海域Bgm："+map.getValue().getApi_map_bgm()+"\t海域BossBgm："+map.getValue().getApi_boss_bgm()+constant.LINESEPARATOR);
                    eBfw.write("海域信息："+map.getValue().getApi_infotext()+constant.LINESEPARATOR);
                    String[] sally=map.getValue().getApi_sally_flag().substring(1, map.getValue().getApi_sally_flag().length()-1).split(",");
                    if(sally[0].equals("1")){
                        eBfw.write("出击舰队信息：仅允许常规部队出击。"+constant.LINESEPARATOR);
                    }
                    if(sally[1].equals("1")){
                        eBfw.write("出击舰队信息：仅允许机动部队出击。"+constant.LINESEPARATOR);
                    }
                    if(sally[1].equals("2")){
                        eBfw.write("出击舰队信息：仅允许水上部队出击。"+constant.LINESEPARATOR);
                    }
                    if(sally[1].equals("4")){
                        eBfw.write("出击舰队信息：仅允许输送部队出击。"+constant.LINESEPARATOR);
                    }
                    if(sally[1].equals("7")){
                        eBfw.write("出击舰队信息：允许机动部队和水上部队出击。"+constant.LINESEPARATOR);
                    }
                    if(map.getValue().getApi_required_defeat_count()!=null){
                        eBfw.write("需要击破次数："+map.getValue().getApi_required_defeat_count()+constant.LINESEPARATOR);
                    }
                    eBfw.write(constant.LINESEPARATOR);
                }
                eBfw.write(constant.LINESEPARATOR+"下载链接如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);                
                for( Map.Entry<String,MapInfo> map : DBCenter.NewMapinfoDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    nData=map.getValue().getApi_id();
                    if(Integer.parseInt(map.getValue().getApi_id())>9&&Integer.parseInt(map.getValue().getApi_id())<100){
                        nData="0"+String.valueOf(((int)(Integer.parseInt(map.getValue().getApi_id())/10)))+"_0"+String.valueOf(((int)(Integer.parseInt(map.getValue().getApi_id())%10))); 
                    }
                    if(100<=Integer.parseInt(map.getValue().getApi_id())&&Integer.parseInt(map.getValue().getApi_id())<1000){
                        int a=Integer.parseInt(map.getValue().getApi_id())/100;
                        int b=(Integer.parseInt(map.getValue().getApi_id())%100)/10;
                        int c=Integer.parseInt(map.getValue().getApi_id())%10;
                        nData=String.valueOf(a)+String.valueOf(b)+"_0"+String.valueOf(c); }                    
                    Data=downloadserver+"resources/swf/map/"+nData+".swf";
                    eBfw.write("海域编号："+map.getValue().getApi_id()+constant.LINESEPARATOR);
                    eBfw.write(Data+constant.LINESEPARATOR);
                    String data[]=map.getValue().getApi_map_bgm().substring(1, map.getValue().getApi_map_bgm().length()-1).split(",");
                    if(!data[0].equals(data[1])){
                        eBfw.write("海域Bgm:"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf"+constant.LINESEPARATOR);
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                    }else{
                        eBfw.write("海域Bgm:"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                    }
                    data=map.getValue().getApi_boss_bgm().substring(1, map.getValue().getApi_boss_bgm().length()-1).split(",");
                    if(!data[0].equals(data[1])){
                        eBfw.write("海域BossBgm:"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf"+constant.LINESEPARATOR);
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                    }else{
                        eBfw.write("海域BossBgm:"+constant.LINESEPARATOR);
                        eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                        DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                    }         
                    eBfw.write(constant.LINESEPARATOR);
                    DBCenter.AddressList.put(Data,nData+".swf");
                }
            }

            if(!DBCenter.NewMapBgm.isEmpty()){  //MapInfo
                wfFlag=true;
                String data[]=null;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR); 
                eBfw.write("发现海域更新了Bgm，地图名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,Integer> map : DBCenter.NewMapBgm.entrySet()){
                    if(map.getKey()==null){continue;}
                    if(map.getValue()==1||map.getValue()==3){
                        data=DBCenter.MapinfoDB.get(map.getKey()).getApi_map_bgm().substring(1, DBCenter.MapinfoDB.get(map.getKey()).getApi_map_bgm().length()-1).split(",");
                        Data=DBCenter.MapinfoDB.get(map.getKey()).getApi_id()+"——"+DBCenter.MapinfoDB.get(map.getKey()).getApi_name()+"——"+DBCenter.MapinfoDB.get(map.getKey()).getApi_opetext();
                        if(!data[0].equals(data[1])){
                            eBfw.write(Data+"海域Bgm:"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf"+constant.LINESEPARATOR);
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                        }else{
                            eBfw.write(Data+"海域Bgm:"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                        }
                    }
                    if(map.getValue()==2||map.getValue()==3){
                        data=DBCenter.MapinfoDB.get(map.getKey()).getApi_boss_bgm().substring(1, DBCenter.MapinfoDB.get(map.getKey()).getApi_boss_bgm().length()-1).split(",");
                        Data=DBCenter.MapinfoDB.get(map.getKey()).getApi_id()+"——"+DBCenter.MapinfoDB.get(map.getKey()).getApi_name()+"——"+DBCenter.MapinfoDB.get(map.getKey()).getApi_opetext();
                        if(!data[0].equals(data[1])){
                            eBfw.write(Data+"海域BossBgm:"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf"+constant.LINESEPARATOR);
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[1]+".swf","sound_b_bgm_"+data[1]+".swf");
                        }else{
                            eBfw.write(Data+"海域BossBgm:"+constant.LINESEPARATOR);
                            eBfw.write(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf"+constant.LINESEPARATOR);
                            DBCenter.AddressList.put(downloadserver+"resources/swf/sound_b_bgm_"+data[0]+".swf","sound_b_bgm_"+data[0]+".swf");
                        }
                    }
                    eBfw.write(constant.LINESEPARATOR);   
                }
                eBfw.write(constant.LINESEPARATOR);        
            }
            
            if(!DBCenter.NewBgmDB.isEmpty()){  //Bgm
                wfFlag=true;
                eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR); 
                eBfw.write("发现新母港Bgm，名字如下："+constant.LINESEPARATOR+constant.LINESEPARATOR);
                for( Map.Entry<String,Bgm> map : DBCenter.NewBgmDB.entrySet()){
                    if(map.getKey()==null){continue;}
                    Data=map.getValue().getApi_name();
                    eBfw.write(Data+constant.LINESEPARATOR);
                }              
            }
            eBfw.write(constant.LINESEPARATOR+constant.LINESEPARATOR+constant.LINESEPARATOR);
        }
        
        if(wfFlag){
            msgPublish.msgPublisher("Start2内发现新文件,即将开始下载。",0,0);
        }else{
            msgPublish.msgPublisher("Start2内没有发现任何新文件。",0,0);
        }
        return true;
    }
    
}
