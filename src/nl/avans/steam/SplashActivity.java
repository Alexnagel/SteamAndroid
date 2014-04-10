package nl.avans.steam;

import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;
import nl.avans.steam.services.DataService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		final SplashActivity splash   = this;
		final DataService dataService = DataService.getInstance();
		dataService.init(getApplicationContext());
		
		Thread startThread = new Thread() {
			@Override
			public void run() {
				User 	user 	= dataService.getUser();
				Game[] 	games 	= dataService.getGames();
				
				Intent intent = new Intent();
				intent.setClass(splash, ProfileActivity.class);
				
				intent.putExtra("user", user);
				intent.putExtra("games", games);
				
				startActivity(intent);
			}
		};
		startThread.start();
	}
}
