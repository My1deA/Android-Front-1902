package com.example.projectthree;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.util.DateUtil;
import com.example.projectthree.util.FileChooseUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="UploadActivity";
    private Button btn_upload;
    private TextView tv_result;
    private TextView tv_filename;
    private ProgressBar pb_horizontal;

    private static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final int GET=1;
    private static final int Post=2;
    private String path;
    private String fileName;
    private String type;
    private ImageView iv_fileimage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        tv_result=findViewById(R.id.tv_result);
        tv_filename=findViewById(R.id.tv_filename);
        pb_horizontal=findViewById(R.id.pb_horizontal);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_confrim).setOnClickListener(this);
        iv_fileimage=findViewById(R.id.iv_fileImage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_upload){
            checkPermission();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");//选择图片
            intent.setType("audio/*"); //选择音频
            intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
            intent.setType("video/*;image/*");//同时选择视频和图片
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,1);
        }else if(v.getId()==R.id.btn_confrim){
            multiFileUpload();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        FileChooseUtil fileChooseUtil=new FileChooseUtil();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开9-
                path = uri.getPath();
                tv_result.setText(path);

                fileName=path.substring(path.lastIndexOf('/')+1);
                tv_filename.setText(fileName);

                Bitmap bitmap=getLoacalBitmap(path);
                iv_fileimage.setImageBitmap(bitmap);

                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = fileChooseUtil.getPath(this, uri);
                tv_result.setText(path);

                fileName=path.substring(path.lastIndexOf('/')+1);
                tv_filename.setText(fileName);
                Bitmap bitmap=getLoacalBitmap(path);
                iv_fileimage.setImageBitmap(bitmap);

                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                tv_result.setText(path);

                fileName=path.substring(path.lastIndexOf('/')+1);
                tv_filename.setText(fileName);
                Bitmap bitmap=getLoacalBitmap(path);
                iv_fileimage.setImageBitmap(bitmap);

                Toast.makeText(UploadActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    //        if(fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith(".jpeg")){
//            type="picture";
//
//        }else if(fileName.endsWith(".mp4")||fileName.endsWith(".mp3")||fileName.endsWith(".3gp")||
//                fileName.endsWith(".avi")||fileName.endsWith(".aac")||fileName.endsWith(".amr")){
//            type="video";
//        }


    public void multiFileUpload()
    {
        String mBaseUrl="http://172.16.86.194:8080/MyWebTest/uploadServlet";
        path=tv_result.getText().toString().trim();
        File file=new File(path);

        if (!file.exists())
        {
            Toast.makeText(UploadActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("uid", "7");
        params.put("time", DateUtil.getNowDateTime());
        params.put("text", "好好吃啊");
        params.put("location", "广州");
        params.put("type","图片");


        String url=mBaseUrl;



        OkHttpUtils.post()//
                .addFile("mFile", fileName, file)//
                .url(url)
                .params(params)//
                .build()//
                .execute(new MyStringCallback());
    }



    //        File file = new File(Environment.getExternalStorageDirectory(), "1.jpg");
    //        File file2 = new File(Environment.getExternalStorageDirectory(), "2.txt");
    //        String url = mBaseUrl + "user!uploadFile";
    //      .addFile("mFile", "service_2.txt", file2)//
//        Map<String, String> params = new HashMap<>();
//        params.put("username", "张鸿洋");
//        params.put("password", "12");
//        params.put("time", "123");
//        params.put("type","图片");


    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            tv_result.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
            tv_result.setText("onResponse:" + response);

            switch (id)
            {
                case 100:
                    Toast.makeText(UploadActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(UploadActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            pb_horizontal.setProgress((int) (100 * progress));
        }
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

        private void checkPermission() {
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(UploadActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(UploadActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            permission=ActivityCompat.checkSelfPermission(UploadActivity.this,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(UploadActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//
//    private void checkPermission() {
//        // Storage Permissions
//        final int REQUEST_EXTERNAL_STORAGE = 1;
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        try {
//            //检测是否有写的权限
//            int permission = ActivityCompat.checkSelfPermission(UploadActivity.this,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // 没有写的权限，去申请写的权限，会弹出对话框
//                ActivityCompat.requestPermissions(UploadActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
//            permission=ActivityCompat.checkSelfPermission(UploadActivity.this,"android.permission.READ_EXTERNAL_STORAGE");
//            if(permission!=PackageManager.PERMISSION_GRANTED)
//            {
//                ActivityCompat.requestPermissions(UploadActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
