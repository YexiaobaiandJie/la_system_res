package com.example.a1.test_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class FirstAcitivity extends Activity {

    /**为登录按钮绑定跳转事件**/

    private TextView filename;//下载事件的文件text
    private ProgressBar progressBar;

    private LoadingButton button1;
    private String userid_input;
    private String userpwd_input;
    private EditText editid;
    private EditText editpwd;
    private User login_userinfo;
    private Button btn_test;//button24
    private Button btn_upload;
    private String uploadFilePath;
    private String uploadFileName;
    private Boolean offline = true;
    public void set_login_userinfo(User login_userinfo){
        this.login_userinfo = login_userinfo;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    set_login_userinfo((User)msg.obj);
                    if(login_userinfo.user_result == 2){
                        //如果登录成功
                        switch (login_userinfo.user_position){
                            case 1:
                                Intent intent = new Intent(FirstAcitivity.this,function_a.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 2:
                                Intent intent2 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent2);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 3:
                                Intent intent3 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent3);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 4:
                                Intent intent4 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent4);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 5:
                                Intent intent5 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent5);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 11:
                                Intent intent11 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent11);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 12:
                                Intent intent12 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent12);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 13:
                                Intent intent13 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent13);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 14:
                                Intent intent14 = new Intent(FirstAcitivity.this,function_b.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent14);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 100:
                                Intent intent100 = new Intent(FirstAcitivity.this,function_admin.class);
                                Transmitter.userinfo = login_userinfo;
                                Toast.makeText(FirstAcitivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent100);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                        }


                    }else{
                        //如果登录失败
                        if(login_userinfo.user_result == 1) {
                            finishLoading();
                            Toast.makeText(FirstAcitivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }else if(login_userinfo.user_result == 0){
                            finishLoading();
                            Toast.makeText(FirstAcitivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };


    //上传文件相关
    //File file;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        button1 = (LoadingButton) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                editid = findViewById(R.id.editText3);
                editpwd = findViewById(R.id.editText4);
                String id = editid.getText().toString();
                String pwd = editpwd.getText().toString();
                if(id.equals("") || pwd.equals("")){
                    Toast.makeText(FirstAcitivity.this,"账户或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    startLoading();

                }
            }
        });
        /*
        filename = findViewById(R.id.file_name);
        filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String downloadname=filename.getText().toString();
                final String url="http://192.168.191.1:3000/post_download_file?filename="+downloadname;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                byte[] bytes = response.body().bytes();
                                DownloadUtil.createFileWithByte(bytes,downloadname);

                            }
                        });

                    }
                }).start();
            }
        });
        */





    }




    private void startLoading() {
        button1.startLoading(new LoadingButton.OnStartListener() {
            @Override
            public void onStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {


                                userid_input = editid.getText().toString();
                                userpwd_input = editpwd.getText().toString();
                                //创建okhttpclient对象
                                OkHttpClient client = new OkHttpClient();
                                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                                User_short userinfo = new User_short();
                                userinfo.set_userid(userid_input);
                                userinfo.set_password(userpwd_input);
                                Gson gson = new Gson();
                                String jsonStr = gson.toJson(userinfo);

                                RequestBody body = RequestBody.create(JSON,jsonStr);
                                Request request = new Request.Builder()
                                        .url("http://192.168.191.1:3000/login")
                                        .post(body)
                                        .build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        //回调的方法执行在子线程
                                        final String responseData = response.body().string();
                                        try{
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            int res = parseInt(jsonObject.getString("result"));
                                            JSONObject json_userinfo = jsonObject.getJSONObject("userinfo");
                                            User userinfo = new User();
                                            userinfo.user_id = json_userinfo.getString("user_id");
                                            userinfo.user_name = json_userinfo.getString("user_name");
                                            userinfo.user_password = json_userinfo.getString("user_password");
                                            userinfo.user_sex = json_userinfo.getString("user_sex");
                                            userinfo.user_dep = json_userinfo.getString("user_dep");
                                            userinfo.user_position = json_userinfo.getInt("user_position");
                                            userinfo.user_cellphone = json_userinfo.getString("user_cellphone");
                                            userinfo.user_email = json_userinfo.getString("user_email");
                                            userinfo.user_result = res;
                                            Message message = Message.obtain();
                                            message.what = 1;
                                            message.obj = userinfo;
                                            handler.sendMessage(message);
                                            System.out.println("message.obj "+message.obj);
                                            offline = false;
                                        } catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }

                                });
                            }
                        }).start();
                    }
                }, 800);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(offline){
                            Toast.makeText(FirstAcitivity.this,"未连接到服务器",Toast.LENGTH_SHORT).show();
                            finishLoading();
                        }

                    }
                }, 3000);

            }
        });
    }

    private void finishLoading() {
        button1.finishLoading(new LoadingButton.OnFinishListener() {
            @Override
            public void onFinish() {

            }
        });
    }




}

