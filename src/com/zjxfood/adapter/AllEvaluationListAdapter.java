package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.view.RoundImageView;

public class AllEvaluationListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils, mBitmapUtils2;

	// private ArrayList<String> mPathList;

	public AllEvaluationListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		// mBitmapUtils = new BitmapUtils(mContext);
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
		mBitmapUtils2 = new BitmapUtils(mContext);
		mBitmapUtils.configDefaultBitmapMaxSize(100, 100);
		mBitmapUtils2.configDefaultBitmapMaxSize(100, 100);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang);
		// mPathList = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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

	public void notify(ArrayList<HashMap<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.all_evaluation_list_item,
					null);

			mHolder.mUserName = (TextView) convertView
					.findViewById(R.id.evaluation_user_info_name);
			mHolder.mdate = (TextView) convertView
					.findViewById(R.id.evaluation_user_info_date);
			mHolder.mContentText = (TextView) convertView
					.findViewById(R.id.evaluation_user_info_datail);
			mHolder.mRoundImageView = (RoundImageView) convertView
					.findViewById(R.id.eval_item_user_head_image);
			mHolder.mImageView1 = (ImageView) convertView
					.findViewById(R.id.all_evaluation_item_image1);
			mHolder.mImageView2 = (ImageView) convertView
					.findViewById(R.id.all_evaluation_item_image2);
			mHolder.mImageView3 = (ImageView) convertView
					.findViewById(R.id.all_evaluation_item_image3);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (mList.get(position).get("create_time") != null) {
			mHolder.mdate.setText(mList.get(position).get("create_time")
					.toString());
		}
		if (mList.get(position).get("username") != null) {
			try{
			if (Constants.checkPhoneNum(mList.get(position).get("username")
						.toString())) {
				String phone = splitePhone(mList.get(position).get("username")
						.toString());
				mHolder.mUserName.setText(phone);
			} else {
				mHolder.mUserName.setText(mList.get(position).get("username")
						.toString());
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if (mList.get(position).get("pltext") != null) {
			mHolder.mContentText.setText(mList.get(position).get("pltext")
					.toString());
		}

		if (mList.get(position).get("avatar") != null) {
//			Log.i("avatar", mList.get(position).get("avatar")+"===============");
			mBitmapUtils.display(mHolder.mRoundImageView, mList.get(position)
					.get("avatar").toString());

		}
		// OnClickListener clickListener = new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// if(mPathList!=null && mPathList.size()>0){
		// Intent intent = new Intent();
		// intent.setClass(mContext,
		// ViewPagerActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putStringArrayList("list", mPathList);
		// intent.putExtras(bundle);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// mContext.startActivity(intent);
		// }
		// }
		// };
		if (mList.get(position).get("images1") != null) {

			mBitmapUtils2.display(mHolder.mImageView1,
					mList.get(position).get("images1").toString());

		}
		if (mList.get(position).get("images2") != null) {

			mBitmapUtils2.display(mHolder.mImageView2,
					mList.get(position).get("images2").toString());

		}
		if (mList.get(position).get("images3") != null) {

			mBitmapUtils2.display(mHolder.mImageView3,
					mList.get(position).get("images3").toString());

		}
		return convertView;
	}

	class ViewHolder {
		TextView mUserName;
		TextView mdate;
		TextView mContentText;
		RoundImageView mRoundImageView;
		ImageView mImageView1;
		ImageView mImageView2;
		ImageView mImageView3;
	}

	public static String splitePhone(String phone) {
		String[] tel = new String[phone.length()];
		StringBuffer sb = new StringBuffer();
		if (tel.length > 0) {
			for (int i = 0; i < tel.length; i++) {
				if (i > 2 && i < 7) {
					sb.append("*");
				} else {
					sb.append(phone.charAt(i));
				}
			}
		}
		return sb.toString();
	}

}
