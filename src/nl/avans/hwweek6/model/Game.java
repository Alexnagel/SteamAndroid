package nl.avans.hwweek6.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import android.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class Game {
	
	private String appID;
	private String playtimeTwoWeeks;
	private String playtimeForever;
	private String name;
	private String imgIconUrl;
	
	private Drawable icon;
	private Activity context;
	
	public Game(String appID, String name, String playtimeTwoWeeks, String playtimeForever, String imgIconUrl, Activity context) {
		this.appID = appID;
		this.playtimeForever = playtimeForever;
		this.playtimeTwoWeeks = playtimeTwoWeeks;
		this.name = name;
		this.imgIconUrl = imgIconUrl;
		this.context = context;
		
		try{
			getDrawable();
		} catch(IOException e) {
			icon =  context.getResources().getDrawable(R.drawable.ic_delete);
		}
	}
	
	public String getAppid() {
		return appID;
	}
	
	public void setAppid(String appid){
		if (appid != "") {
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
	
	public void setIcon(String iconurl) {
		if(iconurl != "") {
			imgIconUrl = iconurl;
			
			try{
				getDrawable();
			} catch(IOException e) {
				icon =  context.getResources().getDrawable(R.drawable.picture_frame);
			}
		}
	}
	
	private void getDrawable() throws IOException, MalformedURLException {
		//Format steam icon url
	     String url 			= "http://media.steampowered.com/steamcommunity/public/images/apps/{0}/{1}.jpg";
	     url 					= MessageFormat.format(url, appID, imgIconUrl);
	     
	     icon = Drawable.createFromStream(((InputStream)new URL(url).getContent()), "Icon");
	}
}