package nl.avans.steam.utils;

import nl.avans.steam.R;
import nl.avans.steam.model.Game;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAdapter extends ArrayAdapter<Game>{
	private Game[]  games;
	private Context context;
	private int 	layout;

	public GameAdapter(Context context, int layoutID, Game[] games) {
		super(context, layoutID, games);
		this.games 		= games;
		this.context 	= context;
		this.layout 	= layoutID;
	}
	
	public void updateGames(Game[] games) {
		this.games = games;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
			v = inflater.inflate(layout, parent, false);
		}

		Game g = games[position];
		if(g != null) {
			ImageView icon = (ImageView) v.findViewById(R.id.gameImage);
			TextView  name = (TextView)  v.findViewById(R.id.gameName);
			TextView  play = (TextView)  v.findViewById(R.id.gamePlayed);

			icon.setImageDrawable(g.getIcon());
			name.setText(g.getName());

			double played = (Double)(Double.parseDouble(g.getPlaytimeTwoWeeks()) / 60);
			double onrecord = (Double)(Double.parseDouble(g.getPlaytimeForever()) / 60);
			if (played == 0)
			{
				play.setText(String.format("%.1f hrs on record", onrecord));
			}
			else
			{
				play.setText(String.format("%.1f hrs on record / %.2f hrs last 2 weeks", onrecord, played));
			}
		}

		return v;
	}
}
