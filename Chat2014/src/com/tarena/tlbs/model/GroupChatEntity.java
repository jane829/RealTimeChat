package com.tarena.tlbs.model;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

public class GroupChatEntity {
	//Message 是最新的消息
   public static 
   ConcurrentHashMap<String, Message>
   map=new ConcurrentHashMap();
}
