package nl.avans.steam;

import nl.avans.steam.model.Game;
import nl.avans.steam.services.DataService;
import nl.avans.steam.utils.GameAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	private ProgressDialog 	progress;
	private DataService 	dataService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dataService = DataService.getInstance();
		
		progress = new ProgressDialog(this);
		progress.setTitle("Laden");
		progress.setMessage("Even geduld terwijl alles laadt aub...");
		progress.show();
		
		setContentView(R.layout.activity_profile);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.action_logout:
	    	 logoutUser();
	         return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	private void logoutUser() {
		dataService.logoutUser();
		
		Intent loginWebView = new Intent(this, LoginActivity.class);
		startActivity(loginWebView);
	}
	
	
	public void setListView(Game[] games) {
		ListView gamesList 	= (ListView) findViewById(R.id.gameListView);
		GameAdapter adapter	= new GameAdapter(this, R.layout.list_game, games);
		gamesList.setAdapter(adapter);
		
		gamesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Toast.makeText(getApplicationContext(),
						((TextView) view.findViewById(R.id.gameName)).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}
