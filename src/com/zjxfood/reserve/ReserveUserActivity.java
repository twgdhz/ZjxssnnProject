package com.zjxfood.reserve;

import java.util.Calendar;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.wheel.adapter.NumericWheelAdapter;
import com.zjxfood.wheel.view.OnWheelScrollListener;
import com.zjxfood.wheel.view.WheelView;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReserveUserActivity extends Activity implements OnClickListener{

	private ImageView mOrderImage;
	private TextView mOrderPirce;
	private Button mSubmitOrderBtn;
	private Bitmap mBitmap;
	private ImageView mBackImage;
	private LinearLayout mDataPickLayout;
	private WheelView year;
	private WheelView month;
	private WheelView day;
//	private WheelView time;
	private WheelView min;
	private WheelView sec;
	private View view=null;
	private int mYear=2015;
//	private int mMonth=10;
//	private int mDay=1;
	private LayoutInflater inflater = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_order_user_info_layout);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		init();
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		Resources res = getResources();
		mBitmap = BitmapFactory.decodeResource(res, R.drawable.after_booking);
		mOrderImage.setImageBitmap(createCornerBitmap(mBitmap, 5));
		
		mDataPickLayout.addView(getDataPick());
	}
	
	private void init(){
		mOrderImage = (ImageView) findViewById(R.id.reserve_user_order_bottom_image);
		mOrderPirce = (TextView) findViewById(R.id.reserve_user_bottom_text);
		mSubmitOrderBtn = (Button) findViewById(R.id.reserve_user_bottom_chose_btn);
		mBackImage = (ImageView) findViewById(R.id.reserve_user_info_info_image);
		mDataPickLayout = (LinearLayout) findViewById(R.id.reserve_order_user_date_select_layout);
		mSubmitOrderBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reserve_user_bottom_chose_btn:
			
			break;

		case R.id.reserve_user_info_info_image:
			finish();
			break;
		}
	}
	
	private Bitmap createCornerBitmap(Bitmap oldBitmap, int num) {
		// 新建画布
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		// --->先画原来的图片
		Paint bitmapPaint = new Paint();
		// 防止抖动
		bitmapPaint.setDither(true);
		// 对Bitmap进行滤波处理
		bitmapPaint.setFilterBitmap(true);
		Rect src = new Rect(0, 0, oldBitmap.getWidth(), oldBitmap.getHeight());
		Rect dst = new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
		canvas.drawBitmap(oldBitmap, src, dst, bitmapPaint);
		// 画圆 设置成数字的背景
		Paint paintCircle = new Paint(); // 设置一个笔刷大小是3的红色色的画笔
		paintCircle.setColor(Color.RED);
		paintCircle.setStrokeJoin(Paint.Join.ROUND);
		paintCircle.setStrokeCap(Paint.Cap.ROUND);
		paintCircle.setStrokeWidth(3);
		canvas.drawCircle(
				width / 2 + DensityUtils.dp2px(getApplicationContext(), 35),
				height / 3 - DensityUtils.dp2px(getApplicationContext(), 15),
				DensityUtils.dp2px(getApplicationContext(), 15), paintCircle);
		// --->再画新加的数字
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setTextSize(DensityUtils.dp2px(getApplicationContext(), 20));
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(num + "",
				width / 2 + DensityUtils.dp2px(getApplicationContext(), 30),
				height / 3 - DensityUtils.dp2px(getApplicationContext(), 8),
				countPaint);
		return newBitmap;
	}
	
	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH)+1;//通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		
		int curYear = mYear;
//		int curMonth =mMonth+1;
//		int curDate = mDay;
		
		view = inflater.inflate(R.layout.wheel_date_picker, null);
		
		year = (WheelView) view.findViewById(R.id.year);
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(this,2010, norYear+10); 
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);//是否可循环滑动
		year.addScrollingListener(scrollListener);
		
		month = (WheelView) view.findViewById(R.id.month);
		NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(this,1, 12, "%02d"); 
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		
		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear,curMonth);
		day.setCyclic(true);
		
//		time= (WheelView) view.findViewById(R.id.time);
//		String[] times = {"上午","下午"} ;
//		ArrayWheelAdapter<String> arrayWheelAdapter=new ArrayWheelAdapter<String>(MainActivity.this,times );
//		time.setViewAdapter(arrayWheelAdapter);
//		time.setCyclic(false);
//		time.addScrollingListener(scrollListener);
		
		min = (WheelView) view.findViewById(R.id.min);
		NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(this,1, 23, "%02d"); 
		numericWheelAdapter3.setLabel("时");
		min.setViewAdapter(numericWheelAdapter3);
		min.setCyclic(true);
		min.addScrollingListener(scrollListener);
		
		sec = (WheelView) view.findViewById(R.id.sec);
		NumericWheelAdapter numericWheelAdapter4=new NumericWheelAdapter(this,1, 59, "%02d"); 
		numericWheelAdapter4.setLabel("分");
		sec.setViewAdapter(numericWheelAdapter4);
		sec.setCyclic(true);
		sec.addScrollingListener(scrollListener);
		
		
		year.setVisibleItems(7);//设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);
//		time.setVisibleItems(7);
		min.setVisibleItems(7);
		sec.setVisibleItems(7);
		
		year.setCurrentItem(curYear - 2010);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		
		return view;
	}
	
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {
		}
		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 2010;//年
			int n_month = month.getCurrentItem() + 1;//月
			Log.i("year.getCurrentItem()", (year.getCurrentItem()+2010)+"=======年========");
			Log.i("month.getCurrentItem()", (month.getCurrentItem()+1)+"=======月========");
			Log.i("day.getCurrentItem()", (day.getCurrentItem()+1)+"========日=======");
			Log.i("min.getCurrentItem()", (min.getCurrentItem()+1)+"========时=======");
			Log.i("sec.getCurrentItem()", (sec.getCurrentItem()+1)+"========分=======");
			initDay(n_year,n_month);
			
			String birthday=new StringBuilder().append((year.getCurrentItem()+2010)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
//			tv1.setText("年龄             "+calculateDatePoor(birthday)+"岁");
//			tv2.setText("星座             "+getAstro(month.getCurrentItem() + 1,day.getCurrentItem()+1));
		}
	};
	
	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
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
