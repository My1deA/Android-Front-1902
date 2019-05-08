package com.example.projectthree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.projectthree.adapter.NineGridAdapter;
import com.example.projectthree.bean.NineGridItem;


import java.io.Serializable;
import java.util.List;

public class RecyclerViewExampleActivity extends AppCompatActivity {

    private static final String ARG_LIST = "list";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NineGridAdapter mAdapter;

    private List<NineGridItem> mList;

    public static void startActivity(Context context, List<NineGridItem> list) {
        Intent intent = new Intent(context, RecyclerViewExampleActivity.class);
        intent.putExtra(ARG_LIST, (Serializable) list);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        getIntentData();
        initView();
    }

    private void getIntentData() {
        mList = (List<NineGridItem>) getIntent().getSerializableExtra(ARG_LIST);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NineGridAdapter(this);
        mAdapter.setList(mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
