package com.tarena.tlbs.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	public void checkNetWorkState(final Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
		if(activeNetwork==null){
			AlertDialog.Builder dialog = new Builder(context);
			dialog.setTitle("")
			.setMessage("��û����������")
			.setPositiveButton("����������", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					context.startActivity(intent);
				}
			})
			.setNegativeButton("ȡ��", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();
			
		}
	}

}
