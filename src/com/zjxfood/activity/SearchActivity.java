package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MerchantSearchListAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppActivity implements OnClickListener {

	private EditText mSearchEdit;
	private TextView mSearchText;
	private int x = 1;
	private ArrayList<HashMap<String, Object>> mMerchantList;
//	private Bitmap[] mBitmaps;
//	private boolean flag = true;
	private MerchantSearchListAdapter mListAdapter;
	private ListView mListView;
	private ImageView mBackImage;
	// private int size = 5;// 列表初始化长度
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_mall_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}

	private void init() {
		mSearchEdit = (EditText) findViewById(R.id.merchant_title_search);
		mSearchText = (TextView) findViewById(R.id.merchant_search_text);
		mListView = (ListView) findViewById(R.id.merchant_search_list);
		mBackImage = (ImageView) findViewById(R.id.merchant_search_back_image);

		mSearchText.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "没有搜索到相关内容！",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				mListAdapter = new MerchantSearchListAdapter(
						getApplicationContext(), mMerchantList);
				mListView.setAdapter(mListAdapter);
				mListView.setOnItemClickListener(mItemClickListener);
				mListView.setOnScrollListener(mScrollListener);
				break;
			case 2:
				mMerchantList.addAll(mAddList);
				mListAdapter.notifyList(mMerchantList);
				break;
			}
		};
	};
	// 列表点击事件
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(mMerchantList!=null && mMerchantList.size()>0){
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			if (mMerchantList.get(position).get("Merchantname") != null) {
				bundle.putString("merchantName", mMerchantList.get(position)
						.get("Merchantname").toString());
			}
			if (mMerchantList.get(position).get("Logoimage") != null) {
				bundle.putString("Logoimage",
						mMerchantList.get(position).get("Logoimage").toString());
			}
			if (mMerchantList.get(position).get("Address") != null) {
				bundle.putString("merchantAddress", mMerchantList.get(position)
						.get("Address").toString());
			}
			if (mMerchantList.get(position).get("Phone") != null) {
				bundle.putString("Phone",
						mMerchantList.get(position).get("Phone").toString());
			}
			if (mMerchantList.get(position).get("Introduction") != null) {
				bundle.putString("Introduction", mMerchantList.get(position)
						.get("Introduction").toString());
			}
			if (mMerchantList.get(position).get("Userid") != null) {
				bundle.putString("Userid",
						mMerchantList.get(position).get("Userid").toString());
			}
			if (mMerchantList.get(position).get("Id") != null) {
				bundle.putString("Id", mMerchantList.get(position).get("Id")
						.toString());
			}
			if (mMerchantList.get(position).get("Flnum") != null) {
				bundle.putString("Flnum",
						mMerchantList.get(position).get("Flnum").toString());
			}
			if (mMerchantList.get(position).get("plstar") != null) {
				bundle.putString("plstar",
						mMerchantList.get(position).get("plstar").toString());
			} else {
				bundle.putString("plstar", "0");
			}
			bundle.putString("verifyState",
					mMerchantList.get(position).get("verifyState").toString());
			if (mMerchantList.get(position).get("Images1") != null) {
				bundle.putString("Images1",
						mMerchantList.get(position).get("Images1").toString());
			}
			if (mMerchantList.get(position).get("Images2") != null) {
				bundle.putString("Images2",
						mMerchantList.get(position).get("Images2").toString());
			}
			if (mMerchantList.get(position).get("Images3") != null) {
				bundle.putString("Images3",
						mMerchantList.get(position).get("Images3").toString());
			}
			if (mMerchantList.get(position).get("Images4") != null) {
				bundle.putString("Images4",
						mMerchantList.get(position).get("Images4").toString());
			}
			if (mMerchantList.get(position).get("Images5") != null) {
				bundle.putString("Images5",
						mMerchantList.get(position).get("Images5").toString());
			}
			if (mMerchantList.get(position).get("iscurrency") != null) {
				bundle.putString("iscurrency",
						mMerchantList.get(position).get("iscurrency").toString());
			}
			if (mMerchantList.get(position).get("location") != null) {
				bundle.putString("location",
						mMerchantList.get(position).get("location").toString());
			}
			if (mMerchantList.get(position).get("ordercount") != null) {
				bundle.putString("ordercount",
						mMerchantList.get(position).get("ordercount").toString());
			}
			if (mMerchantList.get(position).get("Isparking") != null) {
				bundle.putString("Isparking",
						mMerchantList.get(position).get("Isparking").toString());
			}
			if (mMerchantList.get(position).get("money") != null) {
				bundle.putString("money",
						mMerchantList.get(position).get("money").toString());
			}
			if (mMerchantList.get(position).get("km") != null) {
				bundle.putString("km",
						mMerchantList.get(position).get("km").toString());
			}
			if (mMerchantList.get(position).get("currencybackbl") != null) {
				bundle.putString("currencybackbl",
						mMerchantList.get(position).get("currencybackbl").toString());
			}
			if (mMerchantList.get(position).get("istop") != null) {
				bundle.putString("istop",
						mMerchantList.get(position).get("istop").toString());
			}
			// bundle.putSerializable("bitmaps", mBitmaps);
			intent.putExtras(bundle);
			// 跳转到商家详情界面
			intent.setClass(getApplicationContext(), MerchantInfoActivity.class);
			startActivity(intent);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.merchant_search_text:
			if (!(mSearchEdit.getText().toString().equals(""))) {
				x = 1;
				// 加载商家列表
				search();
				// mRunTask = new DownloadTask();
				// String time = System.currentTimeMillis() + "";
				// try {
				// String sign = Constants.sortsStr("cityid=0&groupid=0&name="
				// + mSearchEdit.getText().toString()
				// + "&orderby=0&page="+page+"&pagesize=5"
				// + "&timestamp=" + time);
				// mRunTask.execute(Constants.getMerchant
				// + sign
				// + "&cityid=0&groupid=0&name="
				// + URLEncoder.encode(mSearchEdit.getText()
				// .toString(), "UTF-8")
				// + "&orderby=0&page=1&pagesize=5"
				// + "&timestamp=" + time);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
			}
			break;

		case R.id.merchant_search_back_image:
			finish();
			break;
		}
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mListAdapter.getCount() - 1) {
				x = 2;
				page = page + 1;
				search();
//				mRunTask = new DownloadTask();
//				try {
//					String time = System.currentTimeMillis() + "";
//
//					String sign = Constants.sortsStr("cityid=0&groupid=0&name="
//							+ mSearchEdit.getText().toString()
//							+ "&orderby=0&page=" + page + "&pagesize=5"
//							+ "&timestamp=" + time);
//
//					mRunTask.execute(Constants.getMerchant
//							+ sign
//							+ "&cityid=0&groupid=0&name="
//							+ URLEncoder.encode(mSearchEdit.getText()
//									.toString(), "UTF-8") + "&orderby=0&page="
//							+ page + "&pagesize=5" + "&timestamp=" + time);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};

//	class DownloadTask extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... str) {
//			switch (x) {
//			case 1:
//				// 加载商家数据
//				String res;
//				try {
//					res = ReadJson.readParse(str[0]);
//					Log.i("str[0]", str[0] + "==============");
//					// res = ReadJson.getJson(Constants.searchMerchant, str[0]);
//					mMerchantList = Constants.getJsonArray(res);
//					Log.i("mMerchantList", mMerchantList
//							+ "=======================");
//					if (mMerchantList.size() > 0) {
//						mBitmaps = new Bitmap[mMerchantList.size()];
//						handler.sendEmptyMessageDelayed(1, 0);
//					} else {
//						handler.sendEmptyMessageDelayed(0, 0);
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//
//			case 2:
//				try {
//					res = ReadJson.readParse(str[0]);
//					// mMerchantList = Constants.getJsonArray(res);
//					mAddList = Constants.getJsonArray(res);
//					if (mMerchantList.size() > 0) {
//						handler.sendEmptyMessageDelayed(2, 0);
//					} else {
//						handler.sendEmptyMessageDelayed(0, 0);
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			case 3:
//				String json2;
//				try {
//					json2 = ReadJson.readParse(str[0]);
//					mMerchantList = Constants.getJsonArray(json2);
//					handler.sendEmptyMessageDelayed(5, 0);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (flag) {
//					handler.sendEmptyMessageDelayed(2, 0);
//				} else {
//					handler.sendEmptyMessageDelayed(3, 0);
//				}
//				break;
//			}
//			return null;
//		}
//	}

	// String sign = Constants.sortsStr("cityid=0&groupid=0&name="
	// + mSearchEdit.getText().toString()
	// + "&orderby=0&page="+page+"&pagesize=5"
	// + "&timestamp=" + time);
	private void search() {
		StringBuilder sb = new StringBuilder();
		sb.append("cityid=0&groupid=0&name=" + mSearchEdit.getText().toString()
				+ "&orderby=0&page=" + page + "&pagesize=5");
		XutilsUtils.get(Constants.getMerchant2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {

						Log.i("mMerchantList", mMerchantList
								+ "=======================");

						if (x == 1) {

							mMerchantList = Constants.getJsonArray(res.result);
							if (mMerchantList != null
									&& mMerchantList.size() > 0) {
//								mBitmaps = new Bitmap[mMerchantList.size()];
								handler.sendEmptyMessageDelayed(1, 0);
							} else {
								handler.sendEmptyMessageDelayed(0, 0);
							}
						} else if (x == 2) {
							mAddList = Constants.getJsonArray(res.result);
							if (mAddList.size() > 0) {
								handler.sendEmptyMessageDelayed(2, 0);
							} else {
								handler.sendEmptyMessageDelayed(0, 0);
							}
						} else {
							handler.sendEmptyMessageDelayed(0, 0);
						}
					}
				});
	}

	// private void searchHttp(){
	// RequestParams params = new RequestParams();
	// params.put("cityid", "0");
	// params.put("groupid", "0");
	// params.put("name", mSearchEdit.getText().toString());
	// params.put("orderby", "0");
	// params.put("page", page+"");
	// params.put("pagesize", "5");
	// AsyUtils.get(Constants.getMerchant2, params, new
	// JsonHttpResponseHandler(){
	// @Override
	// public void onSuccess(JSONArray response) {
	// if(x==1){
	// mMerchantList = Constants.getJsonArray(response.toString());
	// handler.sendEmptyMessageDelayed(1, 0);
	// }else if(x==2){
	// mMerchantList = Constants.getJsonArray(response.toString());
	// handler.sendEmptyMessageDelayed(2, 0);
	// }
	// super.onSuccess(response);
	// }
	// @Override
	// public void onFailure(Throwable e, JSONObject errorResponse) {
	// Log.i("onFailure", errorResponse+"=======JSONObject==========");
	// super.onFailure(e, errorResponse);
	// }
	// @Override
	// public void onFailure(int statusCode, Throwable e,
	// JSONObject errorResponse) {
	// Log.i("onFailure",
	// errorResponse+"=======JSONObject=========="+statusCode+"==="+e);
	// super.onFailure(statusCode, e, errorResponse);
	// }
	// });
	// }

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
