package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum DocType {

    /**
     *
     */
    NORMAL("普通文章", 0),
    NOTICE("公告", 1),
    ICON("首页图标", 2),
    CREDIT("首页信用卡", 3),
    CAROUSEL_FIGURE("首页轮播图", 4),
    HOT_CARD("热门银行卡", 5),
    AGREEMENT("用户注册协议", 6),
    DYNAMIC_NEWS("首页新闻动态", 7),
    SHAREBG("分享页背景图", 8),
    LIFE_CHOICE("生活精选", 9),
    LIFE_SPEEDY("生活便利", 10);


    private String desc;
    private int code;

    DocType(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final DocType getType(int code){
        for(DocType type: DocType.values()){
            if(type.getCode() == code){
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "desc='" + desc + '\'' +
                ", code=" + code +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(DocType.NORMAL.name());
    }
}
