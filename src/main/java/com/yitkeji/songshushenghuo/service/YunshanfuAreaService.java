package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.YunshanfuArea;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YunshanfuAreaService extends BaseService<YunshanfuArea> {

    public YunshanfuArea findByAreaCode(Integer areaCode) {
        if (areaCode == null) {
            return null;
        }
        QueryBuilder queryBuilder = new QueryBuilder(YunshanfuArea.class);
        queryBuilder.setEQQueryCond("areaCode", areaCode);
        return this.findOne(queryBuilder);
    }

    public YunshanfuArea findByUser(User user) {
        if (user.getCity() == null) {
            return null;
        }
        QueryBuilder queryBuilder = new QueryBuilder(YunshanfuArea.class);
        queryBuilder.setEQQueryCond("areaName", user.getCity().replace("市", ""));
        queryBuilder.setNotEQQueryCond("provinceCode", 0);
        List<YunshanfuArea> list = this.findList(queryBuilder);
        return list.size() > 0 ? list.get(0) : null;
    }

}
