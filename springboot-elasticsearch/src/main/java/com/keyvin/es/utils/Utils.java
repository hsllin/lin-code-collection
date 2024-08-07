package com.keyvin.es.utils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: weiwenhan708
 * @Date: 2020-4-16 14:48
 */
public class Utils {

    public static long getNumberForCode(String code){
        if(code.matches(".*[a-zA-Z].*")){
             Pattern p = Pattern.compile("[a-zA-z]");
             Matcher matcher = p.matcher(code);
             // 把字母替换成 0
            String qx_new = matcher.replaceAll("");
            return Long.parseLong(qx_new);
        }
        return 0;
    }

    /**
     * 获取uuuid大写
     */
    public static String getUuidUp(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
    /**
     * 获取uuuid小写
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getRandString(int length) {
        String charList = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String rev = "";
        Random f = new Random();
        for(int i=0;i<length;i++){
            rev += charList.charAt(Math.abs(f.nextInt())%charList.length());
        }
        return rev;
    }

    /**
     * 转换成百分比
     */
    public static String rate(float invoke) {
       return  (int)(invoke*100)+"%";
    }

    public static void main(String[] args) {
        System.out.println(getUuid());
        System.out.println(getUuidUp());
    }

}
