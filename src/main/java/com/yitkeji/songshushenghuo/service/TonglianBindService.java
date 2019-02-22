package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.TonglianBind;
import org.springframework.stereotype.Service;

@Service
public class TonglianBindService extends BaseService<TonglianBind> {

    public TonglianBind findByCardId(Long cardId) {
        QueryBuilder queryBuilder = new QueryBuilder(TonglianBind.class);
        queryBuilder.setEQQueryCond("cardId", cardId);
        return findOne(queryBuilder);
    }
}
