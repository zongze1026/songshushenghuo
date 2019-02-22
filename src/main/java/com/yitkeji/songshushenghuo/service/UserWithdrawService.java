package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.UserWithdrawMapper;
import com.yitkeji.songshushenghuo.vo.model.UserWithdraw;
import com.yitkeji.songshushenghuo.vo.res.app.UserWithdrawRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserWithdrawService extends BaseService<UserWithdraw> {

    @Autowired
    private UserWithdrawMapper userWithdrawMapper;

    public UserWithdraw createUserWithdraw(UserWithdraw userWithdraw) {
        userWithdraw.setCreateTime(new Date());
        super.add(userWithdraw);
        return userWithdraw;
    }

    public UserWithdrawRes findUserWithdrawInfo(Long userWithdrawId) {
        UserWithdrawRes userWithdrawRes = userWithdrawMapper.findUserWithdrawInfo(userWithdrawId);
        return userWithdrawRes;
    }
}
