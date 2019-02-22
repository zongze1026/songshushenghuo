package com.yitkeji.songshushenghuo.util;

import com.yitkeji.songshushenghuo.vo.model.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 查询工具类
 */
public class QueryBuilder extends SqlUtil{

    private Class boClass;
    private StringBuffer condSql = new StringBuffer();
    private String selectSql = ""; // 查询用SQL
    private String countSql = ""; // 统计总数用SQL
    private StringBuffer columns = new StringBuffer();
    private String group = "";
    private String order = "";
    private StringBuffer joins = new StringBuffer();
    private List<QueryBuilder> joinBuilders = new ArrayList<>();
    private String table;
    @Setter
    @Getter
    private String aliasName;
    private Map<String, Class> unionTableMap = new HashMap<>();
    private Map<String, Integer> mc = new HashMap<>(); // 每个方法调用的次数集合

    public QueryBuilder(Class boClass) {
        this.boClass = boClass;
        try {
            this.table = getTable(boClass.newInstance());
            this.aliasName = this.table;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入等于条件
     * @param attribute
     * @param object
     */
    public void setEQQueryCond(String attribute, Object object){
        this.appendSql(attribute, "=", object);
    }

    /**
     * 加入不等于条件
     * @param attribute
     * @param object
     */
    public void setNotEQQueryCond(String attribute, Object object){
        this.appendSql(attribute, "<>", object);
    }

    /**
     * 加入小于等于条件
     * @param attribute
     * @param obj
     */
    public void setLessThanOrEqQueryCond(String attribute, Object obj) {
        this.appendSql(attribute, "<=", obj);
    }

    /**
     * 加入大于等于条件
     * @param attribute
     * @param obj
     */
    public void setGreatThanOrEqQueryCond(String attribute, Object obj) {
        this.appendSql(attribute, ">=", obj);
    }

    /**
     * 加入null条件
     * @param attribute
     */
    public void setIsNullCond(String attribute) {
        this.appendSql(attribute, "is null", null);
    }

    /**
     * 加入非null条件
     * @param attribute
     */
    public void setIsNotNullCond(String attribute) {
        this.appendSql(attribute, "is not null", null);
    }

    /**
     * 加入in条件
     * @param attribute
     * @param values
     */
    public void setInQueryCond(String attribute, Object... values) {
        int c = this.getMc("setInQueryCond");
        StringBuffer inStr = new StringBuffer();
        int i=0;
        for(Object value: values){
            String tempKey = "in" + c + "t" + i;
            inStr.append("#{"+tempKey+"},");
            this.put(tempKey, value);
            i += 1;
        }
        if(inStr.length() > 0){
            inStr.setLength(inStr.length() - 1);
        }
        condSql.append(" and "+mFormatColumn(attribute)+" in ("+inStr.toString()+")");
    }

    /**
     * 加入in条件
     * @param attribute
     * @param list
     */
    public void setInQueryCondBy(String attribute, List<String> list) {
        int c = this.getMc("setInQueryCond");
        StringBuffer inStr = new StringBuffer();
        int i=0;
        for(String userId: list){
            inStr.append("\""+userId+"\"");
            inStr.append(",");
        }
        if(inStr.length() > 0){
            inStr.setLength(inStr.length() - 1);
        }
        condSql.append(" and "+mFormatColumn(attribute)+" in ("+inStr.toString()+")");
    }

    /**
     * 加入not in条件
     * @param attribute
     * @param values
     */
    public void setNotInQueryCond(String attribute, Object... values) {
        int c = this.getMc("setNotInQueryCond");
        StringBuffer inStr = new StringBuffer();
        int i=0;
        for(Object value: values){
            String tempKey = "notIn" + c + "t" + i;
            inStr.append("#{"+tempKey+"},");
            this.put(tempKey, value);
            i += 1;
        }
        if(inStr.length() > 0){
            inStr.setLength(inStr.length() - 1);
        }
        condSql.append(" and "+mFormatColumn(attribute)+" not in ("+inStr.toString()+")");
    }


    private void addJoin(String action, QueryBuilder queryBuilder){
        if(StringUtils.isNotBlank(queryBuilder.getOnSql())){
            joins.append(" " + action + " join `" + queryBuilder.getTable() + "`");
            if(!queryBuilder.getAliasName().equals(queryBuilder.getTable())){
                joins.append(" as `"+queryBuilder.getAliasName()+"`");
            }
            joins.append(queryBuilder.getOnSql());
            joinBuilders.add(queryBuilder);
            this.putAll(queryBuilder);
        }
    }

    public void join(QueryBuilder queryBuilder){
        addJoin("", queryBuilder);
    }
    public void leftJoin(QueryBuilder queryBuilder){
        addJoin("left", queryBuilder);
    }
    public void rightJoin(QueryBuilder queryBuilder){
        addJoin("right", queryBuilder);
    }
    public void innerJoin(QueryBuilder queryBuilder){
        addJoin("inner", queryBuilder);
    }
    public void fullJoin(QueryBuilder queryBuilder){
        addJoin("full", queryBuilder);
    }


    /**
     * 加入between条件
     * @param attribute
     * @param minValue
     * @param maxValue
     */
    public void setBetweenQueryCond(String attribute, Object minValue, Object maxValue){
        int c = this.getMc("setBetweenQueryCond");
        condSql.append(String.format(" and %s between #{minValue"+c+"} and #{maxValue"+c+"}", mFormatColumn(attribute)));
        this.put("minValue"+c, minValue);
        this.put("maxValue"+c, maxValue);
    }


    private void appendSql(String pAttr, String operator, Object value){
        String fAttr = mFormatColumn(pAttr);
        pAttr = pAttr.replaceAll("\\W", "_");
        if(value == null){
            condSql.append(String.format(" and %s %s",fAttr, operator));
        }else{
            if(isUnionColumn(value)){
                String vColumn = mFormatColumn((String)value);
                condSql.append(String.format(" and %s %s %s", fAttr, operator, vColumn));
            }else{
                condSql.append(String.format(" and %s %s #{%s}", fAttr, operator, pAttr));
                this.put(pAttr, value);
            }
        }
    }


    /**
     * 添加查询字段，支持count，sum，as
     * @param columns
     */
    public void addColumns(String... columns){
        if(this.columns.length() > 0 && columns.length > 0){
            this.columns.append(", ");
        }
        for(int i=0; i<columns.length; i++){
            String column = columns[i];
            if(column.indexOf(" as ")!= -1 || column.indexOf(" AS ")!= -1){
                String[] tmps = column.split("\\s+");
                tmps[0] = mFormatColumn(tmps[0]);
                column = StringUtils.join(tmps, " ");
            }else{
                column = mFormatColumn(column);
            }
            this.columns.append(column);
            if(i<columns.length - 1){
                this.columns.append(", ");
            }
        }
    }
    public void setGroup(String... attributes){
        StringBuffer sb = new StringBuffer();
        for(String attr: attributes){
            sb.append(formatColumn(this.aliasName) + "." + formatColumn(attr));
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        group += sb.toString();
    }
    public void addOrder(String attribute, boolean isAsc){
        String fc = formatColumn(this.aliasName) + "." + formatColumn(attribute);
        if(!StringUtils.isBlank(order)){
            order += ",";
        }
        order += fc;
        order += (isAsc ? " asc": " desc");
    }


    // 条件
    public String getCondSql() {
        return StringUtils.isBlank(condSql.toString()) ? "": " where 1=1 " + condSql.toString();
    }

    // 条件
    public String getOnSql(){
        if(condSql.length() < 1){
            return "";
        }
        return " on ("+condSql.toString().substring(4)+")";
    }

    public String getColumns(){
        String columnStr;
        if(columns.length() == 0){
            columnStr = formatColumn(this.aliasName) + ".*";
        }else{
            columnStr = columns.toString();
        }
        if(joinBuilders.size() > 0){
            for(QueryBuilder queryBuilder: joinBuilders){
                columnStr += ",";
                columnStr += queryBuilder.getColumns();
            }
        }
        return columnStr;
    }
    public String getPrimaryKey(){
        return getPrimaryKey(getBoClass());
    }
    public String getTable(){
        return getTable(getBoClass());
    }
    private String getFromStr(boolean ignoreJoin){

        // 填入当前表
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("`"+this.table+"`");
        if(!this.aliasName.equals(this.table)){
            stringBuffer.append(" as `"+this.aliasName+"`");
        }

        // 填入连表，剔除join的表
        if(ignoreJoin){
            for(QueryBuilder qb: joinBuilders){
                unionTableMap.remove(qb.getTable());
            }
        }
        for(String str: unionTableMap.keySet()){
            if(str.equals(this.aliasName)){
               continue;
            }
            stringBuffer.append(", `"+str+"`");
        }
        return stringBuffer.toString();
    }


    public String getSelectSql() {
        selectSql = "select "
                + getColumns()
                + " from "
                + getFromStr(true)
                + joins.toString()
                + getCondSql()
                + (StringUtils.isBlank(group) ? "": " group by " + group)
                + (StringUtils.isBlank(order) ? "": " order by " + order);
        return selectSql;
    }
    public String getCountSql(){
        countSql = "select count(1) from "
                + getFromStr(true)
                + joins.toString()
                + getCondSql()
                + (StringUtils.isBlank(group) ? "": " group by " + group);
        return countSql;
    }



    // 字段格式化
    private String mFormatColumn(String column){
        String tableName = this.aliasName;


        // 校验字段合法性
        if((Pattern.matches("^(\\W+)$", column) || Pattern.matches("^(\\d+)$", column)) && !"*".equals(column)){
            return column;
        }


        // count，sum
        if(Pattern.matches("(count|sum)\\([\\w|\\.]+\\)", column)){
            String[] tmp = column.split("\\(|\\)");
            column = tmp[0] + "(" + mFormatColumn(tmp[1]) + ")";
            return column;

            // 连表查询
        }else if(column.indexOf(".")!= -1){
            String[] cs = column.split("\\.");
            try {
                Class tClass = Class.forName("com.yitkeji.songshushenghuo.vo.model." + cs[0]);
                tableName = getTable(tClass);
                unionTableMap.put(tableName, tClass);
                column = cs[1];
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                tableName = cs[0];
                column = cs[1];
            } finally {

            }
        }
        return formatColumn(tableName) + "." + ("*".equals(column) ? column: formatColumn(column));

    }
    private boolean isUnionColumn(Object object){
        if(object instanceof String){
            String obj = (String)object;
            if(obj.indexOf(".") != -1){
                String[] cs = obj.split("\\.");
                try {
                    System.out.println(Class.forName("com.yitkeji.songshushenghuo.vo.model." + cs[0]));
                    Class oclass = Class.forName("com.yitkeji.songshushenghuo.vo.model." + cs[0]);
                    return true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    // 基础方法
    public Class getBoClass() {
        return boClass;
    }
    private void upMc(String key){
        if(null == mc.get(key)){
            mc.put(key, 0);
        }else{
            mc.put(key, mc.get(key) + 1);
        }
    }
    private int getMc(String key){
        upMc(key);
        return null == mc.get(key) ? 0: mc.get(key);
    }

    public static void main(String[] args) {
        /*QueryBuilder queryBuilder = new QueryBuilder(Order.class);
        queryBuilder.setEQQueryCond("userId", "User.userId");
        queryBuilder.setIsNotNullCond("type");
        queryBuilder.setGroup("type");
        queryBuilder.addColumns("type");
        queryBuilder.addColumns("count(orderId) as cOrderId");
        queryBuilder.addColumns("sum(money) as sMoney");
        queryBuilder.addOrder("type", false);*/

        // join
        /*QueryBuilder queryBuilder = new QueryBuilder(Order.class);
        QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
        queryBuilder1.setEQQueryCond("Order.userId", "User.userId");
        queryBuilder1.addColumns("realName");
        queryBuilder.leftJoin(queryBuilder1);*/

        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setAliasName("a");

        QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
        queryBuilder1.setEQQueryCond("a.agentId", "User.userId");
        queryBuilder1.addColumns("User.realName as agentName");
        queryBuilder.leftJoin(queryBuilder1);


        System.out.println(queryBuilder.getSelectSql());
    }
}

