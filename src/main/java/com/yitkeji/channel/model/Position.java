package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
public class Position {
    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;

    public String getProvince() {
        return "[]".equals(province) ? null: province;
    }

    public String getCity() {
        return "[]".equals(city) ? null: city;
    }

    public String getAdcode() {
        return "[]".equals(adcode) ? null: adcode;
    }

    public String getRectangle() {
        return "[]".equals(rectangle) ? null: rectangle;
    }
}
