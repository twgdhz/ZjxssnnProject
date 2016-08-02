package com.zjxfood.delete.list;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActionSheet {
	private static String mAddressId = "";
	private static OnActionSheetSelected mActionSheetSelected;
	private static Dialog dlg;
	private static Context mContext;
	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionSheet() {
	}

	public static Dialog showSheet(Context context, final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener,String addressId) {
		mAddressId = addressId;
		mActionSheetSelected = actionSheetSelected;
		dlg = new Dialog(context, R.style.ActionSheet);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView mContent = (TextView) layout.findViewById(R.id.content);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);

		mContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("delete", "======delete==========");
//				actionSheetSelected.onClick(0);
//				dlg.dismiss();
				if(!(mAddressId.equals(""))){
				deleteAddress();
				}
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}
	
	private static void deleteAddress(){
		RequestParams params = new RequestParams();
		params.put("addressid", mAddressId);
		AsyUtils.get(Constants.deleteAddress2, params, new AsyncHttpResponseHandler(){
			@Override
			@Deprecated
			public void onSuccess(String content) {
				Log.i("content", content+"==============");
				if (content.equals("1")) {
					handler.sendEmptyMessageDelayed(1, 0);
				} else if (content.equals("0")) {
					handler.sendEmptyMessageDelayed(2, 0);
				} else if (content.equals("-1")) {
					handler.sendEmptyMessageDelayed(3, 0);
				}
				super.onSuccess(content);
			}
		});
	}

	static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mActionSheetSelected.onClick(0);
				dlg.dismiss();
				Toast.makeText(mContext, "删除成功！",
						Toast.LENGTH_SHORT).show();
				break;

			case 2:
				dlg.dismiss();
				Toast.makeText(mContext, "删除失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				dlg.dismiss();
				Toast.makeText(mContext, "删除失败，该地址正在使用中！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
}
