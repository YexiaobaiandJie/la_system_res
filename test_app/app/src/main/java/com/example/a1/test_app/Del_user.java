package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Del_user extends AppCompatActivity {
    private String userid_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_user);

        btn_del = findViewById(R.id.del);
        userid = findViewById(R.id.user_id);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid_info = userid.getText().toString();
                if(userid_info.equals("")){
                    Toast.makeText(Del_user.this,"用户id不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User_short info = new User_short();
                            info.set_userid(userid_info);
                            OkHttpClient client = new OkHttpClient();
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            Gson gson = new Gson();
                            String jsonStr = gson.toJson(info);

                            RequestBody body = RequestBody.create(JSON, jsonStr);
                            Request request = new Request.Builder()
                                    .url("http://192.168.191.1:3000/admin_del_user")
                                    .post(body)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                }
                            });
                        }
                    }).start();
                    Intent intent = new Intent(Del_user.this,Del_user.class);
                    startActivity(intent);
                }

            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.back_func:
                //在此界面点击返回功能界面，返回功能界面
                Intent intent_f = new Intent(Del_user.this,function_admin.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(Del_user.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }

    private Button btn_del;
    private EditText userid;
}
