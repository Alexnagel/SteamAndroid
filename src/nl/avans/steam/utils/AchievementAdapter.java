package nl.avans.steam.utils;

import nl.avans.steam.R;
import nl.avans.steam.model.Achievement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementAdapter extends ArrayAdapter<Achievement>{
	private Achievement[]  items;
	private Context context;
	private int 	layout;

	public AchievementAdapter(Context context, int layoutID, Achievement[] items) {
		super(context, layoutID, items);
		this.items 		= items;
		this.context 	= context;
		this.layout 	= layoutID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
			v = inflater.inflate(layout, parent, false);
		}

		Achievement a = items[position];
		if(a != null) {
			ImageView icon = (ImageView) v.findViewById(R.id.achievementImage);
			TextView  name = (TextView)  v.findViewById(R.id.achievementName);
			TextView  desc = (TextView)  v.findViewById(R.id.achievementDescription);
			TextView  perc = (TextView)  v.findViewById(R.id.achievementGlobalPercentage);

			icon.setImageDrawable(a.getIcon());
			name.setText(a.getName());
			desc.setText(a.getDescription());
			double percentage = a.getGlobalPercentage();
			perc.setText(String.format("%.2f%%", percentage) + " of all players");
		}

		return v;
	}
}
