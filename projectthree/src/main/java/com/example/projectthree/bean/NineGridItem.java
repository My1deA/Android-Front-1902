package com.example.projectthree.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NineGridItem implements Serializable {
    private static final long serialVersionUID = 2189052605715370758L;

    public List<String> urlList = new ArrayList<>();

    public boolean isShowAll = true;
}
