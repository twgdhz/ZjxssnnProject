package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.adapter.MainViewPagerAdapter;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

public class GuideActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener{

	private ViewPager mViewPager;
	private FragmentPagAdapter mPagAdapter;
	private ArrayList<Fragment> mFragments;
	// 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex;
	private ArrayList<HashMap<String,Object>> mAdvertisementList;
	private List<View> mViewList;
	private ImageView mViewImage;
	private BitmapUtils mBitmapUtils;
	private MainViewPagerAdapter mViewPagerAdapter;
	private Button mNextBtn;
	public static boolean isStart = true;
	private RelativeLayout mSkipLayout;
	private TextView mTimeText;
	private ScheduledExecutorService scheduledExecutorService;
	private Timer timer;
	private int tiem = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		isStart = true;
//		setImmerseLayout(findViewById(R.id.head_layout));
//		mFragments = new ArrayList<Fragment>();
//		mFragments.add(new GuideOneFragment());
//		mFragments.add(new GuideTwoFragment());
//		mFragments.add(new GuideThreeFragment());
//		mFragments.add(new GuideFourFragment());
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		getBanner();
		timer = new Timer(true);
		timer.schedule(task,1000, 1000);
	}
	
	private void init(){
		mViewPager = (ViewPager) findViewById(R.id.guide_view_pager);
//		mPagAdapter = new FragmentPagAdapter(getSupportFragmentManager(), mFragments);
//		mViewPager.setAdapter(mPagAdapter);
//		mViewPager.setOnPageChangeListener(this);
		mNextBtn = (Button) findViewById(R.id.next_btn);
		mNextBtn.setOnClickListener(this);
		mSkipLayout = (RelativeLayout) findViewById(R.id.guide_tiguo_layout);
		mSkipLayout.setOnClickListener(this);
		mTimeText = (TextView) findViewById(R.id.guide_time_show);
//		initPoint();
	}
	TimerTask task = new TimerTask(){
		public void run() {

		 	handler.sendEmptyMessageDelayed(3,0);
	}
	};
	//获取顶端图片
	private void getBanner() {
//		handler.sendEmptyMessageDelayed(2, 8000);//超过8秒超时
		StringBuilder sb = new StringBuilder();
		sb.append("positionCode=14&top=4");
		XutilsUtils.get(Constants.getAdListByPosition, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
//						handler.sendEmptyMessageDelayed(2, 0);//超过8秒超时
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mAdvertisementList = Constants.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.i("mAdvertisementList", mAdvertisementList+"==========");
						if(mAdvertisementList!=null && mAdvertisementList.size()>0){
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					if(mAdvertisementList!=null && mAdvertisementList.size()>0) {
						mViewList = new ArrayList<View>();
						LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
						View view;
						for (int i = 0; i < mAdvertisementList.size(); i++) {
							view = inflater.inflate(R.layout.mall_detail_viewpager_item, null);
							mViewImage = (ImageView) view
									.findViewById(R.id.mall_detail_viewpager_image);
							if (!Constants.isNull(mAdvertisementList.get(i).get("Images"))) {
								mBitmapUtils.display(mViewImage, mAdvertisementList.get(i).get("Images").toString());
							}
							mViewList.add(view);
						}
						mViewPagerAdapter = new MainViewPagerAdapter(getApplicationContext(),
								mViewList, mAdvertisementList);
						mViewPager.setAdapter(mViewPagerAdapter);
					}else{
						intent.setClass(getApplicationContext(),
								NewMainActivity.class);
						startActivity(intent);
						finish();
					}
					break;
				case 2:
//					Toast.makeText(getApplicationContext(),"网络比较缓慢，请确认网络连接良好",Toast.LENGTH_SHORT).show();

						isStart = false;
						intent.setClass(getApplicationContext(),
								NewMainActivity.class);
						startActivity(intent);
						finish();

					break;
				case 3:
					if(tiem>0) {
						tiem--;
						mTimeText.setText(tiem + "");
						if (tiem == 0) {
							if(isStart) {
								isStart = false;
								intent.setClass(getApplicationContext(),
										NewMainActivity.class);
								startActivity(intent);
								finish();
							}
						}
					}
					break;
			}
		}
	};
	/**
     * 初始化底部小点
     */
//    private void initPoint() {
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guide_ll);
//
//        points = new ImageView[mFragments.size()];
//
//        // 循环取得小点图片
//        for (int i = 0; i < mFragments.size(); i++) {
//            // 得到一个LinearLayout下面的每一个子元素
//            points[i] = (ImageView) linearLayout.getChildAt(i);
//            // 默认都设为灰色
//            points[i].setEnabled(true);
//            // 给每个小点设置监听
//            points[i].setOnClickListener(this);
//            // 设置位置tag，方便取出与当前位置对应
//            points[i].setTag(i);
//        }
//
//        // 设置当面默认的位置
//        currentIndex = 0;
//        // 设置为白色，即选中状态
//        points[currentIndex].setEnabled(false);
//    }
	
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

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
//		int position = (Integer) v.getTag();
//        setCurView(position);
//        setCurDot(position);
		switch (v.getId()){
			//跳过
			case R.id.guide_tiguo_layout:
				isStart = false;
				intent.setClass(getApplicationContext(),
						NewMainActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.next_btn:

				break;
		}
	}
	/**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= mFragments.size()) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }
    /**
     * 设置当前的小点的位置
     */
//    private void setCurDot(int positon) {
//        if (positon < 0 || positon > mFragments.size() - 1 || currentIndex == positon) {
//            return;
//        }
//        points[positon].setEnabled(false);
//        points[currentIndex].setEnabled(true);
//
//        currentIndex = positon;
//    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		if(mAdvertisementList!=null) {
			if (position  == mAdvertisementList.size()-1) {
				mNextBtn.setVisibility(View.VISIBLE);
			}else{
				mNextBtn.setVisibility(View.GONE);
			}
		}
		Log.i("滑动",position+"============="+arg1+"===="+arg2);
	}

	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
//        setCurDot(arg0);
	}
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
