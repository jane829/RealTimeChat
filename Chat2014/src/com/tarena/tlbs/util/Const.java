package com.tarena.tlbs.util;

import android.content.Intent;

public class Const {
	public final static String ACTION_LOGIN="com.tarena.tlbs.view.loginActivity";

	public final static String KEY_IS_SUCCESS="KEY_IS_SUCCESS";

	public static final String ACTION_REGISTER = "com.tarena.tlbs.view.RegisterActivity.register";

	public static final String ACTION_ADD_FRIEND = "com.tarena.tlbs.view.AddFriendActiviyt.addFriend";

	public static final String KEY_GROUP_NAME = "groupName";

	public static final String KEY_FRIEND_USER = "KEY_FRIEND_USER";

	public static final String ACTION_SHOW_PRIVATE_MESSAGE = "ACTION_SHOW_PRIVATE_MESSAGE";
	//类型有五种
	public static final String TYPE_TEXT = "<!---text>";
	public static final String TYPE_FACE = "<!---face>";
	public static final String TYPE_IMAGE = "<!--image>";
	public static final String TYPE_AUDIO = "<!--audio>";
	public static final String TYPE_MAP = "<!----map>";
	public static final String TYPE_END_TAG = "<-->";

	public static final String ACTION_JOIN = "com.tarena.tlbs.view.InputroomFragment.join";
	public  static final String ACTION_SHOW_GROUP_CHAT_MESSAGE = "com.tlbs.view.groupChatActivity.show";

}
