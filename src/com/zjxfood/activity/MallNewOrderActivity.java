package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.MallNewOrderAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MallNewOrderActivity extends AppActivity implements View.OnClickListener{
    private ListView mListView;
    private MallNewOrderAdapter mAdapter;
    private ImageView mBackImage;
    private ArrayList<HashMap<String, Object>> mList,mAddlist;
    private int x = 1;
    private int page = 1;
    private int lastVisibleIndex;
    private TextView mTitleText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_new_order);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        httpRun();
    }

    private void init(){
        mListView = (ListView) findViewById(R.id.mall_new_order_list);
//        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("商城订单");
        mBackImage.setOnClickListener(this);
    }

    private void httpRun() {
        StringBuilder sb = new StringBuilder();
        sb.append("userid=" + Constants.mId+"&page="+page+"&pagesize=10");

        XutilsUtils.get(Constants.getMallOrder2, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("mmmmm",res.result+"=================");
                        if(x==1){
                            mList = Constants.getJsonArray(res.result);
                            Log.i("mList",mList+"=================");
                            handler.sendEmptyMessageDelayed(1, 0);
                        }else if(x==2){
                            mAddlist = Constants.getJsonArray(res.result);
                            handler.sendEmptyMessageDelayed(2, 0);
                        }
                    }
                });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mAdapter = new MallNewOrderAdapter(getApplicationContext(), mList);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnScrollListener(mScrollListener);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && lastVisibleIndex == mAdapter.getCount() - 1) {
                x = 2;
                page = page+1;
                httpRun();
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 计算最后可见条目的索引
            lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back_image:
                finish();
                break;
        }
    }
}
