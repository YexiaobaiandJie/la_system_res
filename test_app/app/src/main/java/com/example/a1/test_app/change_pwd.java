package com.example.a1.test_app;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class change_pwd extends AppCompatActivity {
    private String oldpwd;
    private String newpwd;
    private String conpwd;
    private String userid;
    private String userpwd;
    private String chg_res;
    private String func;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Intent receiveIntent  = getIntent();
        func = receiveIntent.getStringExtra("func");
        userid = Transmitter.userinfo.user_id;
        userpwd = Transmitter.userinfo.user_password;
        btn_change = findViewById(R.id.change_pwd);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_oldpwd = findViewById(R.id.old_pwd);
                oldpwd = edit_oldpwd.getText().toString();
                edit_newpwd = findViewById(R.id.new_pwd);
                newpwd = edit_newpwd.getText().toString();
                edit_confirmpwd = findViewById(R.id.confirm_pwd);
                conpwd = edit_confirmpwd.getText().toString();
                if (newpwd.equals("") || oldpwd.equals("") || conpwd.equals("")){
                    Toast.makeText(change_pwd.this,"新密码或旧密码为空",Toast.LENGTH_SHORT).show();
                }else if(!oldpwd.equals(userpwd)){
                    Toast.makeText(change_pwd.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                } else if(!newpwd.equals(conpwd)){
                    Toast.makeText(change_pwd.this,"确认密码与新密码不一致，请修改",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            chg_pwd_struct pwdinfo = new chg_pwd_struct();
                            pwdinfo.setUserid(userid);
                            pwdinfo.setOldpwd(oldpwd);
                            pwdinfo.setNewpwd(newpwd);
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            Gson gson = new Gson();
                            String jsonStr = gson.toJson(pwdinfo);

                            RequestBody body = RequestBody.create(JSON,jsonStr);
                            Request request = new Request.Builder()
                                    .url("http://192.168.191.1:3000/change_pwd")
                                    .post(body)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String responseData = response.body().string();
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        chg_res=jsonObject.getString("ch_res");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Transmitter.userinfo.user_password=newpwd;
                                }
                            });
                        }
                    }).start();
                    switch (func){
                        case "b":
                            Intent intent_b = new Intent(change_pwd.this,function_b.class);
                            startActivity(intent_b);
                            break;
                        case "a":
                            Intent intent_a = new Intent(change_pwd.this,function_a.class);
                            startActivity(intent_a);
                            break;
                        case "admin":
                            Intent intent_admin = new Intent(change_pwd.this,function_admin.class);
                            startActivity(intent_admin);
                            break;
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
                        Intent intent_b = new Intent(change_pwd.this,function_b.class);
                        startActivity(intent_b);
                        break;
                    case "a":
                        Intent intent_a = new Intent(change_pwd.this,function_a.class);
                        startActivity(intent_a);
                        break;
                    case "admin":
                        Intent intent_admin = new Intent(change_pwd.this,function_admin.class);
                        startActivity(intent_admin);
                        break;
                }
            }
        });

    }


    private EditText edit_oldpwd;//原密码
    private EditText edit_newpwd;//新密码
    private EditText edit_confirmpwd;//确认密码
    private Button btn_change;//修改按钮
    private LinearLayout li_about;//about_me
}
