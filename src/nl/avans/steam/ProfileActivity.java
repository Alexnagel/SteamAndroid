package nl.avans.steam;

import nl.avans.steam.fragments.AchievementsFragment;
import nl.avans.steam.fragments.UserFragment;
import nl.avans.steam.interfaces.ProfileActivityInterface;
import nl.avans.steam.interfaces.UserFragmentInterface;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;
import nl.avans.steam.services.DataService;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class ProfileActivity extends Activity implements UserFragmentInterface {

	private ProfileActivityInterface pListener;
	private DataService dataService;
	private User		user  = null;
	private Game[] 		games = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		dataService = DataService.getInstance();
		dataService.init(getApplicationContext());
		
		Intent intent = getIntent();
		user 	= (User)intent.getParcelableExtra("user");
		Parcelable[] ps = intent.getParcelableArrayExtra("games");
		games = new Game[ps.length];
		System.arraycopy(ps, 0, games, 0, ps.length);
		
		setFragments();
		
	}
	
	private void setFragments()
	{
		FrameLayout container = (FrameLayout)findViewById(R.id.frameContainer);
		if(container != null) {
			UserFragment userFragment = UserFragment.newInstance(user, games);
			pListener = (ProfileActivityInterface)userFragment;
			
			if(!isFinishing()) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.add(R.id.frameContainer, userFragment, "UserFragment");
				transaction.commit();
			}
		}
	}
	
	private void refreshData() {
		Thread updateThread = new Thread() {
			
			@Override
			public void run() {
				games = dataService.updateGames();
				pListener.updateGames(games);
				
				String[] userStatus = dataService.getUserStatus();
				pListener.updateUserStatus(Integer.parseInt(userStatus[0]), userStatus[1]);
			}
		};
		
		updateThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.action_logout:
	    	  dataService.logoutUser();
	    	  Intent intent = new Intent();
			  intent.setClass(this, LoginActivity.class);
	    	  startActivity(intent);
	         return true; 
	      case R.id.action_refresh:
	    	  refreshData();
	    	  return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}

	@Override
	public void onGameSelected(int position) {
		Game g = games[position];
		
		AchievementsFragment fragment = AchievementsFragment.newInstance(g, dataService.getAchievements(g.getAppid()));
			
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    transaction.replace(R.id.frameContainer, fragment, "AchievementsFragment");
		transaction.addToBackStack(null);
	    transaction.commit();	    
	}

}
