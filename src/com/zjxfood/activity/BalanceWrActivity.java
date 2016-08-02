package com.zjxfood.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.project.util.Utils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zjxfood.activity.wxapi.WXEntryActivity;
import com.zjxfood.common.Constants;
import com.zjxfood.http.ReadJson;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/14.
 * 余额提现
 */
public class BalanceWrActivity extends AppActivity implements View.OnClickListener {
    private RelativeLayout mTitleLayout;
    private ImageView mBackIamge;
    private TextView mTitleText;
    private FrameLayout mWxLayout;
    private TextView mGetCodeText, mTextCode;
    private TextView mPriceText, mRealName, mPhoneText;
    private String code = "";
    private Timer mTimer;
    private int n = 60;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    String code1 = null;
    private HashMap<String, Object> mWxMaps;
    private HashMap<String, Object> mWxUsersMaps;
    private HashMap<String, Object> mWxMap;
    private HashMap<String, Object> mLogMaps;
    private Button mTxBtn;
    private ImageView mHeadImage;
    private com.lidroid.xutils.BitmapUtils mBitmapUtils;
    private TextView mWxName;
    private RelativeLayout mTsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_take_money);
        setImmerseLayout(findViewById(R.id.head_layout));
// 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID2, false);
        api.registerApp(Constants.APP_ID2);
        init();
        mBitmapUtils = BitmapUtilSingle.getBitmapInstance(getApplicationContext());
        if (Constants.openid != null) {
            if (Constants.nickname != null) {
                mWxName.setText(Constants.nickname);
            }
            if (Constants.wxheadimgurl != null) {
                mBitmapUtils.display(mHeadImage, Constants.wxheadimgurl);
            }
        }
        Log.i("登录号码", Constants.mUserName + "===================");
        registerBoradcastReceiver();
    }

    private void init() {
        mTitleLayout = (RelativeLayout) findViewById(R.id.main_take_money);
        mBackIamge = (ImageView) mTitleLayout.findViewById(R.id.title_back_image);
        mTitleText = (TextView) mTitleLayout.findViewById(R.id.title_text);
        mTitleText.setText("余额提现");
        mWxLayout = (FrameLayout) findViewById(R.id.wx_log_layout);
        mGetCodeText = (TextView) findViewById(R.id.get_code_text);
        mTextCode = (TextView) findViewById(R.id.get_code_text_time);
        mPriceText = (TextView) findViewById(R.id.user_takemoney_edit);
        mRealName = (TextView) findViewById(R.id.user_name_edt);
        mPhoneText = (TextView) findViewById(R.id.user_phone_edt);
        mTxBtn = (Button) findViewById(R.id.user_takemoney_submit);
        mHeadImage = (ImageView) findViewById(R.id.weixin_login_ig);
        mWxName = (TextView) findViewById(R.id.weixin_login_text);
        mTsLayout = (RelativeLayout) findViewById(R.id.take_tishi_layout);

        mTsLayout.setOnClickListener(this);
        mBackIamge.setOnClickListener(this);
        mWxLayout.setOnClickListener(this);
        mGetCodeText.setOnClickListener(this);
        mTxBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle;
        switch (v.getId()) {
            //温馨提示
           case R.id.take_tishi_layout:
               intent.setClass(getApplicationContext(),WebActivity.class);
               bundle = new Bundle();
               bundle.putString("title","温馨提示");
               bundle.putString("url",Constants.wxts);
               intent.putExtras(bundle);
               startActivity(intent);
            break;
            //提现
            case R.id.user_takemoney_submit:
                if (!mPriceText.getText().toString().equals("")) {
                    if (!mRealName.getText().toString().equals("")) {
                        if (!mPhoneText.getText().toString().equals("")) {
                            if (!mTextCode.getText().toString().equals("")) {
                                if (Constants.nickname != null && Constants.openid != null) {
                                    getBalance();
                                } else {
                                    Toast.makeText(getApplicationContext(), "请登录微信！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "请输入联系电话", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入真实姓名", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入提现金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.title_back_image:
                finish();
                break;
            //获取验证码
            case R.id.get_code_text:
                if (Constants.mUserName != null) {
                    sendCode();
                }
                break;
            //微信登录
            case R.id.wx_log_layout:
                if (Constants.openid == null) {
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                } else {
                    Toast.makeText(getApplicationContext(), "已经登录过了", Toast.LENGTH_SHORT).show();
                }
                Log.i("tag", "微信登录======================openid=" + Constants.openid);

                break;
        }
    }

    //获取授权后的code
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            code1 = intent.getExtras().get("code").toString();
            Constants.code = intent.getExtras().get("code").toString();
            Log.i("code", Constants.code + "====================");
            new Thread(gettoken).start();
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(WXEntryActivity.action);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    // 根据code得到access_token、openid
    Runnable gettoken = new Runnable() {
        @Override
        public void run() {
            try {
                String strr = Constants.gettoken + "appid=" + Constants.APP_ID2
                        + "&secret=" + Constants.AppSecret2 + "&code="
                        + Constants.code + "&grant_type=authorization_code";
                String json = ReadJson.readParse(strr);
                mWxMaps = Constants.getJson2Object(json);
                Log.i("WX",
                        "================"
                                + mWxMaps.get("access_token").toString());
                Constants.access_token = mWxMaps.get("access_token")
                        .toString();
                Constants.openid = mWxMaps.get("openid").toString();
                Log.i("WX_CODE", "================" + mWxMaps);
//                Toast.makeText(getApplication(),Constants.openid,Toast.LENGTH_SHORT).show();
//				Toast.makeText(getApplicationContext(),mUserMapLists+"===",Toast.LENGTH_SHORT).show();
//				handler.sendEmptyMessageDelayed(7, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessageDelayed(3, 0);
        }
    };
    // 根据access_token、openid得到用户信息
    Runnable userinfo = new Runnable() {
        @Override
        public void run() {
            try {
                String strr = Constants.userinfo + "access_token="
                        + Constants.access_token + "&openid="
                        + Constants.openid;
                String json = ReadJson.readParse(strr);
//                 Log.i("mUserMapLists", "================" + json);
                mWxUsersMaps = Constants.getJson2Object(json);
                if (mWxUsersMaps.get("nickname") != null) {
                    Constants.nickname = mWxUsersMaps.get("nickname").toString();
                }
                if (mWxUsersMaps.get("headimgurl") != null) {
                    Constants.wxheadimgurl = mWxUsersMaps.get("headimgurl").toString();
                }
                Log.i("userinfo", "================" + mWxUsersMaps);
                handler.sendEmptyMessageDelayed(4, 0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    // 发送验证码
    private void sendCode() {
        if (Utils.checkPhoneNum(mPhoneText.getText().toString())) {
            Random random = new Random();
            String result = "";
            for (int i = 0; i < 6; i++) {
                result += random.nextInt(10);
            }
            code = result;
            Log.i("验证码", code + "===========code============");
            mTimer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    n--;
                    if (n <= 0) {
                        handler.sendEmptyMessageDelayed(2, 0);
                    } else {
                        handler.sendEmptyMessageDelayed(1, 0);
                    }
                }
            };
            mTimer.schedule(task, 60, 1000);
            StringBuilder sb = new StringBuilder();
            sb.append("mobile=" + Constants.mUserName + "&code=" + code);
            XutilsUtils.get(Constants.sendCode3, sb,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> res) {
                            if (res.result.equals("true")) {
                                handler.sendEmptyMessageDelayed(2, 0);
                            } else {
                                Log.i("codejson", "=========验证码发送============");
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "手机号码格式不正确！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //余额提现
    private void getBalance() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId=" + Constants.mId + "&money=" + mPriceText.getText() + "&realName=" + mRealName.getText() + "&mobile=" + mPhoneText.getText() + "&wxNickName=" + Constants.nickname + "&wxOpenId=" + Constants.openid + "&vCode=" + code);
        XutilsUtils.get(Constants.getBalance, sb,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> res) {
                        mWxMap = Constants.getJsonObject(res.result);
                        if (mWxMap != null && mWxMap.size() > 0) {
                            handler.sendEmptyMessageDelayed(5, 0);
                        }
                        Log.i("余额提现", res.result + "================");
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mTextCode.setText(n + "");
                    mTextCode.setVisibility(View.VISIBLE);
                    mGetCodeText.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case 2:
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                        mTimer = null;
                        mGetCodeText.setTextColor(getResources().getColor(
                                R.color.main_title_color));
                        n = 60;
                        mTextCode.setVisibility(View.GONE);
                    }
                    break;
                case 3:
//                    Log.i("userinfo", Constants.openid+"=========unionid=======" + Constants.unionid+"====="+Constants.nickname);
                    new Thread(userinfo).start();
                    break;
                case 4:
                    Log.i("userinfo", "=====" + mWxUsersMaps);
                    if (mWxUsersMaps.get("headimgurl") != null) {
                        mBitmapUtils.display(mHeadImage, mWxUsersMaps.get("headimgurl").toString());
                    }
                    if (mWxUsersMaps.get("nickname") != null) {
                        mWxName.setText(mWxUsersMaps.get("nickname").toString());
                    }
//                    Toast.makeText(getApplicationContext(),Constants.openid,Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    if(mWxMap.get("Code")!=null){
                        if(mWxMap.get("Code").toString().equals("200")){
                            Toast.makeText(getApplicationContext(),mWxMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),mWxMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

}
