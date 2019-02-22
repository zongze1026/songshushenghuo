package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.YunshanfuRegisted;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YunshanfuRegistedService extends BaseService<YunshanfuRegisted> {

    public YunshanfuRegisted findByIdcard(String idcard) {
        QueryBuilder queryBuilder = new QueryBuilder(YunshanfuRegisted.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        List<YunshanfuRegisted> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
