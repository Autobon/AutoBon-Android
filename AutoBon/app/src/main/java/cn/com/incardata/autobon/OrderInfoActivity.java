package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.Order;
import cn.com.incardata.http.response.OrderConstructionShow;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.OrderInfo_Data_Comment;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.WorkMessagePopupWindow;
import cn.com.incardata.view.wheel.widget.WheelPopupWindow;

/**
 * 我的订单-订单详情
 */
public class OrderInfoActivity extends BaseActivity {

    private TextView money;
    private TextView moneyState;
    private TextView orderNum;
    //    private ImageView orderImage;
    private TextView remark;
    private TextView order_type;
    private TextView shops_name;
    private TextView contact_phone;
    private TextView work_person;
    private TextView worktime;
    private GridView work_before_grid, work_after_grid, order_grid;
    private RatingBar ratingBar;
    private ImageView arriveOnTime;
    private ImageView completeOnTime;
    private ImageView professional;
    private ImageView dressNeatly;
    private ImageView carProtect;
    private ImageView goodAttitude;
    private TextView otherProposal;
    private RelativeLayout rll5;
    private TextView noComment;
    private Display display;
    private Button check_tech_message;
    private Button collection;

    private ImageView img_phone;

    private boolean isMainResponsible;
    private OrderInfo orderInfo;
    private int id;
    private WorkMessagePopupWindow workMessagePopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        initView();
    }


    private void initView() {
        display = getWindowManager().getDefaultDisplay();
        money = (TextView) findViewById(R.id.money);
        moneyState = (TextView) findViewById(R.id.money_state);
        orderNum = (TextView) findViewById(R.id.order_number);
        remark = (TextView) findViewById(R.id.remark);
        order_type = (TextView) findViewById(R.id.order_type);
        shops_name = (TextView) findViewById(R.id.shops_name);
        contact_phone = (TextView) findViewById(R.id.contact_phone);
        work_person = (TextView) findViewById(R.id.work_person);
        worktime = (TextView) findViewById(R.id.sign_in_time);
        order_grid = (GridView) findViewById(R.id.order_grid);
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
        noComment = (TextView) findViewById(R.id.noComment);
        rll5 = (RelativeLayout) findViewById(R.id.rll5);
        check_tech_message = (Button) findViewById(R.id.check_tech_message);

        img_phone = (ImageView) findViewById(R.id.img_phone);

        collection = (Button) findViewById(R.id.collection);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionShop();
            }
        });



        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        if (orderInfo == null){
            T.show(getContext(),R.string.dataUploadFailed);
            return;
        }else {
            updateUI(orderInfo);
        }

        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(orderInfo.getContactPhone())){
                    Uri uri = Uri.parse("tel:" + orderInfo.getContactPhone());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }
            }
        });
    }


    public String getProject(String type) {
        if ("1".equals(type)) {
            return "隔热膜";
        } else if ("2".equals(type)) {
            return "隐形车衣";
        } else if ("3".equals(type)) {
            return "车身改色";
        } else if ("4".equals(type)) {
            return "美容清洁";
        } else
            return null;
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
                    }else {
                        T.show(getContext(),entity1.getMessage());
                    }
                }

            }
        });
    }


    private void updateUI(final OrderInfo data) {
        Myadapter myadapter1;

        final String[] urlOrder;
        String urlOrderPhotos = data.getPhoto();
        if (urlOrderPhotos.contains(",")) {
            urlOrder = urlOrderPhotos.split(",");
        } else {
            urlOrder = new String[]{urlOrderPhotos};
        }
        myadapter1 = new Myadapter(this, urlOrder);
        order_grid.setAdapter(myadapter1);

        order_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openImage(position, urlOrder);
            }
        });
        remark.setText(data.getRemark());
        String type = "";
        String[] types = (data.getType()).split(",");
        for (int i = 0; i < types.length; i++){
            type = type + getProject(types[i]) + ",";
        }
        type = type.substring(0,type.length() - 1);

        if (data.getStatus().equals("CANCELED")) {
            money.setText(R.string.no);
            moneyState.setText(R.string.yetcancel);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
        } else if (data.getStatus().equals("GIVEN_UP")) {
            money.setText(R.string.no);
            moneyState.setText(R.string.yetrenounce);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
        } else if (data.getStatus().equals("EXPIRED")) {
            money.setText(R.string.no);
            moneyState.setText(R.string.yetovertime);
            moneyState.setTextColor(getResources().getColor(R.color.darkgray));
        } else {
            if (data.getPayStatus() == null || data.getPayStatus() == 0){
                money.setText(R.string.no);
                moneyState.setText(R.string.no_calculate);
                moneyState.setTextColor(getResources().getColor(R.color.gray_A3));
            }else if(data.getPayStatus() == 1){
                money.setVisibility(View.VISIBLE);
                money.setText(getResources().getString(R.string.RMB) + data.getPayment());
                moneyState.setText(R.string.pay_wait);
                moneyState.setTextColor(getResources().getColor(R.color.gray_A3));
                money.setTextColor(getResources().getColor(R.color.gray_A3));
            }else if (data.getPayStatus() == 2){
                money.setVisibility(View.VISIBLE);
                money.setText(getResources().getString(R.string.RMB) + data.getPayment());
                moneyState.setText(R.string.pay_done);
                moneyState.setTextColor(getResources().getColor(R.color.main_orange));
                money.setTextColor(getResources().getColor(R.color.gray_A3));
            }
        }

        order_type.setText(type);
        orderNum.setText(getResources().getString(R.string.order_serial_number) + data.getOrderNum());
        shops_name.setText(data.getCoopName());
        contact_phone.setText(data.getContactPhone());


        OrderInfo_Data_Comment comment = data.getComment();


        if ("CANCELED".equals(data.getStatus())) {
            shops_name.setText(R.string.nothave);
            worktime.setText(R.string.nothave);
            work_person.setText(data.getTechName());
            contact_phone.setText(R.string.nothave);
        } else if ("GIVEN_UP".equals(data.getStatus())) {
            shops_name.setText(R.string.nothave);
            worktime.setText(R.string.nothave);
            work_person.setText(data.getTechName());
            contact_phone.setText(R.string.nothave);
        } else if ("EXPIRED".equals(data.getStatus())) {
            shops_name.setText(R.string.nothave);
            contact_phone.setText(R.string.nothave);
            if (orderInfo.getStartTime() != 0) {
                worktime.setText(DateCompute.getDate(orderInfo.getStartTime()));
            } else {
                worktime.setText(R.string.nothave);
            }
            work_person.setText(data.getTechName());
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
            }else {
                noComment.setVisibility(View.VISIBLE);
                rll5.setVisibility(View.GONE);
            }

            worktime.setText(DateCompute.getDate(orderInfo.getStartTime()));
            String techname = "";
            for (OrderConstructionShow orderConstructionShow : data.getOrderConstructionShow()) {
                techname = techname + orderConstructionShow.getTechName() + ",";
            }
            techname = techname.substring(0, techname.length() - 1);

            work_person.setText(techname);

            Myadapter myadapter;

            final String[] urlBefore;
            String urlBeforePhotos = data.getBeforePhotos();
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
            String urlAfterPhotos = data.getAfterPhotos();
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

            check_tech_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (workMessagePopupWindow == null) {
                        workMessagePopupWindow = new WorkMessagePopupWindow(OrderInfoActivity.this,data.getOrderConstructionShow(),data.getConstructionWasteShows());
                        workMessagePopupWindow.init();
                    }
                    workMessagePopupWindow.showAtLocation(findViewById(R.id.rll1), Gravity.CENTER, 0, 0);
                }
            });

        }

    }

    /**
     * 查看图片
     *
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }

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
                view.setTag(imageView);
            } else {
                imageView = (ImageView) view.getTag();
            }
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + urlItem[position], imageView, false);


            return view;
        }
    }
}
