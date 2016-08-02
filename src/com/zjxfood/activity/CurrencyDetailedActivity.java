package com.zjxfood.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.CurrencyDetailAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.date.SlideDateTimeListener;
import com.zjxfood.date.SlideDateTimePicker;
import com.zjxfood.http.XutilsUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/7.
 */
public class CurrencyDetailedActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout mTitleLayout;
    private TextView mTitleText;
    private Bundle mBundle;
    private TextView mStartTime, mEndTime;
    private Button mBtn;
    private ImageView mBackImage;
    private String flag = "start";
    private CurrencyDetailAdapter mAdapter;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String mChangeType, mOperator;
    private int page = 1;
    private TextView mAlertText;
    private HashMap<String, Object> mMaps, mMaps2,mDetailMap;
    private ArrayList<HashMap<String, Object>> mLists,mAddLists;
    private ListView mListView;
    private TextView mHjText;
    private int x = 1;
    private int lastVisibleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_detailed);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        mEndTime.setText(time);
        mBundle = getIntent().getExtras();
        Log.i("标题名字",mBundle.getString("name")+"=============");
        if (mBundle != null) {
            if(mBundle.getString("name")!=null) {
                mTitleText.setText(mBundle.getString("name").toString());
            }
            mChangeType = mBundle.getString("ChangeType");
            mOperator = mBundle.getString("Operator");
        }
        getAllDetail();
        getDetail();
    }

    private void init() {
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("我的余额");
        mStartTime = (TextView) findViewById(R.id.currcy_search_start_date);
        mEndTime = (TextView) findViewById(R.id.currcy_search_end_date);

        mBtn = (Button) findViewById(R.id.currency_search_btn);
        mAlertText = (TextView) findViewById(R.id.currency_alert);
        mListView = (ListView) findViewById(R.id.currency_detail_list);
        mHjText = (TextView) findViewById(R.id.currency_detail_hj);

        mBackImage.setOnClickListener(this);
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.title_back_image:
                finish();
                break;
            //查询
            case R.id.currency_search_btn:
                if (!mStartTime.getText().toString().equals("") && !mEndTime.getText().toString().equals("")) {
                    getDetail();
                    getAllDetail();
                    x= 1;
                    page = 1;
                } else {
                    Toast.makeText(getApplicationContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            //开始日期
            case R.id.currcy_search_start_date:
                flag = "start";
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
                break;
            case R.id.currcy_search_end_date:
                flag = "end";
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
                break;
        }
    }

    private void getDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId=" + Constants.mId + "&currencyType=" + "1" + "&changeType=" + mChangeType + "&beginTime=" + mStartTime.getText() + "&endTime=" + mEndTime.getText() + "&page=" + page + "&pagesize=10");
        XutilsUtils.get(Constants.getGoldMx, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mMaps = Constants.getJsonObject(res.result);

                        if (mMaps != null && mMaps.get("Code") != null) {
                            if (mMaps.get("Code").toString().equals("200")) {
                                mMaps2 = Constants.getJsonObject(mMaps.get("Data").toString());
                                if (mMaps2 != null && mMaps2.get("rows") != null) {
                                    if(x==1){
                                        mLists = Constants.getJsonArray(mMaps2.get("rows").toString());
                                        handler.sendEmptyMessageDelayed(1, 0);
                                    }else if(x==2){
                                        mAddLists = Constants.getJsonArray(mMaps2.get("rows").toString());
                                        handler.sendEmptyMessageDelayed(2, 0);
                                    }
                                }
                            } else {
                                if (mMaps.get("Message") != null) {
                                    Toast.makeText(getApplicationContext(), mMaps.get("Message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        Log.i("detail", res.result + "========================");
                    }
                });
    }
    //获取总额
    private void getAllDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId="+Constants.mId+"&currencyType=" + "1" + "&changeType=" + mChangeType + "&beginTime=" + mStartTime.getText() + "&endTime=" + mEndTime.getText() + "page=" + page + "&pagesize=10");
        XutilsUtils.get(Constants.getAllDetail, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mDetailMap = Constants.getJsonObject(res.result);
                        if(mDetailMap!=null && mDetailMap.get("Code")!=null){
                            if(mDetailMap.get("Code").toString().equals("200")){
                                mHjText.setText("￥"+mDetailMap.get("Data"));
                            }else{
                                if(mDetailMap.get("Message")!=null){
                                    Toast.makeText(getApplicationContext(),mDetailMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        Log.i("detail", res.result + "========================");

                    }
                });
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            if (flag.equals("start")) {
                mStartTime.setText(mFormatter.format(date));
            } else if (flag.equals("end")) {
                mEndTime.setText(mFormatter.format(date));
            }
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mLists != null && mLists.size() > 0) {
                        mAdapter = new CurrencyDetailAdapter(getApplicationContext(), mLists);
                        mAlertText.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnScrollListener(mScrollListener);
                    } else {
                        mAlertText.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if(mAddLists!=null && mAddLists.size()>0){
                        mAdapter.notify(mAddLists);
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
                getDetail();
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 计算最后可见条目的索引
            lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        }
    };
    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
