package com.tarena.tlbs;

import java.util.HashMap;

import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.model.TlbsBiz;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends BaseActivity {

	private EditText et_name;
	private EditText et_nichen;
	private EditText et_pwd;
	private EditText et_repwd;
	private Button btn_login;
	private Button btn_submit;
	private RegistReceiver receiver;
	private String username;
	
	private class RegistReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if("com.tarena.action.REGISTSTATE".equals(action)){
				boolean result=intent.getBooleanExtra("registstate", false);
				if(result){
					Toast.makeText(context, "注册成功！", Toast.LENGTH_SHORT).show();
					TApplication.sp.edit().putString("userName", username).commit();
					Intent intent2=new Intent(RegistActivity.this,LoginActivity.class);
					startActivity(intent2);
					finish();
				}else{
					Toast.makeText(context, "注册失败，请稍后再试！", Toast.LENGTH_LONG).show();
				}
			}
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		setData();
		setView();
		setListener();
		receiver=new RegistReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.tarena.action.REGISTSTATE");
		registerReceiver(receiver, filter);
	}

	private void setData() {
		
	}
	private void setView() {
		et_name=(EditText)findViewById(R.id.et_regist_username);
		et_nichen=(EditText)findViewById(R.id.et_regist_nichen);
		et_pwd=(EditText)findViewById(R.id.et_regist_password);
		et_repwd=(EditText)findViewById(R.id.et_regist_repassword);
		btn_login=(Button)findViewById(R.id.btn_regist_login);
		btn_submit=(Button)findViewById(R.id.btn_regist_submit);
	}

	private void setListener() {
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				regist();
			}
		});
	}

	protected void regist() {
		username = et_name.getText().toString();
		String pwd = et_pwd.getText().toString();
		String repwd = et_repwd.getText().toString();
		String nichen = et_nichen.getText().toString();
		if(username==null||"".equals(username)){
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
			return;
		}
		if(nichen==null||"".equals(nichen)){
			Toast.makeText(this, "请输入昵称", Toast.LENGTH_LONG).show();
			return;
		}
		if(pwd==null||"".equals(pwd)){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			return;
		}
		if(username==null||"".equals(username)){
			Toast.makeText(this, "请再次输入密码", Toast.LENGTH_LONG).show();
			return;
		}
		if(!pwd.equals(repwd)){
			Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
			return;
		}
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("nick", nichen);
		new TlbsBiz(this).regist(username,pwd,map);
		
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.regist, menu);
		return true;
	}

}
