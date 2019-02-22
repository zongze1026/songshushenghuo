package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.util.EncryptUtil;
import com.yitkeji.songshushenghuo.util.StringUtil;
import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import com.yitkeji.songshushenghuo.vo.enums.Vip;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Table("tb_user")
public class User {

    @PrimaryKey("user_id")
    private Long userId;

    private String token;

    private String agentToken;

    private Long agentId;

    private Long roleId;

    private String userName;

    private String password;

    private Date upvipTime;

    private Date upvip3Time;

    private Date upvip4Time;

    private int money;

    private Long debitCardId;

    private String realName;

    private String headImg;

    private String phone;

    private String email;

    private String idcard;

    private int authStatus;

    private int vip;

    private Long referenceId;

    private String referralCode;

    private String longitude;

    private String latitude;

    private String city;

    private UserStatus status;

    private Date createTime;

    private String lastLoginIp;

    private Date lastLoginTime;

    private Date lastupdatetime;


    public static final String encryptionPwd(String userName, String password) {
        String encryUserName = EncryptUtil.md5Encrypt(userName);
        String encryPass = EncryptUtil.md5Encrypt(password);
        return EncryptUtil.md5Encrypt(encryUserName + encryPass);
    }

    public static final String buildReferralCode(Long userId){
        return StringUtil.random(StringUtil.MODEL_STR_UP, 8 - userId.toString().length()) + userId;
    }

    public Vip matchVip() {
        try {
            return Vip.valueOf("VIP" + this.vip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User resetToken(){
        this.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        return this;
    }

    public User resetAgentToken(){
        this.setAgentToken(UUID.randomUUID().toString().replaceAll("-", ""));
        return this;
    }
}