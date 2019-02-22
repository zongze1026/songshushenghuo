package com.yitkeji.songshushenghuo.vo.cfg.bank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.CardType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@ApiModel
public class Banks extends HashMap<String, Bank> {

    @Override
    public Bank get(Object key) {
        String str = JSON.toJSONString(super.get(key));
        return JSON.parseObject(str, Bank.class);
    }

    public static final Bank getByCode(String bankCode, CardType cardType){
        Object object = SystemCfg.getInstance().getBanks().get(bankCode);

        switch (cardType){
            case CREDIT: object = SystemCfg.getInstance().getBanks().get(bankCode); break;
            case DEBIT: object = SystemCfg.getInstance().getDebitbanks().get(bankCode); break;
            default:
        }

        Bank bank = JSON.parseObject(JSON.toJSONString(object), new TypeReference<Bank>(){});
        if(null == bank){
            bank = new Bank();
        }
        return bank;
    }
}
