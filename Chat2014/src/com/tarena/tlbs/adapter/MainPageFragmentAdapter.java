package com.tarena.tlbs.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * ��ǰд��adapter�ǰ�������ʾ��listView
 * �ǰ�fragment��ʾ��viewPager
 * @author pjy
 *
 */
public class MainPageFragmentAdapter extends FragmentPagerAdapter{
	//ArrayList<Entity> list
	ArrayList<Fragment> list;
	public MainPageFragmentAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		
		//�öϵ�
		if (list==null)
		{
			this.list=new ArrayList<Fragment>();
		}else
		{
			this.list=list;
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	//����дgetView()

}
