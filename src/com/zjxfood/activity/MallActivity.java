package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.DensityUtils;
import com.project.util.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.adapter.MallExpandAdapter;
import com.zjxfood.adapter.MallListGridAdapter;
import com.zjxfood.adapter.MallMenuAdapter;
import com.zjxfood.adapter.PopMallThreeTypeAdapter;
import com.zjxfood.adapter.PopMallTwoTypeAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.MallTypeInterImpl;
import com.zjxfood.interfaces.MallTypeInterface;
import com.zjxfood.popupwindow.NewCashListPopup;
import com.zjxfood.view.MyGridView;
import com.zjxfood.view.SlideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 食尚商城
 * 
 * @author zjx
 * 
 */
public class MallActivity extends AppActivity implements OnClickListener {

//	private LinearLayout mMainLayout, mBuyBillLayout, mMyLayout;
	private ArrayList<HashMap<String, Object>> mMallList;
	private int x = 1, m = 1;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private RelativeLayout mAlertLayout;
	// private int index = 0;// 记录选中的图片位置
	private ImageView[] mImageViewIds;// 小圆点ImageView数组
	private static final int IMAGE_COUNT = 2;// 小圆点个数
	private View mListHeadView;
//	private LinearLayout mTypeLayout, mDateSortLayout, mPriceSortLayout;
	private PopupWindow mTypePopup;
	private PopupWindow mTypeTwoPopup, mTypeThreePopup;
	private ListView mTypeListView, mTypeTwoListView, mTypeThreeListView;
//	private ListView mTypeTwoListView, mTypeThreeListView;
//	private PopMallTypeAdapter mPopMallTypeAdapter;
	private PopMallTwoTypeAdapter mTwoTypeAdapter;
	private PopMallThreeTypeAdapter mThreeTypeAdapter;
	private String[] mTypeArrays, mDateSortArrays;
	private int n = 1;
//	private ArrayList<HashMap<String, Object>> mTypeListArray;
	// private MallRunTask mMallRunTask;
	private String mallGroup = "", mTypeCode = "",mProportion="",mShopCode="",mType="";
	private TextView mTypeText;
	private ImageView mSearchImage;
	private Bundle mBundle;
	private String searchName = "";
	// private TextView mNotSearText;
	private RelativeLayout mNotLayout;
	private int page = 1;
	// private int currentNum = 0;
	private ArrayList<HashMap<String, Object>> mAddList;
	private MyGridView mGridView;
	private MallListGridAdapter mListGridAdapter;
	private ImageView mBackImage;
	private TextView mTitleText;
	private ArrayList<HashMap<String, Object>> mTwoList, mThreeList,mAllTypeList,mAdvertisementTopList;
	private String choseName = "全部";
	private boolean isScroll = true;
	private boolean isAddThreeHead = true;
	private TextView mTypeHeadText;
	private String headName;
	private int maxNum = 0;
	private String mChildCode;
	private int mChildPosition = -1;
	private boolean isLoad = true;
	private int num = -2;
	private boolean isAutoScroll = true;
	private SlideMenu mSlidemenu;
	private ExpandableListView mMenuList;
	private MallMenuAdapter mMenuAdapter;
	private LinearLayout mLeftMentLayout;
	private String mTypeName;
	private String mPid = "0";
	private MallExpandAdapter mMallExpandAdapter;
	private MallTypeInterImpl mMallTypeInterImpl;
	private RelativeLayout mQbLayout,mPxLayout;
	private ImageView mQbImage,mPxImage;
	private TextView mQbText,mPxText;
	private View mQbView,mPxView;
	private NewCashListPopup mCashPopup;
	private String[] mList;
	private static int mPosition = 0;
	private int orderby = 1;
	private ViewPager mViewPagerBanner;
	private List<View> mViewList;
	private MainViewPagerAdapter mViewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addMyActivity(this);
		ExitApplication.getInstance().addMallList(this);
		ExitApplication.getInstance().addActivity(this);
		init();
		mViewList = new ArrayList<View>();
		mPosition = 0;
		mList = getResources().getStringArray(R.array.mallsort);
		mBundle = getIntent().getExtras();
		Log.i("bundle", mallGroup + "========mallGroup1=========");
		if (mBundle != null) {
			mTypeName = mBundle.getString("typeName");
			if (mBundle.getString("name") != null) {
				searchName = mBundle.getString("name");
				isAutoScroll = false;
			}
			if(mBundle.getString("pid")!=null) {
				mPid = mBundle.getString("pid");
			}
			if(mBundle.getString("orderby")!=null) {
				orderby = Integer.parseInt(mBundle.getString("orderby"));
			}
			if(mBundle.getString("code")!=null){
				mallGroup = mBundle.getString("code");
			}
				mTypeCode = mBundle.getString("group");
				mChildCode = mBundle.getString("group");

			if (mBundle.getString("titleName") != null) {
				headName = mBundle.getString("titleName");
				mTitleText.setText(mBundle.getString("titleName"));
			}
			if (mBundle.getString("proportion") != null) {
				mProportion = mBundle.getString("proportion");
			}
			if (mBundle.getString("shouCode") != null) {
				mShopCode = mBundle.getString("shouCode");
			}
			if (mBundle.getString("type") != null) {
				mType = mBundle.getString("type");
			}
			Log.i("xxxx", "========xxxxxxxxxxxxxxxxxxxx====2222=====");
		} else {
			x = 1;
			getMallList();
			Log.i("xxxx", "========xxxxxxxxxxxxxxxxxxxx=========");
		}
		Log.i("isAutoScroll", isAutoScroll + "========isAutoScroll====11111=====");
		getMallList();
		mImageViewIds[0].setImageDrawable(getBaseContext().getResources()
				.getDrawable(R.drawable.ic_dot_fouc));
		getMallType2();
		mMallTypeInterImpl = new MallTypeInterImpl();
		mMallTypeInterImpl.setListener(listern);
		if(mType.equals("xj") || mType.equals("xj_tt") || mType.equals("xj_wzcx")|| mType.equals("xj_czth")|| mType.equals("xj_fcdp")){
			// 获取顶端banner
			getBanner();
		}

	}

	private void init() {
//		mMainLayout = (LinearLayout) findViewById(R.id.gift_center_home_menu_layout);
//		mBuyBillLayout = (LinearLayout) findViewById(R.id.gift_center_buy_menu_layout);
//		mMyLayout = (LinearLayout) findViewById(R.id.gift_center_my_menu_layout);
		mSlidemenu = (SlideMenu) findViewById(R.id.mall_silde_view);
		mLeftMentLayout = (LinearLayout) mSlidemenu.findViewById(R.id.mall_menu_left_id);
		ViewGroup.LayoutParams params = mLeftMentLayout.getLayoutParams();
		params.width = DensityUtils.dp2px(getApplicationContext(),500);
		mLeftMentLayout.setLayoutParams(params);
		mMenuList = (ExpandableListView) mSlidemenu.findViewById(R.id.menu_list);
		mAlertLayout = (RelativeLayout) findViewById(R.id.mall_list_progress_layout);
//		mTypeLayout = (LinearLayout) findViewById(R.id.mall_list_head_type);
//		mPriceSortLayout = (LinearLayout) findViewById(R.id.mall_list_head_price_sort);
//		mDateSortLayout = (LinearLayout) findViewById(R.id.mall_list_head_date);
		mTypeText = (TextView) findViewById(R.id.mall_list_type_text);
		mSearchImage = (ImageView) findViewById(R.id.mall_title_search_image);

		mGridView = (MyGridView) findViewById(R.id.mall_list_grid_view);
		mBackImage = (ImageView) findViewById(R.id.mall_list_back_info_image);

		mTitleText = (TextView) findViewById(R.id.mall_list_title_info_text);
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mListHeadView = inflater.inflate(R.layout.mall_list_head_view, null);
		mNotLayout = (RelativeLayout) findViewById(R.id.mall_search_not_alert_layout);
		mImageViewIds = new ImageView[] {
				(ImageView) mListHeadView.findViewById(R.id.mall_dot_1),
				(ImageView) mListHeadView.findViewById(R.id.mall_dot_2), };
		mQbLayout = (RelativeLayout) findViewById(R.id.mall_qb_layout);
		mPxLayout = (RelativeLayout) findViewById(R.id.mall_px_layout);
		mQbImage = (ImageView) findViewById(R.id.mall_quanbu_image);
		mQbText = (TextView) findViewById(R.id.mall_quanbu_text);
		mQbView = findViewById(R.id.mall_quanbu_view);
		mPxImage = (ImageView) findViewById(R.id.mall_px_image);
		mPxText = (TextView) findViewById(R.id.mall_px_text);
		mPxView = findViewById(R.id.mall_px_view);


		mQbLayout.setOnClickListener(this);
		mPxLayout.setOnClickListener(this);
		//
//		mMainLayout.setOnClickListener(this);
//		mBuyBillLayout.setOnClickListener(this);
//		mMyLayout.setOnClickListener(this);
		mSearchImage.setOnClickListener(this);
//		mTypeLayout.setOnClickListener(this);
//		mPriceSortLayout.setOnClickListener(this);
//		mDateSortLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.mall_list_back_info_image:
			Log.i("back", "=============back============");
			if (mTypePopup != null && mTypePopup.isShowing()) {
				dissPopup();
			} else {
				ExitApplication.getInstance().finishMallList();
			}
			break;
//		case R.id.gift_center_home_menu_layout:
//			ExitApplication.getInstance().finishMy();
//			break;

//		case R.id.gift_center_buy_menu_layout:
//			intent.setClass(getApplicationContext(), BuyBillActivity.class);
//			startActivity(intent);
//			finish();
//			break;
//		case R.id.gift_center_my_menu_layout:
//			intent.setClass(getApplicationContext(), MyNewActivity.class);
//			startActivity(intent);
//			finish();
//			break;
		case R.id.mall_qb_layout:
			if (mSlidemenu.isMainScreenShowing()) {
				mSlidemenu.openMenu();
			} else {
				mSlidemenu.closeMenu();
			}
			mQbImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_menu_hover));
			mQbText.setTextColor(getResources().getColor(R.color.main_color16));
			mQbView.setVisibility(View.VISIBLE);
			mPxImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_order_normal));
			mPxText.setTextColor(getResources().getColor(R.color.main_color3));
			mPxView.setVisibility(View.GONE);
			mPosition = 0;
			orderby = 1;
			break;
		case R.id.mall_px_layout:
			x = 1;
			mQbImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_menu_normal));
			mQbText.setTextColor(getResources().getColor(R.color.main_color3));
			mQbView.setVisibility(View.GONE);
			mPxImage.setImageDrawable(getResources().getDrawable(R.drawable.new_cashmall_order_hover));
			mPxText.setTextColor(getResources().getColor(R.color.main_color16));
			mPxView.setVisibility(View.VISIBLE);
			mCashPopup = new NewCashListPopup(MallActivity.this,mPosition,mList,onItemClickListener);
			mCashPopup.showAsDropDown(mPxLayout);
			break;
//		case R.id.mall_list_head_price_sort:
//			dissPopup();
//			try {
//				View view3 = inflater.inflate(
//						R.layout.popupwindow_mall_list_type, null);
//				mTypeListView = (ListView) view3
//						.findViewById(R.id.popupwindow_commodity_type_list);
//				mDateSortArrays = getResources().getStringArray(
//						R.array.priceSort);
//				mMenuAdapter = new MallMenuAdapter(
//						getApplicationContext(), mDateSortArrays);
//				mTypeListView.setAdapter(mMenuAdapter);
//				mTypePopup = new PopupWindow(
//						view3,
//						ScreenUtils.getScreenWidth(getApplicationContext()) / 3,
//						LayoutParams.WRAP_CONTENT, false);
//
//				mTypePopup.setBackgroundDrawable(new BitmapDrawable());
//				mTypePopup.setOutsideTouchable(true);
//				mTypePopup.setFocusable(true);
//				mTypePopup.showAsDropDown(mPriceSortLayout);
//				mTypeListView.setOnItemClickListener(mPriceItemClick);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
		// 搜索
		case R.id.mall_title_search_image:
			intent.setClass(getApplicationContext(), MallSearchActivity.class);
			startActivity(intent);
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
			orderby = i+1;
			getMallList();
//			mAdapter.selectPosition(mPosition);
		}
	};

//	// 一级分类监听
//	OnItemClickListener mTypeItemClick = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//
//		}
//	};


	private void getThreeChildType(String code) {
		RequestParams params = new RequestParams();
		params.put("code", code);
		AsyUtils.get(Constants.getMallChildType2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						try {
							mThreeList = Constants.getJsonArrayByData(response
									.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessageDelayed(9, 0);
						super.onSuccess(response);
					}
				});
	}

	// 二级分类监听
	OnItemClickListener mTwoItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (position != 0) {
				mTwoTypeAdapter.choseItem(position - 1);
				mallGroup = mTwoList.get(position - 1).get("code").toString();
				getThreeChildType(mTwoList.get(position - 1).get("code")
						.toString());
				choseName = mTwoList.get(position - 1).get("name").toString();
			} else {
				dissPopup();
				setChoseName(choseName);
				searchName = "";
				x = 1;
				getMallList();
			}
		}
	};
	// 三级分类监听
	OnItemClickListener mThreeItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mAlertLayout.setVisibility(View.VISIBLE);
			dissPopup();
			if (position != 0) {
				mallGroup = mThreeList.get(position - 1).get("code").toString();
				choseName = mThreeList.get(position - 1).get("name").toString();
			}
			searchName = "";
			x = 1;
			getMallList();
			setChoseName(choseName);

		}
	};

	// 按时间排序
//	OnItemClickListener mDateItemClick = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			try {
//				page = 1;
//				orderby = position + 1;
//				x = 1;
//				getMallList();
//				mTypePopup.dismiss();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	};
	// 按价格排序
//	OnItemClickListener mPriceItemClick = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			try {
//				page = 1;
//				orderby = position + 3 + "";
//				x = 1;
//				getMallList();
//				mTypePopup.dismiss();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			LayoutInflater inflater = LayoutInflater.from(getApplication());
			switch (msg.what) {
			case 3:
				try {
					View gridHeadView = inflater.inflate(R.layout.mall_grid_head_layout, null);
					mViewPagerBanner = (ViewPager) gridHeadView.findViewById(R.id.new_mall_viewpager);
					mViewPagerBanner.setVisibility(View.VISIBLE);
					ViewGroup.LayoutParams layoutParams = mViewPagerBanner
							.getLayoutParams();
					layoutParams.height = (int) (ScreenUtils.getScreenWidth(getApplicationContext()) * 0.51);
					mViewPagerBanner.setLayoutParams(layoutParams);
					View view;
					ImageView mViewImage;

					for (int i = 0; i < mAdvertisementTopList.size(); i++) {
						view = inflater.inflate(
								R.layout.mall_detail_viewpager_item, null);
						mViewImage = (ImageView) view
								.findViewById(R.id.mall_detail_viewpager_image);

						if (mAdvertisementTopList.get(i).get("Images") != null) {
							mBitmapUtils.display(mViewImage, mAdvertisementTopList
									.get(i).get("Images").toString());
						}
						mViewList.add(view);
					}

					mViewPagerAdapter = new MainViewPagerAdapter(
							getApplicationContext(), mViewList,
							mAdvertisementTopList);
					mViewPagerBanner.setAdapter(mViewPagerAdapter);
					mGridView.addHeaderView(gridHeadView);
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case 4:
				if (mAllTypeList != null && mAllTypeList.size() > 0) {
					mTypeArrays = new String[mAllTypeList.size() + 1];
					mTypeArrays[0] = "全部";
					for (int i = 0; i < mAllTypeList.size(); i++) {
							mTypeArrays[i+1] = mAllTypeList.get(i).get("name")
									.toString();
					}
					if(mAllTypeList.get(0).get("code")!=null) {
						mallGroup = mAllTypeList.get(0).get("code").toString();
					}
					if(mAllTypeList.get(0).get("pid")!=null) {
						mPid = mAllTypeList.get(0).get("pid").toString();
					}
				}
				if(mTypeArrays!=null) {
					if (mTypeArrays.length <= 0) {
						mTypeArrays = new String[1];
						mTypeArrays[0] = "";
					}
//					mMenuAdapter = new MallMenuAdapter(getApplicationContext(),mTypeArrays);
					getMallType2();

				}

				break;
			// 按分类显示商品
			case 5:
				dissPopup();
				maxNum = 0;
				Log.i("handler5", "============handler5===========" + mMallList);
				if (mMallList != null && mMallList.size() > 0) {
					num = -2;
					mGridView.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);

					mListGridAdapter = new MallListGridAdapter(
							getApplicationContext(), mMallList,mTypeName);
					mGridView.setAdapter(mListGridAdapter);
					mGridView.setOnScrollListener(mOnScrollListener2);
					mAlertLayout.setVisibility(View.GONE);
					mNotLayout.setVisibility(View.GONE);
					isScroll = true;
				} else {
					mGridView.setVisibility(View.GONE);
					mNotLayout.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
					page = 1;
					maxNum = 0;

					mallGroup = mChildCode;
					if (num < mChildPosition) {
						httpRun3();
					}
					num++;
					Log.i("num+position", "num:" + num + "============mchose+"
							+ mChildPosition);
				}
				break;
			case 6:
				Log.i("handler6", "===========handler6============");
				maxNum = mListGridAdapter.getCount() - 1;
				mListGridAdapter.notify(mAddList);
				isScroll = true;
				break;
			// 根据搜索内容显示商品
			case 7:
				if (mMallList != null && mMallList.size() > 0) {
					mListGridAdapter = new MallListGridAdapter(
							getApplicationContext(), mMallList,mTypeName);
					mGridView.setAdapter(mListGridAdapter);
					mAlertLayout.setVisibility(View.GONE);
					// mNotSearText.setVisibility(View.GONE);
					mNotLayout.setVisibility(View.GONE);
					mGridView.setOnScrollListener(mOnScrollListener3);
				} else {
					// mNotSearText.setVisibility(View.VISIBLE);
					mNotLayout.setVisibility(View.VISIBLE);
					mAlertLayout.setVisibility(View.GONE);
				}
				break;
			case 8:
				if (mTwoList != null && mTwoList.size() > 0) {
				} else {
					x = 1;
					searchName = "";
					getMallList();
					setChoseName(choseName);
				}
				break;
			case 9:
				if (mThreeList != null && mThreeList.size() > 0) {
					mTypeThreeListView.setVisibility(View.VISIBLE);
					mThreeTypeAdapter = new PopMallThreeTypeAdapter(
							getApplicationContext(), mThreeList);
					inflater = LayoutInflater
							.from(getApplicationContext());
					View headView = inflater.inflate(
							R.layout.mall_list_two_head_layout, null);
					if (isAddThreeHead) {
						isAddThreeHead = false;
						mTypeThreeListView.addHeaderView(headView);
					}
					mTypeThreeListView.setVisibility(View.VISIBLE);
					mTypeThreeListView.setAdapter(mThreeTypeAdapter);
					mTypeThreeListView.setOnItemClickListener(mThreeItemClick);

				} else {
					setChoseName(choseName);
					x = 1;
					searchName = "";
					getMallList();
				}
				break;
				case 10:
					mMallExpandAdapter = new MallExpandAdapter(getApplicationContext(),mAllTypeList,mMallTypeInterImpl);
					mMenuList.setAdapter(mMallExpandAdapter);
					mMenuList.setOnGroupClickListener(onGroupClickListener);
//					mMenuList.setOnChildClickListener(onChildClickListener);
					break;
			}
		};
	};

	// 商品类型滑动加载(新)
	OnScrollListener mOnScrollListener2 = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("isScroll", isScroll + "===================");
			if (!isScroll)
				return;
			Log.i("log",
					"mListGridAdapter.getCount():"
							+ (mListGridAdapter.getCount() - 1)
							+ "===lastVisibleIndex:" + lastVisibleIndex
							+ "===maxNum:" + maxNum);
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mListGridAdapter.getCount() - 1
					&& maxNum < mListGridAdapter.getCount() - 1) {
				isScroll = false;
				page = page + 1;
				x = 2;
				getMallList();
			} else if (maxNum == (mListGridAdapter.getCount() - 1)) {
//				if (mListGridAdapter != null
//						&& mListGridAdapter.getCount() > 12) {
//					page = 1;
//					maxNum = 0;
//					mallGroup = mChildCode;
//					if (isAutoScroll) {
//						if (mChildPosition != -1) {
//							// Log.i("isAutoScroll",
//							// isAutoScroll+"滑动到底了============"+3);
//							httpRun3();
//
//						} else {
//							httpRun2();
//							// Toast.makeText(getApplicationContext(),
//							// "滑动到底了============"+2,
//							// Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// Log.i("onScroll",
			// "firstVisibleItem:"+firstVisibleItem+"===visibleItemCount:"+visibleItemCount+"===totalItemCount:"+totalItemCount);
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};
	// 搜索商品滑动监听
	OnScrollListener mOnScrollListener3 = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& lastVisibleIndex == mListGridAdapter.getCount() - 1) {
				m = 2;
				page = page + 1;
				searchHttp();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mTypePopup != null && mTypePopup.isShowing()) {
				dissPopup();
			} else {
				ExitApplication.getInstance().finishMallList();
			}
			break;
		default:
			break;
		}
		return false;
	}

	private void searchHttp() {
		RequestParams params = new RequestParams();
		try {
			params.put("page", page + "");
			params.put("pagesize", "12");
			params.put("name", searchName);
			params.put("group", mallGroup + "");
			params.put("orderby", orderby);
		} catch (Exception e) {
		}
		AsyUtils.get(Constants.getMallList, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						Log.i("getMallList", response + "==================");
						if (m == 1) {
							mMallList = Constants.getJsonArray(response
									.toString());
							handler.sendEmptyMessageDelayed(7, 0);
						} else if (m == 2) {
							mAddList = Constants.getJsonArray(response
									.toString());
							handler.sendEmptyMessageDelayed(6, 0);
						}
						super.onSuccess(response);
					}
				});
	}

	//商品列表
	private void getMallList() {
		StringBuilder sb = new StringBuilder();
		if(mType.equals("ssjb")) {
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" + mShopCode + "&group=" + mallGroup + "&orderby=" + orderby + "&userBuyLevel=" + mProportion+"&name="+searchName+"&userid="+Constants.mId);
		}else if(mType.equals("ssb")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" + mShopCode + "&group=" + mallGroup + "&orderby=" + orderby+"&name="+searchName+"&userid="+Constants.mId);
		}else if(mType.equals("xj")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" + mShopCode + "&group=" + mallGroup + "&orderby=" + orderby+"&name="+searchName+"&userid="+Constants.mId);
		}else if(mType.equals("xj_tt")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode="+mShopCode + "&isGiftBag=true"+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("xj_wzcx")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode="+mShopCode + "&isHalfPrice=true"+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("xj_czth")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" +mShopCode+ "&buyOneGive=2"+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("xj_fcdp")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" + mShopCode + "&group="+mallGroup+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("jb_dp")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" +mShopCode+ "&shmoneyBuyRatio="+mProportion+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("jb_wzth")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" +mShopCode+ "&shmoneyBuyRatio="+mProportion+"&orderBy="+orderby+"&userid="+Constants.mId);
		}else if(mType.equals("jb_tj")){
			sb.append("pageSize=10" + "&page=" + page + "&shopCode=" +mShopCode+ "&isGiftBag=true"+"&shmoneyBuyRatio="+mProportion+"&userid="+Constants.mId+"&orderBy="+orderby);
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
								handler.sendEmptyMessageDelayed(5, 0);
							} else if (x == 2) {
								mAddList = Constants.getJsonArrayByData(res.result);
								handler.sendEmptyMessageDelayed(6, 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	private void httpRun2() {
		StringBuilder sb = new StringBuilder();
		sb.append("page=" + page + "&pagesize=12" + "&name=" + searchName
				+ "&group=" + mallGroup + "&orderby=" + orderby);
		XutilsUtils.get(Constants.getMallList2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mMallList = Constants.getJsonArray(res.result);
						handler.sendEmptyMessageDelayed(5, 0);

					}
				});
	}

	private void httpRun3() {
		Log.i("httpRun3", "===============httpRun3=================");
		if (isLoad) {
			mChildPosition++;
			if (mAllTypeList != null && mAllTypeList.size() > 0) {
				if (mChildPosition < mAllTypeList.size()
						&& mAllTypeList.get(mChildPosition).get("code") != null) {
					mChildCode = mAllTypeList.get(mChildPosition).get("code")
							.toString();
				} else {
					mChildPosition = 0;
					mChildCode = mAllTypeList.get(mChildPosition).get("code")
							.toString();
				}
				mallGroup = mChildCode;
				if (mMenuAdapter != null) {
//					mPopMallTypeAdapter.choseItem(mChildPosition);
					httpRun2();
					choseName = mAllTypeList.get(mChildPosition).get("name")
							.toString();
					setChoseName(choseName);
				}
			}
		}
	}

//	private void getMallType() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("giftTypeCode=" + mallGroup + "&buyLevel=3"+Constants.mId+"&pid="+mPid);
////		sb.append("giftTypeCode=" + mallGroup);
//		XutilsUtils.get(Constants.getMallChildType, sb,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//					@Override
//					public void onSuccess(ResponseInfo<String> res) {
//						Log.i("商品分类", res.result + "==============");
//						if (n == 1) {
//							try {
//								mTypeListArray = Constants
//										.getJsonArrayByData(res.result);
//								handler.sendEmptyMessageDelayed(4, 0);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//	}
	private void getMallType2() {
		StringBuilder sb = new StringBuilder();
//		sb.append("code=" + mallGroup);
		sb.append("giftTypeCode=" + mTypeCode+"&pid="+mPid);
		XutilsUtils.get(Constants.getMallChildType3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("获取所有分类数据", res.result + "==============");
						if (n == 1) {
							try {
								mAllTypeList = Constants
										.getJsonArrayByData(res.result);
								handler.sendEmptyMessageDelayed(10, 0);
								Log.i("商品分类二级", mAllTypeList + "==============");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}
	OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			try {
				int pos = position % IMAGE_COUNT;
				mImageViewIds[pos].setImageDrawable(getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_fouc));
				if (pos > 0) {
					mImageViewIds[pos - 1]
							.setImageDrawable(getBaseContext().getResources()
									.getDrawable(R.drawable.ic_dot_true));
				}
				if (pos < (IMAGE_COUNT - 1)) {
					mImageViewIds[pos + 1]
							.setImageDrawable(getBaseContext().getResources()
									.getDrawable(R.drawable.ic_dot_true));
				}
				if (pos == 0) {
					mImageViewIds[IMAGE_COUNT - 1]
							.setImageDrawable(getBaseContext().getResources()
									.getDrawable(R.drawable.ic_dot_true));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	private void dissPopup() {
		mAlertLayout.setVisibility(View.GONE);
		if (mTypePopup != null && mTypePopup.isShowing()) {
			mTypePopup.dismiss();
		}
		if (mTypeTwoPopup != null && mTypeTwoPopup.isShowing()) {
			mTypeTwoPopup.dismiss();
		}
		if (mTypeThreePopup != null && mTypeThreePopup.isShowing()) {
			mTypeThreePopup.dismiss();
		}
	}

	private void setChoseName(String value) {
		mTitleText.setText(value);
//		mTypeText.setText(value);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		isLoad = false;
		MobclickAgent.onPause(this);
	}

	MallTypeInterface listern = new MallTypeInterface() {
		@Override
		public void onclick(int position,ArrayList<HashMap<String,Object>> list) {
			mSlidemenu.closeMenu();

			try{
			 Log.i("分类数据", position+"==========0======="+list);
				page = 1;
				x = 1;
				maxNum = 0;
				mChildCode = list.get(position).get("code")
						.toString();
				mallGroup = list.get(position).get("code")
						.toString();
				searchName = "";
				choseName = list.get(position).get("name")
						.toString();
				setChoseName(choseName);
				mType = mTypeName;
				getMallList();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
			Log.i("一级分类", "==========0======="+mAllTypeList.get(i).get("Childrens"));

			if(mAllTypeList.get(i).get("Childrens")!=null){
				ArrayList<HashMap<String,Object>> list = Constants.getJsonArray(mAllTypeList.get(i).get("Childrens").toString());
				if(list!=null && list.size()<=0){
					x = 1;
					mType = mTypeName;
					page = 1;
					maxNum = 0;
					mChildCode = mAllTypeList.get(i).get("code")
							.toString();
					mallGroup = mAllTypeList.get(i).get("code")
							.toString();
					searchName = "";
					choseName = mAllTypeList.get(i).get("name")
							.toString();
					setChoseName(choseName);
					getMallList();
						mSlidemenu.closeMenu();

					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	};

	// 获取首页顶端图片
	private void getBanner() {
		StringBuilder sb = new StringBuilder();
		sb.append("positionCode=5&top=5");
		XutilsUtils.get(Constants.getAdListByPosition, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("onFailure", arg1 + "======获取首页顶端图片======");
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							Log.i("mAdvertisementTopList",
									res.result + "======获取首页顶端图片======");
							mAdvertisementTopList = Constants
									.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mAdvertisementTopList != null
								&& mAdvertisementTopList.size() > 0) {
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}
}
