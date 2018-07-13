package cn.com.incardata.autobon;

import android.app.FragmentManager;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

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

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.customfun.GatherImage;
import cn.com.incardata.fragment.DropOrderDialogFragment;
import cn.com.incardata.fragment.ImageChooseFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntityTwo;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.VerifySmsEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/11.
 * 工作前上传图像
 */
public class WorkBeforeActivity extends BaseActivity implements View.OnClickListener, DropOrderDialogFragment.OnClickListener, ImageChooseFragment.OnFragmentInteractionListener {
    private Context context;
    private ImageView iv_my_info, iv_enter_more_page, iv_back;
    private TextView tv_day;
    private Button btn_submit;
    private EditText edit_remark;
    private Button next_btn;
    private RelativeLayout rl_single_pic, rl_default_pic;

    private GridView gv_single_pic;
    private PictureGridAdapter mAdapter;
    private File tempFile;
    private File tempDir;
    private String fileName = "";
    private String fileName1 = "";  //my_picture目录
    private static final int MAX_PICS = 9; //图片数上限

    private Uri carPhotoUri;
    private static final int CAR_PHOTO = 1;
    private static final int COUNT_TIME_FLAG = 1;

    private boolean isRunning = true;
    private OrderInfo orderInfo;
    private OrderInfo_Construction construction;
    private DropOrderDialogFragment dropOderDialog;
    private FragmentManager fragmentManager;

    private ImageChooseFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_before_activity);
        fragmentManager = getFragmentManager();
        initView();
        initFile();
        initData();
        setListener();
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

//                    tv_has_time.setText(hour+context.getString(R.string.tv_hour)+minute+context.getString(R.string.tv_minute)+second+context.getString(R.string.tv_second));
                    break;
            }
        }
    };

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
                    long startWorkTime = construction.getSigninTime();
                    useTime = currentTime - startWorkTime;
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

    private void initView() {
        context = this;
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);

        iv_my_info = (ImageView) findViewById(R.id.iv_my_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_enter_more_page =(ImageView) findViewById(R.id.iv_enter_more_page);
        tv_day = (TextView) findViewById(R.id.tv_day);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edit_remark = (EditText) findViewById(R.id.edit_remark);
//        tv_has_time = (TextView) findViewById(R.id.has_use_time);
        next_btn = (Button) findViewById(R.id.next_btn);
        rl_single_pic = (RelativeLayout) findViewById(R.id.rl_single_pic);
        rl_default_pic = (RelativeLayout) findViewById(R.id.rl_default_pic);

        gv_single_pic = (GridView) findViewById(R.id.gv_single_pic);
        mAdapter = new PictureGridAdapter(this, MAX_PICS);
        gv_single_pic.setAdapter(mAdapter);

//        if (orderInfo.getMainTech().getId() == MyApplication.getInstance().getUserId()){
//            construction = orderInfo.getMainConstruct();
//        }else{
//            construction = orderInfo.getSecondConstruct();
//        }
    }

    private void initData() {
        tv_day.setText(DateCompute.getWeekOfDate());
    }

    private void setListener() {
        iv_my_info.setOnClickListener(this);
//        iv_enter_more_page.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        findViewById(R.id.rl_default_pic).setOnClickListener(this);

        gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mAdapter.getCount() - 1 && !mAdapter.isReachMax()) {
//                    if (tempDir == null) {
//                        tempDir = new File(SDCardUtils.getGatherDir());
//                    }
                    Album.startAlbum(WorkBeforeActivity.this, 0x999
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_info:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;
//            case R.id.iv_enter_more_page:
//                startActivity(MoreActivity.class);
//                break;
            case R.id.rl_default_pic:
                Album.startAlbum(WorkBeforeActivity.this, 0x999
                        , 9 - mAdapter.getPicMap().size()                                                         // 指定选择数量。
                        , ContextCompat.getColor(context, R.color.main_orange)        // 指定Toolbar的颜色。
                        , ContextCompat.getColor(context, R.color.main_orange));  // 指定状态栏的颜色
//                onClickIdentifyPhoto();
//                uploadWorkPhoto();
                break;
            case R.id.btn_submit:
                String remark = edit_remark.getText().toString().trim();
                if (!TextUtils.isEmpty(remark)) {
                    submitRemark(remark);
                } else {
                    T.show(context, "请填写备注");
                    return;
                }
                break;
            case R.id.next_btn:
                submitWorkBeforePhotoURL();
                break;
        }
    }

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

//    private void uploadWorkPhoto() {
//        if (!SDCardUtils.isExistSDCard()) {
//            T.show(this, R.string.uninstalled_sdcard);
//            return;
//        }
//        if (carPhotoUri == null) {
//            carPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "car_photo.jpeg"));
//        }
//        initFile();
//        capture(CAR_PHOTO, carPhotoUri);
//    }

    private void capture(int requestCode, Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 提交订单备注
     *
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
                } else {
                    T.show(getContext(), listNewEntity.getMessage().toString());
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    /**
     * 提交施工前图片地址
     */
    private void submitWorkBeforePhotoURL() {
        Map<Integer, String> picMap = mAdapter.getPicMap();
        if (picMap.size() < 3) {  //图片数量为0,提示用户
            T.show(this, getString(R.string.no_pic_tips_work_before));
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
        BasicNameValuePair bv_urls = new BasicNameValuePair("urls", urls);
        String json = "?orderId=" + orderInfo.getId() + "&urls=" + urls;

        Http.getInstance().putTaskToken(NetURL.SUBMIT_BEFORE_WORK_PHOTO_URLV2 + json, "", IdPhotoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(context, context.getString(R.string.upload_work_before_failed_tips));
                    return;
                }
                IdPhotoEntity idPhotoEntity = (IdPhotoEntity) entity;
                if (idPhotoEntity.isStatus()) {  //跳转
                    orderInfo.setStartTime(System.currentTimeMillis());
                    orderInfo.setStatus("AT_WORK");
                    Intent intent = new Intent(context, WorkFinishActivity.class);
                    intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
                    startActivity(intent);
                    finish();
                } else {
                    T.show(context, idPhotoEntity.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
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

        }
//        if (requestCode == CAR_PHOTO) {
//            try {
//                Bitmap bitmap = BitmapHelper.resizeImage(getContext(), carPhotoUri, 0.35f);
//                Uri uri = Uri.fromFile(tempFile);
//                boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);  //压缩图片保存到新地址
//
//                if (isSuccess) {
//                    if (NetWorkHelper.isNetworkAvailable(context)) {
//                        uploadCarPhoto(uri);
//                    } else {
//                        T.show(context, context.getString(R.string.no_network_tips));
//                        return;
//                    }
//                }
//
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 上传车辆图片
     */
    private void uploadCarPhoto(Uri uri) {
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
                        //iv_car_upload_photo.setImageBitmap(bitmap);
                        rl_default_pic.setVisibility(View.GONE);
                        rl_single_pic.setVisibility(View.VISIBLE);
                        mAdapter.addPic(bitmap, idPhotoEntity.getMessage());  //添加图片
                    } else {
                        T.show(context, getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(uri.getPath(), NetURL.UPLOAD_WORK_PHOTOV2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false; //关闭计时线程
        if (tempDir != null && tempDir.exists()) {
            SDCardUtils.deleteAllFileInFolder(tempDir);  //销毁临时目录及文件
        }
        //清除临时图片文件及目录
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
                    rl_default_pic.setVisibility(View.GONE);
                    rl_single_pic.setVisibility(View.VISIBLE);
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
