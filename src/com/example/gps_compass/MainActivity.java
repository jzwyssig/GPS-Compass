package com.example.gps_compass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {
	
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
		
		// TEST METHODE
		refreshData();
		
		
		// compass image 
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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

	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(resultCode == RESULT_OK){
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
     }
    }
	
	// define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0;

    // device sensor manager
    private SensorManager mSensorManager;
    
    @Override
    protected void onResume() {
        super.onResume();  
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated (azimuth)
        float degree = Math.round(event.values.clone()[0]);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, 
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }
    
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
