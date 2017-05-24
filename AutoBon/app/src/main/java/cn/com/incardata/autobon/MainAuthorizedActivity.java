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
import android.widget.AdapterView;
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
import cn.com.incardata.fragment.InvitationDialogFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.getui.CustomIntentFilter;
import cn.com.incardata.getui.InvitationMsg;
import cn.com.incardata.getui.OrderMsg;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListUnFinishOrder;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.ListUnfinishedOrderEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.http.response.UnfinishOrder;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 已认证的
 *
 * @author wanghao
 */
public class MainAuthorizedActivity extends BaseActivity implements View.OnClickListener, IndentMapFragment.OnFragmentInteractionListener {
    private final static String pageSize = "20";
    private FragmentManager fragmentManager;
    private IndentMapFragment mFragment;
//    private InvitationDialogFragment invitationDialogFragment;

    private TextView today;
    private PullToRefreshView mPull;
    private ListView mListView;
    private RelativeLayout orderLayout;
    private Button immediateOrder;

    private int orderId = -1;//订单ID－抢单
    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;
    private OrderUnfinishedAdapter mAdapter;
    private ArrayList<OrderInfo> mList;
    private OrderInfo orderInfo;

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
        immediateOrder = (Button) findViewById(R.id.immediate_order);

        findViewById(R.id.personal).setOnClickListener(this);
        findViewById(R.id.order_more).setOnClickListener(this);
        findViewById(R.id.order_close_window).setOnClickListener(this);
        immediateOrder.setOnClickListener(this);

        today.setText(DateCompute.getWeekOfDate());

        fragmentManager = getFragmentManager();
        mFragment = new IndentMapFragment().newInstance(false);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.order_container, mFragment).hide(mFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.order_more:
                startActivity(WaitOrderActivity.class);
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
        mList = new ArrayList<OrderInfo>();
        mAdapter = new OrderUnfinishedAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnClickOrderListener(new OrderUnfinishedAdapter.OnClickOrderListener() {
            @Override
            public void onClickOrder(int position) {
                intoOrder(position);
//                showDialog();
//                Http.getInstance().getTaskToken(NetURL.getOrderInfoV2(mList.get(position).getId()), "", OrderInfoEntity.class, new OnResult() {
//                    @Override
//                    public void onResult(Object entity) {
//                        cancelDialog();
//                        if (entity == null){
//                            return;
//                        }
//                        if (entity instanceof OrderInfoEntity && ((OrderInfoEntity) entity).isStatus()){
//                            intoOrder();
//                        }
//                    }
//                });
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainAuthorizedActivity.this, OrderReceiveActivity.class);
                intent.putExtra(AutoCon.ORDER_INFO, mList.get(i));
                intent.putExtra(OrderReceiveActivity.IsLocalData, true);
                startActivity(intent);
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
                if (page >= totalPages) {
                    T.show(getContext(), R.string.has_load_all_label);
                    mPull.loadedCompleted();
                    return;
                }
                getpageList(++page);
            }
        });
        getpageList(1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        isRefresh = true;
        if (mList.size() > 0){
            mList.clear();
        }
        getpageList(1);
    }

    private void intoOrder(int position) {

        if ("TAKEN_UP".equals(mList.get(position).getStatus())) {//进入开始工作
            Intent intent = new Intent(this, OrderReceiveActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
            intent.putExtra(OrderReceiveActivity.IsLocalData, true);
            startActivity(intent);
        } else if ("IN_PROGRESS".equals(mList.get(position).getStatus())) {//进入签到
            Intent intent = new Intent(this, WorkSignInActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
            startActivity(intent);
        } else if ("SIGNED_IN".equals(mList.get(position).getStatus())) {
            //进入工作前照片上传
            Intent intent = new Intent(this, WorkBeforeActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
            startActivity(intent);
        } else if ("AT_WORK".equals(mList.get(position).getStatus())) {
            //进入工作后照片上传
            Intent intent = new Intent(this, WorkFinishActivity.class);
            intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
            startActivity(intent);
        }
//        else if (ActionType.SIGNED_IN.equals(orderInfo.getStatus())){
//            T.show(getContext(), "您已完成此单，等待合伙人提交");
//        }
    }

    private void getpageList(int page) {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.ORDER_LIST, "status=2&page=" + page + "&pageSize=10", ListUnfinishedOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                mPull.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failure);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedOrderEntity) {
                    ListUnfinishedOrderEntity list = (ListUnfinishedOrderEntity) entity;
                    if (list.isStatus()) {
                        ListUnFinishOrder listUnFinishOrder = JSON.parseObject(list.getMessage().toString(), ListUnFinishOrder.class);
                        totalPages = listUnFinishOrder.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }
                        if (listUnFinishOrder.getNumberOfElements() == 0){
                            T.show(getContext(),getString(R.string.no_order));
                        }
                        for (OrderInfo orderInfo : listUnFinishOrder.getContent()) {
                            mList.add(orderInfo);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
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
                        page = 1;
                        isRefresh = true;
                        getpageList(1);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, orderInfo.getId());
                        bundle.putString("OrderNum", orderInfo.getOrderNum());
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        return;
                    } else {
                        T.show(getContext(), takeup.getMessage().toString());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)));
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
                    T.show(getContext(),getString(R.string.gain_data_failed));
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

    private void closeWindow() {
        orderLayout.setVisibility(View.GONE);
        MyApplication.setIsSkipNewOrder(true);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(mFragment).commit();
    }

    private void showWindow() {
        orderLayout.setVisibility(View.VISIBLE);
        MyApplication.setIsSkipNewOrder(false);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(mFragment).commit();
    }

    private final BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mFragment != null && mFragment.isVisible()) return;
            geTuiMsg(intent);
        }
    };

    /**
     * 个推的消息广播及通知处理
     *
     * @param intent
     */
    private void geTuiMsg(Intent intent) {
        final String action = intent.getAction();
        String json = intent.getStringExtra(ActionType.EXTRA_DATA);
        if (ActionType.ACTION_ORDER.equals(action)) {
            if (mFragment != null) {
                OrderMsg orderMsg = JSON.parseObject(json, OrderMsg.class);
//                mFragment.setData(orderMsg.getOrder(), orderMsg.getOrder().getCooperator());
                orderId = orderMsg.getOrder().getId();
                getOrerInfo();

//                orderType.setText(MyApplication.getInstance().getSkill(orderMsg.getOrder().getOrderType()));
                showWindow();
            }
        } else if (ActionType.ACTION_INVITATION.equals(action)) {
//            InvitationMsg invitation = JSON.parseObject(json, InvitationMsg.class);
//            invitationDialogFragment = new InvitationDialogFragment();
//            if (!invitationDialogFragment.isAdded()) {
//                invitationDialogFragment.show(fragmentManager, "Invitation");
//            }
//            invitationDialogFragment.update(invitation);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  if (MyApplication.isRefresh) {
            page = 1;
            isRefresh = true;
            getpageList(1);
            MyApplication.isRefresh = false;
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setMainForego(true);
        registerReceiver(mOrderReceiver, CustomIntentFilter.getInvitationIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.getInstance().setMainForego(false);
        unregisterReceiver(mOrderReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        geTuiMsg(intent);
        super.onNewIntent(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
