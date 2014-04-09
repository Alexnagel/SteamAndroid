package nl.avans.steam.fragments;

import nl.avans.steam.R;
import nl.avans.steam.interfaces.GameListInterface;
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
import android.widget.TextView;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class UserFragment extends Fragment {
	
	private User 	user;
	private Game[] 	games;
	
	private GameListInterface gListener;
	
	public UserFragment() {
		// Required empty public constructor
	}
	
	public static UserFragment newInstance(User user, Game[] games) {
		UserFragment f = new UserFragment();
		
		Bundle args = new Bundle();
		args.putSerializable("user", user);
		args.putSerializable("games", games);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			user 	= (User)getArguments().getSerializable("user");
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
		lastLabel.setText(user.getLastLogOff());
		userImage.setImageDrawable(user.getAvatar());
	}

	private void setGamesList(View v) {
		ListView gamesList = (ListView)v.findViewById(R.id.gamesList);
		GameAdapter adapter = new GameAdapter(getActivity(), R.layout.game_list_item, games);
		gamesList.setAdapter(adapter);
		
		gamesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				gameSelected(position);
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
			gListener = (GameListInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement GameListInterface");
		}
	}
}
