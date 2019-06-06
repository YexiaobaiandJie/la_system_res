package com.example.a1.test_app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class AF_result_wen extends AppCompatActivity {
    private String flsw_id;
    private Wen_info_return flsw_info_ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_result_wen);
        Intent receiveIntent = getIntent();
        flsw_id = receiveIntent.getStringExtra("item_id");


        new Thread(new Runnable() {
            @Override
            public void run() {
                Flsw_id flsw_id_json = new Flsw_id();
                flsw_id_json.setFlsw_id(flsw_id);
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(flsw_id_json);

                RequestBody body = RequestBody.create(JSON, jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/post_wen_result")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    /////////////////////////////////////////////////////////
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();

                        Wen_info_return flsw_info = new Wen_info_return();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            flsw_info.setLa_name(jsonObject.getString("La_name"));
                            flsw_info.setLa_type(jsonObject.getString("La_type"));
                            flsw_info.setLa_creator(jsonObject.getString("La_creator"));
                            flsw_info.setLa_time(jsonObject.getString("La_time"));
                            flsw_info.setLa_content(jsonObject.getString("La_content"));
                            flsw_info.setLa_id(jsonObject.getString("La_id"));
                            flsw_info.setLa_dep(jsonObject.getString("La_dep"));
                            flsw_info.setLa_email(jsonObject.getString("La_email"));
                            flsw_info.setLa_cellphone(jsonObject.getString("La_cellphone"));
                            flsw_info.setLa_remark(jsonObject.getString("La_remark"));
                            flsw_info.setLa_check_record(jsonObject.getString("La_check_record"));
                            flsw_info.setOpinion_content(jsonObject.getString("Opinion_content"));
                            Message message = Message.obtain();
                            message.obj = flsw_info;
                            mhandler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            flsw_info_ui = (Wen_info_return) msg.obj;

            //将信息显示在UI上
            text_name = findViewById(R.id.textView5);
            text_name.setText(flsw_info_ui.getLa_name());
            text_type = findViewById(R.id.textView6);
            text_type.setText(flsw_info_ui.getLa_type());
            text_creator = findViewById(R.id.textView8);
            text_creator.setText(flsw_info_ui.getLa_creator());
            text_time = findViewById(R.id.textView10);
            text_time.setText(flsw_info_ui.getLa_time());
            text_content = findViewById(R.id.textView12);
            text_content.setText(flsw_info_ui.getLa_content());
            text_id = findViewById(R.id.textView115);
            text_id.setText(flsw_info_ui.getLa_id());
            text_dep = findViewById(R.id.textView116);
            text_dep.setText(flsw_info_ui.getLa_dep());
            text_email = findViewById(R.id.textView110);
            text_email.setText(flsw_info_ui.getLa_email());
            text_cellphone = findViewById(R.id.textView112);
            text_cellphone.setText(flsw_info_ui.getLa_cellphone());
            text_remark = findViewById(R.id.textView16);
            text_remark.setText(flsw_info_ui.getLa_remark());
            text_record = findViewById(R.id.editText10);
            text_record.setText(flsw_info_ui.getLa_check_record());
            text_opinion = findViewById(R.id.textView23);
            text_opinion.setText(flsw_info_ui.getOpinion_content());

            button_moreinfo = findViewById(R.id.button);
            layout_moreinfo = (LinearLayout) findViewById(R.id.moreinfo);
            button_moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isVisible) {
                        isVisible = true;
                        layout_moreinfo.setVisibility(View.VISIBLE);
                    } else {
                        layout_moreinfo.setVisibility(View.GONE);
                        isVisible = false;
                    }
                }
            });

            button_checkre = findViewById(R.id.button3);
            layout_checkre = (LinearLayout) findViewById(R.id.check_re);
            button_checkre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isVisible2) {
                        isVisible2 = true;
                        layout_checkre.setVisibility(View.VISIBLE);
                    } else {
                        layout_checkre.setVisibility(View.GONE);
                        isVisible2 = false;
                    }
                }
            });

            btn_end = findViewById(R.id.button6);
            btn_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_end = new Intent(AF_result_wen.this,AF_list.class);
                    startActivity(intent_end);
                }
            });


        }


    };


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.back_func:
                //在此界面点击返回功能界面，返回功能界面
                Intent intent_f = new Intent(AF_result_wen.this,function_a.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(AF_result_wen.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }

    private Button button_moreinfo; /**更多信息按钮**/
    private LinearLayout layout_moreinfo; /**更多信息总布局**/
    private Boolean isVisible = false;
    private Button button_checkre;/**审核记录按钮button3**/
    private LinearLayout layout_checkre;/**审核记录总布局**/
    private Boolean isVisible2 = false;
    private TextView text_name;
    private TextView text_type;
    private TextView text_creator;
    private TextView text_time;
    private TextView text_content;
    private TextView text_id;
    private TextView text_dep;
    private TextView text_email;
    private TextView text_cellphone;
    private TextView text_remark;
    private TextView text_record;
    private TextView text_opinion;
    private Button btn_end;




}
