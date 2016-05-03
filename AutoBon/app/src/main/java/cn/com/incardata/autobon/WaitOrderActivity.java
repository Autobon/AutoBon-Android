package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.OrderWaitAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.TakeupEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;


/**
 * 未被抢的单
 */
public class WaitOrderActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{
    private PullToRefreshView refresh;
    private ListView mListView;
    private OrderWaitAdapter mAdapter;
    private List<Order> orderList;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    private TextView today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order);

        initView();
    }

    private void initView() {
        today = (TextView) findViewById(R.id.today);
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.wait_order_list);

        orderList = new ArrayList<Order>();
        mAdapter = new OrderWaitAdapter(this, orderList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WaitOrderInfoActivity.class);
                intent.putExtra("Order", orderList.get(position));
                startActivityForResult(intent, 0x10);
            }
        });

        refresh.setOnHeaderRefreshListener(this);
        refresh.setOnFooterRefreshListener(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        today.setText(DateCompute.getWeekOfDate());
        loadData(1);

        mAdapter.setOnClickOrderListener(new OrderWaitAdapter.OnClickOrderListener() {
            @Override
            public void onClickOrder(int position) {
                immediateOrder(orderList.get(position).getId());
            }
        });
    }

    /**
     * 抢单
     */
    private void immediateOrder(int orderId) {
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
                        page = 1;
                        isRefresh = true;
                        loadData(1);
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
                        T.show(getContext(), takeup.getMessage());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)));
    }

    private void loadData(int page){
        Http.getInstance().getTaskToken(NetURL.LIST_NEW, "page=" + page + "&pageSize=20", ListNewEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListNewEntity){
                    ListNewEntity listNew = (ListNewEntity) entity;
                    totalPages = listNew.getData().getTotalPages();
                    if (listNew.isResult()){
                        if (isRefresh){
                            orderList.clear();
                        }
                        orderList.addAll(listNew.getData().getList());
                        mAdapter.notifyDataSetInvalidated();
                    }
                    isRefresh = false;
                }
            }
        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (page >= totalPages){
            T.show(getContext(), R.string.has_load_all_label);
            refresh.loadedCompleted();
            return;
        }
        loadData(++page);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        loadData(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x10 & resultCode == 0x11){
            page = 1;
            isRefresh = true;
            loadData(1);
        }
    }
}
