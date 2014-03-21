package nl.avans.hwweek6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.avans.hwweek6.model.Game;
import nl.avans.hwweek6.utils.GameAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	private static final String API_GAMES_URL = 
			"http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=E889B9429FF4CBE7247FA5EBA9B60E60&steamid=";
	private static final String TAG_ID = "appid";
	private static final String TAG_PLAYTWOWEEKS = "playtime_2weeks";
	private static final String TAG_PLAYFOREVER = "playtime_forever";
	private static final String TAG_NAME = "name";
	private static final String TAG_IMGURL = "img_logo_url";
	
	private String userID;
	private ProgressDialog progress;
	private boolean hasConnection = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		progress = new ProgressDialog(this);
		progress.setTitle("Laden");
		progress.setMessage("Even geduld terwijl alles laadt aub...");
		progress.show();
		
		getUserID();
		hasConnection = checkConnection();
		if(hasConnection) {
			new DownloadGamesTask().execute();
		}
		
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
		SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
		prefs.putString("userID", "");
		prefs.commit();
		
		Intent loginWebView = new Intent(this, LoginActivity.class);
		startActivity(loginWebView);
	}
	
	private boolean checkConnection() {
		ConnectivityManager connectManager 	= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
	
	private class DownloadGamesTask extends AsyncTask<String, Void, Game[]> {
		@Override
        protected Game[] doInBackground(String... urls) {
            try {
                return callApi();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Game[] result) {
            setListView(result);
            progress.dismiss();
        }
	}
	
	private void getUserID() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		userID = prefs.getString("userID", "");
	}
	
	public Game[] callApi() throws IOException{
		Game[] games 		= null;
		InputStream stream 	= null;
		
		if(userID != "") {
			try{
				stream 			= downloadUrl(API_GAMES_URL + "76561198063049991");
				String jsonStr 	= convertStreamToString(stream);
				
				JSONObject obj 		= new JSONObject(jsonStr).getJSONObject("response");
				JSONArray gamesStr 	= obj.getJSONArray("games");
				
				games = parseJsonToGames(gamesStr);
			} catch(IOException e) {
				return null;
			} catch (JSONException e) {
				return null;
			}
		}
		
		return games;
	}
	
	private Game[] parseJsonToGames(JSONArray jsonGames) {
		Game[] games = new Game[jsonGames.length()];
		
		int g = 0;
		try {
			for (int i = 0; i < jsonGames.length(); i++) {
				JSONObject jsonGame = jsonGames.getJSONObject(i);
				
				String name 	= jsonGame.getString(TAG_NAME);
				String appid 	= jsonGame.getString(TAG_ID);
				String play2 	= jsonGame.getString(TAG_PLAYTWOWEEKS);
				String playf	= jsonGame.getString(TAG_PLAYFOREVER);
				String imgurl 	= jsonGame.getString(TAG_IMGURL);
				// Create game
				Game game = new Game(appid, name, play2, playf, imgurl, this);
				
				// Add game
				games[g] = game;
				g++;
			}
		} catch (JSONException e) {
			return null;
		}
		return games;
	}
	
	private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
	
	private String convertStreamToString(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder builder = new StringBuilder();
		
		String line = null;
		try{
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
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
