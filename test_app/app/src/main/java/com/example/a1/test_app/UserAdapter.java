package com.example.a1.test_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User_admin> {
    private int resourceId;
    private String pos;
    public UserAdapter(Context context, int textViewResourceId, List<User_admin> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        User_admin user_admin = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView userid = (TextView) view.findViewById(R.id.user_id);
        TextView username = (TextView) view.findViewById(R.id.user_name);
        TextView userdep = (TextView) view.findViewById(R.id.user_dep);
        TextView userpos = (TextView) view.findViewById(R.id.user_pos);
        userid.setText(user_admin.getUserid());
        username.setText(user_admin.getUsername());
        userdep.setText(user_admin.getUserdep());
        pos = String.valueOf(user_admin.getUserposition());
        userpos.setText(pos);
        return view;
    }
}
