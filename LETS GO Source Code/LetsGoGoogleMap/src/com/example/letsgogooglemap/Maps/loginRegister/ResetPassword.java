package com.example.letsgogooglemap.Maps.loginRegister;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.library.UserFunctions;

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

public class ResetPassword extends Activity{
    /**
     * 01. Key Variables
     * 02. Layout Variables
     * 03. onCreate
     * 04. AsyncTask NetCheck
     **/

    // 01. Key Variables

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    // 02. Layout Initialization
    EditText email;
    TextView alert;
    Button resetpass;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset);

        Button login = (Button) findViewById(R.id.bktolog);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


        email = (EditText) findViewById(R.id.forpas);
        alert = (TextView) findViewById(R.id.alert);
        resetpass = (Button) findViewById(R.id.respass);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                NetAsync(view);

            }



        });
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>

    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ResetPassword.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){



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
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert.setText("Error in Network Connection");
            }
        }
    }





    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String forgotpassword;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            forgotpassword = email.getText().toString();
            Log.d("Email", forgotpassword);
            pDialog = new ProgressDialog(ResetPassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.forPass(forgotpassword);
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks if the Password Change Process is sucesss
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if(Integer.parseInt(res) == 1){
                        pDialog.dismiss();
                        alert.setText("A recovery email is sent to you, see it for more details.");



                    }
                    else if (Integer.parseInt(red) == 2)
                    {    pDialog.dismiss();
                        alert.setText("Your email does not exist in our database.");
                    }
                    else {
                        pDialog.dismiss();
                        alert.setText("Error occured in changing Password");
                    }




                }}
            catch (JSONException e) {
                e.printStackTrace();


            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }

}
