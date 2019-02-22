package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.YunketongRegisted;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YunketongRegistedService extends BaseService<YunketongRegisted> {

    public YunketongRegisted findByIdcard(String idcard, String... channels) {
        QueryBuilder queryBuilder = new QueryBuilder(YunketongRegisted.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        if (channels != null && channels.length > 0) {
            queryBuilder.setInQueryCond("channel", channels);
        }
        List<YunketongRegisted> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
