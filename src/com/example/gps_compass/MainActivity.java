package com.example.gps_compass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener, LocationListener {
	
    // device sensor manager
    private SensorManager mSensorManager;
	private LocationManager locationManager;
	
	// Variables to store values for SharedPreferences
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String DEST_NAME = "destination_name";
	    
    // angle, distance between destination and current location
	private float targetAngle = 0;
	private float targetDistance = 0;

    // record the compass picture angle turned
    private float currentDegree_compass = 0;
    private float currentDegree_needle = 0;
    
	// define the display assembly compass picture
    private ImageView image_compass;
    private ImageView image_needle;
    
    //define textView
    private TextView distanceView;

	// returns a DataInterface that contains name and location. 
	public DataInterface getSavedLocation() {
	
		// Load SharedPreferences
		SharedPreferences DestLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		DataInterface tmpObject = new DataInterface();
	
		// IMPORTANT! Replace location object coordinates with valid location object
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
		
		// compass image 
        image_compass = (ImageView) findViewById(R.id.imageViewCompass);
        image_needle = (ImageView) findViewById(R.id.imageViewCompassNeedle);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager =  (LocationManager) getSystemService(LOCATION_SERVICE);
        
        //initialize TextView
        distanceView = (TextView) findViewById(R.id.distance);
        distanceView.setText("Initializing GPS... ");
        
        
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1 ,this);
        else; //Warning PopUp
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
    
    @SuppressWarnings("deprecation")
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

    public void rotate_compass(float degree) {
    	
        // create a rotation animation (reverse turn degree <degrees>)
        RotateAnimation ra = new RotateAnimation(
                currentDegree_compass, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, 
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image_compass.startAnimation(ra);
        currentDegree_compass = -degree;  	
    }
    
    public void rotate_needle(float degree) {
    	
        // create a rotation animation (reverse turn degree <degrees>)
        RotateAnimation ra = new RotateAnimation(
                currentDegree_needle, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, 
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image_needle.startAnimation(ra);
        currentDegree_needle = -degree;  	
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated (azimuth)
        float degree_compass = Math.round(event.values.clone()[0]);
        rotate_compass(degree_compass);
        // degree_compass: counterclockwise, targetAngle: clockwise 
        rotate_needle((degree_compass - targetAngle) % 360);
    }
    
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// load stored DataInterface
		DataInterface target = getSavedLocation();
		// get distance and angle from current location to destination
		targetDistance = arg0.distanceTo(target.coordinates);
		// note: angle is element of [-180°, 180°]
		targetAngle =  (360 + arg0.bearingTo(target.coordinates)) % 360;
		
		// set output to meters for a distance < 1000m, otherwise rounded kilometers.
		if(targetDistance < 1000){
		distanceView.setText("Distance to " + getSavedLocation().name 
				+ ": " + (int)targetDistance + "\u2009" + "m");
		}
		else distanceView.setText("Distance to " + getSavedLocation().name 
				+ ": " + (float)Math.round(targetDistance/10) / 100.0f + "\u2009" + "km");
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
