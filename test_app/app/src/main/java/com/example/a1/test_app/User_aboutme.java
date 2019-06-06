package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class User_aboutme extends AppCompatActivity {


    private String func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_aboutme);
        Intent receiveIntent  = getIntent();
        func = receiveIntent.getStringExtra("func");
        lay_func = (LinearLayout) this.findViewById(R.id.home);
        lay_func.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(func){
                    case "a":
                        Intent intent_a = new Intent(User_aboutme.this,function_a.class);
                        startActivity(intent_a);
                        break;
                    case "b":
                        Intent intent_b = new Intent(User_aboutme.this,function_b.class);
                        startActivity(intent_b);
                        break;
                    case "admin":
                        Intent intent_ad = new Intent(User_aboutme.this,function_admin.class);
                        startActivity(intent_ad);

                }
            }
        });
        name = findViewById(R.id.user_name);
        name.setText(Transmitter.userinfo.user_name);
        dep = findViewById(R.id.user_dep);
        dep.setText(Transmitter.userinfo.user_dep);
        phone = findViewById(R.id.user_cellphone);
        phone.setText(Transmitter.userinfo.user_cellphone);
        chg_ph = findViewById(R.id.phone);
        chg_ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改电话界面
                Intent intent_chg_phone = new Intent(User_aboutme.this,change_phone.class);
                intent_chg_phone.putExtra("func",func);
                startActivity(intent_chg_phone);
            }
        });

        email = findViewById(R.id.user_email);
        email.setText(Transmitter.userinfo.user_email);
        chgpwd = findViewById(R.id.changepwd);
        chgpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改密码界面
                Intent intent_chg_pwd = new Intent(User_aboutme.this,change_pwd.class);
                intent_chg_pwd.putExtra("func",func);
                startActivity(intent_chg_pwd);

            }
        });
        chg_em = findViewById(R.id.email);
        chg_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改邮箱界面
                Intent intent_chg_email = new Intent(User_aboutme.this,change_email.class);
                intent_chg_email.putExtra("func",func);
                startActivity(intent_chg_email);
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
                switch (func){
                    case "a":
                        Intent intent_f = new Intent(User_aboutme.this,function_a.class);
                        startActivity(intent_f);
                        break;
                    case "b":
                        Intent intent_gf = new Intent(User_aboutme.this,function_b.class);
                        startActivity(intent_gf);
                        break;
                }
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(User_aboutme.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }


    private TextView name; //用户姓名
    private TextView dep;//用户部门
    private TextView phone;//用户电话
    private TextView email;//用户邮箱
    private LinearLayout lay_func;//返回功能界面
    private LinearLayout chgpwd;//修改密码
    private LinearLayout chg_ph;//修改电话
    private LinearLayout chg_em;//修改邮箱


}
