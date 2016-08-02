package com.zjxfood.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class FragmentGalleryAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<View> mFragments;
	
	public FragmentGalleryAdapter(Context context,ArrayList<View> fragments){
		this.mContext = context;
		this.mFragments = fragments;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout layout = new RelativeLayout(mContext);
		layout.setPadding(2, 2, 2, 2);
		layout.addView(mFragments.get(position));
		return layout;
	}

}
