package com.tarena.tlbs.view;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.tarena.tlbs.AddFriendActivity;
import com.tarena.tlbs.PrivateChatActivity;
import com.tarena.tlbs.R;
import com.tarena.tlbs.adapter.GroupListExpandableAdapter;
import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.util.Const;

public class GroupListFragment extends Fragment{
	Button btnMore;
	RelativeLayout top;
	public  PopupWindow window;
	private RelativeLayout main;
	private ExpandableListView expand_lv;
	private GroupListExpandableAdapter adapter;
	private ArrayList<RosterGroup> list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//activity.setContentView
		View view=inflater.inflate(R.layout.group_list, null);
		//测试AddFriendBiz
		//AddFriendBiz addFriendBiz=new AddFriendBiz();
		//addFriendBiz.addFriend("pc2", "pc2", new String[]{"1310"});
		setupView(view);
		addListener();
		return view;
		
	}
	@Override
	public void onStart() {
		showGroupList();
		super.onStart();
	}
	private void showGroupList() {
		list=new ArrayList<RosterGroup>(TApplication.conn.getRoster().getGroups());
		adapter=new GroupListExpandableAdapter(getActivity(), list);
		expand_lv.setAdapter(adapter);
	}
	private void addListener() {
		// TODO Auto-generated method stub
		btnMore.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					View viewmore = View.inflate(getActivity(), R.layout.group_list_more, null);
					window=new PopupWindow(viewmore,ViewGroup.
							LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					window.showAtLocation(top, Gravity.RIGHT|Gravity.TOP, 0, top.getHeight()+30);
					//获取PopupWindow上的按钮控件
					Button btn_add = (Button)viewmore.findViewById(R.id.btn_group_list_more_addFriend);
					btn_add.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if(window!=null&&window.isShowing()){
								window.dismiss();
								getActivity().startActivity(new Intent(getActivity(),AddFriendActivity.class));
								window=null;
								//System.gc();
							}
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		main.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				if(window!=null&&window.isShowing()){
					window.dismiss();
				}
				return false;
			}
		});
		expand_lv.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//获得被点击的好友
				//一定要用getUser
				String toUser=adapter.getChild(groupPosition, childPosition).getUser();
//				PrivateChatBiz biz=new PrivateChatBiz();
//				biz.sendMessage(toUser, "你好啊");
				Intent intent=new Intent(getActivity(), PrivateChatActivity.class);
				intent.putExtra(Const.KEY_FRIEND_USER, toUser);
				startActivity(intent);
				Log.i("我被点击了：", "group-child"+groupPosition+","+childPosition);
				return false;
			}
		});
	}
	private void setupView(View view) {
		// TODO Auto-generated method stub
		btnMore=(Button) view.findViewById(R.id.btn_group_list_more);
		top=(RelativeLayout) view.findViewById(R.id.top);
		main=(RelativeLayout) view.findViewById(R.id.rl_group_list);
		expand_lv=(ExpandableListView)view.findViewById(R.id.elv_group_list);
	}
}
