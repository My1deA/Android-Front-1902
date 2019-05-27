package com.example.projectthree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectthree.util.DateUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.projectthree.UploadActivity.getLoacalBitmap;


public class UploadActivity extends Activity implements View.OnClickListener
{
    public static final MediaType JSON
            =MediaType.parse("application/json; charset=utf-8");
    private ArrayList<ImageView> img=new ArrayList<>();
    private int[] imagePath=new int[9];
    private static final int GET=1;
    private static final int POST=2;
    private static final String TAG=UploadActivity.class.getSimpleName();
    private Button Upload;
    private TextView textShow;
    private ProgressBar progressBar;
    private ArrayList<String> arrayList;
    private ArrayList<String> fileName=new ArrayList<String>();
    private GridLayout gridlayout;


    private RecyclerView rv;
    private List<String> images=new ArrayList<String>();//图片地址
    private Context mContext;
    private DisplayImageOptions options;
    private MyRecyclerViewAdapter adapter;
    private HashMap<Integer, float[]> xyMap=new HashMap<Integer, float[]>();//所有子项的坐标
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    int index=0;


    private EditText et_text;
    private TextView tv_cityloc;
    private String type="图片";
    private String username;

//    private static String mBaseUrl="http://172.16.86.194:8080/MyWebTest/uploadServlet";
    private static String mBaseUrl="http://192.168.137.1:8080/MyWebTest/uploadServlet";





    OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build();
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET:
                    textShow.setText((String) msg.obj);
                    break;
                case POST:
                    textShow.setText((String) msg.obj);
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Upload=(Button)findViewById(R.id.Upload);
        textShow=(TextView)findViewById(R.id.textShow);
        textShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        arrayList=new ArrayList<>();
        gridlayout=(GridLayout)findViewById(R.id.gridlayout);
        Upload.setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        et_text=findViewById(R.id.et_text);
        tv_cityloc=findViewById(R.id.tv_cityloc);

        username=MainApplication.getInstance().UserinfoMap.get("username");
        Bundle bundle=getIntent().getExtras();
        tv_cityloc.setText(bundle.getString("city"));

        mContext=this;
        initView();

    }

    /**
     *
     */
    String path;
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FileChooseUtil fileChooseUtil=new FileChooseUtil();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                String name=path.substring(path.lastIndexOf("/")+1);
                fileName.add(name);
                arrayList.add(path);
                textShow.setText("");
                for(String s:arrayList)
                {
                    textShow.append(s+"\n");
                }
//                textShow.setText(path);
                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = fileChooseUtil.getPath(this, uri);
                arrayList.add(path);
                String name=path.substring(path.lastIndexOf("/")+1);
                fileName.add(name);
                textShow.setText("");
                for(String s:arrayList)
                {
                    textShow.append(s+"\n");
                }

                Bitmap bitmap=getLoacalBitmap(path);
                ImageView imageView=new ImageView(this);
                img.add(imageView);
                imageView.setImageBitmap(bitmap);
                imageView.setPadding(2,2,2,2);
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(350,350);//设置图片的宽度和高度
                imageView.setLayoutParams(params);//为ImageView设置布局参数
                gridlayout.addView(imageView);
                initData();
                setEvent();

//                textShow.setText(path);
                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                arrayList.add(path);

                String name=path.substring(path.lastIndexOf("/")+1);
                fileName.add(name);
                textShow.setText("");
                for(String s:arrayList)
                {
                    textShow.append(s+"\n");
                }
//                textShow.setText(path);
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
    /**
     *
     */
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
                    .execute(new MyStringCallback());
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
    public void downloadFile() {
        checkPermission();
        String url = "http://vfx.mtime.cn/Video/2019/04/10/mp4/190410081607863991.mp4";//"http://172.16.86.49:8001/upload/1.jpg";//
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "okhttp-test1.mp4")//
                {
                    @Override
                    public void onBefore(Request request, int id)
                    {
                    }
                    @Override
                    public void inProgress(float progress, long total, int id)
                    {
                        progressBar.setProgress((int) (100 * progress));
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }
                    @Override
                    public void onResponse(File file, int id)
                    {
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
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
            progressBar.setProgress((int) (100 * progress));
        }
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.Upload)
        {
            checkPermission();
            FileChooseUtil fileChooseUtil=new FileChooseUtil();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");//选择图片
            intent.setType("audio/*"); //选择音频
            intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
            intent.setType("video/*;image/*");//同时选择视频和图片
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,1);
        }if(v.getId()==R.id.btn_ok){
            multiFileUpload();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    /**
     * recyclerView item点击事件
     */
    private void setEvent() {
        adapter.setmOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mContext,SecondActivity.class);
                intent.putStringArrayListExtra("urls", (ArrayList<String>) images);
                intent.putExtra("position", position);
                xyMap.clear();//每一次点击前子项坐标都不一样，所以清空子项坐标

                //子项前置判断，是否在屏幕内，不在的话获取屏幕边缘坐标
                View view0=rv.getChildAt(0);
                int position0=rv.getChildPosition(view0);
                if(position0>0)
                {
                    for(int j=0;j<position0;j++)
                    {
                        float[] xyf=new float[]{(1/6.0f+(j%3)*(1/3.0f))*screenWidth,0};//每行3张图，每张图的中心点横坐标自然是屏幕宽度的1/6,3/6,5/6
                        xyMap.put(j, xyf);
                    }
                }

                //其余子项判断
                for(int i=position0;i<rv.getAdapter().getItemCount();i++)
                {
                    View view1=rv.getChildAt(i-position0);
                    if(rv.getChildPosition(view1)==-1)//子项末尾不在屏幕部分同样赋值屏幕底部边缘
                    {
                        float[] xyf=new float[]{(1/6.0f+(i%3)*(1/3.0f))*screenWidth,screenHeight};
                        xyMap.put(i, xyf);
                    }
                    else
                    {
                        int[] xy = new int[2];
                        view1.getLocationOnScreen(xy);
                        float[] xyf=new float[]{xy[0]*1.0f+view1.getWidth()/2,xy[1]*1.0f+view1.getHeight()/2};
                        xyMap.put(i, xyf);
                    }
                }
                intent.putExtra("xyMap",xyMap);
                startActivity(intent);
            }
        });
    }

    private void initView()
    {
        rv=(RecyclerView)findViewById(R.id.rv);
        GridLayoutManager glm=new GridLayoutManager(mContext,3);//定义3列的网格布局
        rv.setLayoutManager(glm);
        rv.addItemDecoration(new RecyclerViewItemDecoration(20,3));//初始化子项距离和列数
        options=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(5))
                .build();
        adapter=new MyRecyclerViewAdapter(images,mContext,options,glm);
        rv.setAdapter(adapter);
    }

    /**
     * 初始化网络图片地址，来自百度图片
     */
    private void initData()
    {
        images.add(arrayList.get(index++));
        adapter.notifyDataSetChanged();
    }
    public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration
    {
        private int itemSpace;//定义子项间距
        private int itemColumnNum;//定义子项的列数

        public RecyclerViewItemDecoration(int itemSpace, int itemColumnNum) {
            this.itemSpace = itemSpace;
            this.itemColumnNum = itemColumnNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom=itemSpace;//底部留出间距
            if(parent.getChildPosition(view)%itemColumnNum==0)//每行第一项左边不留间距，其他留出间距
            {
                outRect.left=0;
            }
            else
            {
                outRect.left=itemSpace;
            }

        }
    }

    /**
     * 重写startActivity方法，禁用activity默认动画
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0,0);
    }
}
class FileChooseUtil {

    private Context context;
    private static FileChooseUtil util = null;

    public FileChooseUtil(Context context) {
        this.context = context;
    }

    public FileChooseUtil() {

    }

    public  static FileChooseUtil getInstance(Context context) {
        if (util == null) {
            util = new FileChooseUtil(context);
        }
        return util;
    }

    /**
     * 对外接口  获取uri对应的路径
     *
     * @param uri
     * @return
     */
    public String getChooseFileResultPath(Uri uri) {
        String chooseFilePath = null;
        if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
            chooseFilePath = uri.getPath();
            Toast.makeText(context, chooseFilePath, Toast.LENGTH_SHORT).show();
            return chooseFilePath;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
            chooseFilePath = getPath(context, uri);
        } else {//4.4以下下系统调用方法
            chooseFilePath = getRealPathFromURI(uri);
        }
        return chooseFilePath;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);

            }

        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);

        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            uri.getPath();

        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}





class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();//初始化ImageLoader
    }
    private void initImageLoader()
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiscCache(new File(Environment.getExternalStorageDirectory()+File.separator+"ImageObserverDemo"+File.separator+"Image_cache")))
                .defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置
                build();
        ImageLoader.getInstance().init(config); // 初始化
    }
}


/**
 * Created by Administrator on 2018/4/28 0028.
 */

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

    private List<String> images=new ArrayList<String>();//Image资源，内容为图片的网络地址
    private Context mContext;
    private DisplayImageOptions options;//UniversalImageLoad
    private GridLayoutManager glm;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerViewAdapter(List<String> images, Context mContext,DisplayImageOptions options,GridLayoutManager glm) {
        this.images = images;
        this.mContext = mContext;
        this.options=options;
        this.glm=glm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.rv_item_layout,null);//加载item布局
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        myViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置图片充满ImageView并自动裁剪居中显示
        ViewGroup.LayoutParams parm = myViewHolder.imageView.getLayoutParams();
        parm.height = glm.getWidth()/glm.getSpanCount()
                - 2*myViewHolder.imageView.getPaddingLeft() - 2*((ViewGroup.MarginLayoutParams)parm).leftMargin;//设置imageView宽高相同
        Bitmap bitmap=getLoacalBitmap(images.get(i));
        myViewHolder.imageView.setImageBitmap(bitmap);
        //ImageLoader.getInstance().displayImage(images.get(i),myViewHolder.imageView,options);//网络加载原图
        if(mOnItemClickListener!=null)//传递监听事件
        {
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(myViewHolder.imageView,i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.iv_item);
        }
    }

    /**
     * 对外暴露子项点击事件监听器
     * @param mOnItemClickListener
     */
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener=mOnItemClickListener;
    }

    /**
     * 子项点击接口
     */
    public interface OnItemClickListener
    {
        public void onClick(View view,int position);
    }
}



