package com.zjxfood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.FavoriteListAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.ReadJson;
import com.zjxfood.interfaces.FavoriteFaceImpl;
import com.zjxfood.interfaces.FavoriteInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyFavoriteActivity extends AppActivity implements OnClickListener {

	private ImageView mBackImage;
	private ListView mListView;
	private FavoriteListAdapter mFavoriteListAdapter;
	private ArrayList<HashMap<String, Object>> mFavoriteList, mAddList;
	private FavoriteFaceImpl mFaceImpl;
	private HashMap<String, String> mChoseMap;
	private int page = 1;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private int x = 1;
//	private HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();
	private TextView mDeleteText, mCancelText;
	private CheckBox mAllCheck;
	private TextView mTitleCancelText;
	private StringBuilder mSb,sb;
//	private ExecutorService mExecutorService = Executors.newCachedThreadPool();
	private HashMap<String, Object> mDeleteMap;
	private RelativeLayout mNotLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favorite_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		mFaceImpl = new FavoriteFaceImpl();
		mFaceImpl.setmInterface(favoriteInterface);
		getFavoriteHttp();
	}

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.my_favorite_back_info_image);
		mListView = (ListView) findViewById(R.id.my_favodite_list);
		mDeleteText = (TextView) findViewById(R.id.favorite_list_delete);
		mCancelText = (TextView) findViewById(R.id.favorite_list_cancel);
		mAllCheck = (CheckBox) findViewById(R.id.favorite_list_check_image);
		mTitleCancelText = (TextView) findViewById(R.id.my_favorite_info_text);
		mNotLayout = (RelativeLayout) findViewById(R.id.my_favorite_not_show_image);

		mBackImage.setOnClickListener(this);
		mDeleteText.setOnClickListener(this);
		mCancelText.setOnClickListener(this);
		mTitleCancelText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 取消
		case R.id.my_favorite_info_text:
			if (mChoseMap != null && mChoseMap.size() > 0) {
				mFavoriteListAdapter.cancelAll();
				mChoseMap.clear();
				mFavoriteListAdapter.notifyDataSetChanged();
				mAllCheck.setChecked(false);
			}
			break;
		case R.id.my_favorite_back_info_image:
			finish();
			break;
		// 删除
		case R.id.favorite_list_delete:
			if (mChoseMap != null && mChoseMap.size() > 0) {
				int k = 0;
				mSb = new StringBuilder();
				sb = new StringBuilder();
				Iterator<Map.Entry<String, String>> it = mChoseMap.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					try {
					if (k == 0) {
						mSb.append("giftid[]=" + entry.getValue());
						sb.append("giftid[]=" + entry.getValue());
					} else {
						mSb.append("," + entry.getValue());
						sb.append("&giftid[]=" + entry.getValue());
					}
					} catch (Exception e) {
					}
					k++;
					
				}
				Log.i("sb", sb + "===================");
//				mExecutorService.execute(deleteRun);
				new Thread(deleteRun).start();
//				delete();
			}
			break;
		// 取消
		case R.id.favorite_list_cancel:
			if (mChoseMap != null && mChoseMap.size() > 0) {
				mFavoriteListAdapter.cancelAll();
				mChoseMap.clear();
				mFavoriteListAdapter.notifyDataSetChanged();
				mAllCheck.setChecked(false);
			}
			break;
		}
	}

	Runnable deleteRun = new Runnable() {
		@Override
		public void run() {
			String time = System.currentTimeMillis() + "";
			String str = "uid=" + Constants.mId + "&" + mSb+"&timestamp="+time;
			String sign = Constants.sortsStr(str);
			try {
				String res = ReadJson.readParse(Constants.deleteFavorite+sign+"&uid="+Constants.mId+"&"+sb+ "&timestamp=" + time);
				
				mDeleteMap = Constants.getJsonObject(res);
				Log.i("mDeleteMap", mDeleteMap+"==================");
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessageDelayed(3, 0);
		}
	};
//	private void delete() {
//		StringBuilder sb = new StringBuilder();
//			sb.append("uid=" + Constants.mId+"&"+mSb);
//		XutilsUtils.get(Constants.deleteFavorite2, sb,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//					@Override
//					public void onSuccess(ResponseInfo<String> res) {
//						try {
//							mDeleteMap = Constants.getJsonObject(res.result);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						handler.sendEmptyMessageDelayed(3, 0);
//					}
//				});
//	}
	
	// 获取用户收藏商品
	private void getFavoriteHttp() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("page", page + "");
		params.put("pagesize", "5");
		AsyUtils.get(Constants.getFavorite, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						if (x == 1) {
							try {
								mFavoriteList = Constants
										.getJsonArrayByData(response.toString());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendEmptyMessageDelayed(1, 0);
						} else if (x == 2) {
							try {
								mAddList = Constants
										.getJsonArrayByData(response.toString());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessageDelayed(2, 0);
						}
						super.onSuccess(response);
					}
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						Log.i("onFailure", errorResponse+"============");
						handler.sendEmptyMessageDelayed(4, 0);
						super.onFailure(e, errorResponse);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				Log.i("mList", mFavoriteList + "================");
				if (mFavoriteList != null && mFavoriteList.size() > 0) {
					mFavoriteListAdapter = new FavoriteListAdapter(
							getApplicationContext(), mFavoriteList, mFaceImpl);
					mListView.setAdapter(mFavoriteListAdapter);
					mListView.setOnScrollListener(mScrollListener);
					mAllCheck.setOnCheckedChangeListener(mChangeListener);
				}else{
					mNotLayout.setVisibility(View.VISIBLE);
				}
				break;

			case 2:
				if (mAddList != null && mAddList.size() > 0) {
					mFavoriteList.addAll(mAddList);
					mFavoriteListAdapter.notifyList(mFavoriteList);
				}
				break;
			case 3:
				if (mDeleteMap != null && mDeleteMap.get("Code").equals("200")) {

					Iterator<Map.Entry<String, String>> it = mChoseMap
							.entrySet().iterator();

					while (it.hasNext()) {
						Map.Entry<String, String> entry = it.next();
						for (int i = 0; i < mFavoriteList.size(); i++) {
							Log.i("====", mFavoriteList.get(i).get("id")+"======"+entry.getValue());
							if (mFavoriteList.get(i).get("id").toString()
									.equals(entry.getValue())) {
								Log.i("delete", "=======删除=========");
								mFavoriteList.remove(i);
							}
						}
					}
					mFavoriteListAdapter.notifyList(mFavoriteList);
					Log.i("mFavoriteList", mFavoriteList.size()
							+ "=============");
					mFavoriteListAdapter.cancelAll();
					mAllCheck.setChecked(false);
					mChoseMap.clear();
				}
				break;
			case 4:
				mNotLayout.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("page", lastVisibleIndex + "===================="
					+ mFavoriteListAdapter.getCount());
			x = 2;
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mFavoriteListAdapter.getCount() - 1) {
				page = page + 1;
				getFavoriteHttp();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};

	private FavoriteInterface favoriteInterface = new FavoriteInterface() {
		@Override
		public void onclick(HashMap<String, String> map) {
			mChoseMap = map;
			Log.i("mChoseIdList", mChoseMap + "==========");
		}
	};
	// 全选
	OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean checked) {
			if (checked) {
				mChoseMap = new HashMap<String, String>();
				mFavoriteListAdapter.choseAll();
				for (int i = 0; i < mFavoriteList.size(); i++) {
					mChoseMap.put(i + "", mFavoriteList.get(i).get("id")
							.toString());
				}
				Log.i("mChoseMap", mChoseMap + "================");
			} else {
				mFavoriteListAdapter.cancelAll();
				mChoseMap.clear();
			}
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
