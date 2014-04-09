package nl.avans.steam;

import nl.avans.steam.fragments.UserFragment;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;
import nl.avans.steam.services.DataService;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;

public class ProfileActivity extends Activity {

	private DataService dataService;
	private  User		user;
	private Game[] 		games;
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		dataService = DataService.getInstance();
		dataService.init(getApplicationContext());
		
		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("One moment please, everything is being loaded...");
		progress.show();
		
		Thread startThread = new Thread() {
			@Override
			public void run() {
				user 	= dataService.getUser();
				games 	= dataService.getGames();
				
				progress.dismiss();
			}
		};
		startThread.start();
		
	}
	
	private void setFragments()
	{
		FrameLayout container = (FrameLayout)findViewById(R.id.frameContainer);
		if(container != null) {
//			UserFragment userFragment = UserFragment.newInstance();
//			FragmentTransaction transaction  = getFragmentManager().beginTransaction();
//			
//			transaction.add(R.id.frameContainer, userFragment, "UserFragment");
//			transaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
