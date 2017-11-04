/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import moe.kcwiki.database.DBCenter;
import static moe.kcwiki.database.DBCenter.swfSrcPatch;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.monitor.start2.Start2Api;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;

/**
 *
 * @author VEP
 */
public class srcdiff {
    private StringBuilder sb = null;
    private HashMap<String,JSONObject> slotitemMap = new HashMap<>();
    private HashMap<String,JSONObject> useitemmMap = new HashMap<>();
    private JSONObject start2 = JSON.parseObject(new Start2Api().GetStart2Api(MainServer.getNewstart2()));
    //private JSONObject start2 = JSON.parseObject(new Start2Api().GetStart2Api("https://acc.kcwiki.org/start2/"));
    
    public String getData() {
        sb = new StringBuilder();
        if(swfSrcPatch.isEmpty()){
            this.addString("<!DOCTYPE html><html><body>");
            this.addString("没有发现存在差异的文件或者抓包程序未运行，请等待后台抓包进程执行。");
            this.addString("</body></html>");
        } else {
            readJSON();
            this.addString(LINESEPARATOR);
            this.addString("<h1>新任务情报：</h1> "+LINESEPARATOR);
            JSONObject dutyList = DBCenter.dutyData;
            List<String> updateList = new ArrayList<>();
            List<String> lostList = new ArrayList<>();
            String regEx="[^0-9]";   
            Pattern p = Pattern.compile(regEx);   
            Matcher m;   
            for(String key:dutyList.keySet()){
                JSONObject duty = (JSONObject) dutyList.get(key);
                this.addString("任务id：\t"+key);
                this.addString("任务完成消息：\t"+duty.getString("message1"));
                
                List<String> update = duty.getObject("update",List.class);
                if(update != null) {
                    if(update.size() > 1){
                        this.addString("以下装备or道具可能只能获得其中一个，请以游戏为准。");
                    }
                    for(String updatekey:update) {
                        updateList.add(updatekey);
                        switch(updatekey){
                            case "slotitem":
                                String from = duty.getString("item_from");
                                if(from == null) continue;
                                if(!from.equals("-1")) {
                                    m = p.matcher(from);
                                    this.addString("消耗装备：\t"+slotitemMap.get(m.replaceAll("").trim()).getString("api_name"));
                                }
                                String to = duty.getString("item_to");
                                if(to == null) continue;
                                if(!to.equals("-1")) {
                                    m = p.matcher(to);
                                    this.addString("获得装备：\t"+slotitemMap.get(m.replaceAll("").trim()).getString("api_name"));
                                }
                                break;
                            case "useitem":
                                from = duty.getString("item_from");
                                if(from == null) continue;
                                if(!from.equals("-1")) {
                                    m = p.matcher(from);
                                    this.addString("消耗道具：\t"+useitemmMap.get(m.replaceAll("").trim()).getString("api_name"));
                                }
                                to = duty.getString("item_to");
                                if(to == null) continue;
                                if(!to.equals("-1")) {
                                    m = p.matcher(to);
                                    this.addString("获得道具：\t"+useitemmMap.get(m.replaceAll("").trim()).getString("api_name"));
                                }
                                break;
                        }
                    }
                }
                
                JSONObject lost = (JSONObject) duty.get("lost");
                if(lost != null) {
                    for(String lostkey:lost.keySet()) {
                        lostList.add(lostkey);
                        switch(lostkey){
                            case "slotitem":
                                JSONArray lostslotitem = lost.getJSONArray(lostkey);
                                for(Object slotitemkey:lostslotitem) {
                                    JSONObject lostobject = (JSONObject) slotitemkey;
                                    String id = lostobject.getString("id").trim();
                                    this.addString("消耗装备：\t"+slotitemMap.get(id).getString("api_name"));
                                    this.addString("消耗数量：\t"+lostobject.getString("count").trim());   
                                }
                                break;
                            case "useitem":
                                lostslotitem = lost.getJSONArray(lostkey);
                                for(Object slotitemkey:lostslotitem) {
                                    JSONObject lostobject = (JSONObject) slotitemkey;
                                    String id = lostobject.getString("id").trim();
                                    this.addString("消耗道具：\t"+useitemmMap.get(id).getString("api_name"));
                                    this.addString("消耗数量：\t"+lostobject.getString("count").trim());   
                                }
                                break;
                        }
                    }
                }
                this.addString(LINESEPARATOR);
                this.addString("-------------------------------------");
                this.addString(LINESEPARATOR);
            }
            this.addString(LINESEPARATOR);
            this.addString(LINESEPARATOR);
            this.addString(LINESEPARATOR);
            this.addString("<h1>完整diff数据：</h1> "+LINESEPARATOR);
            this.addString(JSON.toJSONString(swfSrcPatch));
            this.addString(LINESEPARATOR);
        }
        
        return sb.toString();
    }
    
    public void readJSON(){
        JSONArray List = start2.getJSONArray("api_mst_slotitem");
        for(int i = 0; i < List.size(); i++) {
            JSONObject tmp = List.getJSONObject(i);
            slotitemMap.put(tmp.getString("api_id"), tmp);
        }
        List = start2.getJSONArray("api_mst_useitem");
        for(int i = 0; i < List.size(); i++) {
            JSONObject tmp = List.getJSONObject(i);
            useitemmMap.put(tmp.getString("api_id"), tmp);
        }
    }
    
    public void addData(String str) {
        if(sb == null){
            sb = new StringBuilder();
        }
        this.addString(str);
    }

    
    private void addString(String str) {
        sb.append(str).append(LINESEPARATOR).append("</br>").append(LINESEPARATOR);
    }
}
