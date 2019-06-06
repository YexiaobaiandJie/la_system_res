package com.example.a1.test_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class opinion_list_item_adapter_leader extends ArrayAdapter<Opinion_info_short> {
  private int resourcedId;
  private Button btn_unexe;
  private Button btn_exe;
  public opinion_list_item_adapter_leader(Context context, int textViewResourcedId, List<Opinion_info_short> objects){
    super(context,textViewResourcedId,objects);
    resourcedId = textViewResourcedId;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent){
    final Opinion_info_short opinion_item = getItem(position);
    final View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);
    final TextView opinion_content = (TextView) view.findViewById(R.id.textView69);
    TextView opinion_exe_yn = (TextView) view.findViewById(R.id.textView70);
    final TextView opinion_unexe_res = (TextView) view.findViewById(R.id.textView170);
    opinion_content.setText(opinion_item.getOpinion_content());
    opinion_exe_yn.setText(opinion_item.getOpinion_exe_yn());
    opinion_unexe_res.setText(opinion_item.getOpinion_unexe_res());



    final TextView text_res = (TextView) view.findViewById(R.id.exe_res);
    //同意不执行按钮
    btn_exe = (Button) view.findViewById(R.id.agree);
    btn_exe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String result = opinion_unexe_res.getText().toString();
        final String op_id = opinion_item.getOpinion_id();
        new Thread(new Runnable() {
          @Override
          public void run() {
            Op_status_post op_status_post = new Op_status_post();
            op_status_post.setOp_id(op_id);
            op_status_post.setOp_exe("agree");
            op_status_post.setOp_res(result);
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            Gson gson = new Gson();
            String jsonStr = gson.toJson(op_status_post);

            RequestBody body = RequestBody.create(JSON,jsonStr);
            Request request = new Request.Builder()
                    .url("http://192.168.191.1:3000/op_status_sub")
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

      }
    });

    //强制执行按钮
    btn_unexe = (Button) view.findViewById(R.id.disagree);
    btn_unexe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String result = opinion_unexe_res.getText().toString();
        final String op_id = opinion_item.getOpinion_id();
        new Thread(new Runnable() {
          @Override
          public void run() {
            Op_status_post op_status_post = new Op_status_post();
            op_status_post.setOp_id(op_id);
            op_status_post.setOp_exe("force");
            op_status_post.setOp_res(result);
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            Gson gson = new Gson();
            String jsonStr = gson.toJson(op_status_post);

            RequestBody body = RequestBody.create(JSON,jsonStr);
            Request request = new Request.Builder()
                    .url("http://192.168.191.1:3000/op_status_sub")
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

      }
    });

    return view;
  }

}
