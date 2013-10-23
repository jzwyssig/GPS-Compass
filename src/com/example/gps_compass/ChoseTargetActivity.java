package com.example.gps_compass;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChoseTargetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_target);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chose_target, menu);
		return true;
	}

}
