package com.yitkeji.songshushenghuo.util;


import java.text.DecimalFormat;

public class StringUtil {

    public static final String MODEL_NUM = "0123456789";
    public static final String MODEL_STR_LOW = "abcdefghijklmnopqrstuvwxyz";
    public static final String MODEL_STR_UP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String MODEL_STR = MODEL_NUM + MODEL_STR_LOW + MODEL_STR_UP;


    /**
     * 随机字符串
     * @param model
     * @param length
     * @return
     */
    public static final String random(String model, int length){
        String[] tempArr = model.split("");
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<length; i++){
            sb.append(tempArr[Integer.parseInt(new DecimalFormat("#").format(Math.random() * (tempArr.length - 1)))]);
        }
        return sb.toString();
    }

    public static final String randomStr(int length){
        return random(MODEL_STR, length);
    }


    public static final String randomNum(int length){
        return random(MODEL_NUM, length);
    }
}
