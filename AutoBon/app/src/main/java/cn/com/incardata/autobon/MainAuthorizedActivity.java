package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.OrderMsg;

/**
 * 已认证的
 * @author wanghao
 */
public class MainAuthorizedActivity extends BaseActivity implements View.OnClickListener, IndentMapFragment.OnFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private IndentMapFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_authorized);
        init();
    }

    private void init() {
        findViewById(R.id.personal).setOnClickListener(this);

        fragmentManager = getFragmentManager();
        mFragment = new IndentMapFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.order_container, mFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personal:
                startActivity(MyInfoActivity.class);
                break;
        }
    }

    private final BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ActionType.ACTION_ORDER.equals(action)){
                if (mFragment != null){
                    String json = intent.getStringExtra(ActionType.NEW_ORDER);
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
    public void onFragmentInteraction(Uri uri) {

    }
}
