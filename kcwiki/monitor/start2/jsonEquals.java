/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moe.kcwiki.monitor.start2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author iTeam_VEP
 */
public class jsonEquals {


/**
 * 比较两个json串是否相同
 * @param j1  第一个json串(json串中不能有换行)
 * @param j2 第二个json串(json串中不能有换行)
 * @return 布尔型比较结果
 */
public static boolean jsonEquals(String j1,String j2){
  
  //将json中表示list的[]替换成{}。思想：只保留层次结构，不区分类型
  //这样直接替换，可能会导致某些value中的符号也被替换，但是不影响结果，因为j1、j2的变化是相对的
  j1 = j1.replaceAll("\\[", "{");
  j1 = j1.replaceAll("]", "}");
  j2 = j2.replaceAll("\\[", "{");
  j2 = j2.replaceAll("]", "}");
  //将json中，字符串型的value中的{},字符替换掉，防止干扰(并没有去除key中的，因为不可能存在那样的变量名)
      //未转义regex：(?<=:")(([^"]*\{[^"]*)|([^"]*\}[^"]*)|([^"]*,[^"]*))(?=")
  Pattern pattern = Pattern.compile("(?<=:\")(([^\"]*\\{[^\"]*)|([^\"]*\\}[^\"]*)|([^\"]*,[^\"]*))(?=\")");
      j1 = cleanStr4Special(j1, pattern.matcher(j1));
      j2 = cleanStr4Special(j2, pattern.matcher(j2));
  //转义字符串value中的空格
  //未转义regex:"[^",]*?\s+?[^",]*?"
  pattern = Pattern.compile("\"[^\",]*?\\s+?[^\",]*?\"");
      j1 = cleanStr4Space(j1, pattern.matcher(j1));
      j2 = cleanStr4Space(j2, pattern.matcher(j2));
      //现在可以安全的全局性去掉空格
      j1 = j1.replaceAll(" ", "");
      j2 = j2.replaceAll(" ", "");
  //调用递归方法
  return compareAtom(j1,j2);
}

/**
 * 比较字符串核心递归方法
 * @param j1
 * @param j2
 * @return
 */
private static boolean compareAtom(String j1,String j2){
  
  if(!j1.equals("?:\"?\"")){
    //取出最深层原子
    String a1 = j1.split("\\{",-1)[j1.split("\\{",-1).length-1].split("}",-1)[0];
    String a2 = j2.split("\\{",-1)[j2.split("\\{",-1).length-1].split("}",-1)[0];
    String j2_ = j2;
    String a2_ = a2;
    //转换成原子项
    String i1[] = a1.split(",");
    //在同级原子中寻找相同的原子
    while(!a2.startsWith(",") &&
        !a2.endsWith(",") &&
        a2.indexOf(":,")<0 &&
        a2.indexOf(",,")<0
       ){
      //遍历消除
      for(String s : i1){
        a2_ = a2_.replace(s,"");
      }
      //此时a2_剩下的全是逗号，如果长度正好等于i1的长度-1，说明相等
      if(a2_.length() == i1.length-1){
        //相等则从j1、j2中消除，消除不能简单的替换，因为其他位置可能有相同的结构，必须从当前位置上消除
        int index = 0;
        index = j1.lastIndexOf("{" + a1 + "}");
        j1 = j1.substring(0, index)+j1.substring(index).replace("{" + a1 + "}", "?:\"?\"");
        index = j2.lastIndexOf("{" + a2 + "}");
        j2 = j2.substring(0, index)+j2.substring(index).replace("{" + a2 + "}", "?:\"?\"");
        //递归
        return compareAtom(j1, j2);
      }else{
        //寻找下一个同级原子
        j2_ = j2_.replace("{" + a2 + "}", "");
        a2 = j2_.split("\\{",-1)[j2_.split("\\{",-1).length-1].split("}",-1)[0];
        a2_ = a2;
      }
    }
    return false;
  }else{
    //比较是否相同
    return j1.equals(j2);
  }
}

/**
 * json字符串特殊字符清理辅助方法
 * @param j 需要清理的json字符串
 * @param matcher 正则表达式匹配对象
 * @return 净化的json串
 */
private static String cleanStr4Special(String j,Matcher matcher){
  String group = "";
  String groupNew = "";
  while(matcher.find()){
    group = matcher.group();
    groupNew = group.replaceAll("\\{", "A");
    groupNew = groupNew.replaceAll("}", "B");
    groupNew = groupNew.replaceAll(",", "C");
    j = j.replace(group, groupNew);
  }
  return j;
}

/**
 * json串字符串类型的value中的空格清理辅助方法
 * @param j 需要清理的json字符串
 * @param matcher 正则表达式匹配对象
 * @return 净化的json串
 */
private static String cleanStr4Space(String j,Matcher matcher){
    String group = "";
      String groupNew = "";
      while(matcher.find()){
          group = matcher.group();
          groupNew = group.replaceAll(" ", "S");
          j = j.replace(group, groupNew);
      }
      return j;
}
}
