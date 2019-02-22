package com.yitkeji.test.songshushenghuo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yitkeji.channel.inter.Logger;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.SignUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.*;

public class BaseTest {

    public static String baseurl = "http://api.songshushenghuo.test.yitkeji.com";
//    public static String baseurl = "http://127.0.0.1:9705";
    public static String apiKey = "qskg";
    private static final Map<String, String> headers = new HashMap(){{
        put("token", "2338da05c1904b9ca827e6520d50d1ad");
    }};

    public static HttpUtil httpUtil = new HttpUtil();
    static {
        httpUtil.setLogger(new Logger() {
            @Override
            public void httpLog(String url, String method, String data, String result, Date startTime, Date endTime) {
                System.out.println("发送请求：" + url);
                System.out.println("请求参数：" + data);
                System.out.println("响应结果：" + result);
            }
        });
    }

    /**
     * 发送JSON格式请求
     * @param url
     * @param jsonStr
     * @return
     */
    public static String postJson(String url, String jsonStr) {
        return httpUtil.callHttp(url, jsonStr, "POST", HttpUtil.MEDIA_TYPE_JSON, headers);
    }

    public static String postForm(String url, String formStr){
        return httpUtil.postForm(url, formStr);
    }


    /**
     * 请求签进行签名
     * @param url
     * @param obj
     * @return
     */
    public static String postSignJson(String url, Object obj){
        if(obj == null){
            return null;
        }
        String jsonStr = JSON.toJSONString(obj);
        TreeMap<String, Object> treeMap = JSON.parseObject(jsonStr, new TypeReference<TreeMap<String, Object>>(){});
        treeMap.put("sign", SignUtil.signByMap(apiKey, treeMap));
        return postJson(url, JSON.toJSONString(treeMap));
    }


    public static String postXml(String url, String xmlStr){
        return httpUtil.postXml(url, xmlStr);
    }


    public static String getForm(String url, Object obj){
        StringBuffer stringBuffer = new StringBuffer();
        if(obj != null){
            Map<String, Object> map = ObjectUtil.objectToMap(obj);
            Iterator<String> iterable = map.keySet().iterator();
            while (iterable.hasNext()){
                String key = iterable.next();
                try {
                    stringBuffer.append(key + "=" + URLEncoder.encode(map.get(key) + "", "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                stringBuffer.append("&");
            }
        }
        if(stringBuffer.length() > 0){
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        return httpUtil.callHttp(url, stringBuffer.toString(), HttpUtil.METHOD_GET, HttpUtil.MEDIA_TYPE_FORM, headers);
    }

    public static String getJson(String url, Object obj){
        return httpUtil.callHttp(url, JSON.toJSONString(obj), HttpUtil.METHOD_GET, HttpUtil.MEDIA_TYPE_JSON, headers);
    }


    public static String getSignJson(String url, Object obj){
        if(obj == null){
            return null;
        }
        String jsonStr = JSON.toJSONString(obj);
        TreeMap<String, Object> treeMap = JSON.parseObject(jsonStr, new TypeReference<TreeMap<String, Object>>(){});
        treeMap.put("sign", SignUtil.signByMap(apiKey, treeMap));
        return getJson(url, treeMap);
    }

}
