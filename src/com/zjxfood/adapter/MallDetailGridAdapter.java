package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MallDetailGridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private int clickTemp = -1;

	public MallDetailGridAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mList = list;
		this.mContext = context;
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

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.mall_detail_grid_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView
					.findViewById(R.id.mall_detail_item_text);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
			mHolder.mTextView
					.setBackgroundResource(R.drawable.popup_mall_size_select_solid);
			mHolder.mTextView.setTextColor(mContext.getResources().getColor(
					R.color.white));
		} else {
			mHolder.mTextView
					.setBackgroundResource(R.drawable.popup_mall_size_solid);
			mHolder.mTextView.setTextColor(mContext.getResources().getColor(
					R.color.main_merchant_item_title_color));

		}
		if (!Constants.isNull(mList.get(position).get("color"))
				&& !Constants.isNull(mList.get(position).get("chima"))) {
			mHolder.mTextView.setText(mList.get(position).get("color")
					.toString()
					+ "-" + mList.get(position).get("chima").toString());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mTextView;
	}

}
