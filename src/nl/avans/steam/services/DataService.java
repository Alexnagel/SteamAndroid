package nl.avans.steam.services;

import nl.avans.steam.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class DataService extends Application {
	
	private static DataService dInstance = null;
	
	private static final String USER_KEY = "user_%s";
	
	private SharedPreferences 	preferences;
	private ApiService 			apiService;
	
	private String userID;
	
	/**
	 * The private constructor
	 */
	private DataService() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		userID = preferences.getString("userID", "");
		
		apiService = new ApiService();
	}
	
	/**
	 * Get the DataService instance, create one if it's null
	 * @return DataService instance
	 */
	public static DataService getInstance() {
		if(dInstance == null) {
			dInstance = new DataService();
		}
		return dInstance;
	}
	
	/**
	 * Check if the device has an active internet connection
	 * @return true if has connection
	 */
	private boolean checkConnection() {
		ConnectivityManager connectManager 	= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
	
	/**
	 * Get the user. Check if in SharedPreferences, otherwise get from API and save
	 * @return the User
	 */
	public User getUser() {
		User user = null;
		
		String userKey = String.format(USER_KEY, userID);
		String userStr = preferences.getString(userKey, "");
		
		if (userStr != "") {
			
			try {
				JSONObject userJSON = new JSONObject(userStr);
				user = new User(userJSON, getApplicationContext());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} else {
			if(checkConnection()) {
				user = apiService.getUserFromJSON();
				saveUser(user);
			}
		}
		
		return user;
	}
	
	/**
	 * Save the user to {@link SharedPreferences} for quick retrieving in future
	 * @param user The User class to save
	 * @return Boolean for success
	 */
	public boolean saveUser(User user) {
		boolean success = true;
		
		SharedPreferences.Editor editor = preferences.edit();
		String userKey = String.format(USER_KEY, userID);
		
		editor.putString(userKey, user.toJSONString());
		success = editor.commit();
		
		return success;
	}
	
	/**
	 * Logout the user by removing the userID from {@link SharedPreferences}
	 */
	public void logoutUser() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("userID", "");
		editor.commit();
	}
}
