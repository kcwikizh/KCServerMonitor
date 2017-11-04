/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.swfunpacker.coredecryptor;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author VEP
 */

public class CoreRecover {
        /** 
         * 对二进制文件比较常见的类有FileInputStream，DataInputStream 
         * BufferedInputStream等。类似于DataOutputStream，DataInputStream 
         * 也提供了很多方法用于读入布尔型、字节、字符、整形、长整形、短整形、 
         * 单精度、双精度等数据。 
     * @param swfPath
     * @param tmpath
     * @return 
         */  
    
        public boolean unlockCore(String swfPath,String tmpath) {  
        File file = new File(swfPath);
        InputStream in;
        FileOutputStream opFs; 
        
        try {
            long cutNum=(((int)file.length() - 128)/8);
            byte a[] = new byte[128]; 
            byte b[] = new byte[(int)cutNum]; 
            byte c[] = new byte[(int)cutNum]; 
            byte d[] = new byte[(int)cutNum]; 
            byte e[] = new byte[(int)cutNum]; 
            byte f[] = new byte[(int)cutNum]; 
            byte g[] = new byte[(int)cutNum]; 
            byte h[] = new byte[(int)cutNum]; 
            byte i[] = new byte[(int)cutNum];
            
            in=new FileInputStream(swfPath);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            in.read(a);
            in.read(b);
            in.read(c);
            in.read(d);
            in.read(e);
            in.read(f);
            in.read(g);
            in.read(h);
            in.read(i);
            in.close();
            
            opFs = new FileOutputStream(new File(tmpath+File.separator+"Core_hack.swf")); 
            opFs.write(a);
            opFs.write(b);
            opFs.write(i);
            opFs.write(d);       
            opFs.write(g);
            opFs.write(f);
            opFs.write(e);
            opFs.write(h);
            opFs.write(c);
            
            opFs.close();
            
            return true;
        } catch (IOException e) {
            return false;
        }      
    }
        
    public static void main(String[] args) {
        new CoreRecover().unlockCore("C:\\Users\\VEP\\Desktop\\test\\Core new.swf","C:\\Users\\VEP\\Desktop\\test\\");
    }
}
