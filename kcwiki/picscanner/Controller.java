/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.picscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException; 
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import moe.kcwiki.init.GetModifiedDataThread;
import moe.kcwiki.massagehandler.msgPublish;
import moe.kcwiki.tools.constant;
 
/**
 *
 * @author VEP
 */
public class Controller {
    
    public boolean verifyimg(String newFileFolder,String oldFileFolder) throws FileNotFoundException, IOException, Exception{
        GetModifiedDataThread.addJob();
        new Thread() {  //创建新线程用于下载
            @Override
            public void run() {
                HashMap<String, String> NewHashList = new LinkedHashMap<>();
                HashMap<String, String> OldHashList = new LinkedHashMap<>();
                HashMap<String, String> delList = new LinkedHashMap<>();
                try {
                    NewHashList.clear();
                    OldHashList.clear();
                    delList.clear();
                        
                    if(new File(oldFileFolder).exists()){
                        File[] fileList = new File(oldFileFolder).listFiles();       
                        for (File fileList1 : fileList) {
                            delList.put(new GetHash().getNewHash(oldFileFolder + File.separator + fileList1.getName()),fileList1.getName());
                        }

                        //new ScannerGui().setStatement(2);
                        fileList = new File(newFileFolder).listFiles();
                        for (File fileList1 : fileList) {
                            if(delList.get(new GetHash().getNewHash(newFileFolder+File.separator + fileList1.getName()))!=null){
                                fileList1.delete();
                            }
                        }
                        String filename=oldFileFolder.substring(0, oldFileFolder.lastIndexOf(File.separator));
                        //msgPublish.msgPublisher(filename.substring(filename.lastIndexOf(File.separator)+1, filename.length())+"\t img MD5互查对比完成",1);
                        sleep(1*1000);

                        //new ScannerGui().setStatement(3);
                        NewHashList.clear();
                        File[] newfileList = new File(newFileFolder).listFiles();
                        File[] oldfileList = new File(oldFileFolder).listFiles();
                        Scanner p = new Scanner();
                        int count=0;
                        for (File newfile : newfileList) {
                            NewHashList.put(Scanner.getFeatureValue(newFileFolder+File.separator + newfile.getName()), newFileFolder+File.separator + newfile.getName());
                            count++;
                        }
                        System.out.print(count+constant.LINESEPARATOR);
                        count=0;
                        for (File oldfile : oldfileList) {
                            OldHashList.put(Scanner.getFeatureValue(oldFileFolder+File.separator + oldfile.getName()), oldFileFolder+File.separator + oldfile.getName());
                            count++;
                        }
                        System.out.print(count+constant.LINESEPARATOR);
                        for(String newfile : NewHashList.keySet()){
                            for(String oldfile : OldHashList.keySet()){
                                double score=Scanner.calculateSimilarity(newfile, oldfile);
                                //if(delList.get(OldHashList.get(oldfile))!=null){continue;}
                                if(score > 0.90){
                                    new File(NewHashList.get(newfile)).delete();
                                    //delList.put(OldHashList.get(oldfile),oldfile);
                                    //System.out.print(score+constant.LINESEPARATOR);
                                    break;
                                }
                            }
                        }
                        msgPublish.msgPublisher(filename.substring(filename.lastIndexOf(File.separator)+1, filename.length())+"\t img pHash互查对比完成，剩余文件："+new File(newFileFolder).listFiles().length,0,0);
                        sleep(1*1000);
                    }
                    
                    NewHashList.clear();
                    OldHashList.clear();
                    delList.clear();
                    GetModifiedDataThread.finishJob();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
            }
        }.start();
        return true;
    }
    
}
