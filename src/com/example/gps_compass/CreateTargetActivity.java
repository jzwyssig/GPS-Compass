package com.example.gps_compass;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CreateTargetActivity extends Activity implements LocationListener {

	boolean checked = true;
	LocationManager manager;
	
	// ListView
	private ListView locationList;
	
	// CustomizedAdapter
	private CustomizedAdapter adapter;
	
	// ArrayList to store location names
	private ArrayList<String> locationNames = new ArrayList<String>();
	
	// Variables to store values for SharedPreferences
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String DEST_NAME = "destination_name";
	
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
		
		// load locationList and set the ArrayAdapter with custom Layout listview_items.xml
		locationList = (ListView) findViewById(R.id.ListView);
		adapter = new CustomizedAdapter();
		locationList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_target, menu);
		return true;
	}

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

	// **********************************************************************************************************
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
	}
	// **********************************************************************************************************
	
	public void onCheckBoxClicked(View view) {
		// is the view now checked?
		checked = ((CheckBox) view).isChecked();
	}

	// Source code from button_add
	public void addLocation(View view) {
		
		// contains a name and GPS-coordinates
		DataInterface temp = new DataInterface(); 
		// read out the target's name
		EditText editText = (EditText) findViewById(R.id.set_target_name);
		
		temp.name = editText.getText().toString();

		if (checked) { // Target: current location

			// Check: GPS enabled!
			temp.coordinates = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			SaveLocation(temp.coordinates, temp.name);

		} else { // EXPAND: read in from EditText
		}
		
		// add location name to ArrayList
		locationNames.add(0, editText.getText().toString());
		
		// refresh ArrayAdapter to get new location name
		adapter.notifyDataSetChanged();
		editText.setText("");

		// return to main activity
		 //  setResult(RESULT_OK, null);
		 //  finish();
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
	
	class CustomizedAdapter extends ArrayAdapter<String> {

		// constructor
		public CustomizedAdapter() {
			super(CreateTargetActivity.this, android.R.layout.simple_list_item_1, locationNames);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			
			// if-statement: save resources and processing
			if(row == null) {
				LayoutInflater inflater = getLayoutInflater(); 
				row = inflater.inflate(R.layout.listview_items, null);
			}
			
			
			ImageButton remove_button = (ImageButton) row.findViewById(R.id.remove_location);
			remove_button.setOnClickListener(new DeleteClickListener(position));
			
			ImageButton edit_button = (ImageButton) row.findViewById(R.id.edit_location_name);
			edit_button.setOnClickListener(new editClickListener(position));
			
			((TextView)row.findViewById(R.id.location_name)).setText(locationNames.get(position));
			return row;
		}
	}
	private class editClickListener implements OnClickListener {
		int position;
		
		public editClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTargetActivity.this);

	        alertDialog.setTitle("Rename Location Name");
	        alertDialog.setIcon(R.drawable.ic_menu_manage);

	        // Set an EditText view to get user input 
	        final EditText input = new EditText(CreateTargetActivity.this);
	        alertDialog.setView(input);
	        input.setText(locationNames.get(position));
	  
	        //input.setSelectAllOnFocus(true);

	        		 
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	locationNames.set(position, input.getText().toString());
	            adapter.notifyDataSetChanged();	
	          }
	        });
	 
	        // Setting Negative "No" Button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.cancel();
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
		
		}
	}
	
	private class DeleteClickListener implements OnClickListener {
		int position;
		
		public DeleteClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTargetActivity.this);
			
	        alertDialog.setTitle("Confirm Removal");
	        alertDialog.setMessage("Are you sure you want to delete " + locationNames.get(position) + "?");
	        alertDialog.setIcon(R.drawable.ic_menu_delete);
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	locationNames.remove(position);
	            	adapter.notifyDataSetChanged();	
				}
	        });
	 
	        // Setting Negative "No" Button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.cancel();
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
		
		}
	}
}

