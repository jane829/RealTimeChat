package com.tarena.tlbs;

import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.model.TlbsBiz;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private Button bnt_regist;
	private Button bnt_submit;
	private LoginReceiver receiver;
	private IntentFilter filter;
	private String username;
	private class LoginReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if("com.tarena.action.LOGINSTATE".equals(action)){
				boolean result=intent.getBooleanExtra("loginstate", false);
				Log.i("info", "登陆结果："+result);
				if(result){
					//将登陆的用户名保存到偏好设置
					TApplication.sp.edit().putString("userName", username).commit();
					Intent intent2=new Intent(context,MainFragmentActivity.class);
					context.startActivity(intent2);
				}else{
					Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
				}
			}
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		receiver=new LoginReceiver();
		filter=new IntentFilter();
		filter.addAction("com.tarena.action.LOGINSTATE");
		registerReceiver(receiver, filter);
		setView();
		setListener();
	}

	private void setView() {
		et_username=(EditText)findViewById(R.id.et_regist_username);
		username = TApplication.sp.getString("userName", "");
		if(!"".equals(username)||username!=null){
			et_username.setText(username);
		}
		et_password=(EditText)findViewById(R.id.et_regist_nichen);
		bnt_regist=(Button)findViewById(R.id.btn_regist_login);
		bnt_submit=(Button)findViewById(R.id.btn_regist_submit);
	}

	private void setListener() {
		bnt_regist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				regist();
			}
		});
		bnt_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				login();
			}
		});
	}
	protected void regist() {
		Intent intent=new Intent(this,RegistActivity.class);
		startActivity(intent);
		
	}
	protected void login() {
		//测试用户名:kktao 密码:1
		username=et_username.getText().toString();
		String password=et_password.getText().toString();
		if(username==null||"".equals(username)||"null".equals(username)){
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if(password==null||"".equals(password)){
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
		}
		new TlbsBiz(LoginActivity.this).login(username, password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	@Override
	protected void onRestart() {
		setView();
		super.onRestart();
	}

}
