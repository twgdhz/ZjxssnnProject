package com.zjxfood.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class MerchantImageAdapter extends PagerAdapter {

	private Context mContext;
	private ImageView[] mImageViews;
	private String[] mPath;

	public MerchantImageAdapter(Context context, ImageView[] imageViews,String[] path) {
		this.mContext = context;
		this.mImageViews = imageViews;
		this.mPath = path;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPath.length;
	}

	@Override
	public boolean isViewFromObject(View v, Object arg1) {
		return v == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
//		ImageLoader.getInstance().displayImage(mPath[position % mPath.length], imageView, options)
		((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
		return mImageViews[position % mImageViews.length];
	}
	@Override  
    public void destroyItem(View container, int position, Object object) {  
        ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);  
          
    }
}
