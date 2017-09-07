/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.objectUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author iTeam_VEP
 */
public class dataBlockUtil {

    /**
     * @return the blockCount
     */
    public int getBlockCount() {
        return blockCount;
    }

    /**
     * @param blockCount the blockCount to set
     */
    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    /**
     * @return the blockList
     */
    public HashMap<Integer, Object> getBlockList() {
        return blockList;
    }

    /**
     * @param blockList the blockList to set
     */
    public void setBlockList(HashMap<Integer, Object> blockList) {
        this.blockList = blockList;
    }
    
    private int blockCount = 1;
    private HashMap<Integer, Object> blockList = new HashMap<>();
    
}
