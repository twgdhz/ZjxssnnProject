package com.zjxfood.red;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.RedInterfaceImpl;
import com.zjxfood.popupwindow.GrabRedPopupWindow;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RedListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	private DecimalFormat df;
	private RedInterfaceImpl mClick;
	public RedListAdapter(Context context,ArrayList<HashMap<String, Object>> list,RedInterfaceImpl impl){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		this.mList = list;
		df = new DecimalFormat("0.000");
		mBitmapUtils
		.configDefaultLoadFailedImage(R.drawable.hongbao_faile);
		this.mClick = impl;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void notifyList(ArrayList<HashMap<String, Object>> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.red_list_item_layout, null);
			mHolder = new ViewHolder();
			
			mHolder.mHeadLayout = (LinearLayout) convertView.findViewById(R.id.red_item_head_layout);
			mHolder.mNumberText = (TextView) convertView.findViewById(R.id.red_item_head_content_text3);
			mHolder.mNoImage = (ImageView) convertView.findViewById(R.id.red_item_no_image);
			mHolder.mHongbaoImage = (ImageView) convertView.findViewById(R.id.red_item_head_image);
			mHolder.mUserName = (TextView) convertView.findViewById(R.id.red_item_bottom_content_user);
			mHolder.mKmText = (TextView) convertView.findViewById(R.id.red_item_bottom_content_address);
			mHolder.mMemoText = (TextView) convertView.findViewById(R.id.red_item_head_content_text1);
			mHolder.mContentLayout = (LinearLayout) convertView.findViewById(R.id.red_item_content_layout);
			mHolder.mDate = (TextView) convertView.findViewById(R.id.red_item_bottom_content_date);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (mList.get(position).get("SendUserPhoto") != null) {
			mBitmapUtils.display(mHolder.mHongbaoImage, mList.get(position)
					.get("SendUserPhoto").toString());
		}
		if(mList.get(position).get("SendUserName") != null){
			mHolder.mUserName.setText("红包发起人："+mList.get(position).get("SendUserName").toString());
		}
		if(mList.get(position).get("Memo") != null){
			mHolder.mMemoText.setText(mList.get(position).get("Memo").toString());
		}
		if(mList.get(position).get("SendOn") != null){
			mHolder.mDate.setText("发送时间："+mList.get(position).get("SendOn").toString());
		}
		if(mList.get(position).get("MyDistance") != null){
			
			mHolder.mKmText.setText("距离："+df.format(Double.parseDouble(mList.get(position).get("MyDistance").toString()))+"公里");
		}
		if(mList.get(position).get("IsGeted") != null){
			//已领取
			if(mList.get(position).get("IsGeted").toString().equals("true")){
				mHolder.mHeadLayout.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_top_style2));
				mHolder.mHongbaoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hongbao_gray_png));
				mHolder.mNoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_already_receive));
				mHolder.mNumberText.setVisibility(View.GONE);
				mHolder.mNoImage.setVisibility(View.VISIBLE);
				mHolder.mNumberText.setVisibility(View.GONE);
				mHolder.mContentLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mClick.doClick(position,"3");
					}
				});
			}else if(mList.get(position).get("IsGeted").toString().equals("false")){//未领取
				if(mList.get(position).get("IsTimeOut") != null){
					//已过期
					if(mList.get(position).get("IsTimeOut").toString().equals("true")){
						mHolder.mHeadLayout.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_top_style2));
						mHolder.mHongbaoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hongbao_gray_png));
						mHolder.mNoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_be_overdue));
						mHolder.mNumberText.setVisibility(View.GONE);
						mHolder.mNoImage.setVisibility(View.VISIBLE);
						mHolder.mNumberText.setVisibility(View.GONE);
						mHolder.mContentLayout.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								mClick.doClick(position,"4");
							}
						});
					}else if(mList.get(position).get("IsTimeOut").toString().equals("false")){//未过期
						if(mList.get(position).get("RemainNum") != null){
							if(Integer.parseInt(mList.get(position).get("RemainNum").toString())>0){
								mHolder.mHeadLayout.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_top_style));
								mHolder.mHongbaoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hongbao_png));
//								
								mHolder.mNumberText.setVisibility(View.VISIBLE);
								mHolder.mNoImage.setVisibility(View.GONE);
								if(mList.get(position).get("TotalNum") != null && mList.get(position).get("RemainNum") != null){
									int a = Integer.parseInt(mList.get(position).get("TotalNum").toString())-Integer.parseInt(mList.get(position).get("RemainNum").toString());
									mHolder.mNumberText.setVisibility(View.VISIBLE);
									mHolder.mNumberText.setText("(已领取"+a+"/"+Integer.parseInt(mList.get(position).get("TotalNum").toString())+"个)");
								}
								mHolder.mContentLayout.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										mClick.doClick(position,"1");
									}
								});
							}else{
								mHolder.mHeadLayout.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_top_style2));
								mHolder.mHongbaoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hongbao_gray_png));
								mHolder.mNoImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_red_png));
								mHolder.mNumberText.setVisibility(View.GONE);
								mHolder.mNoImage.setVisibility(View.VISIBLE);
								mHolder.mNumberText.setVisibility(View.GONE);
								mHolder.mContentLayout.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										mClick.doClick(position,"2");
									}
								});
							}
						}
					}
				}
			}
		}
		
		
		return convertView;
	}
//	
//	OnClickListener clickListener = new OnClickListener() {
//		@Override
//		public void onClick(View arg0) {
//
//			if(mList.get(mPosition).get("Id")!=null){
//			mHbId = mList.get(mPosition).get("Id").toString();
//			}
////			mHbUserId = mList.get(position).get("SendUserId").toString();
//			if(mList.get(mPosition).get("SendUserName")!=null){
//			mUserName = mList.get(mPosition).get("SendUserName").toString();
//			}
//			if(mList.get(mPosition).get("SendUserPhoto")!=null){
//			mPath = mList.get(mPosition).get("SendUserPhoto").toString();
//			}
//			mGrabRedPopupWindow = new GrabRedPopupWindow(mContext,
//					clickListener,mPath,mUserName);
//			mGrabRedPopupWindow.showAtLocation(mHeadLayout, Gravity.CENTER, 0, 0);
//		
//		}
//	};
	class ViewHolder{
		private TextView mNumberText;
		private ImageView mNoImage;
		private ImageView mHongbaoImage;
		private LinearLayout mHeadLayout;
		private TextView mUserName;
		private TextView mKmText;
		private TextView mMemoText;
		private LinearLayout mContentLayout;
		private TextView mDate;
	}
}
