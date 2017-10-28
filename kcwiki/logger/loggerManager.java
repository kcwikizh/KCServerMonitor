/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.logger;

/**
 *
 * @author iTeam_VEP
 */

import java.io.InputStream;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
import moe.kcwiki.init.MainServer;
import static moe.kcwiki.tools.constant.FILESEPARATOR;
    
public class loggerManager {    
    
    //http://blog.csdn.net/nullpointer2008/article/details/44242255
    //http://blog.csdn.net/zhao1949/article/details/52787927
    
    private static Map<Class<?>,Logger> loggerCache = new HashMap<Class<?>,Logger>();  

    // 初始化LogManager    
    static {    
        // 读取配置文件    
        ClassLoader cl = loggerManager.class.getClassLoader();    
        InputStream inputStream = null;    
        if (cl != null) {    
            inputStream = cl.getResourceAsStream(MainServer.getDataFolder() + FILESEPARATOR + "log.properties");    
        } else {    
            inputStream = ClassLoader    
                    .getSystemResourceAsStream(MainServer.getDataFolder() + FILESEPARATOR + "log.properties");    
        }    
        java.util.logging.LogManager logManager = java.util.logging.LogManager    
                .getLogManager();    
        if (inputStream == null){  
            System.err.println("LoggerManager: Log configuration NOT found!");  
      
        }else try {    
            // 重新初始化日志属性并重新读取日志配置。    
            logManager.readConfiguration(inputStream);    
            System.out.println("LoggerManager: Log configuration loaded.");  
              
                   } catch (Exception e) {    
            System.err.println(e);    
        }  finally{  
            try {inputStream.close();}  
            catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
    }   
    
    /**  
     * 获取日志对象  
     * @param clazz  
     * @return  
     */    
    public static Logger getLogger(Class<?> clazz) {    
          
        Logger logger = loggerCache.get(clazz);  
        if (logger==null){  
            logger = Logger.getLogger(clazz.getCanonicalName());  
            loggerCache.put(clazz, logger);   
        }  
        return logger;    
    }    
    /** 
     * 获取指定Logger的有效Level 
     * @param logger 
     * @return 
     */  
    public static Level getEffectiveLevel(Logger logger)  
    {  
        Level level = null;  
        Logger parent = logger;  
        while (level==null){  
            if (parent==null)  
                break;  
            level = parent.getLevel();  
            parent = parent.getParent();  
        }  
        return level;  
    }  
    
}    
