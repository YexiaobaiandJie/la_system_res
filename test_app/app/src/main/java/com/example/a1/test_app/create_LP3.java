package com.example.a1.test_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class create_LP3 extends AppCompatActivity {
    private String userid;
    private String lp_tou = " ";
    private String path="";
    private String uploadFilePath;
    private String uploadFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__lp3);
        userid = Transmitter.userinfo.user_id;

        /**对新建单项法人申请进行操作**/
        btn_submit = findViewById(R.id.button27);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(create_LP3.this,AF_list.class);
                startActivity(intent);
                text_name = findViewById(R.id.EditText1);
                String lp_name = text_name.getText().toString();
                text_type = findViewById(R.id.EditText2);
                String lp_type = text_type.getText().toString();

                String lp_creator =  userid;
                text_user = findViewById(R.id.EditText5);
                String lp_user = text_user.getText().toString();
                text_date = findViewById(R.id.EditText6);
                String lp_date = text_date.getText().toString();
                text_range = findViewById(R.id.EditText8);
                String lp_range = text_range.getText().toString();


                //创建json对象
                Lp_cre_info lp_cre = new Lp_cre_info();
                lp_cre.setLp_name(lp_name);
                lp_cre.setLp_type(lp_type);
                lp_cre.setLp_creator(lp_creator);
                lp_cre.setLp_user(lp_user);
                lp_cre.setLp_date(lp_date);
                lp_cre.setLp_tou(lp_tou);
                lp_cre.setLp_remark(uploadFileName);
                lp_cre.setLp_range(lp_range);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(lp_cre);

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/create_lp")
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
        });


        //选择文件
        btn_ch_file = findViewById(R.id.button33);
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
                    Toast.makeText(create_LP3.this,"Please install a file manager",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //上传文件按钮，
        uploadbtn =  findViewById(R.id.button24);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path.equals("")){
                    Toast.makeText(create_LP3.this,"请先选择文件！",Toast.LENGTH_SHORT).show();
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
                        text_file_name = findViewById(R.id.textView41);
                        text_file_name.setText(file.getName());
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
                Intent intent_f = new Intent(create_LP3.this,function_a.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(create_LP3.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }

    private Button btn_submit; //提交新建单项法人申请
    private Button btn_save; //保存新建单项法人申请
    private EditText text_name; //名称
    private EditText text_type; //类型
    private EditText text_creator;
    private EditText text_user;
    private EditText text_date;//授权期限
    private EditText text_remark;//备注
    private EditText text_range;//授权范围
    private RadioGroup rad_group;//是否投标
    private RadioButton btn_y;//投标选项
    private RadioButton btn_n;//不投标选项
    private Button btn_ch_file;//选择文件按钮
    private Button uploadbtn;//上传文件按钮
    private TextView text_file_name;//文件名称
}
