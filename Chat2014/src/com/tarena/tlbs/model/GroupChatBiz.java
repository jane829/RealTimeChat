package com.tarena.tlbs.model;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.tarena.tlbs.util.Const;

import android.content.Intent;

public class GroupChatBiz {
	/**
	 * 
	 * @param room �����ҵ�����
	 * @param name  �س�
	 */
	public void join(final String room,final String name)
	{
		new Thread(){public void run() {
			try {
				String roomName=room+"@conference."+TApplication.serverName;
				MultiUserChat groupChat=
						new MultiUserChat
						(TApplication.conn, roomName);
				groupChat.join(name);
				//��groupChat���浽Tapplication
				TApplication.groupChat=groupChat;
				Intent intent=new Intent(Const.ACTION_JOIN);
				intent.putExtra(Const.KEY_IS_SUCCESS, true);
				TApplication.instance.sendBroadcast(intent);
				
			} catch (Exception e) {
				// TODO: handle exception
				Intent intent=new Intent(Const.ACTION_JOIN);
				intent.putExtra(Const.KEY_IS_SUCCESS, false);
				TApplication.instance.sendBroadcast(intent);
			}
		};}.start();
	}

	public void sendMessage(final String body)
	{

		new Thread(){
			public void run() {
				try {
					
					Message msg=new Message();
					msg.setFrom(TApplication.username+"@"+TApplication.serverName);
					//1310@conference.tarena3gxmpp.com
					msg.setTo(TApplication.groupChat.getRoom());
					msg.setBody(body);
					msg.setType(Type.groupchat);
					//�ŵ�ʵ����
					GroupChatEntity.map.put(TApplication.groupChat.getRoom(), msg);
					TApplication.groupChat.sendMessage(msg);
					
					//֪ͨactivity��ʾ
					Intent intent=new Intent(Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);
				    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					TApplication.instance.sendBroadcast(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}

			
		}.start();
	
	}
}
