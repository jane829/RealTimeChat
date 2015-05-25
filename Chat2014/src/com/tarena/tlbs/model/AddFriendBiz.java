package com.tarena.tlbs.model;

import org.jivesoftware.smack.Roster;

import android.content.Context;
import android.content.Intent;

import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;

public class AddFriendBiz {
	public Context context;
	public AddFriendBiz(Context context){
		this.context=context;
	}

	public void addFriend(final String username, final String nick,
			final String[] groups) {
		new Thread() {
			public void run() {
				try {
					//70%�Ĵ����ڴ����쳣���
					// 1,�ж������Ƿ���
					if (TApplication.conn.isConnected() == false) {
						TApplication.connectChatServer();
						int count = 0;
						while (TApplication.conn.isConnected() == false
								&& count < 300) {
							count++;
							sleep(100);
						}
					}
					if (TApplication.conn.isConnected()) {
						// 2,�жϵ�¼״̬�Ƿ���
						if (TApplication.conn.isAuthenticated() == false) {
							TlbsBiz biz = new TlbsBiz(context);
							biz.login(TApplication.username,
									TApplication.password);
							int count = 0;
							while (TApplication.conn
									.isAuthenticated() == false && count < 300) {
								count++;
								sleep(100);
							}
						}

						// 3����Ӻ���
						if (TApplication.conn.isAuthenticated()) {
							// �����᣺�ŵ��Ǻ��Ѻͺ��ѷ�����Ϣ
							Roster roster = TApplication.conn
									.getRoster();
							// username �û��� pc1
							// user ��ȫ�� pc1@tarena3gxmpp.com
							// name ���س�
							String user = username + "@"
									+ TApplication.serverName;
							roster.createEntry(user, nick, groups);
							
							Intent intent=new Intent(Const.ACTION_ADD_FRIEND);
							intent.putExtra(Const.KEY_IS_SUCCESS, true);
							context.sendBroadcast(intent);


						} else {

						}

					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
					
					Intent intent=new Intent(Const.ACTION_ADD_FRIEND);
					intent.putExtra(Const.KEY_IS_SUCCESS, false);
					context.sendBroadcast(intent);

				}
			};
		}.start();
	}

}
