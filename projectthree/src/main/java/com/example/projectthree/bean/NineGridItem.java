package com.example.projectthree.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NineGridItem implements Serializable {
    private static final long serialVersionUID = 2189052605715370758L;
    public boolean isShowAll = false;

    public String uid;
    public String time;
    public List<String> urlList = new ArrayList<>();
    public String text;
    public String location;
    public String type;
    public boolean bPressed;
    public int id;
    private static int seq=0;

    public NineGridItem(String uid, String time, List<String> urlList, String text, String location, String type) {
        this.id=seq;
        this.seq++;
        bPressed=false;

        this.uid = uid;
        this.time = time;
        this.urlList = urlList;
        this.text = text;
        this.location = location;
        this.type = type;

    }

    public NineGridItem(String uid, String time, List<String> urlList, String text, String location, String type, boolean bPressed) {
        this.id=seq;
        this.seq++;
        bPressed=false;

        this.uid = uid;
        this.time = time;
        this.urlList = urlList;
        this.text = text;
        this.location = location;
        this.type = type;
        this.bPressed = bPressed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
