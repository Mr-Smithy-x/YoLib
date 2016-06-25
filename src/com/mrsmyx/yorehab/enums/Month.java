package com.mrsmyx.yorehab.enums;

/**
 * Created by cj on 1/24/16.
 */
public enum Month {
    Nan(0), Jan(1), Feb(2), Apr(3), Mar(4), May(5), June(6), July(7), Aug(8), Sept(9), Oct(10), Nov(11), Dec(12);

    public int getDate() {
        return dateNum;
    }

    int dateNum;
    Month(int date){
        this.dateNum = date;
    }
}
