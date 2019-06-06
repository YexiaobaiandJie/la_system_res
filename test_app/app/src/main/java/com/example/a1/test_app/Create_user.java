package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Create_user extends AppCompatActivity {
    private String username;
    private String userpwd;
    private String userposition;
    private String userdep;
    private String usersex ="男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editname = findViewById(R.id.username);
        editpwd = findViewById(R.id.userpwd);
        //获得权限选项
        sp_pos = (Spinner) findViewById(R.id.spinner_pos);
        sp_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] poss = getResources().getStringArray(R.array.spinner_pos);
                userposition = poss[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //获得部门选项
        sp_dep = (Spinner) findViewById(R.id.spinner_dep);
        sp_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] posd = getResources().getStringArray(R.array.spinner_dep);
                userdep = posd[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //监听性别选项变化
        rad_group = findViewById(R.id.rad_sex);
        btn_male = findViewById(R.id.btn_male);
        btn_female = findViewById(R.id.btn_female);
        rad_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(btn_male.getId() == checkedId){
                    usersex = "男";
                }else if(btn_female.getId() == checkedId){
                    usersex = "女";
                }
            }
        });


        btn_cre = findViewById(R.id.btn_cre);
        btn_cre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editname.getText().toString();
                userpwd = editpwd.getText().toString();
                if(userpwd.equals("") || username.equals("")){
                    Toast.makeText(Create_user.this,"有必要项为空，请检查",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            OkHttpClient client = new OkHttpClient();
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            Cre_user creinfo = new Cre_user();

                            creinfo.setUserpwd(userpwd);
                            creinfo.setUserposition(userposition);
                            creinfo.setUserdep(userdep);
                            creinfo.setUsername(username);
                            creinfo.setUsersex(usersex);
                            Gson gson = new Gson();
                            String user_json = gson.toJson(creinfo);
                            RequestBody body = RequestBody.create(JSON,user_json);
                            Request request = new Request.Builder()
                                    .url("http://192.168.191.1:3000/admin_cre_user")
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
                    Intent intent_cre_back = new Intent(Create_user.this,function_admin.class);
                    startActivity(intent_cre_back);
                }


            }
        });


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
                Intent intent_f = new Intent(Create_user.this,function_admin.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(Create_user.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }



    private EditText editname;//新建用户名
    private EditText editpwd; //新建用户密码
    private EditText editposition;//新建用户权限
    private EditText editdep; //新建用户部门
    private Button btn_cre;//提交新建用户 信息
    private Spinner sp_pos;//权限
    private Spinner sp_dep;//部门
    private RadioGroup rad_group; //性别
    private RadioButton btn_male;//男
    private RadioButton btn_female;//女
}