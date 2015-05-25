package com.tarena.tlbs;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tarena.tlbs.adapter.MainPageFragmentAdapter;
import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.view.GroupListFragment;
import com.tarena.tlbs.view.InputRoomFragment;
import com.tarena.tlbs.view.MessageFragment;
import com.tarena.tlbs.view.MoreFragment;

public class MainFragmentActivity extends FragmentActivity{
	ViewPager viewPager;
	ArrayList<Fragment> list=new ArrayList<Fragment>();
	private MainPageFragmentAdapter adapter;
	private Button btn_group_list;
	private Button btn_input_room;
	private Button btn_message;
	private Button btn_more;
	protected int currentIndex=0;
	private ArrayList<Button> list2;
	private GroupListFragment grouplist=new GroupListFragment();
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.main);
		TApplication.list.add(this);
		setupView();
		addListener();
		setData();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK)
		{
			//ÅÐ¶ÏgroupListFragment.popupwindow
			if (grouplist.window!=null && grouplist.window.isShowing())
			{
				grouplist.window.dismiss();
				grouplist.window=null;
				return true;
			}else
			{
				//Tools.showDialog()
				//this.finish();
				//return false;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void setData() {
		// TODO Auto-generated method stub
		list.add(grouplist);
		list.add(new InputRoomFragment());
		list.add(new MessageFragment());
		list.add(new MoreFragment());
		
		adapter=new MainPageFragmentAdapter(getSupportFragmentManager(), list);
		viewPager.setAdapter(adapter);
		
		list2=new ArrayList<Button>();
		list2.add(btn_group_list);
		list2.add(btn_input_room);
		list2.add(btn_message);
		list2.add(btn_more);
		updateBtnColor();
		
	}
	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				currentIndex=arg0;
				updateBtnColor();
				if(grouplist.window!=null && grouplist.window.isShowing()){
					grouplist.window.dismiss();
					grouplist.window=null;
				}
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			public void onPageScrollStateChanged(int arg0) {}
		});
		btn_group_list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});
		btn_input_room.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		btn_message.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(2);
			}
		});
		btn_more.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(3);
			}
		});
		
	}
	protected void updateBtnColor() {
		for(int i=0;i<list2.size();i++){
			if(currentIndex==i){
				list2.get(i).setTextColor(0xFFFFFFFF);
			}else{
				list2.get(i).setTextColor(0xFF000000);
			}
		}
		
	}
	private void setupView() {
		viewPager=(ViewPager) findViewById(R.id.viewPager_main);
		btn_group_list=(Button)findViewById(R.id.btn_main_groupList);
		btn_input_room=(Button)findViewById(R.id.btn_main_groupChat);
		btn_message=(Button)findViewById(R.id.btn_main_message);
		btn_more=(Button)findViewById(R.id.btn_main_more);
		
	}

}
