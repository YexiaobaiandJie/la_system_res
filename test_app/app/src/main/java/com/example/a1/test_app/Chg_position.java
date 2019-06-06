package com.example.a1.test_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chg_position extends AppCompatActivity {
    private String userid;
    private String userpos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chg_position);


        text_userid = findViewById(R.id.userid);
        btn_chg_pos = findViewById(R.id.chg_pos);
        sp_pos = (Spinner) findViewById(R.id.spinner_pos);
        sp_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] poss = getResources().getStringArray(R.array.spinner_pos);
                userpos = poss[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_chg_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = text_userid.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        User_short info = new User_short();
                        info.set_userid(userid);
                        info.set_password(userpos);

                        Gson gson = new Gson();
                        String user_json = gson.toJson(info);
                        RequestBody body = RequestBody.create(JSON,user_json);
                        Request request = new Request.Builder()
                                .url("http://192.168.191.1:3000/admin_chg_pos")
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

    }


    private Spinner sp_pos;//权限
    private EditText text_userid;
    private Button btn_chg_pos;
}
