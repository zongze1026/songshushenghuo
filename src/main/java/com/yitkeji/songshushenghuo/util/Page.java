package com.yitkeji.songshushenghuo.util;

import com.yitkeji.songshushenghuo.vo.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 */
@ApiModel
@Setter
@Getter
public class Page<T> {

    @ApiModelProperty("当前页")
    private int npage = 0; // 当前页

    @ApiModelProperty("总页数")
    private int spage = 0; // 总页数

    @ApiModelProperty("总条数")
    private int total = 0; // 总条数

    @ApiModelProperty("每页条数")
    private int pageSize = 20; // 每页条数

    @ApiModelProperty("当前页数据")
    private List<T> data = new ArrayList(); // 当前页附带的数据

    public Page(){}
    public Page(PageReq pageReq){
        BeanUtils.copyProperties(pageReq, this);
    }
    
    public Page(int pageSize){
        this.pageSize = pageSize;
    }

    private void compute(){
        pageSize = pageSize > 0 ? pageSize : 20;
        spage = total % pageSize > 0 ? total / pageSize + 1: total / pageSize;
    }

    public void setTotal(int total) {
        this.total = total;
        this.compute();
    }
}
