package com.zjxfood.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/16.
 */
public class WebActivity extends AppActivity implements View.OnClickListener{

    private ImageView mBackImage;
    private TextView mTitleText;
    private WebView mWebView;
    private Bundle mBundler;
    private String mTitle,mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        mBundler = getIntent().getExtras();
        if(mBundler!=null){
            mTitle = mBundler.getString("title");
            mUrl = mBundler.getString("url");
            mWebView.loadUrl(mUrl);
            mTitleText.setText(mTitle);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private void init(){
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mWebView = (WebView) findViewById(R.id.web_view);

        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_image:
                finish();
                break;
        }
    }
}
