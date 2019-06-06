package com.example.a1.test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LP_info3 extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lp_info3);

    /**点击企发部同意按钮进入流转界面**/
    btn_qifa1 = findViewById(R.id.button2);
    btn_qifa1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(LP_info3.this,circlate.class);
        startActivity(intent);
      }
    });

    /**点击企发部不同意按钮，审批单返回至委托人**/
    ////
    ////

    /**点击更多信息按钮，展示更多信息**/
    button_moreinfo = findViewById(R.id.button);
    layout_moreinfo = (LinearLayout) findViewById(R.id.moreinfo);
    button_moreinfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!isVisible){
          isVisible = true;
          layout_moreinfo.setVisibility(View.VISIBLE);
        }else{
          layout_moreinfo.setVisibility(View.GONE);
          isVisible = false;
        }
      }
    });
  }


  private Button button_moreinfo; /**更多信息按钮**/
  private LinearLayout layout_moreinfo; /**更多信息总布局**/
  private Boolean isVisible = false;
  private Button btn_qifa1;  //button2企发部同意按钮
}
