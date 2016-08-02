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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.SearchUserAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.date.SlideDateTimeListener;
import com.zjxfood.date.SlideDateTimePicker;
import com.zjxfood.http.XutilsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class SearchUserActivity extends FragmentActivity implements View.OnClickListener{

    private RelativeLayout mTitleLayout;
    private TextView mTitleText;
    private TextView mStartText,mEedText;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String flag = "";
    private Bundle mBundle;
    private Spinner mSpinner;
    private String[] mTypeArrays;
    private Button mCxBtn;
    private int x = 1;
    private int page = 1;
    private int mReturnDeep=0;
    private SearchUserAdapter mAdapter;
    private HashMap<String,Object> mMaps;
    private ArrayList<HashMap<String,Object>> mLists,mAddLists;
    private ListView mListView;
    private int lastVisibleIndex;
    private TextView mAlertText;
    private String mIsJh;
    private ImageView mBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_users_search_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mIsJh = mBundle.getString("title");
            if(mIsJh.equals("true")){
                mTitleText.setText("我的会员查询");
            }else{
                mTitleText.setText("我的游客查询");
            }
        }
        mTypeArrays = getResources().getStringArray(R.array.users);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mTypeArrays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(mSelectListener);
    }

    private void init(){
        mTitleText = (TextView)findViewById(R.id.title_text);
        mSpinner = (Spinner) findViewById(R.id.users_spinner);
        mCxBtn = (Button) findViewById(R.id.users_search_btn);
        mTitleText.setText("我的会员查询");
        mStartText = (TextView) findViewById(R.id.user_search_start_date);
        mEedText = (TextView) findViewById(R.id.user_search_end_date);
        mListView = (ListView) findViewById(R.id.users_search_list);
        mAlertText = (TextView) findViewById(R.id.users_search_alert);
        mBackImage = (ImageView) findViewById(R.id.title_back_image);

        mStartText.setOnClickListener(this);
        mEedText.setOnClickListener(this);
        mCxBtn.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.title_back_image:
                finish();
                break;
            //查询
            case R.id.users_search_btn:
                getLists();
                break;
            case R.id.user_search_start_date:
                flag = "start";
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
                break;
            case R.id.user_search_end_date:
                flag = "end";
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
                break;
        }
    }

    private void getLists() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserId="+ Constants.mId + "&BeginTime="+mStartText.getText()+"&EndTime="+mEedText.getText()+"&IsJh="+mIsJh+"&page="+page+"&pagesize=10"+"&ReturnDeep="+mReturnDeep);
        XutilsUtils.get(Constants.getMyUsersubordinate, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("msg",res.result+"========================");
                       mMaps = Constants.getJsonObjectByData(res.result);
                        if(mMaps!=null && mMaps.get("rows")!=null){
                            if(x==1){
                                mLists = Constants.getJsonArray(mMaps.get("rows").toString());
                                Log.i("mLists1",mLists+"========================");
                                handler.sendEmptyMessageDelayed(1,0);
                            }else if(x==2){
                                mAddLists = Constants.getJsonArray(mMaps.get("rows").toString());
                                Log.i("mLists2",mLists+"========================");
                                handler.sendEmptyMessageDelayed(2,0);
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
                    if (mLists != null && mLists.size() > 0) {
                        mAdapter = new SearchUserAdapter(
                                getApplicationContext(), mLists);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnScrollListener(mScrollListener);
                        mAlertText.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }else{
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
                    && lastVisibleIndex == mAdapter.getCount() - 1) {
                x = 2;
                page = page + 1;
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

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            if(flag.equals("start")){
                mStartText.setText(mFormatter.format(date));
            }else if(flag.equals("end")){
                mEedText.setText(mFormatter.format(date));
            }
        }
        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
        }
    };

    AdapterView.OnItemSelectedListener mSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i("msg","==========================="+i);
            switch (i){
                case 0:
                    x = 1;
                    page = 1;
                    mReturnDeep = 0;
                    getLists();
                    break;
                case 1:
                    x = 1;
                    page = 1;
                    mReturnDeep = 1;
                    getLists();
                    break;
                case 2:
                    x = 1;
                    page = 1;
                    mReturnDeep = 2;
                    getLists();
                    break;
                case 3:
                    x = 1;
                    page = 1;
                    mReturnDeep = 3;
                    getLists();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
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
