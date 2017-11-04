/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools.swfunpacker.coredecryptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.database.Ship;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.constant.constant;

/**
 *
 * @author iTeam_VEP
 */
public class AnalyzeList {
    public static void shipData() throws IOException{
        ArrayList unicode=new ArrayList();
        ArrayList rnds=new ArrayList();
        //String str="gmlbignzjwmq";
        //String str="fgyvwqymkekn";
        //String str="gmlbign_zjwmq";
        //String str="fgyvwqymk_ekn";
        //String str="fwy_wlrdttcoc";
        //String str="abcd";
        try (FileWriter fout = new FileWriter(new File(moe.kcwiki.initializer.MainServer.getTempFolder()+File.separator+"Analyze-Ship.txt"))) {
            for(Map.Entry<String,Ship> ship : DBCenter.ShipDB.entrySet()){
            
                char[] chs=ship.getValue().getApi_filename().toCharArray();
                //unicode.add(moe.kcwiki.main.MainServer.getMapid());
                //unicode.add("33");
                for(char ch:chs){
                    int count=Integer.parseInt(String.valueOf(Integer.toHexString(ch)), 16);
                    unicode.add(String.valueOf(Integer.toHexString(count)));
                }
                fout.write(ship.getValue().getApi_id()+"\t"+ship.getValue().getApi_filename()+constant.LINESEPARATOR);
                fout.write(unicode+constant.LINESEPARATOR);

                for(int j=0;j<unicode.size()-1;j++){
                    String rnd=unicode.get(j).toString()+unicode.get(j+1).toString();
                    boolean result=rnd.matches("[0-9]+");
                    System.out.print(rnd+constant.LINESEPARATOR);
                    if(!result){
                        rnds.add(String.valueOf(Integer.parseInt(rnd,16)));
                    }else{
                        rnds.add(String.valueOf(Integer.parseInt(rnd)));
                    }
                    j++;
                }
                fout.write(rnds+constant.LINESEPARATOR+constant.LINESEPARATOR);
                
                unicode.clear();
                rnds.clear();
            }
        }
    }
    
    @SuppressWarnings("element-type-mismatch")
    public static boolean voiceData(String str) {
        String[] sourceStrArray = str.split(",");
        java.util.ArrayList<String>  memberList = new  java.util.ArrayList<>();
        String soundName;
        String data;
        String shipfilename;
            
        int i=0;
            
        try (FileWriter fout = new FileWriter(new File(MainServer.getTempFolder()+File.separator+"Analyze-ShipVoice.txt"))) {
            fout.write(new Date()+constant.LINESEPARATOR+constant.LINESEPARATOR+"舰娘音频下载链接："+constant.LINESEPARATOR+constant.LINESEPARATOR);    
            for(Map.Entry<String,Ship> item : DBCenter.ShipDB.entrySet()){    //采集全部vioce时使用
                int voiceId=0;
                String ship=item.getKey();
                if(Integer.valueOf(ship) >= 1500){continue;}
                for (String ont:sourceStrArray){
                    voiceId++;
                    if(voiceId==sourceStrArray.length){break;}
                    soundName=String.valueOf((Integer.parseInt(ship) + 7) * 17 * (Integer.parseInt(sourceStrArray[voiceId]) - Integer.parseInt(sourceStrArray[voiceId - 1])) % 99173 + 100000);
                    memberList.add(soundName);
                } 
                shipfilename=item.getValue().getApi_filename()+"-"+item.getValue().getApi_name();
                
                
                fout.write(constant.LINESEPARATOR+item.getValue().getApi_name()+"的语音如下："+constant.LINESEPARATOR);
                for(String soundNo:memberList){
                    data="sound/kc"+item.getValue().getApi_filename()+"/"+soundNo+".mp3";
                    fout.write(data+constant.LINESEPARATOR);  //采集vioce文件地址时使用
                }
                fout.write(constant.LINESEPARATOR);
                memberList.clear();
            }
            fout.close();   
            
        } catch (IOException ex) {
            msgPublish.msgPublisher("moe.kcwiki.decryptcore-AnalyzeList:IOException",0,-1);  
            Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    } 
}
