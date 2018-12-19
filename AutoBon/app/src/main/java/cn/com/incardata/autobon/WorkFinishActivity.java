package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.yanzhenjie.album.Album;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incardata.adapter.ConsumeGridViewAdapter;
import cn.com.incardata.adapter.GridViewAdapter;
import cn.com.incardata.adapter.ListViewAdapter;
import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.adapter.RadioFragmentGridAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.customfun.GatherImage;
import cn.com.incardata.fragment.BaseStandardFragment;
import cn.com.incardata.fragment.DropOrderDialogFragment;
import cn.com.incardata.fragment.FiveCarRadioFragment;
import cn.com.incardata.fragment.ImageChooseFragment;
import cn.com.incardata.fragment.SevenCarRadioFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddContact_data_list;
import cn.com.incardata.http.response.ConstructionDetail;
import cn.com.incardata.http.response.ConstructionPosition;
import cn.com.incardata.http.response.ConsumeItem;
import cn.com.incardata.http.response.FinishWorkEntity;
import cn.com.incardata.http.response.GetOrderProjectItem;
import cn.com.incardata.http.response.GetOrderProjectItemEntity;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.ListUnfinishedOrderEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.ProjectPositions;
import cn.com.incardata.http.response.Technician;
import cn.com.incardata.http.response.UnfinishOrder;
import cn.com.incardata.http.response.WorkFinish;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.AddTechPopupWindow;
import cn.com.incardata.view.wheel.widget.WheelPopupWindow;


/**
 * 施工完成
 * Created by zhangming on 2016/3/11.
 */
public class WorkFinishActivity extends BaseActivity implements BaseStandardFragment.OnFragmentInteractionListener,
        View.OnClickListener, DropOrderDialogFragment.OnClickListener, ImageChooseFragment.OnFragmentInteractionListener {
    private GridView gv_single_pic, gv_consume;
    //    private RadioGroup rg_tab;
    private TextView tv_day, tv_has_time, tv_content;
    private PictureGridAdapter mAdapter;
    private LinearLayout ll_other, ll_clean;
    private Button btn_submit;
    private EditText edit_remark;
    private Button finish_work_btn;
    private ImageView iv_left, iv_right, iv_my_info, iv_enter_more_page, iv_back;
    private Context context;
    private static final int MAX_PICS = 9; //图片数上限
    private Button[] buttons = new Button[4];
    private ListView listview_workItem;
    private ImageView add_tech;

    private File tempFile;
    private String fileName = "";  //my_picture目录
    private String fileName1 = "";  //my_picture目录
    private File tempDir;
    private Uri carPhotoUri;  //temp目录
    private static final int CAR_PHOTO = 1;
    private static final int COUNT_TIME_FLAG = 1;

    private boolean isRunning = true;
    private OrderInfo orderInfo;
    private List<GetOrderProjectItem> getOrderProjectItems;
    private List<ConstructionPosition> constructionPositions;
    private List<Technician> technicians;
    private ListViewAdapter listViewAdapter;
    private ConsumeGridViewAdapter consumeGridViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private List<ConsumeItem> consumeItems;
//    private OrderInfo_Construction construction;

    private int userSize = 0;
    private boolean isSuccess = true;
    private Object json;

    private DropOrderDialogFragment dropOderDialog;
    private FragmentManager fragmentManager;

    private ImageChooseFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_finish_activity);
        fragmentManager = getFragmentManager();
        initView();
        initFile();
        getWorkItem();
        new Thread(new MyThread()).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT_TIME_FLAG:
                    Bundle bundle = msg.getData();
                    int hour = bundle.getInt("hour");
                    int minute = bundle.getInt("minute");
                    int second = bundle.getInt("second");
                    tv_has_time.setText(hour + context.getString(R.string.tv_hour) + minute + context.getString(R.string.tv_minute) + second + context.getString(R.string.tv_second));
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                buttons[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_choice_btn));
                buttons[0].setTextColor(getResources().getColor(R.color.main_white));
                buttons[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[1].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[2].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[3].setTextColor(getResources().getColor(R.color.lightgray));
//                for (int i = 0; i < getOrderProjectItems.get(0).getConstructionPositions().length; i++) {
//                    getOrderProjectItems.get(0).getConstructionPositions()[i].setProjectId(getOrderProjectItems.get(0).getId());
//                }
//                if (userSize != technicians.size()){
//                    for (ConstructionPosition constructionPosition : getOrderProjectItems.get(0).getConstructionPositions()){
//                        constructionPosition.setTechnicianId(-1);
//                    }
//                    userSize = technicians.size();
//                }
                listViewAdapter = new ListViewAdapter(technicians, getOrderProjectItems.get(0).getConstructionPositions(), WorkFinishActivity.this,getOrderProjectItems.get(0).getId());
                listview_workItem.setAdapter(listViewAdapter);
                setListViewHeightBasedOnChildren(listview_workItem);

                if (getOrderProjectItems.get(0).getId() == 4){
                    gv_consume.setNumColumns(2);
                }else {
                    gv_consume.setNumColumns(4);
                }
                consumeGridViewAdapter = new ConsumeGridViewAdapter(getOrderProjectItems.get(0).getConstructionPositions(), getOrderProjectItems.get(0).getId(), WorkFinishActivity.this);
                gv_consume.setAdapter(consumeGridViewAdapter);
                listViewAdapter.setOnGetHeight(new ListViewAdapter.OnGetHeight() {
                    @Override
                    public void getHeight() {
                        for (int i = 0; i < getOrderProjectItems.size(); i++) {
                            for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTechnicianId(-1);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTotal(0);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setCheck(false);
                            }
                        }
                        listViewAdapter.notifyDataSetChanged();
                        consumeGridViewAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listview_workItem);
                    }
                });
//                listViewAdapter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        technicians.remove(listViewAdapter.getCheck());
//                        listViewAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnChildren(listview_workItem);
//                    }
//                });
                break;
            case R.id.btn2:
                buttons[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[0].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_choice_btn));
                buttons[1].setTextColor(getResources().getColor(R.color.main_white));
                buttons[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[2].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[3].setTextColor(getResources().getColor(R.color.lightgray));
//                for (int i = 0; i < getOrderProjectItems.get(1).getConstructionPositions().length; i++) {
//                    getOrderProjectItems.get(1).getConstructionPositions()[i].setProjectId(getOrderProjectItems.get(1).getId());
//                }
//                if (userSize != technicians.size()){
//                    for (ConstructionPosition constructionPosition : getOrderProjectItems.get(1).getConstructionPositions()){
//                        constructionPosition.setTechnicianId(-1);
//                    }
//                    userSize = technicians.size();
//                }
                listViewAdapter = new ListViewAdapter(technicians, getOrderProjectItems.get(1).getConstructionPositions(), WorkFinishActivity.this,getOrderProjectItems.get(1).getId());
                listview_workItem.setAdapter(listViewAdapter);
                setListViewHeightBasedOnChildren(listview_workItem);
                if (getOrderProjectItems.get(1).getId() == 4){
                    gv_consume.setNumColumns(2);
                }else {
                    gv_consume.setNumColumns(4);
                }
                consumeGridViewAdapter = new ConsumeGridViewAdapter(getOrderProjectItems.get(1).getConstructionPositions(), getOrderProjectItems.get(1).getId(), WorkFinishActivity.this);
                gv_consume.setAdapter(consumeGridViewAdapter);
                listViewAdapter.setOnGetHeight(new ListViewAdapter.OnGetHeight() {
                    @Override
                    public void getHeight() {
                        for (int i = 0; i < getOrderProjectItems.size(); i++) {
                            for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTechnicianId(-1);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTotal(0);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setCheck(false);
                            }
                        }
                        listViewAdapter.notifyDataSetChanged();
                        consumeGridViewAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listview_workItem);
                    }
                });
//                listViewAdapter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        technicians.remove(listViewAdapter.getCheck());
//                        listViewAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnChildren(listview_workItem);
//                    }
//                });
                break;
            case R.id.btn3:
                buttons[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[0].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[1].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_choice_btn));
                buttons[2].setTextColor(getResources().getColor(R.color.main_white));
                buttons[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[3].setTextColor(getResources().getColor(R.color.lightgray));
//                for (int i = 0; i < getOrderProjectItems.get(2).getConstructionPositions().length; i++) {
//                    getOrderProjectItems.get(2).getConstructionPositions()[i].setProjectId(getOrderProjectItems.get(2).getId());
//                }
//                if (userSize != technicians.size()){
//                    for (ConstructionPosition constructionPosition : getOrderProjectItems.get(2).getConstructionPositions()){
//                        constructionPosition.setTechnicianId(-1);
//                    }
//                    userSize = technicians.size();
//                }
                listViewAdapter = new ListViewAdapter(technicians, getOrderProjectItems.get(2).getConstructionPositions(), WorkFinishActivity.this,getOrderProjectItems.get(2).getId());
                listview_workItem.setAdapter(listViewAdapter);
                setListViewHeightBasedOnChildren(listview_workItem);
                if (getOrderProjectItems.get(2).getId() == 4){
                    gv_consume.setNumColumns(2);
                }else {
                    gv_consume.setNumColumns(4);
                }
                consumeGridViewAdapter = new ConsumeGridViewAdapter(getOrderProjectItems.get(2).getConstructionPositions(), getOrderProjectItems.get(2).getId(), WorkFinishActivity.this);
                gv_consume.setAdapter(consumeGridViewAdapter);
                listViewAdapter.setOnGetHeight(new ListViewAdapter.OnGetHeight() {
                    @Override
                    public void getHeight() {
                        for (int i = 0; i < getOrderProjectItems.size(); i++) {
                            for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTechnicianId(-1);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTotal(0);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setCheck(false);
                            }
                        }
                        listViewAdapter.notifyDataSetChanged();
                        consumeGridViewAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listview_workItem);
                    }
                });
//                listViewAdapter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        technicians.remove(listViewAdapter.getCheck());
//                        listViewAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnChildren(listview_workItem);
//                    }
//                });
                break;
            case R.id.btn4:
                buttons[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[0].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[1].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                buttons[2].setTextColor(getResources().getColor(R.color.lightgray));
                buttons[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_choice_btn));
                buttons[3].setTextColor(getResources().getColor(R.color.main_white));
//                for (int i = 0; i < getOrderProjectItems.get(3).getConstructionPositions().length; i++) {
//                    getOrderProjectItems.get(3).getConstructionPositions()[i].setProjectId(getOrderProjectItems.get(3).getId());
//                }
//                if (userSize != technicians.size()){
//                    for (ConstructionPosition constructionPosition : getOrderProjectItems.get(3).getConstructionPositions()){
//                        constructionPosition.setTechnicianId(-1);
//                    }
//                    userSize = technicians.size();
//                }
                listViewAdapter = new ListViewAdapter(technicians, getOrderProjectItems.get(3).getConstructionPositions(), WorkFinishActivity.this,getOrderProjectItems.get(3).getId());
                listview_workItem.setAdapter(listViewAdapter);
                setListViewHeightBasedOnChildren(listview_workItem);
                if (getOrderProjectItems.get(3).getId() == 4){
                    gv_consume.setNumColumns(2);
                }else {
                    gv_consume.setNumColumns(4);
                }
                consumeGridViewAdapter = new ConsumeGridViewAdapter(getOrderProjectItems.get(3).getConstructionPositions(), getOrderProjectItems.get(3).getId(), WorkFinishActivity.this);
                gv_consume.setAdapter(consumeGridViewAdapter);
                listViewAdapter.setOnGetHeight(new ListViewAdapter.OnGetHeight() {
                    @Override
                    public void getHeight() {
                        for (int i = 0; i < getOrderProjectItems.size(); i++) {
                            for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTechnicianId(-1);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setTotal(0);
                                getOrderProjectItems.get(i).getConstructionPositions()[j].setCheck(false);
                            }
                        }
                        listViewAdapter.notifyDataSetChanged();
                        consumeGridViewAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listview_workItem);
                    }
                });
//                listViewAdapter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        technicians.remove(listViewAdapter.getCheck());
//                        listViewAdapter.notifyDataSetChanged();
//                        setListViewHeightBasedOnChildren(listview_workItem);
//                    }
//                });
                break;
        }
    }

    /**
     * 选图对话框
     *
     * @param type 操作类型
     */
    @Override
    public void onFragmentInteraction(int type) {
        switch (type) {
            case GatherImage.CAPTURE:
                capture(GatherImage.CAPTURE_REQUEST, carPhotoUri);
                break;
            case GatherImage.GALLERY:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GatherImage.GALLERY_REQUEST);
                break;
        }
    }


    private class MyThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long useTime = 0L;
                try {
                    long currentTime = System.currentTimeMillis();
                    long startWorkTime = orderInfo.getStartTime();
                    useTime = currentTime - startWorkTime;
                    Log.i("test", "currentTime===>" + currentTime + ",useTime===>" + useTime);
                } catch (Exception e) {
                    useTime = 0L;
                    e.printStackTrace();
                }
                final int hour = (int) (useTime / (1000 * 3600));
                final int minute = (int) ((useTime - hour * (1000 * 3600)) / (1000 * 60));
                final int second = (int) ((useTime - hour * (1000 * 3600) - minute * (1000 * 60)) / 1000);

                Message msg = Message.obtain();
                msg.what = COUNT_TIME_FLAG;
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                bundle.putInt("second", second);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }

    public void showAddTech() {
    }

    private void initView() {
        context = this;
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        getOrderProjectItems = new ArrayList<GetOrderProjectItem>();
        constructionPositions = new ArrayList<ConstructionPosition>();
        technicians = new ArrayList<Technician>();
        consumeItems = new ArrayList<ConsumeItem>();
        gridViewAdapter = new GridViewAdapter();
        Technician technician = new Technician();
        technician.setId(orderInfo.getTechId());
        technician.setName(orderInfo.getTechName());
        technician.setPhone(orderInfo.getTechPhone());
        technicians.add(technician);
        userSize = technicians.size();
        buttons[0] = (Button) findViewById(R.id.btn1);
        buttons[1] = (Button) findViewById(R.id.btn2);
        buttons[2] = (Button) findViewById(R.id.btn3);
        buttons[3] = (Button) findViewById(R.id.btn4);
        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);
        buttons[3].setOnClickListener(this);


        listview_workItem = (ListView) findViewById(R.id.listview_workItem);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_has_time = (TextView) findViewById(R.id.has_use_time);
        gv_single_pic = (GridView) findViewById(R.id.gv_single_pic);
        gv_consume = (GridView) findViewById(R.id.gv_consume);
//        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        tv_day.setText(DateCompute.getWeekOfDate());
        add_tech = (ImageView) findViewById(R.id.add_tech);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        edit_remark = (EditText) findViewById(R.id.edit_remark);
        finish_work_btn = (Button) findViewById(R.id.finish_work_btn);


        iv_my_info = (ImageView) findViewById(R.id.iv_my_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_enter_more_page = (ImageView) findViewById(R.id.iv_enter_more_page);


        mAdapter = new PictureGridAdapter(this, MAX_PICS);
        gv_single_pic.setAdapter(mAdapter);
//        replaceFragment(FiveCarRadioFragment.class);  //默认是五座车的Fragment

        gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test", "position===>" + position + "," + "count===>" + mAdapter.getCount());
                if (position == mAdapter.getCount() - 1 && !mAdapter.isReachMax()) {
//                    if (tempDir == null) {
//                        tempDir = new File(SDCardUtils.getGatherDir());
//                    }
                    Album.startAlbum(WorkFinishActivity.this, 0x999
                            , 9 - mAdapter.getPicMap().size()                                                         // 指定选择数量。
                            , ContextCompat.getColor(context, R.color.main_orange)        // 指定Toolbar的颜色。
                            , ContextCompat.getColor(context, R.color.main_orange));  // 指定状态栏的颜色
//                    onClickIdentifyPhoto();
                } else {
                    LinkedHashMap<Integer, String> temp = mAdapter.getPicMap();
                    if (temp == null || temp.isEmpty()) {
                        startActivity(EnlargementActivity.class);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(EnlargementActivity.IMAGE_URL, temp.values().toArray(new String[temp.size()]));
                        bundle.putInt(EnlargementActivity.POSITION, position);
                        startActivity(EnlargementActivity.class, bundle);
                    }
                }
            }
        });

        finish_work_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetWorkHelper.isNetworkAvailable(context)) {
                    submitFinishWorkInfo();
                } else {
                    T.show(context, context.getString(R.string.no_network_error));
                    return;
                }
            }
        });

        iv_my_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MyInfoActivity.class);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String remark = edit_remark.getText().toString().trim();
                if (!TextUtils.isEmpty(remark)){
                    submitRemark(remark);
                }else {
                    T.show(context,"请填写备注");
                    return;
                }
            }
        });


        if (RadioFragmentGridAdapter.workItemMap != null && RadioFragmentGridAdapter.workItemMap.size() > 0) {
            RadioFragmentGridAdapter.workItemMap.clear(); //清空记录工作项的map集合
        }

        add_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTechView();
            }
        });

    }

    /**
     * 提交订单备注
     * @param remark
     */
    private void submitRemark(String remark) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId())));
        paramList.add(new BasicNameValuePair("remark", remark));
        Http.getInstance().postTaskToken(NetURL.SUBMIT_ORDER_REMARK, ListNewEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(context, "提交订单备注失败");
                    return;
                }
                ListNewEntity listNewEntity = (ListNewEntity) entity;
                if (listNewEntity.isStatus()) {
                    T.show(context, "提交订单备注成功");
                    edit_remark.setText("");
                }else {
                    T.show(getContext(), listNewEntity.getMessage().toString());
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    /**
     * 照片点击事件
     */
    private void onClickIdentifyPhoto() {
        if (mFragment == null) {
            mFragment = new ImageChooseFragment();
        }

        if (!SDCardUtils.isExistSDCard()) {
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (carPhotoUri == null) {
            carPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "car_photo.jpeg"));
        }
        initFile();

        mFragment.show(getFragmentManager(), "Choose");
    }

    AddTechPopupWindow add;

    public void showAddTechView() {

        add = new AddTechPopupWindow(this);
        add.init();
        add.setListener(checkedListener);

        add.showAtLocation(findViewById(R.id.linl_finish), Gravity.CENTER, 0, 0);
    }

    private AddTechPopupWindow.OnCheckedListener checkedListener = new AddTechPopupWindow.OnCheckedListener() {
        @Override
        public void onChecked(AddContact_data_list list) {
            for (Technician technician : technicians) {
                if (technician.getId() == list.getId()) {
                    T.show(getContext(), getString(R.string.this_tech_yet_select));
                    return;
                }
            }
            Technician technician = new Technician();
            technician.setId(list.getId());
            technician.setName(list.getName());
            technician.setPhone(list.getPhone());
            technicians.add(technician);
            userSize = technicians.size();
            listViewAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listview_workItem);
        }
    };

    private void initFile() {
        if (fileName.equals("")) {
            if (SDCardUtils.isExistSDCard()) {
                String path = SDCardUtils.getGatherDir() + File.separator + "my_picture";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileName = path + File.separator + "my_photo.jpeg";
                fileName1 = path + File.separator;
                tempFile = new File(fileName);
            } else {
                T.show(this, getString(R.string.uninstalled_sdcard));
            }
        }
    }

    private <T> void replaceFragment(Class<T> cls) {
        try {
//            T fragment = BaseStandardFragment.newInstance(cls, String.valueOf(orderInfo.getOrderType()));  //获取fragment实例,传递orderType参数到Fragment中
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            if (fragment instanceof BaseStandardFragment) {
//                BaseStandardFragment bs_fragment = (BaseStandardFragment) fragment;
//                transaction.replace(R.id.fragment_container, bs_fragment);
//            }
//            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO Auto-generated method stub
    }


    /**
     * 提交完成工作后的信息
     */
    private void submitFinishWorkInfo() {


        //TODO 获取上传的图片
        Map<Integer, String> picMap = mAdapter.getPicMap();
        if (picMap.size() < 3) {  //图片数量为0,提示用户
            T.show(context, context.getString(R.string.no_pic_tips_work_after));
            return;
        }
        BasicNameValuePair bv_orderId = new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId()));
        Collection<String> colUrls = picMap.values();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = colUrls.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append(",");
        }
        String urls = sb.toString();
        urls = urls.substring(0, urls.length() - 1);
        Log.i("test", "urls======>" + urls);
        BasicNameValuePair bv_afterPhotos = new BasicNameValuePair("afterPhotos", urls);
        if (isSuccess) {
            int userSize = technicians == null ? 0 : technicians.size();
//        baofeiItems = consumeGridViewAdapter.getBaofeiItems();
            ConstructionDetail[] constructionDetails = new ConstructionDetail[userSize];

            for (int i = 0; i < userSize; i++) {
                ConstructionDetail temp = new ConstructionDetail();
                temp.setTechId(technicians.get(i).getId());
                constructionDetails[i] = temp;
            }
            for (int i = 0; i < getOrderProjectItems.size(); i++) {
                for (ConstructionDetail con : constructionDetails) {
                    List<ProjectPositions> pps = new ArrayList<>();
                    for (ConstructionPosition constructionPosition : getOrderProjectItems.get(i).getConstructionPositions()) {
                        if (con.getTechId() == constructionPosition.getTechnicianId()) {
                            boolean isSkip = false;
                            for (int j = 0; j < pps.size(); j++) {
                                if (pps.get(j).getProject().equals(String.valueOf(getOrderProjectItems.get(i).getId()))) {
                                    pps.get(j).setPosition(pps.get(j).getPosition() + "," + constructionPosition.getId());
                                    isSkip = true;
                                    break;
                                }
                            }
                            if (isSkip) continue;
                            ProjectPositions p = new ProjectPositions();
                            p.setProject(String.valueOf(getOrderProjectItems.get(i).getId()));
                            p.setPosition(String.valueOf(constructionPosition.getId()));
                            pps.add(p);
                            continue;
                        }
                    }
                    if (con.getProjectPositions() == null || con.getProjectPositions().size() <= 0) {
                        con.setProjectPositions(pps);
                    } else {
                        con.getProjectPositions().addAll(pps);
                    }
                    continue;
                }
                for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                    if (getOrderProjectItems.get(i).getConstructionPositions()[j].getTotal() > 0 &&
                            getOrderProjectItems.get(i).getConstructionPositions()[j].getTechnicianId() != -1) {
                        ConsumeItem consumeItem = new ConsumeItem();
                        consumeItem.setProject(String.valueOf(getOrderProjectItems.get(i).getId()));
                        consumeItem.setPosition(String.valueOf(getOrderProjectItems.get(i).getConstructionPositions()[j].getId()));
                        consumeItem.setTechId(String.valueOf(getOrderProjectItems.get(i).getConstructionPositions()[j].getTechnicianId()));
                        consumeItem.setTotal(String.valueOf(getOrderProjectItems.get(i).getConstructionPositions()[j].getTotal()));
                        consumeItems.add(consumeItem);
                    }
                }
            }
            ConsumeItem[] constructionWastes = new ConsumeItem[consumeItems.size()];
            for (int i = 0; i < consumeItems.size(); i++) {
                constructionWastes[i] = consumeItems.get(i);
            }
            boolean isNotNull = false;

            for (int i = 0; i < constructionDetails.length; i++) {
                if (constructionDetails[i].getProjectPositions() != null && constructionDetails[i].getProjectPositions().size() > 0) {
                    isNotNull = true;
                }
            }
            if (!isNotNull) {
                T.show(getContext(), "请最少选择一项施工项目");
                return;
            }


            WorkFinish workFinish = new WorkFinish();
            workFinish.setOrderId(orderInfo.getId());
            workFinish.setAfterPhotos(urls);
            workFinish.setConstructionDetails(constructionDetails);
            workFinish.setConstructionWastes(constructionWastes);
            json = JSON.toJSON(workFinish);
            isSuccess = false;
        }

        Log.e("json", json.toString());
//        T.show(context, json.toString());
        showDialog();
        Http.getInstance().postTaskToken(NetURL.WORK_FINISH_URLV2, json.toString(), FinishWorkEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    isSuccess = false;
                    T.show(context, context.getString(R.string.operate_failed_tips));
                    return;
                }
                FinishWorkEntity finishWorkEntity = (FinishWorkEntity) entity;
                if (finishWorkEntity.isStatus()) {
                    isSuccess = true;
                    //TODO 跳转页面
                    Intent intent = new Intent(getContext(), WorkFinishedActivity.class);
                    intent.putExtra(AutoCon.ORDER_ID, orderInfo.getId());
                    intent.putExtra("OrderNum", orderInfo.getOrderNum());
                    startActivity(intent);
                    finish();
                } else {
                    isSuccess = false;
                    T.show(context, finishWorkEntity.getMessage());
                }
            }
        });

    }


    private void capture(int requestCode, Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CAR_PHOTO:
                try {
                    Bitmap bitmap = BitmapHelper.resizeImage(getContext(), carPhotoUri, 0.35f);
                    Uri uri = Uri.fromFile(tempFile);
                    boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);

                    if (isSuccess) {  //成功保存后上传压缩后的图片
                        if (NetWorkHelper.isNetworkAvailable(context)) {
                            uploadCarPhoto(uri);
                        } else {
                            T.show(context, context.getString(R.string.no_network_tips));
                            return;
                        }
                    }
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GatherImage.CAPTURE_REQUEST:
                try {
                    Bitmap bitmap = BitmapHelper.resizeImage(getContext(), carPhotoUri, 0.35f);
                    Uri uri = Uri.fromFile(tempFile);
                    boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);  //压缩图片保存到新地址

                    if (isSuccess) {
                        if (NetWorkHelper.isNetworkAvailable(context)) {
                            uploadCarPhoto(uri);
                        } else {
                            T.show(context, context.getString(R.string.no_network_tips));
                            return;
                        }
                    }

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case GatherImage.GALLERY_REQUEST:
                try {
                    Bitmap bitmap = BitmapHelper.resizeImage(getContext(), data.getData(), 0.35f);
                    Uri uri = Uri.fromFile(tempFile);
                    boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);  //压缩图片保存到新地址
                    if (isSuccess) {
                        if (NetWorkHelper.isNetworkAvailable(context)) {
                            uploadCarPhoto(uri);
                        } else {
                            T.show(context, context.getString(R.string.no_network_tips));
                            return;
                        }
                    }

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case 0x999:
                List<String> pathList = Album.parseResult(data);
                showDialog(getString(R.string.uploading_image));
                countNum = pathList.size();
                uploadNum = 0;

                for (int i = 0; i < pathList.size(); i++) {
                    try {
                        Bitmap bitmap = BitmapHelper.resizeImage(getContext(), Uri.fromFile(new File(pathList.get(i))), 0.35f);
                        Uri uri = Uri.fromFile(new File(fileName1 + "myphoto" + (i + 1) + ".jpeg"));
                        boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);

                        if (isSuccess) {  //成功保存后上传压缩后的图片
                            if (NetWorkHelper.isNetworkAvailable(context)) {
                                MyAsyncTask task = new MyAsyncTask(i, uri);
                                //task.execute(new String[0]);
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri.getPath(), NetURL.UPLOAD_WORK_PHOTOV2);
                            } else {
                                T.show(context, context.getString(R.string.no_network_tips));
                                uploadNum++;
                                return;
                            }
                        } else {
                            uploadNum++;
                        }
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } catch (Exception e) {
                        uploadNum++;
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadCarPhoto(final Uri carCompressUri) {
        showDialog(getString(R.string.uploading_image));
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String json = HttpClientInCar.uploadImage(params[0], params[1]);
                    return json;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                cancelDialog();
                if (s == null) {
                    T.show(context, getString(R.string.upload_image_failed));
                    return;
                } else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isStatus()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(tempFile).getPath());
                        mAdapter.addPic(bitmap, idPhotoEntity.getMessage());  //添加图片
                        Log.i("test", "上传压缩后的图片成功,width===>" + bitmap.getWidth() + ",height===>" + bitmap.getHeight()
                                + ",size===>" + (bitmap.getByteCount() / 1024) + "KB" + ",url===>" + idPhotoEntity.getMessage());
                    } else {
                        T.show(context, getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(carCompressUri.getPath(), NetURL.UPLOAD_WORK_PHOTOV2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (RadioFragmentGridAdapter.workItemMap != null && RadioFragmentGridAdapter.workItemMap.size() > 0) {
//            RadioFragmentGridAdapter.workItemMap.clear(); //清空记录工作项的map集合
//        }
        isRunning = false; //关闭计时线程
        if (tempDir != null && tempDir.exists()) {
            SDCardUtils.deleteAllFileInFolder(tempDir);  //销毁临时目录及文件
        }
        if (tempFile != null && tempFile.exists()) {
            File dir = tempFile.getParentFile();
            SDCardUtils.deleteAllFileInFolder(dir);
            Log.i("test", "dir===>" + dir.getPath());
            tempFile = null;
        }
        if (mAdapter != null) {
            mAdapter.onDestory();
            System.gc();
        }
    }

    /**
     * 获取当前订单的施工项目和部位
     */
    private void getWorkItem() {
        Http.getInstance().getTaskToken(NetURL.getProjectItemv2(orderInfo.getId()), "", GetOrderProjectItemEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failure);
                    return;
                }
                if (entity instanceof GetOrderProjectItemEntity) {
                    GetOrderProjectItemEntity getOrderProjectItemEntity = (GetOrderProjectItemEntity) entity;
                    if (getOrderProjectItemEntity.isStatus()) {
                        GetOrderProjectItem[] getOrderProjectItemss = JSON.parseObject(getOrderProjectItemEntity.getMessage().toString(), GetOrderProjectItem[].class);
                        for (GetOrderProjectItem getOrderProjectItem : getOrderProjectItemss) {
                            getOrderProjectItems.add(getOrderProjectItem);
                        }
                        if (getOrderProjectItems.size() == 1) {
                            buttons[0].setVisibility(View.VISIBLE);
                            buttons[0].setText(getOrderProjectItems.get(0).getName());
                        } else if (getOrderProjectItems.size() == 2) {
                            buttons[0].setVisibility(View.VISIBLE);
                            buttons[1].setVisibility(View.VISIBLE);
                            buttons[0].setText(getOrderProjectItems.get(0).getName());
                            buttons[1].setText(getOrderProjectItems.get(1).getName());
                        } else if (getOrderProjectItems.size() == 3) {
                            buttons[0].setVisibility(View.VISIBLE);
                            buttons[1].setVisibility(View.VISIBLE);
                            buttons[2].setVisibility(View.VISIBLE);
                            buttons[0].setText(getOrderProjectItems.get(0).getName());
                            buttons[1].setText(getOrderProjectItems.get(1).getName());
                            buttons[2].setText(getOrderProjectItems.get(2).getName());
                        } else {
                            buttons[0].setVisibility(View.VISIBLE);
                            buttons[1].setVisibility(View.VISIBLE);
                            buttons[2].setVisibility(View.VISIBLE);
                            buttons[3].setVisibility(View.VISIBLE);
                            buttons[0].setText(getOrderProjectItems.get(0).getName());
                            buttons[1].setText(getOrderProjectItems.get(1).getName());
                            buttons[2].setText(getOrderProjectItems.get(2).getName());
                            buttons[3].setText(getOrderProjectItems.get(3).getName());
                        }
//                        for (int i = 0; i < getOrderProjectItems.get(0).getConstructionPositions().length; i++) {
//                            getOrderProjectItems.get(0).getConstructionPositions()[i].setProjectId(getOrderProjectItems.get(0).getId());
//                        }
                        buttons[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_choice_btn));
                        buttons[0].setTextColor(getResources().getColor(R.color.main_white));
                        buttons[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                        buttons[1].setTextColor(getResources().getColor(R.color.lightgray));
                        buttons[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                        buttons[2].setTextColor(getResources().getColor(R.color.lightgray));
                        buttons[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_default_btn));
                        buttons[3].setTextColor(getResources().getColor(R.color.lightgray));
                        listViewAdapter = new ListViewAdapter(technicians, getOrderProjectItems.get(0).getConstructionPositions(), WorkFinishActivity.this,getOrderProjectItems.get(0).getId());
                        listview_workItem.setAdapter(listViewAdapter);
                        setListViewHeightBasedOnChildren(listview_workItem);
                        if (getOrderProjectItems.get(0).getId() == 4){
                            gv_consume.setNumColumns(2);
                        }else {
                            gv_consume.setNumColumns(4);
                        }
                        consumeGridViewAdapter = new ConsumeGridViewAdapter(getOrderProjectItems.get(0).getConstructionPositions(), getOrderProjectItems.get(0).getId(), WorkFinishActivity.this);
                        gv_consume.setAdapter(consumeGridViewAdapter);
                        listViewAdapter.setOnGetHeight(new ListViewAdapter.OnGetHeight() {
                            @Override
                            public void getHeight() {
                                for (int i = 0; i < getOrderProjectItems.size(); i++) {
                                    for (int j = 0; j < getOrderProjectItems.get(i).getConstructionPositions().length; j++) {
                                        getOrderProjectItems.get(i).getConstructionPositions()[j].setTechnicianId(-1);
                                        getOrderProjectItems.get(i).getConstructionPositions()[j].setTotal(0);
                                        getOrderProjectItems.get(i).getConstructionPositions()[j].setCheck(false);
                                    }
                                }
                                listViewAdapter.notifyDataSetChanged();
                                consumeGridViewAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(listview_workItem);
                            }
                        });
//                        listViewAdapter.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                technicians.remove(listViewAdapter.getCheck());
//                                listViewAdapter.notifyDataSetChanged();
//                                setListViewHeightBasedOnChildren(listview_workItem);
//                            }
//                        });
                    } else {
                        T.show(getContext(), R.string.loading_data_failure);
                    }
                }
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
//            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 放弃订单
     *
     * @param v
     */
    public void onClickDropOrder(View v) {
        //显示放弃订单对话框
        if (dropOderDialog == null) {
            dropOderDialog = new DropOrderDialogFragment();
        }
        dropOderDialog.show(fragmentManager, "dropOderDialog");
    }

    @Override
    public void onDropClick(View v) {
        if (orderInfo == null) {
            T.show(getContext(), getString(R.string.not_found_order_tips));
            return;
        }
        Intent intent = new Intent(this, CancelOrderReasonActivity.class);
        intent.putExtra(AutoCon.ORDER_ID, orderInfo.getId());
        startActivity(intent);

    }

    private int uploadNum = 0;
    private int countNum;

    private class MyAsyncTask extends AsyncTask<String,Void,String>{
        private int index;
        private Uri uri;


        public MyAsyncTask(int index,Uri uri) {
            this.index = index;
            this.uri = uri;
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("task"+ (index + 1) + " is run " + DateCompute.timeStampToDate(System.currentTimeMillis()) + " thread id "+ Thread.currentThread().getId());
            try {
                String json = HttpClientInCar.uploadImage(params[0], params[1]);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploadNum++;
            if (uploadNum == countNum){
                cancelDialog();
            }
            System.out.println("task"+ (index + 1) + " is finish " + DateCompute.timeStampToDate(System.currentTimeMillis()));
            if (s == null) {
//                T.show(context, getString(R.string.upload_image_failed) + index);
                T.show(context, "第" + (index + 1) + "张图片上传失败，请重新选择上传");
                return;
            } else {
                IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                if (idPhotoEntity.isStatus()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                    mAdapter.addPic(bitmap, idPhotoEntity.getMessage());  //添加图片
                    Log.i("test", "上传压缩后的图片成功,width===>" + bitmap.getWidth() + ",height===>" + bitmap.getHeight()
                            + ",size===>" + (bitmap.getByteCount() / 1024) + "KB" + ",url===>" + idPhotoEntity.getMessage());
                } else {
//                    T.show(context, getString(R.string.upload_image_failed));
                    T.show(context, "第" + (index + 1) + "张图片上传失败，请重新选择上传");
                    return;
                }
            }
        }
    }
}
