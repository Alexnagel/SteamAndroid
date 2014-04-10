package nl.avans.steam.model;

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

public class Achievement implements Parcelable{
	
	public static final String TAG_ID			= "name";
	public static final String TAG_APP_ID		= "app_id";
	public static final String TAG_NAME			= "displayName";
	public static final String TAG_DESCRIPTION	= "description";
	public static final String TAG_HIDDEN		= "hidden";
	public static final String TAG_ICON_ACH		= "icon";
	public static final String TAG_ICON			= "icongray";
	
	private String 	apiName;
	private String 	name;
	private String	description;
	private boolean isHidden;
	private boolean	userAchieved;
	private double 	globalPercentage;
	private String  iconURL;
	private String  iconAchievedURL;
	
	private int 	app_id;
	
	private Drawable icon;
	private Drawable iconAchieved;
	
	private Context  context;
	
	public Achievement(String apiName, String name, String description, boolean isHidden, 
			boolean userAchieved, double globalPercentage, String iconURL, String iconAchURL, int app_id , Context context) {
		this.apiName 			= apiName;
		this.name 		 		= name;
		this.description 		= description;
		this.isHidden	 		= isHidden;
		this.userAchieved		= userAchieved;
		this.globalPercentage	= globalPercentage;
		this.iconURL			= iconURL;
		this.iconAchievedURL	= iconAchURL;
		
		this.app_id 			= app_id;
		
		this.context 			= context;
		
		icon 		 = getImage(this.iconURL);
		iconAchieved = getImage(iconAchievedURL);
	}
	
	public Achievement(Parcel in) {
		apiName 			= in.readString();
		name 		 		= in.readString();
		description 		= in.readString();
		isHidden	 		= (in.readInt() == 1) ? true : false;
		iconURL				= in.readString();
		iconAchievedURL		= in.readString();
		app_id 				= in.readInt();
		
		Bitmap iconBitmap = (Bitmap)in.readParcelable(getClass().getClassLoader());
		icon = (BitmapDrawable)new BitmapDrawable(iconBitmap);
		
		Bitmap iconAchBitmap = (Bitmap)in.readParcelable(getClass().getClassLoader());
		iconAchieved = (BitmapDrawable)new BitmapDrawable(iconAchBitmap);
	}
	
	public Achievement(JSONObject jsonAchievement, Context context) {
		try {
			apiName 			= jsonAchievement.getString(TAG_ID);
			name 		 		= jsonAchievement.getString(TAG_NAME);
			description 		= jsonAchievement.getString(TAG_DESCRIPTION);
			isHidden	 		= (jsonAchievement.getInt(TAG_HIDDEN) == 1) ? true : false ;
			iconURL				= jsonAchievement.getString(TAG_ICON);
			iconAchievedURL		= jsonAchievement.getString(TAG_ICON_ACH);
			
			app_id 				= jsonAchievement.getInt(TAG_APP_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.context 			= context;
		
		icon 		 = getImage(this.iconURL);
		iconAchieved = getImage(iconAchievedURL);
	}
	
	public String getApiName() {
		return apiName;
	}
	
	public int getAppID() {
		return app_id;
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
		Drawable img = context.getResources().getDrawable(R.drawable.picture_frame);
		try {
			DrawableDownloader imageDownloader = new DrawableDownloader();
		     img = imageDownloader.execute(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace(); 
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return img;
	}

	public static final Parcelable.Creator<Achievement> CREATOR = new Parcelable.Creator<Achievement>() {
		public Achievement createFromParcel(Parcel in) {
		    return new Achievement(in);
		}
		
		public Achievement[] newArray(int size) {
		    return new Achievement[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {		
		dest.writeString(apiName);
		dest.writeString(description);
		dest.writeInt((isHidden) ? 1 :0);
		dest.writeString(iconURL);
		dest.writeString(iconAchievedURL);
		dest.writeInt(app_id);
		
		Bitmap iconBitmap = (Bitmap)((BitmapDrawable) icon).getBitmap();
		dest.writeParcelable(iconBitmap, flags);
		
		Bitmap iconAchBitmap = (Bitmap)((BitmapDrawable) iconAchieved).getBitmap();
		dest.writeParcelable(iconAchBitmap, flags);
	}
}
