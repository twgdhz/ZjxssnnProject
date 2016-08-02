package com.zjxfood.cashindiana;

import java.util.HashMap;

import java.util.ArrayList;

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

public class CashMyIndianaListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> mList;
	private BitmapUtils mBitmapUtils;
	
	public CashMyIndianaListAdapter(Context context,ArrayList<HashMap<String, Object>> list){
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
			convertView = mInflater.inflate(R.layout.cash_order_layout, null);
			mHolder = new ViewHolder();
//			mHolder.mOrderText = (TextView) convertView.findViewById(R.id.cash_order_id_text);
//			mHolder.mDateText = (TextView) convertView.findViewById(R.id.cash_order_date_text);
			mHolder.mMoneyText = (TextView) convertView.findViewById(R.id.cash_order_money_text);
			mHolder.mStateText = (TextView) convertView.findViewById(R.id.cash_order_state_text);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(mList.get(position).get("OrderNum")!=null){
			mHolder.mOrderText.setText("订单号："+mList.get(position).get("OrderNum").toString());
		}
		if(mList.get(position).get("CreateOn")!=null){
			mHolder.mDateText.setText("创建时间："+mList.get(position).get("CreateOn").toString());
		}
		if(mList.get(position).get("TotalMoney")!=null){
			mHolder.mMoneyText.setText("支付金额："+mList.get(position).get("TotalMoney").toString());
		}
		if(mList.get(position).get("IsPayed")!=null){
			if(mList.get(position).get("IsPayed").toString().equals("false")){
				mHolder.mStateText.setText("支付状态：未支付");
			}else{
				mHolder.mStateText.setText("支付状态：已支付");
			}
			
		}
		return convertView;
	}
	class ViewHolder{
		private TextView mOrderText;
		private TextView mDateText;
		private TextView mMoneyText;
		private TextView mStateText;
	}
}
