package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.enums.CardStatus;
import com.yitkeji.songshushenghuo.vo.enums.CardType;
import com.yitkeji.songshushenghuo.vo.model.BankAuth;
import com.yitkeji.songshushenghuo.vo.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@CacheConfig(cacheNames = "card")
public class CardService extends BaseService<Card> {

    @Autowired
    private BankAuthService bankAuthService;


    public Card findByCardNo(String cardNo) {
        QueryBuilder queryBuilder = new QueryBuilder(Card.class);
        queryBuilder.setEQQueryCond("cardNo", cardNo);
        return super.findOne(queryBuilder);
    }

    public Card findByPrimaryKey(Long cardId, CardStatus status) {
        QueryBuilder queryCard = new QueryBuilder(Card.class);
        queryCard.setEQQueryCond("status", status.getCode());
        queryCard.setEQQueryCond("cardId", cardId);
        Card card = super.findOne(queryCard);
        return card;
    }

    /**
     * 添加四要素认证
     *
     * @param auth
     * @return
     */
    public int addBankAuth(BankAuth auth, Card card) {
        int isokAdd = bankAuthService.add(auth);
        if (isokAdd < 1) {
            return -1;
        }
        card.setExpiryDate(card.getExpiryDate().replace("/", ""));
        card.setType(CardType.CREDIT.getCode());
        card.setStatus(CardStatus.NORMAL.getCode());
        card.setCreateTime(new Date());
        return super.add(card);
    }

}
