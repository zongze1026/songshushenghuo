package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.SystemMapper;
import com.yitkeji.songshushenghuo.vo.model.TbSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class TbSystemService extends BaseService<TbSystem> {

    @Autowired
    private SystemMapper systemMapper;


    public List<Map<String, Object>> showTableStatus() {
        return systemMapper.showTableStatus();
    }
}
