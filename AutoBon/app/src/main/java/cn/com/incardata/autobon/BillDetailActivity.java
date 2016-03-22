package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.BillDetailAdapter;
import cn.com.incardata.http.response.BillOrderEntity;
import cn.com.incardata.http.response.OrderInfo_Data;

/**
 * Created by zhangming on 2016/3/21.
 * 账单详情
 */
public class BillDetailActivity extends Activity {
    private ListView mList;
    private List<OrderInfo_Data> billDetailList;
    private BillDetailAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_order_activity);
        initView();
        initData();
        getDataFromServer();
    }

    private void initView() {
        mList = (ListView) findViewById(R.id.bill_detail_list_view);
    }

    private void initData() {
        billDetailList = new ArrayList<OrderInfo_Data>();
        billAdapter = new BillDetailAdapter(this, billDetailList);
        mList.setAdapter(billAdapter);
    }

    private void getDataFromServer() {
        String json = "{\"result\":true,\"message\":\"\",\"error\":\"\",\"data\":{\"page\":1,\"totalElements\":1,\"totalPages\":1,\"pageSize\":20,\"count\":1,\"list\":[{\"id\":5,\"orderNum\":\"16031511D8T4KB\",\"orderType\":4,\"photo\":null,\"orderTime\":null,\"addTime\":1458013671000,\"creatorType\":0,\"creatorId\":0,\"creatorName\":null,\"contactPhone\":null,\"positionLon\":null,\"positionLat\":null,\"remark\":null,\"mainTech\":{\"id\":1,\"phone\":\"18812345678\",\"name\":\"tom\",\"gender\":null,\"avatar\":null,\"idNo\":\"422302198608266313\",\"idPhoto\":\"/etc/a.jpg\",\"bank\":\"027\",\"bankAddress\":\"光谷\",\"bankCardNo\":\"88888888888\",\"verifyAt\":null,\"requestVerifyAt\":null,\"verifyMsg\":null,\"lastLoginAt\":1458030318036,\"lastLoginIp\":\"127.0.0.1\",\"createAt\":1455724800000,\"skill\":\"1\",\"pushId\":null,\"status\":\"NEWLY_CREATED\"},\"secondTech\":null,\"mainConstruct\":{\"id\":3,\"orderId\":5,\"techId\":1,\"positionLon\":null,\"positionLat\":null,\"startTime\":1433174400000,\"signinTime\":null,\"endTime\":1433260800000,\"beforePhotos\":null,\"afterPhotos\":null,\"payment\":520,\"payStatus\":0,\"workItems\":null,\"workPercent\":0,\"carSeat\":0},\"secondConstruct\":null,\"comment\":null,\"status\":\"FINISHED\"}]}}";
        BillOrderEntity entity = JSONObject.parseObject(json, BillOrderEntity.class);
        billDetailList.addAll(entity.getData().getList());
        billAdapter.notifyDataSetChanged();
    }

}
