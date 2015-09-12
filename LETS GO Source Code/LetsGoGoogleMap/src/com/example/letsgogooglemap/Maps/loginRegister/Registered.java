package com.example.letsgogooglemap.Maps.loginRegister;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;

public class Registered extends Activity{
    /*
    01. called when the activity is first creaated
    02. Displays The Registration details in Text View
  */


    // 01. Called When The Activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap<String,String> user = new HashMap<String, String>();
        user = db.getUserDetails();

        // 02. Displays the registration details in Text View

        final TextView name = (TextView)findViewById(R.id.name);
        final TextView uname = (TextView)findViewById(R.id.uname);
        final TextView email = (TextView)findViewById(R.id.email);
        final TextView phone = (TextView)findViewById(R.id.phoneR);
        final TextView created_at = (TextView)findViewById(R.id.regat);
        name.setText(user.get("name"));
        uname.setText(user.get("username"));
        email.setText(user.get("email"));
        phone.setText(user.get("phone"));
        created_at.setText(user.get("created_at"));






        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

    }
}
