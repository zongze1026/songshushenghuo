package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Measure;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MeasureService extends BaseService<Measure> {

    public Measure createMeasure(String measureUrl, Card card, User user) {
        Measure measure = findByUserIdAndCardId(user.getUserId(), card.getCardId());
        if (null == measure){
            measure = new Measure();
            measure.setCardId(card.getCardId());
            measure.setUserId(user.getUserId());
            measure.setMeasureUrl(measureUrl);
            measure.setCreateTime(new Date());
            super.add(measure);
        } else {
            measure.setMeasureUrl(measureUrl);
            measure.setCreateTime(new Date());
            super.updateByPrimaryKey(measure);
        }
        return measure;
    }

    public Measure findByUserIdAndCardId(Long userId, Long cardId) {
        QueryBuilder queryBuilder = new QueryBuilder(Measure.class);
        queryBuilder.setEQQueryCond("userId", userId);
        queryBuilder.setEQQueryCond("cardId", cardId);
        return super.findOne(queryBuilder);
    }
}
