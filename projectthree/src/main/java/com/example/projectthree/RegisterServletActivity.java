package com.example.projectthree;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegisterServletActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG="RegisterServletActivity";
    private EditText et_username;
    private EditText et_password;
    private TextView tv_result;

    private final static String Url="http://172.16.86.194:8080/MyWebTest/registerServlet";
//    private final static String Url="http://192.168.137.1:8080/MyWebTest/registerServlet";

    private final static int Register=1;
    private final static int Fail=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_servlet);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        tv_result=findViewById(R.id.tv_result);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_register){
            String username=et_username.getText().toString().trim();
            String password=et_password.getText().toString().trim();
            tv_result.setText(username +"  "+password);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        //连接
                        HttpClient httpClient=new DefaultHttpClient();//客户端
                        HttpPost httpPost=new HttpPost(Url);//post路径
                        //实例化 可以变成Json传过
                        List<NameValuePair> list=new ArrayList<NameValuePair>();
                        list.add(new BasicNameValuePair("username",username));
                        list.add(new BasicNameValuePair("password",password));

                        //json
                        JSONObject item=new JSONObject();
                        item.put("username",username);
                        item.put("password",password);
                        String jsonStr=item.toString();
                        list.add(new BasicNameValuePair("jsonStr",jsonStr));

                        //实例化  将正常的username和password 还有测试的json串传送过去
                        UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"utf-8");
                        httpPost.setEntity(entity);
                        //回应
                        HttpResponse httpResponse=httpClient.execute(httpPost);
                        if(httpResponse.getStatusLine().getStatusCode()==200){

                            HttpEntity httpEntity=httpResponse.getEntity();
                            String row= EntityUtils.toString(httpEntity,"utf-8");
                            Message message=new Message();
                            message.what=Register;
                            message.obj=row;
                            handler.sendMessage(message);


                        }else{
                            Message message=new Message();
                            message.what=Fail;
                            handler.sendMessage(message);
                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Register){
                String row= (String) msg.obj;
                String [] info=row.split("#");
                if(info[1].equalsIgnoreCase("SUCC")){
                    tv_result.setText("注册成功");
                }else{
                    tv_result.setText("注册失败 用户名重复");
                }
            }else if(msg.what==Fail){
                tv_result.setText("服务器繁忙");
            }
        }
    };

}
