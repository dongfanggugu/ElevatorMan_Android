/** 
 * Project Name：CommunityService 
 * File Name：EncryptUtils.java 
 * Package Name：com.cooperlink.communityservice.utils 
 * Date：2015-4-10 下午2:12:50 
 * Copyright (c) 2015, cooperlink.cn All Rights Reserved. 
 * 北京酷博灵科信息技术有限公司
 */
package com.honyum.elevatorMan.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title：加密数据
 * @Description：
 * @Package com.cooperlink.communityservice.utils
 * @ClassName EncryptUtils
 * @author harris   
 * @date 2015-4-10 下午2:12:50
 * @version 
 */
public class EncryptUtils {
	 /**
     * MD5加密
     *
     * @param string
     * @return
     */
//    public static String encryptMD5(String encrypt) {
//        String s = encrypt;
//        if (s == null) {
//            return "";
//        } else {
//            String value = null;
//            MessageDigest md5 = null;
//            try {
//                md5 = MessageDigest.getInstance("MD5");
////                value = Base64.encodeBase64URLSafeString(md5.digest(s.getBytes("utf-8")));
//                value = Base64.encodeToString(md5.digest(s.getBytes("utf-8")), Base64.NO_PADDING);
//
//
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return value;
//        }
//    }
	public static String encryptMD5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
}
