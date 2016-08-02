package com.zjxfood.adapter;


import java.util.List;

import com.project.util.DensityUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
/**
 * 商城推广图片适配器
 * @author lenovo
 *
 */
@SuppressWarnings("deprecation")
public class MallTitleGalleryAdapter extends BaseAdapter{

	private Context mContext;
	//图片组
	private int[] mImages;
//	private List<View> mList;
	//高度
	private static final int IMAGE_PX_HEIGHT = 240;
	public MallTitleGalleryAdapter(Context context,int[] images){
		this.mContext = context;
		this.mImages = images;
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
		imageView.setImageResource(mImages[position % mImages.length]);
		
//		imageView.setImageBitmap(mBitmaps[position % mBitmaps.length]);
		//填充样式
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//设置布局
		imageView.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.FILL_PARENT, DensityUtils.dp2px(mContext, 160)));
//		imageView.setScaleType(ScaleType.FIT_XY);
		RelativeLayout borderImg = new RelativeLayout(mContext);
//		borderImg.setPadding(0, 0, 0, 0);
		//borderImg.setBackgroundResource(R.drawable.bg_gallery);// 设置ImageView边框
		borderImg.addView(imageView);
		return borderImg;
		//
	}
}
