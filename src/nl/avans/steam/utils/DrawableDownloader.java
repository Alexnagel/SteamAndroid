package nl.avans.steam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class DrawableDownloader extends AsyncTask<String, Void, Drawable> {

	@Override
	protected Drawable doInBackground(String... params) {
		Drawable image = null;
		try {
			image = Drawable.createFromStream(((InputStream)new URL(params[0]).getContent()), "Icon");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}

}
