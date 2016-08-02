package com.project.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static String splitePhone(String phone) {
		String[] tel = new String[phone.length()];
		StringBuffer sb = new StringBuffer();
		if (tel.length > 0) {
			for (int i = 0; i < tel.length; i++) {
				if (i > 2 && i < 7) {
					sb.append("*");
				} else {
					sb.append(phone.charAt(i));
				}
			}
		}
		return sb.toString();
	}
	public static int[] formatTime(int ms) {
			int hour = (ms / 3600);
			int min = ((ms - hour * 3600) / 60);
			int senc = (ms - hour * 3600 - min * 60);
			int[] time = { 0, hour, min, senc };
			return time;
	}
	//时间比
	public static boolean dateByEnd(String startTime,String endTimeTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try{
		Date newStartTime = df.parse(startTime);
		Date newEndTime = df.parse(endTimeTime);
		time = (newStartTime.getTime()-newEndTime.getTime());
//		Log.i("time", time+"==============");
		}catch(Exception e){}
		if(time>0){
		return true;
		}else{
			return false;
		}
	}
	//判断是否开始
			public static boolean isStart(String startTime) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long time = 0;
				try{
				Date newStartTime = df.parse(startTime);
				Date newEndTime = new Date();
				time = (newStartTime.getTime()-newEndTime.getTime());
//				Log.i("time", time+"==============");
				}catch(Exception e){}
				if(time>0){
				return true;
				}else{
					return false;
				}
			}
			//判断是否结束
			public static boolean isEnd(String endTime) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					int time = 0;
					try{
					Date newEndTime = df.parse(endTime);
					Date currentTime = new Date();
					time = (int) (newEndTime.getTime()-currentTime.getTime());
					}catch(Exception e){}
					if(time>0){
					return true;
					}else{
						return false;
					}
				}
			public static int[] formatStartTime(String startTime) {
				int[] times = { 0, 0, 0, 0 };
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long time = 0;
				try{
				Date newStartTime = df.parse(startTime);
				Date newEndTime = new Date();
				time = (newStartTime.getTime()-newEndTime.getTime());
				}catch(Exception e){}
				if(time>0){
				 int day=(int) (time/(24*60*60*1000));
				int hour = (int) (time/(60*60*1000)-day*24);
				int min = (int) ((time/(60*1000))-day*24*60-hour*60);
				int senc = (int) (time/1000-day*24*60*60-hour*60*60-min*60);
				times[0] = 0;
				times[1] = hour;
				times[2] = min;
				times[3] = senc;
				return times;
				}else{
					return times;
				}
			}
			public static String subTime(String time) {
				String newStr = "";
				newStr = time.substring(10, time.length());
				return newStr;
			}
			
			public static Double[] getLalon(String str) {
				Double[] strs = new Double[2];
				int a = str.indexOf(",");
				if (a > 0 && a < str.length()) {
					strs[0] = Double.parseDouble(str.substring(0, a - 1));
					strs[1] = Double
							.parseDouble(str.substring(a + 1, str.length() - 1));
				}
				return strs;
			}
	//画图片角标
	public static Bitmap createCornerBitmap(Bitmap oldBitmap, int num, Context context) {
		// 新建画布
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
		Paint paintCircle = new Paint(); // 设置一个笔刷大小是3的红色的画笔
		paintCircle.setColor(Color.RED);
		paintCircle.setStrokeJoin(Paint.Join.ROUND);
		paintCircle.setStrokeCap(Paint.Cap.ROUND);
		paintCircle.setStrokeWidth(3);
		canvas.drawCircle(
				width / 2 + DensityUtils.dp2px(context, 35),
				height / 3 - DensityUtils.dp2px(context, 15),
				DensityUtils.dp2px(context, 35), paintCircle);
		// --->再画新加的数字
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setTextSize(DensityUtils.dp2px(context, 35));
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(num + "",
				width / 2 + DensityUtils.dp2px(context, 30),
				height / 3 - DensityUtils.dp2px(context, 8),
				countPaint);
		return newBitmap;
	}
	/**
	 * 判段手机号格式是否正确
	 *
	 * @param phoneNum
	 * @return boolean
	 */
	public static boolean checkPhoneNum(String phoneNum) {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNum);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}
}
