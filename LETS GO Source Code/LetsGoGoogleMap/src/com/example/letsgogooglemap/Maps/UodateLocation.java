package com.example.letsgogooglemap.Maps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class UodateLocation extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	LocationClient locationClient;
	double myLat,myLon;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		locationClient = new LocationClient(this, this, this);
		locationClient.connect();
		Toast.makeText(getApplicationContext(), "lat:"+myLat+"lon:"+myLon , Toast.LENGTH_LONG);
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle bundle) {
		Location location = locationClient.getLastLocation();
		try {
			myLat = location.getLatitude();
			myLon = location.getLongitude();

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(
					getApplicationContext(),
					"Your present location not found,"
							+ " Turn ON ur GPS or try again.",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
