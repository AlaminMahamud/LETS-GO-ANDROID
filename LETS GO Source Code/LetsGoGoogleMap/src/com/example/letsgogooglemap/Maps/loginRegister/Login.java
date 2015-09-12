package com.example.letsgogooglemap.Maps.loginRegister;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.Map;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.library.UserFunctions;
import com.example.letsgogooglemap.Maps.main.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity{

	/**
	 * 01.  JSON Response Node Names
	 * 
	 */
	
	Button login, register, loginFB, forget;
    EditText phone, password;
    TextView appName,loginErrorMsg;
    
    // 01. JSON Response Node Names

    private static String KEY_SUCCESS = "success";
    private static String KEY_NAME = "name";
    private static String KEY_USERNAME = "username";
    private static String KEY_EMAIL = "email";
    private static String KEY_PHONE = "phone";
    private static String KEY_ERROR = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appName = (TextView) findViewById(R.id.appName);
        phone = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        forget = (Button) findViewById(R.id.forget_button);
        loginErrorMsg = (TextView) findViewById(R.id.errMsgText);

    }
    
    // forget 
    
    public void forget(View v){
    	Intent i = new Intent(Login.this, ResetPassword.class);
        startActivity(i);
    }

    //register
    public void reg(View v){
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
    //login
    public void log(View v){
        if (  ( !phone.getText().toString().equals("")) && ( !password.getText().toString().equals("")) )
        {
            NetAsync(v);
        }
        else if ( ( !phone.getText().toString().equals("")) )
        {
            Toast.makeText(getApplicationContext(),
                    "Password field empty", Toast.LENGTH_SHORT).show();
        }
        else if ( ( !password.getText().toString().equals("")) )
        {
            Toast.makeText(getApplicationContext(),
                    "Phone field empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Email and Password field are empty", Toast.LENGTH_SHORT).show();
        }
    }
    
    // 04. AsyncTask whether internet connection is working or not

    public class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        // gets current Device Stats and checks for Working internet Connection by trying google

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection. Turn on internet");
            }
        }

    }

    // 05. AsyncTask to get and Send data to MySQL Database through JSON response
    public class ProcessLogin extends AsyncTask<String, String, JSONObject>{
        private ProgressDialog pDialog;
        String phone_data,password_data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phone_data = phone.getText().toString();
            password_data = password.getText().toString();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(phone_data, password_data);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        
                        db.addUser(
                                json_user.getString(KEY_NAME),
                                json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_USERNAME),
                                json_user.getString(KEY_PHONE));
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent upanel = new Intent(getApplicationContext(), Main.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        Log.d("GO", "ready");
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }else{

                        pDialog.dismiss();
                        loginErrorMsg.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }


    
    
	
}
