package com.tarena.tlbs.view;

import com.tarena.tlbs.CopyRightActivity;
import com.tarena.tlbs.LoginActivity;
import com.tarena.tlbs.MainActivity;
import com.tarena.tlbs.R;
import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.util.ExceptionUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class MoreFragment extends Fragment{
	Button btnExit;
	private Button btnChangeUser;
	private Button btnAbout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//activity.setContentView
		View view=inflater.inflate(R.layout.more, null);
		setupView(view);
		addListener();
		
		return view;
	}
	private void addListener() {
		// TODO Auto-generated method stub
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					TApplication.exit();
				} catch (Exception e) {
					ExceptionUtil.handle(e);
					// TODO: handle exception
				}
			}
		});
		btnAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					//Ìø×ªµ½°æÈ¨Activity
					startActivity(new Intent(getActivity(),CopyRightActivity.class));
				} catch (Exception e) {
					ExceptionUtil.handle(e);
					// TODO: handle exception
				}
			}
		});
		btnChangeUser.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TApplication.reconection();
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}
	private void setupView(View view) {
//view-->r.layout.more.xml
		btnExit=(Button) view.findViewById(R.id.btn_more_exit);
		btnAbout=(Button) view.findViewById(R.id.btn_more_about);
		btnChangeUser=(Button)view.findViewById(R.id.btn_more_changeuser);
		
	}
	

}
