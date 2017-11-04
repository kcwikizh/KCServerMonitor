/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools.swfunpacker.comparator.src;

import com.alibaba.fastjson.JSON;
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
    
    public String getHash(String str) {
        return  DigestUtils.md5Hex(str);
    }
}
