package com.yitkeji.songshushenghuo.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @author Administrator
 */
public class IdUtil {

    private IdUtil(){}

    private static final Logger LOGGER = LoggerFactory.getLogger(IdUtil.class);
    /**
     * number pattern of datetime
     */
    private static final String NUMBERFORMAT = "yyyyMMddHHmmssSSS";

    private static int increase =0;


    private static long lastTime=-1L;



    /**
     * 生成ID
     *
     * @return 32位的ID
     */
    public synchronized static final String nextId(String bizCode) {
        if (StringUtils.isBlank(bizCode) || bizCode.length() != 3) {
            throw new IllegalArgumentException("bizCode的长度必须等于3");
        }
        //当前时间
        long timestamp = System.currentTimeMillis();
        if(timestamp>lastTime){
            increase=0;
            lastTime=timestamp;
        }
        int id = ++increase;
        String timestampStr = DateUtil.format(new Date(), NUMBERFORMAT);
        int random= (int)(Math.random()*10000000);
        //000代表预留位
        return timestampStr + bizCode + String.format("%08d", random)+String.format("%04d", id);
    }

}
