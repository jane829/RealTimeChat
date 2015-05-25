package com.tarena.tlbs.model;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.util.Log;
import com.tarena.tlbs.R;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.LogUtil;

public class TApplication extends Application {
	public static ConnectionConfiguration config;
	public static XMPPConnection conn;
	public static String host;
	public static int port;
	public static String serverName;
	public static boolean isRelease=false;
	public static ArrayList<Activity> list=new ArrayList<Activity>();
	public static String username;
	protected static String password;
	protected static TApplication instance;
	public static SharedPreferences sp;
	public static MultiUserChat groupChat;


	@Override
	public void onCreate() {
		sp=getSharedPreferences("userName", MODE_PRIVATE);
		instance=this;
		getconfig();
		config=new ConnectionConfiguration(host, port, serverName);
		conn=new XMPPConnection(config);
		connectChatServer();
		super.onCreate();
		registerInterceptor();
		registerListener();
	}
	/**
	 * �˳�
	 */
	public static void exit(){
		for(Activity a:list){
			a.finish();
		}
		conn.disconnect();
		System.exit(0);

	}
	/**
	 * ���½�������
	 */
	public static void reconection(){
		if(conn!=null&&conn.isConnected()){
			conn.disconnect();
			connectChatServer();
		}
	}
	private void registerListener() {
		// TODO Auto-generated method stub
		AllPacketListener allPacketListener=new AllPacketListener();
		conn.addPacketListener(allPacketListener, null);

		//filter����ĳ��������������string���
		//����class.getName�ж�
		//openfire������Ϣ���ж����е�filter�Ƿ񷵻�true
		//new PacketTypeFilter(rosterPacket.class)
		//rosterpacket.class.getName�õ���packet.class
		//�����ǩ��presence,filterʧ��
		//�����ǩ��<iq xmlns="iq:roster">,filter�ɹ�
		GroupListListener grouplistener=new GroupListListener();
		PacketTypeFilter filter=new PacketTypeFilter(RosterPacket.class);
		conn.addPacketListener(grouplistener, filter);

		PrivateChatFilter privateChatFilter=new PrivateChatFilter();
		PrivateChatListener privateChatListener=new PrivateChatListener();
		conn.addPacketListener(privateChatListener, privateChatFilter);
		
		GroupChatFilter groupChatFilter = new GroupChatFilter();
		GroupChatListener groupChatListener = new GroupChatListener();
		conn.addPacketListener(groupChatListener, groupChatFilter);



	}

	private void registerInterceptor() {
		// TODO Auto-generated method stub
		AllPacketInterceptor allPacketInterceptor=new AllPacketInterceptor();
		//�ÿ�ܵĽӿ�ָ��ʵ����
		conn.addPacketInterceptor(allPacketInterceptor, null);
	}
	/**
	 * ��ȡxml�����ļ��е���Ϣ
	 */
	private void getconfig() {
		try {
			XmlResourceParser parser = this.getResources().getXml(R.xml.config);
			int eventType = parser.getEventType();
			while(eventType!=parser.END_DOCUMENT){
				if(eventType==parser.START_TAG){
					String tagName=parser.getName();
					if("host".equals(tagName)){
						host=parser.nextText().trim();
					}else if("port".equals(tagName)){
						port=Integer.parseInt(parser.nextText().trim());
					}else if("serverName".equals(tagName)){
						serverName=parser.nextText().trim();
					}
				}
				eventType=parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ���������������
	 */
	public static void connectChatServer() {

		new Thread(){
			public void run() {
				try {

					Log.i("info", "��ʼ����....");
					conn.connect();
					Log.i("info", "���ӽ��:"+conn.isConnected());
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	class AllPacketInterceptor implements PacketInterceptor
	{

		/**
		 * ����ȥ
		 */
		@Override
		public void interceptPacket(Packet packet) {
			// TODO Auto-generated method stub
			//package
			//packet ����ȥ���ս�������packet���ݰ�
			LogUtil.i("AllPacketInterceptor", packet.toXML());
		}
	}

	class AllPacketListener implements PacketListener{
		//openfire������Ϣ
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			LogUtil.i("AllPacketListener", packet.toXML());
			if(packet instanceof Presence){
				Presence p=(Presence)packet;
				String from = p.getFrom();
				Type type = p.getType();
				if(type==Type.subscribe){
					Log.i("info" , from+"ͬ�������ĺ�������");
				}else if(type==Type.unsubscribe){
					Log.i("info", from+"�ܾ������ĺ�������");
				}

			}

		}
	}
	class GroupListListener implements PacketListener{
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			ArrayList<RosterGroup> list = new ArrayList<RosterGroup>(
					conn.getRoster().getGroups());
			// ArrayList<RosterGroup> list=( ArrayList<RosterGroup>)
			// (xmppConnection.getRoster().getGroups());

			for (int i = 0; i < list.size(); i++) {
				RosterGroup group = list.get(i);
				LogUtil.i("GroupListListener", group.getName());

			}
		}
	}

	class PrivateChatFilter implements PacketFilter
	{
		public boolean accept(Packet packet) {
			// TODO Auto-generated method stub
			if (packet instanceof Message)
			{
				Message msg=(Message) packet;
				org.jivesoftware.smack.packet.
				Message.Type type=msg.getType();
				if (type==org.jivesoftware.smack.packet.
						Message.Type.chat)
				{
					return true;
				}

			}
			return false;
		}
	}

	class PrivateChatListener implements PacketListener
	{

		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			Message msg=(Message) packet;
			String from=msg.getFrom();
			if (from.contains("/"))
			{
				from=from.substring(0, from.indexOf("/"));
			}
			//�����ݷŵ�ʵ����
			PrivateChatEntity.addMessage(from, msg);
			//���㲥

			Intent intent=new Intent(Const.ACTION_SHOW_PRIVATE_MESSAGE);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			TApplication.instance.sendBroadcast(intent);

		}
	}
	class GroupChatFilter implements PacketFilter {

		@Override
		public boolean accept(Packet packet) {
			// TODO Auto-generated method stub
			if (packet instanceof Message) {
				Message msg = (Message) packet;
				org.jivesoftware.smack.packet.Message.Type type = msg.getType();
				if (type == org.jivesoftware.smack.packet.Message.Type.groupchat) {
					return true;
				}

			}
			return false;
		}
	}

	class GroupChatListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			Message msg = (Message) packet;
			// 1310@conference.tarena3gxmpp.com/aaaaaa
			String from = msg.getFrom();
			if (from.contains("/")) {
				from = from.substring(0, from.indexOf("/"));
			}
			// �����ݷŵ�ʵ����

			GroupChatEntity.map.put(from, msg);
			// ���㲥

			Intent intent = new Intent(Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			TApplication.instance.sendBroadcast(intent);

		}
	}



}
