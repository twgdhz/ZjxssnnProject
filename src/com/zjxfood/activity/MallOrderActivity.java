package com.zjxfood.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.utils.KFLog;
import com.appkefu.smack.util.StringUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.NewMallOrderAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.CarsListImpl;
import com.zjxfood.interfaces.CarsListInterface;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class MallOrderActivity extends AppActivity implements OnClickListener {
    private ListView mListView;
//    private MallOrderListAdapter mOrderListAdapter;
    private ArrayList<HashMap<String, Object>> mList;
    private ImageView mBackImage;
    private int x = 1;
    //	private int size = 10;
    // 最后可见条目的索引
    private int lastVisibleIndex;
    //	private RunTask mRunTask;
    private int page = 1;
    private ArrayList<HashMap<String, Object>> mAddlist;
    private TextView mTitleText;
    private NewMallOrderAdapter mNewMallOrderAdapter;
    private HashMap<String, Object> mTempMap,mCancelMap;
    private LinearLayout mQbLayout, mDfhLayout, mDfkLayout, mYfhLayout, mYcsLayout;
    private TextView mQbText,mDfkText,mDfhText,mYfhText,mYcsText;
    private View mQbView,mDfkView,mDfhView,mYfhView,mYcsView;
    private TextView mAlertText;
    private String mOrderOperateStatus = "";
    private int maxNum = 0;
    private CarsListImpl mCarImpl;
    private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mall_order_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
//		mNewMallOrderAdapter = new NewMallOrderAdapter(getApplicationContext());
//		mListView.setAdapter(mNewMallOrderAdapter);
        getMallLists();
        mCarImpl = new CarsListImpl();
        mCarImpl.setListInterface(listInterface);
    }


    private void init() {
        mListView = (ListView) findViewById(R.id.new_mall_order_list);
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("商城订单");
        mBackImage.setOnClickListener(this);

        mQbLayout = (LinearLayout) findViewById(R.id.new_mall_ordera_qb);
        mDfhLayout = (LinearLayout) findViewById(R.id.new_mall_ordera_dfh);
        mDfkLayout = (LinearLayout) findViewById(R.id.new_mall_ordera_dfk);
        mYfhLayout = (LinearLayout) findViewById(R.id.new_mall_ordera_yfh);
        mYcsLayout = (LinearLayout) findViewById(R.id.new_mall_ordera_ycs);
        mQbText = (TextView) findViewById(R.id.new_mall_ordera_qb_text);
        mDfkText = (TextView) findViewById(R.id.new_mall_ordera_dfk_text);
        mDfhText = (TextView) findViewById(R.id.new_mall_ordera_dfh_text);
        mYfhText = (TextView) findViewById(R.id.new_mall_ordera_yfh_text);
        mYcsText = (TextView) findViewById(R.id.new_mall_ordera_ycs_text);
        mQbView = findViewById(R.id.new_mall_ordera_qb_view);
        mDfkView = findViewById(R.id.new_mall_ordera_dfk_view);
        mDfhView = findViewById(R.id.new_mall_ordera_dfh_view);
        mYfhView = findViewById(R.id.new_mall_ordera_yfh_view);
        mYcsView = findViewById(R.id.new_mall_ordera_ycs_view);
        mAlertText = (TextView) findViewById(R.id.alert_text);

        mQbLayout.setOnClickListener(this);
        mDfhLayout.setOnClickListener(this);
        mDfkLayout.setOnClickListener(this);
        mYfhLayout.setOnClickListener(this);
        mYcsLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_image:
                finish();
                break;
            //全部
            case R.id.new_mall_ordera_qb:
                changeView(mQbText,mQbView);
                x = 1;
                page = 1;
                maxNum = 0;
                mOrderOperateStatus = "";
                getMallLists();
                break;
            case R.id.new_mall_ordera_dfh:
                changeView(mDfhText,mDfhView);
                x = 1;
                page = 1;
                maxNum = 0;
                mOrderOperateStatus = "2";
                getMallLists2();
                break;
            case R.id.new_mall_ordera_dfk:
                changeView(mDfkText,mDfkView);
                x = 1;
                page = 1;
                maxNum = 0;
                mOrderOperateStatus = "1";
                getMallLists2();
                break;
            case R.id.new_mall_ordera_yfh:
                changeView(mYfhText,mYfhView);
                x = 1;
                page = 1;
                maxNum = 0;
                mOrderOperateStatus = "3";
                getMallLists2();
                break;
            case R.id.new_mall_ordera_ycs:
                changeView(mYcsText,mYcsView);
                x = 1;
                page = 1;
                maxNum = 0;
                mOrderOperateStatus = "4";
                getMallLists2();
                break;
        }
    }
    private void changeView(TextView textView,View view){
        mQbText.setTextColor(getResources().getColor(R.color.main_color3));
        mDfkText.setTextColor(getResources().getColor(R.color.main_color3));
        mDfhText.setTextColor(getResources().getColor(R.color.main_color3));
        mYfhText.setTextColor(getResources().getColor(R.color.main_color3));
        mYcsText.setTextColor(getResources().getColor(R.color.main_color3));
        mQbView.setVisibility(View.GONE);
        mDfkView.setVisibility(View.GONE);
        mDfhView.setVisibility(View.GONE);
        mYfhView.setVisibility(View.GONE);
        mYcsView.setVisibility(View.GONE);

        textView.setTextColor(getResources().getColor(R.color.main_color16));
        view.setVisibility(View.VISIBLE);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if(mList.size()>0) {
                        mNewMallOrderAdapter = new NewMallOrderAdapter(getApplicationContext(), mList,mCarImpl);
                        mListView.setAdapter(mNewMallOrderAdapter);
                        mListView.setOnScrollListener(mScrollListener);
                        mAlertText.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }else{
                        mAlertText.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    maxNum = mNewMallOrderAdapter.getCount() - 1;
                    mNewMallOrderAdapter.notify(mAddlist);

                    break;
                case 3:
                    if(mCancelMap!=null) {
                        mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
                                MallOrderActivity.this, mCancelMap.get("Message") + "");
                        mCommitmentPopupWindow.showAtLocation(mListView, Gravity.CENTER,
                                0, 0);
                    }
                    break;
            }
        }

        ;
    };

    private void getMallLists() {
        StringBuilder sb = new StringBuilder();
        sb.append("userid=" + Constants.mId + "&currentPage=" + page + "&pagesize=2");
        XutilsUtils.get(Constants.getNewMallOrder, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("商城订单", res.result + "================");
                        try {
                            if (x == 1) {
//							mList = Constants.getJsonArray(res.result);
                                mTempMap = Constants.getJsonObjectByData(res.result);
                                if (mTempMap != null && mTempMap.size() > 0 && mTempMap.get("rows") != null) {
                                    mList = Constants.getJsonArray(mTempMap.get("rows").toString());
                                    if(mList!=null) {
                                        handler.sendEmptyMessageDelayed(1, 0);
                                    }
                                }
                            } else if (x == 2) {
                                mTempMap = Constants.getJsonObjectByData(res.result);
                                if (mTempMap != null && mTempMap.size() > 0 && mTempMap.get("rows") != null) {
                                    mAddlist = Constants.getJsonArray(mTempMap.get("rows").toString());
                                    handler.sendEmptyMessageDelayed(2, 0);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void getMallLists2() {
        StringBuilder sb = new StringBuilder();
        sb.append("userid=" + Constants.mId + "&currentPage=" + page + "&pagesize=2"+"&status="+mOrderOperateStatus);
        XutilsUtils.get(Constants.getNewMallOrder, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("商城订单", res.result + "================");
                        try {
                            if (x == 1) {
//							mList = Constants.getJsonArray(res.result);
                                mTempMap = Constants.getJsonObjectByData(res.result);
                                if (mTempMap != null && mTempMap.size() > 0 && mTempMap.get("rows") != null) {
                                    mList = Constants.getJsonArray(mTempMap.get("rows").toString());
                                    if(mList!=null) {
                                        handler.sendEmptyMessageDelayed(1, 0);
                                    }
                                }
                            } else if (x == 2) {
                                mTempMap = Constants.getJsonObjectByData(res.result);
                                if (mTempMap != null && mTempMap.size() > 0 && mTempMap.get("rows") != null) {
                                    mAddlist = Constants.getJsonArray(mTempMap.get("rows").toString());
                                    handler.sendEmptyMessageDelayed(2, 0);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && lastVisibleIndex == mNewMallOrderAdapter.getCount() - 1 && maxNum < mNewMallOrderAdapter.getCount() - 1) {
                x = 2;
                page = page + 1;
                if(mOrderOperateStatus.equals("")){
                    getMallLists();
                }else{
                    getMallLists2();
                }

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
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        //监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
        //工作组在线状态
        intentFilter.addAction(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS);
        registerReceiver(mXmppreceiver, intentFilter);
    }

    @Override
    protected void onStop() {

        System.gc();
        super.onStop();
        unregisterReceiver(mXmppreceiver);
    }
    private CarsListInterface listInterface = new CarsListInterface() {
        @Override
        public void notifyList(HashMap<String, Object> map, String key, String status,int i,ArrayList<HashMap<String, Object>> list) {
            cancelOrder(key);
        }
        @Override
        public void alertPop(String addressId) {
            mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
                    MallOrderActivity.this,addressId);
            mCommitmentPopupWindow.showAtLocation(mListView, Gravity.CENTER,
                    0, 0);
        }
        @Override
        public void startAppkf(String str) {
            startChat(str);
        }
    };

    private void cancelOrder(String orderid) {
        StringBuilder sb = new StringBuilder();
        sb.append("userId=" + Constants.mId + "&orderId=" + orderid);
        XutilsUtils.get(Constants.cancelOrder, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("取消订单", res.result + "================");
                        mCancelMap = Constants.getJsonObject(res.result);

                        handler.sendEmptyMessageDelayed(3,0);
                    }
                });
    }

    // 1.咨询人工客服
    private void startChat(String str) {
        KFAPIs.startChat(this,
                "storeservices", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
                "食尚客服", // 2. 会话界面标题，可自定义
                str, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
                // 如果不想发送此信息，可以将此信息设置为""或者null
                false, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
                // 显示:true, 不显示:false
                5, // 5. 默认显示消息数量
                //修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
                null, //"http://47.90.33.185/PHP/XMPP/gyfd/chat/web/img/kefu-avatar.png",//6. 修改默认客服头像，如果想显示客服真实头像，设置此参数为null
                Constants.headPath,		//7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
                false, // 8. 默认机器人应答
                false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
                null);
    }
    //监听：连接状态、即时通讯消息、客服在线状态
    private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //监听：连接状态
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
            {
//                updateStatus(intent.getIntExtra("new_state", 0));
            }//监听：即时通讯消息
            else if (action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//监听消息
            {
                String body = intent.getStringExtra("body");
                String from = StringUtils.parseName(intent.getStringExtra("from"));
//				KFSLog.d("body:"+body+" from:"+from);
            }//客服工作组在线状态
            else if (action.equals(KFMainService.ACTION_XMPP_WORKGROUP_ONLINESTATUS)) {
                String onlineStatus = intent.getStringExtra("onlinestatus");
                KFLog.d("客服工作组:" + onlineStatus);//online：在线；offline: 离线
                if (onlineStatus.equals("online")) {
                    KFLog.d("online");
                } else {
                    KFLog.d("offline");
                }
            }
        }
    };
}
