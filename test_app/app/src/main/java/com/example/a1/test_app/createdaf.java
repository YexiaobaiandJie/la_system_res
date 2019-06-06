package com.example.a1.test_app;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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

public class createdaf extends AppCompatActivity {
    //用于接收审批单信息的变量
    private EditText editname; //editText4  项目名称
    private EditText edittype; //editText5  项目类型
    private EditText editcontent; //editText7 项目内容
    private String project_name;
    private String project_type;
    private String project_creator;
    private String project_content;
    private String uploadFilePath;
    private String uploadFileName;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Toast.makeText(createdaf.this,"创建成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(createdaf.this,"创建失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String path="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_af_wen);

        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        editname = findViewById(R.id.editText4);
                        edittype = findViewById(R.id.editText5);
                        editcontent = findViewById(R.id.editText7);
                        project_name = editname.getText().toString();
                        project_type = edittype.getText().toString();
                        project_content = editcontent.getText().toString();
                        //创建okhttpclient对象
                        OkHttpClient client = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        AF_short afinfo = new AF_short();
                        afinfo.setname(project_name);
                        afinfo.settype(project_type);
                        afinfo.setFile_name(uploadFileName);
                        //获得发起人
                        User userinfo = Transmitter.userinfo;
                        afinfo.setcreator(userinfo.user_id);
                        afinfo.setcontent(project_content);
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




                Intent intent = new Intent(createdaf.this,AF_list.class);
                startActivity(intent);
                /**添加悬浮气泡提示创建成功或失败**/
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
                    Toast.makeText(createdaf.this,"Please install a file manager",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //上传文件按钮，
        uploadbtn =  findViewById(R.id.btn_upload);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path.equals("")){
                    Toast.makeText(createdaf.this,"请先选择文件！",Toast.LENGTH_SHORT).show();
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
                        text_filename = findViewById(R.id.textView5);
                        text_filename.setText(file.getName());
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
                Intent intent_f = new Intent(createdaf.this,function_a.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(createdaf.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }


    /**为提交新建审批单绑定事件**/
    private Button button9; /**提交新建审批单按钮**/
    private Button button8; /**保存新建审批单按钮**/
    private Button btn_ch_file;//选择文件
    private Button uploadbtn;//上传文件
    private TextView text_filename;//textView5
}
