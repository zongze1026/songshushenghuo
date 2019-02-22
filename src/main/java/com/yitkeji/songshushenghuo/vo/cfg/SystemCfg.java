package com.yitkeji.songshushenghuo.vo.cfg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitkeji.songshushenghuo.service.TbSystemService;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.model.TbSystem;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 获取系统配置，
 * PS：使用时应当确保始终从该对象中取值，不可作为静态变量使用，以免定时刷新配置的策略失效。
 */
public class SystemCfg extends SystemEntity {

    private Logger logger = Logger.getLogger(SystemCfg.class);


    private static TbSystemService systemService = null;

    private SystemCfg(){
        systemService = SpringUtil.getBean(TbSystemService.class);
        this.freshConfig();
    }


    private static final class SystemCfgHolder{
        private static final SystemCfg SYSTEM_CFG = new SystemCfg();
    }

    public static final SystemCfg getInstance(){
        return SystemCfgHolder.SYSTEM_CFG;
    }

    public Boolean freshConfig(){

        Map<String, JSONObject> cfgMap = new HashMap<>();
        List<TbSystem> list = systemService.findList(new QueryBuilder(TbSystem.class));
        for(TbSystem tbSystem: list){
            String key = tbSystem.getKey();
            String value = tbSystem.getValue();
            try{
                JSONObject jo = JSON.parseObject(value);
                if(key.indexOf(".") != -1){
                    String[] ks = key.split("\\.");
                    JSONObject po;
                    if(cfgMap.get(ks[0]) == null){
                        po = new JSONObject();
                    }else{
                        po = cfgMap.get(ks[0]);
                    }
                    po.put(ks[1], jo);
                    cfgMap.put(ks[0], po);
                    continue;
                }
                cfgMap.put(key, jo);
            }catch (Exception e){
                logger.error("读取配置异常：" + key + e);
            }
        }
        SystemEntity systemEntity = JSON.parseObject(JSON.toJSONString(cfgMap), SystemEntity.class);
        BeanUtils.copyProperties(systemEntity, this);
        return true;
    }

}
