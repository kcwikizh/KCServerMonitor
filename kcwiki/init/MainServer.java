/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.init;

import moe.kcwiki.tools.GetPresetData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dtools.ini.*; 
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;
import static moe.kcwiki.tools.constant.FILESEPARATOR;
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet; 
import moe.kcwiki.tools.CatchError;

/**
 *
 * @author user12
 */
public class MainServer {

    /**
     * @return the privatePath
     */
    public static String getPrivatePath() {
        return privatePath;
    }

    /**
     * @return the tempPath
     */
    public static String getTempPath() {
        return tempPath;
    }

    /**
     * @return the publishPath
     */
    public static String getPublishPath() {
        return publishPath;
    }
    
    
    public MainServer(){}
    
    /**
     * @return the proxyhost
     */
    public static String getProxyhost() {
        return proxyhost;
    }

    /**
     * @return the KcUser
     */
    public static String getKcUser() {
        return KcUser;
    }

    /**
     * @return the KcPassword
     */
    public static String getKcPassword() {
        return KcPassword;
    }

    /**
     * @return the proxyport
     */
    public static int getProxyport() {
        return proxyport;
    }

    /**
     * @return the slotitemno
     */
    public static String getSlotitemno() {
        return slotitemno;
    }

    /**
     * @param aSlotitemno the slotitemno to set
     */
    public static void setSlotitemno(String aSlotitemno) {
        slotitemno = aSlotitemno;
    }

    private static String localpath;
    private static String ffdecFolder;
    private static String tempFolder;
    private static String downloadFolder;
    private static String proxyhost;
    private static int proxyport;  
    private static String kcwikiServerAddress;  
    private static String dmmServerAddress;  
    private static String dmmnetgamecookie; 
    private static String dmmrequestrefreshcookie; 
    private static String oldstart2; 
    private static String newstart2; 
    private static String localoldstart2; 
    private static String mapid; 
    private static String seasonini; 
    private static String slotitemno;
    private static String KcUser;
    private static String KcPassword;
    private static boolean DebugMode = false;
    private static boolean stopScanner = false;
    private static boolean init = false;
    private static String webrootPath;
    private static String dataPath;
    private static String logPath;
    private static String publishPath;
    private static String tempPath;
    private static String privatePath;
    
    public static HashMap<String, String> FileList = new HashMap<>();  
    
    public static boolean init(){
        if(init){return false;}
        try {
            //String path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
            String classPath = MainServer.class.getResource("/").toString();
            String path = java.net.URLDecoder.decode(classPath.substring(5, classPath.length()), "utf-8");
            int lastIndex = path.lastIndexOf("/WEB-INF/classes/") ;
            MainServer.webrootPath = path.substring(0, lastIndex);
            MainServer.localpath = java.net.URLDecoder.decode(getWebrootPath() + FILESEPARATOR + "custom", "utf-8");
            MainServer.dataPath = webrootPath + FILESEPARATOR + "WEB-INF" + FILESEPARATOR + "custom" + FILESEPARATOR + "data";
            MainServer.logPath = webrootPath + FILESEPARATOR + "WEB-INF" + FILESEPARATOR + "custom" + FILESEPARATOR + "log";
            MainServer.publishPath = MainServer.webrootPath + FILESEPARATOR + "Publishing";
            MainServer.privatePath = webrootPath + FILESEPARATOR + "WEB-INF" + FILESEPARATOR + "custom" ;
            
            new CatchError().init();
            readini();
            //readseason();
            init = true;
            sleep(3000);
            return true;
        } catch (UnsupportedEncodingException ex) {
            msgPublish.msgPublisher("UnsupportedEncodingException occurred \t Initialization Failed",0,-1); 
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            msgPublish.msgPublisher("Exception occurred \t Initialization Failed",0,-1); 
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @SuppressWarnings("empty-statement")
    public static void readini() {  
        IniFile iniFile=new BasicIniFile();  
        File file=new File(MainServer.getDataPath()+File.separator+"Preset.ini");  
        IniFileReader rad=new IniFileReader(iniFile,file);  
        try {  
            rad.read();  
            IniSection iniSection = iniFile.getSection("Main");
            MainServer.ffdecFolder = iniSection.getItem("ffdecFolder").getValue();  
            
            while(!new File(MainServer.ffdecFolder).exists()||!MainServer.ffdecFolder.contains("ffdec.jar")){
                msgPublish.msgPublisher(constant.LINESEPARATOR+"检测不到ffdec.jar，请检查文件路径是否正确。",0,-1);  
                return ;
            }
            
            MainServer.DebugMode = iniSection.getItem("debugMode").getValue().equals("1");
            MainServer.tempFolder = getLocalpath()+ File.separator + iniSection.getItem("tempFolder").getValue(); 
            MainServer.downloadFolder = getLocalpath()+ File.separator + iniSection.getItem("downloadFolder").getValue(); 
            MainServer.localoldstart2 = getPrivatePath() + File.separator + iniSection.getItem("localOldstart2").getValue();
            MainServer.kcwikiServerAddress = iniSection.getItem("kcwikiServerAddress").getValue(); 
            if(isDebugMode()){
                MainServer.dmmServerAddress = MainServer.kcwikiServerAddress;
            }else{
                MainServer.dmmServerAddress = iniSection.getItem("dmmServerAddress").getValue(); 
            }
            MainServer.mapid = iniSection.getItem("MapId").getValue(); 
            MainServer.slotitemno = iniSection.getItem("slotitemNo").getValue(); 
            MainServer.newstart2 = iniSection.getItem("newStart2").getValue(); 
            MainServer.oldstart2 = iniSection.getItem("oldStart2").getValue();
            MainServer.seasonini = iniSection.getItem("seasonIni").getValue(); 
            
            if(!(new File(tempFolder).exists()&&new File(tempFolder).isDirectory())){new File(getTempFolder()).mkdirs();}
            if(!(new File(downloadFolder).exists()&&new File(downloadFolder).isDirectory())){new File(getDownloadFolder()).mkdirs();}
            if(!(new File(logPath).exists()&&new File(logPath).isDirectory())){new File(getDownloadFolder()).mkdirs();}
            if(!(new File(publishPath).exists()&&new File(publishPath).isDirectory())){new File(getDownloadFolder()).mkdirs();}
            
            /*
            iniSection = iniFile.getSection("Lua");
            moe.kcwiki.makeluadata.Server.setWikiIdJson(getLocalpath()+File.separator+iniSection.getItem("wikiIdJson").getValue());
            moe.kcwiki.makeluadata.Server.setShipData(getLocalpath()+File.separator+iniSection.getItem("shipData").getValue());
            moe.kcwiki.makeluadata.Server.setSlotitemData(getLocalpath()+File.separator+iniSection.getItem("slotitemData").getValue());
            */
            
            iniSection = iniFile.getSection("DmmLogin");
            MainServer.KcUser = iniSection.getItem("user").getValue(); 
            MainServer.KcPassword = iniSection.getItem("password").getValue(); 
            
            iniSection = iniFile.getSection("Proxy");
            MainServer.proxyport = Integer.parseInt(iniSection.getItem("port").getValue()); 
            MainServer.proxyhost = iniSection.getItem("host").getValue(); 
            
            iniSection = iniFile.getSection("Core");
            moe.kcwiki.unpackswf.Server.setCoremap(iniSection.getItem("coreMapArray").getValue());
            moe.kcwiki.unpackswf.Server.setCoresound(iniSection.getItem("coreSoundArray").getValue());
            
            iniSection = iniFile.getSection("Ship");
            for(IniItem item:iniSection){
                moe.kcwiki.unpackswf.Server.shiprule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("ShipVoice");
            for(IniItem item:iniSection){
                moe.kcwiki.unpackswf.Server.shipvoicerule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("SlotItem");
            for(IniItem item:iniSection){
                moe.kcwiki.unpackswf.Server.slotitemrule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("userList");
            for(IniItem item:iniSection){
                moe.kcwiki.webserver.util.userList.userList.put(item.getName(), item.getValue());
            }
            
            if(!(new File(tempFolder).exists()&&new File(tempFolder).isDirectory())){new File(getTempFolder()).mkdirs();}
            if(!(new File(downloadFolder).exists()&&new File(downloadFolder).isDirectory())){new File(getDownloadFolder()).mkdirs();}
        } catch (IOException e) {
            msgPublish.msgPublisher(constant.LINESEPARATOR+"程序初始化失败，请检查ini文件是否完整。",0,-1);  
        }

    }
    
    private void initFileList(){
        FileList.put(MainServer.getDataPath()+File.separator+"Preset.ini", "http://media.kcwiki.moe/kctoolssyn/Preset.ini");
        FileList.put(MainServer.getDataPath()+File.separator+"season.ini", "http://media.kcwiki.moe/kctoolssyn/season.ini");
        FileList.put(MainServer.getDataPath()+File.separator+"Filedata.txt", "http://media.kcwiki.moe/kctoolssyn/Filedata.txt");
        FileList.put(MainServer.getDataPath()+File.separator+"unknowShip.txt", "http://media.kcwiki.moe/kctoolssyn/unknowShip.txt");
        FileList.put(MainServer.getDataPath()+File.separator+"ShipgraphstdDB.txt", "http://media.kcwiki.moe/kctoolssyn/ShipgraphstdDB.txt");
        FileList.put(MainServer.getDataPath()+File.separator+"oldstart2.json", "http://media.kcwiki.moe/kctoolssyn/oldstart2.json");
    }
    
    
    public void readseason() {  
        String ini[]=new String[2];
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getLocalpath()+File.separator+seasonini)), "UTF-8"));
            while((line=br.readLine()) != null)
            {
                if(line.contains("=")){
                    ini=line.split("=");
                    moe.kcwiki.unpackswf.Server.seasonrule.put(ini[0],ini[1]);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the localpath
     */
    public static String getLocalpath() {
        return localpath;
    }

    /**
     * @return the ffdecFolder
     */
    public static String getFfdecFolder() {
        return ffdecFolder;
    }

    /**
     * @return the tempFolder
     */
    public static String getTempFolder() {
        return tempFolder;
    }

    /**
     * @return the downloadFolder
     */
    public static String getDownloadFolder() {
        return downloadFolder;
    }

    /**
     * @return the kcwikiServerAddress
     */
    public static String getKcwikiServerAddress() {
        return kcwikiServerAddress;
    }

    /**
     * @return the dmmServerAddress
     */
    public static String getDmmServerAddress() {
        return dmmServerAddress;
    }

    /**
     * @return the dmmnetgamecookie
     */
    public static String getDmmnetgamecookie() {
        return dmmnetgamecookie;
    }

    /**
     * @return the dmmrequestrefreshcookie
     */
    public static String getDmmrequestrefreshcookie() {
        return dmmrequestrefreshcookie;
    }

    /**
     * @return the oldstart2data
     */
    public static String getLocaloldstart2data() {
        return localoldstart2;
    }

    /**
     * @return the mapid
     */
    public static String getMapid() {
        return mapid;
    }

    /**
     * @param aMapid the mapid to set
     */
    public static void setMapid(String aMapid) {
        mapid = aMapid;
    }
    
    /**
     * @return the oldstart2
     */
    public static String getOldstart2() {
        return oldstart2;
    }

    /**
     * @return the newstart2
     */
    public static String getNewstart2() {
        return newstart2;
    }

    /**
     * @param aDmmnetgamecookie the dmmnetgamecookie to set
     */
    public static void setDmmnetgamecookie(String aDmmnetgamecookie) {
        dmmnetgamecookie = aDmmnetgamecookie;
    }

    /**
     * @param aDmmrequestrefreshcookie the dmmrequestrefreshcookie to set
     */
    public static void setDmmrequestrefreshcookie(String aDmmrequestrefreshcookie) {
        dmmrequestrefreshcookie = aDmmrequestrefreshcookie;
    }

    /**
     * @return the DebugMode
     */
    public static boolean isDebugMode() {
        return DebugMode;
    }

    /**
     * @return the stopScanner
     */
    public static boolean isStopScanner() {
        return stopScanner;
    }

    /**
     * @param aStopScanner the stopScanner to set
     */
    public static void setStopScanner(boolean aStopScanner) {
        stopScanner = aStopScanner;
    }

    /**
     * @return the webrootPath
     */
    public static String getWebrootPath() {
        return webrootPath;
    }

    /**
     * @return the init
     */
    public static boolean isInit() {
        return init;
    }

    /**
     * @return the dataPath
     */
    public static String getDataPath() {
        return dataPath;
    }

    /**
     * @return the logPath
     */
    public static String getLogPath() {
        return logPath;
    }

    
}
