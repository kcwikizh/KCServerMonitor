/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.tools.swfunpacker.coredecryptor.CoreDecrypt;
import moe.kcwiki.initializer.MainServer;
import moe.kcwiki.initializer.Start2DataThread;
import moe.kcwiki.handler.massage.msgPublish;
import moe.kcwiki.handler.thread.Controller;
import moe.kcwiki.tools.CalculateDate;
import static moe.kcwiki.tools.constant.constant.LINESEPARATOR;
import moe.kcwiki.tools.swfunpacker.Server;
import moe.kcwiki.web.view.login;

/**
 *
 * @author iTeam_VEP
 */
public class monitor {
    private StringBuilder sb = null;
    
    public String getData() {
        sb = new StringBuilder();
        
        this.addString(LINESEPARATOR);
      this.addString("已稳定运行: "+CalculateDate.calculator(MainServer.getInitDate(), new Date().getTime()));
      this.addString(LINESEPARATOR);
      this.addString("各项指针参数： ");
      if(MainServer.isInit()){
        this.addString("ffdecFolder: "+new File(MainServer.getFfdecFolder()).exists());
        this.addString("MainServer.init: "+MainServer.isInit());
        this.addString("ThreadPool.init: "+Controller.isIsPoolInit());
        this.addString("isDebugMode: "+MainServer.isDebugMode());
        this.addString("isHasStart2: "+Start2DataThread.isHasStart2());
        this.addString("isStopScanner: "+MainServer.isStopScanner());
        HttpURLConnection connection = null;
        InputStream is = null;
      //http://blog.csdn.net/zhongguozhichuang/article/details/52471140
      //https://ipapi.co/api/#your-ip-address
      //https://www.ipify.org/
      //http://ip-api.com/docs/api:json
        try {
            Proxy tempproxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(MainServer.getProxyhost(), MainServer.getProxyport()));
            connection = (HttpURLConnection) new URL("http://ip-api.com/json").openConnection(tempproxy);
            connection.setRequestProperty("Host", "ip-api.com");
            connection.setRequestProperty("Origin", "http://ip-api.com");
            connection.setRequestProperty("Referer", "http://ip-api.com/docs/api:json");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty("DNT", "1");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            // 建立实际的连接
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            this.addString("ProxyTest: "+responseCode);
            if (responseCode == 200) {
                is = connection.getInputStream();
                byte[] by = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int len = -1;
                while ((len = is.read(by)) != -1) {
                    bos.write(by, 0, len);
                }
                JSONObject jobj = JSON.parseObject(bos.toString("utf-8"));
                this.addString("proxyIP: "+jobj.get("query"));
            }
            
        } catch (MalformedURLException ex) {
            this.addString("ProxyTest: "+"Error: MalformedURLException");
            Logger.getLogger(monitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.addString("ProxyTest: "+"Error: IOException");
            Logger.getLogger(monitor.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if(is!=null)
                    is.close();
            } catch (IOException ex) {
                Logger.getLogger(monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      } else {
        this.addString("ffdecFolder: "+"Server is not been inited.");
      }
      
      
      this.addString(LINESEPARATOR);
      this.addString("各项路径参数： ");
      if(MainServer.isInit()){
        this.addString("getWebrootPath: "+MainServer.getWebrootPath()+"\tisExists\t"+new File(MainServer.getWebrootPath()).exists());
        this.addString("getTempFolder: "+MainServer.getTempFolder()+"\tisExists\t"+new File(MainServer.getTempFolder()).exists());
        this.addString("getDataFolder: "+MainServer.getDataFolder()+"\tisExists\t"+new File(MainServer.getDataFolder()).exists());
        this.addString("getPublishFolder: "+MainServer.getPublishFolder()+"\tisExists\t"+new File(MainServer.getPublishFolder()).exists());
        this.addString("getLogFolder: "+MainServer.getLogFolder()+"\tisExists\t"+new File(MainServer.getLogFolder()).exists());
        this.addString("getPreviousFolder: "+MainServer.getPreviousFolder()+"\tisExists\t"+new File(MainServer.getPreviousFolder()).exists());
        this.addString("getWorksFolder: "+MainServer.getWorksFolder()+"\tisExists\t"+new File(MainServer.getWorksFolder()).exists());
        this.addString("getLocaloldstart2data: "+MainServer.getLocaloldstart2data()+"\tisExists\t"+new File(MainServer.getLocaloldstart2data()).exists());
      } else {
        this.addString("Server is not been inited.");
      }
      
      this.addString(LINESEPARATOR);
      this.addString("各项预设参数： ");
      if(MainServer.isInit()){
        this.addString("getNewstart2: "+MainServer.getNewstart2());
        this.addString("getOldstart2: "+MainServer.getOldstart2());
        this.addString("getProxyhost: "+MainServer.getProxyhost());
        this.addString("getProxyport: "+MainServer.getProxyport());
        this.addString("getSlotitemno: "+MainServer.getSlotitemno());
        this.addString("getShinkaislotitemno: "+MainServer.getShinkaislotitemno());
        this.addString("getDutyno: "+MainServer.getDutyno());
        this.addString("getMapid: "+MainServer.getMapid());
        this.addString("getCoremap: "+Server.getCoremap());
        this.addString("getCoresound: "+Server.getCoresound());
        this.addString("getAuthorization_superuser: "+(MainServer.getAuthorization_superuser() != null));
        this.addString("getAuthorization_uploadstart2: "+(MainServer.getAuthorization_uploadstart2()!= null));
      } else {
        this.addString("Server is not been inited.");
      }
      
      this.addString(LINESEPARATOR);
      String[] serverlistname = new String[]{"0","横须贺镇守府","呉镇守府","佐世保镇守府","舞鹤镇守府","大凑警备府","トラック泊地","リンガ泊地","ラバウル基地","ショートランド泊地","ブイン基地","タウイタウイ泊地","パラオ泊地","ブルネイ泊地","単冠湾泊地","幌筵泊地","宿毛湾泊地","鹿屋基地","岩川基地","佐伯湾泊地","柱岛泊地"};
    
      this.addString("各项实时数据： ");
      HashMap<String,String> worldMap = MainServer.getWorldMap();
      for(String id:worldMap.keySet()) {
          this.addString("servername: " + serverlistname[Integer.valueOf(id)] + "\t ip: " + worldMap.get(id));
      }
      this.addString("getWorldMapSize: " + worldMap.size());
      
      ArrayList<String> worldList = MainServer.getWorldList();
      for(String ip:worldList) {
          this.addString("ip: " + ip);
      }
      this.addString("getWorldListSize: " + worldList.size());
      
      if(moe.kcwiki.database.DBCenter.AddressList != null){
          moe.kcwiki.database.DBCenter.AddressList.keySet().forEach((_item) -> {
          this.addString("AddressList: "+_item+":\t"+moe.kcwiki.database.DBCenter.AddressList.get(_item));
        });
      }else{
          this.addString("AddressList:  null!");
      }
      this.addString("AddressList.size(): " + moe.kcwiki.database.DBCenter.AddressList.size());
      
      List UrlList = msgPublish.getUrlList();
      UrlList.forEach((json) -> {
          this.addString("getUrlList: "+JSON.toJSONString(json));
        });
      this.addString("getUrlListSize: " + UrlList.size());
      
      List UrlprePublishList = msgPublish.getUrlprePublishList();
      UrlprePublishList.forEach((json) -> {
          this.addString("getUrlList: "+JSON.toJSONString(json));
        });
      this.addString("getUrlprePublishList: " + UrlprePublishList.size());
      
      /*if(CoreDecrypt.shipAddressList != null){
          CoreDecrypt.shipAddressList.keySet().forEach((_item) -> {
          this.addString("shipVioceAddressList: "+JSON.toJSONString(_item));
        });
      }else{
          this.addString("shipVioceAddressList:  null!");
      }
      this.addString("shipVioceAddressList.size(): "+CoreDecrypt.shipAddressList.size());*/
      
      if(CoreDecrypt.shipDataList != null){
          CoreDecrypt.shipDataList.keySet().forEach((_item) -> {
          this.addString("shipDataList: "+_item+":\t"+CoreDecrypt.shipDataList.get(_item));
        });
      }else{
          this.addString("shipDataList:  null!");
      }
      this.addString("shipVioceAddressList.size(): "+CoreDecrypt.shipAddressList.size());
      
      this.addString(LINESEPARATOR);
      this.addString("后台统计： ");
      if(MainServer.isInit()){
          this.addString("user: "+login.getUserList().size());
      } else {
          this.addString("user: "+"Server is not been inited.");
      }
      
      this.addString(LINESEPARATOR);
      // 获取java线程的管理MXBean
        ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的Monitor和synchronizer信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = tmxb.dumpAllThreads(false, false);
        // 遍历线程信息，打印出ID和名称
        for (ThreadInfo info : threadInfos) {
            //System.out.println();
            this.addString("[" + info.getThreadId() + "] " + info.getThreadName());
        }
        this.addString("threadInfos: "+threadInfos.length);
        this.addString(LINESEPARATOR);
        Map<Thread, StackTraceElement[]> AllStackTraces = Thread.getAllStackTraces();
        JSONArray StackTracesArray = new JSONArray();
        int count = 0;
        for (Thread t : AllStackTraces.keySet()) {
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(AllStackTraces.get(t)));
            for (Iterator<Object> it1 = array.iterator(); it1.hasNext();) {
                JSONObject obj =(JSONObject) it1.next();
                StackTracesArray.add(obj);
                if(obj.getString("className").contains("moe.kcwiki")){
                    count++;
                    this.addString("className: "+obj.getString("className")+"\tmethodName: "+obj.getString("methodName")+"\tnativeMethod: "+obj.getString("nativeMethod"));
                }
            }
            //this.addString("getAllStackTraces: "+JSON.toJSONString(AllStackTraces.get(t)));
        }
        //this.addString("getAllStackTraces: "+JSON.toJSONString(AllStackTraces.clone()));
        this.addString("KcWikiStackTraces: "+count);
      
    this.addString(LINESEPARATOR);
    this.addString(LINESEPARATOR);
    this.addString("核心测试项： ");
    MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();   
    MemoryUsage usage = memorymbean.getHeapMemoryUsage();   
    this.addString("INIT HEAP: " + usage.getInit());   
    this.addString("MAX HEAP: " + usage.getMax());   
    this.addString("USE HEAP: " + usage.getUsed());   
    this.addString("\nFull Information:");   
    this.addString("Heap Memory Usage: "   
    + memorymbean.getHeapMemoryUsage());   
    this.addString("Non-Heap Memory Usage: "   
    + memorymbean.getNonHeapMemoryUsage());   
      
    List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();   
    this.addString("===================获取java运行参数信息=============== "); 
    inputArguments.forEach((String s) -> {
        this.addString(s);
    });
  
    this.addString("=======================通过java来获取相关系统状态============================ ");  
    double i = (double)Runtime.getRuntime().totalMemory()/1024;//Java 虚拟机中的内存总量,以字节(Byte)为单位  
    this.addString("总的内存量 ： "+i);  
    double j = (double)Runtime.getRuntime().freeMemory()/1024;//Java 虚拟机中的空闲内存量  
    this.addString("空闲内存量 ： "+j);  
    this.addString("最大内存量 ： "+Runtime.getRuntime().maxMemory()/1024);  
  
    this.addString("=======================获取操作系统相关信息============================ ");  
    OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();  
//   System.out.println(osm.getFreeSwapSpaceSize()/1024);  
//    System.out.println(osm.getFreePhysicalMemorySize()/1024);  
//    System.out.println(osm.getTotalPhysicalMemorySize()/1024);  
      
    //获取操作系统相关信息  
    this.addString("osm.getArch() "+osm.getArch());  
    this.addString("osm.getAvailableProcessors() "+osm.getAvailableProcessors());  
    this.addString("osm.getSystemLoadAverage() "+osm.getSystemLoadAverage());  
    //System.out.println("osm.getCommittedVirtualMemorySize() "+osm.getCommittedVirtualMemorySize());  
    this.addString("osm.getName() "+osm.getName());  
    //System.out.println("osm.getProcessCpuTime() "+osm.getProcessCpuTime());  
    this.addString("osm.getVersion() "+osm.getVersion());  
    //获取整个虚拟机内存使用情况  
    this.addString("=======================获取整个虚拟机内存使用情况============================ ");  
    MemoryMXBean mm=(MemoryMXBean)ManagementFactory.getMemoryMXBean();  
    this.addString("getHeapMemoryUsage "+mm.getHeapMemoryUsage());  
    this.addString("getNonHeapMemoryUsage "+mm.getNonHeapMemoryUsage());  
    //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况  
    this.addString("=======================获取各个线程的各种状态============================ ");  
    ThreadMXBean tm=(ThreadMXBean)ManagementFactory.getThreadMXBean();  
    this.addString("getThreadCount "+tm.getThreadCount());  
    this.addString("getPeakThreadCount "+tm.getPeakThreadCount());  
    this.addString("getCurrentThreadCpuTime "+tm.getCurrentThreadCpuTime());  
    this.addString("getDaemonThreadCount "+tm.getDaemonThreadCount());  
    this.addString("getCurrentThreadUserTime "+tm.getCurrentThreadUserTime());  
      
    //当前编译器情况  
    this.addString("=======================当前编译器情况============================ ");  
    CompilationMXBean gm=(CompilationMXBean)ManagementFactory.getCompilationMXBean();  
    this.addString("getName "+gm.getName());  
    this.addString("getTotalCompilationTime "+gm.getTotalCompilationTime());  
      
    //获取多个内存池的使用情况  
    this.addString("=======================获取多个内存池的使用情况============================ ");  
    List<MemoryPoolMXBean> mpmList=ManagementFactory.getMemoryPoolMXBeans();  
    for(MemoryPoolMXBean mpm:mpmList){  
        this.addString("getUsage "+mpm.getUsage());   
        String[] MemoryManagerNames =mpm.getMemoryManagerNames();
        for(String MemoryManagerName:MemoryManagerNames){
            this.addString("MemoryManagerName ： "+MemoryManagerName);
        }
    }  
    //获取GC的次数以及花费时间之类的信息  
    this.addString("=======================获取GC的次数以及花费时间之类的信息============================ ");  
    List<GarbageCollectorMXBean> gcmList=ManagementFactory.getGarbageCollectorMXBeans();  
    for(GarbageCollectorMXBean gcm:gcmList){  
        this.addString("getName "+gcm.getName());  
        String[] getMemoryPoolNames = gcm.getMemoryPoolNames();
        for(String getMemoryPoolName:getMemoryPoolNames){
            this.addString("getMemoryPoolName ： "+getMemoryPoolName);
        }
    }  
    //获取运行时信息  
    this.addString("=======================获取运行时信息============================ ");  
    RuntimeMXBean rmb=(RuntimeMXBean)ManagementFactory.getRuntimeMXBean();  
    this.addString("getClassPath "+rmb.getClassPath());  
    this.addString("getLibraryPath "+rmb.getLibraryPath());  
    this.addString("getVmVersion "+rmb.getVmVersion());  
    this.addString(LINESEPARATOR);
    this.addString(LINESEPARATOR); 
      
    
      this.addString(LINESEPARATOR);
      this.addString("AllStackTraces： ");
        for (Iterator<Object> it1 = StackTracesArray.iterator(); it1.hasNext();) {
            JSONObject obj =(JSONObject) it1.next();
            this.addString("className: "+obj.getString("className")+"\tmethodName: "+obj.getString("methodName")+"\tnativeMethod: "+obj.getString("nativeMethod"));
        }
        this.addString("StackTracesArray.size()： "+StackTracesArray.size());
        
      this.addString(LINESEPARATOR);
      AllStackTraces.clear();
      StackTracesArray.clear();
        
        return sb.toString();
    }
    
    private void addString(String str) {
        sb.append(str).append(LINESEPARATOR).append("</br>").append(LINESEPARATOR);
    }
}
