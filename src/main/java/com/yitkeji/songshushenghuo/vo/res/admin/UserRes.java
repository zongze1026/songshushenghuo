package com.yitkeji.songshushenghuo.vo.res.admin;

import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserRes {

    private Long userId;

    private String userName;

    private String agentToken;

    private int money;

    private String realName;

    private String phone;

    private String idcard;

    private int authStatus;

    private Long debitCardId;

    private int vip;

    private String longitude; //经度

    private String latitude; //经度

    private String city; //城市

    private UserStatus status;

    private Date createTime;

    private Long referenceId;

    private String referenceName;

    private String referenceUserName;

    private Long agentId;

    private String agentName;

    private String agentUserName;

    private String debitCardNo;

    private String debitCardBank;

}