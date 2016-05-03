package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;

public class WaitOrderInfoActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private IndentMapFragment mFragment;

    private TextView orderType;
    private Button immediateOrder;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order_info);

        initView();
        fillData();
    }

    private void initView() {
        orderType = (TextView) findViewById(R.id.order_type);
        immediateOrder = (Button) findViewById(R.id.immediate_order);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        immediateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                immediateOrder();
            }
        });
        order = getIntent().getExtras().getParcelable("Order");
    }

    private void fillData() {
        fragmentManager = getFragmentManager();
        mFragment = new IndentMapFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.order_container, mFragment).commit();

        if (mFragment != null){
            mFragment.setData(order);
            orderType.setText(MyApplication.getInstance().getSkill(order.getOrderType()));
        }
    }

    /**
     * 抢单
     */
    private void immediateOrder() {
        showDialog(getString(R.string.order_receiving_process));
        Http.getInstance().postTaskToken(NetURL.TAKEUP, TakeupEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.immediate_order_failed);
                    return;
                }
                if (entity instanceof TakeupEntity){
                    TakeupEntity takeup = (TakeupEntity) entity;
                    if (takeup.isResult()){
                        MyApplication.isRefresh = true;
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, takeup.getData().getId());
                        bundle.putString("OrderNum", takeup.getData().getOrderNum());
                        setResult(0x11);
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        finish();
                        return;
                    }
                    if ("ORDER_TAKEN_UP".equals(takeup.getError())){
                        T.show(getContext(), R.string.order_taken_up);
                        return;
                    }else if ("ORDER_CANCELED".equals(takeup.getError())){
                        T.show(getContext(), R.string.order_canceled);
                        return;
                    }else {
                        T.show(getContext(), takeup.getMessage());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(order.getId())));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
