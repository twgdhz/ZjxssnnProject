package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.List;
import com.zjxfood.photoview.activity.ViewPagerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class MallDetailViewPagerAdapter extends PagerAdapter{

	private Context mContext;
	private List<View> mList;
	private ArrayList<String> mPathList;
	
	public MallDetailViewPagerAdapter(Context context,List<View> list,ArrayList<String> pathList){
		this.mContext = context;
		this.mList = list;
		this.mPathList = pathList;
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
		mList.get(position).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(mContext,
						ViewPagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("list", mPathList);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		return mList.get(position);
	}
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}
	
}
