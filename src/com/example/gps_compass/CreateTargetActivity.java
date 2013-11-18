package com.example.gps_compass;

import java.util.ArrayList;

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
		CheckBox cl = (CheckBox) findViewById(R.id.checkbox_use_current_location);
		cl.setChecked(true);
		
		// load locationList and set the ArrayAdapter with custom Layout listview_items.xml
		locationList = (ListView) findViewById(R.id.ListView);
		DatabaseHandler handler = new DatabaseHandler(this);
		Cursor cursor = handler.getCursor();
		adapter = new CustomizedAdapter(cursor);
		locationList.setAdapter(adapter);
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

	public void onCheckBoxClicked(View view) {
		// is the view now checked?
		checked = ((CheckBox) view).isChecked();
	}

	// Source code from button_add
	public void addLocation(View view) {
		
		// contains a name and GPS-coordinates
		DataInterface temp = new DataInterface(); 
		
		// load DatabaseHandler
		DatabaseHandler handler = new DatabaseHandler(this);

		// read out the target's name
		EditText editText = (EditText) findViewById(R.id.set_target_name);
		temp.setName(editText.getText().toString());

		if (checked) {

			// save locations coordinates and set ID
			temp.setCoordinates(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			long id = handler.addDestination(temp);
			temp = handler.getDestination(id);
			setCurrID(id);

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
	
	class CustomizedAdapter extends CursorAdapter {

			public CustomizedAdapter(Cursor cursor) {
				super(CreateTargetActivity.this, cursor, 1);
				mLayoutInflater = LayoutInflater.from(getApplicationContext()); 
			}
			private LayoutInflater mLayoutInflater;
		    
			/**
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
			return row;*/
		

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
		    DatabaseHandler handler = new DatabaseHandler(context);
		    TextView locationName = (TextView) view.findViewById(R.id.location_name);
	        if (locationName != null) {
		       	locationName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
		    }
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
	        View v = mLayoutInflater.inflate(R.layout.listview_items, parent, false);
	        return v;
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
	        //input.setText(locationNames.get(position));
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

