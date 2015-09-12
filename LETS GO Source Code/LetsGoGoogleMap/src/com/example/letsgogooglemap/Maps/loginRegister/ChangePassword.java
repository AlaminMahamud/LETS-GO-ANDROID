package com.example.letsgogooglemap.Maps.loginRegister;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.library.UserFunctions;
import com.example.letsgogooglemap.Maps.main.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePassword extends Activity{
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    EditText newpass;
    TextView alert;
    Button changepass;
    Button cancel;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);


        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent login = new Intent(getApplicationContext(), Main.class);
                startActivity(login);
                finish();
            }

        });


        newpass = (EditText) findViewById(R.id.newpass);
        alert = (TextView) findViewById(R.id.alertpass);
        changepass = (Button) findViewById(R.id.btchangepass);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetAsync(view);
            }
        });

	}

	
private class NetCheck extends AsyncTask<String, String, Boolean>{

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}


private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


    private ProgressDialog pDialog;

    String newpas,email,phone;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap<String,String> user = new HashMap<String, String>();
        user = db.getUserDetails();

        newpas = newpass.getText().toString();
        email = user.get("email");
        email = user.get("phone");
        Log.d("Password", newpas);


        pDialog = new ProgressDialog(ChangePassword.this);
        pDialog.setTitle("Contacting Servers");
        pDialog.setMessage("Getting Data ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {


        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.chgPass(newpas, email);
        Log.d("Button", "Register");
        return json;


    }


    @Override
    protected void onPostExecute(JSONObject json) {

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                alert.setText("");
                String res = json.getString(KEY_SUCCESS);
                String red = json.getString(KEY_ERROR);


                if (Integer.parseInt(res) == 1) {
                    /**
                     * Dismiss the process dialog
                     **/
                    pDialog.dismiss();
                    alert.setText("Your Password is successfully changed.");


                } else if (Integer.parseInt(red) == 2) {
                    pDialog.dismiss();
                    alert.setText("Invalid old Password.");
                } else {
                    pDialog.dismiss();
                    alert.setText("Error occured in changing Password.");
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();


        }

    }}
public void NetAsync(View view){
    new NetCheck().execute();
}


	
}

