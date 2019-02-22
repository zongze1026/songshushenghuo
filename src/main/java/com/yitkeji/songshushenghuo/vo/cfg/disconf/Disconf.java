package com.yitkeji.songshushenghuo.vo.cfg.disconf;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Disconf {

    private String signKey;
    private String securityKey;
    private int expiresHours;
    private Boolean verifier;

}
