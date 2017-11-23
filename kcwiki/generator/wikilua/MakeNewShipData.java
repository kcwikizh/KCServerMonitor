/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.generator.wikilua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import moe.kcwiki.database.*;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.constant.constant;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author VEP
 */
public class MakeNewShipData {
    private HashMap<String, Ship> ShipList = new LinkedHashMap<>();  

            
    public boolean WriteFile() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        Set<String> keys = DBCenter.NewShipDB.keySet();
        for (String key : keys){
            if(Integer.parseInt(key) >= 1500){continue;}
            ShipList.put(DBCenter.NewShipDB.get(key).getApi_id(), DBCenter.NewShipDB.get(key));
        }
        if(ShipList.isEmpty()){return false;}
                
        try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getWorksFolder()+File.separator+"lua_ship.json")), "UTF-8"))) {
            
            Set<String> listkeys = ShipList.keySet();
            int count = 0;
            for (String key : listkeys){
                count++;
                if(Integer.parseInt(key) >= 1500){continue;}
                String[] str;
                if(count == 1) {
                    eBfw.write("\t,"+constant.LINESEPARATOR);
                }
                String wikiID = DBCenter.NewShipDB.get(key).getApi_sortno();
                if( wikiID == null || StringUtils.isBlank(wikiID)){
                    wikiID = DBCenter.beforeShipSortNO.get(DBCenter.NewShipDB.get(key).getApi_id()) + "a";
                } 
                eBfw.write("\t[\""+wikiID+"\"] = {"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"ID\"] = "+DBCenter.NewShipDB.get(key).getApi_id()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"图鉴号\"] = "+DBCenter.NewShipDB.get(key).getApi_sortno()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"日文名\"] = \""+DBCenter.NewShipDB.get(key).getApi_name()+"\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"假名\"] = \""+DBCenter.NewShipDB.get(key).getApi_yomi()+"\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"中文名\"] = \"未知\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"舰种\"] = "+DBCenter.NewShipDB.get(key).getApi_stype()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"级别\"] = \"{\"未知型\",-10086}\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"数据\"] = {"+constant.LINESEPARATOR);
                //eBfw.write("\t\t\t[\"耐久\"] = {"+DBCenter.NewShipDB.get(key).getApi_taik().substring(1, DBCenter.NewShipDB.get(key).getApi_taik().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"耐久\"] = {\"?\",\"?\"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"火力\"] = {"+DBCenter.NewShipDB.get(key).getApi_houg().substring(1, DBCenter.NewShipDB.get(key).getApi_houg().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"雷装\"] = {"+DBCenter.NewShipDB.get(key).getApi_raig().substring(1, DBCenter.NewShipDB.get(key).getApi_raig().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"对空\"] = {"+DBCenter.NewShipDB.get(key).getApi_tyku().substring(1, DBCenter.NewShipDB.get(key).getApi_tyku().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"装甲\"] = {"+DBCenter.NewShipDB.get(key).getApi_souk().substring(1, DBCenter.NewShipDB.get(key).getApi_souk().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"对潜\"] = {\"?\",\"?\"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"回避\"] = {\"?\",\"?\"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"索敌\"] = {\"?\",\"?\"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"运\"] = {"+DBCenter.NewShipDB.get(key).getApi_luck().substring(1, DBCenter.NewShipDB.get(key).getApi_luck().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"速力\"] = "+DBCenter.NewShipDB.get(key).getApi_soku()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"射程\"] = "+DBCenter.NewShipDB.get(key).getApi_leng()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"稀有度\"] = "+DBCenter.NewShipDB.get(key).getApi_backs()+constant.LINESEPARATOR);
                eBfw.write("\t\t},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"装备\"] = {"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"格数\"] = "+DBCenter.NewShipDB.get(key).getApi_slot_num()+","+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"搭载\"] = {"+DBCenter.NewShipDB.get(key).getApi_maxeq().substring(1, DBCenter.NewShipDB.get(key).getApi_maxeq().length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t\t[\"初期装备\"] = \"-10086\""+constant.LINESEPARATOR);
                eBfw.write("\t\t},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"获得\"] = {[\"掉落\"] = -10086,[\"改造\"] = -10086,[\"建造\"] = -10086,[\"时间\"] = "+DBCenter.NewShipDB.get(key).getApi_buildtime()+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"消耗\"] = {[\"燃料\"] = "+DBCenter.NewShipDB.get(key).getApi_fuel_max()+",[\"弹药\"] = "+DBCenter.NewShipDB.get(key).getApi_bull_max()+"},"+constant.LINESEPARATOR);
                str=DBCenter.NewShipDB.get(key).getApi_powup().split(",");
                eBfw.write("\t\t[\"改修\"] = {[\"火力\"] = "+str[0].substring(1, str[0].length())+",[\"雷装\"] = "+str[1]+",[\"对空\"] = "+str[2]+",[\"装甲\"] = "+str[3].substring(0, str[3].length()-1)+"},"+constant.LINESEPARATOR);
                str=DBCenter.NewShipDB.get(key).getApi_broken().split(",");
                eBfw.write("\t\t[\"解体\"] = {[\"燃料\"] = "+str[0].substring(1, str[0].length())+",[\"弹药\"] = "+str[1]+",[\"钢材\"] = "+str[2]+",[\"铝\"] = "+str[3].substring(0, str[3].length()-1)+"},"+constant.LINESEPARATOR);
                String aftershipsortno;
                if(!DBCenter.NewShipDB.get(key).getApi_aftershipid().equals("0")){
                    aftershipsortno=DBCenter.NewShipDB.get(DBCenter.NewShipDB.get(key).getApi_aftershipid()).getApi_sortno();
                }else{
                    aftershipsortno="0";
                }
                eBfw.write("\t\t[\"改造\"] = {[\"等级\"] = "+DBCenter.NewShipDB.get(key).getApi_afterlv()+",[\"弹药\"] = "+DBCenter.NewShipDB.get(key).getApi_afterbull()+",[\"钢材\"] = "+DBCenter.NewShipDB.get(key).getApi_afterfuel()+",[\"改装前\"] = \"\""+",[\"改装后\"] = \"\""+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"画师\"] = \"-10086\""+","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"声优\"] = \"-10086\""+constant.LINESEPARATOR);
                if(count == keys.size()){
                    eBfw.write("\t}"+constant.LINESEPARATOR);  
                }else{
                    eBfw.write("\t},"+constant.LINESEPARATOR);  
                }
            } 
        }
        msgPublish.msgPublisher("舰娘lua文件创建完毕",0, 0);
        return true;
    }
    
}
