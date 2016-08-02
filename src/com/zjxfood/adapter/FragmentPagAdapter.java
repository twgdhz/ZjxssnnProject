package com.zjxfood.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentPagAdapter extends android.support.v4.app.FragmentPagerAdapter{

	private ArrayList<Fragment> mList;
	public FragmentPagAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.mList = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

}
