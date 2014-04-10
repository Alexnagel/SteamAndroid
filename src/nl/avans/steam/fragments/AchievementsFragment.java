package nl.avans.steam.fragments;

import nl.avans.steam.R;
//import nl.avans.steam.interfaces.AchievementListInterface;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.Achievement;
import nl.avans.steam.utils.AchievementAdapter;
//import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.app.Fragment} subclass.
 * 
 */
public class AchievementsFragment extends Fragment {
	
	private Game 	game;
	private Achievement[] 	achievements;
	
	//private AchievementListInterface aListener;
	
	public AchievementsFragment() {
		// Required empty public constructor
	}
	
	public static AchievementsFragment newInstance(Game game, Achievement[] achievements) {
		AchievementsFragment f = new AchievementsFragment();
		
		Bundle args = new Bundle();
		args.putSerializable("game", game);
		args.putSerializable("achievements", achievements);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			game 	= (Game)getArguments().getSerializable("game");
			achievements	= (Achievement[])getArguments().getSerializable("achievements");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_achievements, container, false);
		
		setGameItems(v);
		setAchievementsList(v);
		
		return v;
	}
	
	private void setGameItems(View v) {
		TextView  gameLabel = (TextView)v.findViewById(R.id.gamenameLabel);
		TextView  totalHoursLabel = (TextView)v.findViewById(R.id.totalHoursLabel);
		ImageView gameImage = (ImageView)v.findViewById(R.id.gameImage);
		
		gameLabel.setText(game.getName());
		totalHoursLabel.setText(game.getPlaytimeForever());
		gameImage.setImageDrawable(game.getLogo());
	}

	private void setAchievementsList(View v) {
		ListView achievementsList = (ListView)v.findViewById(R.id.achievementsList);
		AchievementAdapter adapter = new AchievementAdapter(getActivity(), R.layout.achievement_list_item, achievements);
		achievementsList.setAdapter(adapter);
		
		/*achievementsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				achievementSelected(position);
			}
		});*/
	}
	
	/*public void achievementSelected(int position) {
		aListener.onAchievementSelected(position);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		aListener = null;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			aListener = (AchievementListInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement AchievementListInterface");
		}
	}*/
}