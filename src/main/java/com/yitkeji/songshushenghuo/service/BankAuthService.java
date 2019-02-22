package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.BankAuth;
import org.springframework.stereotype.Service;

@Service
public class BankAuthService extends BaseService<BankAuth> {

    /**
     * 根据四要素信息查找认证记录
     *
     * @param name
     * @param idcard
     * @param phone
     * @param cardNo
     * @return
     */
    public BankAuth findByAuthInfo(String name, String idcard, String phone, String cardNo) {
        QueryBuilder queryBuilder = new QueryBuilder(BankAuth.class);
        queryBuilder.setEQQueryCond("phone", phone);
        queryBuilder.setEQQueryCond("idcard", idcard);
        queryBuilder.setEQQueryCond("name", name);
        queryBuilder.setEQQueryCond("cardNo", cardNo);
        return findOne(queryBuilder);
    }
}
