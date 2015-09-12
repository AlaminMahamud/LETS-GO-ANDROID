package com.example.letsgogooglemap.Maps.POJO;

/**
 * Created by Ashraf uddin on 5/30/2015.
 */
public class MarkerPOJO {
    public String userName;
    public String latitude;
    public String longitude;

    public MarkerPOJO(String _u, String _lat, String _lng){
        userName = _u;
        latitude = _lat;
        longitude = _lng;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
    
    
    
    
}
