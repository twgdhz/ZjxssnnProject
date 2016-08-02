package com.zjxfood.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.zjxfood.activity.MerchantTitleImageActivity;
import com.zjxfood.photoview.activity.ViewPagerActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 商城推广图片适配器
 * @author lenovo
 *
 */
@SuppressWarnings("deprecation")
public class MallDetailGalleryAdapter extends BaseAdapter{

	private Context mContext;
	//图片组
//	private int[] mImages = {R.drawable.title_image1,R.drawable.title_image2,R.drawable.title_image3,R.drawable.title_image4};

	private ArrayList<String> mList;
	private BitmapUtils mBitmapUtils;
	
	public MallDetailGalleryAdapter(Context context,ArrayList<String> list){
		this.mContext = context;
		this.mList = list;
		mBitmapUtils = new BitmapUtils(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//定义一个imageview
		ImageView imageView = new ImageView(mContext);
		//设置当前显示图片
		mBitmapUtils.display(imageView, mList.get(position % mList.size()));
		//填充样式
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//设置布局
		imageView.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.FILL_PARENT, ScreenUtils.getScreenWidth(mContext)));
		
		RelativeLayout borderImg = new RelativeLayout(mContext);
//		borderImg.setPadding(2, 2, 2, 2);
		//borderImg.setBackgroundResource(R.drawable.bg_gallery);// 设置ImageView边框
		borderImg.addView(imageView);
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if(MerchantInfoActivity.mBitmaps!=null){
//				mList = new ArrayList<String>();
//				for(int i=0;i<mImagePath.length;i++){
//					mList.add(mImagePath[i]);
//				}
				Intent intent = new Intent();
				intent.setClass(mContext,
						ViewPagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("list", mList);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
//				}
			}
		});
		return borderImg;
	}
}
