package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import cn.com.incardata.adapter.OrderUnfinishedAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.OrderMsg;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 已认证的
 * @author wanghao
 */
public class MainAuthorizedActivity extends BaseActivity implements View.OnClickListener, IndentMapFragment.OnFragmentInteractionListener{
    private final static String pageSize = "20";
    private FragmentManager fragmentManager;
    private IndentMapFragment mFragment;

    private TextView today;
    private PullToRefreshView mPull;
    private ListView mListView;
    private RelativeLayout orderLayout;
    private TextView orderType;
    private Button immediateOrder;

    private int orderId = -1;//订单ID－抢单
    private int page = 1;
    private boolean isRefresh = false;
    private OrderUnfinishedAdapter mAdapter;
    private ArrayList<OrderInfo_Data> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_authorized);
        init();
        initListView();
    }

    private void init() {
        today = (TextView) findViewById(R.id.today);
        mPull = (PullToRefreshView) findViewById(R.id.order_pull);
        mListView = (ListView) findViewById(R.id.unfinished_order_list);
        orderLayout = (RelativeLayout) findViewById(R.id.order_layout);
        orderType = (TextView) findViewById(R.id.order_type);
        immediateOrder = (Button) findViewById(R.id.immediate_order);

        findViewById(R.id.personal).setOnClickListener(this);
        findViewById(R.id.order_more).setOnClickListener(this);
        findViewById(R.id.order_close_window).setOnClickListener(this);
        immediateOrder.setOnClickListener(this);

        today.setText(DateCompute.getWeekOfDate());

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
            case R.id.order_more:
                startActivity(MoreActivity.class);
                break;
            case R.id.order_close_window:
                closeWindow();
                break;
            case R.id.immediate_order:
                immediateOrder();
                closeWindow();
                break;
        }
    }

    private void initListView() {
        mList = new ArrayList<OrderInfo_Data>();
        mAdapter = new OrderUnfinishedAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnClickOrderListener(new OrderUnfinishedAdapter.OnClickOrderListener() {
            @Override
            public void onClickOrder(int position) {
                intoOrder(position);
            }
        });

        mPull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getpageList(1);
            }
        });
        mPull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                getpageList(++page);
            }
        });
        getpageList(1);
    }

    private void intoOrder(int position){
        OrderInfo_Data orderInfo = mList.get(position);
        OrderInfo_Construction construction = null;
        if (MyApplication.getInstance().getUserId() == orderInfo.getMainTech().getId()){
            construction = orderInfo.getMainConstruct();
        }else {
            construction = orderInfo.getSecondConstruct();
        }

        if (construction == null){
            Intent intent = new Intent(this, OrderReceiveActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
            intent.putExtra(OrderReceiveActivity.IsLocalData, true);
            startActivity(intent);
        }else if (construction.getSigninTime() == null){
            Intent intent = new Intent(this, WorkSignInActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
            startActivity(intent);
        }else if (construction.getBeforePhotos() == null){
            //进入工作前照片上传
            Intent intent = new Intent(this, WorkBeforeActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO,orderInfo);
            startActivity(intent);
        }else if (construction.getAfterPhotos() == null){
            //进入工作后照片上传
            Intent intent = new Intent(this, WorkFinishActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO,orderInfo);
            startActivity(intent);
        }
    }

    private void getpageList(int page) {
        Http.getInstance().getTaskToken(NetURL.UNFINISHED_ORDER_LIST, "page=" + page + "&pageSize=5", ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                mPull.loadedCompleted();
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failure);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedEntity){
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isResult()){
                        if (isRefresh) {
                            mList.clear();
                        }
                        mList.addAll(list.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    }else {
                        T.show(getContext(), R.string.loading_data_failure);
                    }
                    isRefresh = false;
                }
            }
        });
    }

    /**
     * 抢单
     */
    private void immediateOrder() {
        Http.getInstance().postTaskToken(NetURL.TAKEUP, TakeupEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.immediate_order_failed);
                    return;
                }
                if (entity instanceof TakeupEntity){
                    TakeupEntity takeup = (TakeupEntity) entity;
                    if (takeup.isResult()){
//                        T.show(getContext(), R.string.immediate_order_success);
                        page = 1;
                        isRefresh = true;
                        getpageList(1);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, takeup.getData().getId());
                        bundle.putString("OrderNum", takeup.getData().getOrderNum());
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        return;
                    }
                    if ("ORDER_TAKEN_UP".equals(takeup.getError())){
                        T.show(getContext(), R.string.order_taken_up);
                        return;
                    }else if ("ORDER_CANCELED".equals(takeup.getError())){
                        T.show(getContext(), R.string.order_canceled);
                        return;
                    }else {
                        T.show(getContext(), R.string.immediate_order_failed);
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)));
    }

    private void closeWindow(){
        orderLayout.setVisibility(View.GONE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(mFragment).commit();
    }

    private void showWindow(){
        orderLayout.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(mFragment).commit();
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
                    orderId = orderMsg.getOrder().getId();
                    orderType.setText(MyApplication.getInstance().getSkill(orderMsg.getOrder().getOrderType()));
                    showWindow();
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
