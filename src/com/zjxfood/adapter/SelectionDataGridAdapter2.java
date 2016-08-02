package com.zjxfood.adapter;

import java.util.List;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.AddressManListAdapter.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectionDataGridAdapter2 extends BaseAdapter{

	private Context mContext;
	private List<String> mList;
	private int clickTemp = -1;

	private int cancelTemp = -1;
	public SelectionDataGridAdapter2(Context context,List<String> list){
		this.mContext = context;
		this.mList = list;
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
		
	public void cancelSelection(int position){
			cancelTemp = position;
			notifyDataSetChanged();
		}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder mHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.selection_data_grid_item, null);
			mHolder = new ViewHolder();
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.selection_data_grid_item_text);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
			if(position==0 || position==1 || position==5 || position==6 ||position==10 || position==11 ||position==15 || position==16 ||position==20 || position==21 ||position==25 || position==26||position==30 || position==31||position==35 || position==36){
			Drawable drawable= mContext.getResources().getDrawable(R.drawable.time_selection_icon);
			drawable.setBounds(0, 00, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mHolder.mTextView.setBackgroundResource(R.color.select_time_back_color);
			mHolder.mTextView.setCompoundDrawables(null, null, drawable, null);
			}
		} else {
			mHolder.mTextView.setBackgroundResource(R.color.white);
			mHolder.mTextView.setCompoundDrawables(null, null, null, null);
		}
		if(cancelTemp==position){
			mHolder.mTextView.setBackgroundResource(R.color.white);
			mHolder.mTextView.setCompoundDrawables(null, null, null, null);
			cancelTemp = -1;
			clickTemp = -1;
		}
		if(position==0 || position==1 || position==5 || position==6 ||position==10 || position==11 ||position==15 || position==16 ||position==20 || position==21 ||position==25 || position==26||position==30 || position==31||position==35 || position==36){
			mHolder.mTextView.setText(mList.get(position));
		}else{
			mHolder.mTextView.setText(" ");
		}
		
		return convertView;
	}
	class ViewHolder{
		private TextView mTextView;
	}
}
