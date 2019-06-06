package com.example.a1.test_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class af_list_item_adapter extends ArrayAdapter<AF_list_listview> {

    private int resourcedId;
    public af_list_item_adapter(Context context, int textViewResourceId, List<AF_list_listview> objects) {
        super(context,textViewResourceId,objects);
        resourcedId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        AF_list_listview listview = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourcedId,parent,false);
        TextView viewname = (TextView) view.findViewById(R.id.textView30);
        TextView viewtype = (TextView) view.findViewById(R.id.textView29);
        TextView viewtime = (TextView) view.findViewById(R.id.textView27);
        TextView viewstatus = (TextView) view.findViewById(R.id.textView28);
        viewname.setText(listview.getname());
        viewtype.setText(listview.getItem_type());
        viewtime.setText(listview.gettime());
        viewstatus.setText(listview.getstatus());

        return view;
    }

}
