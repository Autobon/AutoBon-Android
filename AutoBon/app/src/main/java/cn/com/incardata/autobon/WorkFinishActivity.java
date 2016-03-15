package cn.com.incardata.autobon;

import android.app.Activity;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.adapter.RadioFragmentGridAdapter;
import cn.com.incardata.fragment.BaseStandardFragment;
import cn.com.incardata.fragment.FiveCarRadioFragment;
import cn.com.incardata.fragment.SevenCarRadioFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.FinishWorkEntity;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.SharedPre;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/11.
 */
public class WorkFinishActivity extends Activity implements BaseStandardFragment.OnFragmentInteractionListener{
    private GridView gv_single_pic;
    private RadioGroup rg_tab;
    private TextView tv_day,tv_has_time;
    private PictureGridAdapter mAdapter;
    private LinearLayout ll_other,ll_clean;
    private Button finish_work_btn;
    private Context context;
    private static final int MAX_PICS = 6; //图片数上限

    private File tempDir;
    private Uri carPhotoUri;
    private static final int CAR_PHOTO = 1;

    private Handler handler = new Handler();
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_finish_activity);
        initView();
        //handler.postDelayed(runnable,1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning){
                String startWorkTime = SharedPre.getString(context,AutoCon.START_WORK_TIMER);
                long currentTime = System.currentTimeMillis();
                long useTime = currentTime - Long.parseLong(startWorkTime);
                Log.i("test","currentTime===>"+currentTime+",useTime===>"+useTime);
                final long hour = useTime / (1000*3600);
                final long minute = useTime / (1000*60);
                final long second = useTime / 1000;
                tv_has_time.setText(hour+context.getString(R.string.tv_hour)+
                        minute+context.getString(R.string.tv_minute)+second+context.getString(R.string.tv_second));
                handler.postDelayed(runnable,1000);
            }
        }
    };


    private void initView() {
        context = this;
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_has_time = (TextView) findViewById(R.id.has_use_time);
        gv_single_pic = (GridView)findViewById(R.id.gv_single_pic);
        rg_tab = (RadioGroup)findViewById(R.id.rg_tab);
        tv_day.setText(DateCompute.getWeekOfDate());

        ll_other = (LinearLayout) findViewById(R.id.ll_other);
        ll_clean = (LinearLayout) findViewById(R.id.ll_clean);
        finish_work_btn = (Button) findViewById(R.id.finish_work_btn);

        if(AutoCon.orderType == 4){  //订单类型为美容清洁
            ll_clean.setVisibility(View.VISIBLE);
            ll_other.setVisibility(View.GONE);
            //TODO
        }else{
            ll_clean.setVisibility(View.GONE);
            ll_other.setVisibility(View.VISIBLE);

            mAdapter = new PictureGridAdapter(this,MAX_PICS);
            gv_single_pic.setAdapter(mAdapter);
            replaceFragment(FiveCarRadioFragment.class);  //默认是五座车的Fragment

            gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("test","position===>"+position+","+"count===>"+mAdapter.getCount());
                    if(position == mAdapter.getCount()-1 && !mAdapter.isReachMax()){
                        if(tempDir==null){
                            tempDir = new File(SDCardUtils.getGatherDir());
                            carPhotoUri = Uri.fromFile(new File(tempDir,"car_photo.jpeg"));
                        }
                        capture(CAR_PHOTO,carPhotoUri);
                    }
                }
            });
            rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup roup, int checkedId) {
                    if(checkedId == R.id.five_radio_btn){  //选中五座车的RadioButton
                        replaceFragment(FiveCarRadioFragment.class);
                    }else if(checkedId == R.id.seven_radio_btn){  //选中七座车的RadioButton
                        replaceFragment(SevenCarRadioFragment.class);
                    }
                    if(RadioFragmentGridAdapter.workItemMap!=null && RadioFragmentGridAdapter.workItemMap.size()>0){
                        RadioFragmentGridAdapter.workItemMap.clear(); //清空记录工作项的map集合
                    }
                }
            });

            finish_work_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(NetWorkHelper.isNetworkAvailable(context)){
                        submitFinishWorkInfo();
                    }else{
                        T.show(context,context.getString(R.string.no_network_error));
                        return;
                    }
                }
            });
        }
    }

    private <T> void replaceFragment(Class<T> cls){
        try{
            T fragment = BaseStandardFragment.newInstance(cls);  //获取fragment实例
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(fragment instanceof BaseStandardFragment){
                BaseStandardFragment bs_fragment = (BaseStandardFragment) fragment;
                transaction.replace(R.id.fragment_container,bs_fragment);
            }
            transaction.commit();
        }catch(Exception e){
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
    private void submitFinishWorkInfo(){
        //TODO 获取上传的图片
        Map<Integer,String> picMap = mAdapter.getPicMap();
        if(picMap.size()<1){  //图片数量为0,提示用户
            T.show(context,context.getString(R.string.no_pic_tips));
            return;
        }
        BasicNameValuePair bv_orderId = new BasicNameValuePair("orderId", String.valueOf(AutoCon.orderId));
        Collection<String> colUrls =  picMap.values();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = colUrls.iterator();
        while (iterator.hasNext()){
            sb.append(iterator.next()).append(",");
        }
        String urls = sb.toString();
        urls = urls.substring(0,urls.length()-1);
        Log.i("test","urls======>"+urls);
        BasicNameValuePair bv_afterPhotos = new BasicNameValuePair("afterPhotos",urls);

        if(AutoCon.orderType==4){  //美容清洁

        }else{
            BasicNameValuePair bv_carSeat = null;
            int checkId = rg_tab.getCheckedRadioButtonId();  //获取选中的RadioButton的id
            Log.i("test","checkId===>"+checkId);

            if(checkId == R.id.five_radio_btn){  //选中五座
                bv_carSeat = new BasicNameValuePair("carSeat",String.valueOf(AutoCon.five_carSeat));
            }else if(checkId == R.id.seven_radio_btn){ //选中七座
                bv_carSeat = new BasicNameValuePair("carSeat",String.valueOf(AutoCon.seven_carSeat));
            }

            StringBuilder s = new StringBuilder();
            Set<Map.Entry<Integer,String>> keySets = RadioFragmentGridAdapter.workItemMap.entrySet();
            for(Map.Entry<Integer,String> entry : keySets){
                Log.i("test","key===>"+entry.getKey()+",value===>"+entry.getValue());
                s.append(entry.getKey()).append(",");
            }
            String workItemStr = s.toString();
            if(StringUtil.isNotEmpty(workItemStr)){
                workItemStr = workItemStr.substring(0,workItemStr.length()-1);
                Log.i("test","workItemStr===>"+workItemStr);
            }
            BasicNameValuePair bv_workItems = new BasicNameValuePair("workItems",workItemStr);

            Http.getInstance().postTaskToken(NetURL.WORK_FINISH_URL, FinishWorkEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if(entity == null){
                        T.show(context,context.getString(R.string.operate_failed_tips));
                        return;
                    }
                    FinishWorkEntity finishWorkEntity = (FinishWorkEntity)entity;
                    if(finishWorkEntity.isResult()){
                        //TODO 跳转页面
                        T.show(context,"orderId===>"+finishWorkEntity.getData().getOrderId());
                    }else{
                        T.show(context,finishWorkEntity.getMessage());
                    }
                }
            },bv_orderId,bv_afterPhotos,bv_carSeat,bv_workItems);
        }
    }


    private void capture(int requestCode, Uri imageUri){
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
                try{
                    //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(carPhotoUri));
                    //bitmap = BitmapHelper.resizeImage(bitmap, 0.5f);
                    //BitmapHelper.saveBitmap(carPhotoUri, bitmap, false);
                    //Log.i("test","carPhotoUri_path===>"+carPhotoUri.getPath());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;  // 先设置为TRUE不加载到内存中，但可以得到宽和高
                    Bitmap bitmap = BitmapFactory.decodeFile(carPhotoUri.getPath(),options);
                    options.inJustDecodeBounds = false;
                    int be = (int) (options.outHeight / (float) 200);  // 计算缩放比
                    if (be <= 0){
                        be = 1;
                    }
                    options.inSampleSize = be;
                    bitmap = BitmapFactory.decodeFile(carPhotoUri.getPath(), options);
                    bitmap = BitmapHelper.resizeImage(bitmap, 0.5f);
                    Uri uri = Uri.fromFile(new File(tempDir,new Random().nextInt(10000)+".jpeg")); //产生不同的uri
                    Log.i("test","uri===>"+uri.getPath());
                    BitmapHelper.saveBitmap(uri,bitmap,false);

                    //TODO 上传压缩后的图片
                    uploadCarPhoto(uri);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadCarPhoto(final Uri carCompressUri){
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
                        Bitmap bitmap = BitmapFactory.decodeFile(carCompressUri.getPath());
                        mAdapter.addPic(bitmap,idPhotoEntity.getData());  //添加图片
                        Log.i("test","上传压缩后的图片成功,width===>"+bitmap.getWidth()+",height===>"+bitmap.getHeight()
                                +",size===>"+(bitmap.getByteCount()/1024)+"KB"+",url===>"+idPhotoEntity.getData());
                    }else {
                        T.show(context,getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(carCompressUri.getPath(), NetURL.UPLOAD_WORK_PHOTO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("test","onDestroy...");
        if(tempDir!=null && tempDir.exists()){
            SDCardUtils.deleteAllFileInFolder(tempDir);
        }
        isRunning = false; //关闭计时线程
    }
}
