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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class AF_wen_info extends AppCompatActivity {
    private String user_id;
    private String flsw_id;
    private Flsw_all_info flsw_info_ui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_wen_info);
        Intent receiveIntent  = getIntent();
        flsw_id = receiveIntent.getStringExtra("item_id");
        user_id = receiveIntent.getStringExtra("user_id");


        new Thread(new Runnable() {
            @Override
            public void run() {

                Get_flsw_info_json info_json = new Get_flsw_info_json(flsw_id, user_id);
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(info_json);

                RequestBody body = RequestBody.create(JSON, jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/flsw_info_leader")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        Flsw_all_info flsw_info = new Flsw_all_info();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            flsw_info.setFlsw_name(jsonObject.getString("Flsw_name"));
                            flsw_info.setFlsw_type(jsonObject.getString("Flsw_type"));
                            flsw_info.setFlsw_creator(jsonObject.getString("Flsw_creator"));
                            flsw_info.setFlsw_time(jsonObject.getString("Flsw_time"));
                            flsw_info.setFlsw_content(jsonObject.getString("Flsw_content"));
                            flsw_info.setFlsw_id(jsonObject.getString("Flsw_id"));
                            flsw_info.setFlsw_dep(jsonObject.getString("Flsw_dep"));
                            flsw_info.setFlsw_email(jsonObject.getString("Flsw_email"));
                            flsw_info.setFlsw_cellphone(jsonObject.getString("Flsw_cellphone"));
                            flsw_info.setFlsw_remark(jsonObject.getString("Flsw_remark"));
                            flsw_info.setFlsw_record(jsonObject.getString("Flsw_record"));
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = flsw_info;
                            mhandler.sendMessage(message);
                            //System.out.println("message.obj "+message.obj);
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
            flsw_info_ui = (Flsw_all_info) msg.obj;
            final Post_opinion_yn post_opinion_yn = new Post_opinion_yn();
            //将信息显示在UI上
            text_name = findViewById(R.id.textView5);
            text_name.setText(flsw_info_ui.getFlsw_name());
            text_type = findViewById(R.id.textView6);
            text_type.setText(flsw_info_ui.getFlsw_type());
            text_creator = findViewById(R.id.textView8);
            text_creator.setText(flsw_info_ui.getFlsw_creator());
            text_time = findViewById(R.id.textView10);
            text_time.setText(flsw_info_ui.getFlsw_time());
            text_content = findViewById(R.id.textView12);
            text_content.setText(flsw_info_ui.getFlsw_content());
            text_id = findViewById(R.id.textView115);
            text_id.setText(flsw_info_ui.getFlsw_id());
            text_dep = findViewById(R.id.textView116);
            text_dep.setText(flsw_info_ui.getFlsw_dep());
            text_email = findViewById(R.id.textView110);
            text_email.setText(flsw_info_ui.getFlsw_email());
            text_cellphone = findViewById(R.id.textView112);
            text_cellphone.setText(flsw_info_ui.getFlsw_cellphone());
            text_record = findViewById(R.id.editText10);
            text_record.setText(flsw_info_ui.getFlsw_record());
            button_moreinfo = findViewById(R.id.button);
            text_remark = findViewById(R.id.textView16);
            text_remark.setText(flsw_info_ui.getFlsw_remark());
            layout_moreinfo = (LinearLayout) findViewById(R.id.moreinfo);
            button_moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isVisible) {
                        isVisible = true;
                        layout_moreinfo.setVisibility(View.VISIBLE);
                        layout_checkre.setVisibility(View.GONE);
                        isVisible2 = false;
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
                        layout_moreinfo.setVisibility(View.GONE);
                        isVisible = false;
                    } else {
                        layout_checkre.setVisibility(View.GONE);
                        isVisible2 = false;
                    }
                }
            });
            //按钮绑定响应事件
            btn_cir = findViewById(R.id.button4);
            btn_cir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text_op_wen = findViewById(R.id.op_wen_id);
                    String text_op = text_op_wen.getText().toString();
                    String flsw_wen_id = flsw_info_ui.getFlsw_id();
                    wen_op_post(flsw_wen_id,text_op,user_id);
                    Intent intent = new Intent(AF_wen_info.this,todo_af_list.class);
                    startActivity(intent);
                }
            });
        }

        public void wen_op_post(String flsw_wen_id,String op,String creator){
            final Post_wen_op post_wen_op = new Post_wen_op();
            post_wen_op.setFlsw_wen_id(flsw_wen_id);
            post_wen_op.setOp_wen_con(op);
            post_wen_op.setOp_creaotr(creator);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(post_wen_op);
                    RequestBody body = RequestBody.create(JSON, jsonStr);
                    Request request = new Request.Builder()
                            .url("http://192.168.191.1:3000/post_wen_opinion") //路由
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
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
                Intent intent_f = new Intent(AF_wen_info.this,function_b.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(AF_wen_info.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }

    private TextView text_name;//项目名称
    private TextView text_type;//审批单类型
    private TextView text_creator;//审批单发起人
    private TextView text_time;//审批单发起时间
    private TextView text_content;//审批单内容
    private TextView text_id;//委托单编号
    private TextView text_dep; // 委托单发起人部门
    //没有回复时间
    private TextView text_remark;//附件名称
    private TextView text_email; //委托单发起人邮箱
    private TextView text_cellphone; // 委托单发起人电话
    private TextView text_record;//委托单审核记录

    private Button button_moreinfo; /**更多信息按钮**/
    private LinearLayout layout_moreinfo; /**更多信息总布局**/
    private Boolean isVisible = false;
    private Button button_checkre;/**审核记录按钮button3**/
    private LinearLayout layout_checkre;/**审核记录总布局**/
    private Boolean isVisible2 = false;

    private Button btn_cir; //流转按钮
    private EditText text_op_wen;//给文书类审批单的意见
}
