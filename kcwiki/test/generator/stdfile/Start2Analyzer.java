/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.test.generator.stdfile;

        
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import moe.kcwiki.database.Ship;
import moe.kcwiki.database.SlotItem;
import moe.kcwiki.database.Furniture;
import moe.kcwiki.database.UseItem;
import moe.kcwiki.database.PayItem;
import moe.kcwiki.database.MapInfo;
import moe.kcwiki.database.Bgm;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.database.Mission;
import moe.kcwiki.monitor.start2.Start2Api;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.handler.massage.msgPublish;

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
    
    public boolean ReadNewFile(String url) throws IOException, Exception{
        new moe.kcwiki.database.DBCenter().reset();
        String startdata;
        startdata=new Start2Api().GetStart2Api(url);
        
        
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
            msgPublish.msgPublisher("createstdfile-解析JSON时发生错误",0,-1);
        }       
        //JOptionPane.showMessageDialog(null,"运行完成" , "错误", JOptionPane.ERROR_MESSAGE);
        return true;
    }
 
}