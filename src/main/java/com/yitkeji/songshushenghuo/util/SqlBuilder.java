package com.yitkeji.songshushenghuo.util;

import com.yitkeji.songshushenghuo.vo.model.Order;

import java.util.List;
import java.util.Map;

public class SqlBuilder extends SqlUtil {

    public static String findList(QueryBuilder queryBuilder){
        return queryBuilder.getSelectSql();
    }

    public static String findListByPage(QueryBuilder queryBuilder){
        Page page = ObjectUtil.mapToObject(queryBuilder, Page.class);
        String sql = queryBuilder.getSelectSql();
        if(null != page){
            sql += String.format(" limit %d, %d", page.getNpage() * page.getPageSize(), page.getPageSize());
        }
        return sql;
    }


    public static String findOne(QueryBuilder queryBuilder){
        return queryBuilder.getSelectSql();
    }


    public static String count(QueryBuilder queryBuilder){
        return queryBuilder.getCountSql();
    }


    public static String sum(QueryBuilder queryBuilder) {
        return queryBuilder.getSelectSql();
    }

    /**
     * 插入数据
     * 只检索非空字段
     * @param map
     * @return
     */
    public static String insert(Map<String, Object> map){
        String table = (String)map.get("__table");
        StringBuffer columns = new StringBuffer();
        StringBuffer datas = new StringBuffer();
        for (String key: map.keySet()){
            if(key.startsWith("__") || map.get(key) == null){
                continue;
            }
            columns.append(formatColumn(key) + ",");
            datas.append("#{"+key+"},");
        }
        columns.setLength(columns.length() - 1);
        datas.setLength(datas.length() - 1);
        return String.format("insert into %s(%s) values(%s)", table, columns.toString(), datas.toString());
    }


    public static String insertList(Map<String, Object> map){
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("__list");
        StringBuffer stringBuffer = new StringBuffer();
        if(list.size() > 0){
            stringBuffer.append(insert(list.get(0)));
        }
        for (int i=1; i<list.size(); i++){
            Map<String, Object> oMap = list.get(i);
            StringBuffer datas = new StringBuffer();
            for (String key: oMap.keySet()){
                if(key.startsWith("__") || oMap.get(key) == null){
                    continue;
                }
                datas.append("#{"+key+ i +"},");
            }
            datas.setLength(datas.length() - 1);
            stringBuffer.append(",(" + datas.toString() + ")");
        }
        return stringBuffer.toString();
    }


    /**
     * 根据主键更新所有字段，
     * @param map
     * @return
     */
    public static String updateByPrimaryKey(Map<String, Object> map){

        String primaryKey = (String)map.get("__id");
        String table = (String)map.get("__table");
        String sql = "update " + table + " set";
        String whereSql = String.format(" where `%s` = #{%s}", primaryKey, ObjectUtil.underlineToCamel(primaryKey));

        StringBuffer setSql = new StringBuffer();
        for(String key: map.keySet()){
            if(key.startsWith("__")){
                continue;
            }
            setSql.append(String.format(" %s = #{%s},", formatColumn(key), key));
        }
        if(setSql.length() < 1){
            return null;
        }
        setSql.setLength(setSql.length() - 1);
        return sql + setSql.toString() + whereSql;
    }


    public static String update(QueryBuilder queryBuilder){
        String sql = "update " + queryBuilder.getTable() + " set";

        StringBuffer setSql = new StringBuffer();
        for(String key: queryBuilder.keySet()){
            if(!key.startsWith("$")){
                continue;
            }
            setSql.append(String.format(" %s = #{%s},", formatColumn(key.substring(1)), key));
        }
        if(setSql.length() < 1){
            return null;
        }
        setSql.setLength(setSql.length() - 1);
        return sql + setSql.toString() + queryBuilder.getCondSql();
    }

    public static void main(String[] args) {
        QueryBuilder queryBuilder = new QueryBuilder(Order.class);
        queryBuilder.putAll(ObjectUtil.objectToMap(new Page()));
        System.out.println(SqlBuilder.findListByPage(queryBuilder));
    }
}
