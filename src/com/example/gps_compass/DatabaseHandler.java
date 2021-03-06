package com.example.gps_compass;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	//Database version
	private static final int DATABASE_VERSION = 1;
	//Database name
	private static final String DATABASE_NAME = "destinationManager";
	//Destination table name
	private static final String TABLE_DESTINATION = "Destinations";
	
	//Destination table columns names
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_LONGITUDE = "Longitude";
	private static final String KEY_LATITUDE = "Latitude";
	
	//constructor
	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	//Creating tables
	public void onCreate(SQLiteDatabase db){
		String CREATE_DESTINATION_TABLE = 
			"CREATE TABLE " 
		+ TABLE_DESTINATION
		+ "(" 
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_NAME + " TEXT," 
			+ KEY_LONGITUDE + " TEXT," 
			+ KEY_LATITUDE+ " TEXT" 
		+")";
		
		db.execSQL(CREATE_DESTINATION_TABLE);
	}
	
	//Upgrading database
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		//drop older version if existed
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_DESTINATION);
		
		//Create table again
		onCreate(db);
	}
	
	// GETTER- AND SETTER-METHODS
	
	//Adding new Destination
	public long addDestination(DataInterface dest){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, dest.getName()+"");
		values.put(KEY_LONGITUDE, dest.getLongitude()+"");
		values.put(KEY_LATITUDE, dest.getLatitude()+"");
		
		
		// Inserting Row
		long temp = db.insert(TABLE_DESTINATION, null, values);
		db.close();//close database connection
		return temp;
	}
	
	//Getting single Destination
	public DataInterface getDestination(long id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_DESTINATION, new String[]{ KEY_ID,  KEY_NAME, KEY_LONGITUDE, KEY_LATITUDE },KEY_ID + "=?", new String[]{ String.valueOf(id)}, null, null, null, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		DataInterface dest = new DataInterface(cursor.getLong(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)), cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)), cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
		return dest;
	}
	
	// Cursor
	public Cursor getCursor() {
		String[] columns = new String[]{KEY_ID, KEY_NAME};
		Cursor cursor = this.getReadableDatabase().query(TABLE_DESTINATION, columns, null, null, null, null, "_id desc" );
		return cursor;
	}
	
	
	//Getting all Destinations
	public List<DataInterface> getAllDestinations(){
		List<DataInterface> destinationList = new ArrayList<DataInterface>();
		
		//select all query
		String selectQuery = "SELECT * FROM" + TABLE_DESTINATION;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//iterate through all rows and creating a list
		
		if(cursor.moveToFirst()){
			do{
				DataInterface temp = new DataInterface();
				temp.setId(Integer.parseInt(cursor.getString(0)));
				temp.setName(cursor.getString(1));
				temp.setLongitude(cursor.getDouble(2));
				temp.setLongitude(cursor.getDouble(3));
				
				destinationList.add(temp);				
			}while(cursor.moveToNext());
			
		}
		
		return destinationList;
	}
	
	// Getting Destination-counts
	public int getDestinationNumber(){
		
		String countQuery = "SELECT * FROM" + TABLE_DESTINATION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
				
		return cursor.getCount();
	}
	
	// Updating single Destination
	public int updateDestination(long id, String newName){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_NAME, newName);
		
		//updating row
		return db.update(TABLE_DESTINATION, values, KEY_ID +"=?", new String[]{String.valueOf(id)});
	}
	
	// Deleting single Destination
	public void deleteDestination(long id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DESTINATION, KEY_ID + "=?", new String[]{String.valueOf(id)});
	}
	
	

}
