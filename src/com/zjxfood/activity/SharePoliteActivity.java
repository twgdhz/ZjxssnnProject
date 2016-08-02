package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.DensityUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.SharePopup;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享有礼
 * Created by Administrator on 2016/6/16.
 */
public class SharePoliteActivity extends AppActivity implements View.OnClickListener{

    private LinearLayout mWxLayout,mPyLayout,mMdmLayout;
    private TextView mTitleText;
    private ImageView mBackImage;
    private SharePopup mPopup;
    private ArrayList<HashMap<String,Object>> mSettingList;
    private ImageView mBgImage;
    private com.lidroid.xutils.BitmapUtils bitmapUtils;
    private RelativeLayout mRuleLayout;
    private TextView mDesText;
    private IWXAPI api;
    private TextView mShareText;
    private HashMap<String,Object> mTjMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_polite_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID2);
        bitmapUtils = BitmapUtilSingle
                .getBitmapInstance(getApplicationContext());
        bitmapUtils.display(mBgImage,"http://img.zjxssnn.com/ShareImage/share_bg.jpg");
        getSetting();
        getTjSy();
    }

    private  void init(){
        mShareText = (TextView) findViewById(R.id.share_value);
        mDesText = (TextView) findViewById(R.id.share_des_text);
        mBgImage = (ImageView) findViewById(R.id.share_bg_image);
        ViewGroup.LayoutParams params = mBgImage.getLayoutParams();
        params.height = DensityUtils.dp2px(getApplicationContext(),180);
        mBgImage.setLayoutParams(params);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("分享有礼");
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mWxLayout = (LinearLayout) findViewById(R.id.share_fre_layout);
        mPyLayout = (LinearLayout) findViewById(R.id.share_py_layout);
        mMdmLayout = (LinearLayout) findViewById(R.id.share_mdm_layout);
        mRuleLayout = (RelativeLayout) findViewById(R.id.share_rule_layout);

        mRuleLayout.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
        mWxLayout.setOnClickListener(this);
        mPyLayout.setOnClickListener(this);
        mMdmLayout.setOnClickListener(this);
    }

    private void getSetting() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        XutilsUtils.get(Constants.getSetting, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mSettingList = Constants.getJsonArray(res.result);
                        if(mSettingList.get(14).get("Content")!=null) {
                            mDesText.setText(Html.fromHtml(mSettingList.get(14).get("Content").toString()));
                        }
                    }
                });
    }
    //获取推荐人数和收益
    private void getTjSy() {
        StringBuilder sb = new StringBuilder();
        sb.append("userid=" + Constants.mId);
        XutilsUtils.get(Constants.getTjRy, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        Log.i("用户收益",res.result+"===================");
                        mTjMap = Constants.getJsonObjectByData(res.result);
                        if(mTjMap!=null && mTjMap.size()>0){
                            handler.sendEmptyMessageDelayed(1,0);
                        }
                    }
                });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(mTjMap.get("Num")!=null && mTjMap.get("Money")!=null){
                        mShareText.setText("已成功推荐"+mTjMap.get("Num")+"人，累计获得收益"+mTjMap.get("Money")+"元");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (v.getId()){
            case R.id.share_rule_layout:
                intent.setClass(getApplicationContext(),WebActivity.class);
                bundle = new Bundle();
                bundle.putString("title","奖励规则");
                bundle.putString("url",Constants.jlgz);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.title_back_image:
                finish();
                break;
            //微信好友分享
            case R.id.share_fre_layout:
//                showShare();
                wxShareHy();
                break;
            //微信朋友圈分享
            case R.id.share_py_layout:
//                showShare();
                wxShare();
                break;
            case R.id.share_mdm_layout:
                if (!Constants.mId.equals("")) {
                    mPopup = new SharePopup(SharePoliteActivity.this);
                    mPopup.showAtLocation(mBackImage,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
        }
    }
    //朋友圈分享
    private void wxShare(){


        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://m.zjxssnn.com/pages/my/bind.html?pid="+Constants.mId;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if(mSettingList.get(13)!=null) {
            msg.title = mSettingList.get(13).get("Title").toString();
            msg.description = mSettingList.get(13).get("Content").toString();
        }
        try
        {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }
    //好友分享
    private void wxShareHy(){

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://m.zjxssnn.com/pages/my/bind.html?pid="+Constants.mId;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if(mSettingList.get(13)!=null) {
            msg.title = mSettingList.get(13).get("Title").toString();
            msg.description = mSettingList.get(13).get("Content").toString();
        }
        try
        {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    private void showShare() {
        ShareSDK.initSDK(this,"a793e5e7dcce");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if(mSettingList.get(13)!=null) {
            oks.setTitle(mSettingList.get(13).get("Title").toString());
            oks.setText(mSettingList.get(13).get("Content").toString());
        }
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://m.zjxssnn.com/pages/my/bind.html?pid="+Constants.mId);
        // text是分享文本，所有平台都需要这个字段

        oks.setImageUrl("http://m.zjxssnn.com/images/share/share-logo.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://m.zjxssnn.com/pages/my/bind.html?pid="+Constants.mId);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("分享有礼");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
