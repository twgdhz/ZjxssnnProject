package com.zjxfood.red;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.red.RedListAdapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RedWinInfoAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	public RedWinInfoAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mList = list;
		mBitmapUtils = new BitmapUtils(mContext);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.hongbao_faile);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.red_win_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mUserName = (TextView) convertView.findViewById(R.id.red_win_user_name);
			mHolder.mPriceText = (TextView) convertView.findViewById(R.id.red_win_price_show);
			mHolder.mDateText = (TextView) convertView.findViewById(R.id.red_win_user_date);
			mHolder.mHeadImage = (ImageView) convertView.findViewById(R.id.red_win_user_image);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("ReciveUserName")!=null){
			mHolder.mUserName.setText(mList.get(position).get("ReciveUserName").toString());
		}
		if(mList.get(position).get("HbMoney")!=null){
			mHolder.mPriceText.setText(mList.get(position).get("HbMoney").toString()+"元");
		}
		if(mList.get(position).get("CreateOn")!=null){
			mHolder.mDateText.setText(mList.get(position).get("CreateOn").toString());
		}
		if (mList.get(position).get("ReciveUserPhoto") != null) {
			mBitmapUtils.display(mHolder.mHeadImage, mList.get(position)
					.get("ReciveUserPhoto").toString());
		}else{
			mHolder.mHeadImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hongbao_faile));
		}
//		Log.i("头像", mList.get(position).get("ReciveUserPhoto")+"================"+position);
		return convertView;
	}
	class ViewHolder{
		private TextView mUserName;
		private ImageView mHeadImage;
		private TextView mPriceText;
		private TextView mDateText;
	}
}
