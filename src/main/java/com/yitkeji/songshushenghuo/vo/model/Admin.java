package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.util.EncryptUtil;
import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.AdminStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_admin")
public class Admin {

    @PrimaryKey("admin_id")
    private Long adminId;

    private String token;

    private String userName;

    private String password;

    private String name;

    private Long roleId;

    private String phone;

    private String email;

    private String avatar;

    private int loginCount;

    private String loginIp;

    private Date loginTime;

    private AdminStatus status;

    private Date createTime;

    private Date lastupdatetime;

    public static final String encryptionPwd(String userName, String password) {
        String encryUserName = EncryptUtil.md5Encrypt(userName);
        String encryPass = EncryptUtil.md5Encrypt(password);
        return EncryptUtil.md5Encrypt(encryUserName + encryPass);
    }
}