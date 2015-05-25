package com.tarena.tlbs;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CopyRightActivity extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_copy_right);
		setView();
	}

	private void setView() {
		webView=(WebView)findViewById(R.id.webView1);
		webView.loadUrl("file:///android_asset/about.html");
		WebSettings setting = webView.getSettings();
		setting.setDefaultTextEncodingName("utf-8");
		setting.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("tel:")){
					Phone phone = new Phone();
					String number=url.substring(url.indexOf(":"));
					Log.i("PhoneNumber:", number);
					phone.call(number);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.copy_right, menu);
		return true;
	}
	final class Phone{
		public void call(String phoneNum){
			Uri uri=Uri.parse("tel:"+phoneNum);
			Intent intent=new Intent(Intent.ACTION_CALL,uri);
			startActivity(intent);
		}
	}

}
