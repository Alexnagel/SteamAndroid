package nl.avans.steam.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GamesTable {

	public static final String TABLE_GAMES 			= "games";
	public static final String COLUMN_ID			= "id";
	public static final String COLUMN_NAME			= "name";
	public static final String COLUMN_ICON_URL		= "icon";
	public static final String COLUMN_LOGO_URL		= "logo";
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "
			+ TABLE_GAMES
			+ "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_ICON_URL + " TEXT NOT NULL, "
			+ COLUMN_LOGO_URL + " TEXT NOT NULL"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(GamesTable.class.getName(), "Upgrading database from version: " + oldVersion + " to version: " + newVersion);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
		onCreate(database);
	}
}
