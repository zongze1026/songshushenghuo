package com.yitkeji.songshushenghuo.service;

import com.alibaba.fastjson.JSON;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.model.Role;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = Logger.getLogger(RoleService.class);

    @Override
    public int updateByPrimaryKey(Role role, String... columns) {
        int rows = super.updateByPrimaryKey(role, columns);
        if(rows > 0){
            RedisUtil.set(CacheKey.ROLE.getKey(role.getRoleId()), role, 7, TimeUnit.DAYS);
        }
        return rows;
    }

    public void freshCache(Role role){
        redisTemplate.opsForValue().set(CacheKey.ROLE.getKey(role.getRoleId()), JSON.toJSONString(role), 7, TimeUnit.DAYS);
    }

    public Role findByPrimaryKey(long primaryKey) {
        Object cache = null;
        Role role;
        if(SystemCfg.getInstance().getAdmin().getCache()){
            cache = redisTemplate.opsForValue().get(CacheKey.ROLE.getKey(primaryKey));
        }
        if(cache == null){
            role = super.findByPrimaryKey(primaryKey);
            freshCache(role);
        }else{
            role = JSON.parseObject((String)cache, Role.class);
        }
        return role;
    }
}
