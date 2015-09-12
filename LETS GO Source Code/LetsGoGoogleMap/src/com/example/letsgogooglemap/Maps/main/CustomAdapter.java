package com.example.letsgogooglemap.Maps.main;

import java.util.ArrayList;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.POJO.FriendPOJO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<FriendPOJO>{

	public CustomAdapter(Context context, ArrayList<FriendPOJO>friends) {
        super(context,0, friends);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get The Data for this position
        FriendPOJO friend= getItem(position);
        // Check an Existing View is reused
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookupview for data population
        TextView tvname = (TextView) convertView.findViewById(R.id.Name);
        TextView tvemail = (TextView) convertView.findViewById(R.id.Email);
        TextView tvusername = (TextView) convertView.findViewById(R.id.Username);
        tvname.setText(friend.name);
        tvemail.setText(friend.email);
        tvusername.setText(friend.username);
        
        return convertView;
    }
}
