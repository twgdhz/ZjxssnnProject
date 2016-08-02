package com.zjxfood.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zjxfood.activity.R;
import com.zjxfood.activity.WeixinPayActivity;
import com.zjxfood.common.Constants;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    public static final String action = "jason.broadcast.action";
    //    public static String ACTION_NAME = "code";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        Log.i("微信回调","wx==========0========");
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("微信回调结果", "==============回调结果================" + resp.errCode+"=====flag="+WeixinPayActivity.flag);
        switch (resp.errCode) {
            case 0:
                try {
                    if (WeixinPayActivity.flag.equals("0")) {
                        Log.i("log", "===========微信登录成功========");
                        SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                        String code = sendResp.code;
                        Intent mIntent = new Intent(action);
                        mIntent.putExtra("code", code);
                        // 发送广播
                        sendBroadcast(mIntent);
                        finish();
                    }
                }catch (Exception e){
                    finish();
                    e.printStackTrace();
                }
                break;

            default:
                finish();
                break;
        }
    }
}
