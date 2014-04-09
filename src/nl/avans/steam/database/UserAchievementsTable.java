package nl.avans.steam.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserAchievementsTable {

	public static final String TABLE_USER_ACHIEVEMENTS	= "userachievements";
	public static final String COLUMN_ID				= "api_name";
	public static final String COLUMN_USER_ID			= "user_id";
	public static final String COLUMN_ACHIEVED			= "achieved";
	
	public static final String DATABASE_CREATE =
			"CREATE TABLE " 
			+ TABLE_USER_ACHIEVEMENTS
			+ "("
			+ COLUMN_ID + " TEXT, "
			+ COLUMN_USER_ID + " INTEGER NOT NULL, "
			+ COLUMN_ACHIEVED + " INTEGER NOT NULL, "
			+ "PRIMARY KEY ( " + COLUMN_ID + ", " + COLUMN_USER_ID + " ), "
			+ "FOREIGN KEY (api_name) REFERENCES achievements(api_name)"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(GamesTable.class.getName(), "Upgrading database from version: " + oldVersion + " to version: " + newVersion);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACHIEVEMENTS);
		onCreate(database);
	}
}
