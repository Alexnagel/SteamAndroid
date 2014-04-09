package nl.avans.steam.fragments;

import nl.avans.steam.R;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.User;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class UserFragment extends Fragment {
	
	private User 	user;
	private Game[] 	games;
	
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
			
			setUserItems();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_user, container, false);
		
		setUserItems();
		
		return v;
	}
	
	private void setUserItems() {
		TextView  userLabel = (TextView)getView().findViewById(R.id.usernameLabel);
		TextView  lastLabel = (TextView)getView().findViewById(R.id.lastSeenLabel);
		ImageView userImage = (ImageView)getView().findViewById(R.id.userImage);
		
		userLabel.setText(user.getPlayerName());
		lastLabel.setText(user.getLastLogOff());
		userImage.setImageDrawable(user.getAvatar());
	}

}
