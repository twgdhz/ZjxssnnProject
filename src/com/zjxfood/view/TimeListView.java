package com.zjxfood.view;

import com.zjxfood.activity.R;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimeListView extends RelativeLayout implements Runnable {

	private TextView timedown_day, timedown_hour, timedown_min,
			timedown_second;
	private Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
	private int[] times,time2;
	private long mday, mhour, mmin, msecond;// 天，小时，分钟，秒
	private boolean run = false; // 是否启动了
	private boolean isRun = true;
	private TextView mTypeText;
	private String startEnd;
	private String position;

	public TimeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	public TimeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TimeListView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.timedown_layout, TimeListView.this);
		timedown_day = (TextView) this.findViewById(R.id.timedown_day);
		timedown_hour = (TextView) this.findViewById(R.id.timedown_hour);
		timedown_min = (TextView) this.findViewById(R.id.timedown_min);
		timedown_second = (TextView) this.findViewById(R.id.timedown_second);
		mTypeText = (TextView) this.findViewById(R.id.time_type_text);

		mPaint = new Paint();
	}

	public int[] getTimes() {
		return times;
	}
	public void setType(String type){
		mTypeText.setText(type);
	}
	public void setIsStart(String str){
		startEnd = str;
	}
	public void setIsTime(int[] time){
		this.time2 = time;
	}

	public void setTimes(int[] times) {
		isRun = true;
		this.times = times;
		mday = times[0];
		mhour = times[1];
		mmin = times[2];
		msecond = times[3];
	
	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		
			msecond--;
			if (msecond < 0) {
				mmin--;
				msecond = 59;
				if (mmin < 0) {
					mmin = 59;
					mhour--;
					if (mhour < 0) {
						// 倒计时结束
						mday=0;
						mhour=0;
						mmin=0;
						msecond=0;
						if(startEnd!=null){
						if(startEnd.equals("nostart")){
							setType("竞拍中");
						}else if(startEnd.equals("isstart")){
							setType("已结束");
//							isRun = false;
							end();
						}
						}
						return;
					}
				}
		}
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	@Override
	public void run() {
		// 标示已经启动
		run = true;
		ComputeTime();
		String strTime = "还剩" + mday + "天" + mhour + "小时" + mmin + "分钟"
				+ msecond + "秒";
		// this.setText(strTime);
		timedown_day.setText(mday + "");
		timedown_hour.setText(mhour + "");
		timedown_min.setText(mmin + "");
		timedown_second.setText(msecond + "");
		postDelayed(this, 1000);
//		}else{
//			Log.i("倒计时结束", "=======cancel===1===="+position);
//			cancel();
//			
//		}
	}
	

	public void cancel() {
		timedown_day.setText("00");
		timedown_hour.setText("00");
		timedown_min.setText("00");
		timedown_second.setText("00");
		// postDelayed(this, 1000);
		// this.removeCallbacks(this);
	}
	
	public void end(){
		timedown_day.setText("00");
		timedown_hour.setText("00");
		timedown_min.setText("00");
		timedown_second.setText("00");
		removeCallbacks(this);
	}
}
