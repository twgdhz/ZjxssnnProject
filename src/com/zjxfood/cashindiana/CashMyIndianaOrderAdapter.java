package com.zjxfood.cashindiana;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.zjxfood.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CashMyIndianaOrderAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	public CashMyIndianaOrderAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mList = list;
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
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
			convertView = mInflater.inflate(R.layout.my_indiana_list_item, null);
			mHolder = new ViewHolder();
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.my_indiana_item_image);
			mHolder.mNameText = (TextView) convertView.findViewById(R.id.my_indiana_item_content_name);
			mHolder.mButton = (Button) convertView.findViewById(R.id.my_indiana_item_content_btn);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("ImgUrl")!=null){
			mBitmapUtils.display(mHolder.mImageView, mList.get(position).get("ImgUrl").toString());
		}
		if(mList.get(position).get("ProductName")!=null && mList.get(position).get("Id")!=null){
			mHolder.mNameText.setText("【"+mList.get(position).get("Id")+"期】"+mList.get(position).get("ProductName").toString());
		}
		if(mList.get(position).get("EndTime")!=null){
			if(Utils.isEnd(mList.get(position).get("EndTime").toString())){
				mHolder.mButton.setBackgroundResource(R.drawable.shape_indiana_cj_bg);
				mHolder.mButton.setTextColor(mContext.getResources().getColor(R.color.indiana_item_choujiang_coloe2));
				mHolder.mButton.setText("进行中");
			}else{
				mHolder.mButton.setBackgroundResource(R.drawable.shape_indiana_cj_end_bg);
				mHolder.mButton.setTextColor(mContext.getResources().getColor(R.color.my_user_decription));
				mHolder.mButton.setText("已结束");
			}
		}
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mNameText;
		private Button mButton;
	}
}
