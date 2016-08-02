package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.adapter.MyCurrencyAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 我的余额
 * @author zjx
 *
 */
public class MyCurrencyActivity extends AppActivity implements OnClickListener{

	private TextView mCurrencyText;
	private HashMap<String, Object> mCurrencyMap;
	private ImageView mBackImage;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mListMap;
	private MyCurrencyAdapter myCurrencyAdapter;
	private int page = 1;
	private int lastVisibleIndex;
	private int x = 1;
	private Button mButton;
//	private CurrencyCzPopupWindow mCzPopupWindow;
	private PopupWindow mPopupWindow;
	private TextView mOkText,mCancelText;
	private EditText mEditText;
	private TextView mShowText;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_currency_bi_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		getCurrencyHttp();
		getCurrencyList();
	}
	
	private void init(){
		mCurrencyText = (TextView) findViewById(R.id.currency_bi_now_surplus_number);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("我的余额");
		mListView = (ListView) findViewById(R.id.my_currency_list);
		mButton = (Button) findViewById(R.id.currency_bi_bottom_btn);
		
		mBackImage.setOnClickListener(this);
		mButton.setOnClickListener(this);
	}
	//获取消费币
//	private void getCurrencyHttp(){
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		AsyUtils.get(Constants.getCurrency, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				mCurrencyMap = Constants.getJsonObject(content);
//				handler.sendEmptyMessageDelayed(1, 0);
//				super.onSuccess(statusCode, content);
//			}
//		});
//	}
	private void getCurrencyHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getCurrency, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mCurrencyMap = Constants.getJsonObject(res.result);
						Log.i("res.result", res.result+"=============");
						handler.sendEmptyMessageDelayed(1, 0);
					}
				});
	}
	//获取消费币列表
//	private void getCurrencyList(){
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		params.put("page", page+"");
//		params.put("pagesize","10");
//		AsyUtils.get(Constants.getCurrencyList, params, new AsyncHttpResponseHandler(){
//			@Override
//			@Deprecated
//			public void onSuccess(int statusCode, String content) {
//				try {
//					mListMap = Constants.getJsonArrayByData(content);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				if(x==1){
//				handler.sendEmptyMessageDelayed(2, 0);
//				}else if(x==2){
//					handler.sendEmptyMessageDelayed(3, 0);
//				}
//				super.onSuccess(statusCode, content);
//			}
//		});
//	}
	private void getCurrencyList() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId+"&page="+page+"&pagesize=10");
		XutilsUtils.get(Constants.getCurrencyList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mListMap = Constants.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(x==1){
						handler.sendEmptyMessageDelayed(2, 0);
						}else if(x==2){
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mCurrencyMap!=null && mCurrencyMap.get("Code")!=null && mCurrencyMap.get("Code").toString().equals("200")){
					mCurrencyText.setText(mCurrencyMap.get("Data").toString());
				}
				break;

			case 2:
				if(mListMap!=null && mListMap.size()>0){
				myCurrencyAdapter = new MyCurrencyAdapter(getApplicationContext(), mListMap);
				mListView.setAdapter(myCurrencyAdapter);
				mListView.setOnScrollListener(mScrollListener);
				}
				break;
			case 3:
				myCurrencyAdapter.notifyList(mListMap);
				break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == myCurrencyAdapter.getCount()-1) {
				x = 2;
				page = page+1;
				getCurrencyList();
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
		case R.id.title_back_image:
			finish();
			break;
			//食尚币充值 
		case R.id.currency_bi_bottom_btn:
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(
					R.layout.currency_cz_popup, null);
			mOkText = (TextView) view
					.findViewById(R.id.currency_cz_ok);
			mCancelText = (TextView) view.findViewById(R.id.currency_cz_cancel);
			mShowText = (TextView) view.findViewById(R.id.currency_show_title_show);
			mEditText = (EditText) view.findViewById(R.id.display_popup_chujia_value);
			mPopupWindow = new PopupWindow(view,
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			//设置SelectPicPopupWindow弹出窗体的背景
			mPopupWindow.setBackgroundDrawable(dw);
			//设置SelectPicPopupWindow弹出窗体动画效果
			mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
			mPopupWindow.showAtLocation(mCurrencyText, Gravity.CENTER, 0, 0);
			mOkText.setOnClickListener(clickListener);
			mCancelText.setOnClickListener(clickListener);
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences("余额充值", MODE_PRIVATE);
			if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
				mShowText.setText(sp.getString("Content", ""));
			}
			break;
		}
	}
	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.currency_cz_ok:
				intent.setClass(getApplicationContext(), CurrencyPayWayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("price", mEditText.getText().toString());
				bundle.putString("merchantName", "xfb");
				intent.putExtras(bundle);
				startActivity(intent);
				mPopupWindow.dismiss();
				break;
			case R.id.currency_cz_cancel:
				mPopupWindow.dismiss();
				break;
			}
		}
	};
}
