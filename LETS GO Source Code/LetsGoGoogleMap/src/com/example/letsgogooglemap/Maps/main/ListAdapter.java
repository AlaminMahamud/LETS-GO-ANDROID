package com.example.letsgogooglemap.Maps.main;

import java.util.ArrayList;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.POJO.FriendPOJO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{

	Context ctx;
    LayoutInflater inflater;
    ArrayList<FriendPOJO>objects;

    ListAdapter(Context context, ArrayList<FriendPOJO> friends){
        ctx = context;
        objects = friends;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	@Override
	public int getCount() {
        return objects.size();
	}

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            FriendPOJO p = getProduct(position);
            ((TextView) convertView.findViewById(R.id.Name)).setText(p.name);
            ((TextView) convertView.findViewById(R.id.Email)).setText(p.email);
            ((TextView) convertView.findViewById(R.id.Username)).setText(p.username);
            CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.cbBox);
            cbBuy.setOnCheckedChangeListener(myCheckChangeList);
            cbBuy.setTag(position);
            cbBuy.setChecked(p.box);
        }
		
		return convertView;

	}
	

    private FriendPOJO getProduct(int position) {
        return ((FriendPOJO)getItem(position));
    }

    ArrayList<FriendPOJO> getBox(){
        ArrayList<FriendPOJO> box = new ArrayList<FriendPOJO>();
        for(FriendPOJO p:objects){
            if(p.box){
                box.add(p);
            }
        }
        return box;
    }
    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getProduct((Integer)buttonView.getTag()).box = isChecked;
        }
    };


}
