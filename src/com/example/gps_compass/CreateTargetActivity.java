package com.example.gps_compass;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class CreateTargetActivity extends Activity implements LocationListener {

	
	boolean checked=false;
	LocationManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_target);
		
		
		manager = (LocationManager) this.getSystemService(LOCATION_SERVICE); 
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);//get Location	
		
		CheckBox cl = (CheckBox)findViewById(R.id.checkbox_use_current_location);//RadioButton default set on
		cl.setChecked(true);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_target, menu);
		return true;
	}
	
	
	public void onCheckBoxClicked (View view){
		//is the view now checked?
		checked = ((CheckBox) view).isChecked();
	}

	
	//Sourcecode from button_set
	public void setTarget(View view){		
	
//		RadioButton rb= (RadioButton) findViewById(R.id.radio_current_location);
		
		DataInterface temp = new DataInterface();//contains a name and gps-coordinates
	
		EditText editText=(EditText) findViewById(R.id.set_target_name);// read out the target's name
		temp.name = editText.getText().toString();	
		
		if(checked){//Target: current location
			
			//Check: GPS enabled!
			temp.coordinates=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
		}
		else{//EXPAND: read in from EditText			
			
		}
			
				
		// save Target (sharepref), set automatically as current Target
		
		Intent intent= new Intent(this, MainActivity.class);// goto Main
		startActivity(intent);
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
