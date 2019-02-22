package com.yitkeji.songshushenghuo.controller;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.inter.GlobalService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.enums.Sensitivity;
import com.yitkeji.songshushenghuo.vo.model.*;
import com.yitkeji.songshushenghuo.vo.req.CondReq;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class BaseController {


    private <T>T getCache(HttpServletRequest request, String key){
        if(request.getAttribute(key) == null){
            return null;
        }
        return (T)request.getAttribute(key);
    }

    protected Role getCacheRole(HttpServletRequest request){
        return getCache(request, "role");
    }

    protected Admin getCacheAdmin(HttpServletRequest request){
        return getCache(request, "admin");
    }

    protected User getCacheAgent(HttpServletRequest request){
        return getCache(request, "agent");
    }

    protected QueryBuilder initQueryBuilder(String module, CondReq condReq) throws BaseException {
        String className = "com.yitkeji.songshushenghuo.vo.model." + module.substring(0,1).toUpperCase() + module.substring(1);
        QueryBuilder queryBuilder = null;
        try {
            queryBuilder = new QueryBuilder(Class.forName(className));
            if(null == condReq.getLocalCond()){
                return queryBuilder;
            }
            for(Map<String, List> condMap: condReq.getLocalCond()){
                for(String key: condMap.keySet()){
                    List values = condMap.get(key);
                    String attribute = (String)values.get(0);
                    switch (key){
                        case "=": queryBuilder.setEQQueryCond(attribute, values.get(1)); break;
                        case "between": queryBuilder.setBetweenQueryCond(attribute, values.get(1), values.get(2)); break;
                        case "order": queryBuilder.addOrder(attribute, "asc".equals(((String) values.get(1)).toLowerCase())); break;
                        case "in": queryBuilder.setInQueryCond(attribute, values.subList(1, values.size()).toArray()); break;
                        default:
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new BaseException("请求路径不正确");
        } catch (Exception e){
            throw new BaseException("参数不正确");
        }
        return queryBuilder;
    }

    protected GlobalService getGlobalService(String module){
        GlobalService globalService = null;
        try{
            globalService = (GlobalService) SpringUtil.getBean(module + "Service");
        }catch (Exception e){
            e.printStackTrace();
        }
        return globalService;
    }

    private void filterAttr(Map<String, Object> map, String key, String reg, String reg2){
        if(map == null || StringUtils.isBlank(key) || StringUtils.isBlank(reg) || StringUtils.isBlank(reg2)){
            return;
        }
        Object value = map.get(key);
        if(value instanceof String){
            value = ((String)value).replaceAll(reg, reg2);
            map.put(key, value);
        }
    }



    /**
     * 回传过滤
     * 回传对象时转换为Res后缀的对象，找不到对应的Res类时则返回原对象。
     * @param object
     * @return
     */
    public Object filterRes(Object object, HttpServletRequest request, Boolean noSensitivity){
        if(object == null){
            return null;
        }
        Map<String, Object> map = ObjectUtil.objectToMap(object);
        Role role = getCacheRole(request);
        User agent = getCacheAgent(request);

        filterAttr(map, "password", "\\S*", "*");

        if(!noSensitivity){
            if(role.getSensitivity().compareTo(Sensitivity.LOW) > -1){
                filterAttr(map, "idcard", "^(\\w{6})\\w+", "$1*********");
                filterAttr(map, "cardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "debitCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "sourceCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "targetCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
//                filterAttr(map, "phone", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "userName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "referralUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "agentUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "sourceUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "targetUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "sourceAgentUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
//                filterAttr(map, "targetAgentUserName", "^(\\w{3})\\w+(\\w{4})$", "$1****$2");
            }

            if(role.getSensitivity().compareTo(Sensitivity.NORMAL) > -1){
                filterAttr(map, "idcard", "^(\\w{6})\\w+", "$1*********");
                filterAttr(map, "cardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "debitCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "sourceCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
                filterAttr(map, "targetCardNo", "^(\\w{4})\\w+(\\w{4})$", "$1 **** **** $2");
//                filterAttr(map, "name", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "realName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "userRealName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "referralName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "agentName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "sourceUserRealName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "targetUserRealName", "^(\\S)\\S?(\\S)*", "$1*$2");
//                filterAttr(map, "targetAgentName", "^(\\S)\\S?(\\S)*", "$1*$2");
            }

            if(role.getSensitivity().compareTo(Sensitivity.HIGH) > -1){

            }

            if(role.getSensitivity().compareTo(Sensitivity.MOST) > -1){

            }
        }

        Class resClass = getResClass(object.getClass(), agent == null ? "admin": "agent");
        if(null != resClass){
            return ObjectUtil.convert(map, resClass);
        }
        return ObjectUtil.convert(map, object.getClass());
    }

    public List filterRes(List list, HttpServletRequest request, Boolean noSensitivity){
        if(null == list){
            return list;
        }
        for(int i=0; i<list.size(); i++){
            list.set(i, filterRes(list.get(i), request, noSensitivity));
        }
        return list;
    }

    public Page filterRes(Page page, HttpServletRequest request, Boolean noSensitivity){
        if(null == page){
            return page;
        }
        page.setData(filterRes(page.getData(), request, noSensitivity));
        return page;
    }


    protected Class getResClass(Class modelClass, String packageName){
        try {
            String resClassName = modelClass.getName().replaceAll("model\\.(\\w+)", "res."+packageName+".$1Res");
            return  Class.forName(resClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 添加连表字段
     * @param queryBuilder
     */
    protected void addJoinColumns(QueryBuilder queryBuilder){
        String simpleName = queryBuilder.getBoClass().getSimpleName();
        Class boClass = queryBuilder.getBoClass();
        // 补充代理商信息
        boolean hasAgentId = ObjectUtil.getField(boClass, "agentId") != null;
        if(hasAgentId){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("a");
            queryBuilder1.setEQQueryCond("a.userId", simpleName + ".agentId");
            queryBuilder1.addColumns("a.realName as agentName");
            queryBuilder1.addColumns("a.userName as agentUserName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充用户信息
        boolean hasUserId = ObjectUtil.getField(boClass, "userId") != null;
        if(hasUserId && !boClass.equals(User.class)){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("b");
            queryBuilder1.setEQQueryCond("b.userId", simpleName + ".userId");
            queryBuilder1.addColumns("b.realName as userRealName");
            queryBuilder1.addColumns("b.userName as userName");
            queryBuilder1.addColumns("b.idcard as idcard");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充推荐人信息
        boolean hasReferenceId = ObjectUtil.getField(boClass, "referenceId") != null;
        if(hasReferenceId){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("c");
            queryBuilder1.setEQQueryCond("c.userId", simpleName + ".referenceId");
            queryBuilder1.addColumns("c.realName as referenceName");
            queryBuilder1.addColumns("c.userName as referenceUserName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充卡片信息
        boolean hasCardId = ObjectUtil.getField(boClass, "cardId") != null;
        if(hasCardId && !boClass.equals(Card.class)){
            QueryBuilder queryBuilder1 = new QueryBuilder(Card.class);
            queryBuilder1.setAliasName("d");
            queryBuilder1.setEQQueryCond("d.cardId", simpleName + ".cardId");
            queryBuilder1.addColumns("d.cardNo as cardNo");
            queryBuilder1.addColumns("d.cardBank as cardBank");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充来源卡片信息
        boolean hasSourceCardId = ObjectUtil.getField(boClass, "sourceCardId") != null;
        if(hasSourceCardId){
            QueryBuilder queryBuilder1 = new QueryBuilder(Card.class);
            queryBuilder1.setAliasName("e");
            queryBuilder1.setEQQueryCond("e.cardId", simpleName + ".sourceCardId");
            queryBuilder1.addColumns("e.cardNo as sourceCardNo");
            queryBuilder1.addColumns("e.cardBank as sourceCardBank");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充目标卡片信息
        boolean hasTargetCardId = ObjectUtil.getField(boClass, "targetCardId") != null;
        if(hasTargetCardId){
            QueryBuilder queryBuilder1 = new QueryBuilder(Card.class);
            queryBuilder1.setAliasName("f");
            queryBuilder1.setEQQueryCond("f.cardId", simpleName + ".targetCardId");
            queryBuilder1.addColumns("f.cardNo as targetCardNo");
            queryBuilder1.addColumns("f.cardBank as targetCardBank");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充来源用户信息
        boolean hasSourceUserId = ObjectUtil.getField(boClass, "sourceUserId") != null;
        if(hasSourceUserId){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("g");
            queryBuilder1.setEQQueryCond("g.userId", simpleName + ".sourceUserId");
            queryBuilder1.addColumns("g.realName as sourceUserRealName");
            queryBuilder1.addColumns("g.userName as sourceUserName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充目标用户信息
        boolean hasTargetUserId = ObjectUtil.getField(boClass, "targetUserId") != null;
        if(hasTargetUserId){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("h");
            queryBuilder1.setEQQueryCond("h.userId", simpleName + ".targetUserId");
            queryBuilder1.addColumns("h.realName as targetUserRealName");
            queryBuilder1.addColumns("h.userName as targetUserName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充目标代理商信息
        boolean hasTargetAgentId = ObjectUtil.getField(boClass, "targetAgentId") != null;
        if(hasTargetAgentId){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("i");
            queryBuilder1.setEQQueryCond("i.userId", simpleName + ".targetAgentId");
            queryBuilder1.addColumns("i.realName as targetAgentName");
            queryBuilder1.addColumns("i.userName as targetAgentUserName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充目标用户储蓄卡信息
        boolean hasTargetDebitCardId = ObjectUtil.getField(boClass, "debitCardId") != null;
        if(hasTargetDebitCardId){
            QueryBuilder queryBuilder1 = new QueryBuilder(Card.class);
            queryBuilder1.setAliasName("j");
            queryBuilder1.setEQQueryCond("j.cardId", simpleName + ".debitCardId");
            queryBuilder1.addColumns("j.cardNo as debitCardNo");
            queryBuilder1.addColumns("j.cardBank as debitCardBank");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充管理员信息
        boolean hasAdminId = ObjectUtil.getField(boClass, "adminId") != null;
        if(hasAdminId){
            QueryBuilder queryBuilder1 = new QueryBuilder(Admin.class);
            queryBuilder1.setAliasName("k");
            queryBuilder1.setEQQueryCond("k.adminId", simpleName + ".adminId");
            queryBuilder1.addColumns("k.name as adminName");
            queryBuilder.leftJoin(queryBuilder1);
        }

        // 补充交易信息
        boolean hasOrderId = ObjectUtil.getField(boClass, "orderId") != null;
        if(hasOrderId){
            QueryBuilder queryBuilder1 = new QueryBuilder(Order.class);
            queryBuilder1.setAliasName("l");
            queryBuilder1.setEQQueryCond("l.orderId", simpleName + ".orderId");
            queryBuilder1.addColumns("l.orderNo as orderNo");
            queryBuilder1.addColumns("l.money as orderMoney");
            queryBuilder.leftJoin(queryBuilder1);
        }
    }
}
