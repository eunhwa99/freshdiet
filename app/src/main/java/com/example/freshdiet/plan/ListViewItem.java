package com.example.freshdiet.plan;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String namestr;

    public void setIcon(Drawable icon){
        iconDrawable=icon;
    }
    public void setName(String name){
        namestr=name;
    }

    public String getName(){
        return this.namestr;
    }
    public Drawable getIcon(){
        return this.iconDrawable;
    }
}
