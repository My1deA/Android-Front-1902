package com.example.projectthree.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectthree.R;
import com.example.projectthree.UploadActivity;
import com.example.projectthree.util.PermissionUtil;

public class AppMessageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG="MessageFragment";
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象
    private String[] Permissionrequest={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_message,container,false);

        mView.findViewById(R.id.btn_upload).setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_upload){
//             Intent intent=new Intent(mContext, UploadActivity.class);
//             startActivity(intent);
            if (PermissionUtil.checkMultiPermission((Activity) mContext, Permissionrequest,R.id.btn_upload % 4096)){
                PermissionUtil.goActivity(mContext, UploadActivity.class);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode==R.id.btn_upload%4096){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionUtil.goActivity(mContext, UploadActivity.class);
            } else {
                Toast.makeText(mContext, "需要允许SD卡权限才能上传文件噢", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
