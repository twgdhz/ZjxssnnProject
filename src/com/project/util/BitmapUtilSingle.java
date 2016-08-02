package com.project.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public class BitmapUtilSingle {

	private static BitmapUtils mBitmapUtils;

	private BitmapUtilSingle() {
	}

	public static BitmapUtils getBitmapInstance(Context context) {
		
		if (mBitmapUtils == null) {
			mBitmapUtils = new BitmapUtils(context);
		}
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setBitmapConfig(Bitmap.Config.RGB_565);
		config.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
		mBitmapUtils.configDefaultDisplayConfig(config);
		return mBitmapUtils;
	}
}
