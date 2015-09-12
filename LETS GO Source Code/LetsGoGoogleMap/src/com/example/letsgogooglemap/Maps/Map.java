package com.example.letsgogooglemap.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.letsgogooglemap.R;
import com.example.letsgogooglemap.Maps.POJO.MarkerPOJO;
import com.example.letsgogooglemap.Maps.library.DatabaseHandler;
import com.example.letsgogooglemap.Maps.library.JSONParser;
import com.example.letsgogooglemap.Maps.library.UserFunctions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("NewApi")
public class Map extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private static final String KEY_USERNAME = "username";
	private static final String KEY_LATITUDE = "lat";
	private static final String KEY_LONGITUDE = "lng";

	// 01. JSON Response Node Names
	public static ArrayList<MarkerPOJO> data;
	String lat = "", lon = "", getLat, getLon;
	JSONParser jParser = new JSONParser();

	private static String KEY_SUCCESS = "success";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_PHONE = "phone";
	private static String KEY_ERROR = "error";

	GoogleMap googleMap;
	MarkerOptions marker;
	double myLat, Lat, myLon, Lon;
	PolylineOptions line;
	LatLng loc, myLoc;
	boolean draw = false;
	LocationClient locationClient;
	ArrayList<String> checkDirection = new ArrayList<String>();
	private static final String MYMARKER = "You are Here";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		data = new ArrayList<MarkerPOJO>();

		try {
			// Loading map
			dataSearch();

		} catch (Exception e) {
			e.printStackTrace();
		}

		googleMap
				.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(Marker marker1) {
						markerClikced(marker1);
						return true;
					}
				});

	}

	private void markerClikced(Marker marker1) {
		LatLngBounds bounds;
		String title = marker1.getTitle();
		marker1.showInfoWindow();
		int j = 0;
		if (title.equals(MYMARKER)) {
			draw = true;
		} else if (checkDirection.contains(title)) {
			draw = true;
		} else {
			for (MarkerPOJO d : data) {
				if (title.equals(d.userName)) {
					Lat = Double.parseDouble(d.latitude);
					Lon = Double.parseDouble(d.longitude);
					loc = new LatLng(Lat, Lon);
					draw = false;
					checkDirection.add(title);
					break;
				}
			}
		}
		// it will draw the direction
		if (draw == false) {
			try {
				draw = true;
				Toast.makeText(getApplicationContext(),
						"Wait to get the direction", Toast.LENGTH_LONG).show();
				try {
					bounds = new LatLngBounds.Builder().include(myLoc)
							.include(loc).build();
					googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
							bounds, 65));
					draw = false;
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Direction can not be shown, please try again.",
							Toast.LENGTH_LONG).show();
				}
				String str_origin = "origin=" + Lat + "," + Lon;
				// Destination of route
				String str_dest = "destination=" + myLat + "," + myLon;
				// Sensor enabled
				String sensor = "sensor=false";
				// Building the parameters to the web service
				String parameters = str_origin + "&" + str_dest + "&" + sensor;
				// Output format
				String output = "json";
				// Building the url to the web service
				String url = "https://maps.googleapis.com/maps/api/directions/"
						+ output + "?" + parameters;
				DownloadTask downloadTask = new DownloadTask();
				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Direction can not be shown, please try again.",
						Toast.LENGTH_LONG).show();
			}
		} else
			Toast.makeText(getApplicationContext(), MYMARKER, Toast.LENGTH_LONG)
					.show();
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	@SuppressLint("NewApi")
	public void initializeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();

			}

		}

		// adding multiple marker

		for (MarkerPOJO d : data) {
			HashMap<String, String> dataHashMap = new HashMap<String, String>();
			String phone;
			try {
				UserFunctions u = new UserFunctions();
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				dataHashMap = db.getUserDetails();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (d.userName.equals(dataHashMap.get("username"))) {
				continue;
			}
			if (d.latitude.equals("") && d.longitude.equals("")) {
				Lat = 00.0000;
				Lon = 00.0000;
			} else {
				Lat = Double.parseDouble(d.latitude);
				Lon = Double.parseDouble(d.longitude);
			}

			loc = new LatLng(Lat, Lon);
			marker = new MarkerOptions().position(loc).title(d.userName);
			googleMap.addMarker(marker);
		}

		// getting present location by LocationListener
		locationClient = new LocationClient(this, this, this);
		locationClient.connect();

	}

	// end of initialize map function

	@Override
	protected void onPause() {
		super.onPause();
		locationClient.disconnect();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initializeMap();
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * 
	 * get GPS latitude and longitude add polyline and create own Marker
	 * 
	 * @param bundle
	 */

	@Override
	public void onConnected(Bundle bundle) {
		Location location = locationClient.getLastLocation();

		try {
			myLat = location.getLatitude();
			myLon = location.getLongitude();
			myLoc = new LatLng(myLat, myLon);
		} catch (Exception e) {
			// TODO: handle exception
			// Toast.makeText(
			// getApplicationContext(),
			// "Your present location not found,"
			// + " Turn ON ur GPS or try again.",
			// Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
			// finish();
		}
		marker = new MarkerOptions().position(myLoc).title(MYMARKER);
		// adding color,zoom 4 MarkerOption
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 7.0f));
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		googleMap.addMarker(marker);

		HashMap<String, String> dataHashMap = new HashMap<String, String>();
		String phone;
		try {
			UserFunctions u = new UserFunctions();
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			dataHashMap = db.getUserDetails();
			Toast.makeText(
					getApplicationContext(),
					"Wait " + dataHashMap.get("phone") + " Lat " + " "
							+ Double.toString(myLat) + "Long"
							+ Double.toString(myLon), Toast.LENGTH_LONG).show();

			u.updateGpsData(dataHashMap.get("phone"), Double.toString(myLat),
					Double.toString(myLon));
			Toast.makeText(getApplicationContext(), "Updated",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}

		// adding line,path
		for (MarkerPOJO d : data) {
			if (d.userName.equals(dataHashMap.get("username"))) {
				continue;
			}
			Toast.makeText(getApplicationContext(),
					d.latitude + "    " + d.longitude, Toast.LENGTH_SHORT)
					.show();
			if (d.latitude.equals("") || d.longitude.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Invalid Data found in Database", Toast.LENGTH_SHORT)
						.show();
			} else {
				Lat = Double.parseDouble(d.latitude.toString());
				Lon = Double.parseDouble(d.longitude.toString());
				loc = new LatLng(Lat, Lon);
				line = new PolylineOptions().add(loc, myLoc).width(5)
						.color(Color.GREEN);
				googleMap.addPolyline(line);
			}
		}
		// myMarker.showInfoWindow();
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	// download Url - draw the direction between two markers
	@SuppressLint("LongLogTag")
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			// Connecting to url
			urlConnection.connect();
			// Reading data from url
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {

			iStream.close();

			urlConnection.disconnect();

		}
		return data;
	}

	// download task
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	// parser task
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);
					

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);
					
					Toast.makeText(getApplicationContext(),"lat = " +lat +" lng = "+lng,Toast.LENGTH_SHORT ).show();
					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(8);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);
		}
	}

	// / data search

	public void dataSearch() {
		// TODO Auto-generated method stub
		new ProcessData().execute();
	}

	// 05. AsyncTask to get and Send data to MySQL Database through JSON
	// response
	public class ProcessData extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Map.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.getGPSData();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString("success") != null) {

					String res = json.getString("success");

					if (Integer.parseInt(res) == 1) {
						pDialog.setMessage("Loading User Space");
						pDialog.setTitle("Getting Data");

						JSONArray json_user;
						try {
							json_user = json.getJSONArray("user");
							for (int i = 0; i < json_user.length(); i++) {
								JSONObject o = json_user.getJSONObject(i);
								data.add(new MarkerPOJO(
										o.getString("username"), o
												.getString("lat"), o
												.getString("lng")));
							}

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						pDialog.dismiss();
					} else {

						pDialog.dismiss();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			initializeMap();
		}
	}

}
