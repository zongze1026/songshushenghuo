package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.enums.DocStatus;
import com.yitkeji.songshushenghuo.vo.enums.DocType;
import com.yitkeji.songshushenghuo.vo.model.Doc;
import com.yitkeji.songshushenghuo.vo.res.app.DocRes;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DocService extends BaseService<Doc> {

    public List<DocRes> findByType(DocType docType) {
        QueryBuilder queryBuilder = new QueryBuilder(Doc.class);
        queryBuilder.setEQQueryCond("status", DocStatus.NORMAL.getCode());
        queryBuilder.setEQQueryCond("type", docType);
        return super.findList(queryBuilder, DocRes.class);
    }
}
