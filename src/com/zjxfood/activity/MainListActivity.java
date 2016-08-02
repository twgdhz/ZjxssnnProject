package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.ActivityListAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.model.ActListModel;
import com.zjxfood.popupwindow.ContentPopup;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/16.
 * 活动列表
 */
public class MainListActivity extends AppActivity implements View.OnClickListener {

    private ListView mListView;
    private ArrayList<HashMap<String, Object>> mLists;
    private ActivityListAdapter mAdapter;
    private int lastVisibleIndex;
    private TextView mTitleText;
    private ImageView mBackImage;
    private ContentPopup mPopup;
    private ArrayList<ActListModel> mListModel = new ArrayList<ActListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_list);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        getBanner();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.activity_list);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("活动列表");
        mBackImage = (ImageView) findViewById(R.id.title_back_image);

        mBackImage.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("handler1", "=======================" + mLists);
                    mAdapter = new ActivityListAdapter(getApplicationContext(), mLists);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(onItemClickListener);
                    break;
            }
        }
    };

    //获取活动列表
    private void getBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("positionCode=10&top=20");
        XutilsUtils.get(Constants.getAdListByPosition, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        try {
                            mLists = Constants.getJsonArrayByData(res.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("mLists", mLists + "==========");

                        if (mLists != null && mLists.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_image:
                finish();
                break;
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            Bundle bundle;

            if(mLists.get(i).get("AndroidForm")!=null){
                if(mLists.get(i).get("AndroidForm").toString().equals("h5")){
                    intent.setClass(getApplicationContext(), WebActivity.class);
                    bundle = new Bundle();
                    if(mLists.get(i).get("Title")!=null) {
                        bundle.putString("title", mLists.get(i).get("Title").toString());
                    }
                    if(mLists.get(i).get("LinkUrl")!=null) {
                        bundle.putString("url", mLists.get(i).get("LinkUrl").toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Log.i("页面名字",mLists.get(i).get("AndroidForm")+"===========");
                    try {
                        intent.setClass(getApplicationContext(), Class.forName("com.zjxfood.activity." + mLists.get(i).get("AndroidForm")));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "请下载最新版本", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(mLists.get(i).get("Content")!=null){
                mPopup = new ContentPopup(MainListActivity.this,
                        mLists.get(i).get("Title").toString());
                mPopup.showAtLocation(mBackImage, Gravity.CENTER, 0, 0);
            }else if(mLists.get(i).get("LinkUrl")!=null){
                intent.setClass(getApplicationContext(), WebActivity.class);
                bundle = new Bundle();
                if(mLists.get(i).get("Title")!=null) {
                    bundle.putString("title", mLists.get(i).get("Title").toString());
                }
                if(mLists.get(i).get("LinkUrl")!=null) {
                    bundle.putString("url", mLists.get(i).get("LinkUrl").toString());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }


//            if (mLists.get(i).get("Title") != null) {
//                if (mLists.get(i).get("Title").toString().equals("注册大礼包")) {
//                    intent.setClass(getApplicationContext(), WebActivity.class);
//                    bundle = new Bundle();
//                    if(mLists.get(i).get("Title")!=null) {
//                        bundle.putString("title", mLists.get(i).get("Title").toString());
//                    }
//                    if(mLists.get(i).get("LinkUrl")!=null) {
//                        bundle.putString("url", mLists.get(i).get("LinkUrl").toString());
//                    }
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else if (mLists.get(i).get("Title").toString().equals("分享有礼")) {
//                    if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
//                        intent.setClass(getApplicationContext(),
//                                SharePoliteActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (mLists.get(i).get("Title").toString().equals("食尚红包")) {
//                    if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
//                        intent.setClass(getApplicationContext(),
//                                RedListActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (mLists.get(i).get("Title").toString().equals("食尚夺宝")) {
//                    intent.setClass(getApplicationContext(), IndianaListActivity.class);
//                    startActivity(intent);
//                } else if (mLists.get(i).get("Title").toString().equals("限时拍卖")) {
//                    intent.setClass(getApplicationContext(),
//                            DisplayAuctionActivity.class);
//                    startActivity(intent);
//                } else if (mLists.get(i).get("Title").toString().equals("2元购车")) {
//                    intent.setClass(getApplicationContext(), CashIndianaListActivity.class);
//                    startActivity(intent);
//                }
//            }
        }
    };

    private android.view.View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
}
