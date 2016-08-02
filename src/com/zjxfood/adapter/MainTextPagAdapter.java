package com.zjxfood.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainTextPagAdapter extends PagerAdapter{
	private Context mContext;
	private List<View> mList;
	
	public MainTextPagAdapter(Context context,List<View> list){
		this.mContext = context;
		this.mList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public Object instantiateItem(View arg0, final int position) {
		((ViewPager) arg0).addView(mList.get(position));
		return mList.get(position);
	}
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

}
