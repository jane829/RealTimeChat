package com.tarena.tlbs.model;

import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.widget.Toast;

import com.tarena.tlbs.LoginActivity;
import com.tarena.tlbs.MainActivity;
public class TlbsBiz {
	private Context context;
	public TlbsBiz(Context context){
		this.context=context;
	}
	public void login(final String username,final String pwd){
		final Intent intent=new Intent();
		intent.setAction("com.tarena.action.LOGINSTATE");
		new Thread(){
			public void run() {
				try {
					int count=0;
					while(count<10 && !TApplication.conn.isConnected()){
						try {
							sleep(1000);
							count++;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(TApplication.conn.isConnected()){
						Log.i("info", "开始登陆:"+username+","+pwd);
						TApplication.conn.login(username, pwd);
						TApplication.username=username;
						Log.i("info", "登陆结果："+TApplication.conn.isAuthenticated());
						intent.putExtra("loginstate", true);
						context.sendBroadcast(intent);
					}else{
						intent.putExtra("loginstate", false);
						context.sendBroadcast(intent);
						Log.i("info", "连接超时！");
					}

				} catch (Exception e) {
					e.printStackTrace();
					intent.putExtra("loginstate", false);
					Log.i("info", "登陆异常！");
					context.sendBroadcast(intent);
				}
			}
		}.start();
	}
	public void regist(final String username, 
			final String pwd, final HashMap<String, String> map) {
		final Intent intent=new Intent();
		intent.setAction("com.tarena.action.REGISTSTATE");
		new Thread(){
			public void run() {
				try {
					int count=0;
					while(count<10 && !TApplication.conn.isConnected()){
						try {
							sleep(1000);
							count++;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(TApplication.conn.isConnected()){
						Log.i("info", "开始注册:"+username+","+pwd+","+map.get("nick"));
						AccountManager manager = TApplication.conn.getAccountManager();
						manager.createAccount(username, pwd, map);
						Log.i("info", "注册结果："+"true");
						intent.putExtra("registstate", true);
						context.sendBroadcast(intent);
					}else{
						intent.putExtra("registstate", false);
						context.sendBroadcast(intent);
						Log.i("info", "连接超时！");
					}

				} catch (Exception e) {
					e.printStackTrace();
					intent.putExtra("registstate", false);
					Log.i("info", "注册失败！");
					context.sendBroadcast(intent);
				}
			}
		}.start();
	}
}
