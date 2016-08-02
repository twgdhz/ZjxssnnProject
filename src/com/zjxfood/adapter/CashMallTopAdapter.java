package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjxfood.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CashMallTopAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;

	public CashMallTopAdapter(Context context,
							  ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
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

	public void notify(ArrayList<HashMap<String, Object>> list) {
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.new_cash_popup_grid_item, null);
			mHolder = new ViewHolder();

			mHolder.mTextView1 = (TextView) convertView.findViewById(R.id.cash_popup_item);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("name")!=null) {
			mHolder.mTextView1.setText(mList.get(position).get("name").toString());
		}
		return convertView;
	}

	class ViewHolder {
		TextView mTextView1;
	}

}
