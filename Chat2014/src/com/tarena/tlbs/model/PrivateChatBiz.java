package com.tarena.tlbs.model;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;

public class PrivateChatBiz {
  /**
   * 
   * @param toUser   ���ѵ�ȫ�� ��@tarena3gxmpp.com
   * @param body ��������
   */
	public void sendMessage(final String toUser,
		  final String body)
  {
	  new Thread(){
		  public void run() {
			  try {
				  Message msg=new Message();
				  String from=TApplication.username+"@"+TApplication.serverName;
				  msg.setFrom(from);
				  msg.setTo(toUser);
				  msg.setBody(body);
				  msg.setType(Type.chat);//˽��	
				  //�����ݷŵ�ʵ����
				  PrivateChatEntity.addMessage(toUser, msg);
				  //���㲥
				  
				  Intent intent=new Intent(Const.ACTION_SHOW_PRIVATE_MESSAGE);
				  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  TApplication.instance.sendBroadcast(intent);

				TApplication.conn.sendPacket(msg);
			} catch (Exception e) {
				// TODO: handle exception
				ExceptionUtil.handle(e);
			}
		  };
	  }.start();
  }
}
