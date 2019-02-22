package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.TonglianRegisted;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TonglianRegistedService extends BaseService<TonglianRegisted> {

    private Logger logger = Logger.getLogger(TonglianRegistedService.class);

    public TonglianRegisted findByIdcard(String idcard) {
        QueryBuilder queryBuilder = new QueryBuilder(TonglianRegisted.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        return findOne(queryBuilder);
    }

}
