package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.CurrcyAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/1.
 * 我的余额
 */
public class MyNewCurrencyActivity extends AppActivity implements View.OnClickListener{

    private ArrayList<HashMap<String,Object>> mLists,mListsZc;
    private HashMap<String,Object> mMaps;
    private ImageView mBackImage;
    private ListView mListViewSr,mListViewZc;
    private CurrcyAdapter mAdapter;
    private TextView mSrText,mZcText,mKeText,mIntoText;
    private Button mBalanceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_new_currency_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        ExitApplication.getInstance().addActivity(this);
        init();
        getCurrency();
    }

    private void init(){
        mBackImage = (ImageView) findViewById(R.id.currency_bi_back_info_image);
        mListViewSr = (ListView) findViewById(R.id.currency_list_sr);
        mListViewZc = (ListView) findViewById(R.id.currency_list_zc);
        mSrText = (TextView) findViewById(R.id.gross_income_show_text);
        mZcText = (TextView) findViewById(R.id.gross_out_show_text);
        mKeText = (TextView) findViewById(R.id.currency_keyong_yue_show_text);
        mIntoText = (TextView) findViewById(R.id.currency_yue_show_text);
        mBalanceBtn = (Button) findViewById(R.id.cz_btn);

        mBackImage.setOnClickListener(this);
        mBalanceBtn.setOnClickListener(this);
    }
    private void getCurrency() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId=" + Constants.mId + "&hasitem=true");
        XutilsUtils.get(Constants.getMyCurrency, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mMaps = Constants.getJsonObjectByData(res.result);
                        if(mMaps!=null && mMaps.size()>0){
                            if(mMaps.get("BalanceItems")!=null){
                                mLists = Constants.getJsonArray(mMaps.get("SumBalanceItems").toString());
                            }
                            if(mMaps.get("SubBalanceItems")!=null){
                                mListsZc = Constants.getJsonArray(mMaps.get("SubBalanceItems").toString());
                            }
                            handler.sendEmptyMessageDelayed(1,0);
                        }
                        Log.i("mCurrencyMap",mMaps+"================"+mLists);
                    }
                });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(mMaps.get("AllIncome")!=null){
                        mSrText.setText(mMaps.get("AllIncome").toString());
                    }
                    if(mMaps.get("AllCost")!=null) {
                        mZcText.setText(mMaps.get("AllCost").toString());
                    }
                    if(mMaps.get("AvailableBalance")!=null) {
                        mKeText.setText(mMaps.get("AvailableBalance").toString());
                    }
                    if(mMaps.get("UnavailableBalance")!=null) {
                        mIntoText.setText(mMaps.get("UnavailableBalance").toString());
                    }
                    mAdapter = new CurrcyAdapter(getApplicationContext(),mLists);
                    mListViewSr.setAdapter(mAdapter);
                    mListViewSr.setOnItemClickListener(onItemClickListener);
                    mAdapter = new CurrcyAdapter(getApplicationContext(),mListsZc);
                    mListViewZc.setAdapter(mAdapter);
                    mListViewZc.setOnItemClickListener(onItemClickListener2);
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.currency_bi_back_info_image:
                finish();
                break;
            case R.id.cz_btn:
                intent.setClass(getApplicationContext(),BalanceWrActivity.class);
                startActivity(intent);
                break;
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),CurrencyDetailedActivity.class);
            Bundle bundle = new Bundle();
            if(mLists.get(i).get("DisplayName")!=null) {
                bundle.putString("name", mLists.get(i).get("DisplayName").toString());
            }
            if(mLists.get(i).get("ChangeType")!=null) {
                bundle.putString("ChangeType", mLists.get(i).get("ChangeType").toString());
            }
            if(mLists.get(i).get("Operator")!=null) {
                bundle.putString("Operator", mLists.get(i).get("Operator").toString());
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    AdapterView.OnItemClickListener onItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),CurrencyDetailedActivity.class);
            Bundle bundle = new Bundle();
            if(mListsZc.get(i).get("DisplayName")!=null) {
                bundle.putString("name", mListsZc.get(i).get("DisplayName").toString());
            }
            if(mListsZc.get(i).get("ChangeType")!=null) {
                bundle.putString("ChangeType", mListsZc.get(i).get("ChangeType").toString());
            }
            if(mListsZc.get(i).get("Operator")!=null) {
                    bundle.putString("Operator", mListsZc.get(i).get("Operator").toString());
                }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

}
