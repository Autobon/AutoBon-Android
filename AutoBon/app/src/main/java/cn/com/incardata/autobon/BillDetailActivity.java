package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.BillDetailAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BillOrderEntity;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * Created by zhangming on 2016/3/21.
 * 账单详情
 */
public class BillDetailActivity extends BaseActivity {
    private PullToRefreshView pullToRefreshView;
    private ListView mList;
    private List<OrderInfo_Data> billDetailList;
    private BillDetailAdapter billAdapter;
    private int billId;
    public static String[] workItems;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_order_activity);
        initView();
        initData();
    }

    private void initView() {
        billId = getIntent().getIntExtra("BillID", 0);

        mList = (ListView) findViewById(R.id.bill_detail_list_view);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.bill_detail_list_pull);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        workItems = getResources().getStringArray(R.array.work_item);

        billDetailList = new ArrayList<OrderInfo_Data>();
        billAdapter = new BillDetailAdapter(this, billDetailList);
        mList.setAdapter(billAdapter);

        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                billDetailList.clear();
                getDataFromServer(1);
            }
        });
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page == totalPages){
                    T.show(getContext(), R.string.has_load_all_label);
                    pullToRefreshView.loadedCompleted();
                    return;
                }
                getDataFromServer(++page);
            }
        });

        getDataFromServer(1);
    }

    private void getDataFromServer(int page) {
        Http.getInstance().getTaskToken(NetURL.getBillOrderInfo(billId), "page=" + page + "&pageSize=20", BillOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                pullToRefreshView.loadedCompleted();
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failure);
                    return;
                }
                if (entity instanceof BillOrderEntity){
                    BillOrderEntity billOrder = (BillOrderEntity) entity;
                    totalPages = billOrder.getData().getTotalPages();
                    if (billOrder.isResult()){
                        billDetailList.addAll(billOrder.getData().getList());
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
//        String json = "{\"result\":true,\"message\":\"\",\"error\":\"\",\"data\":{\"page\":1,\"totalElements\":1,\"totalPages\":1,\"pageSize\":20,\"count\":1,\"list\":[{\"id\":5,\"orderNum\":\"16031511D8T4KB\",\"orderType\":4,\"photo\":null,\"orderTime\":null,\"addTime\":1458013671000,\"creatorType\":0,\"creatorId\":0,\"creatorName\":null,\"contactPhone\":null,\"positionLon\":null,\"positionLat\":null,\"remark\":null,\"mainTech\":{\"id\":1,\"phone\":\"18812345678\",\"name\":\"tom\",\"gender\":null,\"avatar\":null,\"idNo\":\"422302198608266313\",\"idPhoto\":\"/etc/a.jpg\",\"bank\":\"027\",\"bankAddress\":\"光谷\",\"bankCardNo\":\"88888888888\",\"verifyAt\":null,\"requestVerifyAt\":null,\"verifyMsg\":null,\"lastLoginAt\":1458030318036,\"lastLoginIp\":\"127.0.0.1\",\"createAt\":1455724800000,\"skill\":\"1\",\"pushId\":null,\"status\":\"NEWLY_CREATED\"},\"secondTech\":null,\"mainConstruct\":{\"id\":3,\"orderId\":5,\"techId\":1,\"positionLon\":null,\"positionLat\":null,\"startTime\":1433174400000,\"signinTime\":null,\"endTime\":1433260800000,\"beforePhotos\":null,\"afterPhotos\":null,\"payment\":520,\"payStatus\":0,\"workItems\":null,\"workPercent\":0,\"carSeat\":0},\"secondConstruct\":null,\"comment\":null,\"status\":\"FINISHED\"}]}}";
//        BillOrderEntity entity = JSONObject.parseObject(json, BillOrderEntity.class);
    }

}
