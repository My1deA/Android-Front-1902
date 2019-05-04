package com.example.projectthree;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.projectthree.fragment.AppFindFragment;
import com.example.projectthree.fragment.AppHomeFragment;
import com.example.projectthree.fragment.AppMeFragment;
import com.example.projectthree.fragment.AppMessageFragment;

public class AppMainActivity extends AppCompatActivity {
    private static final String TAG="AppMainActivity";
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        Bundle bundle=new Bundle();//用于传递信息
        bundle.putString("tag",TAG);

        tabHost=findViewById(android.R.id.tabhost);
        //把内容放在标签栏正上方
        tabHost.setup(this,getSupportFragmentManager(),R.id.fl_content);

        //放置fragment
        tabHost.addTab(getTabView(R.string.home,R.drawable.tab_home_selector), AppHomeFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.find,R.drawable.tab_find_selector), AppFindFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.message,R.drawable.tab_message_selector), AppMessageFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.me,R.drawable.tab_me_selector), AppMeFragment.class,bundle);

        //不设置各标签的之间的分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    private TabSpec getTabView(int textId,int imageId){
        String text=getResources().getString(textId);
        Drawable drawable=getResources().getDrawable(imageId);

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        View item_tabbar=getLayoutInflater().inflate(R.layout.item_tabbar,null);
        TextView tv_item=item_tabbar.findViewById(R.id.tv_item_tabbar);

        tv_item.setCompoundDrawables(null, drawable, null, null);
        // 生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }

}
