package cn.com.incardata.autobon;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.fragment.BaseStandardFragment;
import cn.com.incardata.fragment.FiveCarRadioFragment;
import cn.com.incardata.fragment.SevenCarRadioFragment;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.SDCardUtils;

/**
 * Created by zhangming on 2016/3/11.
 */
public class WorkFinishActivity extends Activity implements BaseStandardFragment.OnFragmentInteractionListener{
    private GridView gv_single_pic;
    private RadioGroup rg_tab;
    private TextView tv_day;
    private PictureGridAdapter mAdapter;
    private static final int MAX_PICS = 6; //图片数上限

    private File tempDir;
    private Uri carPhotoUri;
    private static final int CAR_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_finish_activity);
        initView();
    }

    private void initView() {
        tv_day = (TextView) findViewById(R.id.tv_day);
        gv_single_pic = (GridView)findViewById(R.id.gv_single_pic);
        rg_tab = (RadioGroup)findViewById(R.id.rg_tab);

        tv_day.setText(DateCompute.getWeekOfDate());
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
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.five_radio_btn){  //选中五座车的RadioButton
                    replaceFragment(FiveCarRadioFragment.class);
                }else if(checkedId == R.id.seven_radio_btn){  //选中七座车的RadioButton
                    replaceFragment(SevenCarRadioFragment.class);
                }
            }
        });
    }

    private <T> void replaceFragment(Class<T> cls){
        try{
            T fragment = BaseStandardFragment.newInstance(cls);
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

                    //TODO 上传图片
                    mAdapter.addPic(bitmap,"");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(tempDir!=null && tempDir.exists()){
            SDCardUtils.deleteAllFileInFolder(tempDir);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
