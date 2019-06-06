package com.example.a1.test_app;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class AF_list extends AppCompatActivity {

    ArrayList<AF_list_listview> af_list = new ArrayList<>();
    public String userid;
    private Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_list);
        userid = Transmitter.userinfo.user_id;
        initAFs();

        //获取审批单信息
        new Thread(new Runnable() {
            @Override
            public void run() {

                //获得userid

                Creator_list creator = new Creator_list();
                creator.set_list_owner(userid);
                //创建okhttpclient对象
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(creator);

                RequestBody body = RequestBody.create(JSON,jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/flsw_list")
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



    }
    //载入审批单信息
    private void initAFs(){
        final ListView listView = this.findViewById(R.id.list_view);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                af_list = (ArrayList<AF_list_listview>)msg.obj;
                System.out.println("-----------af_list--------------- "+af_list);
                af_list_item_adapter adapter = new af_list_item_adapter(AF_list.this, R.layout.af_list_item,af_list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AF_list_listview af_item = af_list.get(position);
                        //Toast.makeText(AF_list.this,af_item.getid(),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(AF_list.this,af_item.get_opinion_yn(),Toast.LENGTH_SHORT).show();
                        //change 0 表退回  ------ yes 1 表同意-----  no 7表示不同意，审批单进入最终状态
                        switch(af_item.get_opinion_yn()){
                            case "no":
                                switch (af_item.getstatus()){
                                    case "0":
                                        //领导还未审核
                                        Toast.makeText(AF_list.this,"待部门领导审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "7":
                                        //领导审核不同意
                                        Toast.makeText(AF_list.this,"审核不通过,流转结束",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AF_list.this,AF_result2.class);
                                        intent.putExtra("item_id",af_item.getid());
                                        startActivity(intent);
                                        break;
                                    case "7b":
                                        //单项法人申请，领导审核不同意
                                        Intent intent_lp = new Intent(AF_list.this,LP_result2.class);
                                        intent_lp.putExtra("item_id",af_item.getid());
                                        startActivity(intent_lp);
                                        break;

                                }
                                break;
                            case "yes":
                                switch (af_item.getstatus()){
                                    case "1":
                                        //待用户提交至企发部
                                        switch (af_item.getItem_type()){
                                            case "单项投标":
                                                Intent intent_lp = new Intent(AF_list.this,LP_result.class);
                                                intent_lp.putExtra("item_id",af_item.getid());
                                                startActivity(intent_lp);
                                                break;
                                            case "单项不投标":
                                            case "常年法人":
                                                Intent intent_lp_n = new Intent(AF_list.this,LP_result.class);
                                                intent_lp_n.putExtra("item_id",af_item.getid());
                                                startActivity(intent_lp_n);
                                                break;
                                            case "文书类审批单":
                                                Intent intent_wen = new Intent(AF_list.this,AF_result.class);
                                                intent_wen.putExtra("item_id",af_item.getid());
                                                startActivity(intent_wen);
                                                break;
                                            case "审核类审批单":
                                                Intent intent = new Intent(AF_list.this,AF_result.class);
                                                intent.putExtra("item_id",af_item.getid());
                                                startActivity(intent);
                                                break;
                                        }

                                        break;
                                    case "2":
                                        //待企发部人员审核
                                        Toast.makeText(AF_list.this,"待企发部人员审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "2a":
                                        Toast.makeText(AF_list.this,"待企发部人员审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "2b":
                                        Toast.makeText(AF_list.this,"待企发部人员审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "3":
                                        //待用户对每条意见进行执行判定
                                        Intent intent3 = new Intent(AF_list.this,AF_exe.class);
                                        intent3.putExtra("item_id",af_item.getid());
                                        startActivity(intent3);
                                        break;
                                    case "3b":
                                        Toast.makeText(AF_list.this,"待经营科人员审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "4":
                                        //待部门领导对不执行意见进行审核
                                        Toast.makeText(AF_list.this,"等待部门领导对不执行意见进行审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "4b":
                                        Toast.makeText(AF_list.this,"待法务科人员审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "5":
                                        //待总经理对不执行意见进行审核
                                        Toast.makeText(AF_list.this,"等待总经理对不执行意见进行审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "5b":
                                        Toast.makeText(AF_list.this,"待企发部主任审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "6":
                                        //待院长对不执行意见进行审核
                                        Toast.makeText(AF_list.this,"等待院长对不执行意见进行审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "6b":
                                        Toast.makeText(AF_list.this,"待副总经理审核",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "7":
                                        //审核完毕，流转结束
                                        Intent intent_res = new Intent(AF_list.this,Flsw_Res.class);
                                        intent_res.putExtra("item_id",af_item.getid());
                                        startActivity(intent_res);
                                        break;
                                    case "7b":
                                        Toast.makeText(AF_list.this,"待总经理审核",Toast.LENGTH_SHORT).show();
                                    break;
                                    case "9b":
                                        Toast.makeText(AF_list.this,"审核不通过",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "10":
                                        //特殊情况，不是三重一大
                                        Toast.makeText(AF_list.this,"审批单不是三重一大，流转结束",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "7a":
                                        //文书类审批单，流转结束，待查看
                                        Toast.makeText(AF_list.this,"文书类审批单，流转结束",Toast.LENGTH_SHORT).show();
                                        Intent intent_wen_res = new Intent(AF_list.this,AF_result_wen.class);
                                        intent_wen_res.putExtra("item_id",af_item.getid());
                                        startActivity(intent_wen_res);
                                    case "8b":
                                        //单项法人申请，流转结束，同意，待查看
                                        Toast.makeText(AF_list.this,"流转结束",Toast.LENGTH_SHORT).show();
                                        break;

                                }
                                break;
                            case "change":
                                switch(af_item.getstatus()){
                                    case "-1":
                                        switch (af_item.getItem_type()){ //领导审核退回，待修改
                                            case "文书类审批单":
                                                Intent intent_wen = new Intent(AF_list.this,AF_result3.class);
                                                intent_wen.putExtra("item_id",af_item.getid());
                                                Toast.makeText(AF_list.this,"审批单已退回，请按要求修改后重新提交",Toast.LENGTH_SHORT).show();
                                                startActivity(intent_wen);
                                                break;
                                            case "审核类审批单":
                                                Intent intent = new Intent(AF_list.this,AF_result3.class);
                                                intent.putExtra("item_id",af_item.getid());
                                                Toast.makeText(AF_list.this,"审批单已退回，请按要求修改后重新提交",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                break;
                                            case "单项投标":
                                            case "单项不投标":
                                                Intent intent_lp = new Intent(AF_list.this,LP_result3.class);
                                                intent_lp.putExtra("type","单项");
                                                intent_lp.putExtra("item_id",af_item.getid());
                                                startActivity(intent_lp);
                                                break;
                                            case "常年法人":
                                                Intent intent_lp3 = new Intent(AF_list.this,LP_result3.class);
                                                intent_lp3.putExtra("type","常年");
                                                intent_lp3.putExtra("item_id",af_item.getid());
                                                startActivity(intent_lp3);
                                                break;


                                        }
                                        break;
                                }
                                break;

                        }

                    }
                });
            }
        };
        System.out.println("-----------af_list2--------------- "+af_list);

    }
//载入待处理审批单信息

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.back_func:
                //在此界面点击返回功能界面，返回功能界面
                Intent intent_f = new Intent(AF_list.this,function_a.class);
                startActivity(intent_f);
                break;
            case R.id.exit:
                //在登录界面点击注销界面，返回登录界面
                Intent intent_l = new Intent(AF_list.this,FirstAcitivity.class);
                startActivity(intent_l);
                break;
            default:
        }
        return true;
    }


}

















// private TextView text_af_name;/**审批单名称**/
// private TextView text_af_user;/**审批单申请人**/
//private TextView text_af_time;/**审批单提交时间**/
// private TextView text_af_status;/**审批单状态**/


//private TextView text_af_name2;/**审批单名称**/
// private TextView text_af_user2;/**审批单申请人**/
// private TextView text_af_time2;/**审批单提交时间**/
// private TextView text_af_status2;/**审批单状态**/


//private TextView text_af_name3;/**审批单名称**/
// private TextView text_af_user3;/**审批单申请人**/
//private TextView text_af_time3;/**审批单提交时间**/
//private TextView text_af_status3;/**审批单状态**/





/**一条审批信息**同意**/
        /*
        text_af_name = (TextView) this.findViewById(R.id.textView51);
        text_af_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AF_list.this,AF_result.class);
                startActivity(intent);
            }
        });

        text_af_user = (TextView) this.findViewById(R.id.textView52);
        text_af_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AF_list.this,AF_result.class);
                startActivity(intent2);
            }
        });

        text_af_time = (TextView) this.findViewById(R.id.textView53);
        text_af_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AF_list.this,AF_result.class);
                startActivity(intent3);
            }
        });

        text_af_status = (TextView) this.findViewById(R.id.textView50);
        text_af_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(AF_list.this,AF_result.class);
                startActivity(intent4);
            }
        });
*/



/**一条审批信息**不同意**/
        /*
        text_af_name2 = (TextView) this.findViewById(R.id.textView30);
        text_af_name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AF_list.this,AF_result2.class);
                startActivity(intent);
            }
        });

        text_af_user2 = (TextView) this.findViewById(R.id.textView29);
        text_af_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AF_list.this,AF_result2.class);
                startActivity(intent2);
            }
        });

        text_af_time2 = (TextView) this.findViewById(R.id.textView27);
        text_af_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AF_list.this,AF_result2.class);
                startActivity(intent3);
            }
        });

        text_af_status2 = (TextView) this.findViewById(R.id.textView28);
        text_af_status2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(AF_list.this,AF_result2.class);
                startActivity(intent4);
            }
        });
*/


/**一条审批信息**退回**/
        /*
        text_af_name3 = (TextView) this.findViewById(R.id.textView26);
        text_af_name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AF_list.this,AF_result3.class);
                startActivity(intent);
            }
        });

        text_af_user3 = (TextView) this.findViewById(R.id.textView25);
        text_af_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AF_list.this,AF_result3.class);
                startActivity(intent2);
            }
        });

        text_af_time3 = (TextView) this.findViewById(R.id.textView24);
        text_af_time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AF_list.this,AF_result3.class);
                startActivity(intent3);
            }
        });

        text_af_status3 = (TextView) this.findViewById(R.id.textView23);
        text_af_status3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(AF_list.this,AF_result3.class);
                startActivity(intent4);
            }
        });
        */
