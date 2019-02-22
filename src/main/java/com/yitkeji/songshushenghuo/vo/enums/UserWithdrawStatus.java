package com.yitkeji.songshushenghuo.vo.enums;

import com.yitkeji.songshushenghuo.vo.res.app.UserWithdrawStatusRes;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum  UserWithdrawStatus {

    ALL("全部", 10),
    CHECK_WAIT("待审核", 0),
    REMITTANCE_BEING("打款中", 1),
    CHECK_NO_PASS("审核不通过", 2),
    WITHDRAW_SUCCESS("提现成功", 3);

    private String desc;
    private int code;

    UserWithdrawStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final UserWithdrawStatus getStatus(int code){
        for(UserWithdrawStatus status: UserWithdrawStatus.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }

    public static final List<UserWithdrawStatusRes> getUserWithdrawStatus(){
        List<UserWithdrawStatusRes> userWithdrawStatusResList = new ArrayList<>();
        for(UserWithdrawStatus status: UserWithdrawStatus.values()){
            UserWithdrawStatusRes userWithdrawStatusRes = new UserWithdrawStatusRes();
            userWithdrawStatusRes.setCode(status.getCode());
            userWithdrawStatusRes.setDesc(status.getDesc());
            userWithdrawStatusResList.add(userWithdrawStatusRes);
        }
        return userWithdrawStatusResList;
    }
}
