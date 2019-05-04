package com.example.projectthree.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PermissionUtil {
    private final static String TAG="PermissionUtil";

    public static boolean  checkPermission(Activity activity,String permission,int requestCode){
        Log.d(TAG, "checkPermission: "+" Single");
        boolean result=true;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int check= ContextCompat.checkSelfPermission(activity,permission);
            if(check!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
                result=false;
            }
        }
        return result;
    }

    public static boolean checkMultiPermission(Activity activity,String [] permissions,int requestCode){
        Log.d(TAG, "checkPermission: "+" Multi");
        boolean reuslt=true;
        int check=PackageManager.PERMISSION_GRANTED;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            for(String permission:permissions){
                check=ContextCompat.checkSelfPermission(activity,permission);
                if(check!=PackageManager.PERMISSION_GRANTED){
                    break;
                }
            }
            if(check!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,permissions,requestCode);
                reuslt=false;
            }
        }
        return reuslt;
    }

    public static void goActivity(Context context,Class<?> cls){
        Intent intent=new Intent(context,cls);
        context.startActivity(intent);
    }

}
