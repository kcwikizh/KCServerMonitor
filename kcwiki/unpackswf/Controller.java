/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.unpackswf;

import moe.kcwiki.init.MainServer;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;

/**
 *
 * @author x5171
 */
public class Controller {
    private final String previousfolder;
    private final String currentfolder;    
    private final UnpackSwf ffdec;
    private final moe.kcwiki.picscanner.Controller verify;
    
    
    public Controller() throws IOException{
        this.currentfolder = MainServer.getTempFolder()+File.separator+"currentswf";
        this.previousfolder = MainServer.getPreviousFolder();
        if(!new File(currentfolder).exists()){new File(currentfolder).mkdirs();}
        ffdec=new UnpackSwf();
        verify=new moe.kcwiki.picscanner.Controller();
    }
    
    public void Analysis(String filename,String filepath,String sourcepath) throws InterruptedException, Exception{
        String outputpath=currentfolder+File.separator+sourcepath+File.separator+(filename.substring(0, filename.length()-4));
        if(filename.contains("Core")){
            if(!new moe.kcwiki.decryptcore.CoreRecover().unlockCore(filepath+File.separator+filename, MainServer.getTempFolder())){msgPublish.msgPublisher("Core.swf 解码失败。",0,-1);}
            ffdec.ffdec(outputpath,MainServer.getTempFolder()+File.separator+"Core_hack.swf");
            new moe.kcwiki.decryptcore.CoreDecrypt().getData(outputpath+File.separator+Server.getCoremap(), outputpath+File.separator+Server.getCoresound());
        }
        if(new File(previousfolder+File.separator+(filename.substring(0, filename.length()-4))).exists()){
            msgPublish.msgPublisher(filename+"开始解压分析",0,0);
            ffdec.ffdec(outputpath,filepath+File.separator+filename);
            new VerifyScr().verifyscr(outputpath+File.separator+"scripts", previousfolder+File.separator+(filename.substring(0, filename.length()-4))+File.separator+"scripts");
            verify.verifyimg(outputpath+File.separator+"images", previousfolder+File.separator+(filename.substring(0, filename.length()-4))+File.separator+"images");
        } 
    }
}
