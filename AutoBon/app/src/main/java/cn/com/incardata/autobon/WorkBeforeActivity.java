package cn.com.incardata.autobon;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.OrderInfo_Construction;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/11.
 * 工作前上传图像
 */
public class WorkBeforeActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private ImageView iv_my_info,iv_enter_more_page,iv_camera;
    private TextView tv_day,tv_has_time;
    private Button next_btn;
    private RelativeLayout rl_single_pic,rl_default_pic;

    private GridView gv_single_pic;
    private PictureGridAdapter mAdapter;
    private File tempFile;
    private String fileName = "";
    private static final int MAX_PICS = 9; //图片数上限

    private Uri carPhotoUri;
    private static final int CAR_PHOTO = 1;
    private static final int COUNT_TIME_FLAG = 1;

    private boolean isRunning = true;
    private OrderInfo_Data orderInfo;
    private OrderInfo_Construction construction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_before_activity);
        initView();
        initData();
        setListener();
        new Thread(new MyThread()).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COUNT_TIME_FLAG:
                    Bundle bundle = msg.getData();
                    int hour = bundle.getInt("hour");
                    int minute = bundle.getInt("minute");
                    int second = bundle.getInt("second");

                    tv_has_time.setText(hour+context.getString(R.string.tv_hour)+minute+context.getString(R.string.tv_minute)+second+context.getString(R.string.tv_second));
                    break;
            }
        }
    };

    private class MyThread implements Runnable{
        @Override
        public void run() {
            while (isRunning){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                long useTime = 0L;
                try{
                    long currentTime = System.currentTimeMillis();
                    long startWorkTime = construction.getSigninTime();
                    useTime = currentTime - startWorkTime;
                }catch (Exception e){
                    useTime = 0L;
                    e.printStackTrace();
                }
                final int hour = (int)(useTime / (1000*3600));
                final int minute =(int)((useTime - hour*(1000*3600)) / (1000*60));
                final int second = (int)((useTime - hour*(1000*3600) - minute*(1000*60)) / 1000);

                Message msg = Message.obtain();
                msg.what = COUNT_TIME_FLAG;
                Bundle bundle = new Bundle();
                bundle.putInt("hour",hour);
                bundle.putInt("minute",minute);
                bundle.putInt("second",second);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }

    private void initView(){
        context = this;
        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);

        iv_my_info = (ImageView) findViewById(R.id.iv_my_info);
        iv_enter_more_page =(ImageView) findViewById(R.id.iv_enter_more_page);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_has_time = (TextView) findViewById(R.id.has_use_time);
        next_btn = (Button) findViewById(R.id.next_btn);
        rl_single_pic = (RelativeLayout) findViewById(R.id.rl_single_pic);
        rl_default_pic = (RelativeLayout) findViewById(R.id.rl_default_pic);

        gv_single_pic = (GridView)findViewById(R.id.gv_single_pic);
        gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mAdapter = new PictureGridAdapter(this,MAX_PICS);
        gv_single_pic.setAdapter(mAdapter);

        if (orderInfo.getMainTech().getId() == MyApplication.getInstance().getUserId()){
            construction = orderInfo.getMainConstruct();
        }else{
            construction = orderInfo.getSecondConstruct();
        }
    }

    private void initData(){
        tv_day.setText(DateCompute.getWeekOfDate());
    }

    private void setListener(){
        iv_my_info.setOnClickListener(this);
        iv_enter_more_page.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        next_btn.setOnClickListener(this);

        gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == mAdapter.getCount()-1 && !mAdapter.isReachMax()){
                    capture(CAR_PHOTO,carPhotoUri);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_my_info:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.iv_enter_more_page:
                startActivity(MoreActivity.class);
                break;
            case R.id.iv_camera:
                uploadWorkPhoto();
                break;
            case R.id.next_btn:
                submitWorkBeforePhotoURL();
                break;
        }
    }

    private void initFile() {
        if(fileName.equals("")) {
            if(SDCardUtils.isExistSDCard()) {
                String path = SDCardUtils.getGatherDir() + File.separator + "my_picture";
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                fileName = path + File.separator +"my_photo.jpeg";
                tempFile = new File(fileName);
            } else {
                T.show(this,getString(R.string.uninstalled_sdcard));
            }
        }
    }

    private void uploadWorkPhoto(){
        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }
        if (carPhotoUri == null) {
            carPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "car_photo.jpeg"));
        }
        initFile();
        capture(CAR_PHOTO,carPhotoUri);
    }

    private void capture(int requestCode, Uri imageUri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 提交施工前图片地址
     */
    private void submitWorkBeforePhotoURL(){
        Map<Integer,String> picMap = mAdapter.getPicMap();
        if(picMap.size()<1){  //图片数量为0,提示用户
            T.show(this,getString(R.string.no_pic_tips_work_before));
            return;
        }
        BasicNameValuePair bv_orderId = new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId()));
        Collection<String> colUrls =  picMap.values();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = colUrls.iterator();
        while (iterator.hasNext()){
            sb.append(iterator.next()).append(",");
        }
        String urls = sb.toString();
        urls = urls.substring(0,urls.length()-1);
        Log.i("test","urls======>"+urls);
        BasicNameValuePair bv_urls = new BasicNameValuePair("urls",urls);

        Http.getInstance().postTaskToken(NetURL.SUBMIT_BEFORE_WORK_PHOTO_URL, IdPhotoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.upload_work_before_failed_tips));
                    return;
                }
                IdPhotoEntity idPhotoEntity = (IdPhotoEntity) entity;
                if(idPhotoEntity.isResult()){  //跳转
                    Intent intent = new Intent(context,WorkFinishActivity.class);
                    intent.putExtra(AutoCon.ORDER_INFO,orderInfo);
                    startActivity(intent);
                    finish();
                }else{
                    T.show(context,idPhotoEntity.getMessage());
                }
            }
        },bv_orderId,bv_urls);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if(requestCode == CAR_PHOTO){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(carPhotoUri));
                bitmap = BitmapHelper.resizeImage(bitmap, 0.5f);
                Uri uri = Uri.fromFile(tempFile);
                boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap, true);  //压缩图片保存到新地址

                if(isSuccess){
                    if(NetWorkHelper.isNetworkAvailable(context)){
                        uploadCarPhoto(uri);
                    }else{
                        T.show(context,context.getString(R.string.no_network_tips));
                        return;
                    }
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传车辆图片
     */
    private void uploadCarPhoto(Uri uri){
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
                if (s == null) {
                    T.show(context,getString(R.string.upload_image_failed));
                    return;
                }else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isResult()){
                        Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(tempFile).getPath());
                        //iv_car_upload_photo.setImageBitmap(bitmap);
                        rl_default_pic.setVisibility(View.GONE);
                        rl_single_pic.setVisibility(View.VISIBLE);
                        mAdapter.addPic(bitmap,idPhotoEntity.getData());  //添加图片
                    }else {
                        T.show(context,getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(uri.getPath(), NetURL.UPLOAD_WORK_PHOTO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false; //关闭计时线程
        //清除临时图片文件及目录
        if(tempFile!=null && tempFile.exists()){
            File dir = tempFile.getParentFile();
            SDCardUtils.deleteAllFileInFolder(dir);
            Log.i("test","dir===>"+dir.getPath());
            tempFile = null;
        }
    }
}
