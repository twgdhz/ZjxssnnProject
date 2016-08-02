package com.zjxfood.adapter;

import java.util.List;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class FirstPagerAdapter extends PagerAdapter{

	private Context mContext;
	private List<View> mList;
	private ImageView[] mImageViews;
	public FirstPagerAdapter(Context context,ImageView[] imageViews){
		this.mContext = context;
//		this.mList = list;
		this.mImageViews = imageViews;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageViews.length;
	}

	@Override
	public boolean isViewFromObject(View v, Object arg1) {
		return v == arg1;
	}
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
		return mImageViews[position % mImageViews.length];
	}
	
	@Override  
    public void destroyItem(View container, int position, Object object) {  
        ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);  
          
    }

}
