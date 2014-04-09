package nl.avans.steam.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AchievementsTable {
	
	public static final String TABLE_ACHIEVEMENTS	= "achievements";
	public static final String COLUMN_ID			= "id";
	public static final String COLUMN_APP_ID		= "app_id";
	public static final String COLUMN_NAME			= "name";
	public static final String COLUMN_DESCRIPTION	= "description";
	public static final String COLUMN_GLOBAL_PERC	= "global_percentage";
	public static final String COLUMN_HIDDEN		= "is_hidden";
	public static final String COLUMN_ICON_ACH		= "icon_achieved";
	public static final String COLUMN_ICON			= "icon";
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "
			+ TABLE_ACHIEVEMENTS
			+ "("
			+ COLUMN_ID + " TEXT PRIMARY KEY, "
			+ COLUMN_APP_ID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT, "
			+ COLUMN_GLOBAL_PERC + " DOUBLE NOT NULL, "
			+ COLUMN_HIDDEN + " INTEGER NOT NULL, "
			+ COLUMN_ICON_ACH + " TEXT NOT NULL, "
			+ COLUMN_ICON + " TEXT NOT NULL, "
			+ "FOREIGN KEY (app_id) REFERENCES games(id)"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(GamesTable.class.getName(), "Upgrading database from version: " + oldVersion + " to version: " + newVersion);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
		onCreate(database);
	}
}
