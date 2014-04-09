package nl.avans.steam.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import nl.avans.steam.utils.DrawableDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class Game implements Serializable{
	private static final long serialVersionUID = 7098909000464083523L;
	
	private static final String TAG_ID 				= "appid";
	private static final String TAG_PLAYTWOWEEKS 	= "playtime_2weeks";
	private static final String TAG_PLAYFOREVER 	= "playtime_forever";
	private static final String TAG_NAME 			= "name";
	private static final String TAG_LOGO_URL 		= "img_logo_url";
	private static final String TAG_ICON_URL 		= "img_icon_url";
	
	private int	   appID;
	private String playtimeTwoWeeks;
	private String playtimeForever;
	private String name;
	private String imgIconUrl;
	private String imgLogoUrl;
	
	private Drawable icon;
	private Drawable logo;
	
	private Context  context;
	
	private Achievement[] achievements;
	
	public Game(int appID, String name, String playtimeTwoWeeks, String playtimeForever, String imgIconUrl, String imgLogoUrl, Context context) {
		this.appID 				= appID;
		this.playtimeForever 	= playtimeForever;
		this.playtimeTwoWeeks 	= playtimeTwoWeeks;
		this.name 				= name;
		this.context 			= context;
		
		setIcon(imgIconUrl);
		setLogo(imgLogoUrl);
	}
	
	public Game(JSONObject game, Context context) {
		this.context = context;
		
		try{
			appID 				= game.getInt(TAG_ID);
			name 				= game.getString(TAG_NAME);
			playtimeForever 	= game.getString(TAG_PLAYFOREVER);
			
			if(game.has(TAG_PLAYTWOWEEKS)) {
				playtimeTwoWeeks 	= game.getString(TAG_PLAYTWOWEEKS);
			} else {
				playtimeTwoWeeks	= "0";
			}
			
			setIcon(game.getString(TAG_ICON_URL));
			setLogo(game.getString(TAG_LOGO_URL));
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public int getAppid() {
		return appID;
	}
	
	public void setAppid(int appid){
		if (appid != 0) {
			this.appID = appid;
		}
	}
	
	public String getPlaytimeTwoWeeks() {
		return playtimeTwoWeeks;
	}
	
	public void setPlaytimeTwoWeeks(String playtime) {
		if (playtime != "") {
			playtimeTwoWeeks = playtime;
		}
	}
	
	public String getPlaytimeForever() {
		return playtimeForever;
	}
	
	public void setPlaytimeForever(String playtime) {
		if(playtime != "") {
			playtimeForever = playtime;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name != "") {
			this.name = name;
		}
	}
	
	public Drawable getIcon() {
		return icon;
	}
	
	public String getIconURL() {
		return imgIconUrl;
	}
	
	public void setIcon(String iconurl) {
		if(iconurl != "") {
			imgIconUrl = iconurl;
			
			try{
				icon = getDrawable(imgIconUrl);
			} catch(InterruptedException e) {
				icon =  context.getResources().getDrawable(R.drawable.picture_frame);
			} catch(ExecutionException e) {
				icon =  context.getResources().getDrawable(R.drawable.picture_frame);
			}
		}
	}
	
	public void setAchievements(Achievement[] achievements) {
		this.achievements = achievements;
	}
	
	public Achievement[] getAchievments() {
		return achievements;
	}
	
	public Drawable getLogo() {
		return logo;
	}
	
	public String getLogoURL() {
		return imgLogoUrl;
	}
	
	public void setLogo(String logourl) {
		if(logourl != "") {
			imgLogoUrl = logourl;
			
			try{
				logo = getDrawable(imgLogoUrl);
			} catch(InterruptedException e) {
				logo =  context.getResources().getDrawable(R.drawable.picture_frame);
			} catch(ExecutionException e) {
				logo =  context.getResources().getDrawable(R.drawable.picture_frame);
			}
		}
	}
	
	private Drawable getDrawable(String hash) throws InterruptedException, ExecutionException {
		//Format steam icon url
	     String url 			= "http://media.steampowered.com/steamcommunity/public/images/apps/{0}/{1}.jpg";
	     url 					= MessageFormat.format(url, Integer.toString(appID), hash);
	     
	     DrawableDownloader imageDownloader = new DrawableDownloader();
	     return imageDownloader.execute(url).get();
	}
}
