package com.jsf.nettydemo.demo2.util;

/**
 * @author ：jiasanfeng
 * @date ：Created in 2020-07-03 11:19
 */
public class LogUtil {

    public static void log(String fromat,Object ... params){
        System.out.println(String.format(fromat,params));
    }
}
