package nl.avans.steam.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class Achievement {

	public static final String TAG_ID			= "apiname";
	public static final String TAG_APP_ID		= "app_id";
	public static final String TAG_NAME			= "name";
	public static final String TAG_DESCRIPTION	= "description";
	public static final String TAG_HIDDEN		= "is_hidden";
	public static final String TAG_ICON_ACH		= "icon_achieved";
	public static final String TAG_ICON			= "icon";
	
	private String 	apiName;
	private String 	name;
	private String	description;
	private boolean isHidden;
	private boolean	userAchieved;
	private double 	globalPercentage;
	private String  iconURL;
	private String  iconAchievedURL;
	
	private Drawable icon;
	private Drawable iconAchieved;
	
	private Context  context;
	
	public Achievement(String apiName, String name, String description, boolean isHidden, 
			boolean userAchieved, double globalPercentage, String iconURL, String iconAchURL, Context context) {
		this.apiName 			= apiName;
		this.name 		 		= name;
		this.description 		= description;
		this.isHidden	 		= isHidden;
		this.userAchieved		= userAchieved;
		this.globalPercentage	= globalPercentage;
		this.iconURL			= iconURL;
		this.iconAchievedURL	= iconAchURL;
		
		this.context 			= context;
		
		icon 		 = getImage(this.iconURL);
		iconAchieved = getImage(iconAchievedURL);
	}
	
	public Achievement(JSONObject jsonAchievement, Context context) {
		
	}
	
	public String getApiName() {
		return apiName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
	public boolean getUserAchieved() {
		return userAchieved;
	}
	
	public void setUserAchieved(boolean achieved) {
		userAchieved = achieved;
	}
	
	public double getGlobalPercentage() {
		return globalPercentage;
	}
	
	public void setGlobalPercentage(double percentage) {
		globalPercentage = percentage;
	}
	
	public String getIconURL() {
		return iconURL;
	}
	
	public String getIconAchievedURL() {
		return iconAchievedURL;
	}
	
	public Drawable getIcon() {
		if(userAchieved) {
			return iconAchieved;
		} else {
			return icon;
		}
	}
	
	private Drawable getImage(String url) {
		Drawable img;
		try {
			img = Drawable.createFromStream(((InputStream)new URL(url).getContent()), "Icon");
		} catch (MalformedURLException e) {
			img = context.getResources().getDrawable(R.drawable.ic_delete);
		} catch (IOException e) {
			img = context.getResources().getDrawable(R.drawable.ic_delete);
		}
		
		return img;
	}
}
