/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.test.generator.stdfile;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import moe.kcwiki.database.Ship;
import moe.kcwiki.initializer.MainServer;

/**
 *
 * @author x5171
 */
public class autoSetStdver {
    private HashMap<String, Ship> shipdb1 = new LinkedHashMap<>();  
    private HashMap<String, Ship> shipdb2 = new LinkedHashMap<>();  
    private HashMap<String, Ship> shipdb3 = new LinkedHashMap<>();  
    private String newfile=null;
    private String oldfile=null;
                
    public boolean setnewStdver() throws UnsupportedEncodingException, IOException, Exception{
        shipdb1.clear();
        shipdb2.clear();
        shipdb3.clear();
        newfile=MainServer.getDataFolder()+File.separator+"ShipgraphstdDB.txt";
        oldfile=MainServer.getDataFolder()+File.separator+"ShipgraphstdDB backup.txt";
        
        if(!(new File(newfile).exists())){
            JOptionPane.showMessageDialog(null,"找不到基本立绘库文件，无法使用自动生成工具。" , "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        new moe.kcwiki.database.DBCenter().reset();
        
        if(new Start2Analyzer().ReadNewFile("https://acc.kcwiki.org/start2")){
            for( Map.Entry<String,Ship> map : moe.kcwiki.database.DBCenter.ShipDB.entrySet()){
                if(map.getKey()==null){continue;}
                Ship newship=new Ship();
                newship.setApi_id(map.getValue().getApi_id());
                newship.setApi_name(map.getValue().getApi_name());
                newship.setApi_filename(map.getValue().getApi_filename());
                newship.setApi_version(map.getValue().getApi_version());
                newship.setApi_boko_n(map.getValue().getApi_boko_n());
                newship.setApi_battle_n(map.getValue().getApi_battle_n());
                newship.setApi_weda(map.getValue().getApi_weda());
                shipdb3.put(map.getValue().getApi_id(), newship);
            }
        }
        
        new moe.kcwiki.database.DBCenter().reset();
        if(new Start2Analyzer().ReadNewFile("https://acc.kcwiki.org/start2/prev")){
            for( Map.Entry<String,Ship> map : moe.kcwiki.database.DBCenter.ShipDB.entrySet()){
                if(map.getKey()==null){continue;}
                Ship newship=new Ship();
                newship.setApi_id(map.getValue().getApi_id());
                newship.setApi_name(map.getValue().getApi_name());
                newship.setApi_filename(map.getValue().getApi_filename());
                newship.setApi_version(map.getValue().getApi_version());
                newship.setApi_boko_n(map.getValue().getApi_boko_n());
                newship.setApi_battle_n(map.getValue().getApi_battle_n());
                newship.setApi_weda(map.getValue().getApi_weda());
                shipdb2.put(map.getValue().getApi_id(), newship);
            }
        }
        
        new moe.kcwiki.database.DBCenter().reset();
        String encode=new Encoder().codeString(newfile);
        BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(newfile), encode));
        
        String s=null;
        while((s=nBfr.readLine())!=null){
            Ship newship=new Ship();
            if(s.equals("\r\n")){continue;}
            String[] str=s.split("\t");
            newship.setApi_id(str[0]);
            newship.setApi_name(str[1]);
            newship.setApi_filename(str[2]);
            newship.setApi_version(str[3]);
            newship.setApi_boko_n(str[4]);
            newship.setApi_battle_n(str[5]);
            newship.setApi_weda(str[6]);
            shipdb1.put(str[0], newship);
        }    
        nBfr.close();
        
        makenewStdver();
        return true;
    }
    
    public void makenewStdver() throws UnsupportedEncodingException, FileNotFoundException, IOException{
        new moe.kcwiki.database.DBCenter().reset();
        if(new File(oldfile).exists()){new File(oldfile).delete();}
        new File(newfile).renameTo(new File(oldfile)); 
        
        FileOutputStream eFos=new FileOutputStream(new File(newfile));
        OutputStreamWriter eOsw=new OutputStreamWriter(eFos, "UTF-8");
        BufferedWriter  eBfw=new BufferedWriter(eOsw);

        Ship newship=new Ship();
        for( Map.Entry<String,Ship> map : shipdb3.entrySet()){
            if(map.getKey()==null){continue;}
            
            if(shipdb2.get(map.getKey())==null){
                newship=map.getValue(); 
            }else if(shipdb1.get(map.getKey())==null){
                newship=shipdb2.get(map.getKey()); 
            }else{
                newship=shipdb1.get(map.getKey()); 
                
                String[] s1=shipdb1.get(map.getKey()).getApi_version().substring(2, shipdb1.get(map.getKey()).getApi_version().length()-2).split("\",\"");
                String[] s2=shipdb2.get(map.getKey()).getApi_version().substring(2, shipdb2.get(map.getKey()).getApi_version().length()-2).split("\",\"");
                String[] s3=shipdb3.get(map.getKey()).getApi_version().substring(2, shipdb3.get(map.getKey()).getApi_version().length()-2).split("\",\"");
                
                if(shipdb1.get(map.getKey()).getApi_boko_n().equals(shipdb3.get(map.getKey()).getApi_boko_n())){        
                    if(!(shipdb1.get(map.getKey()).getApi_boko_n().equals(shipdb2.get(map.getKey()).getApi_boko_n()))){         //梅雨立绘        
                        newship=shipdb3.get(map.getKey());
                        newship.setApi_version(s3[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                    }
                }
                
                if(shipdb1.get(map.getKey()).getApi_boko_n().equals(shipdb2.get(map.getKey()).getApi_boko_n())){
                    if(shipdb2.get(map.getKey()).getApi_boko_n().equals(shipdb3.get(map.getKey()).getApi_boko_n())){        //三次没变化
                        newship=shipdb3.get(map.getKey());
                        newship.setApi_version(s3[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                    }else{      //初夏立绘
                        newship=shipdb2.get(map.getKey());
                        newship.setApi_version(s2[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                    }
                }
                
                if(shipdb2.get(map.getKey()).getApi_boko_n().equals(shipdb3.get(map.getKey()).getApi_boko_n())){
                    newship=shipdb3.get(map.getKey());
                    newship.setApi_version(s3[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                }

            }
            if(newship.getApi_version().contains("[\"")){
                eBfw.write(newship.getApi_id()+"\t"+newship.getApi_name()+"\t"+newship.getApi_filename()+"\t"+newship.getApi_version()+"\t"+newship.getApi_boko_n()+"\t"+newship.getApi_battle_n()+"\t"+newship.getApi_weda());
            }else{
                eBfw.write(newship.getApi_id()+"\t"+newship.getApi_name()+"\t"+newship.getApi_filename()+"\t[\""+newship.getApi_version()+"\"]\t"+newship.getApi_boko_n()+"\t"+newship.getApi_battle_n()+"\t"+newship.getApi_weda());
            }
            eBfw.write("\r\n");
        }
        eBfw.close();
        eOsw.close();
        eFos.close();
        JOptionPane. showMessageDialog(null, "基本立绘库已成功生成。", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
