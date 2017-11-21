/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools.swfunpacker.comparator.src;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.Encoder;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author VEP
 */
public class scrdiff {
    
    public ArrayList differ(String oldFile,String newFile){
        HashMap<String,String> oldFileData = new LinkedHashMap<>();
        ArrayList<String> diffData = new ArrayList<>();
        BufferedReader nBfr = null;
        List<String> lines = new ArrayList<>();
        
        try {
            if(!oldFile.contains(".as") || !newFile.contains(".as")) return diffData;
            String line;
            if(new File(oldFile).exists()){
                nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(oldFile)), Encoder.codeString(oldFile)));
                while((line=nBfr.readLine())!=null){
                    oldFileData.put(getHash(line), line);
                }
            }
            
            nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(newFile)), Encoder.codeString(newFile)));
            while((line=nBfr.readLine())!=null){
                lines.add(line);
                if(!oldFileData.containsKey(getHash(line))){
                   diffData.add(line); 
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(scrdiff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(scrdiff.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(newFile.contains("DutyConst")){
            //DBCenter.dutyData = JSON.parseObject(sb.toString());
            dutydiffer(lines);
        }
        if(newFile.contains("BattleConsts")){
            //DBCenter.dutyData = JSON.parseObject(sb.toString());
            battlediffer(lines);
            
        }
        return diffData;
    }
    
    public boolean dutydiffer(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        boolean isRecord = false;
        for(String line : lines) {
            if(line.contains("public static const") && line.contains("Object")) {
                isRecord = true;
                sb.append("{");
                continue;
            }
            if(line.contains("};") && isRecord) {
                isRecord = false;
                sb.append("}");
                break;
            }
            if(isRecord)
                sb.append(line);
        }
        DBCenter.dutyData = JSON.parseObject(sb.toString());
        return true;
    }
    
    public boolean battlediffer(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        List<String> structlines = new ArrayList<>();
        boolean isBossData = false;
        boolean isLastGauge = false;
        String indent = "";
                
        for(String line : lines) {
            if(line.contains("if") && line.contains("BossDamaged")) {
                isBossData = true;
                byte[] str = line.getBytes();
                for(int i = 0; i<line.length(); i++){
                    if(str[i] == 32){
                        indent += " ";
                    }else{
                        break;
                    }   
                }
            }
            
            if(line.contains("function") && line.contains("LastGauge")) {
                isLastGauge = true;
                byte[] str = line.getBytes();
                for(int i = 0; i<line.length(); i++){
                    if(str[i] == 32){
                        indent += " ";
                    }else{
                        break;
                    }   
                }
            }
            
            if((isBossData || isLastGauge) && line.startsWith(indent)){
                sb.append(line).append(LINESEPARATOR);
                structlines.add(line);
            }
            
            //if(isRecord && line.startsWith("}", indent.length())) {
            if((isBossData || isLastGauge) && !line.startsWith(indent)) {
                if(isBossData){
                    isBossData = false;
                    getBossData(structlines);
                }
                if(isLastGauge){
                    isLastGauge = false;
                    getLastGauge(structlines);
                }
                indent = "";
                structlines.clear();
            }

        }
        
        return true;
    }
    
    public boolean getLastGauge(List<String> lines) {
        HashMap<String,String> map = new LinkedHashMap<>();
        ArrayList<HashMap<String,String>> tmpList = new ArrayList<>();
        boolean isRecord = false;
        String mapID = "";
        String parameter = "";
        String difficulty = "";
        String mapHP = "";
        String indent = "";
        
        for(String line : lines) {
            
            if(line.contains("var") && line.contains("=") && line.contains("mapID") ) {
                String[] tmp = line.split("=");
                tmp = tmp[0].replace("var", "").split(":");
                parameter = tmp[0].trim();
                continue;
            }
            
            if(line.contains("=") && line.contains("difficulty") ) {
                String[] tmp = line.split("=");
                difficulty = tmp[0].trim();
                continue;
            }
            
            if(line.contains("=") && line.contains("mapHPNow") ) {
                String[] tmp = line.split("=");
                mapHP = tmp[0].trim();
                continue;
            }
            
            
            
            if(line.contains("if") && line.contains(parameter) && line.contains("==")) {
                String tmp = line.split("==")[1].trim();
                if(tmp.endsWith(")")){
                    mapID = tmp.substring(0, tmp.length()-1);
                }else{
                    mapID = tmp;
                }
                isRecord = true;
                byte[] str = line.getBytes();
                for(int i = 0; i<line.length(); i++){
                    if(str[i] == 32){
                        indent += " ";
                    }else{
                        break;
                    }   
                }
            }
            if( isRecord && !line.startsWith(indent)) {
                isRecord = false;
                DBCenter.lastgaugeData.put(mapID, (ArrayList<HashMap<String, String>>) tmpList.clone());
                indent = "";
            }
            if(isRecord && line.contains("&&")){
                String[] value = line.split("&&");
                String[] tmp;
                if(value[0].contains(difficulty)){
                    tmp = value[0].split("==");
                    map.put("difficulty", tmp[1].trim());
                    tmp = value[1].split("=");
                    map.put("mapHP", tmp[1].trim().substring(0, tmp[1].trim().length()-1));
                }else{
                    tmp = value[1].split("==");
                    map.put("difficulty", tmp[1].trim().substring(0, tmp[1].trim().length()-1));
                    tmp = value[0].split("=");
                    map.put("mapHP", tmp[1].trim());
                }
                tmpList.add((HashMap) map.clone());
                map.clear();
            }
        }
        return true;
    }
    
    public boolean getBossData(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        boolean isRecord = false;
        
        for(String line : lines) {
            if(line.contains("return") ) {
                isRecord = true;
                sb.append("{");
                continue;
            }
            if(line.contains("};") && isRecord) {
                isRecord = false;
                sb.append("}");
                JSON.parseObject(sb.toString());
                DBCenter.battleData.add((JSONObject) JSON.parseObject(sb.toString()).clone());
                sb = new StringBuilder();
            }
            if(isRecord){
                String value = line.split(":")[1];
                boolean theend = true;
                if(value.endsWith(",")) {
                    value = value.substring(0, value.length()-1);
                    theend = false;
                }
                if(line.contains(":") && !StringUtils.isNumeric(value) && !value.contains("\"")){
                    sb.append(line.split(":")[0]).append(":").append("\""+value+"\"");
                    if(!theend) {
                        sb.append(",");
                    }
                    continue;
                }
                sb.append(line);
            }
        }
        return true;
    }
    
    public String getHash(String str) {
        return  DigestUtils.md5Hex(str);
    }
    
    public static void main(String[] args) {
        String file = "C:\\Users\\iTeam_VEP\\Desktop\\tset\\scripts\\battle/BattleConsts.as";
        new scrdiff().differ(file, file);
        new moe.kcwiki.web.api.whatsnew().getData();
        System.out.print("");
    }
}
