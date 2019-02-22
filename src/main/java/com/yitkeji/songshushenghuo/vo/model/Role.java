package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.enums.Sensitivity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_role")
public class Role {

    @PrimaryKey("role_id")
    private Long roleId;

    private String roleName;

    private String comment;

    private RoleStatus status;

    private Date createTime;

    private Date lastupdatetime;

    private String authority;

    private Sensitivity sensitivity;

    private Boolean readOnly;

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    public boolean checkAuth(String url) {
        if (!RoleStatus.NORMAL.equals(status)) {
            return false;
        }
        String[] authorities = authority.split("[\\s|,]+");
        for (String auth : authorities) {
            if (auth.equals(url)) {
                return true;
            }
        }
        return false;
    }
}