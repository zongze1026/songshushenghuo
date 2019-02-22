package com.yitkeji.songshushenghuo.util;

import org.apache.commons.lang.StringUtils;

public class HideDataUtil {


    public static final String hide(String data, String reg1, String reg2){
        if(StringUtils.isBlank(data)){
            return data;
        }
        return data.replaceAll(reg1, reg2);
    }

    public static final String hideName(String name){
        if(StringUtils.isBlank(name)){
            return name;
        }
        Double length = Double.parseDouble(name.length() + "");
        switch (length.intValue()){
            case 1: return name;
            case 2: return hide(name, "^\\S+(\\S{1})$", "*$1");
            default: return hide(name, "^(\\S{"+(Math.round(length / 3))+"})\\S+(\\S{"+(Math.round(length / 3))+"})$", "$1*$2");
        }
    }

    public static final String hidePhone(String phone){
        return hide(phone, "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
    }

    public static final String hideIdcard(String idcard){
        return hide(idcard, "^(\\w{6})\\w+", "$1*********");
    }

    public static final String hideCardNo(String cardNo){
        return hide(cardNo, "^(\\w{4})\\w+(\\w{4})$", "$1********$2");
    }

}
