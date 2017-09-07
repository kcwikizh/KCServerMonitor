/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;



/**
 *
 * @author iTeam_VEP
 */
public class StatusUtil {
     //public static Logger LOG = LoggerFactory.getLogger(StatusUtil.class);
    //private static ClientStatus clientStatus = new ClientStatus();
    
    
    /**
     * 
     * @param projectName 工程名称
     * @param version 版本号
     * @param group 分组号，对应用进行分组
     * @param ipAddress 可以为NULL，为NULL则会自动获取，但是如果主机有多个网卡，可能会取错
     * @param startCMD 启动进程的命令，当应用死掉后，会调用此命令来启动
     * @param remark 备注，如果没有可以为空
     * @return
     */
    
    /*
    public static ClientStatus getClientStatus(String projectName,int version,String group,String ipAddress,String startCMD,String[] remark){
        clientStatus.setProjectName(projectName);
        clientStatus.setVersion(version);
        clientStatus.setRemark(remark);
        clientStatus.setGroup(group);
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();
        //空闲内存
        long freeMemory = runtime.freeMemory();
        clientStatus.setFreeMemory(byteToM(freeMemory));
        //内存总量
        long totalMemory = runtime.totalMemory();
        clientStatus.setTotalMemory(byteToM(totalMemory));
        //最大允许使用的内存
        long maxMemory = runtime.maxMemory();
        clientStatus.setMaxMemory(byteToM(maxMemory));
        //操作系统
        clientStatus.setOsName(System.getProperty("os.name"));
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            clientStatus.setHost(hostName);
            if(ipAddress == null){
                ipAddress = localHost.getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LOG.error("无法获取当前主机的主机名与Ip地址");
            clientStatus.setHost("未知");
        }
        //ip
        clientStatus.setIpAddress(ipAddress);
        clientStatus.setId(makeClientId(projectName,ipAddress));
        //程序启动时间
        long startTime = runtimeMXBean.getStartTime();
        Date startDate = new Date(startTime);
        clientStatus.setStartTime(startDate);
        //类所在路径
        clientStatus.setClassPath(runtimeMXBean.getBootClassPath());
        //程序运行时间
        clientStatus.setRuntime(runtimeMXBean.getUptime());
        //线程总数
        clientStatus.setThreadCount(ManagementFactory.getThreadMXBean().getThreadCount());
        clientStatus.setProjectPath(new File("").getAbsolutePath());
        clientStatus.setCommitDate(new Date());
        clientStatus.setPid(getPid());
        return clientStatus;
    }
    */
    
    /**
     * 把byte转换成M
     * @param bytes 
     * @return
     */
    public static long byteToM(long bytes){
        long kb =  (bytes / 1024 / 1024);
        return kb;
    }
    
    /**
     * 创建一个客户端ID
     * @param projectName
     * @param ipAddress
     * @return
     */
    public static String makeClientId(String projectName,String ipAddress){
        String t = projectName + ipAddress + new File("").getAbsolutePath();
        int client_id = t.hashCode();
        client_id = Math.abs(client_id);
        return String.valueOf(client_id);
    }
    
    /**
     * 获取进程号，适用于windows与linux
     * @return
     */
    public static long getPid(){
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();  
            String pid = name.split("@")[0];  
            return Long.parseLong(pid);
        } catch (NumberFormatException e) {
            System.out.println("无法获取进程Id");
            return 0;
        }
    }
    
    public static void gcTest() throws IOException {
        Runtime run = Runtime.getRuntime();
        System.in.read();   // 暂停程序执行

        // System.out.println("memory> total:" + run.totalMemory() + " free:" + run.freeMemory() + " used:" + (run.totalMemory()-run.freeMemory()) );
        run.gc();
        System.out.println("time: " + (new Date()));
        // 获取开始时内存使用量
        long startMem = run.totalMemory()-run.freeMemory();
        System.out.println("memory> total:" + run.totalMemory() + " free:" + run.freeMemory() + " used:" + startMem );

        String str = "";
        for(int i=0; i<50000; ++i){
            str += i;
        }

        System.out.println("time: " + (new Date()));
        long endMem = run.totalMemory()-run.freeMemory();
        System.out.println("memory> total:" + run.totalMemory() + " free:" + run.freeMemory() + " used:" + endMem );
        System.out.println("memory difference:" + (endMem-startMem));
        
        run.gc();
        System.out.println("memory> total:" + run.totalMemory() + " free:" + run.freeMemory() + " used:" + (run.totalMemory()-run.freeMemory()) );
        
        System.in.close();
        /**/
        
        Runtime runtime=Runtime.getRuntime();
			System.out.println("处理器的数目"+runtime.availableProcessors());
			System.out.println("空闲内存量："+runtime.freeMemory()/ 1024L/1024L + "M av");
			System.out.println("使用的最大内存量："+runtime.maxMemory()/ 1024L/1024L + "M av");
			System.out.println("内存总量："+runtime.totalMemory()/ 1024L/1024L + "M av");
    }
    
    public static void getJVMStatus() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();
        //空闲内存
        long freeMemory = runtime.freeMemory();
        //内存总量
        long totalMemory = runtime.totalMemory();
        //最大允许使用的内存
        long maxMemory = runtime.maxMemory();
        //操作系统
        String osname = System.getProperty("os.name");
        InetAddress localHost;
        String ipAddress;
        try {
            localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            ipAddress = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("无法获取当前主机的主机名与Ip地址");
        }
        //程序启动时间
        long startTime = runtimeMXBean.getStartTime();
        Date startDate = new Date(startTime);
        //类所在路径
        String BootClassPath = runtimeMXBean.getBootClassPath();
        //程序运行时间
        Long Uptime = runtimeMXBean.getUptime();
        //线程总数
        int ThreadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        String ProjectPath = new File("").getAbsolutePath();
        Date Date = new Date();
        Long Pid = getPid();
    }
}
