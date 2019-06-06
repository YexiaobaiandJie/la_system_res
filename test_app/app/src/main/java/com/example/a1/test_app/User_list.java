package com.example.a1.test_app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_list extends AppCompatActivity {
    private String userid;
    private Handler handler;
    ArrayList<User_admin> user_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        userid = Transmitter.userinfo.user_id;
        initUsers();
        //获得所有用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Creator_list creator = new Creator_list();
                creator.set_list_owner(userid);
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(creator);

                RequestBody body = RequestBody.create(JSON,jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/admin_user_list")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //拿到json,并转化成String
                        final String responseData = response.body().string();

                        //Json的解析类对象
                        JsonParser parser = new JsonParser();
                        //将Json的string转换成一个JsonArray对象
                        JsonArray jsonArray = parser.parse(responseData).getAsJsonArray();
                        Gson gson = new Gson();
                        ArrayList<User_admin> user_list_bean = new ArrayList<>();

                        for(JsonElement user :jsonArray){
                            //使用gson ,转换成bean对象
                            User_admin user_listview_bean = gson.fromJson(user,User_admin.class);
                            user_list_bean.add(user_listview_bean);
                        }
                        Message message = Message.obtain();
                        message.obj = user_list_bean;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();
    }


    private void initUsers(){
        final ListView listview = findViewById(R.id.list_view);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                user_list = (ArrayList<User_admin>) msg.obj;
                UserAdapter adapter = new UserAdapter(User_list.this, R.layout.user_item, user_list);
                listview.setAdapter(adapter);
            }
        };
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
                Intent intent_f = new Intent(User_list.this,function_admin.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(User_list.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }
}
