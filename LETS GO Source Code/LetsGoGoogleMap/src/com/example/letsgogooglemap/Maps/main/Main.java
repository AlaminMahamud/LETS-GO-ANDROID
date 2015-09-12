package com.example.letsgogooglemap.Maps.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.Map;
import com.example.letsgogooglemap.Maps.newMap;
import com.example.letsgogooglemap.Maps.POJO.FriendPOJO;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.loginRegister.ChangePassword;
import com.example.letsgogooglemap.Maps.loginRegister.Login;

public class Main extends Activity{

    // 01. Declaring All Variables
    // 02. onCreate
    // 03. Initializing

    TextView appName,friends, search;
    ListView lv;
    Button locate,logout,changePassword;
    
    ArrayList<FriendPOJO>friends_data;
    ListAdapter boxAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        friends_data = new ArrayList<FriendPOJO>();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        friends_data =db.getAllFriends();
        for(FriendPOJO a : friends_data){
        	Log.d("Name", a.name);
        }
        boxAdapter = new ListAdapter(this, friends_data);        
        appName = (TextView)findViewById(R.id.textView6);
        friends = (TextView)findViewById(R.id.textView7);
        search = (Button)findViewById(R.id.btn_srch_main);
        logout = (Button) findViewById(R.id.button3);
        changePassword = (Button) findViewById(R.id.button);
        lv = (ListView)findViewById(R.id.listView2);
        lv.setAdapter(boxAdapter);
        
        locate = (Button) findViewById(R.id.locate_button); 
        search.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i= new Intent(Main.this, Search.class);
			startActivity(i);
		}
	});
       locate.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i= new Intent(Main.this, Map.class);
			startActivity(i);
		}
	});

        // I want to get the data of my Friends Here
        // Intent Service
        //Intent i = new Intent(Main.this, UpdateGPSData.class);
        //startService(i);
       
       changePassword.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i =new Intent(Main.this, ChangePassword.class);
			startActivity(i);
		}
	});
       
       logout.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i =new Intent(Main.this, Login.class);
			startActivity(i);
		}
	});
    }


   

}
