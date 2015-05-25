package com.tarena.tlbs.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.tarena.tlbs.R;
import com.tarena.tlbs.model.TApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class GroupListExpandableAdapter extends BaseExpandableListAdapter {
	private ArrayList<RosterGroup> list;
	private LayoutInflater inflater;
	private LayoutInflater inflater2;

	public GroupListExpandableAdapter(Context context, ArrayList<RosterGroup> list) {
		super();
		inflater=LayoutInflater.from(context);
		inflater2=LayoutInflater.from(context);
		if(list==null){
			this.list=new ArrayList<RosterGroup>();
		}else{
			this.list=list;
		}
	}
	public void updateView(ArrayList<RosterGroup> list){
		if(list==null){
			this.list=new ArrayList<RosterGroup>();
		}else{
			this.list=list;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String groupName=list.get(groupPosition).getName();
		if(getChildList(groupName)!=null){
			return getChildList(groupName).size();
		}else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public RosterEntry getChild(int groupPosition, int childPosition) {
		RosterGroup group=(RosterGroup) getGroup(groupPosition);
		return getChildList(group.getName()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.group_list_item, null);
			holder.tv_groupName=(TextView)convertView.findViewById(R.id.tv_group_list_groupName);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_groupName.setText(list.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView=inflater2.inflate(R.layout.friend_list_item, null);
		TextView tv_friendName = (TextView)convertView.findViewById(R.id.tv_friend_list_item_friendName);
		String groupName=list.get(groupPosition).getName();
		String childName=getChildList(groupName).get(childPosition).getName();
		tv_friendName.setText(childName);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	public ArrayList<RosterEntry> getChildList(String groupName){
		RosterGroup rosterGroup=
				TApplication.conn
				.getRoster().getGroup(groupName);
		ArrayList<RosterEntry> list=new ArrayList(rosterGroup.getEntries());
		return list;
	}
	class ViewHolder{
		private TextView tv_groupName;
	}

}
