package com.example.projectthree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.projectthree.R;
import com.example.projectthree.adapter.NineGridAdapter;
import com.example.projectthree.bean.NineGridItem;
import com.example.projectthree.widget.SpacesItemDecoration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class AppFindFragment3 extends Fragment implements View.OnClickListener {

    private final static String TAG="FindFragment3";
    protected View mView;
    protected Context mContext;

    private final static String Url="http://172.16.86.194:8080/MyWebTest/queryServlet";
//    private final static String Url="http://192.168.137.1:8080/MyWebTest/queryServlet";

    private EditText et_username;
    private RecyclerView rv_dynamic; //循环视图
    private RecyclerView.LayoutManager rv_manager;//布局管理器
    private NineGridAdapter adapter;//适配器
    private ArrayList<NineGridItem> mList=new ArrayList<NineGridItem>();
    private int query=1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_find3,container,false);
        rv_dynamic=mView.findViewById(R.id.rv_dynamic);
        mView.findViewById(R.id.btn_query).setOnClickListener(this);
        et_username=mView.findViewById(R.id.et_username);
        return  mView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_query){
            GetfromMysql();
        }
    }

    private void GetfromMysql(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //客户端
                    HttpClient httpClient=new DefaultHttpClient();
                    //post方式
                    HttpPost httpPost=new HttpPost(Url);
                    //传输数据
                    List<NameValuePair> list=new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("username",et_username.getText().toString().trim()));

                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"utf-8");
                    httpPost.setEntity(entity);

                    //回应
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){

                        HttpEntity entity1=httpResponse.getEntity();
                        String jstr= EntityUtils.toString(entity1,"utf-8");
                        Message message=new Message();
                        message.what=query;
                        message.obj=jstr;
                        messageHander.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler messageHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==query){
                initPublicArray((String) msg.obj);//译码
                initRecyclerDynamic(); // 初始化动态线性布局的循环视图
            }
        }

    };

    private void initRecyclerDynamic() {
        rv_manager = new LinearLayoutManager(mContext);
        rv_dynamic.setLayoutManager(rv_manager);
        adapter = new NineGridAdapter(mContext);
        adapter.setList(mList);
        rv_dynamic.setAdapter(adapter);

        rv_dynamic.setItemAnimator(new DefaultItemAnimator());
        // 给rv_dynamic添加列表项之间的空白装饰
        rv_dynamic.addItemDecoration(new SpacesItemDecoration(1));

    }

    private void initPublicArray(String jstr) {
        //ali size() org length()
        JSONArray array= JSON.parseArray(jstr);
        for(int i=0;i<array.size();i++){

            JSONObject jsonObject=array.getJSONObject(i);
            String uid=jsonObject.getString("uid");
            String time=jsonObject.getString("time");
            String url=jsonObject.getString("url");
            String text=jsonObject.getString("text");
            String location=jsonObject.getString("location");
            String type=jsonObject.getString("type");
            Log.e(TAG,uid+"  "+url);
            Log.w(TAG,uid+"  "+url);
//            url="http://172.16.86.194:8080/upload"+url;


            String []urls=url.split("#");
            List<String> urlList = new ArrayList<String>();
            for(int j=0;j<urls.length;j++){
                urlList.add("http://172.16.86.194:8080/upload"+urls[j]);
            }

            NineGridItem item=new NineGridItem(uid,time,urlList,text,location,type);
            mList.add(item);


//            Picinfo picinfo=new Picinfo(uid,text,url,location,false);
//            PublicArray.add(picinfo);
//            AllArray.add(picinfo);
        }

    }

}
