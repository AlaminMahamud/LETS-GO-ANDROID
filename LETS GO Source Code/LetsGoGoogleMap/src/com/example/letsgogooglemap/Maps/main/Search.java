package com.example.letsgogooglemap.Maps.main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
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

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.Map;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.library.UserFunctions;

public class Search extends Activity{
    TextView appName,err_search_text;
    EditText name, email, phone;
    Button search, add,back;

    // 01. JSON Response Node Names

    private static String KEY_SUCCESS = "success";
    private static String KEY_NAME = "name";
    private static String KEY_USERNAME = "username";
    private static String KEY_EMAIL = "email";
    static String KEY_PHONE = "phone";
    private static String KEY_ERROR = "error";
    public static String phone_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        phone= (EditText) findViewById(R.id.search_phone);
        email = (EditText) findViewById(R.id.email_found);
        name = (EditText) findViewById(R.id.name_found);
        search = (Button) findViewById(R.id.btn_search);
        add = (Button) findViewById(R.id.button2);
        back = (Button) findViewById(R.id.back_button);
        
        appName = (TextView)findViewById(R.id.textView12);
        err_search_text = (TextView) findViewById(R.id.error_search_text);

       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               NetAsync(v);
           }
       });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        	   Intent i = new Intent(Search.this, Main.class);
        	   startActivity(i);
           }
       });
       

    }

  
    // 04. AsyncTask whether internet connection is working or not

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Search.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        // gets current Device Stats and checks for Working internet Connection by trying google

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
                err_search_text.setText("Error in Network Connection. Turn on internet");
            }
        }

    }

    // 05. AsyncTask to get and Send data to MySQL Database through JSON response
    private class ProcessLogin extends AsyncTask<String, String, JSONObject>{
        private ProgressDialog pDialog;
        String phone_data,password_data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Search.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
        	
            UserFunctions userFunction = new UserFunctions();
            phone_search=phone.getText().toString() ;
            Log.d("phone", phone.getText().toString() );
            JSONObject json = userFunction.getContactByPhone(phone.getText().toString());
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
                        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        final JSONObject json_user = json.getJSONObject("user");
                        
                        /**
                         * Clear all previous data in SQlite database.
                         **/
       
                        err_search_text.setText("Data Found");
                        name.setText(
                                json_user.getString(KEY_NAME));
                        email.setText(json_user.getString(KEY_EMAIL));
                        
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                	if(!db.isFriendExisted(json_user.getString(KEY_PHONE))){
                                        db.addFriend(
                                                json_user.getString(KEY_NAME),
                                                json_user.getString(KEY_EMAIL),
                                                json_user.getString(KEY_USERNAME),
                                                json_user.getString(KEY_PHONE)
                                        		          );
                                        Toast.makeText(getApplicationContext(), json_user.getString(KEY_USERNAME)+ " is Added", Toast.LENGTH_LONG).show();	
                                	}else{
                                		Toast.makeText(getApplicationContext(), "Already Added", Toast.LENGTH_LONG).show();
                                	}
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                        pDialog.dismiss();
                    }else{

                        pDialog.dismiss();
                        err_search_text.setText("Incorrect phone || Try Again");
                    }
//                Intent intent = new Intent(Search.this, Map.class);
//                startActivity(intent);
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
