package com.example.a1.test_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class AF_exe extends AppCompatActivity {
  private String user_id;
  private String flsw_id;
  private Flsw_all_info flsw_info_ui;

  ArrayList<Opinion_info_short> op_list = new ArrayList<>();
  private Handler handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_af_exe);
    Intent receiveIntent = getIntent();
    flsw_id = receiveIntent.getStringExtra("item_id");
    user_id = Transmitter.userinfo.user_id;
    initOPs();
    new Thread(new Runnable() {
      @Override
      public void run() {

        //获得审批单信息
        Get_flsw_info_json info_json = new Get_flsw_info_json(flsw_id, user_id);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(info_json);

        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://192.168.191.1:3000/flsw_info_result")
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
              flsw_info.setFlsw_opinion(jsonObject.getString("Flsw_opinion"));
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
        //-----------------------------获得审批单意见信息------------
        Flsw_id flsw_id_json = new Flsw_id();
        flsw_id_json.setFlsw_id(flsw_id);
        String jsonStr2 = gson.toJson(flsw_id_json);
        RequestBody body2 = RequestBody.create(JSON,jsonStr2);
        Request request2 = new Request.Builder()
                .url("http://192.168.191.1:3000/get_opinion")
                .post(body2)
                .build();
        client.newCall(request2).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {

          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            final String responseData = response.body().string();
            try{
              JSONObject jsonObject = new JSONObject(responseData);
              String Op_info = jsonObject.getString("Opinion_info");
              int count = jsonObject.getInt("Opinion_count");
              JsonParser parser = new JsonParser();
              JsonArray jsonArray = parser.parse(Op_info).getAsJsonArray();
              Gson gson2 = new Gson();
              ArrayList<Opinion_info_short> op_list_bean = new ArrayList<>();
              for(JsonElement op :jsonArray){
                Opinion_info_short op_listview_bean = gson2.fromJson(op,Opinion_info_short.class);
                op_list_bean.add(op_listview_bean);
              }
              Message message2 = Message.obtain();
              message2.obj = op_list_bean;
              handler.sendMessage(message2);
              System.out.println("Op_info = "+Op_info);
            }catch(JSONException e){
              e.printStackTrace();
            }
          }
        });


      }
    }).start();

//下载附件

    filename = findViewById(R.id.textView1112);
    filename.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String downloadname=filename.getText().toString();
        final String url="http://192.168.191.1:3000/post_download_file?filename="+downloadname;
        new Thread(new Runnable() {
          @Override
          public void run() {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
              @Override
              public void onFailure(Call call, IOException e) {

              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                DownloadUtil.createFileWithByte(bytes,downloadname);
              }
            });

          }
        }).start();
      }
    });






  }

  //载入审批单信息
  Handler mhandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      flsw_info_ui = (Flsw_all_info) msg.obj;
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
      text_remark = findViewById(R.id.textView1112);
      text_remark.setText(flsw_info_ui.getFlsw_remark());

      text_record = findViewById(R.id.editText10);
      text_record.setText(flsw_info_ui.getFlsw_record());

      button_moreinfo = findViewById(R.id.button);
      layout_moreinfo = (LinearLayout) findViewById(R.id.moreinfo);
      button_moreinfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!isVisible) {
            isVisible = true;
            layout_moreinfo.setVisibility(View.VISIBLE);
            layout_checkre.setVisibility(View.GONE);
            isVisible2 = false;
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
            layout_moreinfo.setVisibility(View.GONE);
            isVisible = false;
          } else {
            layout_checkre.setVisibility(View.GONE);
            isVisible2 = false;
          }
        }
      });






    }
  };

  private void initOPs(){
    final ListView op_listview = this.findViewById(R.id.op_listview);
    handler = new Handler(){
      @Override
      public void handleMessage(Message msg){
        super.handleMessage(msg);
        op_list = (ArrayList<Opinion_info_short>)msg.obj;

        opinion_list_item_adapter adapter = new opinion_list_item_adapter(AF_exe.this,R.layout.opinion_list_item,op_list);
        op_listview.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(op_listview);

        //点击流转按钮流转
        btn_cir = findViewById(R.id.button25);
        btn_cir.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            new Thread(new Runnable() {
              @Override
              public void run() {
                Flsw_id flsw_id_json = new Flsw_id();
                flsw_id_json.setFlsw_id(flsw_id);
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(flsw_id_json);

                RequestBody body = RequestBody.create(JSON, jsonStr);
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:3000/check_la_op")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                  @Override
                  public void onFailure(Call call, IOException e) {

                  }

                  @Override
                  public void onResponse(Call call, Response response) throws IOException {

                  }
                });
              }
            }).start();
                  Intent intent = new Intent(AF_exe.this,AF_list.class);
                  startActivity(intent);
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
          Intent intent_f = new Intent(AF_exe.this,function_a.class);
          startActivity(intent_f);
        break;
      case R.id.exit:
        //在登录界面点击注销界面，返回登录界面
          Intent intent_l = new Intent(AF_exe.this,FirstAcitivity.class);
          startActivity(intent_l);
        break;
      default:
    }
    return true;
  }
 // private ListView op_listview;
  private TextView text_name;
  private TextView text_type;
  private TextView text_creator;
  private TextView text_time;
  private TextView text_content;
  private TextView text_id;
  private TextView text_dep;
  private TextView text_email;
  private TextView text_cellphone;
  private TextView text_remark;
  private TextView text_record;
  //private TextView text_opinion;
  private Button button_moreinfo; /**更多信息按钮**/
  private LinearLayout layout_moreinfo; /**更多信息总布局**/
  private Boolean isVisible = false;
  private Button button_checkre;/**审核记录按钮button3**/
  private LinearLayout layout_checkre;/**审核记录总布局**/
  private Boolean isVisible2 = false;
  /**button5 流转按钮**/
  private Button btn_cir;
  private TextView filename;
}
