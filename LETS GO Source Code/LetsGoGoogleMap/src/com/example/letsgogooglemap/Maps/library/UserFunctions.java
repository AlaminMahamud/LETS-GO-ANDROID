package com.example.letsgogooglemap.Maps.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class UserFunctions {
	/**
	 * 01. URL of the PHP API
	 * 02. Constructor
	 * 03. Functions to Login
	 * 04. Functions to reset the password
	 * 05. Functions to Register
	 * 06. Function to Logout User - Reset the temporary data stored in SQLite Database
	 */

	private JSONParser jsonParser;
	
	///01. URL Of the PHP API
	private static String loginURL = "http://ineedahelp.com/ineedahelp_login_api/";
    private static String registerURL = "http://ineedahelp.com/ineedahelp_login_api/";
    private static String forpassURL = "http://ineedahelp.com/ineedahelp_login_api/";
    private static String chgpassURL = "http://ineedahelp.com/ineedahelp_login_api/";
    private static String updateGPSURL = "http://ineedahelp.com/ineedahelp_login_api/update.php";
    private static String searchURL = "http://ineedahelp.com/ineedahelp_login_api/";
    private static String getGPSURL = "http://ineedahelp.com/ineedahelp_login_api/getUp.php";
    private static String getURL = "http://ineedahelp.com/ineedahelp_login_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String getGPS_tag	= "getGPSData";
    private static String search_tag = "getContactByPhone";
    private static String get_tag = "getContacts";

    // 02. Constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    // 03. Functions to Login
    public JSONObject loginUser(String phone, String password){
        // building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    // 04. Functions To Register
    public JSONObject registerUser(
    							   String name,
                                   String email,
                                   String username,
                                   String phone,
                                   String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }
 
    // 05. Functions to Change The Password
    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);

        return json;
    }

    // 06. Function to Reset the Password
    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("email", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
    

    // 07. Update GPS Data
    public void updateGpsData(String phone, String lat, String longi){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("longi", longi));
        new UpadateTask().execute(params);
    }

    // 08.get GPS Data
    public JSONObject getGPSData( ){
    	// building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag",getGPS_tag));
    	JSONObject json  = jsonParser.getJSONFromUrl(getGPSURL, params);
    	return json;
    	
    }
    
    //09. Search user
    public JSONObject getContactByPhone(String phone){
    	// building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", search_tag));
    	params.add(new BasicNameValuePair("phone",phone));
    	JSONObject json  = jsonParser.getJSONFromUrl(searchURL, params);
    	return json;
    	
    }
    
    //10. Logout user
    public boolean logoutUser(Context context){
    	DatabaseHandler db = new DatabaseHandler(context);
    	db.resetUserTable();
    	db.resetFriendsTable();
    	return true;
    }

    //11. Get All Contacts
    public JSONObject getAllContacts(){
    	// building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", get_tag));
    	JSONObject json  = jsonParser.getJSONFromUrl(getURL, params);
    	return json;   
    	}
    
   class UpadateTask extends AsyncTask<List<NameValuePair>, Void, Void>{

	@Override
	protected Void doInBackground(List<NameValuePair>... params) {
		// TODO Auto-generated method stub
		JSONObject json = jsonParser.getJSONFromUrl(updateGPSURL,params[0]);
		return null;
	}
	   
   }
    
}

