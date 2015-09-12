package com.example.letsgogooglemap.Maps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.letsgogooglemap.Maps.POJO.MarkerPOJO;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.library.JSONParser;
import com.example.letsgogooglemap.Maps.library.UserFunctions;
import com.example.letsgogooglemap.Maps.main.Search;

public class newMap extends Activity {
	public ArrayList<MarkerPOJO> data;
	String lat="",lon ="", getLat,getLon;
//	String url=  "http://ineedahelp.com/ineedahelp_login_api/getUp.php";
	JSONParser jParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new AsyncTaskRunnerCurrent().execute();
		
	}
	
	class AsyncTaskRunnerCurrent extends AsyncTask<String, String, String> {
		private static final String KEY_USERNAME = "username";
		private static final String KEY_LATITUDE = "lat";
		private static final String KEY_LONGITUDE = "lng";
		ProgressDialog progressDialog = new ProgressDialog(newMap.this);
        int success;
       
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
			UserFunctions u = new UserFunctions();
			JSONObject json2 = u.getGPSData();
			JSONArray json_user = json2.getJSONArray("user");
			for (int i = 0; i < json_user.length(); i++) {
				JSONObject o = json_user.getJSONObject(i);
				data.add(new MarkerPOJO(o.getString(KEY_USERNAME),
						o.getString(KEY_LATITUDE), o
								.getString(KEY_LONGITUDE)));
			
			/* DatabaseHandler db = new DatabaseHandler(newMap.this);	
			String phone = db.getUserDetails().get("phone");
			//** nijer location - myLat , myLng
				u.updateGpsData(phone,(Double.toString(myLat)) , (Double.toString(myLon)) ); */
			}

			
			} catch (Exception e) {
				// Toast.makeText(getApplicationContext(),"A problem occured. Please restart the application once again",
				// Toast.LENGTH_LONG).show();

				Log.d("Problemasdddddddddddddd", e + "");
			
			}
			return null;
		}

		protected void onPostExecute(String string) {

			progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), getLat+" "+getLon, Toast.LENGTH_LONG).show();
			
		}

		protected void onPreExecute() {
             
			progressDialog.setMessage("Please wait. Loading..");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

	}
	
	
	
}
