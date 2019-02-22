package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.ChangjieNewRegisted;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChangjieNewRegistedService extends BaseService<ChangjieNewRegisted> {

    private Logger logger = Logger.getLogger(ChangjieNewRegistedService.class);

    public ChangjieNewRegisted findByIdcard(String idcard, String... channels) {
        QueryBuilder queryBuilder = new QueryBuilder(ChangjieNewRegisted.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        if (channels != null && channels.length > 0) {
            queryBuilder.setInQueryCond("channel", channels);
        }
        List<ChangjieNewRegisted> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public ChangjieNewRegisted findByIdcard(String idcard, Channel... channels) {
        QueryBuilder queryBuilder = new QueryBuilder(ChangjieNewRegisted.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        if (channels != null && channels.length > 0) {
            queryBuilder.setInQueryCond("channel", channels);
        }
        List<ChangjieNewRegisted> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
