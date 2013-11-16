package com.example.gps_compass;

import android.location.Location;


public class DataInterface {
	
	int id;
	private String name;
	private Location coordinates;
	private double longitude;
	private double latitude;
	
	//constructors
	public DataInterface(){
	}
	public DataInterface(int id, String name, double longitude, double latitude){
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.coordinates.setLatitude(latitude);
		this.coordinates.setLongitude(longitude);
	}
	
	//setters
	public void setCoordinates(Location coord){
		this.coordinates = coord;
		this.longitude = coord.getLongitude();
		this.latitude = coord.getLatitude();
	}
	public void setLatitude(double latitude){
		this.coordinates.setLatitude(latitude);
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude){
		this.coordinates.setLongitude(longitude);
		this.longitude = longitude;
	}
	
	public void setName( String name){
		this.name = name;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	//getters
	public double getLongitude(){
		return this.longitude;
	}

	public double getLatitude(){
		return this.latitude;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Location getCoordinates(){
		return this.coordinates;
	}
	
	public int getId(){
		return this.id;
	}
	
}
