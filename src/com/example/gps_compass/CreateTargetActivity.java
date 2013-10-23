package com.example.gps_compass;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateTargetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_target);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_target, menu);
		return true;
	}

}
