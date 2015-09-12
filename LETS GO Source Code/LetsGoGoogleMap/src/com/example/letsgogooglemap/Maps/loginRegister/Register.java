package com.example.letsgogooglemap.Maps.loginRegister;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
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
import android.widget.Toast;

public class Register extends Activity {


    /*
    01. JSON RESPONSE NODE NAMES
    02. Defining Layout Items
    03. Called when the Activity is firstCreated
    04. Defining All LayoutItems
    05. Button which Switches back to the login Screen on Clicked
    06. AsyncTask to Check Whether Internet Connection is Working
    07. Gets current device state and checks for working internet Connection by trying Google

*/

        // 01. JSON Response Node Names

        private static String KEY_SUCCESS = "success";
        private static String KEY_NAME = "name";
        private static String KEY_USERNAME = "username";
        private static String KEY_EMAIL = "email";
        private static String KEY_PHONE = "phone";
        private static String KEY_LATITUDE = "latitude";
        private static String KEY_LONGITUDE = "longitude";
        private static String KEY_CREATED_AT = "created_at";
        private static String KEY_ERROR = "error";

    /**
     * 02.Defining layout items.
     **/
    TextView appName;
    EditText inputName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPhone;
    EditText inputPassword;
    Button btnRegister;
    Button btnLogin;
    TextView registerErrorMsg;

    /**
     * 03. Called When The Activity is first Created
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * 04. Defining A Layout items
         *
         */
        inputName = (EditText) findViewById(R.id.fullName);
        inputUsername = (EditText) findViewById(R.id.userName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnregister);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        registerErrorMsg = (TextView) findViewById(R.id.regErr);



        /**
         * 05. button Which Switches back to the Login Screen on Clicked
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


        /**
         * 06. Register Button Click Event
         *     A Toast is set to alert when the fieldsare empty
         *     Another toast is set to alert Username must be 5 characters.
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (    ( !inputUsername.getText().toString().equals("")) &&
                        ( !inputPassword.getText().toString().equals("")) &&
                        ( !inputName.getText().toString().equals(""))&&
                        ( !inputPhone.getText().toString().equals("")) &&
                        ( !inputEmail.getText().toString().equals("")) )
                {
                    if ( inputUsername.getText().toString().length() > 2 ){
                        NetAsync(view);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 2 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 07. AsyncTask to check whether internet connection is working
     */
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }
    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String email,password,name,username,phone;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = inputName.getText().toString();
            email = inputEmail.getText().toString();
            username= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            phone = inputPhone.getText().toString();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            Log.d("Password", password);
            JSONObject json = userFunction.registerUser(name, email, username, phone,password);

            return json;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);

                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        registerErrorMsg.setText("Successfully Registered");


                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(
                                json_user.getString(KEY_NAME),
                                json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_USERNAME),
                                json_user.getString(KEY_PHONE));
                        
                        Intent registered = new Intent(getApplicationContext(), Registered.class);

                        /**
                         * Close all views before launching Registered screen
                         **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);


                        finish();
                    }

                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        registerErrorMsg.setText("User already exists");
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        registerErrorMsg.setText("Invalid Email id");
                    }

                }


                else{
                    pDialog.dismiss();

                    registerErrorMsg.setText("Error occured in registration");
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
