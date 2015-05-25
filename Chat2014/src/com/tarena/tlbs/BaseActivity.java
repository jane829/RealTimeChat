package com.tarena.tlbs;

import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.util.LogUtil;
import com.tarena.tlbs.util.NetWorkUtil;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TApplication.list.add(this);
		LogUtil.i("ÍË³ö", "add "+this);
		NetWorkUtil netWorkUtil=new NetWorkUtil();
		netWorkUtil.checkNetWorkState(this);
	}

}
