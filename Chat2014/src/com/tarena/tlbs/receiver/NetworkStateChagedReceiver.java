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
				LogUtil.i("NetworkState", "�û��Ͽ�����");

			} else {

				LogUtil.i("NetworkState", "�û���������");
				// ��������
				// ���µ�¼

				// ����1G
				// 30��Ǯ ����4G 320MB,280MB
				// 1024byte=1 1G=1024 100
				NetworkInfo wifi = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi != null && wifi.isConnected()) {
					LogUtil.i("NetworkState", "�û�����wifi����");

				}
				NetworkInfo mobile = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobile != null && mobile.isConnected()) {
					LogUtil.i("NetworkState", "�û������ƶ�����");

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			ExceptionUtil.handle(e);
		}
	}

}
