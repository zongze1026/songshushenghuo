package com.yitkeji.songshushenghuo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类，常用方法封装
 */
public class ZGLTool {
    private static final String STR_FORMAT = "00000000";
    private static char[] hexDigs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String CHASET_UTF_8 = "UTF-8";
    /**
     * 格式为
     * 将日期时间转化为统一格式的字符串一起
     */
    public static String formatToLocaleDateTimeTogether(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");//hh为12小时制，HH为24小时制
        try {
            synchronized (sdf) {
                return sdf.format(date);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 将日期转化为统一格式的字符串
     */
    public static String formatLongToLocale(long date) {
        if (date == 0) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(date);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * 将日期转化为统一格式的字符串
     */
    public static String formatToLocale(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            synchronized (sdf) {
                return sdf.format(date);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 将日期时间转化为统一格式的字符串
     */
    public static String formatToLocaleDateTime(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//hh为12小时制，HH为24小时制
        try {
            synchronized (sdf) {
                return sdf.format(date);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 将日期时间转化为统一格式的字符串
     */
    public static String formatToLocaleTime(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            synchronized (sdf) {
                return sdf.format(date);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 将年月日转化为统一格式的字符串
     */
    public static String formatToLocale(int year, int month, int dayOfMonth) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (month+1 < 10) {
            sb.append(0);
        }
        sb.append(month + 1).append("-");
        if (dayOfMonth < 10) {
            sb.append(0);
        }
        sb.append(dayOfMonth);
        return sb.toString();
    }

    /**
     * 时分转化为统一格式的字符串
     */
    public static String formatToHourMinute(int hour, int minute) {
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append(0).append(hour).append(":");
        }else{
            sb.append(hour).append(":");
        }
        if(minute<10){
            sb.append(0).append(minute);
        }else
        {
            sb.append(minute);
        }

        return sb.toString();
    }

    /**
     * 将字符串转化为时间
     */
    public static Date parseToLocale(String date) {
        if (isBlank(date)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            synchronized (sdf) {
                return sdf.parse(date);
            }
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串为空内容
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 将float类型的数字转化为货币格式, 如 100,000.00的格式
     */
    public static String formatMoneyToFloat(float value) {
        NumberFormat format = NumberFormat.getInstance(Locale.CHINA);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(value);
    }

    /**
     * 获取年份的后2位
     * @return
     */
    private static String getYearLast2Bit(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String str_date=sdf.format(new Date());
        return str_date.substring(2);
    }

    /**
     * 随即生成指定长度的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {           //length表示生成字符串的长度
//	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   //生成字符串从此序列中取
        String base = "0123456789";   //生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成唯一的uuid用做token
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 生成唯一的userId用做token
     * @return
     */
    public static String getUserUUID(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 随机生成指定长度字符串，由数字和小写字母组成
     *
     * @return
     */
    public static String randomString(int length) {
        if (length <= 0) {
            return StringPool.EMPTY;
        }
        String seed = "abcdefghijklmnopqrstuvwxyz0123456789";
        if (length >= seed.length()) {
            throw new IllegalArgumentException("长度必须小于" + seed.length());
        }
        char[] seedArray = seed.toCharArray();
        String result = "";
        for (int j = 0; j < length; j++) {
            char c = seedArray[(int) (Math.random() * 36)];
            result += c;
        }
        return result;
    }

    /**
     * 读取src/main/java/jdbc.properties里面的信息
     * @return
     */
    public static String getProperties(){
        String baseurl=null;
        Properties prop = new Properties();
        try {
            prop.load(ZGLTool.class.getResourceAsStream("/jdbc.properties"));
            baseurl = prop.getProperty("baseurl").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseurl;
    }

    public static boolean toDirectoryCheck(File toDirectory) {
        boolean isok = true;
        if (!toDirectory.exists()) {
            isok = toDirectory.mkdirs();
        }
        return isok;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     * author: muzg
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename + "_";
    }

    /*
     * Java文件操作 获取文件扩展名
     *
     * Author: muzg
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    /**
     * 生成签名字符串
     * @param params
     * @return
     */
    public static String genSignStr(Map params){
        params.remove("sign");
        String[] keys = (String[])params.keySet().toArray(new String[0]);
        Arrays.sort(keys);                                                 //排序
        StringBuilder strBuilder = new StringBuilder();
        for (String key : keys) {
            String value = ""+params.get(key);                             //所有值都强制转换为字符串
            strBuilder.append(key).append(value);                          //拼装排序的字符串
        }
        return strBuilder.toString();
    }

    /**
     * 将响应输出
     * @param response,res
     * @return
     */
    public static void outToPrint(HttpServletResponse response, Result res){
        try {
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSONString(res));
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 有盾接口MD5算法
     * @param source
     * @return
     */
    public static String MD5Encrpytion(String source) {
        try {
            byte[] strTemp = source.getBytes(Charset.forName("UTF-8"));
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigs[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigs[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 有盾接口MD5算法
     * @param source
     * @return
     */
    public static String MD5Encrpytion(byte[] source) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigs[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigs[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * 有盾接口Http请求
     */
    public static JSONObject doHttpRequest(String url, JSONObject reqJson) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        //设置传入参数
        StringEntity entity = new StringEntity(reqJson.toJSONString(), CHASET_UTF_8);
        entity.setContentEncoding(CHASET_UTF_8);
        entity.setContentType("application/json");
        System.out.println(url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            String respContent = EntityUtils.toString(he, CHASET_UTF_8);
            return (JSONObject) JSONObject.parse(respContent);
        }
        return null;
    }
}
