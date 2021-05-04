package com.example.freshdiet.calorie;

import java.io.Serializable;

public class FoodListItem implements Serializable {

    private String titleStr ;
    private String descStr ;


    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}

