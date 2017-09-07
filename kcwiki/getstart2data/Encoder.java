/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.getstart2data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import moe.kcwiki.massagehandler.msgPublish;

/**
 *
 * @author VEP
 */
public class Encoder {
/** 
 * 判断文件的编码格式 
 * @param fileName :file 
 * @return 文件编码格式 
 * @throws Exception 
 */  
    public static String codeString(String fileName) {  
          
        String charset = "GBK"; // 默认编码
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
                bis.mark(0);
                int read = bis.read(first3Bytes, 0, 3);
                if (read == -1)
                    return charset;
                if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                    charset = "UTF-16LE";
                    checked = true;
                } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1]
                        == (byte) 0xFF) {
                    charset = "UTF-16BE";
                    checked = true;
                } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1]
                        == (byte) 0xBB
                        && first3Bytes[2] == (byte) 0xBF) {
                    charset = "UTF-8";
                    checked = true;
                }
                bis.reset();
                if (!checked) {
                    int loc = 0;
                    while ((read = bis.read()) != -1) {
                        loc++;
                        if (read >= 0xF0)
                            break;
                        //单独出现BF以下的，也算是GBK
                        if (0x80 <= read && read <= 0xBF)
                            break;
                        if (0xC0 <= read && read <= 0xDF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) { // (0x80 -
                            // 0xBF),也可能在GB编码内
                            } else
                                break;
                            // 也有可能出错，但是几率较小
                        } else if (0xE0 <= read && read <= 0xEF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                read = bis.read();
                                if (0x80 <= read && read <= 0xBF) {
                                    charset = "UTF-8";
                                    break;
                                } else
                                    break;
                            } else
                                break;
                        }
                    }
                    //System.out.println(loc + " " + Integer.toHexString(read));
                }
            }
        } catch (IOException e) {
            msgPublish.msgPublisher("moe.kcwiki.getstart2data-Encoder-codeString:IOException",0,-1);
        }
        return charset;
    }
    /*
    public void readFile(String file,String code) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        FileInputStream fInputStream = new FileInputStream(file);  
        //code为上面方法里返回的编码方式  
        InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, code);  
        BufferedReader in = new BufferedReader(inputStreamReader);  
        

        String strTmp = "";  
        //按行读取  
        while (( strTmp = in.readLine()) != null) {  
            sBuffer.append(strTmp + "/n");  
        }  
        return sBuffer.toString();  
    }
    */
}
