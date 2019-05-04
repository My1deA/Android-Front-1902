package com.example.projectthree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectthree.R;

public class AppMessageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG="MessageFragment";
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_message,container,false);


        return mView;
    }

    @Override
    public void onClick(View v) {

    }
}
