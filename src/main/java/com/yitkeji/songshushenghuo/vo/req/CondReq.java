package com.yitkeji.songshushenghuo.vo.req;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 条件查询
 */
public class CondReq extends BaseReq{
    @Setter
    private String cond;

    @Getter
    private List<Map<String, List>> localCond = new ArrayList<>();

    public void setCond(String cond){
        try {
            this.cond = URLDecoder.decode(cond, "utf8");
            this.localCond = JSON.parseObject(this.cond, this.localCond.getClass());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
