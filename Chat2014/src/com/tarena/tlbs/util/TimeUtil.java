package com.tarena.tlbs.util;
/**
 * 积累
 * 单位安排有经验的程序员写公用类（工具，联网，解析，实体类，安全，性能），占80%
 * 得一段代码用的时间
 * @author pjy
 *
 */
public class TimeUtil {
	long startTime;
	public TimeUtil() {
		// TODO Auto-generated constructor stub
	startTime=System.currentTimeMillis();
	}
	
	public void showTime(String tag)
	{
		LogUtil.i(tag, System.currentTimeMillis()-startTime);
	}
	
	

}
