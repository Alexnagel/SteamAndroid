package nl.avans.steam.services;

import nl.avans.steam.model.Achievement;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class DataService {

private static DataService dInstance = null;
	
	private static final String USER_KEY = "user_%s";
	
	private SharedPreferences 	preferences;
	private ApiService 			apiService;
	private DatabaseService		databaseService;
	private Context				context;
	
	private String 				userID;
	
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
	
	public void init(Context context) {
		if(this.context == null) {
			this.context = context;
			
			preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
			userID 		= preferences.getString("userID", "");
			
			// FOR DEBUG
			userID 		= "76561198063049991"; 
			
			apiService 		= new ApiService(this.context, userID);
			databaseService = new DatabaseService(this.context, userID);
		}
	}
	
	/**
	 * Check if the device has an active internet connection
	 * @return true if has connection
	 */
	private boolean checkConnection() {
		ConnectivityManager connectManager 	= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
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
				user = new User(userJSON, context);
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
	
	public String[] getUserStatus() {
		User user = apiService.getUserFromJSON();
		
		String status = Integer.toString(user.getOnlineStatus());
		String game	  = apiService.getCurrentGame();
		
		return new String[]{status, game};
	}
	
	/**
	 * Get a specified game {@link Game}
	 * @param app_id The game id
	 * @return The game
	 */
	public Game getGame(int app_id) {
		Game game = null;
		game = databaseService.getGame(app_id);
		if(game == null) {
			game = apiService.getGameFromJSON(app_id);
			
			final Game thrGame = game;
			Thread thread = new Thread() {
				@Override
				public void run() {
					databaseService.saveGame(thrGame);
				}
			};
			thread.start();
		}
		
		return game;
	}
	
	/**
	 * Get all recently played games{@link Game} 
	 * @return The Games
	 */
	public Game[] getGames() {
		Game[] games = null;
		
		games = databaseService.getGames();
		if(games == null) {
			games = apiService.getGamesFromJSON();
			
			final Game[] thrGames = games;
			Thread thread = new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < thrGames.length; i++) {
						databaseService.saveGame(thrGames[i]);
					}
				}
			};
			thread.start();
		}
		return games;
	}
	
	/**
	 * Get the achievements for a specified {@link Game}
	 * @param app_id The game id
	 * @return array with all the achievements
	 */
	public Achievement[] getAchievements(int app_id) {
		Achievement[] achievements = null;
		
		achievements = databaseService.getAchievements(app_id);
		if(achievements == null) {
			achievements = apiService.getAchievementsFromJSON(app_id);
			
			final Achievement[] thrAchievements = achievements;
			if (achievements != null){
				Thread thread = new Thread() {
					@Override
					public void run() {
						for (int i = 0; i < thrAchievements.length; i++) {
							databaseService.saveAchievement(thrAchievements[i]);
						}
					}
				};
				thread.start();
			}
		}
		return achievements;
	}
	
	public Game[] updateGames() {
		Game[] games = apiService.getGamesFromJSON();
		
		final Game[] thrGames = games;
		Thread thread = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < thrGames.length; i++) {
					databaseService.saveGame(thrGames[i]);
				}
			}
		};
		thread.start();
		
		return games;
	}
	
	public User updateUser() {
		User user = apiService.getUserFromJSON();
		saveUser(user);
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
