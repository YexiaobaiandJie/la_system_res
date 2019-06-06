package com.example.a1.test_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
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

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class change_flsw extends AppCompatActivity {
    private String name;
    private String content;
    private String remark;
    private String type;
    private String uploadFilePath;
    private String uploadFileName;
    private String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_flsw);
        Intent receiveIntent  = getIntent();
        name = receiveIntent.getStringExtra("flsw_name");
        content = receiveIntent.getStringExtra("flsw_content");
        type = receiveIntent.getStringExtra("flsw_type");
        remark = receiveIntent.getStringExtra("flsw_remark");
        text_name = findViewById(R.id.editText4);
        text_name.setText(name);
        text_type = findViewById(R.id.editText5);
        text_type.setText(type);
        text_content = findViewById(R.id.editText7);
        text_content.setText(content);
        text_remark = findViewById(R.id.textView5);
        text_remark.setText(remark);
        //提交按钮
        btn_sub = findViewById(R.id.button9);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String project_name = text_name.getText().toString();
                        String project_type = text_type.getText().toString();
                        String project_content = text_content.getText().toString();
                        //创建okhttpclient对象
                        OkHttpClient client = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        AF_short afinfo = new AF_short();
                        afinfo.setname(project_name);
                        afinfo.settype(project_type);
                        //获得发起人
                        User userinfo = Transmitter.userinfo;
                        afinfo.setcreator(userinfo.user_id);
                        afinfo.setcontent(project_content);
                        afinfo.setFile_name(uploadFileName);
                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(afinfo);

                        RequestBody body = RequestBody.create(JSON,jsonStr);
                        Request request = new Request.Builder()
                                .url("http://192.168.191.1:3000/create_af")
                                .post(body)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Message message = Message.obtain();
                                if(response.isSuccessful()){
                                    message.what = 1;
                                }else{
                                    message.what = 2;
                                }
                            }
                        });


                    }
                }).start();




                Intent intent = new Intent(change_flsw.this,AF_list.class);
                startActivity(intent);
            }
        });
        //选择文件按钮
        btn_ch_file = findViewById(R.id.choose_file);
        btn_ch_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try{
                    //startActivityForResult(intent,1);
                    startActivityForResult(Intent.createChooser(intent,"Select a file to upload"),0);
                }catch(android.content.ActivityNotFoundException ex){
                    Toast.makeText(change_flsw.this,"Please install a file manager",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //上传文件按钮，

        uploadbtn =  findViewById(R.id.btn_upload);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path.equals("")){
                    Toast.makeText(change_flsw.this,"请先选择文件！",Toast.LENGTH_SHORT).show();
                }else{
                    uploadFilePath = path;
                    File file = new File(path);
                    uploadFileName = file.getName();
                    try {
                        Upload_file.upload(uploadFilePath,uploadFileName);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //回调代码
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    System.out.println("file uri=" + uri.toString());
                    try {
                        path = Upload_file.getPath(this, uri);
                        File file = new File(path);
                        text_remark = findViewById(R.id.textView5);
                        text_remark.setText(file.getName());
                        file.getName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                Intent intent_f = new Intent(change_flsw.this,function_a.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(change_flsw.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }


    private EditText text_name;//editText4
    private EditText text_type;//editText5
    private EditText text_content;//editText7
    private TextView text_remark;//textView5
    private Button btn_sub;//button9
    private Button uploadbtn; //btn_upload 上传文件
    private Button btn_ch_file;//choose_file


}
