package com.tarena.tlbs.model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class PrivateChatEntity {
	//面试题：android UI是不是线程安全的？
	//UI不是线程安全，性能高。
	//ConCurrent并发，同时进行
	public static ConcurrentHashMap<String, Vector<Message>>
	map=new ConcurrentHashMap();
	public static void addMessage(final String toUser, Message msg) {
		Vector<Message> vector= PrivateChatEntity.map.get(toUser);
		  if (vector==null)
		  {
			  vector=new Vector<Message>();
			  PrivateChatEntity.map.put(toUser, vector);
		  }
		  vector.add(msg);
	};


}
