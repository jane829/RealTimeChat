package com.tarena.tlbs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class ExceptionUtil {
  public static void handle(Exception e)
  {
	//发邮件有三种方式，推荐用javamail
		//保存到sdcard
	  StringWriter sw=new StringWriter();
	  PrintWriter pw=new PrintWriter(sw);
	  e.printStackTrace(pw);
	  String str=sw.toString();
	  //保存到sdcard
	  Log.i("ExceptionUtil", str);
	  //开发阶段。
	  e.printStackTrace();
  }
}
