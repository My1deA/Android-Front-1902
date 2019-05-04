package com.example.projectthree.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.projectthree.R;
//compile 'com.github.bumptech.glide:glide:4.0.0'
public class AppHomeFragment extends Fragment {
    private static final String TAG="HomeFragment";
    protected View mView;
    protected Context mContext;

    private ImageView iv_icon1;
    private String url="http://172.16.86.194:8080/MyWebTest/123/service_2.jpg";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_home,container,false);
        iv_icon1=mView.findViewById(R.id.iv_icon1);
//        url="https://pic.cnblogs.com/avatar/1142647/20170416093225.png";
        url="https://b-ssl.duitang.com/uploads/item/201901/28/20190128152613_hkhni.jpg";
        Glide.with(mContext).load(url).into(iv_icon1);
        return mView;
    }
}
