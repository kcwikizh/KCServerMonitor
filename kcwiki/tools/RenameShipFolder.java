/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.FileUtils; 
import org.apache.commons.io.filefilter.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.database.DBCenter;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;


/**
 *
 * @author user13
 */
public class RenameShipFolder {
    private static final HashMap<String, String> ShipList = new LinkedHashMap<>(); 
    
    public boolean shipFolder(String shipfolder){
        for(String shipid:DBCenter.ShipDB.keySet()){
            ShipList.put("kc"+DBCenter.ShipDB.get(shipid).getApi_filename(), DBCenter.ShipDB.get(shipid).getApi_name());
        }
        
        File[] fileList = new File(shipfolder).listFiles();
        if(fileList == null){
            return true;
        }
        for (File file : fileList) {
            try {
                String newFile=file.getParent()+File.separator+ShipList.get(file.getName())+"-"+file.getName();
                FileUtils.moveDirectory(file,new File(newFile));
            } catch (IOException ex) {
                Logger.getLogger(RenameShipFolder.class.getName()).log(Level.SEVERE, null, ex);
                CatchError.WriteError("RenameShipFolder-shipFolder模块读写时发生IOException错误"+constant.LINESEPARATOR+Arrays.toString(ex.getStackTrace()));
            }
        }
        return true;
    }

}
