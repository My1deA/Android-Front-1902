package com.example.projectthree;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.fragment.AppFindFragment;
import com.example.projectthree.fragment.AppFindFragment2;
import com.example.projectthree.fragment.AppHomeFragment;
import com.example.projectthree.fragment.AppMeFragment;
import com.example.projectthree.fragment.AppMessageFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AppMainActivity2 extends AppCompatActivity {
    private static final String TAG="AppMainActivity";
    private FragmentTabHost tabHost;
    public String username=null;
    public String password=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        Intent intent=getIntent();
        Bundle temp=intent.getExtras();
        username=temp.getString("username");
        password=temp.getString("password");


        Bundle bundle=new Bundle();//用于传递信息
        bundle.putString("tag",TAG);
        bundle.putString("username",username);
        bundle.putString("password",password);

        tabHost=findViewById(android.R.id.tabhost);
        //把内容放在标签栏正上方
        tabHost.setup(this,getSupportFragmentManager(),R.id.fl_content);

        //放置fragment
        tabHost.addTab(getTabView(R.string.home,R.drawable.tab_home_selector), AppHomeFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.find,R.drawable.tab_find_selector), AppFindFragment2.class,bundle);
        tabHost.addTab(getTabView(R.string.message,R.drawable.tab_message_selector), AppMessageFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.me,R.drawable.tab_me_selector), AppMeFragment.class,bundle);

        //不设置各标签的之间的分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //initMap();
        } else {
            AppMainActivity2PermissionsDispatcher.ApplySuccessWithCheck(this);
        }
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AppMainActivity2PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功时
     */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void ApplySuccess() {
        //initMap();
    }

    /**
     * 申请权限告诉用户原因时
     * @param request
     */
    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForMap(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开定位的权限", request);
    }

    /**
     * 申请权限被拒绝时
     *
     */
    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapDenied() {
        Toast.makeText(this,"你拒绝了权限，该功能不可用",Toast.LENGTH_LONG).show();
    }

    /**
     * 申请权限被拒绝并勾选不再提醒时
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapNeverAskAgain() {
        AskForPermission();
    }

    /**
     * 告知用户具体需要权限的原因
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前应用缺少定位权限,请去设置界面打开\n打开之后按两次返回键可回到该应用哦");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + AppMainActivity2.this.getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

}
