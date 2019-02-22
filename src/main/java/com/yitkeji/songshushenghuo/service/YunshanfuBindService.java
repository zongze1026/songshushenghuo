package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.YunshanfuBind;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YunshanfuBindService extends BaseService<YunshanfuBind> {

    public YunshanfuBind findByCardId(Long cardId) {
        QueryBuilder queryBuilder = new QueryBuilder(YunshanfuBind.class);
        queryBuilder.setEQQueryCond("cardId", cardId);
        List<YunshanfuBind> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
