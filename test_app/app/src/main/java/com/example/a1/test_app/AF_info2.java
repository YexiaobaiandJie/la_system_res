package com.example.a1.test_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.Button;
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


public class AF_info2 extends AppCompatActivity {
    private String user_id;
    private String flsw_id;
    private Flsw_all_info flsw_info_ui;
    private String tri_result="no";
    private String[] opinions = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_info2);
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
            text_filename = findViewById(R.id.textView16);
            text_filename.setText(flsw_info_ui.getFlsw_remark());
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
            //此处为按钮绑定响应事件
            //点击更多信息按钮显示更多信息
            button_moreinfo = findViewById(R.id.button);
            layout_moreinfo = (LinearLayout) findViewById(R.id.moreinfo);
            button_moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isVisible){
                        isVisible = true;
                        layout_moreinfo.setVisibility(View.VISIBLE);
                        layout_checkre.setVisibility(View.GONE);
                        isVisible2 = false;
                    }else{
                        layout_moreinfo.setVisibility(View.GONE);
                        isVisible = false;
                    }
                }
            });
            //点击审核记录按钮显示审核记录
            button_checkre = findViewById(R.id.button3);
            layout_checkre = (LinearLayout) findViewById(R.id.check_re);
            button_checkre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isVisible2){
                        isVisible2 = true;
                        layout_checkre.setVisibility(View.VISIBLE);
                        layout_moreinfo.setVisibility(View.GONE);
                        isVisible = false;
                    }else{
                        layout_checkre.setVisibility(View.GONE);
                        isVisible2 = false;
                    }
                }
            });
            //点击审批意见添加TextView添加输入框
            btn_add = findViewById(R.id.button22);
            layout_yijian =findViewById(R.id.yijian);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count == 5){
                        Toast.makeText(AF_info2.this,"最多只能生成5条意见",Toast.LENGTH_SHORT).show();
                    }else{

                        EditText add;
                        add = new EditText(AF_info2.this);
                        add.setTextSize(17);

                        add.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                opinions[count-1] = s.toString();
                            }
                        });

                        count++;
                        layout_yijian.addView(add);
                    }



                }
            });
            //点击删除按钮删除最下面一行意见栏

            btn_del = findViewById(R.id.button23);
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count > 0){
                        layout_yijian.removeViewAt(count-1);
                        count--;
                    }else if(count == 0)
                    {
                        //此处应提示用户审批条数至少有一条
                        Toast.makeText(AF_info2.this,"已经没有意见了",Toast.LENGTH_SHORT).show();
                    }


                }
            });

            //交办按钮功能，保存按钮功能





            //判断是否为三重一大
            rad_group = findViewById(R.id.tri_yn);
            btn_yes = findViewById(R.id.btn_yes);
            btn_no = findViewById(R.id.btn_no);
            rad_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if(btn_yes.getId() == checkedId){
                        tri_result = "yes";
                        System.out.println("YES运行，tri_result="+tri_result);
                    }else if(btn_no.getId() == checkedId){
                        tri_result = "no";
                        System.out.println("NO运行 ，tri_result="+tri_result);
                    }
                }
            });

            //流转按钮功能
            btn_cir = findViewById(R.id.button4);
            btn_cir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Opinion_qifa opinion = new Opinion_qifa();
                    opinion.setLa_op_id(flsw_id);
                    opinion.setOp_creator(user_id);
                    opinion.setOp_user(flsw_info_ui.getFlsw_creator());
                    opinion.setOp_exe_yn("none");
                    opinion.setOp_unexe_res("");
                    opinion.setOp_unexe_status("0");

                    // 修改三重一大状态以及审批单信息在另一个api里
                    Tri_post tri_post_info = new Tri_post();
                    tri_post_info.setLa_id(flsw_id);
                    tri_post_info.setLa_qifa_id(user_id);
                    tri_post_info.setLa_tri_yn(tri_result);
                    post_tri(tri_post_info);

                    //提交意见


                    post_result(opinion);
                    Intent intent = new Intent(AF_info2.this,todo_af_list.class);
                    startActivity(intent);
                }

            });


        }
        //用于传递企发部人员对三重一大的的判定
        public void post_tri(Tri_post tri_post_info){
            final Tri_post tri_post_json =tri_post_info;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client2 = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(tri_post_json);
                    RequestBody body = RequestBody.create(JSON, jsonStr);
                    Request request2 = new Request.Builder()
                            .url("http://192.168.191.1:3000/tri_post")
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


        //用于传递企发部人员操作的函数，绑定按钮
        public void post_result(Opinion_qifa opinion) {
            final Opinion_qifa opinion_json = opinion;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String jsonStr;
                    RequestBody body;
                    Request request2;
                    OkHttpClient client2 = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    Gson gson = new Gson();
                    int i;
                    for(i = 0;i<count;i++){
                        opinion_json.setOp_content(opinions[i]);
                        jsonStr = gson.toJson(opinion_json);
                        body = RequestBody.create(JSON, jsonStr);
                        request2 = new Request.Builder()
                                .url("http://192.168.191.1:3000/opinion_post")
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
                Intent intent_f = new Intent(AF_info2.this,function_b.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(AF_info2.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }














    private RadioButton btn_no;
    private RadioButton btn_yes;
    private RadioGroup rad_group;
    private EditText text_op;
    private EditText text_op2;
    private EditText text_op3;
    private EditText text_op4;
    private EditText text_op5;
    private TextView text_name;//项目名称
    private TextView text_type;//审批单类型
    private TextView text_creator;//审批单发起人
    private TextView text_time;//审批单发起时间
    private TextView text_content;//审批单内容
    private TextView text_id;//委托单编号
    private TextView text_dep; // 委托单发起人部门
    //没有回复时间
    private TextView text_filename;//附件名
    private TextView text_email; //委托单发起人邮箱
    private TextView text_cellphone; // 委托单发起人电话
    private TextView text_record;//委托单审核记录
    /**textView14**/
    private TextView textView_add;/**添加审核意见**/
    private Button button_moreinfo; /**更多信息按钮**/
    private LinearLayout layout_moreinfo; /**更多信息总布局**/
    private Boolean isVisible = false;
    private Button button_checkre;/**审核记录按钮button3**/
    private LinearLayout layout_checkre;/**审核记录总布局**/
    private Boolean isVisible2 = false;

    private Button btn_add; /**添加审批信息button22**/
    private Button btn_del; /**删除审批信息button23**/
    private int index = 0; //已有审批意见的条数=index
    private int count = 0; //已有审批意见的条数
    private  LinearLayout layout_yijian;/**容纳审批意见的布局yijian**/
    private Button btn_cir; //流转按钮
}




