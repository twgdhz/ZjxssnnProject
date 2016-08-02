package com.zjxfood.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.DensityUtils;
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
public class TitleGalleryAdapter extends BaseAdapter{

	private Context mContext;
	//图片组
//	private int[] mImages = {R.drawable.title_image1,R.drawable.title_image2,R.drawable.title_image3,R.drawable.title_image4};

	private Bitmap[] mBitmaps;
	private String[] mImagePath;
	//高度
	private static final int IMAGE_PX_HEIGHT = 240;
	private ImageView[] mImageViews;
	private ArrayList<String> mList;
	private BitmapUtils mBitmapUtils;
	
	public TitleGalleryAdapter(Context context,String[] imagePath){
		this.mContext = context;
		this.mImagePath = imagePath;
		mBitmapUtils = new BitmapUtils(mContext);
//		this.mImageViews = imageViews;
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
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.cacheInMemory(false).cacheOnDisc(true)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();
		//定义一个imageview
		ImageView imageView = new ImageView(mContext);
		//设置当前显示图片
//		imageView.setImageResource(mImages[position % mImages.length]);
//		imageView.setImageResource(mImageViews[position % mImageViews.length]);
//		imageView.setImageBitmap(mBitmaps[position % mBitmaps.length]);
//		ImageLoader.getInstance().displayImage(mImagePath[position % mImagePath.length], imageView);
		mBitmapUtils.display(imageView, mImagePath[position % mImagePath.length]);
		//填充样式
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//设置布局
		imageView.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 160)));
		
		RelativeLayout borderImg = new RelativeLayout(mContext);
//		borderImg.setPadding(2, 2, 2, 2);
		//borderImg.setBackgroundResource(R.drawable.bg_gallery);// 设置ImageView边框
		borderImg.addView(imageView);
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if(MerchantInfoActivity.mBitmaps!=null){
				mList = new ArrayList<String>();
				for(int i=0;i<mImagePath.length;i++){
					mList.add(mImagePath[i]);
				}
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
