package nl.avans.steam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity {

	private static final String LOGIN_URL = "https://steamcommunity.com/login/home/";
	private WebView 			loginWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check for login cookie, if available start profile Activity
		if (!hasLoginCookie()) {
			// Request the progressbar feature for webview
			getWindow().requestFeature(Window.FEATURE_PROGRESS);
			setContentView(R.layout.activity_login);
			
			// Start the webview
			startWebView();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.action_refresh:
	    	  loginWebView.reload();
	         return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	private boolean hasLoginCookie() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String userID 			= prefs.getString("userID", "");
		
		if(userID != "") {
			Intent splashView = new Intent(this, SplashActivity.class);
			startActivity(splashView);
			return true;
		}
		return false;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void startWebView() {
		loginWebView = (WebView) findViewById(R.id.loginWebView);

		loginWebView.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;

		loginWebView.setWebViewClient(new WebViewClient(){
			@Override
	         public void onPageFinished(WebView view, String url) {
	        	 super.onPageFinished(view, url);
	        	 
	        	 try {
	        		//Get the steamLogin cookie
		        	 String steamLogin = getCookie("https://steamcommunity.com", "steamLogin");
		        	 if(steamLogin != null) {
		        		 String[] loginArr 	= steamLogin.split("%7C");
		        		 String userID 		= loginArr[0];
		        		 
		        		 SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit();
		        		 editor.putString("userID", userID);
		        		 editor.commit();
		        		 
		        		 hasLoginCookie();
		        	 }
	        	 } catch (NullPointerException e) {
	        		 view.loadUrl(LOGIN_URL);
	        		 e.printStackTrace();
	        	 }
            }
		});
		loginWebView.setWebChromeClient(new WebChromeClient(){

		         public void onProgressChanged(WebView view, int progress) {
	                 activity.setTitle("Loading...");
	                 activity.setProgress(progress * 100);
	                    if(progress == 100)
	                       activity.setTitle("Login to Steam");
		         }
		});

		loginWebView.loadUrl(LOGIN_URL);
	}
	
	public String getCookie(String siteName,String CookieName){     
	    String CookieValue = null;

	    CookieManager cookieManager = CookieManager.getInstance();
	    String cookies = cookieManager.getCookie(siteName);
	    String[] temp=cookies.split("[;]");
	    
	    for (String ar1 : temp ){
	        if(ar1.contains(CookieName)){
	            String[] temp1=ar1.split("[=]");
	            CookieValue = temp1[1];
	        }
	    }
	    
	    // For logging out, remove steamLogin
	    String cookieStr = "steamLogin='';expires=Mon, 12 Mar 2014 10:10:10 UTC";
	    cookieManager.setCookie("https://steamcommunity.com", cookieStr);
	    
	    return CookieValue; 
	}

}
