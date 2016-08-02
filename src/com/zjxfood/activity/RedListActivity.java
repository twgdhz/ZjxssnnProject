package com.zjxfood.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.RedClickInterface;
import com.zjxfood.interfaces.RedInterfaceImpl;
import com.zjxfood.popupwindow.GrabRedPopupWindow;
import com.zjxfood.red.RedIssuedActivity;
import com.zjxfood.red.RedListAdapter;
import com.zjxfood.red.RedWinInfoActivity;
import com.zjxfood.red.RefreshableView;
import com.zjxfood.red.RefreshableView.PullToRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 红包列表
 * 
 * @author zjx
 * 
 */
public class RedListActivity extends AppActivity implements OnClickListener {

	private ListView mListView;
	private RefreshableView refreshLayout;
	private RedListAdapter mRedListAdapter;
	private GrabRedPopupWindow mGrabRedPopupWindow;
	private ImageView mBackImage;
	private TextView mRedCordText;// 红包记录
	private HashMap<String, Object> mRedMap;
	private ArrayList<HashMap<String, Object>> mList, mAddList;
	private int page = 1;
	private String mHbId, mUserName;
	private HashMap<String, Object> mGrabRedMap;
	private int lastVisibleIndex;
	private int x = 1;
	private int maxNum = 0;
	private String mPath;
	private TextView mNoAlertText;
	private RelativeLayout mAlertLayout;
	private RedInterfaceImpl mImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.red_list_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mImpl = new RedInterfaceImpl();
		mImpl.setClickInterface(mInterface);
		IntentFilter filter = new IntentFilter(RedWinInfoActivity.action);
		registerReceiver(receiver, filter);
		getRedList();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.red_list);
		mBackImage = (ImageView) findViewById(R.id.red_list_back_info_image);
		mRedCordText = (TextView) findViewById(R.id.title_red_list_record);
		refreshLayout = (RefreshableView) findViewById(R.id.crefreshLayout);
		mNoAlertText = (TextView) findViewById(R.id.red_no_alert_text);
		mAlertLayout = (RelativeLayout) findViewById(R.id.red_content_list_layout);

		mBackImage.setOnClickListener(this);
		mRedCordText.setOnClickListener(this);
		refreshLayout.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(1000); // sleep 8 seconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				x = 1;
				page = 1;
				maxNum = 0;
				getRedList();
				refreshLayout.finishRefreshing();
//				new AsyncTask<Void, Void, Void>() {
//				@Override
//				protected Void doInBackground(Void... params) {
//					
//					return null;
//				}
//				@Override
//				protected void onPostExecute(Void result) {
//					// adapter.count = 15;
//					// adapter.notifyDataSetChanged();
//					mRedListAdapter.notifyDataSetChanged();
////					refreshLayout.finishRefreshing();
//					super.onPostExecute(result);
//				}
//			}.execute();
//				refreshLayout.finishRefreshing();
			}
		}, 0);
//		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				new AsyncTask<Void, Void, Void>() {
//					@Override
//					protected Void doInBackground(Void... params) {
//						try {
//							Thread.sleep(1000); // sleep 8 seconds
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						x = 1;
//						page = 1;
//						maxNum = 0;
//						getRedList();
//						return null;
//					}
//					@Override
//					protected void onPostExecute(Void result) {
//						// adapter.count = 15;
//						// adapter.notifyDataSetChanged();
//						mRedListAdapter.notifyDataSetChanged();
//						refreshLayout.setRefreshing(false);
//						super.onPostExecute(result);
//					}
//				}.execute();
//			}
//		});
	}

	private void getRedList() {
		try{
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&x=" + Constants.longt + ""
				+ "&y=" + Constants.lat + "&pageSize=5" + "&currentPage="
				+ page);
		XutilsUtils.get(Constants.getRedList, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mRedMap = Constants.getJsonObjectByData(res.result);
						if (mRedMap!=null && mRedMap.size() > 0
								&& mRedMap.get("rows")!=null) {
							if (x == 1) {
								mList = Constants.getJsonArray(mRedMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(1, 0);
							} else if (x == 2) {
								mAddList = Constants.getJsonArray(mRedMap.get(
										"rows").toString());
								handler.sendEmptyMessageDelayed(3, 0);
							}
						}
					}
				});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// OnItemClickListener mItemClickListener = new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	// long arg3) {
	// if(mList.get(position).get("Id")!=null){
	// mHbId = mList.get(position).get("Id").toString();
	// }
	// if(mList.get(position).get("SendUserName")!=null){
	// mUserName = mList.get(position).get("SendUserName").toString();
	// }
	// if(mList.get(position).get("SendUserPhoto")!=null){
	// mPath = mList.get(position).get("SendUserPhoto").toString();
	// }
	// mGrabRedPopupWindow = new GrabRedPopupWindow(RedListActivity.this,
	// clickListener,mPath,mUserName);
	// mGrabRedPopupWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
	// }
	// };

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mRedListAdapter.getCount() - 1
					&& maxNum < mRedListAdapter.getCount() - 1) {
				x = 2;
				page = page + 1;
				getRedList();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.grab_red_image:
				ObjectAnimator anim = ObjectAnimator//
						.ofFloat(v, "rotationY", 0.0F, 360.0F)//
						.setDuration(700);
				anim.start();

				anim.addListener(new AnimatorListener() {
					public void onAnimationStart(Animator arg0) {

					}

					public void onAnimationRepeat(Animator arg0) {

					}

					public void onAnimationEnd(Animator arg0) {
						grabRed();
					}
					public void onAnimationCancel(Animator arg0) {
					}
				});
				break;
			case R.id.grab_red_content_right_x:
				if (mGrabRedPopupWindow != null
						&& mGrabRedPopupWindow.isShowing()) {
					mGrabRedPopupWindow.dismiss();
				}
				break;
			}
		}
	};

	private void grabRed() {
		StringBuilder sb = new StringBuilder();
		sb.append("hbId=" + mHbId + "&userId=" + Constants.mId);
		XutilsUtils.get(Constants.grabRed, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (mGrabRedPopupWindow != null
						&& mGrabRedPopupWindow.isShowing()) {
					mGrabRedPopupWindow.dismiss();
				}
				Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				mGrabRedMap = Constants.getJsonObject(res.result);
				if (mGrabRedMap != null && mGrabRedMap.size() > 0) {
					handler.sendEmptyMessageDelayed(2, 0);
				}
				// Log.i("抢红包", mGrabRedMap + "==================");
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				if (mList != null && mList.size() > 0) {
					mRedListAdapter = new RedListAdapter(
							getApplicationContext(), mList, mImpl);
					mListView.setAdapter(mRedListAdapter);
					mListView.setOnScrollListener(mScrollListener);
					mNoAlertText.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
				} else {
					mNoAlertText.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
					mAlertLayout.setVisibility(View.GONE);
				}
				break;

			case 2:
				if (mGrabRedMap.get("Message") != null) {
					intent.setClass(getApplicationContext(),
							RedWinInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("userName", mUserName);
					if (mGrabRedMap.get("Data") != null) {
						bundle.putString("price", mGrabRedMap.get("Data")
								.toString());
					}
					if (mGrabRedMap.get("Message") != null) {
						bundle.putString("message", mGrabRedMap.get("Message")
								.toString());
					}
					bundle.putString("state", "1");
					bundle.putString("hbId", mHbId);
					intent.putExtras(bundle);
					startActivity(intent);
					if (mGrabRedPopupWindow != null
							&& mGrabRedPopupWindow.isShowing()) {
						mGrabRedPopupWindow.dismiss();
					}
				}
				break;
			case 3:
				maxNum = mRedListAdapter.getCount() - 1;
				mRedListAdapter.notifyList(mAddList);
				break;
			}
		};
	};

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String data = intent.getExtras().getString("data");
			if (data.equals("1")) {
				maxNum = 0;
				x = 1;
				page = 1;
				getRedList();
			}
		}
	};

	private RedClickInterface mInterface = new RedClickInterface() {
		@Override
		public void onclick(int position, String s) {
			if (s.equals("1")) {
				if (mList.get(position).get("Id") != null) {
					mHbId = mList.get(position).get("Id").toString();
				}
				if (mList.get(position).get("SendUserName") != null) {
					mUserName = mList.get(position).get("SendUserName")
							.toString();
				}
				if (mList.get(position).get("SendUserPhoto") != null) {
					mPath = mList.get(position).get("SendUserPhoto").toString();
				}
				mGrabRedPopupWindow = new GrabRedPopupWindow(
						RedListActivity.this, clickListener, mPath, mUserName);
				mGrabRedPopupWindow.showAtLocation(mListView, Gravity.CENTER,
						0, 0);
			} else if (s.equals("2")) {// 领取完
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(getApplicationContext(),
						RedWinInfoActivity.class);

				if (mList.get(position).get("Id") != null) {
					bundle.putString("hbId", mList.get(position).get("Id")
							.toString());
				}
				if (mList.get(position).get("SendUserName") != null) {
					bundle.putString("userName",
							mList.get(position).get("SendUserName").toString());
				}
				if (mList.get(position).get("TotalMoney") != null) {
					bundle.putString("price",
							mList.get(position).get("TotalMoney").toString());
				}

				if (mList.get(position).get("Memo") != null) {
					bundle.putString("message", mList.get(position).get("Memo")
							.toString());
				}
				bundle.putString("state", "3");
				intent.putExtras(bundle);
				startActivity(intent);
			} else if (s.equals("3")) {// 已领取
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(getApplicationContext(),
						RedWinInfoActivity.class);

				if (mList.get(position).get("Id") != null) {
					bundle.putString("hbId", mList.get(position).get("Id")
							.toString());
				}
				if (mList.get(position).get("SendUserName") != null) {
					bundle.putString("userName",
							mList.get(position).get("SendUserName").toString());
				}
				if (mList.get(position).get("TotalMoney") != null) {
					bundle.putString("price",
							mList.get(position).get("TotalMoney").toString());
				}

				if (mList.get(position).get("Memo") != null) {
					bundle.putString("message", mList.get(position).get("Memo")
							.toString());
				}
				bundle.putString("state", "4");
				intent.putExtras(bundle);
				startActivity(intent);
			} else if (s.equals("4")) {// 已过期
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(getApplicationContext(),
						RedWinInfoActivity.class);

				if (mList.get(position).get("Id") != null) {
					bundle.putString("hbId", mList.get(position).get("Id")
							.toString());
				}
				if (mList.get(position).get("SendUserName") != null) {
					bundle.putString("userName",
							mList.get(position).get("SendUserName").toString());
				}
				if (mList.get(position).get("TotalMoney") != null) {
					bundle.putString("price",
							mList.get(position).get("TotalMoney").toString());
				}
				if (mList.get(position).get("Memo") != null) {
					bundle.putString("message", mList.get(position).get("Memo")
							.toString());
				}
				bundle.putString("state", "5");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.red_list_back_info_image:
			if (receiver != null) {
				this.unregisterReceiver(receiver);
				receiver = null;
			}
			finish();
			break;
		// 红包记录
		case R.id.title_red_list_record:
			intent.setClass(getApplicationContext(), RedIssuedActivity.class);
			startActivity(intent);
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (receiver != null) {
				this.unregisterReceiver(receiver);
				receiver = null;
			}
			finish();
			break;
		default:
			break;
		}
		return false;
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
