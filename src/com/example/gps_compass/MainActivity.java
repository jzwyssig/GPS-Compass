package com.example.gps_compass;

//import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		refreshData();
	}
	
	// get Data
	public void refreshData() {
		SharedPreferences destLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		TextView testIfSharedPrefWorks = (TextView) findViewById(R.id.DestName);
		testIfSharedPrefWorks.setText("Destination: " + destLocation.getString(DEST_NAME, ""));
		testIfSharedPrefWorks.invalidate();
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
	// Call 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(resultCode == RESULT_OK){
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
     }
    }

}
