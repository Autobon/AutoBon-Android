package cn.com.incardata.autobon;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.OrderInfo_Data_Comment;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseActivity {

    private TextView money;
    private TextView moneyState;
    private TextView orderNum;
    private ImageView orderImage;
    private TextView remark;
    private TextView workTime;
    private TextView workDuration;
    private TextView workItem;
    private RatingBar ratingBar;
    private ImageView arriveOnTime;
    private ImageView completeOnTime;
    private ImageView professional;
    private ImageView dressNeatly;
    private ImageView carProtect;
    private ImageView goodAttitude;
    private TextView otherProposal;

    private boolean isMainResponsible;
    private OrderInfo_Data orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        initView();
        updateUI(orderInfo);
    }

    private void initView() {
        money = (TextView) findViewById(R.id.money);
        moneyState = (TextView) findViewById(R.id.money_state);
        orderNum = (TextView) findViewById(R.id.order_number);
        orderImage = (ImageView) findViewById(R.id.order_image);
        remark = (TextView) findViewById(R.id.remark);
        workTime = (TextView) findViewById(R.id.work_time);
        workDuration = (TextView) findViewById(R.id.work_duration);
        workItem = (TextView) findViewById(R.id.work_item);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        arriveOnTime = (ImageView) findViewById(R.id.arrive_on_time);
        completeOnTime = (ImageView) findViewById(R.id.complete_on_time);
        professional = (ImageView) findViewById(R.id.professional);
        dressNeatly = (ImageView) findViewById(R.id.dress_neatly);
        carProtect = (ImageView) findViewById(R.id.car_protect);
        goodAttitude = (ImageView) findViewById(R.id.good_attitude);
        otherProposal = (TextView) findViewById(R.id.other_proposal);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        isMainResponsible = getIntent().getBooleanExtra("isMain", false);
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
    }

    private void getOrderInfo(){
        Http.getInstance().getTaskToken(NetURL.getOrderInfo(2), "", OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failure);
                    return;
                }
                if (entity instanceof OrderInfoEntity){
                    if (((OrderInfoEntity) entity).isResult()){
                        updateUI(((OrderInfoEntity) entity).getData());
                    }else {
                        T.show(getContext(), R.string.loading_data_failure);
                        return;
                    }
                }
            }
        });
    }

    private void updateUI(OrderInfo_Data data){
        remark.setText(data.getRemark());
        workTime.setText(DateCompute.getDate(data.getOrderTime()));
        orderNum.setText(getResources().getString(R.string.order_serial_number) + data.getOrderNum());
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + data.getPhoto(), orderImage, false, R.mipmap.load_image_failed);

        OrderInfo_Data_Comment comment = data.getComment();
        if (comment != null) {
            ratingBar.setRating(comment.getStar());
            if (comment.isArriveOnTime()) {
                arriveOnTime.setImageResource(R.mipmap.radio_select);
            }
            if (comment.isCompleteOnTime()) {
                completeOnTime.setImageResource(R.mipmap.radio_select);
            }
            if (comment.isProfessional()) {
                professional.setImageResource(R.mipmap.radio_select);
            }
            if (comment.isDressNeatly()) {
                dressNeatly.setImageResource(R.mipmap.radio_select);
            }
            if (comment.isCarProtect()) {
                carProtect.setImageResource(R.mipmap.radio_select);
            }
            if (comment.isGoodAttitude()) {
                goodAttitude.setImageResource(R.mipmap.radio_select);
            }
            otherProposal.setText(comment.getAdvice());
        }


        OrderInfo_Construction construct;
        if (isMainResponsible){
            construct = data.getMainConstruct();
        }else {
            construct = data.getSecondConstruct();
        }

        if (construct == null){
            return;
        }
        money.setText(getResources().getString(R.string.RMB) + construct.getPayment());

        if (construct.getPayStatus() == 2){
            moneyState.setText(R.string.pay_done);
            moneyState.setTextColor(getResources().getColor(R.color.main_orange));
        }else {
            moneyState.setText(R.string.pay_wait);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
        }
        workDuration.setText(duration(construct.getStartTime(), construct.getEndTime()));

        if (data.getOrderType() == 4) {//美容清洁
            workItem.setText(MyApplication.getInstance().getSkill(4));
            return;
        }
        String item = construct.getWorkItems();
        if (TextUtils.isEmpty(item)){
            workItem.setText(null);
        }else {
            if (item.contains(",")){
                String[] items = item.split(",");
                String tempItem = "";
                for (String str : items){
                    tempItem += MyOrderActivity.workItems[Integer.parseInt(str)] + ",";
                }
                workItem.setText(tempItem.substring(0, tempItem.length() - 1));
            }else {
                workItem.setText(MyOrderActivity.workItems[1]);
            }
        }
    }

    private String duration(long startTime, long endTime){
        long useTime = 0L;
        try{
            useTime = endTime - startTime;
        }catch (Exception e){
            e.printStackTrace();
            return "0分钟";
        }
        final int hour = (int)(useTime / (1000*3600));
        final int minute =(int)((useTime - hour*(1000*3600)) / (1000*60));
//        final int second = (int)((useTime - hour*(1000*3600) - minute*(1000*60)) / 1000);

        if (hour <= 0){
            return minute + "分钟";
        }else {
            return hour + "小时" + minute + "分钟";
        }
    }
}
