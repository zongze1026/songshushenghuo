package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.model.Admin;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class AdminService extends BaseService<Admin> {

    @Override
    public int updateByPrimaryKey(Admin admin, String... columns) {
        int rows = super.updateByPrimaryKey(admin, columns);
        RedisUtil.set(CacheKey.ADMIN_TOKEN.getKey(admin.getToken()), admin, 30, TimeUnit.MINUTES);
        return rows;
    }
}
