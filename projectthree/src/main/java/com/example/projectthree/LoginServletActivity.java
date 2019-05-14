package com.example.projectthree;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginServletActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG="LoginServletActivity";
    private EditText et_username;
    private EditText et_password;
    private TextView tv_result;
    private static String url="http://172.16.86.194:8080/MyWebTest/loginServlet";
    private final static int Login=1;
    private final static int Fail=2;
    String username=null;
    String password=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_servlet);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        tv_result=findViewById(R.id.tv_result);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_login){
            username=et_username.getText().toString().trim();
            password=et_password.getText().toString().trim();
//            if(username.equals("1")){
//                MainApplication.getInstance().UserinfoMap.put("username",username);
//                MainApplication.getInstance().UserinfoMap.put("password",password);
//
//
//                Intent intent=new Intent(LoginServletActivity.this,AppMainActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("username",username);
//                bundle.putString("password",password);
////                    Toast.makeText(LoginServletActivity.this,username+" "+password,Toast.LENGTH_SHORT).show();
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }else{

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(url);
                            List<NameValuePair> list = new ArrayList<NameValuePair>();
                            list.add(new BasicNameValuePair("username", username));
                            list.add(new BasicNameValuePair("password", password));
                            //实例化
                            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                            httpPost.setEntity(entity);
                            HttpResponse httpResponse = httpClient.execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity1 = httpResponse.getEntity();
                                String row = EntityUtils.toString(entity1, "utf-8");
                                Message message = new Message();
                                message.what = Login;
                                message.obj = row;
                                handler.sendMessage(message);

                            } else {
                                Message message = new Message();
                                message.what = Fail;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//            }

        }else if(v.getId()==R.id.btn_register){
            Intent intent=new Intent(this,RegisterServletActivity.class);
            startActivity(intent);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Login){
                String row=(String)msg.obj;
                String[] info=row.split("#");

                if(info[1].equalsIgnoreCase("SUCC")){
                    tv_result.setText("Login SUCC");
                    Toast.makeText(LoginServletActivity.this,"Succ",Toast.LENGTH_SHORT).show();

                    MainApplication.getInstance().UserinfoMap.put("username",username);
                    MainApplication.getInstance().UserinfoMap.put("password",password);


                    Intent intent=new Intent(LoginServletActivity.this,AppMainActivity2.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",username);
                    bundle.putString("password",password);
//                    Toast.makeText(LoginServletActivity.this,username+" "+password,Toast.LENGTH_SHORT).show();
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    tv_result.setText("Login Fail 请检查用户名和密码是否正确");
                    Toast.makeText(LoginServletActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what==Fail){
                tv_result.setText("服务器繁忙");
//                Toast.makeText(LoginServletActivity.this,"Succ",Toast.LENGTH_SHORT).show();
            }

        }
    };
}
