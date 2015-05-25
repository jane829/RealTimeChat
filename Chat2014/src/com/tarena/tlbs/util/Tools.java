package com.tarena.tlbs.util;

import android.content.Context;
import android.widget.Toast;

public class Tools {
	public static boolean isNull(String str)
	{
		if (str==null||"".equals(str)||"null".equals(str))
		{
			return true;
		}
		return false;
	}
	public static void showInfo(Context context,String msg) {
//		Toast toast=new Toast(this);
//		View view=View.inflate
//				(this, R.layout.login, null);
//		toast.setView(view);
//		toast.show();
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
