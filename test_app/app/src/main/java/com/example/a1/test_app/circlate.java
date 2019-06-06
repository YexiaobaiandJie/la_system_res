package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class circlate extends AppCompatActivity {
  private String result;
  private String lp_id;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_circlate);
    Intent receiveIntent  = getIntent();
    result = receiveIntent.getStringExtra("result");
    lp_id = receiveIntent.getStringExtra("item_id");
    //点击“流转”按钮，审批单流转而界面返回至todo_af_list
    btn_cir = findViewById(R.id.button21);
    btn_cir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(circlate.this,todo_af_list.class);
        startActivity(intent);
        //点击流转按钮，修改法人申请状态，提交至经营科
        new Thread(new Runnable() {
          @Override
          public void run() {
            Flsw_id lp_id_json = new Flsw_id();
            lp_id_json.setFlsw_id(lp_id);
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            Gson gson = new Gson();
            String jsonStr = gson.toJson(lp_id_json);

            RequestBody body = RequestBody.create(JSON,jsonStr);
            Request request = new Request.Builder()
                    .url("http://192.168.191.1:3000/lp_submit_jy")
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
      }
    });


    //点击返回按钮返回至上一界面
    btn_back = findViewById(R.id.button22);
    btn_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent2 = new Intent(circlate.this,LP_info.class);
        intent2.putExtra("item_id",lp_id);
        startActivity(intent2);
      }
    });
  }

  private Button btn_cir;//button21
  private Button btn_back; //button22
}
