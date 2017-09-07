/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.init;

import java.io.File;
import static java.lang.Thread.sleep;

/**
 *
 * @author VEP
 */
public class FileExerciser {
        /*
        private JTextArea jTextArea;
        public DeleteDir(JTextArea jTextArea){
        this.jTextArea = jTextArea; //用于显示日志
        }
        */

           
//删除文件夹
//param folderPath 文件夹完整绝对路径

     public static boolean delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
            sleep(500);
            return !new File(folderPath).exists();
        } catch (InterruptedException e) {
            return false;
        }
    }

//删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
   public static boolean delAllFile(String path) {

       boolean flag = false;
       File file = new File(path);

       String[] tempList = file.list();
       File temp;
        for (String tempList1 : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList1);
            } else {
                temp = new File(path + File.separator + tempList1);
            }
            if (temp.isFile()) {
                temp.delete();
            }if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList1); //先删除文件夹里面的文件
                delFolder(path + File.separator + tempList1); //再删除空文件夹
                flag = true;
            }
        }
        tempList = file.list();
        if (tempList.length ==0) { 
            flag = true;
        }
       return flag;
     }        

}
