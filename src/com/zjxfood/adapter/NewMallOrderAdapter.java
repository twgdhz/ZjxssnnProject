package com.zjxfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjxfood.activity.LogisticsActivity;
import com.zjxfood.activity.MallPayWayActivity;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.interfaces.CarsListImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/25.
 */
public class NewMallOrderAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private NewMallOrderAdapter2 mAdapter;
    private ArrayList<HashMap<String,Object>> mList;
    private ArrayList<HashMap<String,Object>> mItemLists,mItemLists2;
    private CarsListImpl mCarImpl;
    private String mMallDetail = "商品信息";
    public NewMallOrderAdapter(Context context,ArrayList<HashMap<String,Object>> list,CarsListImpl carImpl){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mList = list;
        this.mCarImpl = carImpl;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void notify(ArrayList<HashMap<String,Object>> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view==null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.new_mall_order_item2,null);
            mHolder.mListView = (ListView) view.findViewById(R.id.new_mall_order_item_list2);
            mHolder.mAllNums = (TextView) view.findViewById(R.id.new_mall_bottom_num);
            mHolder.mAllPrices = (TextView) view.findViewById(R.id.new_mall_bottom_price);
            mHolder.mOrderId = (TextView) view.findViewById(R.id.new_order_id);
            mHolder.mPayStatus = (TextView) view.findViewById(R.id.pay_status);
            mHolder.mSsbText = (TextView) view.findViewById(R.id.new_order_item_zfssb_value);
            mHolder.mPayBtn = (Button) view.findViewById(R.id.new_mall_order_pay);
            mHolder.mCheckAddressBtn = (Button) view.findViewById(R.id.new_mall_order_check_btn);
            mHolder.mKefuLayout = (ImageView) view.findViewById(R.id.mall_contact_qq_image);
            mHolder.mYfText = (TextView) view.findViewById(R.id.new_mall_bottom_text4);
                    view.setTag(mHolder);
            mHolder.mCashText = (TextView) view.findViewById(R.id.new_order_item_cash_value);
            mHolder.mGoldText = (TextView) view.findViewById(R.id.new_order_item_zfjb_value);
            mHolder.mUserAdress = (TextView) view.findViewById(R.id.new_order_item_address_value);
            mHolder.mUserName = (TextView) view.findViewById(R.id.new_order_item_username_value);
            mHolder.mUserPhone = (TextView) view.findViewById(R.id.new_order_item_phone_value);
            mHolder.mLogistics = (Button) view.findViewById(R.id.new_mall_order_logistics_btn);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        if(mList.get(i).get("UserNickName")!=null){
            mHolder.mUserName.setText(mList.get(i).get("UserNickName")+"");
        }
        if(mList.get(i).get("UserPhone")!=null){
            mHolder.mUserPhone.setText(mList.get(i).get("UserPhone")+"");
        }
        if(mList.get(i).get("UserAddress")!=null){
            mHolder.mUserAdress.setText(mList.get(i).get("UserAddress")+"");
        }
        if(mList.get(i).get("GoldTotal")!=null){
            mHolder.mGoldText.setText(mList.get(i).get("GoldTotal")+"");
        }
        if(mList.get(i).get("ProductCashTotal")!=null && mList.get(i).get("YFTotal")!=null && mList.get(i).get("CurrencyPayProduct")!=null){
            mHolder.mCashText.setText((Float.parseFloat(mList.get(i).get("ProductCashTotal").toString()) + Float.parseFloat(mList.get(i).get("YFTotal").toString()) - Float.parseFloat(mList.get(i).get("CurrencyPayProduct").toString()))+"");
        }
        if(mList.get(i).get("YFTotal")!=null){
            mHolder.mYfText.setText("(含运费：￥"+mList.get(i).get("YFTotal")+")");
        }
        if(mList.get(i).get("OrderId")!=null){
            mHolder.mOrderId.setText("订单编号："+mList.get(i).get("OrderId"));
        }
        if(mList.get(i).get("OrderStatusText")!=null){
                mHolder.mPayStatus.setText(mList.get(i).get("OrderStatusText")+"");
        }
        if(mList.get(i).get("OrderStatus")!=null){
            if(mList.get(i).get("OrderStatus").toString().equals("1")){
                mHolder.mPayBtn.setVisibility(View.VISIBLE);
                mHolder.mCheckAddressBtn.setVisibility(View.VISIBLE);
                mHolder.mLogistics.setVisibility(View.GONE);
            }else if(mList.get(i).get("OrderStatus").toString().equals("3")){
               mHolder.mLogistics.setVisibility(View.VISIBLE);
                mHolder.mPayBtn.setVisibility(View.GONE);
                mHolder.mCheckAddressBtn.setVisibility(View.GONE);
            }else if(mList.get(i).get("OrderStatus").toString().equals("4")){
                mHolder.mLogistics.setVisibility(View.GONE);
                mHolder.mPayBtn.setVisibility(View.GONE);
                mHolder.mCheckAddressBtn.setVisibility(View.GONE);
            }else if(mList.get(i).get("OrderStatus").toString().equals("2")){
                mHolder.mLogistics.setVisibility(View.GONE);
                mHolder.mPayBtn.setVisibility(View.GONE);
                mHolder.mCheckAddressBtn.setVisibility(View.GONE);
            }
        }

        if(mList.get(i).get("OrderItems")!=null){
            mItemLists = Constants.getJsonArray(mList.get(i).get("OrderItems").toString());
            mHolder.mAllNums.setText(mItemLists.size()+"");
        }
        if(mList.get(i).get("ProductCashTotal")!=null && mList.get(i).get("YFTotal")!=null){
            mHolder.mAllPrices.setText(Float.parseFloat(mList.get(i).get("ProductCashTotal").toString()) + Float.parseFloat(mList.get(i).get("YFTotal").toString()) + "");
        }
        if(mList.get(i).get("SSBTotal")!=null){
            mHolder.mSsbText.setText(mList.get(i).get("SSBTotal").toString());
        }
        mAdapter = new NewMallOrderAdapter2(mContext,mItemLists);
        mHolder.mListView.setAdapter(mAdapter);
        mHolder.mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemLists2 = Constants.getJsonArray(mList.get(i).get("OrderItems").toString());
                Intent intent = new Intent();
                intent.setClass(mContext,
                        MallPayWayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("price",mList.get(i).get("WaitPayTotal") + "");
//                bundle.putString("price","0.1");
                bundle.putString("merchantName", mItemLists2.get(0).get("ProductName")+"");
                //商家ID 商城页面随便传一个来充数
                bundle.putString("mId", "52503");
                bundle.putString("type", "gift");
                bundle.putString("flum", "0");
                bundle.putString("LoginImage", mItemLists2.get(0).get("ProductImage")+"");
                bundle.putString("mallOrMerchant", "mall");
                bundle.putString("orderId", mList.get(i).get("OrderId")+"_"+(int)(Math.random()*10000));
//                bundle.putString("useCurrency", mUseCurrency);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        mHolder.mCheckAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCarImpl.alertPop(mList.get(i).get("UserAddress")+"");
                mCarImpl.notifyList(null,mList.get(i).get("OrderId")+"","",0,null);
            }
        });
        mHolder.mKefuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemLists2 = Constants.getJsonArray(mList.get(i).get("OrderItems").toString());
                mMallDetail = "<img border=\\\"0\" src=\""+mItemLists2.get(0).get("ProductImage")+"\" />  <p>商品类型："+mItemLists2.get(0).get("ProudctTypeText")+"</p>  <p>商品名称："+mItemLists2.get(0).get("ProductName")+"</p>  <p>商品价格："+mList.get(i).get("WaitPayTotal")+",运费："+mList.get(i).get("YFTotal")+"</p>  <p>用户账号："+Constants.mUserName+"</p>  <p>用户等级："+Constants.UserLevelMemo+"</p>";
                mCarImpl.startAppkf(mMallDetail);
            }
        });
        mHolder.mLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("fh",mList.get(i).get("ShipmentCompany")+":"+ mList.get(i).get("ShipmentNum"));
                intent.putExtras(bundle);
                intent.setClass(mContext, LogisticsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
    class ViewHolder{
        TextView mOrderId;
        TextView mAllNums;
        TextView mAllPrices;
        TextView mSsbText;
        TextView mPayStatus;
        Button mPayBtn;
        ListView mListView;
        Button mCheckAddressBtn;
        ImageView mKefuLayout;
        TextView mYfText;
        TextView mCashText;
        TextView mGoldText;
        TextView mUserName;
        TextView mUserPhone;
        TextView mUserAdress;
        Button mLogistics;
    }

}
