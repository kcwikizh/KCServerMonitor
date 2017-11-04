/*
 * Copyright (C) 2016 VEP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
 * @author VEP
 */
public class setStdver {
    public HashMap<String, Ship> shipdb1 = new LinkedHashMap<>();  
    public HashMap<String, Ship> shipdb2 = new LinkedHashMap<>();  
    public HashMap<String, Ship> shipdb3 = new LinkedHashMap<>();  

                
    public void setnewStdver(String file1,String file2,String file3) throws UnsupportedEncodingException, IOException, Exception{
        shipdb1.clear();
        shipdb2.clear();
        shipdb3.clear();
        
        new moe.kcwiki.database.DBCenter().reset();
        if(file1.contains("ShipgraphstdDB")||file1.contains("shipgraphstddb")){
            String encode=Encoder.codeString(file1);
            try (BufferedReader nBfr = new BufferedReader(new InputStreamReader(new FileInputStream(file1), encode))) {
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
            }
        }else{
            new Start2Analyzer().ReadNewFile(file1);
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
                shipdb1.put(map.getValue().getApi_id(), newship);
            }    
        }
        
        
        new moe.kcwiki.database.DBCenter().reset();
        new Start2Analyzer().ReadNewFile(file2);
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
        
        
        new moe.kcwiki.database.DBCenter().reset();
        new Start2Analyzer().ReadNewFile(file3);
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
        
        makenewStdver();
    }
    
    public void makenewStdver() throws UnsupportedEncodingException, FileNotFoundException, IOException{
        new moe.kcwiki.database.DBCenter().reset();
        String newfile=MainServer.getDataFolder()+File.separator+"shipgraphstddb.txt";
        String oldfile=MainServer.getDataFolder()+File.separator+"shipgraphstddb backup.txt";
        if(new File(oldfile).exists()){new File(oldfile).delete();}
        new File(newfile).renameTo(new File(oldfile)); 
        
        FileOutputStream eFos=new FileOutputStream(new File(newfile));
        OutputStreamWriter eOsw=new OutputStreamWriter(eFos, "UTF-8");
        BufferedWriter  eBfw=new BufferedWriter(eOsw); 
        
        Ship newship;
        
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
                        if(!s1[2].equals(s3[2])){
                            newship.setApi_version(s3[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                        }
                    }
                }
                
                if(shipdb1.get(map.getKey()).getApi_boko_n().equals(shipdb2.get(map.getKey()).getApi_boko_n())){
                    if(shipdb2.get(map.getKey()).getApi_boko_n().equals(shipdb3.get(map.getKey()).getApi_boko_n())){        //三次没变化
                        newship=shipdb3.get(map.getKey());
                        //newship.setApi_version(s3[0]+"\",\""+s3[1]+"\",\""+s3[2]);
                    }else{
                        newship=shipdb2.get(map.getKey());
                        //newship.setApi_version(s2[0]+"\",\""+s1[1]+"\",\""+s1[2]);
                    }
                }
                
                if(shipdb2.get(map.getKey()).getApi_boko_n().equals(shipdb3.get(map.getKey()).getApi_boko_n())){
                    newship=shipdb3.get(map.getKey());
                    if(!s2[2].equals(s3[2])){
                        newship.setApi_version(s3[0]+"\",\""+s2[1]+"\",\""+s2[2]);
                    }
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
        JOptionPane. showMessageDialog(null, "基本立绘库已成功生成。", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
