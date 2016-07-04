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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.OrderMsg;
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

    private TextView mAuthorization;
    private boolean isVerifying;//是否正在审核
    /**
     * 认证通过
     */
    private boolean isVerified;

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
        if (isVerifying){
            mAuthorization.setText(R.string.authorization_progress);
        }
        mAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInvoke();
            }
        });

        transaction = fragmentManager.beginTransaction();
        mFragment = new IndentMapFragment();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
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
                    mFragment.setData(orderMsg.getOrder(), orderMsg.getOrder().getCooperator());
                }
            }else if (ActionType.ACTION_VERIFIED.equals(action)){
                isVerified = true;
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
