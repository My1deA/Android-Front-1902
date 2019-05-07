package com.example.projectthree.util;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    private static final String Tag="DateUtil";


    public static String getNowDateTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getNowDateTime(String format){
        String tempFormat = format;
        if (TextUtils.isEmpty(tempFormat)) {
            tempFormat = "yyyy-MM-dd HH:mm:ss";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(tempFormat);
        return sdf.format(new Date());
    }

    public static String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

}
