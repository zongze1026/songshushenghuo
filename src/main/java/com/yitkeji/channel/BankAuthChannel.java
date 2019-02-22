package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.songshushenghuo.service.BankAuthService;
import com.yitkeji.songshushenghuo.service.LogApiService;
import com.yitkeji.songshushenghuo.util.EncryptUtil;
import com.yitkeji.songshushenghuo.util.HttpUtils;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.BankAuthCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.BankAuth;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BankAuthChannel extends BaseChannel {
    private static HttpUtils httpUtils = new HttpUtils();
    private static LogApiService logApiService = null;
    private static BankAuthService bankAuthService = null;
    private static BankAuthCfg bankAuthCfg;

    private static final class ChannelHolder{
        private static final BankAuthChannel CHANNEL = new BankAuthChannel();
    }

    private BankAuthChannel() {
        super(Channel.BANKAUTH);
        logApiService = SpringUtil.getBean(LogApiService.class);
        bankAuthService = SpringUtil.getBean(BankAuthService.class);
        httpUtils.setLogger(new ChannelLogger(Channel.BANKAUTH.name(), logApiService));
    }

    public static final BankAuthChannel getInstance(){
        return ChannelHolder.CHANNEL;
    }

    static {
        bankAuthCfg = SystemCfg.getInstance().getChannel().getBankauth();
    }

    /**
     * 银行四要素验证
     */
    private static Object bankAuth(Card card) {
        Map<String, String> headers = new HashMap();
        headers.put("Authorization", "APPCODE " + bankAuthCfg.getAppCode());

        Map<String, String> body = new HashMap<>();
        body.put("ReturnBankInfo", "YES");
        body.put("cardNo", card.getCardNo());
        body.put("idNo", card.getIdcard());
        body.put("name", card.getName());
        body.put("phoneNo", card.getPhone());

        // 添加请求缓存，5分钟，避免重复调用接口
        String apiCacheKey = CacheKey.API_CACHE.getKey(EncryptUtil.md5Encrypt(JSON.toJSONString(body)));
        String retMsg = RedisUtil.get(apiCacheKey);
        if(retMsg == null){
            // 根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            Map<String, String> query = new HashMap<>();
            retMsg = httpUtils.doPost(bankAuthCfg.getUrl(), bankAuthCfg.getPath(), headers, query, body);
            RedisUtil.set(apiCacheKey, retMsg, 10, TimeUnit.MINUTES);
        }

        JSONObject result = (JSONObject) JSONObject.parse(retMsg);

        if (result == null) {
            return "系统异常，请联系客服";
        }else if ("0000".equals(result.getString("respCode"))){
            return result;
        }else {
            return result.getString("respMessage");
        }
    }

    /**
     * 信用卡认证
     * @param card
     * @param user
     * @return BankAuth || String
     */
    public Object cardVerify(Card card, User user){
        Object retMsg = BankAuthChannel.bankAuth(card);
        if(retMsg instanceof String){
            return retMsg;
        }
        JSONObject result = (JSONObject) JSONObject.toJSON(retMsg);
        BankAuth bankAuth = new BankAuth();
        bankAuth.setName(user.getRealName());
        bankAuth.setIdcard(user.getIdcard());
        bankAuth.setCardNo(result.getString("cardNo"));
        bankAuth.setPhone(result.getString("phoneNo"));
        bankAuth.setBankName(result.getString("bankName"));
        bankAuth.setBankKind(result.getString("bankKind"));
        bankAuth.setBankType(result.getString("bankType"));
        bankAuth.setBankCode(result.getString("bankCode"));
        bankAuth.setCreateTime(new Date());
        bankAuthService.add(bankAuth);
        return bankAuth;
    }

}
