/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.swfunpacker.comparator.src;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.Encoder;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author VEP
 */
public class scrdiff {
    public HashMap differ(String oldFile,String newFile){
        HashMap<String,String> oldFileData = new LinkedHashMap<>();
        HashMap<String,String> diffData = new LinkedHashMap<>();
        BufferedReader nBfr = null;
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
            int count = 0;
            while((line=nBfr.readLine())!=null){
                count++;
                if(!oldFileData.containsKey(getHash(line))){
                   diffData.put(getHash(line), line); 
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(scrdiff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(scrdiff.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(newFile.contains("DutyConst")){
            JSON.toJSONString(diffData);
            return diffData;
        }
        return diffData;
    }
    
    public String getHash(String str) {
        return  DigestUtils.md5Hex(str);
    }
}
