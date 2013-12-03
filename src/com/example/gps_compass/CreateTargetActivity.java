package com.example.gps_compass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CreateTargetActivity extends Activity implements LocationListener {

	boolean checked = true;
	LocationManager manager;
	
	// ListView
	private ListView locationList;
	
	// CustomizedAdapter
	private CustomizedAdapter adapter;
	private Cursor cursor;
	private DatabaseHandler handler;


	// Store id (SharedPreferences)
	public static final String ID = "id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_target);

		manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		// get location
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
		// RadioButton default set on
		CheckBox cb = (CheckBox) findViewById(R.id.checkbox_use_current_location);
		cb.setChecked(true);
		
		LinearLayout enterCoordinatesManually = (LinearLayout) findViewById(R.id.coordinates);
		enterCoordinatesManually.setVisibility(View.GONE);
		
		// load locationList and set the ArrayAdapter with custom Layout listview_items.xml
		locationList = (ListView) findViewById(R.id.ListView);
		handler = new DatabaseHandler(this);
		cursor = handler.getCursor();
		adapter = new CustomizedAdapter(cursor);
		locationList.setAdapter(adapter);
		
		// disable  button by default (activate onLocationChanged)
		Button addButton = (Button) findViewById(R.id.add_button);
		addButton.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_target, menu);
		return true;
	}

	// set id for current location
	public void setCurrID(long id) {
		SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = currentLocation.edit();
		editor.putLong(ID, id).commit();
	}
	
	// get id from current location
	public long getCurrID() {
		SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		return currentLocation.getLong(ID, -1);
	}

	public void onCheckBoxClicked(View view) {
		
		// is the view now checked?
		checked = ((CheckBox) view).isChecked();
		LinearLayout coordinates = (LinearLayout) findViewById(R.id.coordinates);
		Button add_button = (Button) findViewById(R.id.add_button);
		// disable/enable coordinates entry on click
		// and control add_button
		coordinates.setVisibility(View.VISIBLE);
		add_button.setEnabled(false);
		
		if (checked) 
		{
			coordinates.setVisibility(View.GONE);
		}

	}
	
	// SCRIPT FROM:
	// http://www.swisstopo.admin.ch/internet/swisstopo/de/home/products/software/products/skripts.html
	
	// Convert CH y/x to WGS lat
	private double CHtoWGSlat(double y, double x) {
		// Converts militar to civil and to unit = 1000km
		// Axiliary values (% Bern)
		double y_aux = (y - 600000) / 1000000;
		double x_aux = (x - 200000) / 1000000;

		// Process lat
		double lat = (16.9023892 + (3.238272 * x_aux))
				- (0.270978 * Math.pow(y_aux, 2))
				- (0.002528 * Math.pow(x_aux, 2))
				- (0.0447 * Math.pow(y_aux, 2) * x_aux)
				- (0.0140 * Math.pow(x_aux, 3));

		// Unit 10000" to 1 " and converts seconds to degrees (dec)
		lat = (lat * 100) / 36;

		return lat;
	}

	// Convert CH y/x to WGS long
	private double CHtoWGSlong(double y, double x) {
		// Converts militar to civil and to unit = 1000km
		// Axiliary values (% Bern)
		double y_aux = (y - 600000) / 1000000;
		double x_aux = (x - 200000) / 1000000;

		// Process long
		double lng = (2.6779094 + (4.728982 * y_aux)
				+ (0.791484 * y_aux * x_aux) + (0.1306 * y_aux * Math.pow(
				x_aux, 2))) - (0.0436 * Math.pow(y_aux, 3));

		// Unit 10000" to 1 " and converts seconds to degrees (dec)
		lng = (lng * 100) / 36;

		return lng;
	}
	
	// Source code from button_add
	public void addLocation(View view) {
		
		// contains a name and GPS-coordinates
		DataInterface temp = new DataInterface(); 
		
		// load DatabaseHandler
		DatabaseHandler handler = new DatabaseHandler(this);

		// read out the target's name
		EditText location_name = (EditText) findViewById(R.id.set_target_name);
		temp.setName(location_name.getText().toString());

		if (checked) {

			// save locations coordinates and set ID
			temp.setCoordinates(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

		} else {
			/*
			// Longitude (WGS84)
			EditText longitude_degree = (EditText) findViewById(R.id.longitude_degree);
			EditText longitude_minutes = (EditText) findViewById(R.id.longitude_minutes);
			EditText longitude_seconds = (EditText) findViewById(R.id.longitude_seconds);
			
			double longDeg = Double.valueOf(longitude_degree.getText().toString());
			double longMin = Double.valueOf(longitude_minutes.getText().toString()) / 60;
			double longSec = Double.valueOf(longitude_seconds.getText().toString()) / 3600;
			
			// Latitude (WGS84)
			EditText latitude_degree = (EditText) findViewById(R.id.latitude_degree);
			EditText latitude_minutes = (EditText) findViewById(R.id.latitude_minutes);
			EditText latitude_seconds = (EditText) findViewById(R.id.latitude_seconds);
			
			double latDeg = Double.valueOf(latitude_degree.getText().toString());
			double latMin = Double.valueOf(latitude_minutes.getText().toString()) / 60;
			double latSec = Double.valueOf(latitude_seconds.getText().toString()) / 3600;
						
			
			temp.setLatitude(Math.signum(latDeg) * Math.abs(latDeg) + latMin + latSec); 
			temp.setLongitude(Math.signum(longDeg) * Math.abs(longDeg)+ longMin + longSec);	
			*/
			
			// Swiss-Grid (CH1903)
			EditText easting = (EditText) findViewById(R.id.easting);
			EditText northing = (EditText) findViewById(R.id.northing);
			
			int y = Integer.valueOf(easting.getText().toString());
			int x = Integer.valueOf(northing.getText().toString());
			
			temp.setLatitude(CHtoWGSlat(y,x));
			temp.setLongitude(CHtoWGSlong(y,x));


			
		}
		
		// add destination and set id
		long id = handler.addDestination(temp);
		temp = handler.getDestination(id);
		setCurrID(id);
		
		// delete location name in editText
		location_name.setText("");
		
		// refresh list
		cursor = handler.getCursor();
    	adapter.changeCursor(cursor);	

		// return to main activity
    	setResult(RESULT_OK, null);
		finish();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		
		// enable Add-Button when GPS is ready
		Button addButton = (Button) findViewById(R.id.add_button);
		addButton.setEnabled(true);
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
	
	class CustomizedAdapter extends CursorAdapter {

			public CustomizedAdapter(Cursor cursor) {
				super(CreateTargetActivity.this, cursor, 2);
				mLayoutInflater = LayoutInflater.from(getApplicationContext()); 
			}
			private LayoutInflater mLayoutInflater;

		@Override
		public void bindView(View row, Context context, Cursor cursor) {
			
			long rowId = cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(0)));

			TextView locationName = (TextView) row.findViewById(R.id.location_name);
	        locationName.setText(handler.getDestination(rowId).getName());
	        
	        RelativeLayout idSetterRow = (RelativeLayout) row.findViewById(R.id.idsetter);
	        idSetterRow.setOnClickListener(new setIdClickListener(rowId));
		    
	        ImageButton remove_button = (ImageButton) row.findViewById(R.id.remove_location);
	        remove_button.setOnClickListener(new DeleteClickListener(rowId));
	        
	        ImageButton edit_button = (ImageButton) row.findViewById(R.id.edit_location_name);	
	        edit_button.setOnClickListener(new editClickListener(rowId));

	        if(rowId == getCurrID()) {
	        	idSetterRow.setBackgroundResource(R.drawable.abc_list_selector_holo_light_current);
	        }
	        else idSetterRow.setBackgroundResource(R.drawable.abc_list_selector_holo_light);
		}
		

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
	        return mLayoutInflater.inflate(R.layout.listview_items, parent, false);
		}
	}
	
	private class setIdClickListener implements OnClickListener {
		long id;
		
		public setIdClickListener(long id) {
			this.id = id;
		}
		
		@Override
		public void onClick(View row) {

			setCurrID(id);
			// Refresh list
			//cursor = handler.getCursor();
        	//adapter.changeCursor(cursor);
			// return to main activity
			setResult(RESULT_OK, null);
			finish();
	        
		}
		
	}
	
	private class editClickListener implements OnClickListener {
		long id;
		
		public editClickListener(long id) {
			this.id = id;
		}

		@Override
		public void onClick(View view) {
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTargetActivity.this);

	        alertDialog.setTitle("Rename Location");
	        alertDialog.setIcon(R.drawable.ic_menu_manage);

	        // Set an EditText view to get user input 
	        final EditText input = new EditText(CreateTargetActivity.this);
	        alertDialog.setView(input);
	        input.setText(handler.getDestination(id).getName());
	        input.setSelectAllOnFocus(true);
	        // show Keyboard while opening alertDialog 
	        input.setOnFocusChangeListener(new OnFocusChangeListener() {
	            @Override
	            public void onFocusChange(View v, boolean hasFocus) {
	                input.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        InputMethodManager inputMethodManager= (InputMethodManager) CreateTargetActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
	                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
	                    }
	                });
	            }
	        });
	       input.requestFocus();
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	handler.updateDestination(id, input.getText().toString());
	        	cursor = handler.getCursor();
            	adapter.changeCursor(cursor);
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
		long id;
		
		public DeleteClickListener(long id) {
			this.id = id;
		}

		@Override
		public void onClick(View view) {
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTargetActivity.this);
			
	        alertDialog.setTitle("Confirm Removal");
	        alertDialog.setMessage("remove " + handler.getDestination(id).getName() + "?");
	        alertDialog.setIcon(R.drawable.ic_menu_delete);
			      
			// Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	// set default ID to -1 if current location was deleted
	            	if(id == getCurrID()) setCurrID(-1);
	            	handler.deleteDestination(id);
	            	
	            	// refresh list
	            	cursor = handler.getCursor();
	            	adapter.changeCursor(cursor);
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

