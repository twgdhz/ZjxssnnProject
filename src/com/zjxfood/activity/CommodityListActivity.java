package com.zjxfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.ScreenUtils;
import com.project.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.CommNearListAdapter;
import com.zjxfood.adapter.CommodityListAdapter;
import com.zjxfood.adapter.PopCommListTypeAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 商家列表
 * 
 * @author zjx
 * 
 */
public class CommodityListActivity extends AppActivity implements OnClickListener,
		OnGeocodeSearchListener, Runnable {
	private CommodityListAdapter mCommodityListAdapter;
	private ListView mListView;
	private PopCommListTypeAdapter mPopCommListTypeAdapter;
	private LinearLayout mTypeLayout, mNearbyLayout, mSortLayout;// 餐饮类型、附近、排序
	private ListView mTypePopList;
	// private String[] mTypeArrays, mNearbyArrays, mSortArrays;
	private String[] mTypeArrays, mSortArrays;
	private ArrayList<HashMap<String, Object>> mNearByAreaIdList;
	private TextView mTypeText, mNearbyText, mSortText;
	private PopupWindow mTypePopWindow, mNearbyPopWindow, mSortPopWindow;
	private Bundle mBundle;
	private String type, mGroupId;// 要获取的商家id
	private RelativeLayout mHeadLayout;
	private LinearLayout mBackLayout;
	// private ArrayList<String> mAreaList;
	private ArrayList<HashMap<String, Object>> merchantList;
	// private BitmapLoader1 mLoader;
	// private Bitmap[] mBitmaps2;
	private RelativeLayout mProgressLayout;
	// private int listNum = 10;// listview显示的条目
	// 最大条目数
	private int maxNumbers = 30;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private int index = 1;
	// private boolean flag = true;
	private boolean firstNotify = true;// 第一次加载
//	private MapView mapView;
//	private AMap aMap;
//	private Marker geoMarker;
//	private Marker regeoMarker;
//	private GeocodeSearch geocoderSearch;
	private String addressName;
	// private List<String> mAddressList;// 地址列表
	private List<String> mDistanceList;
	private double longt, lat;
	private int x = 1;// 忘了加载数据或者是图片
	private boolean isShow = false;
	private String areaId = "0";// 区域Id
	private CommNearListAdapter mNearListAdapter;
	private String cityId = "0";// 城市Id
	private String mPosition = "0";
	private TextView mNotAlertShow;
	private ImageView mSearchImage;
	// private int maxListNum = 0;
	private int page = 1;
	private ArrayList<HashMap<String, Object>> mAddList;
	private boolean isScroll = true;
	private String istj = "false";
	

	@SuppressLint({ "InflateParams", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commodity_list_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
//		setChildView(R.layout.base_layout);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if(mBundle.getString("type")!=null){
				type = mBundle.getString("type");
			}
			if(mBundle.getString("id")!=null){
				mGroupId = mBundle.getString("id");
			}else{
				mGroupId = "0";
			}
			cityId = mBundle.getString("cityId");
			if(mBundle.getString("istj")!=null){
				istj = mBundle.getString("istj");
			}
		}
		//
		initView();
		httpRun();
//		mapView.onCreate(savedInstanceState);
		if (Constants.mAreaLists != null && Constants.mAreaLists.size() > 0) {
			mNearByAreaIdList = Constants.mAreaLists;
		} else {
			mNearByAreaIdList = new ArrayList<HashMap<String, Object>>();
		}
		handler.postDelayed(this, 8000);// 超过8秒不加载
	}

	// 初始化控件
	private void initView() {
		mListView = (ListView) findViewById(R.id.commodity_list);
		mTypeLayout = (LinearLayout) findViewById(R.id.commodity_list_head_type);
		mNearbyLayout = (LinearLayout) findViewById(R.id.commodity_list_head_nearby);
		mSortLayout = (LinearLayout) findViewById(R.id.commodity_list_head_sort);
		mTypeText = (TextView) findViewById(R.id.commodity_list_type_text);
		mNearbyText = (TextView) findViewById(R.id.commodity_list_nearby_text);
		mSortText = (TextView) findViewById(R.id.commodity_list_sort_text);
		mProgressLayout = (RelativeLayout) findViewById(R.id.commodity_list_progress_layout);

		mHeadLayout = (RelativeLayout) findViewById(R.id.commodity_list_title_layout);
		mBackLayout = (LinearLayout) mHeadLayout
				.findViewById(R.id.commodity_title_left_layout);
//		mapView = (MapView) findViewById(R.id.comodity_map);
		mNotAlertShow = (TextView) findViewById(R.id.commodity_not_resource_show);
		mSearchImage = (ImageView) findViewById(R.id.commodity_title_search_image);
		//
//		if (aMap == null) {
//			aMap = mapView.getMap();
//			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//		}
//		geocoderSearch = new GeocodeSearch(this);
//		geocoderSearch.setOnGeocodeSearchListener(this);

		mTypeText.setText(type);
		mTypeLayout.setOnClickListener(this);
		mNearbyLayout.setOnClickListener(this);
		mSortLayout.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		mSearchImage.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		Intent intent = new Intent();
		switch (v.getId()) {
		// 搜索
		case R.id.commodity_title_search_image:
			intent.setClass(getApplicationContext(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.commodity_list_head_type:
			// 餐饮类型选择
			try{
			View view = inflater.inflate(
					R.layout.popupwindow_commodity_list_type, null);
			mTypePopList = (ListView) view
					.findViewById(R.id.popupwindow_commodity_type_list);
			mTypeArrays = getResources().getStringArray(R.array.types);
			mPopCommListTypeAdapter = new PopCommListTypeAdapter(
					getApplicationContext(), mTypeArrays);
			mTypePopList.setAdapter(mPopCommListTypeAdapter);
			mTypePopWindow = new PopupWindow(view,
					ScreenUtils.getScreenWidth(getApplicationContext()) / 3,
					LayoutParams.MATCH_PARENT, false);

			mTypePopWindow.setBackgroundDrawable(new BitmapDrawable());
			mTypePopWindow.setOutsideTouchable(true);
			mTypePopWindow.setFocusable(true);
			mTypePopWindow.showAsDropDown(mTypeLayout);
			mTypePopList.setOnItemClickListener(mTypeItemClick);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.commodity_list_head_nearby:
			// 附近选择
			try{
			View view2 = inflater.inflate(
					R.layout.popupwindow_commodity_list_type, null);
			mTypePopList = (ListView) view2
					.findViewById(R.id.popupwindow_commodity_type_list);
			mNearListAdapter = new CommNearListAdapter(getApplicationContext(),
					mNearByAreaIdList);
			mTypePopList.setAdapter(mNearListAdapter);
			mTypePopList.setOnItemClickListener(mNearbyItemClick);
			mNearbyPopWindow = new PopupWindow(view2,
					ScreenUtils.getScreenWidth(getApplicationContext()) / 3,
					LayoutParams.WRAP_CONTENT, false);

			mNearbyPopWindow.setBackgroundDrawable(new BitmapDrawable());
			mNearbyPopWindow.setOutsideTouchable(true);
			mNearbyPopWindow.setFocusable(true);
			mNearbyPopWindow.showAsDropDown(mNearbyLayout);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.commodity_list_head_sort:

			// 排序选择
			try{
			View view3 = inflater.inflate(
					R.layout.popupwindow_commodity_list_type, null);
			mTypePopList = (ListView) view3
					.findViewById(R.id.popupwindow_commodity_type_list);
			mSortArrays = getResources().getStringArray(R.array.sort);
			mPopCommListTypeAdapter = new PopCommListTypeAdapter(
					getApplicationContext(), mSortArrays);
			mTypePopList.setAdapter(mPopCommListTypeAdapter);
			mTypePopList.setOnItemClickListener(mSortItemClick);
			mSortPopWindow = new PopupWindow(view3,
					ScreenUtils.getScreenWidth(getApplicationContext()) / 3,
					LayoutParams.WRAP_CONTENT, false);
			//
			mSortPopWindow.setBackgroundDrawable(new BitmapDrawable());
			mSortPopWindow.setOutsideTouchable(true);
			mSortPopWindow.setFocusable(true);
			mSortPopWindow.showAsDropDown(mSortLayout);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.commodity_title_left_layout:
			System.gc();
			finish();
			break;
		}
	}

	// 餐饮类型选择
	OnItemClickListener mTypeItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			if (position == 0) {
				page = 1;
				x = 1;
				mProgressLayout.setVisibility(View.VISIBLE);
				mNotAlertShow.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				firstNotify = true;
				mTypeText.setText(mTypeArrays[position]);
				mGroupId = 0 + "";
				httpRun();
				mTypePopWindow.dismiss();
			} else if (position > 0) {
				page = 1;
				x = 1;
				mProgressLayout.setVisibility(View.VISIBLE);
				mNotAlertShow.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				firstNotify = true;
				mTypeText.setText(mTypeArrays[position]);
				mGroupId = position + "";
				httpRun();
				mTypePopWindow.dismiss();
			}
		}
	};
	// 附近类型选择
	OnItemClickListener mNearbyItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			if (position > 0) {
				mNearbyText.setText(mNearByAreaIdList.get(position)
						.get("Areaname").toString());
				areaId = mNearByAreaIdList.get(position).get("Areaid")
						.toString();
				mNearbyPopWindow.dismiss();
				page = 1;
				x = 1;
				mProgressLayout.setVisibility(View.VISIBLE);
				mNotAlertShow.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				firstNotify = true;
				httpRun();
			} else {
				mNearbyText.setText(mNearByAreaIdList.get(position)
						.get("Areaname").toString());
				areaId = 0 + "";
				mNearbyPopWindow.dismiss();
				page = 1;
				x = 1;
				mProgressLayout.setVisibility(View.VISIBLE);
				mNotAlertShow.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				firstNotify = true;
				httpRun();
			}
		}
	};
	// 排序类型选择
	OnItemClickListener mSortItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			mSortText.setText(mSortArrays[position]);
			mSortPopWindow.dismiss();
			x = 1;
			// pageSize = 10;
//			Log.i("商家排序", position+"=================");
			page = 1;
			if(position==0){
				mPosition = "-1";
			}else {
				mPosition = (position + 1) + "";
			}
			
			mProgressLayout.setVisibility(View.VISIBLE);
			mNotAlertShow.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			firstNotify = true;
			httpRun();
		}
	};

	// 商家列表点击事件
	OnItemClickListener mCommodityItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
//			Log.i("merchantList", merchantList.get(position) + "===========");
			if (merchantList.get(position).get("Merchantname") != null) {
				bundle.putString("merchantName", merchantList.get(position)
						.get("Merchantname").toString());
			}
			if (merchantList.get(position).get("Logoimage") != null) {
				bundle.putString("Logoimage",
						merchantList.get(position).get("Logoimage").toString());
			}
			if (merchantList.get(position).get("Address") != null) {
				bundle.putString("merchantAddress", merchantList.get(position)
						.get("Address").toString());
			}
			if (merchantList.get(position).get("Phone") != null) {
				bundle.putString("Phone",
						merchantList.get(position).get("Phone").toString());
			}
			if (merchantList.get(position).get("Introduction") != null) {
				bundle.putString("Introduction", merchantList.get(position)
						.get("Introduction").toString());
			}
			if (merchantList.get(position).get("Userid") != null) {
				bundle.putString("Userid",
						merchantList.get(position).get("Userid").toString());
			}
			if (merchantList.get(position).get("Id") != null) {
				bundle.putString("Id", merchantList.get(position).get("Id")
						.toString());
			}
			if (merchantList.get(position).get("plstar") != null) {
				bundle.putString("plstar",
						merchantList.get(position).get("plstar").toString());
			} else {
				bundle.putString("plstar", "0");
			}
			if (merchantList.get(position).get("verifyState") != null) {
				bundle.putString("verifyState",
						merchantList.get(position).get("verifyState")
								.toString());
			}
			if (merchantList.get(position).get("Images1") != null) {
				bundle.putString("Images1",
						merchantList.get(position).get("Images1").toString());
			}
			if (merchantList.get(position).get("Images2") != null) {
				bundle.putString("Images2",
						merchantList.get(position).get("Images2").toString());
			}
			if (merchantList.get(position).get("Images3") != null) {
				bundle.putString("Images3",
						merchantList.get(position).get("Images3").toString());
			}
			if (merchantList.get(position).get("Images4") != null) {
				bundle.putString("Images4",
						merchantList.get(position).get("Images4").toString());
			}
			if (merchantList.get(position).get("Images5") != null) {
				bundle.putString("Images5",
						merchantList.get(position).get("Images5").toString());
			}
			if (merchantList.get(position).get("iscurrency") != null) {
				bundle.putString("iscurrency",
						merchantList.get(position).get("iscurrency").toString());
			}
			
			if (merchantList.get(position).get("location") != null) {
				bundle.putString("location",
						merchantList.get(position).get("location").toString());
			}
			if (merchantList.get(position).get("Isparking") != null) {
				bundle.putString("Isparking",
						merchantList.get(position).get("Isparking").toString());
			}
			
			if (merchantList.get(position).get("money") != null) {
				bundle.putString("money",
						merchantList.get(position).get("money").toString());
			}
			if (merchantList.get(position).get("ordercount") != null) {
				bundle.putString("ordercount",
						merchantList.get(position).get("ordercount").toString());
			}
			if (merchantList.get(position).get("Flnum") != null) {
				bundle.putString("Flnum",
						merchantList.get(position).get("Flnum").toString());
			}
			if (merchantList.get(position).get("km") != null) {
				bundle.putString("km",
						merchantList.get(position).get("km").toString());
			}
			if (merchantList.get(position).get("currencybackbl") != null) {
				bundle.putString("currencybackbl",
						merchantList.get(position).get("currencybackbl").toString());
			}
			if (merchantList.get(position).get("istop") != null) {
				bundle.putString("istop",
						merchantList.get(position).get("istop").toString());
			}
			intent.putExtras(bundle);
			// 跳转到商家详情界面
			intent.setClass(getApplicationContext(), MerchantInfoActivity.class);
			startActivity(intent);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (merchantList.size() > 0) {
					if (firstNotify) {
						mCommodityListAdapter = new CommodityListAdapter(
								getApplicationContext(), merchantList);
						mListView.setVisibility(View.VISIBLE);
						mListView.setAdapter(mCommodityListAdapter);
						mListView.setOnItemClickListener(mCommodityItemClick);
						mListView.setOnScrollListener(mScrollListener);
						mProgressLayout.setVisibility(View.GONE);
						mNotAlertShow.setVisibility(View.GONE);
						firstNotify = false;
					}
				} else {
					mNotAlertShow.setVisibility(View.VISIBLE);
					mProgressLayout.setVisibility(View.GONE);
				}
				break;
			case 6:
				isScroll = true;
				mCommodityListAdapter.notifyList(mAddList);
				break;
			}
		};
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			break;
		default:
			break;
		}
		return false;
	}

	OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			Log.i("page", page + "===========1=========");
			if (isScroll) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mCommodityListAdapter.getCount() - 1) {
					x = 2;
					page = page + 1;
					httpRun();
					isScroll = false;
				}
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			// 所有的条目已经和最大条数相等，则移除底部的View
			if (totalItemCount == maxNumbers) {
			}
			if (totalItemCount == maxNumbers && index == 1) {
				index++;
			}
		}
	};

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 响应地理编码
	 */
//	public void getLatlon(final String name) {
//		GeocodeQuery query = new GeocodeQuery(name, "成都市");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
//		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
//	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// 获取两点之间距离
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				addressName = address.getLatLonPoint() + "";
				String[] distance = addressName.split(",");
				lat = Double.parseDouble(distance[0]);
				longt = Double.parseDouble(distance[1]);
				DecimalFormat df = new DecimalFormat("###.00");
				double d = (Constants.getDistance(Constants.longt,
						Constants.lat, longt, lat)) / 1000;
				mDistanceList.add(df.format(d) + "km");
			} else {
				mDistanceList.add("--km");
			}
			if (mDistanceList.size() >= merchantList.size()) {
				handler.sendEmptyMessageDelayed(4, 0);
			}
		} else if (rCode == 27) {
			ToastUtil.show(CommodityListActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(CommodityListActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(CommodityListActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
	}

	private void httpRun() {
		StringBuilder sb = new StringBuilder();
//		sb.append("cityid="+cityId+"&areaid="+areaId+"&groupid="+mGroupId+"&name="+"&orderby="+mPosition+"&page="+page+"&pagesize=10");
		sb.append("page="+page+"&pagesize=10"+"&orderby="+mPosition+"&name="+"&groupid="+mGroupId+"&cityid="+cityId+"&areaid="+areaId+"&x="+Constants.longt+"&y="+Constants.lat+"&istj="+istj);

		XutilsUtils.get(Constants.getMerchant2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("merchantList", res.result+"===========");
						if (x == 1) {
							merchantList = Constants.getJsonArray(res.result);
							handler.sendEmptyMessageDelayed(1, 0);
						} else if (x == 2) {
							mAddList = Constants.getJsonArray(res.result);
							merchantList.addAll(mAddList);
							handler.sendEmptyMessageDelayed(6, 0);
						}
					}
				});
	}
	
	@Override
	public void run() {
		// Log.i("handler", "请求超时==========================");
		if (isShow) {
			LayoutInflater inflater = LayoutInflater
					.from(CommodityListActivity.this);

			View view = inflater.inflate(R.layout.popup_not_network, null);
		}
	}

	// 读取本地数据获取区域
	public String readDistrictFile(String districtName) {
		String district = "";
		try {
			Workbook book = Workbook.getWorkbook(getResources().getAssets()
					.open("district.xls"));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int Rows = sheet.getRows();
			int Cols = sheet.getColumns();
			for (int i = 0; i < Rows; ++i) {
				for (int j = 0; j < Cols; ++j) {
					// getCell(Col,Row)获得单元格的值
					if ((sheet.getCell(j, i)).getContents()
							.equals(districtName)) {
						Log.i("district",
								(sheet.getCell(j, i)).getContents()
										+ "===="
										+ (sheet.getCell(j - 1, i))
												.getContents());
						district = (sheet.getCell(j - 1, i)).getContents();
					}
				}
			}
			book.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return district;
	}

}
