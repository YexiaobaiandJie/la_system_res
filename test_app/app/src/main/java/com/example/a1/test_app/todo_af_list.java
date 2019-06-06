package com.example.a1.test_app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class todo_af_list extends AppCompatActivity {

    ArrayList<AF_list_listview> af_list = new ArrayList<>();
    public String userid;
    public int userposition;
    public Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_af_list);
        initAFs();

        //获取审批单信息
        new Thread(new Runnable() {
            @Override
            public void run() {

                //获得userid
                userid = Transmitter.userinfo.user_id;
                Creator_list creator = new Creator_list();
                creator.set_list_owner(userid);
                System.out.println("----------------userid ="+creator.get_list_owner());
                //创建okhttpclient对象
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(creator);

                RequestBody body = RequestBody.create(JSON,jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/flsw_todo_list")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //拿到json,并转化成String
                        final String responseData = response.body().string();

                        //Json的解析类对象
                        JsonParser parser = new JsonParser();
                        //将Json的string转换成一个JsonArray对象
                        JsonArray jsonArray = parser.parse(responseData).getAsJsonArray();
                        Gson gson = new Gson();
                        ArrayList<AF_list_listview> af_list_bean = new ArrayList<>();

                        for(JsonElement af :jsonArray){
                            //使用gson ,转换成bean对象
                            AF_list_listview af_listview_bean = gson.fromJson(af,AF_list_listview.class);
                            af_list_bean.add(af_listview_bean);
                        }
                        Message message = Message.obtain();
                        message.obj = af_list_bean;
                        handler.sendMessage(message);


                    }
                });
            }
        }).start();







/*
        text_af_status = (TextView) this.findViewById(R.id.textView28);
        text_af_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(todo_af_list.this, AF_info.class);
                startActivity(intent4);
            }

        });
  */

    }
    private void initAFs(){
        final ListView listView2 = this.findViewById(R.id.list_view_todo);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                af_list = (ArrayList<AF_list_listview>)msg.obj;
                System.out.println("-----------af_list--------------- "+af_list);
                af_list_item_adapter adapter = new af_list_item_adapter(todo_af_list.this, R.layout.af_list_item,af_list);
                listView2.setAdapter(adapter);
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AF_list_listview af_item = af_list.get(position);
                        //String item_status = af_item.getstatus();
                        //Toast.makeText(todo_af_list.this,af_item.getid(),Toast.LENGTH_SHORT).show();
                        //获取被点击item的id,进入af_info页面
                        //首先对用户权限进行判定，
                        //1 普通部门普通员工 2 企发部普通员工 3 部门领导/企发部主任 4总经理 5院长
                        // no 0 或者 yes 4 此时部门领导审核
                        userposition = Transmitter.userinfo.user_position;
                        String itemtype = af_item.getItem_type();
                        switch(userposition){
                            case 2:
                                //企发部员工界面
                                switch (itemtype){
                                    case "审核类审批单":
                                        Intent intent_q = new Intent(todo_af_list.this,AF_info2.class);
                                        intent_q.putExtra("item_id",af_item.getid());
                                        startActivity(intent_q);
                                        break;
                                    case "文书类审批单":
                                        Intent intent_wen = new Intent(todo_af_list.this,AF_wen_info.class);
                                        intent_wen.putExtra("item_id",af_item.getid());
                                        intent_wen.putExtra("user_id",userid);
                                        startActivity(intent_wen);
                                        //Toast.makeText(todo_af_list.this,"现在应该跳转去文书页面",Toast.LENGTH_SHORT ).show();
                                        break;
                                    case "单项投标":
                                        //这里是单项法人申请投标的操作页面
                                        Intent intent_lp = new Intent(todo_af_list.this,LP_info.class);
                                        intent_lp.putExtra("item_id",af_item.getid());
                                        startActivity(intent_lp);
                                        break;
                                    case "单项不投标":
                                    case "常年法人":
                                        //这里是单项法人申请不投标的操作页面
                                        Intent intent_lp_n = new Intent(todo_af_list.this,LP_info_n.class);
                                        intent_lp_n.putExtra("item_id",af_item.getid());
                                        startActivity(intent_lp_n);
                                        break;

                                }
                                break;
                            case 3:
                                //部门领导

                                switch(af_item.getItem_type()){
                                    case "审核类审批单":
                                        //审核类审批单操作
                                        switch (af_item.getstatus()){
                                            case "4"://审批单经过员工执行判断，存在不执行情况，需领导审核
                                                Intent intent_f = new Intent(todo_af_list.this,AF_info_next.class);
                                                intent_f.putExtra("item_id",af_item.getid());
                                                startActivity(intent_f);
                                                break;
                                            case "0"://审批单刚提交或修改后再次提交
                                                Intent intent = new Intent(todo_af_list.this,AF_info.class);
                                                intent.putExtra("item_id",af_item.getid());
                                                startActivity(intent);
                                                break;
                                        }

                                        break;
                                    case "文书类审批单":
                                        //文书类审批单操作
                                        Intent intent_wen = new Intent(todo_af_list.this,AF_info.class);
                                        intent_wen.putExtra("item_id",af_item.getid());
                                        startActivity(intent_wen);
                                        break;
                                    case "单项投标":
                                        //这里是单项法人申请投标的操作
                                        //Toast.makeText(todo_af_list.this,"现在应该转到lp信息界面",Toast.LENGTH_SHORT).show();
                                        Intent intent_lp = new Intent(todo_af_list.this,LP_pre_info.class);
                                        intent_lp.putExtra("item_id",af_item.getid());
                                        startActivity(intent_lp);
                                        break;
                                    case "单项不投标":
                                    case "常年法人":
                                        Intent intent_lp_n = new Intent(todo_af_list.this,LP_pre_info.class);
                                        intent_lp_n.putExtra("item_id",af_item.getid());
                                        startActivity(intent_lp_n);
                                        break;
                                }

                                break;
                            case 4:
                                //经营科界面
                                //默认为单项法人申请
                                Intent intent_jy = new Intent(todo_af_list.this,LP_info_jy.class);
                                intent_jy.putExtra("item_id",af_item.getid());
                                startActivity(intent_jy);
                                break;
                            case 5:
                                //法务科界面
                                Intent intent_fw = new Intent(todo_af_list.this,LP_info_fw.class);
                                intent_fw.putExtra("item_id",af_item.getid());
                                startActivity(intent_fw);
                                break;
                            case 11://企发部主任界面
                                switch(af_item.getItem_type()){
                                    case "单项投标":
                                        Intent intent_qf_m = new Intent(todo_af_list.this,LP_info_qf_m.class);
                                        intent_qf_m.putExtra("item_id",af_item.getid());
                                        startActivity(intent_qf_m);
                                        break;
                                    case "单项不投标":
                                    case "常年法人":
                                        Intent intent_qf_m_n = new Intent(todo_af_list.this,LP_info_n_qf_m.class);
                                        intent_qf_m_n.putExtra("item_id",af_item.getid());
                                        startActivity(intent_qf_m_n);
                                        break;
                                }
                                break;
                            case 12:
                                //分管副总经理界面
                                switch (af_item.getItem_type()){
                                    case "单项投标":
                                        Intent intent_fg = new Intent(todo_af_list.this,LP_info_fg.class);
                                        intent_fg.putExtra("item_id",af_item.getid());
                                        startActivity(intent_fg);
                                        break;
                                    case "单项不投标":
                                    case "常年法人":
                                        Intent intent_n_fg = new Intent(todo_af_list.this,LP_info_n_fg.class);
                                        intent_n_fg.putExtra("item_id",af_item.getid());
                                        startActivity(intent_n_fg);
                                        break;
                                }
                                break;
                            case 13:
                                //总经理界面
                                switch (af_item.getItem_type()){
                                    case "单项投标":
                                        Intent intent_z = new Intent(todo_af_list.this,LP_info_over.class);
                                        intent_z.putExtra("item_id",af_item.getid());
                                        startActivity(intent_z);
                                        break;
                                    case "单项不投标":
                                    case "常年法人":
                                        Intent intent_o = new Intent(todo_af_list.this,LP_info_n_over.class);
                                        intent_o.putExtra("item_id",af_item.getid());
                                        startActivity(intent_o);
                                        break;
                                    case "审核类审批单":
                                        Intent intent_s = new Intent(todo_af_list.this,AF_info_next.class);
                                        intent_s.putExtra("item_id",af_item.getid());
                                        startActivity(intent_s);
                                        break;
                                }
                                break;
                            case 14:
                                //院长界面
                                switch(af_item.getItem_type()){
                                    case "审核类审批单":
                                        Intent intent_y = new Intent(todo_af_list.this,AF_info_next.class);
                                        intent_y.putExtra("item_id",af_item.getid());
                                        startActivity(intent_y);
                                        break;
                                }
                        }
                    }
                });
            }
        };


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
                Intent intent_f = new Intent(todo_af_list.this,function_b.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(todo_af_list.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }
}






/**审核类审批单审核界面
 text_af_name = (TextView) this.findViewById(R.id.textView30);
 text_af_name.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(todo_af_list.this, AF_exe.class);
startActivity(intent);
}
});

 text_af_user = (TextView) this.findViewById(R.id.textView29);
 text_af_user.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent2 = new Intent(todo_af_list.this, AF_info.class);
startActivity(intent2);
}
});

 text_af_time = (TextView) this.findViewById(R.id.textView27);
 text_af_time.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent3 = new Intent(todo_af_list.this, AF_info.class);
startActivity(intent3);
}
});
 **/




/**文书类审批单审核界面
 text_af_name2 = (TextView) this.findViewById(R.id.textView26);
 text_af_name2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(todo_af_list.this,AF_info.class);
startActivity(intent);
}
});

 text_af_user2 = (TextView) this.findViewById(R.id.textView25);
 text_af_user2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent2 = new Intent(todo_af_list.this,AF_info.class);
startActivity(intent2);
}
});

 text_af_time2 = (TextView) this.findViewById(R.id.textView24);
 text_af_time2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent3 = new Intent(todo_af_list.this,AF_info.class);
startActivity(intent3);
}
});

 text_af_status2 = (TextView) this.findViewById(R.id.textView23);
 text_af_status2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent4 = new Intent(todo_af_list.this,AF_info.class);
startActivity(intent4);
}
});
 **/
/**单项投标委托审核界面
 text_af_name3 = (TextView) this.findViewById(R.id.textView51);
 text_af_name3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(todo_af_list.this,LP_info.class);
startActivity(intent);
}
});

 text_af_user3 = (TextView) this.findViewById(R.id.textView52);
 text_af_user3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent2 = new Intent(todo_af_list.this,LP_info.class);
startActivity(intent2);
}
});

 text_af_time3 = (TextView) this.findViewById(R.id.textView53);
 text_af_time3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent3 = new Intent(todo_af_list.this,LP_info.class);
startActivity(intent3);
}
});

 text_af_status3 = (TextView) this.findViewById(R.id.textView50);
 text_af_status3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent4 = new Intent(todo_af_list.this,LP_info.class);
startActivity(intent4);
}
});
 **/
/**单项不投标审核界面
 text_af_name4 = (TextView) this.findViewById(R.id.textView63);
 text_af_name4.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(todo_af_list.this,LP_info2.class);
startActivity(intent);
}
});

 text_af_user4 = (TextView) this.findViewById(R.id.textView65);
 text_af_user4.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent2 = new Intent(todo_af_list.this,LP_info2.class);
startActivity(intent2);
}
});

 text_af_time4 = (TextView) this.findViewById(R.id.textView64);
 text_af_time4.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent3 = new Intent(todo_af_list.this,LP_info2.class);
startActivity(intent3);
}
});

 text_af_status4 = (TextView) this.findViewById(R.id.textView62);
 text_af_status4.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent4 = new Intent(todo_af_list.this,LP_info2.class);
startActivity(intent4);
}
});
 **/
/**常年法人委托审核界面
 text_af_name5 = (TextView) this.findViewById(R.id.textView163);
 text_af_name5.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(todo_af_list.this,LP_info3.class);
startActivity(intent);
}
});

 text_af_user5 = (TextView) this.findViewById(R.id.textView165);
 text_af_user5.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent2 = new Intent(todo_af_list.this,LP_info3.class);
startActivity(intent2);
}
});

 text_af_time5 = (TextView) this.findViewById(R.id.textView164);
 text_af_time5.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent3 = new Intent(todo_af_list.this,LP_info3.class);
startActivity(intent3);
}
});

 text_af_status5 = (TextView) this.findViewById(R.id.textView162);
 text_af_status5.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent4 = new Intent(todo_af_list.this,LP_info3.class);
startActivity(intent4);
}
});
 **/





