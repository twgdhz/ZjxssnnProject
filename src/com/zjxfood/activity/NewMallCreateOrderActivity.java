package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.CreateCarsAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.model.CarPrice;
import com.zjxfood.model.Products;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/28.
 */
public class NewMallCreateOrderActivity extends AppActivity implements View.OnClickListener {

    private CreateCarsAdapter mAdapter;
    private ListView mListView;
    private TextView mTitleText;
    private ImageView mBackImage;
    private static ArrayList<HashMap<String, Object>> mList;

    private TextView mSsbText, mSsjbText, mCashText;
    private HashMap<String, Object> mDefaulAddressMap;
    private TextView mUserName, mUserPhone, mUserAddress;
    private static String mAddressId, mUseCurrency;
    private Button mCreateBtn;
    private PopupWindow mPopupWindow;
    private TextView mOkText, mCancelText;
    private HashMap<String, Object> mMallMap, mOrderIdMap;
    private String mOrderId;
    private HashMap<String, Object> mPriceMap, mTempMap;
    private RelativeLayout mAddressLayout;
    private String userName, userPhone, userAddress, flag;
    private boolean isContiun = false;
    private LinearLayout mBottomLayout;
    private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_create_cars_order);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();

        flag = getIntent().getStringExtra("flag");
        if(flag != null && flag.equals("1")){
            mList = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("list");
            mUseCurrency = getIntent().getStringExtra("useCurrency");
            if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
                getDefaultAddress();
            }
        }else if (flag != null && flag.equals("2")) {
            userName = getIntent().getStringExtra("userName");
            userPhone = getIntent().getStringExtra("userPhone");
            userAddress = getIntent().getStringExtra("userAddress");
            mAddressId = getIntent().getStringExtra("addressId");
            if (userName != null) {
                mUserName.setText(userName);
            } else {
                mUserName.setText("");
            }
            if (userPhone != null) {
                mUserPhone.setText(userPhone);
            } else {
                mUserPhone.setText("");
            }
            if (userAddress != null) {
                mUserAddress.setText(userAddress);
            } else {
                mUserAddress.setText("");
            }
        }
        mAdapter = new CreateCarsAdapter(getApplicationContext(), mList);
        mListView.setAdapter(mAdapter);
        getAllPrice();

    }

    private void init() {
        mListView = (ListView) findViewById(R.id.create_cars_order_list);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mSsbText = (TextView) findViewById(R.id.cars_ssb_text2);
        mSsjbText = (TextView) findViewById(R.id.cars_ssjb_text2);
        mCashText = (TextView) findViewById(R.id.cars_money_text2);
        mUserAddress = (TextView) findViewById(R.id.create_order_address_value);
        mUserName = (TextView) findViewById(R.id.create_order_name_value);
        mUserPhone = (TextView) findViewById(R.id.create_order_phone_value);
        mCreateBtn = (Button) findViewById(R.id.create_order_btn);
        mAddressLayout = (RelativeLayout) findViewById(R.id.new_cars_bottom_zonghui_layout);
        mBottomLayout = (LinearLayout) findViewById(R.id.create_order_bottom);
//        mListView.setPadding(0,0,0,mBottomLayout.getHeight());


        mTitleText.setText("创建订单");
        mBackImage.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
        mAddressLayout.setOnClickListener(this);
    }

    private void getDefaultAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid=" + Constants.mId);
        XutilsUtils.get(Constants.getDefaultAddress2, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            mDefaulAddressMap = Constants.getJsonObject(res.result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mDefaulAddressMap != null
                                && mDefaulAddressMap.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_back_image:
                finish();
                break;
            //创建订单
            case R.id.create_order_btn:

                createOrder();
                break;
            //更换地址
            case R.id.new_cars_bottom_zonghui_layout:
                intent.setClass(getApplicationContext(),
                        NewAddressManageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.auction_pay_ok_true:
                    mPopupWindow.dismiss();
//                    createOrder();
                    handler.sendEmptyMessageDelayed(2,0);
                    break;

                case R.id.auction_pay_cancel_false:
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),MallOrderActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void createOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append("&userId=" + Constants.mId + "&addressId=" + mAddressId
                + "&memo=" + "&useCurrency=" + mUseCurrency
                + "&products=" + mList.toString());

        Products products1 = new Products();
        products1.setAddressId(mAddressId);
        products1.setMemo("");
        products1.setUseCurrency(mUseCurrency);
        products1.setUserId(Constants.mId);
        products1.setProducts(mList);

        String a = JSONObject.toJSON(products1).toString();
        Log.i("表单数据", a + "============================");
        RequestParams params = new RequestParams("UTF-8");
        try {
            params.setBodyEntity(new StringEntity(a, "UTF-8"));
            params.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XutilsUtils.post(Constants.createNewMallOrder, sb, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("新商城订单", res.result
                                + "========onSuccess==============");
                        mMallMap = Constants.getJsonObject(res.result);
                        if (mMallMap != null && mMallMap.size() > 0) {
                            if (mMallMap.get("Code").toString().equals("200")
                                    && mMallMap.get("Data") != null) {
                                mOrderIdMap = Constants.getJsonObject(mMallMap.get("Data").toString());
                                mOrderId = mOrderIdMap.get("PayId").toString();
                                if(Float.parseFloat(mOrderIdMap.get("PayMoney")+"")==0){
                                    handler.sendEmptyMessageDelayed(6, 0);
                                }else{
                                    handler.sendEmptyMessageDelayed(5, 0);
                                }
//                                handler.sendEmptyMessageDelayed(2, 0);
                                Log.i("mallOrderId", mOrderId
                                        + "========mallOrderId==============");

                               SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
                                String result = sp.getString("cars",null);
                                ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
                                if(result!=null && result.length()>0) {
                                    Constants.mCarsMap = Constants.getJsonObjectToMap(result);
                                    for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
                                        Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                        list.add(entry.getValue());
                                    }
                                }
                                for(int i=0;i<mList.size();i++){
                                   Constants.mCarsMap.remove(mList.get(i).get("AttrId")+"");
                                }
//                                Constants.mCarsMap = new HashMap<String, HashMap<String, Object>>();
//                                for(int i=0;i<list.size();i++){
//                                    Constants.mCarsMap.put(list.get(i).get("AttrId")+"",list.get(i));
//                                }
//                                Constants.mCarsMap.put(mDetailMap.get("Id")+"", mCarsList);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("cars", com.alibaba.fastjson.JSONObject.toJSON(Constants.mCarsMap).toString());
                                editor.commit();
                            }else{
                                handler.sendEmptyMessageDelayed(4, 0);
                            }
//                            Toast.makeText(getApplicationContext(),mMallMap.get("Message")+"",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getAllPrice() {
        StringBuilder sb = new StringBuilder();
        sb.append("&userId=" + Constants.mId + "&useCurrency=true" + "&products=" + mList.toString());

        CarPrice carPrice = new CarPrice();
        carPrice.setUserId(Constants.mId);
        carPrice.setUseCurrency("true");
        carPrice.setProducts(mList);

        String a = JSONObject.toJSON(carPrice).toString();
        Log.i("表单数据", a + "============================");
        RequestParams params = new RequestParams("UTF-8");
        try {
            params.setBodyEntity(new StringEntity(a, "UTF-8"));
            params.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XutilsUtils.post(Constants.getAllPrice, sb, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("费用计算", res.result
                                + "========onSuccess==============");
                        mTempMap = Constants.getJsonObject(res.result);
                        if (mTempMap != null && mTempMap.get("Code") != null) {
                            if (mTempMap.get("Code").toString().equals("200")) {
                                isContiun = true;
                                mPriceMap = Constants.getJsonObjectByData(res.result);
                                handler.sendEmptyMessageDelayed(3, 0);
                            } else {
                                isContiun = false;
//                                Toast.makeText(getApplicationContext(),mTempMap.get("Message")+"=========",Toast.LENGTH_SHORT);
                            }
                        }

                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mDefaulAddressMap != null && mDefaulAddressMap.size() > 0) {
                        if (mDefaulAddressMap.get("Id").toString()
                                .equals("null")) {
                            return;
                        }
                        mUserName.setText(mDefaulAddressMap.get("Realname") + "");
                        mUserPhone.setText(mDefaulAddressMap.get("Mobile") + "");
                        mUserAddress.setText(mDefaulAddressMap.get("Address") + "");
                        mAddressId = mDefaulAddressMap.get("Id").toString();
                    }
                    break;
                case 2:
                    if (isContiun) {
                        intent.setClass(getApplicationContext(),
                                MallPayWayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("price", mOrderIdMap.get("PayMoney") + "");
//                        bundle.putString("price",  "0.1");
                        bundle.putString("merchantName", mList.get(0).get("ProductName").toString());
                        //商家ID 商城页面随便传一个来充数
                        bundle.putString("mId", "52503");
                        bundle.putString("type", "gift");
                        bundle.putString("flum", "0");
                        bundle.putString("LoginImage", mList.get(0).get("Image").toString());
                        bundle.putString("mallOrMerchant", "mall");
                        bundle.putString("orderId", mOrderId+"_"+(int)(Math.random()*10000));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }

                        SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
                        String result = sp.getString("cars",null);

                        if(result!=null && result.length()>0) {
                            Constants.mCarsMap = Constants.getJsonObjectToMap(result);
                            for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
                                Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                mList.add(entry.getValue());
                            }

                        }
                    }else {
                        if (mTempMap != null && mTempMap.get("Message") != null) {
                            Toast.makeText(getApplicationContext(), mTempMap.get("Message") + "", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 3:
                    if (mPriceMap.get("SSBCost") != null) {
                        mSsbText.setText(mPriceMap.get("SSBCost") + "");
                    }
                    if (mPriceMap.get("GoldCost") != null) {
                        mSsjbText.setText(mPriceMap.get("GoldCost") + "");
                    }
                    if (mPriceMap.get("CashCost") != null) {
                        mCashText.setText(mPriceMap.get("CashCost") + "");
                    }
                    break;
                case 4:
                    mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
                            NewMallCreateOrderActivity.this,mMallMap.get("Message")+"");
                    mCommitmentPopupWindow.showAtLocation(mListView, Gravity.CENTER,
                            0, 0);
                    break;
                case 5:
                    LayoutInflater inflater = LayoutInflater
                        .from(getApplicationContext());

                View view = inflater.inflate(R.layout.auction_value_popup, null);
                    TextView message = (TextView) view.findViewById(R.id.merchant_settled_popup_content_show);
                mOkText = (TextView) view.findViewById(R.id.auction_pay_ok_true);
                mCancelText = (TextView) view
                        .findViewById(R.id.auction_pay_cancel_false);
                    message.setText(mMallMap.get("Message")+"");
                mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, false);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                // 设置SelectPicPopupWindow弹出窗体的背景
                mPopupWindow.setBackgroundDrawable(dw);
                // 设置SelectPicPopupWindow弹出窗体动画效果
                mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
                mPopupWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
                mOkText.setOnClickListener(mClickListener);
                mCancelText.setOnClickListener(mClickListener);
                    break;
                case 6:
                   inflater = LayoutInflater
                            .from(getApplicationContext());

                  view = inflater.inflate(R.layout.auction_value_popup, null);
                    message = (TextView) view.findViewById(R.id.merchant_settled_popup_content_show);
                    mOkText = (TextView) view.findViewById(R.id.auction_pay_ok_true);
                    mCancelText = (TextView) view
                            .findViewById(R.id.auction_pay_cancel_false);
                    message.setText(mMallMap.get("Message")+"");
                    mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, false);
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setFocusable(true);
                    dw = new ColorDrawable(0xb0000000);
                    // 设置SelectPicPopupWindow弹出窗体的背景
                    mPopupWindow.setBackgroundDrawable(dw);
                    // 设置SelectPicPopupWindow弹出窗体动画效果
                    mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
                    mPopupWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
                    mOkText.setOnClickListener(mOrderClick);
                    mCancelText.setOnClickListener(mClickListener);
                    break;
            }
        }
    };
    View.OnClickListener mOrderClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),MallOrderActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
