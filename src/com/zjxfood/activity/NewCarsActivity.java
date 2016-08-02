package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.CarsAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.CarsListImpl;
import com.zjxfood.interfaces.CarsListInterface;
import com.zjxfood.model.CarPrice;
import com.zjxfood.toast.MyToast;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/27.
 */
public class NewCarsActivity extends AppActivity implements View.OnClickListener{

    private ListView mListView;
    private CarsAdapter mAdapter;
    private LinearLayout mMainLayout,mBuyLayout,mCarLayout,mMyLayout;
    private View mBottomView;
    private TextView mBottomText;
    private ImageView mBottomImage;
    private Button mNextBtn;
    private ArrayList<HashMap<String, Object>> mList,mList2,mList3;
    private TextView mSsbText,mSsjbText,mXjText;
    private HashMap<String,Object> mMallMap;
    private HashMap<String,Object> mTempMap,mPriceMap;
    private boolean isExit = false;
    private MyToast mToast;
    private CarsListImpl mCarsImpl;
    private TextView mAlertText;
    private boolean isContiun = false;
    private  Bitmap mOldMap;
    private ImageView mCarImage;
    private SharedPreferences sp;
    private TextView mClearText;
    private String mFlag = "";
    private Bundle mBundle;
    private RelativeLayout mPriceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_cars_layout);
        setImmerseLayout(findViewById(R.id.new_cars_layout));
        init();
        mToast = new MyToast(getApplicationContext());
        mList = new ArrayList<HashMap<String, Object>>();
        mList2 = new ArrayList<HashMap<String, Object>>();
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mFlag = mBundle.getString("flag");
        }
//        String json = sp.getString("cars",null);


        sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
        String result = sp.getString("cars",null);
        Log.i("购物车数据", "========================="+result);
        Log.i("购物车数据", "========================="+result);
        if(result!=null && result.length()>0) {
            Constants.mCarsMap = Constants.getJsonObjectToMap(result);
            for (Map.Entry<String, HashMap<String, Object>> entry : Constants.mCarsMap.entrySet()) {
                Log.i("key", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                entry.getValue().put("isTrue", "true");
                mList.add(entry.getValue());
            }
        }
        mCarsImpl = new CarsListImpl();
        mCarsImpl.setListInterface(listInterface);

        if(mList!=null && mList.size()>0){
            mAlertText.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter = new CarsAdapter(getApplicationContext(), mList,mCarsImpl);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(onItemClickListener);
//            Log.i("temolist数据3",mList2+"=========================");
        }else{
            mAlertText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

        if(mList!=null && mList.size()>0) {
            Bitmap bitmap2 = ((BitmapDrawable) mCarImage.getDrawable()).getBitmap();
            mCarImage.setImageBitmap(Constants.createCarsBitmap(bitmap2, mList.size(), getApplicationContext()));
            getAllPrice();
        }
    }

    private void init(){
        mCarImage = (ImageView) findViewById(R.id.new_car_menu_image);
        mListView = (ListView) findViewById(R.id.new_cars_list);
        mMainLayout = (LinearLayout) findViewById(R.id.new_main_home_menu_layout);
        mBuyLayout = (LinearLayout) findViewById(R.id.new_main_buy_menu_layout);
        mCarLayout = (LinearLayout) findViewById(R.id.new_my_buy_car);
        mMyLayout = (LinearLayout) findViewById(R.id.new_main_my_menu_layout);
        mBottomText = (TextView) findViewById(R.id.bottom_cars_text);
        mBottomView = findViewById(R.id.new_main_main_view3);
        mBottomImage = (ImageView) findViewById(R.id.new_car_menu_image);
        mBottomText.setTextColor(getResources().getColor(R.color.white));
//        mBottomView.setVisibility(View.VISIBLE);
        mBottomImage.setImageDrawable(getResources().getDrawable(R.drawable.shopping_car_hover));
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mCarLayout.setBackgroundColor(getResources().getColor(R.color.main_color13));
        mSsbText = (TextView) findViewById(R.id.cars_ssb_text2);
        mSsjbText = (TextView) findViewById(R.id.cars_ssjb_text2);
        mXjText = (TextView) findViewById(R.id.cars_money_text2);
        mAlertText = (TextView) findViewById(R.id.cars_alert_text);
        mClearText = (TextView) findViewById(R.id.car_titel_clear);
        mPriceLayout = (RelativeLayout) findViewById(R.id.cars_all_layout);

        mClearText.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mMainLayout.setOnClickListener(click);
        mBuyLayout.setOnClickListener(click);
        mCarLayout.setOnClickListener(click);
        mMyLayout.setOnClickListener(click);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("flag", "1");
            if(mList.get(position).get("ProductId")!=null){
                bundle.putString("Id", mList.get(position).get("ProductId")
                        .toString());
            }
            if(mList.get(position).get("ProductName")!=null){
                bundle.putString("Title", mList.get(position).get("ProductName")
                        .toString());
            }
            if(mList.get(position).get("Price")!=null){
                bundle.putString("Price", mList.get(position).get("Price")
                        .toString());
            }
            if(mList.get(position).get("Yf")!=null){
                bundle.putString("Yf", mList.get(position).get("Yf")
                        .toString());
            }
            if(mList.get(position).get("Dhnumber")!=null){
                bundle.putString("Dhnumber",
                        mList.get(position).get("Dhnumber").toString());
            }
            if(mList.get(position).get("Image")!=null){
                bundle.putString("Image1", mList.get(position).get("Image")
                        .toString());
            }
            if (mList.get(position).get("titleImage1") != null) {
                bundle.putString("titleImage1", mList.get(position)
                        .get("titleImage1").toString());
            } else {
                bundle.putString("titleImage1", "");
            }
            if (mList.get(position).get("titleImage1") != null) {
                bundle.putString("titleImage2", mList.get(position)
                        .get("titleImage1").toString());
            } else {
                bundle.putString("titleImage2", "");
            }
            if (mList.get(position).get("titleImage1") != null) {
                bundle.putString("titleImage3", mList.get(position)
                        .get("titleImage1").toString());
            } else {
                bundle.putString("titleImage3", "");
            }
            if(mList.get(position).get("Salenumber")!=null){
                bundle.putString("sale",
                        mList.get(position).get("Salenumber").toString());
            }
            if(mList.get(position).get("Money")!=null){
                bundle.putString("money", mList.get(position).get("Money")
                        .toString());
            }
            bundle.putString("typeName", mList.get(position).get("typeName").toString());
            if(mList.get(position).get("ProductId")!=null){
                bundle.putString("giftId", mList.get(position).get("ProductId")
                        .toString());
            }
            if (mList.get(position).get("Content") != null) {
                bundle.putString("Content",
                        mList.get(position).get("Content").toString());
            }
            intent.putExtras(bundle);
            intent.setClass(getApplicationContext(), MallDetailActivity.class);
            startActivity(intent);
        }
    };

    private void getAllPrice() {
        mList2 = new ArrayList<>();
        for(int i=0;i<mList.size();i++){
            if(mList.get(i).get("isTrue").equals("true")){
                mList2.add(mList.get(i));
            }
        }
//        mList3 = new ArrayList<>();
//        mList3 = mList2;
        StringBuilder sb = new StringBuilder();
        sb.append("&userId=" + Constants.mId + "&useCurrency=true" + "&products=" + mList2.toString() );

        CarPrice carPrice = new CarPrice();
        carPrice.setUserId(Constants.mId);
        carPrice.setUseCurrency("true");
        carPrice.setProducts(mList2);

        String a= JSONObject.toJSON(carPrice).toString();
        Log.i("表单数据",a+"============================");
        RequestParams params = new RequestParams("UTF-8");
        try {
            params.setBodyEntity(new StringEntity(a,"UTF-8"));
            params.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XutilsUtils.post(Constants.getAllPrice,sb,params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("费用计算", res.result
                                + "========onSuccess==============");
                        mTempMap = Constants.getJsonObject(res.result);
                        if(mTempMap!=null && mTempMap.get("Code")!=null){
                            if(mTempMap.get("Code").toString().equals("200") ) {
                                isContiun = true;
                                mPriceMap = Constants.getJsonObjectByData(res.result);
                                handler.sendEmptyMessageDelayed(1, 0);
                            }else{
                                isContiun = false;
                                handler.sendEmptyMessageDelayed(2, 0);
                            }
                        }

                    }
                });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(mPriceMap.get("SSBCost")!=null) {
                        mSsbText.setText(mPriceMap.get("SSBCost") + "");
                    }
                    if(mPriceMap.get("GoldCost")!=null) {
                        mSsjbText.setText(mPriceMap.get("GoldCost") + "");
                    }
                    if(mPriceMap.get("CashCost")!=null) {
                        mXjText.setText("￥"+mPriceMap.get("CashCost") + "");
                    }
                    mNextBtn.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    if(mTempMap!=null && mTempMap.size()>0) {
                        Toast.makeText(getApplicationContext(), mTempMap.get("Message") + "", Toast.LENGTH_SHORT).show();
                    }
                    mSsbText.setText("");
                    mSsjbText.setText("");
                    mXjText.setText("");
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (v.getId()){
            //清空购物车
            case R.id.car_titel_clear:
                sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                mList.clear();
                mSsbText.setText("");
                mSsjbText.setText("");
                mXjText.setText("");
//                getAllPrice();
                mListView.setVisibility(View.GONE);
                mAlertText.setVisibility(View.VISIBLE);
                break;
            case R.id.next_btn:
                bundle = new Bundle();
                if(Constants.onLine==1 && !Constants.mId.equals("0")) {
                    if(Constants.mCarsMap!=null && Constants.mCarsMap.size()>0) {
                        if (isContiun) {
                            intent.setClass(getApplicationContext(), NewMallCreateOrderActivity.class);
                            if (mPriceMap != null) {
                                intent.putExtra("list", mList2);
                                intent.putExtra("useCurrency", mPriceMap.get("CanUseCurrency") + "");
                                intent.putExtra("flag", "1");
                            }
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            if (mTempMap != null && mTempMap.get("Message") != null) {
                                Toast.makeText(getApplicationContext(), mTempMap.get("Message") + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "购物车还是空的", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    intent.setClass(getApplicationContext(), MyUserLogActivity.class);
                    bundle = new Bundle();
                    bundle.putString("flag","cars");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(mFlag.equals("car")){
                    finish();
                }else{
                    exit();
                }

                break;
            default:
                break;
        }
        return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            mToast.show("再按一次退出程序", 1);
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            System.gc();
            ExitApplication.getInstance().exit();
        }
    }
    private CarsListInterface listInterface = new CarsListInterface() {
        @Override
        public void notifyList(HashMap<String, Object> map,String key,String status,int i,ArrayList<HashMap<String,Object>> list) {
            if(status.equals("remove")){
                mList.remove(i);
//                Log.i("数据1","==========remove1===========");
                Constants.mCarsMap.remove(key);
            }else if(status.equals("change")){
                mList.get(i).put("quantity",key);
                Constants.mCarsMap.put(map.get("AttrId").toString(),map);
            }else if(status.equals("check+")){
//                Log.i("增加的数据",mList.get(i)+"====================");
//                mList.add(mList.get(i));
                mList.get(i).put("isTrue","true");
//                Constants.mCarsMap.get(map.get("AttrId").toString()).put("isCheck","true");
            }else if(status.equals("check-")){
//                mList2.remove(i);
                mList.get(i).put("isTrue","false");
            }
            Log.i("数据2",mList+"==========mlist===========");
            if(mList!=null && mList.size()<=0){
                mListView.setVisibility(View.GONE);
                mAlertText.setVisibility(View.VISIBLE);

                    mSsbText.setText("");
                    mSsjbText.setText("");
                    mXjText.setText("");
            }
            SharedPreferences sp = getApplication().getSharedPreferences("cars", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("cars", com.alibaba.fastjson.JSONObject.toJSON(Constants.mCarsMap).toString());
            editor.commit();
            if(mList!=null && mList.size()>0){
                getAllPrice();
            }

        }
        @Override
        public void alertPop(String addressId) {}
        @Override
        public void startAppkf(String str) {}
    };
}
