/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.tools.swfunpacker;

import moe.kcwiki.tools.swfunpacker.comparator.src.VerifyScr;
import moe.kcwiki.initializer.MainServer;
import java.io.File;
import java.io.IOException;
import moe.kcwiki.handler.massage.msgPublish;

/**
 *
 * @author x5171
 */
public class Controller {
    private final String previousfolder;
    private final String currentfolder;    
    private final UnpackSwf ffdec;
    private final moe.kcwiki.tools.swfunpacker.comparator.image.Controller verify;
    
    
    public Controller() throws IOException{
        this.currentfolder = MainServer.getPublishFolder()+File.separator+"currentswf";
        this.previousfolder = MainServer.getPreviousFolder();
        if(!new File(currentfolder).exists()){new File(currentfolder).mkdirs();}
        ffdec=new UnpackSwf();
        verify=new moe.kcwiki.tools.swfunpacker.comparator.image.Controller();
    }
    
    public void Analysis(String filename,String filepath,String sourcepath) throws InterruptedException, Exception{
        String outputpath=currentfolder+File.separator+sourcepath+File.separator+(filename.substring(0, filename.length()-4));
        if(filename.contains("Core")){
            if(!new moe.kcwiki.tools.swfunpacker.coredecryptor.CoreRecover().unlockCore(filepath+File.separator+filename, MainServer.getTempFolder())){msgPublish.msgPublisher("Core.swf 解码失败。",0,-1);}
            ffdec.ffdec(outputpath,MainServer.getTempFolder()+File.separator+"Core_hack.swf");
            new moe.kcwiki.tools.swfunpacker.coredecryptor.CoreDecrypt().getData(outputpath+File.separator+Server.getCoremap(), outputpath+File.separator+Server.getCoresound());
            filename = "Core_hack.swf";
        }
        //filename = (filename.substring(0, filename.length()-4));
        if(new File(previousfolder+File.separator+filename).exists()){
            if(!filename.contains("Core_hack")){
                //msgPublish.msgPublisher(filename+"\t开始解压",0,0);
                ffdec.ffdec(outputpath,filepath+File.separator+filename);
            }
            new VerifyScr().verifyscr(outputpath+File.separator+"scripts", previousfolder+File.separator+filename+File.separator+"scripts");
            verify.verifyimg(outputpath+File.separator+"images", previousfolder+File.separator+filename+File.separator+"images");
        } 
    }
}
