package com.tarena.tlbs.view;

import com.tarena.tlbs.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//activity.setContentView
		View view=inflater.inflate(R.layout.message, null);
		return view;
	}

}
