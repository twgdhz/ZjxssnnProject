package com.zjxfood.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjxfood.adapter.CreateOrderAdapter;

/**
 * Created by Administrator on 2016/6/3.
 * 创建商城订单
 */
public class CreateOrderActivity extends AppActivity implements View.OnClickListener{

    private RelativeLayout mTitleLayout;
    private TextView mTitleText;
    private ImageView mBackImage;
    private ListView mListView;
    private CreateOrderAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mall_order_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        init();
        mAdapter = new CreateOrderAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
    }

    private void init(){
        mBackImage = (ImageView) findViewById(R.id.title_back_image);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("创建订单");
        mListView = (ListView) findViewById(R.id.create_list);

        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back_image:
                finish();
                break;
        }
    }
}
