package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.MyGoldAdapter;
import com.zjxfood.adapter.PopCommListTypeAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.GoldPopup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/1.
 * 我的金币
 */
public class MyGoldActivity extends AppActivity implements View.OnClickListener{
    private TextView mTitleText;
    private ImageView mBackImage;
    private MyGoldAdapter mAdapter;
    private ListView mListView;
    private int page = 1;
    private int x = 1;
    private ArrayList<HashMap<String, Object>> mList,mAddList;
    private HashMap<String,Object> mMaps,mGoldMaps;
    private int lastVisibleIndex;
    private String mOperator  = "+";
    private RelativeLayout mTypeLayout;
    private PopupWindow mTypePopWindow;
    private ListView mTypeList;
    private String[] mTypeArrays;
    private PopCommListTypeAdapter mPopCommListTypeAdapter;
    private Spinner mSpinner;
    private Button mCzBtn,mDhBtn;
    private TextView mKyPrice,mAllSrPrice,mZcPrice;
    private TextView mAlertText;
    private TextView mTypeText;
    private GoldPopup mPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_gold_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();

        getLists();
        getAllSource();
    }

    private void init(){
        mKyPrice = (TextView) findViewById(R.id.currency_glod_keyong_show_text);
        mAllSrPrice = (TextView) findViewById(R.id.currency_glod_left_show_text);
        mZcPrice = (TextView) findViewById(R.id.currency_glod_right_show_text);

        mTitleText = (TextView) findViewById(R.id.restaurant_bi_info_text);
        mTitleText.setText("我的金币");
        mBackImage = (ImageView) findViewById(R.id.currency_bi_back_info_image);
        mBackImage.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.my_gold_list);
        mSpinner = (Spinner) findViewById(R.id.gold_spinner);
        mCzBtn = (Button) findViewById(R.id.cz_btn);
        mAlertText = (TextView) findViewById(R.id.gold_alert_text);
        mTypeText = (TextView) findViewById(R.id.gold_type_text);
        mDhBtn = (Button) findViewById(R.id.qdh_btn);

        mTypeLayout = (RelativeLayout) findViewById(R.id.my_gold_type_layout);

        mTypeLayout.setOnClickListener(this);
        mCzBtn.setVisibility(View.GONE);
        mDhBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        Intent intent = new Intent();
        switch (v.getId()){

            case R.id.currency_bi_back_info_image:
                finish();
                break;
            //类型选择
            case R.id.my_gold_type_layout:
                mPopup = new GoldPopup(this,onItemClickListener,mTypeLayout.getWidth());
                mPopup.showAsDropDown(mTypeLayout);
                break;
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            dissPop();
            switch (i){
                case 1:
                    mOperator = "+";
                    x = 1;
                    page = 1;
                    getLists();
                    mTypeText.setText("收入");
                    break;
                case 2:
                    mOperator = "-";
                    x = 1;
                    page = 1;
                    getLists();
                    mTypeText.setText("支出");
                    break;
                default:
                    mTypeText.setText("所有");
                    break;
            }
        }
    };
    private void dissPop(){
        if(mPopup!=null && mPopup.isShowing()){
            mPopup.dismiss();
        }
    }

    private void getLists() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId="+Constants.mId + "&currencyType="+"3"+"&operator="+mOperator+"&page="+page+"&pagesize=10");
        XutilsUtils.get(Constants.getGoldMx, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("msg",res.result+"========================");
                        mMaps = Constants.getJsonObjectByData(res.result);
                        if(x==1){
                            if(mMaps!=null && mMaps.size()>0){
                                mList = Constants.getJsonArray(mMaps.get("rows").toString());
                            }
                            Log.i("mList",mList+"=========1===============");
                            handler.sendEmptyMessageDelayed(1, 0);
                        }else if(x==2){
                            mAddList =  Constants.getJsonArray(mMaps.get("rows").toString());
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }
    private void getAllSource() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId="+Constants.mId+"&currencyType=3");
        XutilsUtils.get(Constants.getGoldShmony, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("msg2",res.result+"==============================");
                        mGoldMaps = Constants.getJsonObjectByData(res.result);
                        if(mGoldMaps!=null && mGoldMaps.size()>0){
                            handler.sendEmptyMessageDelayed(3,0);
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
                    if(mList!=null && mList.size()>0){
                        mAdapter = new MyGoldAdapter(getApplicationContext(),mList);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnScrollListener(mScrollListener);
                        mAlertText.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }else{
                        Log.i("visible","=================");
                        mAlertText.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if(mAddList!=null && mAddList.size()>0){
                        mAdapter.notify(mAddList);
                    }
                    break;
                case 3:

                    if(mGoldMaps.get("AvailableBalance")!=null) {
                        mKyPrice.setText(mGoldMaps.get("AvailableBalance").toString());
                    }
                    if(mGoldMaps.get("AllIncome")!=null) {
                        mAllSrPrice.setText(mGoldMaps.get("AllIncome").toString());
                    }
                    if(mGoldMaps.get("AllCost")!=null) {
                        mZcPrice.setText(mGoldMaps.get("AllCost").toString());
                    }
                    break;
            }
        }
    };

    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && lastVisibleIndex == mAdapter.getCount()-1) {
                x = 2;
                page = page+1;
                getLists();
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 计算最后可见条目的索引
            lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        }
    };
}
