package com.zjxfood.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;
import com.zjxfood.common.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCurrencyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;

	public MyCurrencyAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_currency_list_item,
					null);
			mHolder = new ViewHolder();
			mHolder.mDate = (TextView) convertView
					.findViewById(R.id.my_currency_item_text_date);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.my_currency_item_text_title);
			mHolder.mPrice = (TextView) convertView
					.findViewById(R.id.my_currency_item_text_money);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (!Constants.isNull(mList.get(position).get("createtime"))) {
			mHolder.mDate.setText(mList.get(position).get("createtime")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("changetypename"))) {
			mHolder.mName.setText(mList.get(position).get("changetypename")
					.toString());
		}
		if (!Constants.isNull(mList.get(position).get("currency"))) {
			mHolder.mPrice.setText(mList.get(position).get("currency")
					.toString());
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mName;
		private TextView mDate;
		private TextView mPrice;
	}

}
