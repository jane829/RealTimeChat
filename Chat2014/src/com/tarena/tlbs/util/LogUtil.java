package com.tarena.tlbs.util;

import com.tarena.tlbs.model.TApplication;

import android.util.Log;
/**
 * baidu log4j
 * baidu log4j android
 * 日志统一处理
 * @author pjy
 *
 */
public class LogUtil {
	public static void i(String tag, String msg) {
		if (!TApplication.isRelease) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, Object msg) {
		i(tag, String.valueOf(msg));
	}
}
