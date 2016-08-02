package com.zjxfood.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.ScreenUtils;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.JHPayWayActivity;
import com.zjxfood.activity.MallChoseAddressActivity;
import com.zjxfood.activity.MallDetailContentActivity;
import com.zjxfood.activity.MallPayWayActivity;
import com.zjxfood.activity.NewAddressActivity;
import com.zjxfood.activity.NewAddressManageActivity;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.MallDetailGridAdapter;
import com.zjxfood.adapter.MallDetailViewPagerAdapter;
import com.zjxfood.adapter.MallParameterAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.interfaces.NotifyMallDetailImpl;
import com.zjxfood.interfaces.NotifyMallDetailInterface;
import com.zjxfood.popupwindow.JhPopupWindow;
import com.zjxfood.popupwindow.MallSizePopupWindow;
import com.zjxfood.popupwindow.ServiceCommitmentPopupWindow;
import com.zjxfood.qq.Util;
import com.zjxfood.view.MyScrollListView;
import com.zjxfood.view.ScrollViewContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MallDetailFragment extends Fragment implements
		OnClickListener {
	private LinearLayout mCheckSizeLayout;// 查看颜色尺码
	private LinearLayout mMallIntroductionLayout;// 产品介绍
	private LinearLayout mAlertLayout;
	private MallDetailGridAdapter mGridAdapter;
	private Bundle mBundle;
	private static String price, title, yf, LoginImage = "", sale, dhnumber,
			money, giftId, id = "";// 价格、内容、运费、图片路径、销量、商品Id
	private TextView mMallName;// 商品名称
	private TextView mMarketValueText;// 市场价
	private TextView mMemberPriceText;// 会员价
	private TextView mKuaidiText;// 快递
	private TextView mStockText;// 库存
	private TextView mSalesText;// 销量
	private TextView mUserName;// 收货人姓名
	private TextView mUserPhone;// 收货人电话
	private TextView mUserAddress;// 收货人地址
	private ArrayList<HashMap<String, Object>> mDefaulAddressList;
	private Button mModifyAddressBtn;
	private String addressId = "";// 收货地址Id
	// private String userName = "", userPhone = "", userAddress = "";
	private String otherYf = "0";// 额外运费
	private Button mConfirmBtn;
	private String mShmoney = "0";
	private PopupWindow mJhPopupWindow;
	private String mJhMoney = "";// 激活金额
	private static String mSizeId = "";
	private String sizeId = "";// 选择中的尺码
	private ArrayList<HashMap<String, Object>> mSizeList;// 商品尺码list
	private ImageView mMallTitleImage;
	private ArrayList<HashMap<String, Object>> mDetailList;// 商品详情list
	private ArrayList<String> mArrayBitmaps;
	private ImageView mBackImage;
	private Button mSizeChoseBtn;
	private int mSizePosition = 0;
	private boolean isConfirmBuy = false;
	private static ArrayList<String> mImageList;
	private ViewPager mViewPager;
	private List<View> mViewList;

	private ImageView mViewImage;
	private MallDetailViewPagerAdapter mViewPagerAdapter;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	// private MallSizePopupWindow mSizePopupWindow;
	private View view;
	private BitmapUtils mBitmapUtils;
	// private boolean isJhPup = true;
	private static String sizeString;
	private TextView mServiceCommitmentText;
	private ServiceCommitmentPopupWindow mCommitmentPopupWindow;
	private int data = 0;
	private JhPopupWindow mJhPupWindow;
	private LinearLayout mContactCustomerLayout;// 联系客户
	public static String mAppid;
	public static Tencent mTencent;
	private LinearLayout mQqLayout1, mQqLayout2;
	private LinearLayout mGallertImageLayout;
	private ImageView[] mImageViewIds;// 小圆点ImageView数组
	private HashMap<String, Object> mIsFavoriteMap;
	private HashMap<String, Object> mAddFavoriteMap;
	private HashMap<String, Object> mDeleteFavoriteMap;
	private ImageView mFavoriteImage;
	private TextView mFavoriteText;
	private RelativeLayout mFavoriteLayout;
	private boolean isFavorite = true;
	private HashMap<String, Object> mIsUserBuyMap;
	private TextView mGraphicDetails, mProductParameters;
	private View mGraphicView, mProductView;
	private LinearLayout mGraphiLayout, mProductLayout;
	private WebView mWebView;
	private LinearLayout mGraphiContentLayout, mProductContentLayout;
	private MyScrollListView mParameterList;
	private ArrayList<HashMap<String, Object>> mParamterArrayList;
	private MallParameterAdapter mParamterAdapter;
	private Button mCommoditySpecificationsBtn;
	private MallSizePopupWindow mSizePopupWindow;
	private NotifyMallDetailImpl mNotifyMallDetailImpl;
	// private MallBuyWindow mBuyWindow;
	private LinearLayout mBottomLayout;
	public static String mFlag = "1";
	private TextView mShowText;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mall_detail_new_layout, null);
		init(view);

		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.mall_detail_new_layout);
		mBitmapUtils = new BitmapUtils(getActivity());
		ExitApplication.getInstance().addActivity(getActivity());
		// ExitApplication.getInstance().addAddressActivity(getActivity());
		ExitApplication.getInstance().addMallActivity(getActivity());
		mFlag = "1";
//		init();
		mNotifyMallDetailImpl = new NotifyMallDetailImpl();
		mNotifyMallDetailImpl.setListener(mInterface);
		checkTencentInstance();
		mBundle = getActivity().getIntent().getExtras();
		if (mBundle != null) {
			if (mBundle.getString("flag").equals("1")) {
				sizeString = null;
				mSizeId = "";
				mImageList = new ArrayList<String>();
				price = mBundle.getString("Price");
				title = mBundle.getString("Title");
				yf = mBundle.getString("Yf");
				dhnumber = mBundle.getString("Dhnumber");

				LoginImage = mBundle.getString("Image1");
				id = mBundle.getString("Id");
				sale = mBundle.getString("sale");
				money = mBundle.getString("money");
				giftId = mBundle.getString("giftId");
				if (mBundle.getString("Image1") != null
						&& !(mBundle.getString("Image1").equals(""))) {
					mImageList.add(LoginImage);
				}
				if (!(mBundle.getString("titleImage1").equals(""))) {
					mImageList.add(mBundle.getString("titleImage1"));
				}
				if (!(mBundle.getString("titleImage2").equals(""))) {
					mImageList.add(mBundle.getString("titleImage2"));
				}
				if (!(mBundle.getString("titleImage3").equals(""))) {
					mImageList.add(mBundle.getString("titleImage3"));
				}
			}
		}
		mViewList = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		if (mImageList != null && mImageList.size() > 0) {
			mGallertImageLayout.removeAllViews();
			mImageViewIds = new ImageView[mImageList.size()];
			for (int i = 0; i < mImageList.size(); i++) {
				view = inflater.inflate(R.layout.mall_detail_viewpager_item,
						null);
				mViewImage = (ImageView) view.findViewById(R.id.mall_detail_viewpager_image);
				mBitmapUtils.display(mViewImage, mImageList.get(i));
				mViewList.add(view);

				ImageView imageView = new ImageView(getActivity());
				imageView.setImageResource(R.drawable.ic_dot_true);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 0, 10, 0);
				imageView.setLayoutParams(lp);
				mGallertImageLayout.addView(imageView);
				mImageViewIds[i] = imageView;
			}
		}
		if (mImageViewIds != null && mImageViewIds.length > 0) {
			mImageViewIds[0].setImageDrawable(getActivity().getBaseContext().getResources()
					.getDrawable(R.drawable.ic_dot_fouc));
		}
		mViewPagerAdapter = new MallDetailViewPagerAdapter(
				getActivity(), mViewList, mImageList);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		// getMallSize();
		// mExecutorService.execute(sizeRun);
		getMallDetail();
		setResource();
		Log.i("sizeString", sizeString + "===========" + mSizeId);
		if (sizeString != null && !(mSizeId.equals(""))) {
			mSizeChoseBtn.setText(sizeString);
			mSizeChoseBtn.setVisibility(View.VISIBLE);
		}
		IntentFilter filter = new IntentFilter(NewAddressActivity.action);
		// filter.addAction(NewAddressActivity.action);
		filter.addAction(MallParameterFragment.action);
		filter.addAction(ScrollViewContainer.action);
		getActivity().registerReceiver(mBroadcastReceiver, filter);
		if (Constants.onLine == 1) {
			isFavorite();
		}
		getMallValue();
		getSizeHttp();
		SharedPreferences sp = getActivity().getSharedPreferences(
				"商品详情", getActivity().MODE_PRIVATE);
		if (sp != null && sp.getString("Content", "") != null
				&& !sp.getString("Content", "").toString().equals("")) {
			mShowText.setText(sp.getString("Content", ""));
		}
	}

	private void init(View view) {
		mCheckSizeLayout = (LinearLayout) view.findViewById(R.id.mall_detail_color_layout);
		mAlertLayout = (LinearLayout) view.findViewById(R.id.mall_detail_not_log_alert_view);
		mMallName = (TextView) view.findViewById(R.id.mall_detail_name_text);
		mMarketValueText = (TextView) view.findViewById(R.id.mall_detail_market_value);
		mMemberPriceText = (TextView) view.findViewById(R.id.mall_detail_member_price_value_text);
		mKuaidiText = (TextView) view.findViewById(R.id.mall_detail_express_value);
		mStockText = (TextView) view.findViewById(R.id.mall_detail_stock);
		mSalesText = (TextView) view.findViewById(R.id.mall_detail_sales);
		mUserName = (TextView) view.findViewById(R.id.mall_detail_receiving_name);
		mUserPhone = (TextView) view.findViewById(R.id.mall_detail_receiving_phone);
		mUserAddress = (TextView) view.findViewById(R.id.mall_detail_receiving_address);
		mModifyAddressBtn = (Button) view.findViewById(R.id.mall_detail_receiving_modity_btn);
		mMallIntroductionLayout = (LinearLayout) view.findViewById(R.id.mall_detail_introduction_layout);
		mConfirmBtn = (Button) view.findViewById(R.id.mall_detail_confirm_buy_btn);
		mMallTitleImage = (ImageView) view.findViewById(R.id.mall_detail_title_image);
		mBackImage = (ImageView) view.findViewById(R.id.mall_new_back_info_image);
		mSizeChoseBtn = (Button) view.findViewById(R.id.mall_detail_size_chose_btn);
		mServiceCommitmentText = (TextView) view.findViewById(R.id.srvice_commitment_text);
		mGallertImageLayout = (LinearLayout) view.findViewById(R.id.mall_detail_view_image_layout);
		mViewPager = (ViewPager) view.findViewById(R.id.mall_detail_view_pager);
		mContactCustomerLayout = (LinearLayout) view.findViewById(R.id.mall_detail_contact_customer_layout);
		mQqLayout1 = (LinearLayout) view.findViewById(R.id.mall_contact_list_layout);
		mQqLayout2 = (LinearLayout) view.findViewById(R.id.mall_contact_list_layout2);
		mFavoriteImage = (ImageView) view.findViewById(R.id.mall_detail_favorite_image);
		mFavoriteText = (TextView) view.findViewById(R.id.mall_detail_favorite_text);
		mFavoriteLayout = (RelativeLayout) view.findViewById(R.id.mall_detail_favorite_layout);

		mGraphicDetails = (TextView) view.findViewById(R.id.mall_introduction_graphic_details);
		mProductParameters = (TextView) view.findViewById(R.id.mall_introduction_product_parameters);
		mGraphicView = view.findViewById(R.id.mall_introduction_graphic_details_view);
		mProductView = view.findViewById(R.id.mall_introduction_product_parameters_view);
		mGraphiLayout = (LinearLayout) view.findViewById(R.id.mall_detail_introduction_head_child_layout1);
		mProductLayout = (LinearLayout) view.findViewById(R.id.mall_detail_introduction_head_child_layout2);
		mWebView = (WebView) view.findViewById(R.id.mall_introduction_webview);
		mGraphiContentLayout = (LinearLayout) view.findViewById(R.id.bottom_scroll_graphic_details);
		mProductContentLayout = (LinearLayout) view.findViewById(R.id.bottom_scroll_product_parameters);
		mParameterList = (MyScrollListView) view.findViewById(R.id.mall_detail_parameter_list);
		mCommoditySpecificationsBtn = (Button) view.findViewById(R.id.mall_detail_commodity_specifications_btn);
		mBottomLayout = (LinearLayout) view.findViewById(R.id.mall_detail_bottom_layout);
		mShowText = (TextView) view.findViewById(R.id.mall_detail_buy_purchase_show);

		ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mViewPager
				.getLayoutParams();
		layoutParams.height = ScreenUtils
				.getScreenWidth(getActivity());
		mViewPager.setLayoutParams(layoutParams);

		mBottomLayout.setOnClickListener(this);
		mCommoditySpecificationsBtn.setOnClickListener(this);
		mGraphiLayout.setOnClickListener(this);
		mProductLayout.setOnClickListener(this);
		// mSizeLayout.setOnClickListener(getActivity());

		mFavoriteLayout.setOnClickListener(this);
		mQqLayout1.setOnClickListener(this);
		mQqLayout2.setOnClickListener(this);
		mContactCustomerLayout.setOnClickListener(this);
		mServiceCommitmentText.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mCheckSizeLayout.setOnClickListener(this);
		mAlertLayout.setOnClickListener(this);
		mModifyAddressBtn.setOnClickListener(this);
		mMallIntroductionLayout.setOnClickListener(this);
		mConfirmBtn.setOnClickListener(this);
	}

	private void setResource() {
		mMallName.setText(title);
		if (money != null) {
			Double moneys = Double.parseDouble(money + "");
			mMarketValueText.setText("￥"
					+ new java.text.DecimalFormat("#.00").format(moneys) + "");
		}
		mMarketValueText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
		if (price != null) {
			Double pri = Double.parseDouble(price);
			mMemberPriceText.setText(new java.text.DecimalFormat("#.00")
					.format(pri) + "");
		}
		mSalesText.setText("销量：" + sale);
		mStockText.setText("库存：" + dhnumber + "件");
		String yfs = Float.parseFloat(yf) + "";

		Double Yf = Double.parseDouble(yfs + "");
		mKuaidiText.setText(new java.text.DecimalFormat("#.00").format(Yf)
				+ "元");
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle mBundle = new Bundle();
		switch (v.getId()) {
		case R.id.mall_detail_bottom_layout:

			break;
		// 商品规格
		case R.id.mall_detail_commodity_specifications_btn:
			Log.i("mSizeList", mSizeList + "==============");
			if (mSizeList != null && mSizeList.size() > 0) {
				mGridAdapter = new MallDetailGridAdapter(
						getActivity(), mSizeList);
				mSizePopupWindow = new MallSizePopupWindow(
						getActivity(), mOnItemClickListener,
						mGridAdapter, itemsOnClick);
				mSizePopupWindow.showAtLocation(getActivity()
						.findViewById(R.id.mall_detail_id), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);

			} else {
				Toast.makeText(getActivity(), "该商品没有规格可选！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.mall_detail_introduction_head_child_layout1:
			mGraphicView.setVisibility(View.VISIBLE);
			mProductView.setVisibility(View.GONE);
			mGraphicDetails.setTextColor(getResources().getColor(R.color.red));
			mProductParameters.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mGraphiContentLayout.setVisibility(View.VISIBLE);
			mProductContentLayout.setVisibility(View.GONE);
			break;
		case R.id.mall_detail_introduction_head_child_layout2:
			mGraphicView.setVisibility(View.GONE);
			mProductView.setVisibility(View.VISIBLE);
			mGraphicDetails.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mProductParameters.setTextColor(getResources()
					.getColor(R.color.red));
			mGraphiContentLayout.setVisibility(View.GONE);
			mProductContentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.mall_detail_introduction_head_child_layout3:
			// mBottomPager.setCurrentItem(2);
			mGraphicView.setVisibility(View.GONE);
			mProductView.setVisibility(View.GONE);
			// mSizeView.setVisibility(View.VISIBLE);
			mGraphicDetails.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			mProductParameters.setTextColor(getResources().getColor(
					R.color.service_provider_color));
			// mSizeText.setTextColor(getResources().getColor(R.color.red));
			break;
		// 收藏
		case R.id.mall_detail_favorite_layout:
			Log.i("isFavorite", isFavorite + "===========");
			if (Constants.onLine == 1) {
				if (!isFavorite) {
					addFavorite();
				} else {
					cancelFavorite();
				}
			} else {
				Toast.makeText(getActivity(), "请先登录帐号",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 联系QQ1
		case R.id.mall_contact_list_layout:
			String uin = "2924007010";
			if (!"".equals(uin)) {
				int ret = mTencent.startWPAConversation(
						getActivity(), uin, "");
				if (ret != 0) {
					Toast.makeText(getActivity(),
							"start WPA conversation failed. error:" + ret,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		// 联系QQ2
		case R.id.mall_contact_list_layout2:
			uin = "3289142544";
			if (!"".equals(uin)) {
				int ret = mTencent.startWPAConversation(
						getActivity(), uin, "");
				if (ret != 0) {
					Toast.makeText(getActivity(),
							"start WPA conversation failed. error:" + ret,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		// 联系客服：
		case R.id.mall_detail_contact_customer_layout:
			break;
		// 服务承诺
		case R.id.srvice_commitment_text:
//			mCommitmentPopupWindow = new ServiceCommitmentPopupWindow(
//					getActivity());
//			mCommitmentPopupWindow.showAtLocation(mViewPager, Gravity.CENTER,
//					0, 0);
			break;
		// 点击标题图片放大显示
		case R.id.mall_detail_title_image:

			break;
		// 选择颜色、尺码
		case R.id.mall_detail_color_layout:
			handler.sendEmptyMessageDelayed(6, 0);
			break;

		case R.id.mall_detail_not_log_alert_view:
			break;
		// 填写/更换地址
		case R.id.mall_detail_receiving_modity_btn:
			if (Constants.onLine == 1) {
				if (mModifyAddressBtn.getText().equals("去填写")) {
					intent.setClass(getActivity(),
							NewAddressActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "1");
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (mModifyAddressBtn.getText().equals("修改")) {
					intent.setClass(getActivity(),
							NewAddressManageActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "1");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			} else {
				Toast.makeText(getActivity(), "请先登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 产品介绍
		case R.id.mall_detail_introduction_layout:
			if (mDetailList != null && mDetailList.size() > 0) {
				if (mDetailList.get(0).get("detail") != null) {
					intent.setClass(getActivity(),
							MallDetailContentActivity.class);
					mBundle = new Bundle();
					mBundle.putString("detail", mDetailList.get(0)
							.get("detail").toString());
					mBundle.putString("gid", giftId);
					intent.putExtras(mBundle);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "该商品暂时没有更多介绍！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), "该商品暂时没有更多介绍！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 确认购买
		case R.id.mall_detail_confirm_buy_btn:
			if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
				getUserShmoney();

			} else {
				Toast.makeText(getActivity(), "请先登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 返回
		case R.id.mall_new_back_info_image:
			ExitApplication.getInstance().finishMall();
			break;
		}
	}

	// Grid监听事件
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mSizePosition = position;
			mGridAdapter.setSeclection(position);
			mGridAdapter.notifyDataSetChanged();
			// mSizeId = sizeId;
			if (mSizeList != null && mSizeList.size() > 0) {
				if (!Constants.isNull(mSizeList.get(position).get("Id"))) {
					sizeId = mSizeList.get(position).get("Id").toString();
				}
			}
			Log.i("mSizeId", mSizeId + "=======确认=======");
		}
	};

	// 获取默认地址
	// private void getDefaultAddress() {
	// RequestParams params = new RequestParams();
	// params.put("uid", Constants.mId);
	// AsyUtils.get(Constants.getDefaultAddress2, params,
	// new JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(JSONObject response) {
	// try {
	// mDefaulAddressList = getJsonLists(response
	// .toString());
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// if (mDefaulAddressList != null
	// && mDefaulAddressList.size() > 0) {
	// handler.sendEmptyMessageDelayed(1, 0);
	// }
	// super.onSuccess(response);
	// }
	// });
	// }

	// 获取额外运费
	private void getOtherYf() {
		RequestParams params = new RequestParams();
		params.put("addressid", addressId);
		AsyUtils.get(Constants.getOtherYf2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("content", content
								+ "===========content==========");
						otherYf = content;
						handler.sendEmptyMessageDelayed(2, 0);
						super.onSuccess(content);
					}
				});
	}

	// 获取用户食尚币
	private void getUserShmoney() {
		RequestParams params = new RequestParams();
		params.put("username", Constants.mUserName);
		AsyUtils.get(Constants.getUserShmoney2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("用户食尚币", content + "=====================");
						mShmoney = content;
						handler.sendEmptyMessageDelayed(3, 0);
						super.onSuccess(content);
					}
				});
	}

	// 获取激活金额
	private void getJhMoney() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		AsyUtils.get(Constants.getJhMoney2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						Log.i("激活金额", content + "=====================");
						mJhMoney = content;
						if (content.length() > 10) {// 获取的数据不为金额
							handler.sendEmptyMessageDelayed(5, 0);
						} else {
							handler.sendEmptyMessageDelayed(4, 0);
						}

						super.onSuccess(content);
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error, String content) {
						handler.sendEmptyMessageDelayed(5, 0);
						super.onFailure(error, content);
					}
				});
	}

	private void getMallDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=" + id);
		XutilsUtils.get(Constants.getMallDetail2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mDetailList = Constants.getJsonArray(res.result);
						handler.sendEmptyMessageDelayed(7, 0);
					}
				});
	}

	Handler handler = new Handler() {
		Intent intent = new Intent();

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mDefaulAddressList != null && mDefaulAddressList.size() > 0) {
					Log.i("mModifyAddressBtn", "=======2=========="
							+ mDefaulAddressList);
					if (mDefaulAddressList.get(0).get("Id").toString()
							.equals("null")) {
						return;
					}
					mUserName.setText("收货人姓名："
							+ mDefaulAddressList.get(0).get("Realname")
									.toString());
					mUserPhone.setText("收货人电话："
							+ mDefaulAddressList.get(0).get("Mobile")
									.toString());
					mUserAddress.setText("收货人地址："
							+ mDefaulAddressList.get(0).get("Address")
									.toString());
					mModifyAddressBtn.setText("修改");
					addressId = mDefaulAddressList.get(0).get("Id").toString();
					getOtherYf();
				} else {
					Log.i("mModifyAddressBtn", "=======3==========");
					mModifyAddressBtn.setText("去填写");
				}
				break;

			case 2:
				String money = Float.parseFloat(yf) + Float.parseFloat(otherYf)
						+ "";

				Double Yf = Double.parseDouble(money + "");
				mKuaidiText.setText(new java.text.DecimalFormat("#.00")
						.format(Yf) + "元");
				break;
			case 3:
				// 判断是否有可选择的尺码规格
				boolean isBuy = true;
				if (mSizeList != null && mSizeList.size() > 0) {
					if (mSizeId.equals("")) {
						isBuy = false;
					} else {
						isBuy = true;
					}
				}
				Constants.mShMoney = mShmoney;
				if (isConfirmBuy) {
					if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
						if (Float.parseFloat(mShmoney) > Float
								.parseFloat(mMemberPriceText.getText()
										.toString())) {
							// if (!(addressId.equals(""))) {
							// if (!(mUserName.getText().toString().equals(""))
							// && !(mUserPhone.getText().toString()
							// .equals(""))
							// && !(mUserAddress.getText().toString()
							// .equals(""))) {
							// if (!(mKuaidiText.getText().toString()
							// .equals(""))) {
							if (isBuy) {
								// if (Constants.mIsjh.equals("1")) {
								if (mKuaidiText.getText().toString() != null) {
									// mBuyWindow = new MallBuyWindow(
									// MallDetailActivity.getActivity(),
									// mBuyClick,
									// mKuaidiText
									// .getText()
									// .toString());
									// mBuyWindow.showAtLocation(mMallName,
									// Gravity.CENTER,
									// 0, 0);
									intent.setClass(getActivity(),
											MallChoseAddressActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("flag", "1");
									bundle.putString("price",
											Float.parseFloat(yf) + "");
									bundle.putString("merchantName", title);
									// 商家ID 商城页面随便传一个来充数
									bundle.putString("mId", "52913");
									bundle.putString("type", "gift");
									bundle.putString("flum", "0");
									bundle.putString("LoginImage", LoginImage);
									bundle.putString("mallOrMerchant", "mall");
									// bundle.putString("address", addressId);
									bundle.putString("giftId", giftId);
									bundle.putString("sizeId", mSizeId);
									intent.putExtras(bundle);
									startActivity(intent);
								}
								// } else {
								// isUserBuy();
								// }
							} else {
								Toast.makeText(getActivity(),
										"请选择商品规格！", Toast.LENGTH_SHORT).show();
							}
							// } else {
							// Toast.makeText(getActivity(),
							// "地区获取不正确！", Toast.LENGTH_SHORT)
							// .show();
							// }
							// } else {
							// Toast.makeText(getActivity(),
							// "收货人信息填写不完整！", Toast.LENGTH_SHORT)
							// .show();
							// }
							// } else {
							// Toast.makeText(getActivity(),
							// "地址获取不正确！", Toast.LENGTH_SHORT).show();
							// }
						} else {
							Toast.makeText(getActivity(), "食尚币不足！",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(getActivity(), "请先登录账户！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "数据加载失败！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 跳转到激活页面
			case 4:
				Bundle bundle = new Bundle();
				// 跳转到支付方式选择页面
				bundle.putString("price", mJhMoney);
				bundle.putString("merchantName", "食尚男女");
				bundle.putString("mId", Constants.mId);
				bundle.putString("LoginImage", "");
				bundle.putString("flum", "");
				bundle.putString("type", "");
				intent.putExtras(bundle);
				intent.setClass(getActivity(), JHPayWayActivity.class);
				startActivity(intent);
				break;
			case 5:
				Toast.makeText(getActivity(), "激活失败，请重新登录！",
						Toast.LENGTH_SHORT).show();

				break;
			case 6:
				if (mSizeList != null && mSizeList.size() > 0) {
					// mGridAdapter = new MallDetailGridAdapter(
					// getActivity(), mSizeList);
					// mSizePopupWindow = new MallSizePopupWindow(
					// MallDetailActivity.getActivity(), mOnItemClickListener,
					// mGridAdapter, itemsOnClick);
					// mSizePopupWindow.showAtLocation(MallDetailActivity.getActivity()
					// .view.findViewById(R.id.mall_detail_id), Gravity.BOTTOM
					// | Gravity.CENTER_HORIZONTAL, 0, 0);

				} else {
					Toast.makeText(getActivity(), "该商品没有规格可选！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 7:
				if (mDetailList != null && mDetailList.size() > 0) {
					// 设置两页菜单
					String str = "";
					if (mDetailList.get(0).get("detail") != null) {
						str = mDetailList.get(0).get("detail").toString();
					}
					if (str != null && !(giftId.equals(""))) {
						mWebView.loadDataWithBaseURL(null, str, "text/html",
								"utf-8", null);
						Log.i("设置两页菜单", "============设置两页菜单2==============");
					}
					mArrayBitmaps = new ArrayList<String>();
					if (mDetailList.get(0).get("image") != null) {
						mArrayBitmaps.add(mDetailList.get(0).get("image")
								.toString());
					}
					if (mDetailList.get(0).get("image1") != null) {
						mArrayBitmaps.add(mDetailList.get(0).get("image1")
								.toString());
					}
					if (mDetailList.get(0).get("image2") != null) {
						mArrayBitmaps.add(mDetailList.get(0).get("image2")
								.toString());
					}
					if (mDetailList.get(0).get("image3") != null) {
						mArrayBitmaps.add(mDetailList.get(0).get("image3")
								.toString());
					}

					if (mDetailList.get(0).get("image") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailList
								.get(0).get("image").toString());
					} else if (mDetailList.get(0).get("image1") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailList
								.get(0).get("image1").toString());
					} else if (mDetailList.get(0).get("image2") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailList
								.get(0).get("image2").toString());
					} else if (mDetailList.get(0).get("image3") != null) {
						mBitmapUtils.display(mMallTitleImage, mDetailList
								.get(0).get("image3").toString());
					}
				}
				break;
			case 8:
				isConfirmBuy = true;
				break;
			case 9:
				// mTitleGallery.setSelection(index);
				break;
			case 10:
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case 11:
				mViewPagerAdapter = new MallDetailViewPagerAdapter(
						getActivity(), mViewList, mImageList);
				mViewPager.setAdapter(mViewPagerAdapter);
				mViewPager.setOnPageChangeListener(new MyPageChangeListener());
				break;
			case 12:
				if (mIsFavoriteMap != null && mIsFavoriteMap.size() > 0) {
					if (mIsFavoriteMap.get("Code") != null
							&& mIsFavoriteMap.get("Code").equals("200")) {
						if (!Constants.isNull(mIsFavoriteMap.get("Data"))) {
							if (mIsFavoriteMap.get("Data").toString()
									.equals("false")) {
								mFavoriteImage
										.setImageResource(R.drawable.icon_shoucangqian);
								mFavoriteText.setText("收藏");
								isFavorite = false;
							} else if (mIsFavoriteMap.get("Data").toString()
									.equals("true")) {
								mFavoriteImage
										.setImageResource(R.drawable.icon_shoucanghou);
								mFavoriteText.setText("已收藏");
								isFavorite = true;
							}
						}
					}
				}
				break;
			case 13:
				if (mAddFavoriteMap != null && mAddFavoriteMap.size() > 0) {
					Toast.makeText(getActivity(),
							mAddFavoriteMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					if (!Constants.isNull(mAddFavoriteMap.get("Code"))
							&& mAddFavoriteMap.get("Code").equals("200")) {
						mFavoriteImage
								.setImageResource(R.drawable.icon_shoucanghou);
						mFavoriteText.setText("已收藏");
						isFavorite = true;
					}
				} else {
					Toast.makeText(getActivity(), "添加收藏失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 14:
				if (mDeleteFavoriteMap != null && mDeleteFavoriteMap.size() > 0) {
					Toast.makeText(getActivity(),
							mDeleteFavoriteMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					if (!Constants.isNull(mDeleteFavoriteMap.get("Code"))
							&& mDeleteFavoriteMap.get("Code").equals("200")) {
						mFavoriteImage
								.setImageResource(R.drawable.icon_shoucangqian);
						mFavoriteText.setText("收藏");
						isFavorite = false;
					}
				} else {
					Toast.makeText(getActivity(), "取消收藏失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 15:
				if (mIsUserBuyMap != null && mIsUserBuyMap.size() > 0) {
					if (!Constants.isNull(mIsUserBuyMap.get("Data"))
							&& mIsUserBuyMap.get("Data").toString()
									.equals("true")) {
						intent.setClass(getActivity(),
								MallPayWayActivity.class);
						bundle = new Bundle();
						bundle.putString(
								"price",
								Float.parseFloat(yf)
										+ Float.parseFloat(otherYf) + "");
						bundle.putString("merchantName", title);
						bundle.putString("mId", "52913");
						bundle.putString("type", "gift");
						bundle.putString("flum", "0");
						bundle.putString("LoginImage", LoginImage);
						bundle.putString("mallOrMerchant", "mall");
						bundle.putString("address", addressId);
						bundle.putString("giftId", giftId);
						bundle.putString("sizeId", mSizeId);
						intent.putExtras(bundle);
						startActivity(intent);

					} else {
						mJhPupWindow = new JhPopupWindow(
								getActivity(), mJhClick,
								mIsUserBuyMap.get("Message").toString());
						mJhPupWindow.showAtLocation(mMallName, Gravity.CENTER,
								0, 0);
					}
				} else {
					mJhPupWindow = new JhPopupWindow(getActivity(),
							mJhClick, "");
					mJhPupWindow
							.showAtLocation(mMallName, Gravity.CENTER, 0, 0);
				}
				break;
			case 16:
				if (mParamterArrayList != null && mParamterArrayList.size() > 0) {
					mParamterAdapter = new MallParameterAdapter(
							getActivity(), mParamterArrayList);
					mParameterList.setAdapter(mParamterAdapter);
				}
				break;
			}
		};
	};

	// private OnClickListener mBuyClick = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent();
	// switch (v.getId()) {
	// case R.id.mall_buy_ok:
	// intent.setClass(getActivity(),
	// MallChoseAddressActivity.class);
	// Bundle bundle = new Bundle();
	// bundle.putString("flag", "1");
	// bundle.putString("price",
	// Float.parseFloat(yf)+"");
	// bundle.putString("merchantName", title);
	// //商家ID 商城页面随便传一个来充数
	// bundle.putString("mId", "52913");
	// bundle.putString("type", "gift");
	// bundle.putString("flum", "0");
	// bundle.putString("LoginImage", LoginImage);
	// bundle.putString("mallOrMerchant", "mall");
	// // bundle.putString("address", addressId);
	// bundle.putString("giftId", giftId);
	// bundle.putString("sizeId", mSizeId);
	// intent.putExtras(bundle);
	// startActivity(intent);
	// mBuyWindow.dismiss();
	// break;
	// }
	// }
	// };

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			mSizePopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.pop_mall_detail_x:
				break;
			case R.id.popup_mall_bottom_confirm_btn:
				mSizeChoseBtn.setVisibility(View.VISIBLE);
				mSizeId = sizeId;
				if (!Constants.isNull(mSizeList.get(mSizePosition).get("yf"))) {
					yf = mSizeList.get(mSizePosition).get("yf").toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("price"))) {
					price = mSizeList.get(mSizePosition).get("price")
							.toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("money"))) {
					money = mSizeList.get(mSizePosition).get("money")
							.toString();
				}
				if (!Constants.isNull(mSizeList.get(mSizePosition)
						.get("amount"))) {
					dhnumber = mSizeList.get(mSizePosition).get("amount")
							.toString();
				}
				if (!Constants
						.isNull(mSizeList.get(mSizePosition).get("color"))
						&& !Constants.isNull(mSizeList.get(mSizePosition).get(
								"chima"))) {
					sizeString = mSizeList.get(mSizePosition).get("color")
							.toString()
							+ "-"
							+ mSizeList.get(mSizePosition).get("chima")
									.toString();
					Log.i("mSizeId", mSizeList.get(mSizePosition).get("chima")
							+ "===========");
				}
				mSizeChoseBtn.setText(sizeString);
				Log.i("mSizeId", mSizeId + "=======确认=======");

				Double pri = Double.parseDouble(money);
				mMarketValueText.setText("￥" + pri + "");
				mMarketValueText.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
				mMemberPriceText.setText(price);
				mStockText.setText("库存：" + dhnumber + "件");
				getOtherYf();
				break;
			default:
				break;
			}
		}
	};

	// 去激活
	OnClickListener mJhClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mJhPupWindow.dismiss();
			if (!(Constants.mId.equals(""))) {
				getJhMoney();
			} else {
				Toast.makeText(getActivity(), "激活失败，请重新登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 取消激活提示框
	OnClickListener mCancelJhClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mAlertLayout.setVisibility(View.GONE);
			mJhPopupWindow.dismiss();
		}
	};

	public ArrayList<HashMap<String, Object>> getJsonLists(String json)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Id", jsonObject.getString("Id"));
		map.put("Address", jsonObject.getString("Address"));
		map.put("Realname", jsonObject.getString("Realname"));
		map.put("Mobile", jsonObject.getString("Mobile"));
		map.put("Cityid", jsonObject.getString("Cityid"));

		map.put("Provinceid", jsonObject.getString("Provinceid"));
		map.put("Areaid", jsonObject.getString("Areaid"));
		map.put("Status", jsonObject.getString("Status"));

		map.put("Create_time", jsonObject.getString("Create_time"));
		map.put("Use_time", jsonObject.getString("Use_time"));
		map.put("Userid", jsonObject.getString("Userid"));

		map.put("Cityname", jsonObject.getString("Cityname"));
		map.put("Provincename", jsonObject.getString("Provincename"));
		map.put("Areaname", jsonObject.getString("Areaname"));
		map.put("Yf", jsonObject.getString("Yf"));
		list.add(map);
		return list;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			ExitApplication.getInstance().finishMall();
			// ExitApplication.getInstance().finishAddress()
			break;
		default:
			break;
		}
		return false;
	};

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			if (mImageViewIds != null && mImageViewIds.length > 0) {
				currentItem = position;
				mImageViewIds[oldPosition].setImageDrawable(getActivity().getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_true));
				mImageViewIds[position].setImageDrawable(getActivity().getBaseContext()
						.getResources().getDrawable(R.drawable.ic_dot_fouc));

				oldPosition = position;
			}
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//	}
//
//	@Override
//	protected void onStop() {
//
//		System.gc();
//		super.onStop();
//	}

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % mViewList.size();
				handler.sendEmptyMessageDelayed(10, 0); // 通过Handler切换图片
			}
		}
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
		Util.release();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			LayoutInflater inflater = LayoutInflater
					.from(getActivity());
			for (int i = 0; i < mImageList.size(); i++) {
				view = inflater.inflate(R.layout.mall_detail_viewpager_item,
						null);
				mViewImage = (ImageView) view
						.findViewById(R.id.mall_detail_viewpager_image);
				mBitmapUtils.display(mViewImage, mImageList.get(i));
				mViewList.add(view);
			}
			handler.sendEmptyMessageDelayed(11, 0);
		}
	};

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			data = Integer.parseInt(intent.getExtras().getString("data"));
			switch (data) {
			case 1:
				mUserName.setText("收货人姓名："
						+ intent.getExtras().getString("userName"));

				mUserPhone.setText("收货人电话："
						+ intent.getExtras().getString("userPhone"));

				mUserAddress.setText("收货人地址："
						+ intent.getExtras().getString("userAddress"));
				addressId = intent.getExtras().getString("addressId");
				getOtherYf();
				break;

			case 2:
				mSizeId = intent.getExtras().getString("sizeId");
				mSizeList = (ArrayList<HashMap<String, Object>>) intent
						.getExtras().getSerializable("list");
				mSizePosition = intent.getExtras().getInt("mSizePosition");
				if (mSizeList != null && mSizeList.size() > 0) {
					yf = mSizeList.get(mSizePosition).get("yf").toString();
					price = mSizeList.get(mSizePosition).get("price")
							.toString();
					money = mSizeList.get(mSizePosition).get("money")
							.toString();
					dhnumber = mSizeList.get(mSizePosition).get("amount")
							.toString();
					sizeString = mSizeList.get(mSizePosition).get("color")
							.toString()
							+ "-"
							+ mSizeList.get(mSizePosition).get("chima")
									.toString();
					mSizeChoseBtn.setText(sizeString);
					Log.i("mSizeId", mSizeId + "=======确认=======");

					Double pri = Double.parseDouble(price);
					mMarketValueText.setText("￥" + pri + "");
					mMarketValueText.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
					mMemberPriceText.setText(money);
					mStockText.setText("库存：" + dhnumber + "件");
					getOtherYf();
				}
				Log.i("data", mSizeList.get(mSizePosition).get("chima")
						+ "==========确认尺码============" + mSizeId);
				break;
			case 3:
				Log.i("data", "滚动到底了==============");
				mGraphicView.setVisibility(View.GONE);
				mProductView.setVisibility(View.VISIBLE);
				mGraphicDetails.setTextColor(getResources().getColor(
						R.color.service_provider_color));
				mProductParameters.setTextColor(getResources().getColor(
						R.color.red));
				mGraphiContentLayout.setVisibility(View.GONE);
				mProductContentLayout.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	protected void checkTencentInstance() {
		MallDetailFragment.mAppid = "222222";
		MallDetailFragment.mTencent = Tencent.createInstance(
				MallDetailFragment.mAppid, getActivity());
	}

	// 检测是否已经收藏该商品
	private void isFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid", giftId);
		AsyUtils.get(Constants.isFavorite, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						mIsFavoriteMap = Constants.getJsonObject(response
								.toString());

						Log.i("mFavoriteMap", mIsFavoriteMap
								+ "=========response===========");
						handler.sendEmptyMessageDelayed(12, 0);
						super.onSuccess(response);
					}
				});
	}

	// 添加商品收藏
	private void addFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid", giftId);
		AsyUtils.get(Constants.addFavorite, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {

						mAddFavoriteMap = Constants.getJsonObject(response
								.toString());

						Log.i("mAddFavoriteMap", mAddFavoriteMap
								+ "=========response===========");
						handler.sendEmptyMessageDelayed(13, 0);
						super.onSuccess(response);
					}
				});
	}

	// 取消商品收藏
	private void cancelFavorite() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		params.put("giftid[]", giftId);
		AsyUtils.get(Constants.deleteFavorite2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i("response", response + "====================");

						mDeleteFavoriteMap = Constants.getJsonObject(response
								.toString());

						handler.sendEmptyMessageDelayed(14, 0);
						super.onSuccess(response);
					}
				});
	}

	// 获取商品详情 产品参数
	private void getMallValue() {
		Log.i("getMallValue", "==============");
		RequestParams params = new RequestParams();
		params.put("gid", giftId);
		AsyUtils.get(Constants.getMallValue, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i("JSONArray", response + "=============");
						try {
							mParamterArrayList = Constants
									.getJsonArrayByData(response.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessageDelayed(16, 0);
						super.onSuccess(response);
					}
				});
	}

	private void getSizeHttp() {
		RequestParams params = new RequestParams();
		params.put("gid", giftId);
		AsyUtils.get(Constants.getMallSize2, params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						mSizeList = Constants.getJsonArray(content);
						handler.sendEmptyMessageDelayed(8, 0);
						super.onSuccess(content);
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error, String content) {
						isConfirmBuy = false;
						Log.i("mSizeList", content + "=======onFailure========");
						super.onFailure(error, content);
					}
				});
	}

	// 检测滚动到底切换到产品参数
	private NotifyMallDetailInterface mInterface = new NotifyMallDetailInterface() {
		@Override
		public void notifyScrollBottom() {
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}

}
