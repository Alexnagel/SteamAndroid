package nl.avans.steam.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.avans.steam.R;
import nl.avans.steam.interfaces.UserFragmentInterface;
import nl.avans.steam.interfaces.ProfileActivityInterface;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;
import nl.avans.steam.utils.GameAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class UserFragment extends Fragment implements ProfileActivityInterface {
	
	private User 	user;
	private Game[] 	games;
	
	private UserFragmentInterface gListener;
	
	public UserFragment() {
		// Required empty public constructor
	}
	
	public static UserFragment newInstance(User user, Game[] games) {
		UserFragment f = new UserFragment();
		
		Bundle args = new Bundle();
		//args.putSerializable("user", user);
		args.putParcelable("user", user);
		args.putSerializable("games", games);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			user 	= (User)getArguments().getParcelable("user");
			games	= (Game[])getArguments().getSerializable("games");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_user, container, false);
		
		setUserItems(v);
		setGamesList(v);
		
		return v;
	}
	
	private void setUserItems(View v) {
		TextView  userLabel = (TextView)v.findViewById(R.id.usernameLabel);
		TextView  lastLabel = (TextView)v.findViewById(R.id.lastSeenLabel);
		ImageView userImage = (ImageView)v.findViewById(R.id.userImage);
		
		userLabel.setText(user.getPlayerName());
		lastLabel.setText("Last seen: " + getLogOffTime(Long.parseLong(user.getLastLogOff())));
		userImage.setImageDrawable(user.getAvatar());
	}
	
	private String getLogOffTime(long timestamp) {
		Date lastSeenDate = new Date((timestamp * 1000));
		SimpleDateFormat format = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
		
		return format.format(lastSeenDate);
	}

	private void setGamesList(View v) {
		ListView gamesList = (ListView)v.findViewById(R.id.gamesList);
		GameAdapter adapter = new GameAdapter(getActivity(), R.layout.game_list_item, games);
		gamesList.setAdapter(adapter);
		
		gamesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				ProgressBar progress = (ProgressBar)getView().findViewById(R.id.gameProgress);
				progress.setVisibility(ProgressBar.VISIBLE);
				gameSelected(position);
				progress.setVisibility(ProgressBar.INVISIBLE);
			}
		});
	}
	
	public void gameSelected(int position) {
		gListener.onGameSelected(position);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		gListener = null;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			gListener = (UserFragmentInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement GameListInterface");
		}
	}

	@Override
	public void updateUserStatus(int onlineStatus, String currentGame) {
		TextView  lastLabel = (TextView)getView().findViewById(R.id.lastSeenLabel);
		String lastSeenLabel = "";
		
		if (currentGame.equals("")) {
			
			switch(onlineStatus) {
				case 1: lastSeenLabel = "Online"; break;
				case 2: lastSeenLabel = "Busy"; break;
				case 3: lastSeenLabel = "Away"; break;
				case 4: lastSeenLabel = "Snooze"; break;
				case 5: lastSeenLabel = "Looking to trade"; break;
				case 6: lastSeenLabel = "Looking to play"; break;
				default: lastSeenLabel = "Last seen:" + getLogOffTime(System.currentTimeMillis()); break; 
			}
		} else {
			lastSeenLabel = "In-game: " + currentGame;
		}
		
		if (!lastSeenLabel.equals(lastLabel.getText())) {
			lastLabel.setText(lastSeenLabel);
		}
	}

	@Override
	public void updateGames(Game[] games) {
		ListView gamesList = (ListView)getView().findViewById(R.id.gamesList);
		final GameAdapter adapter = (GameAdapter) gamesList.getAdapter();
		adapter.updateGames(games);
		
		getActivity().runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	adapter.notifyDataSetChanged();
		        }
		});
	}
}
