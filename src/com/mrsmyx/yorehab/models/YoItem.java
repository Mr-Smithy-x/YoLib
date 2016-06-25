package com.mrsmyx.yorehab.models;

import com.mrsmyx.yorehab.enums.Month;

import java.io.Serializable;

/**
 * Created by cj on 1/24/16.
 */
public class YoItem implements Serializable{

    private String item_name, item_price, item_img, item_rel, item_base_url;
    private int item_year;
    private Month item_month;

    private String item_cat;

    public String getItem_cat_base_url() {
        return item_cat_base_url;
    }

    public YoItem setItem_cat_base_url(String item_cat_base_url) {
        this.item_cat_base_url = item_cat_base_url;
        return this;
    }

    private String item_cat_base_url;

    public String getItem_cat() {
        return item_cat;
    }

    public YoItem setItem_cat(String item_cat) {
        this.item_cat = item_cat;
        return this;
    }
    public int getItem_year() {
        return item_year;
    }

    public YoItem setItem_year(int item_year) {
        this.item_year = item_year;
        return this;
    }

    public Month getItem_month() {
        return item_month;
    }

    public YoItem setItem_month(Month item_month) {
        this.item_month = item_month;
        return this;
    }

    public static YoItem Builder(){
        return new YoItem();
    }

    protected YoItem(){}

    public String getItem_base_url() {
        return item_base_url;
    }

    public YoItem setItem_base_url(String item_base_url) {
        this.item_base_url = item_base_url;
        return this;
    }

    public String getItem_rel() {
        return item_rel;
    }

    public YoItem setItem_rel(String item_rel) {
        this.item_rel = item_rel;
        return this;
    }

    public String getItem_img() {
        return item_img;
    }

    public YoItem setItem_img(String item_img) {
        this.item_img = item_img;
        return this;
    }

    public String getItem_price() {
        return item_price;
    }

    public YoItem setItem_price(String item_price) {
        this.item_price = item_price;
        return this;
    }

    public String getItem_name() {
        return item_name;
    }

    public YoItem setItem_name(String item_name) {
        this.item_name = item_name;
        return this;
    }




}
