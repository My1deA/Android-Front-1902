package com.example.projectthree.fragment;

import android.app.Activity;
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
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.projectthree.R;
import com.example.projectthree.adapter.LinenarDynamicAdapter;
import com.example.projectthree.bean.Picinfo;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.widget.RecyclerExtras.OnItemClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemDeleteClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemLongClickListener;
import com.example.projectthree.widget.SpacesItemDecoration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AppHomeFragment extends Fragment implements View.OnClickListener,OnItemClickListener, OnItemLongClickListener, OnItemDeleteClickListener,OnRefreshListener{
    private static final String TAG="HomeFragment";
    protected View mView;
    protected Context mContext;
    private SwipeRefreshLayout srl_dynamic;//转圈圈
    private RecyclerView rv_dynamic; //循环视图
    private LinenarDynamicAdapter adapter;//适配器
    private ArrayList<Picinfo> PublicArray=new ArrayList<Picinfo>();//数据链表
    private ArrayList<Picinfo> AllArray=new ArrayList<Picinfo>();//数据链表
    private static int download=1;
    private final static String Url="http://172.16.86.194:8080/MyWebTest/downloadServlet";
//    private TextView tv_result;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_home,container,false);
        srl_dynamic=mView.findViewById(R.id.srl_dynamic);
        srl_dynamic.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        srl_dynamic.setColorSchemeResources(
                R.color.red, R.color.orange, R.color.green, R.color.blue);
        rv_dynamic=mView.findViewById(R.id.rv_dynamic);
//        mView.findViewById(R.id.btn_start).setOnClickListener(this);
//        tv_result=mView.findViewById(R.id.tv_result);
        GetfromMysql();

        return mView;
    }

    private void initRecyclerDynamic() {
        rv_dynamic=mView.findViewById(R.id.rv_dynamic);

        // 创建一个垂直方向的线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false);
        // 设置循环视图的布局管理器
        rv_dynamic.setLayoutManager(manager);

        adapter = new LinenarDynamicAdapter(mContext, PublicArray);
        // 设置线性列表的点击监听器
        adapter.setOnItemClickListener(this);
        // 设置线性列表的长按监听器
        adapter.setOnItemLongClickListener(this);
        // 设置线性列表的删除按钮监听器
        adapter.setOnItemDeleteClickListener(this);
        // 给rv_dynamic设置公众号线性适配器
        rv_dynamic.setAdapter(adapter);
        // 设置rv_dynamic的默认动画效果
        rv_dynamic.setItemAnimator(new DefaultItemAnimator());
        // 给rv_dynamic添加列表项之间的空白装饰
        rv_dynamic.addItemDecoration(new SpacesItemDecoration(1));


    }

    @Override
    public void onRefresh() {
        // 延迟若干秒后启动刷新任务
        mHandler.postDelayed(mRefresh, 2000);
    }
    private Handler mHandler = new Handler(); // 声明一个处理器对象
    // 定义一个刷新任务
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            // 结束下拉刷新布局的刷新动作
            srl_dynamic.setRefreshing(false);
            int position = (int) (Math.random() * 100 % AllArray.size());
            Picinfo old_item = AllArray.get(position);
            Picinfo new_item = new Picinfo(old_item.uid,
                    old_item.desc, old_item.location);
            PublicArray.add(0, new_item);
            // 通知适配器列表在第一项插入数据
            adapter.notifyItemInserted(0);
            // 让循环视图滚动到第一项所在的位置
            rv_dynamic.scrollToPosition(0);
            // 当循环视图的列表项已经占满整个屏幕时，再往顶部添加一条新记录，
            // 感觉屏幕没有发生变化，也没看到插入动画。
            // 此时就要调用scrollToPosition(0)方法，表示滚动到第一条记录。
        }
    };

    @Override
    public void onItemClick(View view, int position) {
        String desc = String.format("您点击了第%d项，标题是%s", position + 1,
                PublicArray.get(position).desc);
        Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Picinfo item=PublicArray.get(position);
        item.bPressed=!item.bPressed;
        PublicArray.set(position,item);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onItemDeleteClick(View view, int position) {
        PublicArray.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onClick(View v) {

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
                    list.add(new BasicNameValuePair("download","download"));
                    list.add(new BasicNameValuePair("count","count"));

                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"utf-8");
                    httpPost.setEntity(entity);

                    //回应
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){

                        HttpEntity entity1=httpResponse.getEntity();
                        String jstr= EntityUtils.toString(entity1,"utf-8");
                        Message message=new Message();
                        message.what=download;
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
            if(msg.what==download){
//                tv_result.setText((String) msg.obj);
                initPublicArray((String) msg.obj);
                initRecyclerDynamic(); // 初始化动态线性布局的循环视图
            }
        }

    };

    private void initPublicArray(String jstr) {
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
            url="http://172.16.86.194:8080/upload"+url;
            Picinfo picinfo=new Picinfo(uid,text,url,location,false);
            PublicArray.add(picinfo);
            AllArray.add(picinfo);
        }

    }

}


//if(v.getId()==R.id.btn_start){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        //客户端
//                        HttpClient httpClient=new DefaultHttpClient();
//                        //post方式
//                        HttpPost httpPost=new HttpPost(Url);
//                        //传输数据
//                        List<NameValuePair> list=new ArrayList<NameValuePair>();
//                        list.add(new BasicNameValuePair("download","download"));
//                        list.add(new BasicNameValuePair("count","count"));
//
//                        UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"utf-8");
//                        httpPost.setEntity(entity);
//
//                        //回应
//                        HttpResponse httpResponse=httpClient.execute(httpPost);
//                        if(httpResponse.getStatusLine().getStatusCode()==200){
//
//                            HttpEntity entity1=httpResponse.getEntity();
//                            String jstr= EntityUtils.toString(entity1,"utf-8");
//                            Message message=new Message();
//                            message.what=download;
//                            message.obj=jstr;
//                            messageHander.sendMessage(message);
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }).start();}


//compile 'com.github.bumptech.glide:glide:4.0.0'
//
//    private ImageView iv_icon1;
//    private String url="http://172.16.86.194:8080/MyWebTest/123/service_2.jpg";
//        iv_icon1=mView.findViewById(R.id.iv_icon1);
////        url="https://pic.cnblogs.com/avatar/1142647/20170416093225.png";
//        url="https://b-ssl.duitang.com/uploads/item/201901/28/20190128152613_hkhni.jpg";
//        Glide.with(mContext).load(url).into(iv_icon1);
