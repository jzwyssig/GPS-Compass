package com.example.gps_compass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class CreateTargetActivity extends Activity {

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_target);
		
		RadioButton rb= (RadioButton) findViewById(R.id.radio_current_location);//RadioButton default set on
		rb.setChecked(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_target, menu);
		return true;
	}

	//Sourcecode from button_set
public void setTarget(View view){
		
	
		RadioButton rb= (RadioButton) findViewById(R.id.radio_current_location);
		
		DataInterface temp = new DataInterface();//contains a name and gps-coordinates
	
		EditText editText=(EditText) findViewById(R.id.set_target_name);// read out the target's name
		temp.name = editText.getText().toString();	
		
		
		if(rb.isChecked()){//Target: current location
			
			temp.coordinates=null;	//EXPAND: read in current coordinates	
			
		}
		else{}//EXPAND: read in from EditText
			
		
		// save Target (sharepref), set automatically as current Target
		
		Intent intent= new Intent(this, MainActivity.class);// goto Main
		startActivity(intent);
	
	}
	
}
