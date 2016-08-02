package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.NewCashMallAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.NewCashListPopup;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/15.
 * 现金商城
 */
public class NewCashListActivity extends AppActivity implements View.OnClickListener {

    private TextView mTitleText;
    private ArrayList<HashMap<String, Object>> mMallList, mAddList;
    private int page = 1;
    private ImageView mBackImage;
    private NewCashMallAdapter mGridAdapter;
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
    private RelativeLayout mQbLayout,mPxLayout;
    private NewCashListPopup mCashPopup;
//    private ArrayList<String> mList;
    private String[] mList;
    private ImageView mQbImage,mPxImage;
    private TextView mQbText,mPxText;
    private View mQbView,mPxView;
    private static int mPosition = 0;
    private String mGroup = "";
    private int mOrderBy = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_xj_list_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mBundle = getIntent().getExtras();
        mPosition = 0;
        init();
        mList = getResources().getStringArray(R.array.mallsort);
        if(mBundle!=null){
            mTypeName = mBundle.getString("typeName");
            mType = mBundle.getString("type");
            if(mBundle.getString("attr")!=null){
                mProportion = mBundle.getString("attr");
            }
            if(mBundle.getString("group")!=null){
                mGroup = mBundle.getString("group");
            }
            if(mType.equals("xj_tt")){
                mTitleText.setText("天天特价");
            }else if(mType.equals("xj_left")){
                mTitleText.setText("五折畅享");
            }else if(mType.equals("xj_right")){
                mTitleText.setText("超值特惠");
            }else if(mType.equals("xj")){
                mTitleText.setText("现金商城");
            }else if(mType.equals("xj_type")){
                mTitleText.setText(mBundle.get("title").toString());
            }else if(mType.equals("ssjb")){
                mTitleText.setText("金币商城");
            }
            getMallList();
        }
    }

    private void init() {
        mTitleText = (TextView) findViewById(R.id.add_bank_info_text);
        mQbLayout = (RelativeLayout) findViewById(R.id.cash_qb_layout);
        mPxLayout = (RelativeLayout) findViewById(R.id.cash_px_layout);

        mBackImage = (ImageView) findViewById(R.id.mall_back_info_image);
        mGridView = (GridView) findViewById(R.id.tt_tj_grid);
        mAlertText = (TextView) findViewById(R.id.tt_alert_text);
        mQbImage = (ImageView) findViewById(R.id.new_cash_quanbu_image);
        mQbText = (TextView) findViewById(R.id.new_cash_quanbu_text);
        mQbView = findViewById(R.id.new_cash_quanbu_view);
        mPxImage = (ImageView) findViewById(R.id.new_cash_px_image);
        mPxText = (TextView) findViewById(R.id.new_cash_px_text);
        mPxView = findViewById(R.id.new_cash_px_view);

        mBackImage.setOnClickListener(this);
        mQbLayout.setOnClickListener(this);
        mPxLayout.setOnClickListener(this);
    }

    //商品列表
    private void getMallList() {
        StringBuilder sb = new StringBuilder();
        if(mType.equals("xj_tt")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&isGiftBag=true"+"&orderBy="+mOrderBy);
        }else if(mType.equals("xj_left")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&isHalfPrice=true"+"&orderBy="+mOrderBy);
        }else if(mType.equals("xj_right")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3" + "&buyOneGive=2"+"&orderBy="+mOrderBy);
        }else if(mType.equals("xj_type")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3"+"&group="+mGroup+"&orderBy="+mOrderBy);
        }else if(mType.equals("xj")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=3"+"&group="+mGroup+"&orderBy="+mOrderBy);
        }else if(mType.equals("ssjb")){
            sb.append("pageSize=10" + "&page=" + page + "&shopCode=2"+"&group="+mGroup+"&buyLevel=3"+"&orderBy="+mOrderBy);
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
                        mGridAdapter = new NewCashMallAdapter(getApplicationContext(),
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
            //全部
            case R.id.cash_qb_layout:
                mQbImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_menu_hover));
                mQbText.setTextColor(getResources().getColor(R.color.main_color16));
                mQbView.setVisibility(View.VISIBLE);
                mPxImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_order_normal));
                mPxText.setTextColor(getResources().getColor(R.color.main_color3));
                mPxView.setVisibility(View.GONE);

                mOrderBy = 1;
                mPosition = 0;
                getMallList();
                break;
            //排序
            case R.id.cash_px_layout:
                mQbImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_menu_normal));
                mQbText.setTextColor(getResources().getColor(R.color.main_color3));
                mQbView.setVisibility(View.GONE);
                mPxImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_order_hover));
                mPxText.setTextColor(getResources().getColor(R.color.main_color16));
                mPxView.setVisibility(View.VISIBLE);
                mCashPopup = new NewCashListPopup(NewCashListActivity.this,mPosition,mList,onItemClickListener);
                mCashPopup.showAsDropDown(mQbLayout);
                break;
        }
    }
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mPosition = i;
            if(mCashPopup!=null && mCashPopup.isShowing()){
                mCashPopup.dismiss();
            }
            mOrderBy = i+1;
            getMallList();
//			mAdapter.selectPosition(mPosition);
        }
    };
}
