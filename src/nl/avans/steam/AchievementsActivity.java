package nl.avans.steam;

import nl.avans.steam.fragments.AchievementsFragment;
import nl.avans.steam.interfaces.AchievementListInterface;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.Achievement;
import nl.avans.steam.services.DataService;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AchievementsActivity extends Activity implements AchievementListInterface {

	private DataService dataService;
	private Game			game  = null;
	private Achievement[] 	achievements = null;
	private int app_id;
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievements);
		app_id = this.getIntent().getIntExtra("app_id", -1);
		Toast.makeText(getApplicationContext(), "App_ID: "+app_id,
				   Toast.LENGTH_LONG).show();
		dataService = DataService.getInstance();
		dataService.init(getApplicationContext());
		
		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("One moment please, achievements are being loaded...");
		progress.show();
		
		Thread startThread = new Thread() {
			@Override
			public void run() {
				game 	= dataService.getGame(app_id);
				achievements 	= dataService.getAchievements(app_id);
				
				progress.dismiss();
				setFragments();
			}
		};
		startThread.start();
		
	}
	
	private void setFragments()
	{
		FrameLayout container = (FrameLayout)findViewById(R.id.frameContainer);
		if(container != null) {
			AchievementsFragment achievementsFragment = AchievementsFragment.newInstance(game, achievements);
			
			if(!isFinishing()) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.add(R.id.frameContainer, achievementsFragment, "AchievementsFragment");
				transaction.commit();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public void onAchievementSelected(int position) {
		// TODO Auto-generated method stub
		
	}

}
