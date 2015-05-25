package com.tarena.tlbs;

import com.tarena.tlbs.R;
import com.tarena.tlbs.model.AddFriendBiz;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.Tools;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFriendActivity extends BaseActivity {
	
	private Button btnBack;
	private Button btnSubmit;
	private EditText etUsername;
	private EditText etName;
	private EditText etGroup;
	private AddFriendReceiver addFriendReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);
		setupView();
		addListener();
		addFriendReceiver = new AddFriendReceiver();
		IntentFilter filter = new IntentFilter(Const.ACTION_ADD_FRIEND);
		registerReceiver(addFriendReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(addFriendReceiver);
	}

	private void addListener() {
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String username = etUsername.getText().toString();
					String name = etName.getText().toString();
					String group = etGroup.getText().toString();
					// ºÏ≤È
					AddFriendBiz addFriendBiz = new AddFriendBiz(AddFriendActivity.this);
					addFriendBiz.addFriend(username, name,
							new String[] { group });
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AddFriendActivity.this,MainFragmentActivity.class));
				finish();
			}
		});
	}

	private void setupView() {
		btnBack = (Button) findViewById(R.id.btn_add_friend_back);
		btnSubmit = (Button) findViewById(R.id.btn_add_friend_submit);
		etUsername = (EditText) findViewById(R.id.et_add_friend_username);
		etName = (EditText) findViewById(R.id.et_add_friend_nick);
		etGroup = (EditText) findViewById(R.id.et_add_friend_group);
	}
	class AddFriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				boolean isSuccess = intent.getBooleanExtra(
						Const.KEY_IS_SUCCESS, false);
				if (isSuccess) {
					startActivity(new Intent(AddFriendActivity.this,MainFragmentActivity.class));
					finish();
				} else {
					Tools.showInfo(context, "∑¢ÀÕ ß∞‹");

				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
	}
}
