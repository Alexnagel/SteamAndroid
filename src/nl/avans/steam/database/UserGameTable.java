package nl.avans.steam.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserGameTable {

	public static final String TABLE_USER_GAMES		= "usergames";
	public static final String COLUMN_USER_ID		= "user_id";
	public static final String COLUMN_APP_ID		= "app_id";
	public static final String COLUMN_PLAY_2WEEKS	= "playtime_two_weeks";
	public static final String COLUMN_PLAY_FOREVER	= "playtime_forever";
	
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "
			+ TABLE_USER_GAMES
			+ "("
			+ COLUMN_APP_ID + " INTEGER NOT NULL, "
			+ COLUMN_USER_ID + " TEXT NOT NULL, "
			+ COLUMN_PLAY_2WEEKS + " DOUBLE NOT NULL, "
			+ COLUMN_PLAY_FOREVER + " DOUBLE NOT NULL, "
			+ "PRIMARY KEY ( " + COLUMN_APP_ID + " , " + COLUMN_USER_ID + ") , "
			+ "FOREIGN KEY (app_id) REFERENCES games(id)"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(GamesTable.class.getName(), "Upgrading database from version: " + oldVersion + " to version: " + newVersion);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GAMES);
		onCreate(database);
	}
}
