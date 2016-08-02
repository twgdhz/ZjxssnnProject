package com.zjxfood.view;

import com.zjxfood.interfaces.ScrollInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

public class MyWebView extends WebView{
	ScrollInterface mt;
	 private OnScrollChangedCallback mOnScrollChangedCallback;
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("hhah",""+l+" "+t+" "+oldl+" "+oldt);
//        mt.onSChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    
    public void setOnCustomScroolChangeListener(ScrollInterface t){
        this.mt=t;
    }
    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
