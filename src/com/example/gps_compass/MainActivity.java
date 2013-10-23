package com.example.gps_compass;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	//Gian is awesome

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
// Sourcecode of button_create
public void newTarget(View view){
		
		Intent intent= new Intent(this, CreateTargetActivity.class);
		startActivity(intent);
		
	}

//Sourcecode of button_select
public void selectTarget(View view){
	
	Intent intent= new Intent(this, ChoseTargetActivity.class);
	startActivity(intent);
	
}
	
	
	

}
