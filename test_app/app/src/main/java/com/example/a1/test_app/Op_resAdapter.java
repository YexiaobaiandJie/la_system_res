package com.example.a1.test_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Op_resAdapter extends ArrayAdapter<Op_res_info> {
    private int resourcedId;
    public Op_resAdapter(Context context, int textViewResourcedId, List<Op_res_info> objects) {
        super(context, textViewResourcedId, objects);
        resourcedId = textViewResourcedId;
    }




        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            Op_res_info op_res = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);
            final TextView opinion_content = (TextView) view.findViewById(R.id.op_content);
            opinion_content.setText(op_res.getOp_content());
            final TextView opinion_exe = (TextView) view.findViewById(R.id.exe_yn);
            String exe_yn = op_res.getOp_exe_yn();
            if (exe_yn.equals("yes")){
                opinion_exe.setText("执行");
            }else if(exe_yn.equals("no")){
                opinion_exe.setText("不执行");
            }else if(exe_yn.equals("force")){
                opinion_exe.setText("强制执行");
            }else{
                opinion_exe.setText("获得意见状态出错");
            }
            return view;
        }

    }

