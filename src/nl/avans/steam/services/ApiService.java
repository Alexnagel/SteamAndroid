package nl.avans.steam.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import nl.avans.steam.model.Achievement;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class ApiService {

	private static final String API_USER_URL = 
			"http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=E889B9429FF4CBE7247FA5EBA9B60E60&steamids=%s";
	/*private static final String API_RECENT_GAMES_URL = 
			"http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=E889B9429FF4CBE7247FA5EBA9B60E60&steamid=%s";*/
	private static final String API_OWNED_GAMES_URL = 
			"http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=E889B9429FF4CBE7247FA5EBA9B60E60&steamid=%s&include_appinfo=1&include_played_free_games=1&format=json";
	private static final String API_GAME_ACHIEVEMENTS = 
			"http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v0002/?key=E889B9429FF4CBE7247FA5EBA9B60E60&appid=%d=english&format=json";
	private static final String API_GLOBAL_PERCENTAGE =
			"http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=%d";
	private static final String API_USER_ACHIEVEMENTS =
			"http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?key=E889B9429FF4CBE7247FA5EBA9B60E60&appid=%d&steamid=%s";
	
	private String	userID;
	private Context context;
	
	public ApiService(Context context, String userID) {
		this.context = context;
		this.userID  = userID;
	}
	
	/**
	 * Get the user from the API and set the User class
	 * @return the User
	 */
	public User getUserFromJSON() {
		String     userURLStr = String.format(API_USER_URL, userID);
		User       user 	  = null;
		JSONObject userJSON   = null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			userJSON = apiBackgroundCaller.execute(userURLStr).get();
			if (userJSON != null) {
				JSONObject jsonUser = userJSON.getJSONObject("response").getJSONArray("players").getJSONObject(0);
				user = new User(jsonUser, context);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public String getCurrentGame() {
		String     userURLStr = String.format(API_USER_URL, userID);
		String     game 	  = null;
		JSONObject gameJSON   = null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			gameJSON = apiBackgroundCaller.execute(userURLStr).get();
			if (gameJSON != null) {
				JSONObject jsonUser = gameJSON.getJSONObject("response").getJSONArray("players").getJSONObject(0);
				if(jsonUser.has("gameextrainfo")) {
					game = jsonUser.getString("gameextrainfo");
				} else {
					game = "";
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return game;
	}
	
	public Game getGameFromJSON(int app_id) {
		String gamesURLStr 	 = String.format(API_OWNED_GAMES_URL, userID);
		Game game 	   	 	 = null;
		JSONObject gamesJSON = null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			gamesJSON = apiBackgroundCaller.execute(gamesURLStr).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		JSONArray gameArr = null;
		if(gamesJSON != null) {
			try {
				gameArr = gamesJSON.getJSONObject("response").getJSONArray("games");
		
				if(gameArr != null) {
					
					for (int i = 0; i < gameArr.length(); i++) {
						JSONObject jsonGame = gameArr.getJSONObject(i);
						
						if(jsonGame.getInt("appid") == app_id) {
							game = new Game(jsonGame, context);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return game;
	}
	
	/**
	 * Get the games from the API
	 * @return An Array with all the games
	 */
	public Game[] getGamesFromJSON() {
		String gamesURLStr 	 = String.format(API_OWNED_GAMES_URL, userID);
		Game[] games 	   	 = null;
		JSONObject gamesJSON = null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			gamesJSON = apiBackgroundCaller.execute(gamesURLStr).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		JSONArray gameArr = null;
		if(gamesJSON != null) {
			try {
				gameArr = gamesJSON.getJSONObject("response").getJSONArray("games");
		
				if(gameArr != null) {
					games = new Game[gameArr.length()];
					
					for (int i = 0; i < gameArr.length(); i++) {
						JSONObject jsonGame = gameArr.getJSONObject(i);
						
						Game game = new Game(jsonGame, context);
						games[i] = game;
					}
				} else {
					games = new Game[0];
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return games;
	}
	
	/**
	 * Get the achievements belonging to a game
	 * @param app_id
	 * @return
	 */
	public Achievement[] getAchievementsFromJSON(int app_id) {
		String 		  achievementURLStr = String.format(API_GAME_ACHIEVEMENTS, app_id);
		Achievement[] achievements		= null;
		JSONObject	  achievementJSON	= null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			achievementJSON = apiBackgroundCaller.execute(achievementURLStr).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		JSONArray achievementArr = null;
		if(achievementJSON != null) {
			try {
				achievementArr = achievementJSON.getJSONObject("game").getJSONObject("availableGameStats").getJSONArray("achievements");
				
				if(achievementArr != null) {
					achievements = new Achievement[achievementArr.length()];
					
					for (int i = 0; i < achievementArr.length(); i++) {
						JSONObject jsonAchievement = achievementArr.getJSONObject(i);
						
						jsonAchievement.put("app_id", app_id);
						Achievement achievement = new Achievement(jsonAchievement, context);
						achievements[i] = achievement;
					}
				} else {
					achievements = new Achievement[0];
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
		if (achievements != null){
			// Set the user and global percentages for the achievements
			setUserAchievementsFromJSON(app_id, achievements);
			setGlobalAchievementPercentageFromJSON(app_id, achievements);
			// Sort array on rarity
			Arrays.sort(achievements, new Comparator<Achievement>() {
			    @Override
			    public int compare(Achievement arg0, Achievement arg1) {
			        return Double.compare(arg1.getGlobalPercentage(), arg0.getGlobalPercentage());
			    }
			});
		}
		return achievements;
	}
	
	/**
	 * Set the user achievements
	 * @param app_id The app for which the achievements must be set
	 * @param achievements The achievements that must be set
	 */
	private void setUserAchievementsFromJSON(int app_id, Achievement[] achievements) {
		String 		achievementURLStr 	= String.format(API_USER_ACHIEVEMENTS, app_id, userID);
		JSONObject 	achievementJSON		= null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			achievementJSON = apiBackgroundCaller.execute(achievementURLStr).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		JSONArray achievementArr = null;
		if(achievementJSON != null) {
			try {
				achievementArr = achievementJSON.getJSONObject("playerstats").getJSONArray("achievements");
				
				if(achievementArr != null) {
					for (int i = 0; i < achievementArr.length(); i++) {
						JSONObject jsonAchievement 	= achievementArr.getJSONObject(i);
						
						if(achievements[i].getApiName().equals(jsonAchievement.getString("apiname"))) {
							if(jsonAchievement.getInt("achieved") == 1) {
								achievements[i].setUserAchieved(true);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Set the global achievement percentage for every achievement
	 * @param app_id The app for which the achievement percentages should be retrieved
	 * @param achievements The achievements for which the percentages should be set
	 */
	private void setGlobalAchievementPercentageFromJSON(int app_id, Achievement[] achievements) {
		String achievementURLStr = String.format(API_GLOBAL_PERCENTAGE, app_id);
		JSONObject 	achievementJSON		= null;
		
		try {
			CallApiBackground apiBackgroundCaller = new CallApiBackground();
			achievementJSON = apiBackgroundCaller.execute(achievementURLStr).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		JSONArray achievementArr = null;
		if(achievementJSON != null) {
			try {
				achievementArr = achievementJSON.getJSONObject("achievementpercentages").getJSONArray("achievements");
				
				if(achievementArr != null) {
					for (int i = 0; i < achievementArr.length(); i++) {
						JSONObject jsonAchievement 	= achievementArr.getJSONObject(i);
						
						for (Achievement ach : achievements) {
							if(ach.getApiName().equals(jsonAchievement.getString("name"))) {
								ach.setGlobalPercentage(jsonAchievement.getDouble("percent"));
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Call the API URL and receive the {@link InputStream} data
	 * @param urlString The URL to call
	 * @return The JSON String
	 * @throws IOException 
	 */
	private JSONObject callApiUrl(String urlString) {
		InputStream stream = null;
		try {
			URL url = new URL(urlString);
	        
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        
	        // Starts the query
	        conn.connect();
	        stream = conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        if(stream != null) {
        	return convertStreamToJSONObject(stream);
        }
        
        return null;
    }
	
	/**
	 * Convert a {@link InputStream} to a {@link JSONObject}
	 * @param stream The InputStream to be converted
	 * @return The converted String
	 */
	private JSONObject convertStreamToJSONObject(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder builder = new StringBuilder();
		
		String 	   line	= null;
		JSONObject json = null;
		try{
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
			
			json = new JSONObject(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return json;
	}
	
	private class CallApiBackground extends AsyncTask<String, Void, JSONObject> {
		@Override
        protected JSONObject doInBackground(String... urls) {
            return callApiUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            
        }
	}
}
