package com.tarena.tlbs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class ExceptionUtil {
  public static void handle(Exception e)
  {
	//���ʼ������ַ�ʽ���Ƽ���javamail
		//���浽sdcard
	  StringWriter sw=new StringWriter();
	  PrintWriter pw=new PrintWriter(sw);
	  e.printStackTrace(pw);
	  String str=sw.toString();
	  //���浽sdcard
	  Log.i("ExceptionUtil", str);
	  //�����׶Ρ�
	  e.printStackTrace();
  }
}
