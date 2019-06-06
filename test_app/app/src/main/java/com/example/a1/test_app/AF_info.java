package com.example.a1.test_app;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class AF_info extends AppCompatActivity {
    private String user_id;
    private String flsw_id;
    private Flsw_all_info flsw_info_ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_info);
        Intent receiveIntent  = getIntent();
        flsw_id = receiveIntent.getStringExtra("item_id");
        user_id = Transmitter.userinfo.user_id;

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




    // private void init() {

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
            text_remark =findViewById(R.id.textView16);
            text_remark.setText(flsw_info_ui.getFlsw_remark());
            button_moreinfo = findViewById(R.id.button);
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

            btn_accept = findViewById(R.id.button5);
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text_opinion = findViewById(R.id.editText2);
                    String opinion = text_opinion.getText().toString();
                    if(opinion.equals("")){
                        Toast.makeText(AF_info.this,"审阅意见不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        post_opinion_yn.set_flswid(flsw_info_ui.getFlsw_id());
                        post_opinion_yn.set_userid(Transmitter.userinfo.user_id);
                        post_opinion_yn.set_opinion(opinion);
                        post_opinion_yn.set_yn("yes");
                        post_result(post_opinion_yn);
                        Toast.makeText(AF_info.this,"审阅成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AF_info.this,todo_af_list.class);
                        startActivity(intent);
                    }
                }
            });
            btn_disacc = findViewById(R.id.button6);
            btn_disacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text_opinion = findViewById(R.id.editText2);
                    String opinion = text_opinion.getText().toString();
                    if(opinion.equals("")){
                        Toast.makeText(AF_info.this,"审阅意见不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        post_opinion_yn.set_flswid(flsw_info_ui.getFlsw_id());
                        post_opinion_yn.set_userid(Transmitter.userinfo.user_id);
                        post_opinion_yn.set_opinion(opinion);
                        post_opinion_yn.set_yn("no");
                        post_result(post_opinion_yn);
                        Toast.makeText(AF_info.this, "审阅成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AF_info.this, todo_af_list.class);
                        startActivity(intent);
                    }
                }
            });
            btn_repush = findViewById(R.id.button4);
            btn_repush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text_opinion = findViewById(R.id.editText2);
                    String opinion = text_opinion.getText().toString();
                    if(opinion.equals("")){
                        Toast.makeText(AF_info.this,"审阅意见不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        post_opinion_yn.set_flswid(flsw_info_ui.getFlsw_id());
                        post_opinion_yn.set_userid(Transmitter.userinfo.user_id);
                        post_opinion_yn.set_opinion(opinion);
                        post_opinion_yn.set_yn("change");
                        post_result(post_opinion_yn);
                        Toast.makeText(AF_info.this, "审阅成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AF_info.this, todo_af_list.class);
                        startActivity(intent);
                    }
                }
            });

            //下载附件

            filename = findViewById(R.id.textView16);
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
                        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
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

        }




        public void post_result(Post_opinion_yn post_info) {
            final Post_opinion_yn post_info_json = post_info;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client2 = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(post_info_json);
                    RequestBody body = RequestBody.create(JSON, jsonStr);
                    Request request2 = new Request.Builder()
                            .url("http://192.168.191.1:3000/flsw_opinion_yn")
                            .post(body)
                            .build();
                    client2.newCall(request2).enqueue(new okhttp3.Callback() {
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

    //   }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.back_func:
                //在此界面点击返回功能界面，返回功能界面
                Intent intent_f = new Intent(AF_info.this,function_b.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(AF_info.this,FirstAcitivity.class);
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
    private TextView text_email; //委托单发起人邮箱
    private TextView text_cellphone; // 委托单发起人电话
    private TextView text_record;//委托单审核记录
    private TextView text_remark;//委托单附件
    private Button button_moreinfo; /**更多信息按钮**/
    private LinearLayout layout_moreinfo; /**更多信息总布局**/
    private Boolean isVisible = false;
    private Button button_checkre;/**审核记录按钮button3**/
    private LinearLayout layout_checkre;/**审核记录总布局**/
    private Boolean isVisible2 = false;
    private TextView filename;
    private Button btn_accept;//同意按钮
    private Button btn_disacc;//不同意按钮
    private Button btn_repush;//退回按钮
    private TextView text_opinion; // 领导的意见


}
//进行网络请求，获得信息
//{"flsw_id":"3231231",
// "user_id":"129"    //用户id
// }
//{
//  "flsw_name":"项目1",
//  "flsw_type":"审批类型审批单",
//  "flsw_creator":"王某",
//  "flsw_time":"2017-01-02",
//  "flsw_content":"这里是审批单的内容",
////以下为更多信息里的内容
//  "flsw_id":"2312312312312", //（即flswid）
//  "flsw_dep":"104",
//  "flsw_email":"1124@qq.com",
//  "flsw_cellphone":"12542562359",
//  "flsw_remark":"这里是备注"
//  "flsw_record":"这里是审核记录"
//  "flsw_opinions":[
//      {
//          "opinion_creator":"企发部小王",
//          "opinion_content":"意见内容",
//          "opinion_user":"意见执行者--王某",
//          "opinion_exe_yn":"no",
//          "opinion_unexe_res":"意见不执行的理由"
//      }
//
// ]
// }