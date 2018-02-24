package com.yy.yhttputils.utils;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MCach {
    private static Map<String,String> datas = new HashMap<>();

    private static Map<String, Cookie> cookies = new HashMap<>();

    public static void putMetaDatas(Bundle metaData, Class clasz){
        if(metaData != null){
            String className = clasz.getSimpleName();
            Set<String> keys = metaData.keySet();

            for(String key : keys){
                String value = metaData.getString(key);
                if(value == null && metaData.get(key) != null){
                    value = String.valueOf(metaData.get(key));
                }
                datas.put(className + "_" + key,value);
            }
        }
    }

    public static String getMetaData(String key, Class clasz){
        String className = clasz.getSimpleName();
        return datas.get(className + "_" + key);
    }

//    public static String getUrl(String key, Class clasz){
//        if(!key.endsWith(".url")){
//            throw new RuntimeException("key不是获取url的key");
//        }
//        String host = getMetaData("host", App.class);
//        String className = clasz.getSimpleName();
//        return host + datas.get(className + "_" + key);
//    }


    public static void addCookie(String name, Cookie cookie){
        MCach.cookies.put(name,cookie);
    }

    public static void saveCookies(List<Cookie> cookies){
        for(Cookie cookie : cookies){
            MCach.cookies.put(cookie.name(),cookie);
        }
    }

    public static Cookie getCookie(String name){
        return MCach.cookies.get(name);
    }

    public static List<Cookie> getCookies(){
        List<Cookie> cookies = new ArrayList<>();
        for(String name : MCach.cookies.keySet()){
            cookies.add(MCach.cookies.get(name));
        }
        return cookies;
    }


}
