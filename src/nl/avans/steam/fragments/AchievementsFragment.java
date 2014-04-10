package nl.avans.steam.fragments;

import nl.avans.steam.R;
//import nl.avans.steam.interfaces.AchievementListInterface;
import nl.avans.steam.model.Game;
import nl.avans.steam.model.Achievement;
import nl.avans.steam.utils.AchievementAdapter;
//import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
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
		args.putParcelable("game", game);
		args.putParcelableArray("achievements", achievements);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			game 			= (Game)getArguments().getParcelable("game");
			Parcelable[] ps = getArguments().getParcelableArray("achievements");
			
			achievements	= new Achievement[ps.length];
			System.arraycopy(ps, 0, achievements, 0, ps.length);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_achievements, container, false);
		
		if (achievements != null && achievements.length > 0)
		{
			setAchievementsList(v);
		}
		setGameItems(v);
		
		return v;
	}
	
	private void setGameItems(View v) {
		TextView  gameLabel = (TextView)v.findViewById(R.id.gamenameLabel);
		TextView  totalHoursLabel = (TextView)v.findViewById(R.id.totalHoursLabel);
		TextView  progressLabel = (TextView)v.findViewById(R.id.achievementCompletionLabel);
		ImageView gameImage = (ImageView)v.findViewById(R.id.gameImage);
		
		gameLabel.setText(game.getName());
		totalHoursLabel.setText(String.format("%.2f hrs on records",(Double.parseDouble(game.getPlaytimeForever())/60)));
		progressLabel.setText(game.getAchievementProgress(achievements));
		gameImage.setImageDrawable(game.getLogo());
	}

	private void setAchievementsList(View v) {
		ListView achievementsList = (ListView)v.findViewById(R.id.achievementsList);
		AchievementAdapter adapter = new AchievementAdapter(getActivity(), R.layout.achievement_list_item, achievements);
		achievementsList.setAdapter(adapter);
	}
}
