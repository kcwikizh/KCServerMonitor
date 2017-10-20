/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.init;

import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import moe.kcwiki.threadpool.Controller;
import moe.kcwiki.tools.CatchError;

/**
 *
 * @author iTeam_VEP
 */
public class AppInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MainServer.init(true); 
        CatchError.init();
        Controller.ininPool();
        MainServer.setInitDate(new Date().getTime());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MainServer.setStopScanner(true);
        Controller.getInstance().shutdownNow();
    }
}
