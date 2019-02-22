package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.YunketongBind;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YunketongBindService extends BaseService<YunketongBind> {

    public YunketongBind findByCardId(Long cardId, String... channels) {
        QueryBuilder queryBuilder = new QueryBuilder(YunketongBind.class);
        queryBuilder.setEQQueryCond("cardId", cardId);
        if (channels != null && channels.length > 0) {
            queryBuilder.setInQueryCond("channel", channels);
        }
        List<YunketongBind> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
