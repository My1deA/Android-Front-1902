package com.example.projectthree;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.util.DateUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class UploadVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG="UploadVideoActivity";
    private EditText et_text;
    private TextView tv_cityloc;
    private FrameLayout fl_video;
    private ImageView iv_video;
    private ImageView iv_pause;
    private ProgressBar progressBar;
    private TextView textShow;

//    private static String mBaseUrl="http://172.16.86.194:8080/MyWebTest/uploadServlet";
    private static String mBaseUrl="http://192.168.137.1:8080/MyWebTest/uploadServlet";

    private String type="视频";
    private String username;

    private String path;


    private ArrayList<String> arrayList=new ArrayList<String>();
    private ArrayList<String> fileName=new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedioupload);
        et_text=findViewById(R.id.et_text);
        tv_cityloc=findViewById(R.id.tv_cityloc);
        fl_video=findViewById(R.id.fl_video);
        iv_video=findViewById(R.id.iv_video);
        iv_pause=findViewById(R.id.iv_pause);
        progressBar=findViewById(R.id.progressBar);
        textShow=findViewById(R.id.textShow);

        findViewById(R.id.btn_addvideo).setOnClickListener(this);
        findViewById(R.id.btn_confrim).setOnClickListener(this);
        iv_pause.setOnClickListener(this);
        iv_video.setOnClickListener(this);

        username=MainApplication.getInstance().UserinfoMap.get("username");
        Bundle bundle=getIntent().getExtras();
        tv_cityloc.setText(bundle.getString("city"));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_addvideo){

            FileChooseUtil fileChooseUtil=new FileChooseUtil();
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

        }else if(v.getId()==R.id.iv_pause||v.getId()==R.id.iv_video){
            Intent intent=new Intent(this,MoviePlayActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("path",path);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    public void multiFileUpload() {
        //String mBaseUrl="http://172.16.86.49:8001/upload";
        //mBaseUrl="http://172.16.86.194:8080/MyWebTest/uploadServlet";

        String Allurl="";
        for(int i=0;i<fileName.size();i++)
        {
            Allurl+="/"+username+"/"+fileName.get(i)+"#";
        }

        Map<String, String> params = new HashMap<>();
        params.put("uid",username);
        params.put("time", DateUtil.getNowDateTime());
        params.put("location",tv_cityloc.getText().toString().trim());
        params.put("type",type);
        params.put("text",et_text.getText().toString().trim());
//        params.put("Allurl",Allurl);

        String url = mBaseUrl;

        //Log.e(TAG,Calendar.getInstance()+name);
        for(int i=0;i<arrayList.size();i++)
        {
            String s=arrayList.get(i);
            String name=s.substring(s.lastIndexOf("/")+1);
            File file=new File(s);
            if (!file.exists()||!file.exists())
            {
                return;
            }

            if(i==(arrayList.size()-1)){
                params.put("Allurl",Allurl);
            }

            OkHttpUtils.post()//
                    .addFile("mFile", fileName.get(i), file)//
//                .addFile("mFile", "2.txt", file2)//
                    .url(url)
                    .params(params)//
                    .build()//
                    .execute(new UploadVideoActivity.MyStringCallback());
        }

    }

    public class MyStringCallback extends StringCallback {
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
            textShow.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
            textShow.setText("onResponse:" + response);

            switch (id)
            {
                case 100:
                    Toast.makeText(UploadVideoActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(UploadVideoActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            progressBar.setProgress((int) (100 * progress));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FileChooseUtil fileChooseUtil = new FileChooseUtil();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                String name = path.substring(path.lastIndexOf("/") + 1);
                fileName.add(name);
                arrayList.add(path);
                textShow.setText("");
                for (String s : arrayList) {
                    textShow.append(s + "\n");
                }
//                textShow.setText(path);
                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = fileChooseUtil.getPath(this, uri);
                arrayList.add(path);
                String name = path.substring(path.lastIndexOf("/") + 1);
                fileName.add(name);
                textShow.setText("");
                for (String s : arrayList) {
                    textShow.append(s + "\n");
                }

                getImage();
                fl_video.setVisibility(View.VISIBLE);

//                textShow.setText(path);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                arrayList.add(path);

                String name = path.substring(path.lastIndexOf("/") + 1);
                fileName.add(name);
                textShow.setText("");
                for (String s : arrayList) {
                    textShow.append(s + "\n");
                }
//                textShow.setText(path);
                Toast.makeText(UploadVideoActivity.this, path + "222222", Toast.LENGTH_SHORT).show();
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

    private void getImage() {
        FFmpegMediaMetadataRetriever retriever = new  FFmpegMediaMetadataRetriever();
        try {
//                        retriever.setDataSource("/storage/emulated/0/test.mp4"); //file's path
//            retriever.setDataSource("http://xyj.xueyijia.com.cn/2018/7/14/%E6%89%93%E9%A2%86%E5%B8%A6%E7%9A%84%E7%8B%90%E7%8B%B8%E5%B0%BC%E5%85%8B_2018_08_14_19_02_52.mp4"); //file's path
            retriever.setDataSource(arrayList.get(0));
            Bitmap bitmap = retriever.getFrameAtTime(100000,FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC );  //这个时间就是第一秒的
//            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//            Glide.with(MainActivity.this).load(drawable).into(iv_image);
            iv_video.setImageBitmap(bitmap);
//            iv_pause.setImageDrawable(R.drawable.pause);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            retriever.release();
        }
    }

}
