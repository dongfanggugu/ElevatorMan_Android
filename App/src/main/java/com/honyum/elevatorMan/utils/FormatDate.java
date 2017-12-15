package com.honyum.elevatorMan.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/** 
* 日期格式 
* @author Ben 
* 
*/ 
public class FormatDate { 

    public static String currString  = "";
    /** 
     * 返回系统当前的完整日期时间 <br> 
     * 格式 1：2008-05-02 13:12:44 <br> 
     * 格式 2：2008/05/02 13:12:44 <br> 
     * 格式 3：2008年5月2日 13:12:44 <br> 
     * 格式 4：2008年5月2日 13时12分44秒 <br> 
     * 格式 5：2008年5月2日 星期五 13:12:44 <br> 
     * 格式 6：2008年5月2日 星期五 13时12分44秒 <br> 
     * @param 参数(formatType) :格式代码号 
     * @return 字符串 
     */ 
    public static String get(int formatType) { 
        SimpleDateFormat sdf = null; 
        if(formatType==1) { 
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        } else if(formatType==2) { 
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
        } else if(formatType==3) { 
            sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); 
        } else if(formatType==4) { 
            sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"); 
        } else if(formatType==5) { 
            sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm:ss", Locale.CHINA);
        } else if(formatType==6) { 
            sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH时mm分ss秒");
        }
        return currString = sdf.format(new java.util.Date());
    } 
    
    /** 
     * 返回：当前系统年份 
     * @return String 
     */ 
    public static String getYear() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split("-")[0]; 
    } 
    
    /** 
     * 返回：当前系统月份 
     * @return 09 
     */ 
    public static String getMonth() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split("-")[1]; 
    } 
    
    /** 
     * 返回：当前系统日 
     * @return 09 
     */ 
    public static String getDate() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split("-")[2].split(" ")[0]; 
    } 
    
    /** 
     * 返回：系统当前日期 
     * @return 2009-09-09 
     */ 
    public static String getCurrentDate(){ 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split(" ")[0]; 
    } 
    
    /** 
     * 返回：系统当前时间 
     * @return 09:09:09 
     */ 
    public static String getCurrentTime() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split(" ")[1]; 
    } 
    
    /** 
     * 返回：系统当前完整日期时间 
     * @return 2009-09-09 09:09:09 
     */ 
    public static String getCurrentFullTime() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()); 
    } 
    
    /** 
     * 返回：系统当前时间时 
     * @return 09 
     */ 
    public static String getHours() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split(" ")[1].split(":")[0]; 
    } 
    
    /** 
     * 返回：系统当前时间分 
     * @return 12 
     */ 
    public static String getMinutes() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split(" ")[1].split(":")[1]; 
    } 
    
    /** 
     * 返回：系统当前时间秒 
     * @return 12 
     */ 
    public static String getSeconds() { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new java.util.Date()).split(" ")[1].split(":")[2]; 
    } 

}