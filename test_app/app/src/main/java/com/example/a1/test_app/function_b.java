package com.example.a1.test_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class function_b extends AppCompatActivity {

    private String func="b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_b);
        name = findViewById(R.id.user_name);
        name.setText(Transmitter.userinfo.user_name);
        dep = findViewById(R.id.user_dep);
        String dep_num = Transmitter.userinfo.user_dep;
        switch(dep_num){
            case "100":
                dep.setText("后勤部");
                break;
            case "101":
                dep.setText("开发部");
                break;
            case "102":
                dep.setText("营销部");
                break;
            case "103":
                dep.setText("人力资源部");
                break;
            case "104":
                dep.setText("企发部");
                break;
            case "110":
                dep.setText("主管部");
                break;
            case "200":
                dep.setText("经营科");
                break;
            case "201":
                dep.setText("法务科");
                break;
            case "300":
                dep.setText("管理员");
                break;
            case "900":
                dep.setText("测试部");
                break;
        }
        phone = findViewById(R.id.user_cellphone);
        phone.setText(Transmitter.userinfo.user_cellphone);
        c_phone = findViewById(R.id.chg_phone);
        c_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改电话界面
                Intent intent_chg_phone = new Intent(function_b.this,change_phone.class);
                intent_chg_phone.putExtra("func",func);
                startActivity(intent_chg_phone);
            }
        });
        email = findViewById(R.id.user_email);
        email.setText(Transmitter.userinfo.user_email);
        c_email = findViewById(R.id.chg_email);
        c_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改邮箱界面
                Intent intent_chg_email = new Intent(function_b.this,change_email.class);
                intent_chg_email.putExtra("func",func);
                startActivity(intent_chg_email);
            }
        });
        pwd = findViewById(R.id.changepwd);
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至修改密码界面
                Intent intent_chg_pwd = new Intent(function_b.this,change_pwd.class);
                intent_chg_pwd.putExtra("func",func);
                startActivity(intent_chg_pwd);
            }
        });
        /**浏览待审批列表**/
        textview_falist= (TextView) this.findViewById(R.id.textView);
        textview_falist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(function_b.this,todo_af_list.class);
                startActivity(intent);


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

                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(function_b.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }
    private TextView textview_falist;// textView
    private LinearLayout layout_about;//关于我界面
    private TextView name;//用户名
    private TextView dep;//部门
    private TextView phone;//电话
    private TextView email;//邮箱
    private LinearLayout pwd;//密码
    private LinearLayout c_phone;//修改电话
    private LinearLayout c_email;//修改邮箱
}
