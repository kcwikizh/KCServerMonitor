/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.swfunpacker.coredecryptor;

import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.initializer.GetModifiedDataThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.database.*;
import moe.kcwiki.initializer.Start2DataThread;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.handler.thread.corePool;
import static moe.kcwiki.handler.thread.start2dataPool.getTaskNum;
import moe.kcwiki.tools.Encoder;
import moe.kcwiki.tools.constant.constant;

/**
 *
 * @author VEP
 */
public class CoreDecrypt {
    private final String tppath;
    private final String ffpath;
    private String mapCode;
    private String soundCode;
    private BufferedReader Bfr;
    private final String kcwikiserver;
    private boolean isMapNumNull=false;
    private final Runtime runtime;
    public static HashMap<String, String> shipAddressList = new LinkedHashMap<>();
    public static HashMap<String, String> shipDataList = new LinkedHashMap<>();
    public static HashMap<String, String> mapAddressList = new LinkedHashMap<>();
    private static String timeStamp;
    
    public CoreDecrypt(){
        this.tppath=MainServer.getTempFolder();
        this.ffpath=MainServer.getFfdecFolder();
        this.kcwikiserver=MainServer.getKcwikiServerAddress();
        runtime=Runtime.getRuntime();
    }
    
    
    public boolean getData(String map,String sound){
        GetModifiedDataThread.addJob();
        final int taskID = getTaskNum();
            corePool.addTask(new Callable<Integer>() {
                @Override
                public Integer call() {
                
                try {
                    Bfr = new BufferedReader(new InputStreamReader(new FileInputStream(map), Encoder.codeString(map)));
                    String line;
                    while ((line=Bfr.readLine())!=null) {
                        if(line.contains("RND:Array")){
                            int i=line.indexOf("RND:Array");   //取得子串的初始位置
                            mapCode=line.substring(i+13,line.length()-2);
                            break;
                        }
                    }
                    Bfr.close();
                    msgPublish.msgPublisher("地图加密数据读取成功",0,1);
                    
                    Bfr = new BufferedReader(new InputStreamReader(new FileInputStream(sound), Encoder.codeString(sound)));
                    while ((line=Bfr.readLine())!=null) {
                        if(line.contains("vcKey:Array")){
                            int i=line.indexOf("vcKey:Array");   //取得子串的初始位置
                            soundCode=line.substring(i+15,line.length()-2);
                            break;
                        }
                    }
                    Bfr.close();
                    msgPublish.msgPublisher("语音加密数据读取成功",0,1);
                    
                    ecryptMap(mapCode,moe.kcwiki.initializer.MainServer.getMapid());
                    AnalyzeList.shipData();
                    new moe.kcwiki.downloader.DLThread().modifieddata(MainServer.getDownloadFolder()+File.separator+"Maps",ffpath, 1);
                    
                    while(!moe.kcwiki.initializer.Start2DataThread.isHasStart2()){
                        if(!GetModifiedDataThread.getTimeStamp().equals(timeStamp)){
                            msgPublish.msgPublisher("语音下载线程的TaskID无法与实时TaskID匹配，自动停止下载。",0,-1);
                            return taskID;
                        }
                        if(MainServer.isStopScanner() && !Start2DataThread.isHasStart2()){
                                msgPublish.msgPublisher("语音下载线程已停止。",0,0);
                            return taskID;
                        }
                        try {
                            sleep(30*1000);
                        } catch (InterruptedException ex) {
                            msgPublish.msgPublisher("moe.kcwiki.decryptcore-CoreDecrypt-getData:InterruptedException",0,-1);
                            Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
                            return taskID;
                        }
                    }
                    ecryptSound(soundCode); 
                    AnalyzeList.voiceData(soundCode);
                    msgPublish.msgPublisher("开始下载语音数据。",0,0);
                    new moe.kcwiki.downloader.DLThread().modifieddata(MainServer.getDownloadFolder()+File.separator+"ShipVoice",ffpath, 2);
                } catch (UnsupportedEncodingException ex) {
                    msgPublish.msgPublisher("moe.kcwiki.decryptcore-CoreDecrypt-getData:UnsupportedEncodingException",0,-1);  
                    Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
                    return taskID;
                } catch (IOException ex) {
                    msgPublish.msgPublisher("moe.kcwiki.decryptcore-CoreDecrypt-getData:IOException",0,-1);
                    Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
                    return taskID;
                }   catch (Exception ex) {
                        Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
                    }
                msgPublish.msgPublisher("decryptCore\t子线程运行完毕",0,0);
                GetModifiedDataThread.finishJob();
                return taskID;
            }
        },taskID,"moe.kcwiki.decryptcore-CoreDecrypt-getData"); 
        return true;
    }
    
    public boolean ecryptMap(String str,String mapId) {
        String[] sourceStrArray = str.split(",");
        java.util.ArrayList<String>  memberList = new  java.util.ArrayList<>();
        java.util.List<String[][]> nmemberlist = new ArrayList<>();
        if((mapId.equals(""))){isMapNumNull=true;}
        for (String sourceStr:sourceStrArray) {
            //JOptionPane.showMessageDialog(null,sourceStr,"sourceStr:", JOptionPane.WARNING_MESSAGE);
            if(Integer.parseInt(sourceStr)> 10000){
                sourceStr=Long.toHexString(Integer.parseInt(sourceStr)); //把小写字母换为大写：sourceStr=Long.toHexString(Integer.parseInt(sourceStr)).toUpperCase();
                /*
                StringBuffer buff = new StringBuffer();
                for (int i = sourceStr.length(); i > 0; i -= 2) {
                buff.append(sourceStr.substring(i - 2, i));
                }
                sourceStr=buff.toString();*/
                //JOptionPane.showMessageDialog(null,sourceStr,"", JOptionPane.WARNING_MESSAGE);
            }
            
            memberList.add(sourceStr.substring(0, 2));
            memberList.add(sourceStr.substring(2, 4));
            //System.out.printf("\",\""+sourceStr.substring(0,2)+"\",\""+sourceStr.substring(2, 4));
        }
        int area_h = 0;
        String hash_h = "";
        for(String  member:memberList ){
            int rndint=Integer.parseInt(member, 16);
            //JOptionPane.showMessageDialog(null,rndint, "",JOptionPane.WARNING_MESSAGE);
            if(rndint<90) {
                if(!"".equals(hash_h)){
                    String[][] s=new String[1][2];
                    
                    s[0][0]= String.valueOf(Integer.parseInt(String.valueOf(area_h), 16));
                    s[0][1]=hash_h;
                    nmemberlist.add(s);
                    //JOptionPane.showMessageDialog(null,s[0][0]+","+s[0][1],"", JOptionPane.WARNING_MESSAGE);
                    hash_h = "";
                }
                area_h = Integer.parseInt(Long.toHexString(Integer.parseInt(member)));
            }else{
                hash_h += (char)(rndint);
                //JOptionPane.showMessageDialog(null,hash_h, "",JOptionPane.WARNING_MESSAGE);
            }
        }
        String[][] s=new String[1][2];
        s[0][0]=String.valueOf(area_h);
        s[0][1]=hash_h;
        nmemberlist.add(s);
        //JOptionPane.showMessageDialog(null,s[0][0]+","+s[0][1],"", JOptionPane.WARNING_MESSAGE);
        try (FileWriter fout = new FileWriter(new File(tppath+File.separator+"Core-Map.txt"))) {
            fout.write(new Date()+constant.LINESEPARATOR+constant.LINESEPARATOR+"新活动地图下载链接："+constant.LINESEPARATOR+constant.LINESEPARATOR);
            int n=0;
            String data;
            for(int i=0;i<nmemberlist.size();i++){
                s=nmemberlist.get(i);
                
                //JOptionPane.showMessageDialog(null,Integer.parseInt(s[0][0])+",33","", JOptionPane.WARNING_MESSAGE);
                if(isMapNumNull!=true){
                    if (Integer.parseInt(s[0][0]) == Integer.parseInt(mapId))  {
                        n++;
                        data=kcwikiserver+"resources/swf/map/"+s[0][1]+".swf";
                        fout.write("第"+n+"张地图："+constant.LINESEPARATOR+data+constant.LINESEPARATOR);
                        //mapAddressList.put(data,s[0][1]+".swf");
                        mapAddressList.put(data,"第"+n+"张地图"+".swf");
                        //JOptionPane.showMessageDialog(null,s[0][0]+","+s[0][1],"", JOptionPane.WARNING_MESSAGE);
                    }
                }else{
                    n++;
                    data=kcwikiserver+"resources/swf/map/"+s[0][1]+".swf";
                    fout.write("第"+n+"张地图："+constant.LINESEPARATOR+data+constant.LINESEPARATOR);
                    //mapAddressList.put(data,s[0][1]+"-第"+n+"张地图.swf");
                    mapAddressList.put(data,"第"+n+"张地图"+".swf");
                }
            }
            fout.close();
        } catch (IOException ex) {
            msgPublish.msgPublisher("moe.kcwiki.decryptcore-CoreDecrypt-ecryptMap:IOException",0,-1);
            Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        /*
        if(!mapAddressList.isEmpty()){
        runtime.exec("cmd.exe /c start \"\" \""+tppath+File.separator+"Core-Map.txt"+"\"");
        }
        */
        return true;
    }
    
    @SuppressWarnings("element-type-mismatch")
    public boolean ecryptSound(String str) {
        String[] sourceStrArray = str.split(",");
        java.util.ArrayList<String>  memberList = new  java.util.ArrayList<>();
        String soundName;
        String data;
        String shipfilename;
            
        //int i=0;
            
        try (FileWriter fout = new FileWriter(new File(tppath+File.separator+"Core-ShipVoice.txt"))) {
            fout.write(new Date()+constant.LINESEPARATOR+constant.LINESEPARATOR+"舰娘音频下载链接："+constant.LINESEPARATOR+constant.LINESEPARATOR); 
            for(Map.Entry<String,Ship> item : DBCenter.NewShipDB.entrySet()){
            //for(Map.Entry<String,Ship> item : DBCenter.ShipDB.entrySet()){    //采集全部vioce时使用
                if(item==null){continue;}
                int voiceId=0;
                String ship=item.getKey();
                //if(Integer.valueOf(ship)>500&&Integer.valueOf(ship)<800){continue;}
                if(Integer.valueOf(ship) >= 1500){continue;}
                for (String one:sourceStrArray){
                    voiceId++;
                    if(voiceId==sourceStrArray.length){break;}
                    soundName=String.valueOf((Integer.parseInt(ship) + 7) * 17 * (Integer.parseInt(sourceStrArray[voiceId]) - Integer.parseInt(sourceStrArray[voiceId - 1])) % 99173 + 100000);
                    memberList.add(soundName);
                } 
                shipfilename=item.getValue().getApi_filename()+"-"+item.getValue().getApi_name();

                fout.write(constant.LINESEPARATOR+item.getValue().getApi_name()+"的语音如下："+constant.LINESEPARATOR);
                int count=1;
                for(String soundNo:memberList){
                    data=kcwikiserver+"sound/kc"+item.getValue().getApi_filename()+"/"+soundNo+".mp3";
                    fout.write("第"+count+"个语音："+constant.LINESEPARATOR+data+constant.LINESEPARATOR);
                    //fout.write(data+constant.LINESEPARATOR);  //采集vioce文件地址时使用
                    shipAddressList.put(data,String.valueOf(count));
                    shipDataList.put(data, item.getKey());
                    count++;
                }
                fout.write(constant.LINESEPARATOR);
                memberList.clear();
            }
            for(Map.Entry<String,String> item : DBCenter.SuspectSoundData.entrySet()){
            //for(Map.Entry<String,Ship> item : DBCenter.ShipDB.entrySet()){    //采集全部vioce时使用
                if(item==null){continue;}
                int voiceId=0;
                String ship=item.getKey();
                if(Integer.valueOf(ship)>1500){continue;}
                for (String one:sourceStrArray){
                    voiceId++;
                    if(voiceId==sourceStrArray.length){break;}
                    soundName=String.valueOf((Integer.parseInt(ship) + 7) * 17 * (Integer.parseInt(sourceStrArray[voiceId]) - Integer.parseInt(sourceStrArray[voiceId - 1])) % 99173 + 100000);
                    memberList.add(soundName);
                } 
                
                shipfilename=DBCenter.ShipDB.get(item.getKey()).getApi_filename()+"-"+DBCenter.ShipDB.get(item.getKey()).getApi_name();

                fout.write(constant.LINESEPARATOR+DBCenter.ShipDB.get(item.getKey()).getApi_name()+"的语音如下："+constant.LINESEPARATOR);
                int count=1;
                for(String soundNo:memberList){
                    data=kcwikiserver+"sound/kc"+DBCenter.ShipDB.get(item.getKey()).getApi_filename()+"/"+soundNo+".mp3";
                    fout.write("第"+count+"个语音："+constant.LINESEPARATOR+data+constant.LINESEPARATOR);
                    //fout.write(data+constant.LINESEPARATOR);  //采集vioce文件地址时使用
                    shipAddressList.put(data,String.valueOf(count));
                    shipDataList.put(data, item.getKey());
                    count++;
                }
                fout.write(constant.LINESEPARATOR);
                memberList.clear();
            }
            for(Map.Entry<String,String> item : DBCenter.SuspectBaseSoundData.entrySet()){
            //for(Map.Entry<String,Ship> item : DBCenter.ShipDB.entrySet()){    //采集全部vioce时使用
                if(item==null){continue;}
                int voiceId=0;
                String ship=item.getKey();
                if(Integer.valueOf(ship)>1500){continue;}
                for (String one:sourceStrArray){
                    voiceId++;
                    if(voiceId==sourceStrArray.length){break;}
                    soundName=String.valueOf((Integer.parseInt(ship) + 7) * 17 * (Integer.parseInt(sourceStrArray[voiceId]) - Integer.parseInt(sourceStrArray[voiceId - 1])) % 99173 + 100000);
                    memberList.add(soundName);
                } 
                shipfilename=DBCenter.ShipDB.get(item.getKey()).getApi_filename()+"-"+DBCenter.ShipDB.get(item.getKey()).getApi_name();
                //System.out.print(item.getKey()+constant.LINESEPARATOR);
                
                fout.write(constant.LINESEPARATOR+DBCenter.ShipDB.get(item.getKey()).getApi_name()+"的语音如下："+constant.LINESEPARATOR);
                int count=1;
                for(String soundNo:memberList){
                    data=kcwikiserver+"sound/kc"+DBCenter.ShipDB.get(item.getKey()).getApi_filename()+"/"+soundNo+".mp3";
                    fout.write("第"+count+"个语音："+constant.LINESEPARATOR+data+constant.LINESEPARATOR);
                    //fout.write(data+constant.LINESEPARATOR);  //采集vioce文件地址时使用
                    shipAddressList.put(data,String.valueOf(count));
                    shipDataList.put(data, item.getKey());
                    count++;
                }
                fout.write(constant.LINESEPARATOR);
                memberList.clear();
            }
            fout.close(); 
            /*
            if(!shipAddressList.isEmpty()){
                runtime.exec("cmd.exe /c start \"\" \""+tppath+File.separator+"Core-ShipVoice.txt"+"\""); 
            }
            */
        } catch (IOException ex) { 
            msgPublish.msgPublisher("moe.kcwiki.decryptcore-CoreDecrypt-ecryptSound:IOException",0,-1);
            Logger.getLogger(CoreDecrypt.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * @return the timeStamp
     */
    public static String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param aTimeStamp the timeStamp to set
     */
    public static void setTimeStamp(String aTimeStamp) {
        timeStamp = aTimeStamp;
    }

}
