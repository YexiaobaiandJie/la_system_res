package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class change_phone extends AppCompatActivity {

    private String func;
    private String userid;
    private String newnumber="";
    private String oldnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        Intent receiveIntent  = getIntent();
        func = receiveIntent.getStringExtra("func");
        userid = Transmitter.userinfo.user_id;
        oldnumber = Transmitter.userinfo.user_cellphone;

        text_newphone = findViewById(R.id.new_phone);
        btn_chge_phone = findViewById(R.id.btn_chg);
        btn_chge_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newnumber = text_newphone.getText().toString();
                if(oldnumber.equals(newnumber)){
                    Toast.makeText(change_phone.this,"新号码不能与原号码一致",Toast.LENGTH_SHORT).show();
                }else{
                    if(newnumber.equals("")){
                        Toast.makeText(change_phone.this,"新电话号码不能为空",Toast.LENGTH_SHORT).show();
                    }else if(!isMobile(newnumber)){//判断电话号码的格式
                        //如果格式错误
                        Toast.makeText(change_phone.this,"电话号码格式错误",Toast.LENGTH_SHORT).show();
                    }else{
                        //如果格式正确
                        //符合所有条件，进行数据交互，修改数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client = new OkHttpClient();
                                user_phone info = new user_phone();
                                info.setUserid(userid);
                                info.setUser_phone(newnumber);
                                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                                Gson gson = new Gson();
                                String jsonStr = gson.toJson(info);

                                RequestBody body = RequestBody.create(JSON,jsonStr);
                                Request request = new Request.Builder()
                                        .url("http://192.168.191.1:3000/change_phone")
                                        .post(body)
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        //Toast.makeText(change_phone.this,"修改失败，请稍后再试",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response){
                                        Transmitter.userinfo.user_cellphone=newnumber;

                                    }
                                });
                            }
                        }).start();
                        switch (func){
                            case "b":
                                Intent intent_b = new Intent(change_phone.this,function_b.class);
                                startActivity(intent_b);
                                break;
                            case "a":
                                Intent intent_a = new Intent(change_phone.this,function_a.class);
                                startActivity(intent_a);
                                break;
                            case "admin":
                                Intent intent_admin = new Intent(change_phone.this,function_admin.class);
                                startActivity(intent_admin);
                                break;
                        }

                    }
                }

            }
        });
        li_about = findViewById(R.id.about_me);
        li_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (func){
                    case "b":
                        Intent intent_b = new Intent(change_phone.this,function_b.class);
                        startActivity(intent_b);
                        break;
                    case "a":
                        Intent intent_a = new Intent(change_phone.this,function_a.class);
                        startActivity(intent_a);
                        break;
                    case "admin":
                        Intent intent_admin = new Intent(change_phone.this,function_admin.class);
                        startActivity(intent_admin);
                        break;
                }
            }
        });



    }

    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1(3|5|7|8|4)\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private EditText text_newphone;
    private Button btn_chge_phone;
    private LinearLayout li_about;//about_me
}
