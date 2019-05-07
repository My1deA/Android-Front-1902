package com.example.projectthree.bean;

import java.util.ArrayList;

public class Picinfo {
    public String uid;
    public String desc;
    public String url;
    public String location;
    public boolean bPressed;
    public int id;
    private static int seq=0;

    public Picinfo(String uid, String desc, String location, boolean bPressed) {
        this.uid = uid;
        this.desc = desc;
        this.url = url;
        this.location = location;
        this.bPressed = false;
        this.id=seq;
        this.seq++;

    }



    public Picinfo(String uid, String desc, String url, String location,boolean bPressed) {
        this.uid = uid;
        this.desc = desc;
        this.url = url;
        this.location = location;
        this.bPressed = bPressed;
        this.id=seq;
        this.seq++;

    }

    public Picinfo(String uid, String desc, String location) {
        this.uid = uid;
        this.desc = desc;
        this.url = "http://192.168.1.102:8080/upload/1.jpg";
        this.url="https://b-ssl.duitang.com/uploads/item/201901/28/20190128152613_hkhni.jpg";
        this.location = location;
        this.bPressed = false;
        this.id=seq;
        this.seq++;
    }

    public static ArrayList<Picinfo> getDefaultList(){
        ArrayList<Picinfo> arrayList=new ArrayList<Picinfo>();
        for(int i=0;i<uidArray.length;i++){
            arrayList.add(new Picinfo(uidArray[i],descArray[i],loactionArray[i]));
        }
        return arrayList;
    }


    public static String[] uidArray={"123","aino","伊尔"};
    public static String[] descArray={"太好吃了吧","真甜","朋友圈打卡"};
    public static String[] loactionArray={"广州","上海","北京"};

}
