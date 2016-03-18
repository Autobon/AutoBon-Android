package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.OrderMsg;

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
    private boolean isVerifying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unauthorized);

        fragmentManager = getFragmentManager();
        isVerifying = getIntent().getExtras().getBoolean("isVerifying", false);
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
                    mFragment.setData(orderMsg.getOrder());
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mOrderReceiver, CustomIntentFilter.getOrderIntentFilter());
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
}
