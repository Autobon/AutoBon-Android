package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.OrderWaitAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CollectionShopEntity;
import cn.com.incardata.http.response.CollectionShop_Data;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.ListNew_Data;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderInfo;
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
    private List<OrderInfo> orderList;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    private TextView today;

    private List<CollectionShop_Data> collectionShopList;

    public static boolean isGetCollectionShop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order);

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGetCollectionShop){
            isGetCollectionShop = false;
            getCollectionShopList();
        }
    }

    private void initView() {
        collectionShopList = new ArrayList<>();
        today = (TextView) findViewById(R.id.today);
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.wait_order_list);

        orderList = new ArrayList<OrderInfo>();
        mAdapter = new OrderWaitAdapter(this, orderList,collectionShopList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WaitOrderInfoActivity.class);
                intent.putExtra(AutoCon.ORDER_INFO, orderList.get(position));
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
        getCollectionShopList();
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
        Http.getInstance().postTaskToken(NetURL.TAKEUPV2, TakeupEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.immediate_order_failed);
                    return;
                }
                if (entity instanceof TakeupEntity){
                    TakeupEntity takeup = (TakeupEntity) entity;
                    if (takeup.isStatus()){
                        OrderInfo orderInfo = JSON.parseObject(takeup.getMessage().toString(),OrderInfo.class);
                        MyApplication.isRefresh = true;
                        page = 1;
                        isRefresh = true;
                        loadData(1);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AutoCon.ORDER_ID, orderInfo.getId());
                        bundle.putString("OrderNum", orderInfo.getOrderNum());
                        startActivity(ImmediateSuccessedActivity.class, bundle);
                        return;
                    }else {
                        T.show(getContext(), takeup.getMessage().toString());
                        return;
                    }
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)));
    }

    /**
     * 获取收藏技师列表
     */
    private void getCollectionShopList(){
        Http.getInstance().getTaskToken(NetURL.YETCOLLECTIONSHOP, "page=1&pageSize=200", CollectionShopEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(),R.string.request_failed);
                    return;
                }
                if (entity instanceof CollectionShopEntity){
                    if (collectionShopList!= null && collectionShopList.size() > 0){
                        collectionShopList.clear();
                    }
                    CollectionShopEntity collectionShopEntity = (CollectionShopEntity) entity;
                    collectionShopList.addAll(collectionShopEntity.getList());
                    mAdapter.notifyDataSetInvalidated();
                }
            }
        });
    }

    private void loadData(int page){
        Http.getInstance().getTaskToken(NetURL.LIST_NEWV2, "page=" + page + "&pageSize=20", ListNewEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    T.show(getContext(),R.string.request_failed);
                    return;
                }
                if (entity instanceof ListNewEntity){
                    ListNewEntity listNew = (ListNewEntity) entity;
                    if (listNew.isStatus()){
                        ListNew_Data listNew_data = JSON.parseObject(listNew.getMessage().toString(),ListNew_Data.class);
                        totalPages = listNew_data.getTotalPages();
                        if (isRefresh){
                            orderList.clear();
                        }
                        if (listNew_data.getTotalElements() == 0){
                            T.show(getContext(),getString(R.string.no_order));
                        }
                        orderList.addAll(listNew_data.getList());
                        mAdapter.notifyDataSetInvalidated();
                    }else {
                        T.show(getContext(),listNew.getMessage().toString());
                        return;
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
