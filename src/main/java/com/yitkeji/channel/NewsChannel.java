package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.NewsCfg;

import java.util.HashMap;
import java.util.Map;

public class NewsChannel{

    private static HttpUtil httpUtil = new HttpUtil();

    /**
     * 财经新闻头条接口
     */
    private static Object headlines(){
        NewsCfg cfg = SystemCfg.getInstance().getChannel().getNews();
        if(cfg.getDisabled()){
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("type", "caijing");
        params.put("key", cfg.getKey());
        String result = httpUtil.postForm(cfg.getBaseurl(), params);
        JSONObject jsonResult = JSONObject.parseObject(result);

        if ("0".equals(jsonResult.getString("error_code"))){
            JSONObject data = JSONObject.parseObject(jsonResult.getString("result"));
            return data;
        }else {
            return jsonResult.getString("reason");
        }
    }

    /**
     * 获取新闻
     */
    public static Object  getHeadlines(){
        Object result = NewsChannel.headlines();
        if(result instanceof String){
            return result;
        }
        JSONObject jsonResult = (JSONObject) JSONObject.toJSON(result);
        return jsonResult;
    }

    public static void main(String[] args) {
        HttpUtil httpUtil = new HttpUtil();
        Map<String, Object> params = new HashMap<>();
        params.put("type", "caijing");
        params.put("key", "58be58fa4ca144cb7eb8a22420cb1840");
        String str = httpUtil.postForm("http://v.juhe.cn/toutiao/index", params);
        System.out.println(JSON.toJSONString(str));
    }
}
