package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.MallListGridAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/15.
 * 现金商城特惠/5折/天天特价
 */
public class TtActivity extends AppActivity implements View.OnClickListener {

    private TextView mTitleText;
    private ArrayList<HashMap<String, Object>> mMallList, mAddList;
    private int page = 1;
    private ImageView mBackImage;
    private MallListGridAdapter mGridAdapter;
    private GridView mGridView;
    private int lastVisibleIndex;
    private int x = 1;
    private int maxNum = 0;
    private boolean isScroll = true;
    private TextView mAlertText;
    private Bundle mBundle;
    private String mType="";
    private String mProportion;
    private String mTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_tejia_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mBundle = getIntent().getExtras();

        init();
        if(mBundle!=null){
            mTypeName = mBundle.getString("typeName");
            mType = mBundle.getString("type");
            if(mBundle.getString("attr")!=null){
                mProportion = mBundle.getString("attr");
            }
            if(mType.equals("xj_tt")){
                mTitleText.setText("天天特价");
            }else if(mType.equals("xj_left")){
                mTitleText.setText("五折畅享");
            }else if(mType.equals("xj_right")){
                mTitleText.setText("超值特惠");
            }else if(mType.equals("jb_tj")){
                mTitleText.setText("特价专区");
            }else if(mType.equals("jb_dp")){
                mTitleText.setText("大牌尝鲜");
            }else if(mType.equals("jb_pz")){
                mTitleText.setText("品质生活");
            }
            getMallList();
        }

    }

    private void init() {
        mTitleText = (TextView) findViewById(R.id.add_bank_info_text);


        mBackImage = (ImageView) findViewById(R.id.mall_back_info_image);
        mGridView = (GridView) findViewById(R.id.tt_tj_grid);
        mAlertText = (TextView) findViewById(R.id.tt_alert_text);

        mBackImage.setOnClickListener(this);
    }

    //商品列表
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        if(mType.equals("xj_tt")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&isGiftBag=true");
        }else if(mType.equals("xj_left")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&isHalfPrice=true");
        }else if(mType.equals("xj_right")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&buyOneGive=2");
        }else if(mType.equals("jb_tj")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=2" + "&isSpecPrice=true"+"&shmoneyBuyRatio="+mProportion);
        }else if(mType.equals("jb_dp")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=2" + "&shmoneyBuyRatio="+mProportion+"&orderBy=1");
        }else if(mType.equals("jb_pz")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=2" + "&shmoneyBuyRatio="+mProportion+"&orderBy=5");
        }
        XutilsUtils.get(Constants.getShopList, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("商品列表", res.result + "=================");
                        try {
                            if (x == 1) {
                                mMallList = Constants.getJsonArrayByData(res.result);
                                handler.sendEmptyMessageDelayed(1, 0);
                            } else if (x == 2) {
                                mAddList = Constants.getJsonArrayByData(res.result);
                                handler.sendEmptyMessageDelayed(2, 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mMallList != null && mMallList.size() > 0) {
                        mGridAdapter = new MallListGridAdapter(getApplicationContext(),
                                mMallList,mTypeName);
                        mGridView.setAdapter(mGridAdapter);
                        mGridView.setOnScrollListener(mOnScrollListener3);
                        isScroll = true;
                        mAlertText.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    } else {
                        maxNum = 0;
                        mAlertText.setVisibility(View.VISIBLE);
                        mGridView.setVisibility(View.GONE);
                    }
                    break;
                case 2:

                    maxNum = mGridAdapter.getCount() - 1;
                    mGridAdapter.notify(mAddList);
                    isScroll = true;
                    break;
            }
        }
    };



    // 搜索商品滑动监听
    AbsListView.OnScrollListener mOnScrollListener3 = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (!isScroll)
                return;
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && lastVisibleIndex == mGridAdapter.getCount() - 1 && maxNum < mGridAdapter.getCount() - 1) {
                Log.i("滑动","=========2======");
                x = 2;
                page = page + 1;
                getMallList();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mall_back_info_image:
                finish();
                break;
        }
    }
}
