package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.IpMapper;
import com.yitkeji.songshushenghuo.vo.model.Ip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class IpService extends BaseService<Ip> {

    @Autowired
    private IpMapper ipMapper;

    public List<Ip> findNearlyIps(String province, String city) {
        if (province == null && city == null) {
            return new ArrayList<>();
        }
        if (province == null) {
            return ipMapper.findByCity("%" + city + "%");
        }
        return ipMapper.findByProvinceAndCity("%" + province + "%", "%" + city + "%");
    }
}
