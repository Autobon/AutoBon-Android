package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;

/**
 * 未抢订单详情
 *
 * @author wanghao
 */
public class WaitOrderInfoActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;

    private Button immediateOrder;

    private OrderInfo orderInfo;

    private TextView collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order_info);
        fragmentManager = getFragmentManager();
        initView();
        fillData();
    }

    private void initView() {
        immediateOrder = (Button) findViewById(R.id.immediate_order);

        collection = (TextView) findViewById(R.id.collection);

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
        orderInfo = getIntent().getExtras().getParcelable(AutoCon.ORDER_INFO);


        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionShop();
            }
        });
    }

    /**
     * 收藏商户方法
     */
    private void collectionShop(){
        showDialog();
        Http.getInstance().postTaskToken(NetURL.deleteCollectionShop(orderInfo.getCoopId()), "", BaseEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(),R.string.request_failed);
                    return;

                }
                if (entity instanceof BaseEntity){
                    BaseEntity entity1 = (BaseEntity) entity;
                    if (entity1.isResult()){
                        T.show(getContext(),"收藏商户成功");
                        WaitOrderActivity.isGetCollectionShop = true;
                    }else {
                        T.show(getContext(),entity1.getMessage());
                    }
                }

            }
        });
    }

    private void fillData() {
        transaction = fragmentManager.beginTransaction();
        mFragment = new IndentMapFragment();
        transaction.replace(R.id.order_container, mFragment);
        transaction.commit();
        if (mFragment != null && orderInfo != null) {
            mFragment.setData(orderInfo);
        }
    }



    /**
     * 抢单
     */
    private void immediateOrder() {
        showDialog(getString(R.string.order_receiving_process));
        Http.getInstance().postTaskToken(NetURL.TAKEUPV2, TakeupEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.immediate_order_failed);
                    return;
                }
                if (entity instanceof TakeupEntity) {
                    TakeupEntity takeup = (TakeupEntity) entity;
                    if (takeup.isStatus()) {
                        OrderInfo orderInfo = JSON.parseObject(takeup.getMessage().toString(), OrderInfo.class);
                        MyApplication.isRefresh = true;
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, orderInfo.getId());
                        bundle.putString("OrderNum", orderInfo.getOrderNum());
                        setResult(0x11);
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        finish();
                        return;
                    } else {
                        T.show(getContext(), takeup.getMessage().toString());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId())));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
