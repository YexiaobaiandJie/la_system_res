package com.example.a1.test_app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class LP_info_over extends AppCompatActivity {
  private String userid;
  private String lp_id;
  private LP_all_info lp_info_ui;
  private String lp_type = "单项投标";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lp_info_over);
    userid = Transmitter.userinfo.user_id;
    Intent receiveIntent  = getIntent();
    lp_id = receiveIntent.getStringExtra("item_id");

    //根据lp_id获得法人申请的数据
    new Thread(new Runnable() {
      @Override
      public void run() {
        Flsw_id lp_id_json = new Flsw_id();
        lp_id_json.setFlsw_id(lp_id);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(lp_id_json);

        RequestBody body = RequestBody.create(JSON,jsonStr);
        Request request = new Request.Builder()
                .url("http://192.168.191.1:3000/get_lp_all_info")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {

          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            final String responseData = response.body().string();
            LP_all_info lp_info = new LP_all_info();
            try {
              JSONObject jsonObject = new JSONObject(responseData);
              lp_info.setLp_id(jsonObject.getString("Lp_id"));
              lp_info.setLp_name(jsonObject.getString("Lp_name"));
              lp_info.setLp_type(jsonObject.getString("Lp_type"));
              lp_info.setLp_tou(jsonObject.getString("Lp_tou"));
              lp_info.setLp_creator(jsonObject.getString("Lp_creator"));
              lp_info.setLp_cre_dep(jsonObject.getString("Lp_cre_dep"));
              lp_info.setLp_use_dep(jsonObject.getString("Lp_use_dep"));
              lp_info.setLp_user(jsonObject.getString("Lp_user"));
              lp_info.setLp_range(jsonObject.getString("Lp_range"));
              lp_info.setLp_date(jsonObject.getString("Lp_date"));
              lp_info.setLp_time(jsonObject.getString("Lp_time"));
              lp_info.setLp_remark(jsonObject.getString("Lp_remark"));
              lp_info.setLp_todo(jsonObject.getString("Lp_todo"));
              lp_info.setLp_node(jsonObject.getString("Lp_node"));
              lp_info.setLp_opinion_yn(jsonObject.getString("Lp_opinion_yn"));
              lp_info.setLp_opinion(jsonObject.getString("Lp_opinion"));
              Message message = Message.obtain();
              message.what = 1;
              message.obj = lp_info;
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


  Handler mhandler = new Handler(){
    @Override
    public void handleMessage(Message msg){
      super.handleMessage(msg);
      final LP_all_info lp_info_ui = (LP_all_info)msg.obj;
      final Post_opinion_yn post_opinion_yn = new Post_opinion_yn();
      //将信息显示在UI上
      text_name = findViewById(R.id.textView5);
      text_name.setText(lp_info_ui.getLp_name());
      text_type = findViewById(R.id.textView6);
      text_type.setText(lp_info_ui.getLp_type());
      text_cre_dep = findViewById(R.id.textView8);
      text_cre_dep.setText(lp_info_ui.getLp_cre_dep());
      text_user = findViewById(R.id.textView10);
      text_user.setText(lp_info_ui.getLp_user());
      text_creator = findViewById(R.id.textView12);
      text_creator.setText(lp_info_ui.getLp_creator());
      text_tou = findViewById(R.id.textView48);
      text_tou.setText(lp_info_ui.getLp_tou());
      text_id = findViewById(R.id.textView115);
      text_id.setText(lp_info_ui.getLp_id());
      text_range = findViewById(R.id.textView116);
      text_range.setText(lp_info_ui.getLp_range());
      text_date = findViewById(R.id.textView118);
      text_date.setText(lp_info_ui.getLp_date());
      text_remark = findViewById(R.id.textView15);
      text_remark.setText(lp_info_ui.getLp_remark());
      text_use_dep = findViewById(R.id.textView112);
      text_use_dep.setText(lp_info_ui.getLp_use_dep());
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

      //总经理同意
      btn_y = findViewById(R.id.button19);
      btn_y.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(LP_info_over.this,circlate_cre.class);
          intent.putExtra("result","yes");
          intent.putExtra("item_id",lp_id);
          intent.putExtra("lp_type",lp_type);
          startActivity(intent);
        }
      });
      //总经理不同意
      btn_n = findViewById(R.id.button20);
      btn_n.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent2 = new Intent(LP_info_over.this,circulate_back.class);
          intent2.putExtra("result","no");
          intent2.putExtra("item_id",lp_id);
          String last="z";
          intent2.putExtra("last_view",last);
          intent2.putExtra("lp_type",lp_type);
          startActivity(intent2);
        }
      });

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
        Intent intent_f = new Intent(LP_info_over.this,function_b.class);
        startActivity(intent_f);
        break;
      case R.id.exit:
        //在登录界面点击注销界面，返回登录界面
        Intent intent_l = new Intent(LP_info_over.this,FirstAcitivity.class);
        startActivity(intent_l);
        break;
      default:
    }
    return true;
  }









  private Button button_moreinfo; /**更多信息按钮**/
  private LinearLayout layout_moreinfo; /**更多信息总布局**/
  private Boolean isVisible = false;

  private Button btn_accept;   //button30
  private Button btn_disaccept; // button29
  private Button btn_change; //button28
  private TextView text_name; //textView5
  private TextView text_type; //textView6
  private TextView text_cre_dep; //textView8
  private TextView text_user; //textView10
  private TextView text_creator; //textView12
  private TextView text_tou;//textView48
  private TextView text_id; //textView115
  private TextView text_range; //textView116
  private TextView text_date; //textView118
  private TextView text_remark; //textView110
  private TextView text_use_dep; //textView112
  ///////////////////////////////////////////////
  private EditText edit_op; //edit_op

  private Button btn_y;  //button11经营科同意按钮
  private Button btn_n;  //button12经营科不同意按钮
}



