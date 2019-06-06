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

public class circulate_back extends AppCompatActivity {
    private String result;
    private String lp_id;
    private String last;
    private String lp_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulate_back);
        Intent receiveIntent  = getIntent();
        result = receiveIntent.getStringExtra("result");
        lp_id = receiveIntent.getStringExtra("item_id");
        last = receiveIntent.getStringExtra("last_view");
        lp_type = receiveIntent.getStringExtra("lp_type");
        //点击“流转”按钮，审批单流转而界面返回至todo_af_list
        btn_cir = findViewById(R.id.button21);
        btn_cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(circulate_back.this,todo_af_list.class);
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
                                .url("http://192.168.191.1:3000/lp_return_cre")
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
                switch (last){
                    case "qf":
                        switch(lp_type){
                            case "单项投标":
                                Intent intent2 = new Intent(circulate_back.this,LP_info.class);
                                intent2.putExtra("item_id",lp_id);
                                startActivity(intent2);
                                break;
                            case "单项不投标":
                                Intent intent2_n = new Intent(circulate_back.this,LP_info_n.class);
                                intent2_n.putExtra("item_id",lp_id);
                                startActivity(intent2_n);
                                break;
                        }
                        break;
                    case "jy":
                        Intent intent3 = new Intent(circulate_back.this,LP_info_jy.class);
                        intent3.putExtra("item_id",lp_id);
                        startActivity(intent3);
                        break;
                    case "fw":
                        Intent intent4 = new Intent(circulate_back.this,LP_info_fw.class);
                        intent4.putExtra("item_id",lp_id);
                        startActivity(intent4);
                        break;
                    case "qf_m":
                        switch (lp_type){
                            case "单项投标":
                                Intent intent5 = new Intent(circulate_back.this,LP_info_qf_m.class);
                                intent5.putExtra("item_id",lp_id);
                                startActivity(intent5);
                                break;
                            case "单项不投标":
                                Intent intent5_n = new Intent(circulate_back.this,LP_info_n_qf_m.class);
                                intent5_n.putExtra("item_id",lp_id);
                                startActivity(intent5_n);
                                break;
                        }
                        break;
                    case "fg":
                        switch (lp_type){
                            case "单项投标":
                                Intent intent6 = new Intent(circulate_back.this,LP_info_fg.class);
                                intent6.putExtra("item_id",lp_id);
                                startActivity(intent6);
                                break;
                            case "单项不投标":
                                Intent intent6_n = new Intent(circulate_back.this,LP_info_n_fg.class);
                                intent6_n.putExtra("item_id",lp_id);
                                startActivity(intent6_n);
                                break;
                        }
                        break;
                    case "z":
                        switch(lp_type){
                            case "单项投标":
                                Intent intent7 = new Intent(circulate_back.this,LP_info_over.class);
                                intent7.putExtra("item_id",lp_id);
                                startActivity(intent7);
                                break;
                            case "单项不投标":
                                Intent intent7_n = new Intent(circulate_back.this,LP_info_n_over.class);
                                intent7_n.putExtra("item_id",lp_id);
                                startActivity(intent7_n);
                                break;
                        }
                        break;

                }

            }
        });
    }
    private Button btn_cir;//button21
    private Button btn_back; //button22

}
