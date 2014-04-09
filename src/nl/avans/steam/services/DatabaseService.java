package nl.avans.steam.services;

import java.util.ArrayList;
import java.util.List;

import nl.avans.steam.database.AchievementsTable;
import nl.avans.steam.database.GamesTable;
import nl.avans.steam.database.SQLiteDatabaseHelper;
import nl.avans.steam.database.UserAchievementsTable;
import nl.avans.steam.database.UserGameTable;
import nl.avans.steam.model.Achievement;
import nl.avans.steam.model.Game;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseService {

	private final static String GAME_QUERY  		= "SELECT * FROM usergames u INNER JOIN games g ON u.app_id = g.id WHERE u.user_id =? AND u.app_id =?";
	private final static String GAMES_QUERY 		= "SELECT * FROM usergames u INNER JOIN games g ON u.app_id = g.id WHERE u.user_id =?";
	private final static String ACHIEVEMENTS_QUERY	= "SELECT * FROM userachievements u INNER JOIN achievements a ON u.api_name = a.id WHERE u.user_id =? AND a.app_id=?";
	
	private SQLiteDatabase 			database;
	private SQLiteDatabaseHelper	dbHelper;
	
	private Context					context;
	private String					userID;
	
	public DatabaseService(Context context, String userID) {
		dbHelper 		= new SQLiteDatabaseHelper(context);
		this.userID 	= userID;
		this.context 	= context;
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Save a game, without user variables.
	 * User variables from game are saved afterwards
	 * @param game
	 * @return
	 */
	public boolean saveGame(Game game) {	
		open();
		
		ContentValues values = new ContentValues();
		values.put(GamesTable.COLUMN_ID, Integer.toString(game.getAppid()));
		values.put(GamesTable.COLUMN_NAME, game.getName());
		values.put(GamesTable.COLUMN_ICON_URL, game.getIconURL());
		values.put(GamesTable.COLUMN_LOGO_URL, game.getLogoURL());
		
		long insertedID = database.insert(GamesTable.TABLE_GAMES, null, values);
		if(insertedID == -1)
			return false;
		
		close();
		
		return saveUserGame(game);
	}
	
	public boolean saveUserGame(Game game) {
		boolean success = true;
		open();
		
		ContentValues values = new ContentValues();
		values.put(UserGameTable.COLUMN_USER_ID, userID);
		values.put(UserGameTable.COLUMN_APP_ID, Integer.toString(game.getAppid()));
		values.put(UserGameTable.COLUMN_PLAY_2WEEKS, game.getPlaytimeTwoWeeks());
		values.put(UserGameTable.COLUMN_PLAY_FOREVER, game.getPlaytimeForever());
		
		long insertedID = database.insert(UserGameTable.TABLE_USER_GAMES, null, values);
		if(insertedID == -1)
			success = false;
		
		close();
		return success;
	}
	
	public Game getGame(int app_id) {
		open();
		
		String[] queryVal = new String[]{userID, Integer.toString(app_id)};
		Cursor cursor = database.rawQuery(GAME_QUERY, queryVal);
		cursor.moveToFirst();
		
		close();
		return cursorToGame(cursor);
	}
	
	public Game[] getGames() {
 		open();
		
		String[] queryVal = new String[]{userID};
		Cursor cursor = database.rawQuery(GAMES_QUERY, queryVal);
		cursor.moveToFirst();
		
		List<Game> games = new ArrayList<Game>();
		while(!cursor.isAfterLast()) {
			games.add(cursorToGame(cursor));
			cursor.moveToNext();
		}
		
		close();
		
		if(games.isEmpty()) {
			return null;
		} else {
			return games.toArray(new Game[games.size()]);
		}
	}
	
	private Game cursorToGame(Cursor cursor) {
		int appID 				= cursor.getInt(1);
		String name 			= cursor.getString(5);
		String playtimeTwoWeeks = cursor.getString(2);
		String playtimeForever  = cursor.getString(3);
		String imgIconUrl 		= cursor.getString(6);
		String imgLogoUrl		= cursor.getString(7);
		
		return new Game(appID, name, playtimeTwoWeeks, playtimeForever, imgIconUrl, imgLogoUrl, context);
	}
	
	public boolean saveAchievement(Achievement achievement) {
		
		ContentValues values = new ContentValues();
		values.put(AchievementsTable.COLUMN_ID, achievement.getApiName());
		values.put(AchievementsTable.COLUMN_APP_ID, Integer.toString(achievement.getAppID()));
		values.put(AchievementsTable.COLUMN_NAME, achievement.getName());
		values.put(AchievementsTable.COLUMN_DESCRIPTION, achievement.getDescription());
		values.put(AchievementsTable.COLUMN_GLOBAL_PERC, achievement.getGlobalPercentage());
		values.put(AchievementsTable.COLUMN_HIDDEN, (achievement.isHidden()) ? 1 : 0);
		values.put(AchievementsTable.COLUMN_ICON, achievement.getIconURL());
		values.put(AchievementsTable.COLUMN_ICON_ACH, achievement.getIconAchievedURL());
		
		open();
		long insertedID = database.insert(AchievementsTable.TABLE_ACHIEVEMENTS, null, values);
		close();
		if(insertedID == -1)
			return false;
	
		return saveUserAchievement(achievement);
	}
	
	public boolean saveUserAchievement(Achievement achievement) {
		boolean success = true;
		
		ContentValues values = new ContentValues();
		values.put(UserAchievementsTable.COLUMN_ID, achievement.getApiName());
		values.put(UserAchievementsTable.COLUMN_USER_ID, userID);
		values.put(UserAchievementsTable.COLUMN_ACHIEVED, (achievement.getUserAchieved()) ? 1 : 0);
		
		open();
		long insertedId = database.insert(UserAchievementsTable.TABLE_USER_ACHIEVEMENTS, null, values);
		close();
		
		if(insertedId == -1)
			success = false;
		
		return success;
	}
	
	public Achievement[] getAchievements(int app_id) {
		open();
		
		String[] queryVal = new String[]{ userID, Integer.toString(app_id) };
		Cursor cursor  = database.rawQuery(ACHIEVEMENTS_QUERY, queryVal);
		cursor.moveToFirst();
		
		List<Achievement> achievements = new ArrayList<Achievement>();
		while(!cursor.isAfterLast()) {
			String apiName 			= cursor.getString(0);
			String name				= cursor.getString(5);
			String description		= cursor.getString(6);
			boolean isHidden		= (cursor.getInt(8) == 1) ? true : false;
			boolean userAchieved	= (cursor.getInt(2) == 1) ? true : false;
			double globalPercentage = cursor.getDouble(7);
			String iconURL			= cursor.getString(9);
			String iconAchURL		= cursor.getString(10);
			
			Achievement ach = new Achievement(apiName, name, description, isHidden, userAchieved, globalPercentage, iconURL,iconAchURL, app_id, context);
			achievements.add(ach);
			
			cursor.moveToNext();
		}
		close();
		
		if(achievements.isEmpty()) {
			return null;
		} else {
			return achievements.toArray(new Achievement[achievements.size()]);
		}
	}
}
