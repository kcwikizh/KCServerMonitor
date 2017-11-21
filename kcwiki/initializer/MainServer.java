/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.initializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.dtools.ini.*; 
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.tools.constant.constant;
import static moe.kcwiki.tools.constant.constant.FILESEPARATOR;
import static moe.kcwiki.web.view.login.setUserList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author user12
 */

//http://www.cnblogs.com/younggun/archive/2013/12/12/3470821.html
public class MainServer {

    /**
     * @return the date
     */
    public static long getDate() {
        return date;
    }

    /**
     * @return the shinkaislotitemno
     */
    public static String getShinkaislotitemno() {
        return shinkaislotitemno;
    }

    /**
     * @return the Authorization_superuser
     */
    public static String getAuthorization_superuser() {
        return Authorization_superuser;
    }

    /**
     * @return the Authorization_uploadstart2
     */
    public static String getAuthorization_uploadstart2() {
        return Authorization_uploadstart2;
    }

    /**
     * @param aLocaloldstart2 the localoldstart2 to set
     */
    public static void setLocaloldstart2(String aLocaloldstart2) {
        localoldstart2 = aLocaloldstart2;
    }

    /**
     * @param aTempFolder the tempFolder to set
     */
    public static void setTempFolder(String aTempFolder) {
        tempFolder = aTempFolder;
    }

    private static boolean useproxy; 
    private static String proxyhost;
    private static int proxyport;  
    private static String kcwikiServerAddress;  
    private static String dmmServerAddress;  
    private static String oldstart2; 
    private static String newstart2; 
    private static String localoldstart2; 
    private static String mapid;  
    private static String slotitemno;
    private static String shinkaislotitemno;
    private static String dutyno;
    private static String seasonini;
    private static String KcUser;
    private static String KcPassword;
    private static String Authorization_superuser = null;
    private static String Authorization_uploadstart2 = null;
    private static boolean DebugMode = false;
    private static boolean EventMode = false;
    private static boolean stopScanner = false;
    private static boolean init = false;
    private static String webrootPath;
    private static String ffdecFolder;
    private static String dataFolder;
    private static String logFolder;
    private static String publishFolder;
    private static String worksFolder;
    private static String tempFolder;
    private static String downloadFolder;
    private static String previousFolder;
    private static String museumFolder;
    private static Proxy httpproxy = null;
    private static Long zipFolder = null;
    private static Long initDate = null;
    private static HashMap<String,String> worldMap = new LinkedHashMap<>();
    private static ArrayList<String> worldList = new ArrayList<>();
    private static Long date = new Date().getTime();
    
    private static HashMap<String, String> FileList = new HashMap<>();  
    
    
    public static boolean init(boolean reinit){
        if(isInit() || !reinit){return false;}
        try {
            //String path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
            String classPath = MainServer.class.getResource("/").toString();
            String path = null;
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                path = java.net.URLDecoder.decode(classPath.substring(6, classPath.length()), "utf-8");
            } else {
                path = java.net.URLDecoder.decode(classPath.substring(5, classPath.length()), "utf-8");
            }
            int lastIndex = path.lastIndexOf("/WEB-INF/classes/") ;
            if(lastIndex == -1) {
                webrootPath = path.substring(0, path.length() -1);
            } else {
                webrootPath = path.substring(0, lastIndex);
            }
            String publicPath = java.net.URLDecoder.decode(getWebrootPath() + FILESEPARATOR + "custom", "utf-8");
            String privatePath = java.net.URLDecoder.decode(getWebrootPath() + FILESEPARATOR + "WEB-INF" + FILESEPARATOR + "custom", "utf-8"); 
            MainServer.publishFolder = publicPath + FILESEPARATOR + "Publishing";
            MainServer.worksFolder = publicPath + FILESEPARATOR + "works";
            MainServer.dataFolder = privatePath + FILESEPARATOR + "data";
            MainServer.logFolder = privatePath + FILESEPARATOR + "log";
            MainServer.setTempFolder(privatePath+ FILESEPARATOR + "temp"); 
            MainServer.downloadFolder = privatePath+ FILESEPARATOR + "download"; 
            MainServer.previousFolder = privatePath+ FILESEPARATOR + "previousswf"; 
            MainServer.museumFolder = privatePath+ FILESEPARATOR + "previouszip"; 
            FileUtils.deleteDirectory(new File(publicPath));
            
            if(readini()) {
                init = true;
            } else {
                init = false;
            }
            //readseason();
            getWorldlist();
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
    public static boolean readini() {  
        IniFile iniFile=new BasicIniFile();  
        File file=new File(MainServer.dataFolder+File.separator+"Preset.ini");  
        IniFileReader rad=new IniFileReader(iniFile,file);  
        try {  
            rad.read();  
            IniSection iniSection = iniFile.getSection("Main");
            MainServer.ffdecFolder = iniSection.getItem("ffdecFolder").getValue();  
            
            if(!new File(MainServer.ffdecFolder).exists()||!MainServer.ffdecFolder.contains("ffdec.jar")) {
                msgPublish.msgPublisher(constant.LINESEPARATOR+"检测不到ffdec.jar，请检查文件路径是否正确。",0,-1);  
            }
            
            MainServer.DebugMode = iniSection.getItem("debugMode").getValue().equals("1");
            MainServer.EventMode = iniSection.getItem("eventMode").getValue().equals("1");
            MainServer.setLocaloldstart2(MainServer.dataFolder + File.separator + iniSection.getItem("localOldstart2").getValue());
            MainServer.kcwikiServerAddress = iniSection.getItem("kcwikiServerAddress").getValue(); 
            if(isDebugMode()){
                MainServer.dmmServerAddress = MainServer.getKcwikiServerAddress();
            }else{
                MainServer.dmmServerAddress = iniSection.getItem("dmmServerAddress").getValue(); 
            }
            MainServer.mapid = iniSection.getItem("MapId").getValue(); 
            MainServer.slotitemno = iniSection.getItem("slotitemNo").getValue(); 
            MainServer.shinkaislotitemno = iniSection.getItem("shinkaiSlotitemNo").getValue(); 
            MainServer.dutyno = iniSection.getItem("dutyNO").getValue(); 
            MainServer.newstart2 = iniSection.getItem("newStart2").getValue(); 
            MainServer.oldstart2 = iniSection.getItem("oldStart2").getValue();
            MainServer.seasonini = iniSection.getItem("seasonIni").getValue(); 
            
            if(!(new File(MainServer.tempFolder).exists()&&new File(MainServer.tempFolder).isDirectory())){new File(MainServer.tempFolder).mkdirs();}
            if(!(new File(MainServer.dataFolder).exists()&&new File(MainServer.dataFolder).isDirectory())){new File(MainServer.dataFolder).mkdirs();}
            if(!(new File(MainServer.downloadFolder).exists()&&new File(MainServer.downloadFolder).isDirectory())){new File(MainServer.downloadFolder).mkdirs();}
            if(!(new File(MainServer.logFolder).exists()&&new File(MainServer.logFolder).isDirectory())){new File(MainServer.logFolder).mkdirs();}
            if(!(new File(MainServer.publishFolder).exists() || new File(MainServer.publishFolder).isDirectory())){new File(MainServer.publishFolder).mkdirs();}
            if(!(new File(MainServer.worksFolder).exists() || new File(MainServer.worksFolder).isDirectory())){new File(MainServer.worksFolder).mkdirs();}
            if(!(new File(MainServer.previousFolder).exists() || new File(MainServer.previousFolder).isDirectory())){new File(MainServer.previousFolder).mkdirs();}
            if(!(new File(MainServer.museumFolder).exists() || new File(MainServer.museumFolder).isDirectory())){new File(MainServer.museumFolder).mkdirs();}
            
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
            MainServer.useproxy = iniSection.getItem("enable").getValue().equals("1"); 
            MainServer.proxyport = Integer.parseInt(iniSection.getItem("port").getValue()); 
            MainServer.proxyhost = iniSection.getItem("host").getValue(); 
            if(!StringUtils.isBlank(proxyhost)){
                httpproxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyhost, proxyport));
            }
            
            iniSection = iniFile.getSection("Core");
            moe.kcwiki.tools.swfunpacker.Server.setCoremap(iniSection.getItem("coreMapArray").getValue());
            moe.kcwiki.tools.swfunpacker.Server.setCoresound(iniSection.getItem("coreSoundArray").getValue());
            
            iniSection = iniFile.getSection("Ship");
            for(IniItem item:iniSection){
                moe.kcwiki.tools.swfunpacker.Server.shiprule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("ShipVoice");
            for(IniItem item:iniSection){
                if(item.getValue() == null || StringUtils.isBlank(item.getValue()) )
                    moe.kcwiki.tools.swfunpacker.Server.shipvoicerule.put(item.getName(), "");
                else
                    moe.kcwiki.tools.swfunpacker.Server.shipvoicerule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("SlotItem");
            for(IniItem item:iniSection){
                if(item.getValue() == null || StringUtils.isBlank(item.getValue()) )
                    moe.kcwiki.tools.swfunpacker.Server.slotitemrule.put(item.getName(), "");
                else
                    moe.kcwiki.tools.swfunpacker.Server.slotitemrule.put(item.getName(), item.getValue());
            }
            
            iniSection = iniFile.getSection("userList");
            for(IniItem item:iniSection){
                moe.kcwiki.web.model.userList.userList.put(item.getName(), item.getValue());
            }
            setUserList(moe.kcwiki.web.model.userList.getUserList());
            
            iniSection = iniFile.getSection("Authorization");
            MainServer.Authorization_superuser = iniSection.getItem("superuser").getValue(); 
            MainServer.Authorization_uploadstart2 = iniSection.getItem("uploadstart2").getValue(); 
            
        } catch (IOException e) {
            msgPublish.msgPublisher(constant.LINESEPARATOR+"程序初始化失败，请检查ini文件是否完整。",0,-1);  
        }
        return true;
    }
    
    private void initFileList(){
        getFileList().put(MainServer.dataFolder+File.separator+"Preset.ini", "http://media.kcwiki.moe/kctoolssyn/Preset.ini");
        getFileList().put(MainServer.dataFolder+File.separator+"season.ini", "http://media.kcwiki.moe/kctoolssyn/season.ini");
        getFileList().put(MainServer.dataFolder+File.separator+"Filedata.txt", "http://media.kcwiki.moe/kctoolssyn/Filedata.txt");
        getFileList().put(MainServer.dataFolder+File.separator+"unknowShip.txt", "http://media.kcwiki.moe/kctoolssyn/unknowShip.txt");
        getFileList().put(MainServer.dataFolder+File.separator+"ShipgraphstdDB.txt", "http://media.kcwiki.moe/kctoolssyn/ShipgraphstdDB.txt");
        getFileList().put(MainServer.dataFolder+File.separator+"oldstart2.json", "http://media.kcwiki.moe/kctoolssyn/oldstart2.json");
    }
    
    
    public void readseason() {  
        String ini[]=new String[2];
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(MainServer.dataFolder+File.separator+getSeasonini())), "UTF-8"));
            while((line=br.readLine()) != null)
            {
                if(line.contains("=")){
                    ini=line.split("=");
                    moe.kcwiki.tools.swfunpacker.Server.seasonrule.put(ini[0],ini[1]);
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
    
    public static boolean getWorldlist() {
        
        HttpURLConnection conn = null;
        try {  
            String formatCode;
            while(true){
                URL url = new URL("https://api.kcwiki.moe/servers");  
                
                conn = (HttpURLConnection) url.openConnection();  
                conn.setDoOutput(true); 
                conn.setRequestMethod("GET");  
                conn.setRequestProperty("Host", url.getHost()); 
                conn.setRequestProperty("connection", "keep-alive");  
                conn.setRequestProperty("Upgrade-Insecure-Requests", "1"); 
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
                conn.setRequestProperty("DNT", "1");       
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");  
                conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");      

                try{
                    conn.connect();
                }catch(UnknownHostException e){
                    return false;
                }catch(IOException e){
                    return false;
                }
                
                
                // normally, 3xx is redirect
                boolean redirect = false;
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                            || responseCode == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
                }


                if (redirect) {
                    // get redirect url from "location" header field
                    String newUrl = conn.getHeaderField("Location");
                    // get the cookie if need, for login
                    String cookies = conn.getHeaderField("Set-Cookie");
                    // open the new connnection again
                    conn = (HttpURLConnection) new URL(newUrl).openConnection();
                    conn.setRequestProperty("Cookie", cookies);
                    conn.setRequestProperty("connection", "keep-alive");  
                    conn.setRequestProperty("Upgrade-Insecure-Requests", "1"); 
                    conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");  
                    conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
                    conn.setRequestProperty("DNT", "1");       
                    conn.setRequestProperty("Accept-Encoding", "gzip, deflate");  
                    conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8"); 
                }
                conn.connect();
                responseCode = conn.getResponseCode();  
                if (responseCode <= 300) {  
                    formatCode=conn.getContentEncoding();
                    break;
                }else{
                    return false;
                }
            }
            BufferedReader reader;
            if(formatCode!=null){
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
            }else{
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            }
            
            String str = "";
            String line;
            while ((line = reader.readLine()) != null){
                str+=line;       
            }
            JSONArray worlds = JSON.parseArray(str);
            for(Object item:worlds) {
                JSONObject world = (JSONObject) item;
                /*if(!isApacheServer(world.getString("ip").trim())){
                    getWorldMap().put(world.getString("id"), world.getString("ip"));
                    getWorldList().add(world.getString("ip"));
                }*/
                getWorldMap().put(world.getString("id"), world.getString("ip"));
                getWorldList().add(world.getString("ip"));
            }
            
        } catch (MalformedURLException e) {  
            return false;
        } catch (ProtocolException e) {  
            return false;
        } catch (IOException e) {  
            return false;
        }  
        
        return true; 
    }

    public static boolean isApacheServer(String ip) {
        
        try {
            URL serverUrl = new URL("http://"+ip+"/kcs/");
            msgPublish.msgPublisher(serverUrl.toString(),0,0);
            HttpURLConnection urlcon = (HttpURLConnection) serverUrl.openConnection();;
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
            System.setProperty("sun.net.client.defaultReadTimeout", "15000");
            
            
            urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
            urlcon.setRequestMethod("HEAD");
            String ServerType = urlcon.getHeaderField("Server");
            msgPublish.msgPublisher(ServerType,0,0);
            if(ServerType.contains("nginx") || ServerType.contains("Nginx")){
                return false;
            }else{
                return true;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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
     * @return the oldstart2data
     */
    public static String getLocaloldstart2data() {
        return getLocaloldstart2();
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
     * @return the init
     */
    public static boolean isInit() {
        return init;
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
    
    /**
     * @return the proxyhost
     */
    public static String getProxyhost() {
        return proxyhost;
    }

    /**
     * @return the localoldstart2
     */
    public static String getLocaloldstart2() {
        return localoldstart2;
    }

    /**
     * @return the seasonini
     */
    public static String getSeasonini() {
        return seasonini;
    }

    /**
     * @return the dataFolder
     */
    public static String getDataFolder() {
        return dataFolder;
    }

    /**
     * @return the logFolder
     */
    public static String getLogFolder() {
        return logFolder;
    }

    /**
     * @return the publishFolder
     */
    public static String getPublishFolder() {
        return publishFolder;
    }

    /**
     * @return the worksFolder
     */
    public static String getWorksFolder() {
        return worksFolder;
    }

    /**
     * @return the FileList
     */
    public static HashMap<String, String> getFileList() {
        return FileList;
    }

    /**
     * @return the useproxy
     */
    public static boolean isUseproxy() {
        return useproxy;
    }

    /**
     * @return the webrootPath
     */
    public static String getWebrootPath() {
        return webrootPath;
    }

    /**
     * @return the previousFolder
     */
    public static String getPreviousFolder() {
        return previousFolder;
    }

    /**
     * @return the httpproxy
     */
    public static Proxy getHttpproxy() {
        return httpproxy;
    }

    /**
     * @return the museumFolder
     */
    public static String getMuseumFolder() {
        return museumFolder;
    }

    /**
     * @return the zipFolder
     */
    public static Long getZipFolder() {
        return zipFolder;
    }

    /**
     * @param aZipFolder the zipFolder to set
     */
    public static void setZipFolder(Long aZipFolder) {
        zipFolder = aZipFolder;
    }

    /**
     * @return the initDate
     */
    public static Long getInitDate() {
        return initDate;
    }

    /**
     * @param aInitDate the initDate to set
     */
    public static void setInitDate(Long aInitDate) {
        initDate = aInitDate;
    }

    /**
     * @return the worldMap
     */
    public static HashMap<String,String> getWorldMap() {
        return worldMap;
    }

    /**
     * @return the worldList
     */
    public static ArrayList<String> getWorldList() {
        return worldList;
    }

    /**
     * @return the dutyno
     */
    public static String getDutyno() {
        return dutyno;
    }

    /**
     * @return the EventMode
     */
    public static boolean isEventMode() {
        return EventMode;
    }
}
