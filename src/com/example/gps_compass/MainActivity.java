package com.example.gps_compass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	// Variables to store values for SharedPreferences
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String DEST_NAME = "destination_name";

	// returns a DataInterface that contains name and location. 
	public DataInterface getSavedLocation() {
	
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
		return tmpObject;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		refreshData();
	}
	
	// test method
	public void refreshData() {
		TextView testIfSharedPrefWorks = (TextView) findViewById(R.id.DestName);
		testIfSharedPrefWorks.setText("Destination: " + getSavedLocation().name);
		//testIfSharedPrefWorks.invalidate(); // force view to draw
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Source code of button_create
	public void newTarget(View view) {
		
		Intent intent = new Intent(this, CreateTargetActivity.class);
		startActivityForResult(intent, 1);			
	}

	// Source code of button_select
	public void selectTarget(View view) {

		Intent intent = new Intent(this, ChoseTargetActivity.class);
		startActivity(intent);
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(resultCode == RESULT_OK){
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
     }
    }

}
