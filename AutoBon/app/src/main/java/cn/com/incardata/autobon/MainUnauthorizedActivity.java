package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.OrderMsg;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.utils.T;

/**
 * 未认证主页
 * @author wanghao
 */
public class MainUnauthorizedActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener{
    private int requestCode = 0x10;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;

    private TextView mAuthorization,receive_indent;
    private boolean isVerifying;//是否正在审核

    private int orderId;
    private OrderInfo orderInfo;
    /**
     * 认证通过
     */
    private boolean isVerified;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unauthorized);
        MyApplication.setMainForego(true);
        fragmentManager = getFragmentManager();
        isVerifying = getIntent().getExtras().getBoolean("isVerifying", false);
        isVerified = false;
        init();
    }

    private void init() {
        mAuthorization = (TextView) findViewById(R.id.start_authorization);
        receive_indent = (TextView) findViewById(R.id.receive_indent);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (isVerifying){
            mAuthorization.setText(R.string.authorization_progress);
        }
        mAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInvoke();
            }
        });
        receive_indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerified){
                    startActivity(MainAuthorizedActivity.class);
                    finish();
                    return;
                }else {
                    T.show(getContext(),getString(R.string.authorize_pass_take_order));
                    return;
                }
            }
        });

        transaction = fragmentManager.beginTransaction();
        mFragment = new IndentMapFragment();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
    }



    /**\
     * 通过订单Id获取订单详情
     */

    public void getOrerInfo(){
        showDialog();
        Http.getInstance().getTaskToken(NetURL.getOrderInfoV2(orderId), "", OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(),R.string.gain_data_failed);
                    return;
                }
                if (entity instanceof OrderInfoEntity){
                    OrderInfoEntity orderInfoEntity = (OrderInfoEntity) entity;
                    if (orderInfoEntity.isStatus()){
                        orderInfo = JSON.parseObject(orderInfoEntity.getMessage().toString(),OrderInfo.class);
                        mFragment.setData(orderInfo);
                    }else {
                        T.show(getContext(),orderInfoEntity.getMessage().toString());
                    }
                }
            }
        });
    }

    private void onClickInvoke(){
        if (isVerified){
            startActivity(MainAuthorizedActivity.class);
            finish();
            return;
        }

        if (isVerifying){
            startActivity(AuthorizationProgressActivity.class);
        }else {
            Intent intent = new Intent(this, AuthorizeActivity.class);
            startActivityForResult(intent, requestCode);
        }
    }

    private final BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ActionType.ACTION_ORDER.equals(action)){
                if (mFragment != null){
                    String json = intent.getStringExtra(ActionType.EXTRA_DATA);
                    OrderMsg orderMsg = JSON.parseObject(json, OrderMsg.class);
                    orderId = orderMsg.getOrder().getId();
                    getOrerInfo();
//                    mFragment.setData(orderMsg.getOrder(), orderMsg.getOrder().getCooperator());
                }
            }else if (ActionType.ACTION_VERIFIED.equals(action)){
                isVerified = true;
                startActivity(MainAuthorizedActivity.class);
                finish();
                return;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mOrderReceiver, CustomIntentFilter.getOrderAndVerifiedIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mOrderReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK){
            isVerifying = true;
            mAuthorization.setText(R.string.authorization_progress);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                T.show(this, getString(R.string.again_to_exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
