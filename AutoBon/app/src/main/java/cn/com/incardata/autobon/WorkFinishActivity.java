package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/11.
 */
public class WorkFinishActivity extends Activity{
    private GridView gv_single_pic;
    private PictureGridAdapter mAdapter;
    private static final int MAX_PICS = 6; //图片数上限

    private File tempFile;
    private String fileName = "";
    private static final int OPEN_GALLERY_CODE = 1;
    private static final int CROP_PHOTO_CODE = 2;
    private static final int CROP = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_finish_activity);
        initView();
    }

    private void initView() {
        gv_single_pic = (GridView)findViewById(R.id.gv_single_pic);
        mAdapter = new PictureGridAdapter(this,MAX_PICS);
        gv_single_pic.setAdapter(mAdapter);

        gv_single_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == mAdapter.getCount()-1 && !mAdapter.isReachMax()){
                    initFile();
                    openGallery(); //从相册选取照片,显示到界面上
                }
            }
        });
    }

    private void initFile() {
        if(fileName.equals("")) {
            if(SDCardUtils.isExistSDCard()) {
                String path = Environment.getExternalStorageDirectory() + File.separator + "my_picture";
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                fileName = path + File.separator +"my_photo.jpg";
                tempFile = new File(fileName);
            } else {
                T.show(this,getString(R.string.uninstalled_sdcard));
            }
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile)); //将选中的相册的图像结果写入到临时文件tempFile
        startActivityForResult(intent, OPEN_GALLERY_CODE);
    }

    /**
     * 裁剪图片
     * @param uri
     * @param crop 裁剪大小
     */
    public void cropPhoto(Uri uri,int crop) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",crop);
        intent.putExtra("outputY",crop);
        startActivityForResult(intent, CROP_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1){
            return;
        }
        switch (requestCode) {
            case OPEN_GALLERY_CODE:
                if(data!=null){
                    cropPhoto(data.getData(),CROP);
                }
                break;
            case CROP_PHOTO_CODE:
                try{
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;  // 先设置为TRUE不加载到内存中，但可以得到宽和高

                    Bitmap bitmap = BitmapFactory.decodeFile(fileName,options);
                    options.inJustDecodeBounds = false;
                    int be = (int) (options.outHeight / (float) 200);  // 计算缩放比
                    if (be <= 0){
                        be = 1;
                    }
                    options.inSampleSize = be;

                    // 这样就不会内存溢出了
                    bitmap = BitmapFactory.decodeFile(fileName, options);
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
    protected void onDestroy() {
        super.onDestroy();
        //清除临时图片文件
        if(tempFile!=null && tempFile.exists()){
            tempFile.delete();
            tempFile = null;
        }
    }
}
