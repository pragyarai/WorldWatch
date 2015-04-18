package com.example.madproject;

import com.parse.Parse;

import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "WhqWj009luOxOtIH3rM9iWJICLdf0NKbgqdaui8Q", "2J6eFTDshoQDmvcFArvOF99L6PtpSALmnEy1RjBJ");

	}
}
