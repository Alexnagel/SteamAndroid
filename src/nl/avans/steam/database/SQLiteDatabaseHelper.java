package nl.avans.steam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME 	 = "steam_avans.db";
	private static final int 	DATABASE_VERSION = 1;
	
	public SQLiteDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		GamesTable.onCreate(database);
		UserGameTable.onCreate(database);
		AchievementsTable.onCreate(database);
		UserAchievementsTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		GamesTable.onUpgrade(database, oldVersion, newVersion);
		UserGameTable.onUpgrade(database, oldVersion, newVersion);
		AchievementsTable.onUpgrade(database, oldVersion, newVersion);
		UserAchievementsTable.onUpgrade(database, oldVersion, newVersion);
	}
}
