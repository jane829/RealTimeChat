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
	 * 退出
	 */
	public static void exit(){
		for(Activity a:list){
			a.finish();
		}
		conn.disconnect();
		System.exit(0);

	}
	/**
	 * 重新建立连接
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

		//filter根据某个条件，条件是string相等
		//根据class.getName判断
		//openfire过来信息，判断所有的filter是否返回true
		//new PacketTypeFilter(rosterPacket.class)
		//rosterpacket.class.getName得到是packet.class
		//如果标签是presence,filter失败
		//如果标签是<iq xmlns="iq:roster">,filter成功
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
		//让框架的接口指向实现类
		conn.addPacketInterceptor(allPacketInterceptor, null);
	}
	/**
	 * 获取xml配置文件中的信息
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
	 * 与服务器建立连接
	 */
	public static void connectChatServer() {

		new Thread(){
			public void run() {
				try {

					Log.i("info", "开始连接....");
					conn.connect();
					Log.i("info", "连接结果:"+conn.isConnected());
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
		 * 发出去
		 */
		@Override
		public void interceptPacket(Packet packet) {
			// TODO Auto-generated method stub
			//package
			//packet 发出去，收进来都叫packet数据包
			LogUtil.i("AllPacketInterceptor", packet.toXML());
		}
	}

	class AllPacketListener implements PacketListener{
		//openfire返回信息
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			LogUtil.i("AllPacketListener", packet.toXML());
			if(packet instanceof Presence){
				Presence p=(Presence)packet;
				String from = p.getFrom();
				Type type = p.getType();
				if(type==Type.subscribe){
					Log.i("info" , from+"同意了您的好友请求");
				}else if(type==Type.unsubscribe){
					Log.i("info", from+"拒绝了您的好友请求");
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
			//把数据放到实体类
			PrivateChatEntity.addMessage(from, msg);
			//发广播

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
			// 把数据放到实体类

			GroupChatEntity.map.put(from, msg);
			// 发广播

			Intent intent = new Intent(Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			TApplication.instance.sendBroadcast(intent);

		}
	}



}
