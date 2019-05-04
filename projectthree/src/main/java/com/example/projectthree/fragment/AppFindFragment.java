package com.example.projectthree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.projectthree.R;
import com.example.projectthree.adapter.LinenarDynamicAdapter;
import com.example.projectthree.bean.Picinfo;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemDeleteClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemLongClickListener;
import com.example.projectthree.widget.SpacesItemDecoration;

import java.util.ArrayList;

public class AppFindFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener, OnItemDeleteClickListener,OnRefreshListener{
    private static final String TAG="FindFragment";
    protected View mView;
    protected Context mContext;
    private SwipeRefreshLayout srl_dynamic;
    private RecyclerView rv_dynamic; // 声明一个循环视图对象
    private LinenarDynamicAdapter adapter;
    private ArrayList<Picinfo> mPublicArray;
    private ArrayList<Picinfo> mAllArray;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
        mView=inflater.inflate(R.layout.fragment_app_find,container,false);
        srl_dynamic=mView.findViewById(R.id.srl_dynamic);
        srl_dynamic.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        srl_dynamic.setColorSchemeResources(
                R.color.red, R.color.orange, R.color.green, R.color.blue);

        initRecyclerDynamic(); // 初始化动态线性布局的循环视图

        return mView;
    }

    private void initRecyclerDynamic() {
        rv_dynamic=mView.findViewById(R.id.rv_dynamic);

        // 创建一个垂直方向的线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false);
        // 设置循环视图的布局管理器
        rv_dynamic.setLayoutManager(manager);

        mAllArray = Picinfo.getDefaultList();
        mPublicArray = Picinfo.getDefaultList();

        adapter = new LinenarDynamicAdapter(mContext, mPublicArray);
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


    // 一旦在下拉刷新布局内部往下拉动页面，就触发下拉监听器的onRefresh方法
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
            int position = (int) (Math.random() * 100 % mAllArray.size());
            Picinfo old_item = mAllArray.get(position);
            Picinfo new_item = new Picinfo(old_item.uid,
                    old_item.desc, old_item.location);
            mPublicArray.add(0, new_item);
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
                mPublicArray.get(position).desc);
        Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Picinfo item=mPublicArray.get(position);
        item.bPressed=!item.bPressed;
        mPublicArray.set(position,item);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onItemDeleteClick(View view, int position) {
        mPublicArray.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
