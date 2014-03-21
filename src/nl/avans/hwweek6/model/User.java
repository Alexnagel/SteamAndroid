package nl.avans.hwweek6.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class User {
	private int steamID;
	private String playerName;
	private String avatarUrl;
	private String lastLogOff;
	
	private Drawable avatar;
	private Activity context;
	
	public User(int steamID, String playerName, String avatarUrl, String lastLogOff, Activity context) {
		this.steamID 	= steamID;
		this.playerName = playerName;
		this.avatarUrl 	= avatarUrl;
		this.lastLogOff = lastLogOff;
		
		this.context = context;
	}
	
	public int getSteamID() {
		return steamID;
	}
	
	public void setSteamID(int steamID) {
		if(steamID != 0) {
			this.steamID = steamID;
		}
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		if(playerName != "") {
			this.playerName = playerName;
		}
	}
	
	public String getLastLogOff() {
		return lastLogOff;
	}
	
	public void setLastLogOff(String lastLogOff) {
		if(lastLogOff != "") {
			this.lastLogOff = lastLogOff;
		}
	}
	
	public Drawable getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatarUrl) {
		if(avatarUrl != "") {
			this.avatarUrl = avatarUrl;
			
			try {
				avatar = Drawable.createFromStream(((InputStream)new URL(this.avatarUrl).getContent()), "Icon");
			} catch (MalformedURLException e) {
				avatar = context.getResources().getDrawable(R.drawable.ic_delete);
			} catch (IOException e) {
				avatar = context.getResources().getDrawable(R.drawable.ic_delete);
			}
		}
		
	}
}
