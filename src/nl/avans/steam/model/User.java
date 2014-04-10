package nl.avans.steam.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import nl.avans.steam.utils.DrawableDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	
	private static final String STEAM_ID 		= "steamid";
	private static final String PLAYER_NAME 	= "personaname";
	private static final String AVATAR_URL  	= "avatarfull";
	private static final String LAST_LOG_OFF	= "lastlogoff";
	private static final String ONLINE_STATE	= "personastate";
	private static final String LAST_UPDATED 	= "lastupdated";
	
	private int 	steamID;
	private String 	playerName;
	private String 	avatarUrl;
	private String 	lastLogOff;
	private int 	onlineState;
	private Date	lastUpdated;
	
	private Drawable avatar;
	private Context context;
	
	public User(int steamID, String playerName, String avatarUrl, String lastLogOff, Context context) {
		this.steamID 	= steamID;
		this.playerName = playerName;
		this.lastLogOff = lastLogOff;
		
		this.context 	= context;
		
		setAvatar(avatarUrl);
	}
	
	@SuppressWarnings("deprecation")
	private User(Parcel in) {
		steamID 	= in.readInt();
		playerName 	= in.readString();
		avatarUrl   = in.readString();
		lastLogOff  = in.readString();
		onlineState = in.readInt();
		
		Long lastUpdate = in.readLong();
		lastUpdated 	  = new Date(lastUpdate);
		
		Bitmap bitmap = (Bitmap)in.readParcelable(getClass().getClassLoader());
		avatar = new BitmapDrawable(bitmap);
	}
	
	public User(JSONObject userJSON, Context context) {
		this.context = context;
		
		try {
			steamID 	= userJSON.getInt(STEAM_ID);
			playerName 	= userJSON.getString(PLAYER_NAME);
			onlineState	= userJSON.getInt(ONLINE_STATE);
			lastLogOff 	= userJSON.getString(LAST_LOG_OFF);
			
			setAvatar(userJSON.getString(AVATAR_URL));
			
			if(!userJSON.has(LAST_UPDATED)) {
				lastUpdated = new Date();
			} else {
				SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
				lastUpdated = format.parse(userJSON.getString(LAST_UPDATED));
			}
		} catch (JSONException e) {
			lastUpdated = new Date();
			e.printStackTrace();
		} catch (ParseException e) {
			lastUpdated = new Date();
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a JSON string from the user class
	 * @return string containing JSON
	 */
	public String toJSONString() {
		String userStr = "";
		
		userStr += "{\"steamid\":" + steamID + ",";
		userStr += "\"personaname\":\"" + playerName + "\",";
		userStr += "\"avatarfull\":\"" + avatarUrl + "\",";
		userStr += "\"lastlogoff\":" + lastLogOff + ",";
		userStr += "\"personastate\":" + onlineState + ",";
		userStr += "\"lastupdated\":\"" + lastUpdated + "\"}";
		
		return userStr;
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
	
	public int getOnlineStatus() {
		return onlineState;
	}
	
	public void setOnlineStatus(int status) {
		if(status != 0) {
			onlineState = status;
		}
	}
	
	public Drawable getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatarUrl) {
		if(avatarUrl != "") {
			this.avatarUrl = avatarUrl;
			
			DrawableDownloader imageDownloader = new DrawableDownloader();
			try {
				avatar = imageDownloader.execute(avatarUrl).get();
				
				if(avatar == null)
					avatar = context.getResources().getDrawable(R.drawable.picture_frame);
			} catch (InterruptedException e) {
				avatar = context.getResources().getDrawable(R.drawable.picture_frame);
				e.printStackTrace();
			} catch (ExecutionException e) {
				avatar = context.getResources().getDrawable(R.drawable.picture_frame);
				e.printStackTrace();
			}
		}
		
	}

	public static final Parcelable.Creator<User> CREATOR
		    = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
		    return new User(in);
		}
		
		public User[] newArray(int size) {
		    return new User[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(steamID);
		dest.writeString(playerName);
		dest.writeString(avatarUrl);
		dest.writeString(lastLogOff);
		dest.writeInt(onlineState);
		dest.writeLong(lastUpdated.getTime());
		
		Bitmap bitmap = (Bitmap)((BitmapDrawable) avatar).getBitmap();
		dest.writeParcelable(bitmap, flags);
	}
}
