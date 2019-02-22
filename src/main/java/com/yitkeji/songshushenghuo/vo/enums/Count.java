package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Count{

    // 表统计（记录条数）
    TB_USER("tb_user"),
    TB_ADMIN("tb_admin"),
    TB_AUTH("tb_auth"),
    TB_BANK_AUTH("tb_bank_auth"),
    TB_CARD("tb_card"),
    TB_CHANGJIE_BIND("tb_changjie_bind"),
    TB_CHANGJIE_REGISTED("tb_changjie_registed"),
    TB_COMMISSION("tb_commission"),
    TB_DOC("tb_doc"),
    TB_LOG_API("tb_log_api"),
    TB_ORDER("tb_order"),
    TB_ROLE("tb_role"),
    TB_SYSTEM("tb_system"),
    TB_USER_WITHDRAW("tb_user_withdraw"),
    TB_CHANGJIE_NEW_REGISTED("tb_changjie_new_registed"),
    TB_CHANGJIE_NEW_BIND("tb_changjie_new_bind"),
    TB_LOG_ACTION("tb_log_action"),

    TB_YUNKETONG_REGISTED("tb_yunketong_registed"),
    TB_YUNKETONG_BIND("tb_yunketong_bind"),
    TB_TONGLIAN_BIND("tb_tonglian_bind"),
    TB_TONGLIAN_REGISTED("tb_tonglian_registed"),
    TB_YUNSHANFU_REGISTED("tb_yunshanfu_registed"),
    TB_YUNSHANFU_BIND("tb_yunshanfu_bind"),
    TB_YUNSHANFU_AREA("tb_yunshanfu_area"),

    TB_IP("tb_ip");

    @Setter
    private Integer count = 0;
    private String desc;

    Count(String desc){
        this.desc = desc;
    }
}
