package com.zjxfood.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.zjxfood.activity.WebActivity;
import com.zjxfood.popupwindow.ContentPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JbBannerAdapter extends PagerAdapter {

	private Context mContext;
	private List<View> mList;
	private ArrayList<HashMap<String, Object>> mArrayList;
	private ContentPopup mPopup;

	public JbBannerAdapter(Context context, List<View> list,
						   ArrayList<HashMap<String, Object>> arraylist) {
		this.mContext = context;
		this.mList = list;
		this.mArrayList = arraylist;
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
	public Object instantiateItem(View arg0, final int i) {
		((ViewPager) arg0).addView(mList.get(i));
		mList.get(i).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				if(mArrayList.get(i).get("AndroidForm")!=null){
					if(mArrayList.get(i).get("AndroidForm").toString().equals("h5")){
						intent.setClass(mContext, WebActivity.class);
						bundle = new Bundle();
						if(mArrayList.get(i).get("Title")!=null) {
							bundle.putString("title", mArrayList.get(i).get("Title").toString());
						}
						if(mArrayList.get(i).get("LinkUrl")!=null) {
							bundle.putString("url", mArrayList.get(i).get("LinkUrl").toString());
						}
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}else {
						if(mArrayList.get(i).get("AndroidForm").toString().equals("MallActivity")) {
							try {
								intent.setClass(mContext, Class.forName("com.zjxfood.activity." + mArrayList.get(i).get("AndroidForm")));
								bundle = new Bundle();
								bundle.putString("type", "jb_tj");
								bundle.putString("typeName", "ssjb");
								bundle.putString("titleName", "天天特价");
								bundle.putString("proportion", "1");
								bundle.putString("shouCode", "2");
								bundle.putString("group", "2");
								intent.putExtras(bundle);
								mContext.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(mContext, "请下载最新版本", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}else if(mArrayList.get(i).get("Content")!=null){
					mPopup = new ContentPopup((Activity)mContext,
							mArrayList.get(i).get("Title").toString());
					mPopup.showAtLocation(mList.get(i), Gravity.CENTER, 0, 0);
				}else if(mArrayList.get(i).get("LinkUrl")!=null){
					intent.setClass(mContext, WebActivity.class);
					bundle = new Bundle();
					if(mArrayList.get(i).get("Title")!=null) {
						bundle.putString("title", mArrayList.get(i).get("Title").toString());
					}
					if(mArrayList.get(i).get("LinkUrl")!=null) {
						bundle.putString("url", mArrayList.get(i).get("LinkUrl").toString());
					}
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});
		return mList.get(i);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

}
