package com.example.gps_compass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTargetActivity extends Activity implements LocationListener {

	boolean checked = true;
	LocationManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_target);

		manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		// get location
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
		// RadioButton default set on
		CheckBox cl = (CheckBox) findViewById(R.id.checkbox_use_current_location); 
		cl.setChecked(true);
		getSavedLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_target, menu);
		return true;
	}

		// Variables to store values for SharedPreferences
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String DEST_NAME = "destination_name";
	
	// Save Destination Name, Latitude and Longitude
	public void SaveLocation(Location location, String LocationName) {
		SharedPreferences DestLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = DestLocation.edit();
		// Save values, use double converted longs
		editor.putLong(LATITUDE, Double.doubleToLongBits(location.getLatitude()));
		editor.putLong(LONGITUDE, Double.doubleToLongBits(location.getLongitude()));
		editor.putString(DEST_NAME, LocationName);
		editor.commit();
	}
	
	// TEST METHOD! Get saved GPS-Coordinates and location's name 
	public void getSavedLocation() {
	
		// Load SharedPreferences
		SharedPreferences DestLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		DataInterface tmpObject = new DataInterface();
	
		// IMPORTANT! Replace Location object coordinates with valid Location Object
		tmpObject.coordinates = new Location("GPS_PROVIDER");
		
		double latitude = Double.longBitsToDouble(DestLocation.getLong(LATITUDE, 0));
		tmpObject.coordinates.setLatitude(latitude);
		
		double longitude = Double.longBitsToDouble(DestLocation.getLong(LONGITUDE, 0));
		tmpObject.coordinates.setLongitude(longitude);
		
		tmpObject.name = DestLocation.getString(DEST_NAME, "");

		// Getting reference to TextView Longitude
		TextView Longitude = (TextView) findViewById(R.id.getLongitude);

		// Getting reference to TextView Latitude
		TextView Latitude = (TextView) findViewById(R.id.getLatitude);
		
		// Getting reference to TextView Latitude
		TextView DestinationName = (TextView) findViewById(R.id.getDestinationName);

		// Setting Current Longitude
		Longitude.setText("Longitude: " + longitude);

		// Setting Current Latitude
		Latitude.setText("Latitude: " + latitude);
		
		// Setting Current Destination Name
		DestinationName.setText("Destination Name: " + tmpObject.name);
	}

	public void onCheckBoxClicked(View view) {
		// is the view now checked?
		checked = ((CheckBox) view).isChecked();
	}

	// Source code from button_set
	public void setTarget(View view) {

		DataInterface temp = new DataInterface(); // contains a name and GPS-coordinates
		// read out the target's name
		EditText editText = (EditText) findViewById(R.id.set_target_name); 
		
		temp.name = editText.getText().toString();

		if (checked) { // Target: current location

			// Check: GPS enabled!
			temp.coordinates = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			SaveLocation(temp.coordinates, temp.name);		
			getSavedLocation();

		} else { // EXPAND: read in from EditText

		}

		// return to main activity
		setResult(RESULT_OK, null);
		finish();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
