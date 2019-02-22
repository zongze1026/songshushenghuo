package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.ChangjieNewBind;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangjieNewBindService extends BaseService<ChangjieNewBind> {

    public ChangjieNewBind findByCardId(Long cardId, String... channels) {
        QueryBuilder queryBuilder = new QueryBuilder(ChangjieNewBind.class);
        queryBuilder.setEQQueryCond("cardId", cardId);
        if (channels != null && channels.length > 0) {
            queryBuilder.setInQueryCond("channel", channels);
        }
        List<ChangjieNewBind> list = super.findList(queryBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
