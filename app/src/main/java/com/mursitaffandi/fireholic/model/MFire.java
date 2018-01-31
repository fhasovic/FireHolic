package com.mursitaffandi.fireholic.model;

/**
 * Created by mursitaffandi on 1/19/18.
 */

public class MFire {
    String str_title, str_child;

    public MFire(String str_title) {

    }

    public MFire(String str_title, String str_child) {
        this.str_title = str_title;
        this.str_child = str_child;
    }

    public String getStr_title() {
        return str_title;
    }

    public void setStr_title(String str_title) {
        this.str_title = str_title;
    }

    public String getStr_child() {
        return str_child;
    }

    public void setStr_child(String str_child) {
        this.str_child = str_child;
    }
}
