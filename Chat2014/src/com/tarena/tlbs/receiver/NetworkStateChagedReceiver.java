package com.tarena.tlbs.receiver;

import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateChagedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			if (networkInfo == null) {
				LogUtil.i("NetworkState", "用户断开网络");

			} else {

				LogUtil.i("NetworkState", "用户打开了网络");
				// 重新连接
				// 重新登录

				// 下载1G
				// 30块钱 包月4G 320MB,280MB
				// 1024byte=1 1G=1024 100
				NetworkInfo wifi = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi != null && wifi.isConnected()) {
					LogUtil.i("NetworkState", "用户打开了wifi网络");

				}
				NetworkInfo mobile = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobile != null && mobile.isConnected()) {
					LogUtil.i("NetworkState", "用户打开了移动网络");

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			ExceptionUtil.handle(e);
		}
	}

}
