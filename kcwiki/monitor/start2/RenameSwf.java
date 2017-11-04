/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.monitor.start2;

import java.util.HashMap;
import java.util.LinkedHashMap;
import moe.kcwiki.database.Ship;

/**
 *
 * @author user13
 */
public class RenameSwf {
    private static final HashMap<String, Ship> ShipList = new LinkedHashMap<>(); 
    /*
    public boolean shipSwf111(){
        moe.kcwiki.init.Start2DataThread.addJob();
        new Thread() {  //创建新线程用于下载
            @Override
            public void run() {
                String downloadpath=MainServer.getDownloadFolder()+File.separator+"resources"+File.separator+"swf"+File.separator+"ships";
                String shipFile;
                String filename;
                
                if(!DBCenter.NewShipDB.isEmpty()){
                    for( Map.Entry<String,Ship> map : DBCenter.NewShipDB.entrySet()){
                        ShipList.put(map.getKey(), map.getValue());
                    }
                }
                if(!DBCenter.SuspectShipData.isEmpty()){
                    for( Map.Entry<String,String> map : DBCenter.SuspectShipData.entrySet()){
                        ShipList.put(map.getKey(), DBCenter.ShipDB.get(map.getKey()));
                    }
                }
                
                if(!ShipList.isEmpty()){  //Ship
                    for( Map.Entry<String,Ship> map : ShipList.entrySet()){
                        if(map.getKey()==null){continue;}
                        shipFile=downloadpath+File.separator+map.getValue().getApi_filename()+".swf";
                        String newname=downloadpath+File.separator+map.getValue().getApi_filename()+"-"+map.getValue().getApi_name()+".swf";
                        
                        if(new File(shipFile).exists()){
                            new File(shipFile).renameTo(new File(newname));
                        }
                    }
                }
                moe.kcwiki.massagehandler.msgPublish.msgPublisher("舰娘立绘重命名子线程运行结束",0,1);
                moe.kcwiki.init.Start2DataThread.finishJob();
            }
        }.start();
        return true;
    }
    */
}
