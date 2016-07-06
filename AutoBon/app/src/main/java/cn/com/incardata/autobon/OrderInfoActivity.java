package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.OrderInfo_Data_Comment;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;

/**
 * 我的订单-订单详情
 */
public class OrderInfoActivity extends BaseActivity {

    private TextView money;
    private TextView moneyState;
    private TextView orderNum;
    private ImageView orderImage;
    private TextView remark;
    private TextView order_type;
    private TextView work_item;
    private TextView work_person;
    private TextView sign_in_time;
    private GridView work_before_grid, work_after_grid;
    private RatingBar ratingBar;
    private ImageView arriveOnTime;
    private ImageView completeOnTime;
    private ImageView professional;
    private ImageView dressNeatly;
    private ImageView carProtect;
    private ImageView goodAttitude;
    private TextView otherProposal;
    private Display display;

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
        display = getWindowManager().getDefaultDisplay();
        money = (TextView) findViewById(R.id.money);
        moneyState = (TextView) findViewById(R.id.money_state);
        orderNum = (TextView) findViewById(R.id.order_number);
        orderImage = (ImageView) findViewById(R.id.order_image);
        remark = (TextView) findViewById(R.id.remark);
        order_type = (TextView) findViewById(R.id.order_type);
        work_item = (TextView) findViewById(R.id.work_item);
        work_person = (TextView) findViewById(R.id.work_person);
        sign_in_time = (TextView) findViewById(R.id.sign_in_time);
        work_before_grid = (GridView) findViewById(R.id.work_before_grid);
        work_after_grid = (GridView) findViewById(R.id.work_after_grid);
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
        findViewById(R.id.order_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(0, orderInfo.getPhoto());
            }
        });

        isMainResponsible = getIntent().getBooleanExtra("isMain", false);
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
    }

    private void getOrderInfo() {
        Http.getInstance().getTaskToken(NetURL.getOrderInfo(2), "", OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failure);
                    return;
                }
                if (entity instanceof OrderInfoEntity) {
                    if (((OrderInfoEntity) entity).isResult()) {
                        updateUI(((OrderInfoEntity) entity).getData());
                    } else {
                        T.show(getContext(), R.string.loading_data_failure);
                        return;
                    }
                }
            }
        });
    }

    private void updateUI(OrderInfo_Data data) {
        OrderInfo_Construction construct;
        if (isMainResponsible) {
            construct = data.getMainConstruct();
        } else {
            construct = data.getSecondConstruct();
        }
        OrderInfo_Data_Comment comment = data.getComment();
        remark.setText(data.getRemark());
        if (data.getOrderType() == 1) {
            order_type.setText(R.string.skill_item_1);
        } else if (data.getOrderType() == 2) {
            order_type.setText(R.string.skill_item_2);
        } else if (data.getOrderType() == 3) {
            order_type.setText(R.string.skill_item_3);
        } else {
            order_type.setText(R.string.skill_item_4);
        }
        MyInfo_Data tech;
        if (isMainResponsible) {
            tech = data.getMainTech();
        } else {
            tech = data.getSecondTech();
        }
        if (tech == null) {
            return;
        }
        work_person.setText(tech.getName());
        orderNum.setText(getResources().getString(R.string.order_serial_number) + data.getOrderNum());
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + data.getPhoto(), orderImage, false, R.mipmap.load_image_failed);

        if ("CANCELED".equals(data.getStatus())) {
            money.setText(getResources().getString(R.string.RMB) + 0);
            moneyState.setText(R.string.yetcancel);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
            work_item.setText(R.string.nothave);
            sign_in_time.setText(R.string.nothave);
        } else if ("GIVEN_UP".equals(data.getStatus())) {
            money.setText(getResources().getString(R.string.RMB) + 0);
            moneyState.setText(R.string.yetrenounce);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
            work_item.setText(R.string.nothave);
            sign_in_time.setText(R.string.nothave);
        } else if ("EXPIRED".equals(data.getStatus())) {
            money.setText(getResources().getString(R.string.RMB) + 0);
            moneyState.setText(R.string.yetovertime);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
            work_item.setText(R.string.nothave);
            if (construct == null) {
                sign_in_time.setText(R.string.nothave);
            } else {
                sign_in_time.setText(DateCompute.getDate(construct.getSigninTime()));
            }
        } else {
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
            if (construct == null){
                money.setText(getResources().getString(R.string.RMB) + 0);
            }else {
                money.setText(getResources().getString(R.string.RMB) + construct.getPayment());
            }

            if (construct.getPayStatus() == 2) {
                moneyState.setText(R.string.pay_done);
                moneyState.setTextColor(getResources().getColor(R.color.main_orange));
            } else {
                moneyState.setText(R.string.pay_wait);
                moneyState.setTextColor(getResources().getColor(R.color.darkgray));
            }


            if (data.getOrderType() == 4) {//美容清洁
                work_item.setText(MyApplication.getInstance().getSkill(4));
            }
            String item = construct.getWorkItems();

            L.i("items====",item);
            L.i("======","=============================================");
            if (TextUtils.isEmpty(item)) {
                work_item.setText(null);
            } else {
                if (item.contains(",")) {
                    String[] items = item.split(",");


                    String tempItem = "";
                    for (String str : items) {
                        L.i("aaaa====",str);
                        L.i("======","=============================================");
                        tempItem += MyOrderActivity.workItems[Integer.parseInt(str)] + ",";
                        L.i("bbbb====",MyOrderActivity.workItems[Integer.parseInt(str)]);
                        L.i("======","=============================================");

                    }
                    work_item.setText(tempItem.substring(0, tempItem.length() - 1));
                } else {
                    work_item.setText(MyOrderActivity.workItems[1]);
                }
            }
            sign_in_time.setText(DateCompute.getDate(construct.getSigninTime()));
            Myadapter myadapter;
            final String[] urlBefore;
            String urlBeforePhotos = construct.getBeforePhotos();
            if (urlBeforePhotos.contains(",")) {
                urlBefore = urlBeforePhotos.split(",");
            } else {
                urlBefore = new String[]{urlBeforePhotos};
            }
            myadapter = new Myadapter(this, urlBefore);
            work_before_grid.setAdapter(myadapter);

            work_before_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    openImage(position, urlBefore);
                }
            });

            final String[] urlAfter;
            String urlAfterPhotos = construct.getAfterPhotos();
            if (urlAfterPhotos.contains(",")) {
                urlAfter = urlAfterPhotos.split(",");
            } else {
                urlAfter = new String[]{urlAfterPhotos};
            }
            myadapter = new Myadapter(this, urlAfter);
            work_after_grid.setAdapter(myadapter);
            work_after_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openImage(position, urlAfter);
                }
            });
        }

    }

    /** 查看图片
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls){
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }

//    private String duration(long startTime, long endTime) {
//        long useTime = 0L;
//        try {
//            useTime = endTime - startTime;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "0分钟";
//        }
//        final int hour = (int) (useTime / (1000 * 3600));
//        final int minute = (int) ((useTime - hour * (1000 * 3600)) / (1000 * 60));
////        final int second = (int)((useTime - hour*(1000*3600) - minute*(1000*60)) / 1000);
//
//        if (hour <= 0) {
//            return minute + "分钟";
//        } else {
//            return hour + "小时" + minute + "分钟";
//        }
//    }


    class Myadapter extends BaseAdapter {
        private Context context;
        private String[] urlItem;

        public Myadapter(Context context, String[] urlItem) {
            this.context = context;
            this.urlItem = urlItem;
        }

        @Override
        public int getCount() {
            return urlItem.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_image, viewGroup, false);
                imageView = (ImageView) view.findViewById(R.id.imgGridItem);
//                imageView.setLayoutParams(new GridView.LayoutParams(display.getWidth() / 3,display.getWidth() / 3));
//                GridView.LayoutParams params = new GridView.LayoutParams(display.getWidth() / 3, display.getWidth() / 3);
//                view.setLayoutParams(params);
                view.setTag(imageView);
            } else {
                imageView = (ImageView) view.getTag();
            }
//            imageView.setImageResource(imgUrl[position]);
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + urlItem[position], imageView, false);


            return view;
        }
    }
}
