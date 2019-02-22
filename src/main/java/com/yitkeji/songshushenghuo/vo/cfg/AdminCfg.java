package com.yitkeji.songshushenghuo.vo.cfg;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminCfg {
    /**
     * 异常通知邮件列表
     */
    private String[] noticeEmails;
    /**
     * 上线启用部分缓存，以提高性能
     * 角色，IP查询
     */
    private Boolean cache;
    /**
     * 是否启用后台账号单点登录的限制
     */
    private Boolean syncLogin;
}
