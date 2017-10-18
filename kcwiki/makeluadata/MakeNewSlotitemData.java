/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.makeluadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import moe.kcwiki.database.*;
import moe.kcwiki.init.MainServer;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;

/**
 *
 * @author VEP
 */
public class MakeNewSlotitemData {

    public boolean WriteFile() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        if(DBCenter.NewSlotitemDB.isEmpty()){return false;}
        
        try (BufferedWriter eBfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(MainServer.getWorksPath()+File.separator+"lua_weapon.json")), "UTF-8"))) {
            Set<String> keys = DBCenter.NewSlotitemDB.keySet();
            int count = 0;
            for (String key : keys){
                count++;
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_id()) >= 500){continue;}
                String id;
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_id())<10){
                    id="00"+DBCenter.NewSlotitemDB.get(key).getApi_id();
                }else if(9<Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_id())&&Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_id())<100){
                    id="0"+DBCenter.NewSlotitemDB.get(key).getApi_id();
                }else{
                    id=DBCenter.NewSlotitemDB.get(key).getApi_id();
                }
                if(count == 1) {
                    eBfw.write("\t,"+constant.LINESEPARATOR);
                }
                eBfw.write("\t[\""+id+"\"] = {"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"日文名\"] = \""+DBCenter.NewSlotitemDB.get(key).getApi_name()+"\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"中文名\"] = \"\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"类别\"] = {"+DBCenter.NewSlotitemDB.get(key).getApi_type().substring(1, DBCenter.NewSlotitemDB.get(key).getApi_type().length()-1)+"},"+constant.LINESEPARATOR);
                String rare="";
                for(int i=0;i<=Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_rare());i++){
                    rare=rare+"☆";
                }
                eBfw.write("\t\t[\"稀有度\"] = \""+rare+"\","+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"状态\"] = {[\"开发\"] = 0,[\"改修\"] = 0,[\"更新\"] = 0,[\"熟练\"] = 0},"+constant.LINESEPARATOR);
                String state="";
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_taik())>0){
                    state=state+"[\"耐久\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_taik()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_souk())>0){
                    state=state+"[\"装甲\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_souk()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_houg())>0){
                    state=state+"[\"火力\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_houg()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_raig())>0){
                    state=state+"[\"雷装\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_raig()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_soku())>0){
                    state=state+"[\"速力\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_soku()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_baku())>0){
                    state=state+"[\"爆装\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_baku()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_tyku())>0){
                    state=state+"[\"対空\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_tyku()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_tais())>0){
                    state=state+"[\"対潜\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_tais()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_atap())>0){
                    state=state+"[\"(0)\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_atap()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_houm())>0){
                    state=state+"[\"命中\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_houm()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_raim())>0){
                    state=state+"[\"雷撃命中\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_raim()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_houk())>0){
                    state=state+"[\"回避\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_houk()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_raik())>0){
                    state=state+"[\"雷撃回避\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_raik()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_bakk())>0){
                    state=state+"[\"爆撃回避\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_bakk()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_saku())>0){
                    state=state+"[\"索敵\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_saku()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_sakb())>0){
                    state=state+"[\"索敵妨害\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_sakb()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_luck())>0){
                    state=state+"[\"運\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_luck()+",";
                }
                if(Integer.parseInt(DBCenter.NewSlotitemDB.get(key).getApi_leng())>0){
                    state=state+"[\"射程\"] = "+DBCenter.NewSlotitemDB.get(key).getApi_leng()+",";
                }
                if(state.equals("")){
                    eBfw.write("\t\t[\"属性\"] = {[\"射程\"] = \"无\"},"+constant.LINESEPARATOR);
                }else{
                    eBfw.write("\t\t[\"属性\"] = {"+state.substring(0, state.length()-1)+"},"+constant.LINESEPARATOR);
                }
                String broken[]=DBCenter.NewSlotitemDB.get(key).getApi_broken().split(",");
                eBfw.write("\t\t[\"废弃\"] = {[\"燃料\"] = "+broken[0].substring(1, broken[0].length())+",[\"弹药\"] = "+broken[1]+",[\"钢材\"] = "+broken[2]+",[\"铝\"] = "+broken[3].substring(0, broken[0].length()-1)+"},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"装备适用\"] = {},"+constant.LINESEPARATOR);
                eBfw.write("\t\t[\"备注\"] = \"\""+constant.LINESEPARATOR);
                if(count == keys.size()){
                    eBfw.write("\t}"+constant.LINESEPARATOR);  
                }else{
                    eBfw.write("\t},"+constant.LINESEPARATOR);  
                }
            }
        }
        msgPublish.msgPublisher("装备lua文件创建完毕", 0,0);
        return true;
    }
}
