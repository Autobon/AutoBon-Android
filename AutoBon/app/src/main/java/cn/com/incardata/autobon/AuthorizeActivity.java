package cn.com.incardata.autobon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cn.com.incardata.adapter.BankNameAdapter;
import cn.com.incardata.customfun.GatherImage;
import cn.com.incardata.fragment.ImageChooseFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AvatarEntity;
import cn.com.incardata.http.response.CommitCertificateEntity;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.utils.BankUtil;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * 认证
 * @author wanghao
 */
public class AuthorizeActivity extends BaseActivity implements View.OnClickListener, ImageChooseFragment.OnFragmentInteractionListener{

    private ImageView headerImage;
    private EditText name;
    private EditText identifyNumber;
    private TextView[] skillItem = new TextView[4];
    private ImageView identifyPhoto;
    private Spinner bankSpinner;
    private EditText bankNumber;
    private Button submit;
    private TextView authorizeAgreement;

    private boolean[] skillArray = new boolean[4];
    private String nameStr; //姓名
    private String idNumStr; //身份证号
    private String bankNumStr; //银行卡号
    private String bankNameStr; //银行卡所属银行
    private boolean isUploadIDImage = false;//身份证照片是否已上传
    private String IDImageUrl;

    private Uri imageUri; //头像拍照
    private Uri imageCorpUri; //头像裁剪
    private Uri idPhotoUri; //身份证照片

    private boolean isAgain = false;//再次认证／修改认证信息
    private ImageChooseFragment mFragment;
    /**
     * 是否需要裁剪
     */
    private boolean isCrop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        initView();
        initSpinner();
        checkStatus();
    }

    /**
     * 检查是否是再次认证
     */
    private void checkStatus() {
        if (isAgain){
            submit.setText(R.string.again_authorization);

            nameStr = getIntent().getStringExtra("name");
            idNumStr = getIntent().getStringExtra("idNumber");
            String headUrl = getIntent().getStringExtra("headUrl");
            String skill = getIntent().getStringExtra("skillArray");
            IDImageUrl = getIntent().getStringExtra("idUrl");
            bankNumStr = getIntent().getStringExtra("bankNo");
            bankNameStr = getIntent().getStringExtra("bankName");

            name.setText(nameStr);
            identifyNumber.setText(idNumStr);
            bankNumber.setText(bankNumStr);
            if (!TextUtils.isEmpty(IDImageUrl)){
                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + IDImageUrl, identifyPhoto, false);
                isUploadIDImage = true;
            }
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + headUrl, headerImage, false);

            String[] bankArray = getResources().getStringArray(R.array.bank_array);
            for(int i=0;i<bankArray.length;i++){
                if(bankNameStr.equals(bankArray[i])){
                    bankSpinner.setSelection(i);
                    break;
                }
            }

            if (TextUtils.isEmpty(skill)) return;
            String[] skillAr = skill.split(",");
            for (String element : skillAr){
                try{
                    int ski = Integer.parseInt(element);
                    onClickSkillItem(ski -1);
                }catch(NumberFormatException e){
                    continue;
                }
            }
        }
    }

    private void initView() {
        headerImage = (ImageView) findViewById(R.id.header_image);
        name = (EditText) findViewById(R.id.name);
        identifyNumber = (EditText) findViewById(R.id.IDNo);
        skillItem[0] = (TextView) findViewById(R.id.skill_item_1);
        skillItem[1] = (TextView) findViewById(R.id.skill_item_2);
        skillItem[2] = (TextView) findViewById(R.id.skill_item_3);
        skillItem[3] = (TextView) findViewById(R.id.skill_item_4);
        identifyPhoto = (ImageView) findViewById(R.id.identify_photo);
        bankSpinner = (Spinner) findViewById(R.id.carInfo_brand);
        bankNumber = (EditText) findViewById(R.id.bank_number);
        submit = (Button) findViewById(R.id.submit);
        authorizeAgreement = (TextView) findViewById(R.id.authorize_agreement);

        findViewById(R.id.iv_back).setOnClickListener(this);
        headerImage.setOnClickListener(this);
        skillItem[0].setOnClickListener(this);
        skillItem[1].setOnClickListener(this);
        skillItem[2].setOnClickListener(this);
        skillItem[3].setOnClickListener(this);
        identifyPhoto.setOnClickListener(this);
        submit.setOnClickListener(this);
        authorizeAgreement.setOnClickListener(this);

        isAgain = getIntent().getBooleanExtra("isAgain", false);
    }

    /**
     * 银行名单
     */
    private void initSpinner() {
        String[] bankArray = getResources().getStringArray(R.array.bank_array);
        BankNameAdapter engineAdapter = new BankNameAdapter(this, bankArray);
        bankSpinner.setAdapter(engineAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.header_image:
                isCrop = true;
                onClickHeaderImage();
                break;
            case R.id.skill_item_1:
                onClickSkillItem(0);
                break;
            case R.id.skill_item_2:
                onClickSkillItem(1);
                break;
            case R.id.skill_item_3:
                onClickSkillItem(2);
                break;
            case R.id.skill_item_4:
                onClickSkillItem(3);
                break;
            case R.id.identify_photo:
                isCrop = false;
                onClickIdentifyPhoto();
                break;
            case R.id.submit:
                onClickSubmit();
                break;
            case R.id.authorize_agreement:
                onClickAuthorizeAgreement();
                break;
        }
    }

    private void onClickHeaderImage() {
        if (mFragment == null){
            mFragment = new ImageChooseFragment();
        }

        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (imageUri == null) {
            File file1 = new File(SDCardUtils.getGatherDir() + File.separator + "image.jpeg");
            File file2 = new File(SDCardUtils.getGatherDir() + File.separator + "imageCorpUri.jpeg");

            if (file1.exists()){
                file1.delete();
            }
            if (file2.exists()){
                file2.delete();
            }
            imageUri = Uri.fromFile(file1);
            imageCorpUri = Uri.fromFile(file2);
        }

        mFragment.show(getFragmentManager(), "Choose");
//        capture(GatherImage.CAPTURE_REQUEST, imageUri);
    }

    private void onClickSkillItem(int item) {
        int paddingPixel = getResources().getDimensionPixelSize(R.dimen.dp5);

        skillArray[item ] = !skillArray[item];

        if (skillArray[item]){
            skillItem[item].setBackgroundResource(R.drawable.skill_on);
            skillItem[item].setTextColor(Color.WHITE);
        }else {
            skillItem[item].setBackgroundResource(R.drawable.skill_off);
            skillItem[item].setTextColor(getResources().getColor(R.color.darkgray));
        }
        skillItem[item].setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
    }

    private void onClickIdentifyPhoto() {
        if (mFragment == null){
            mFragment = new ImageChooseFragment();
        }

        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (idPhotoUri == null) {
            idPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "idPhoto.jpeg"));
        }
        mFragment.show(getFragmentManager(), "Choose");
    }

    private void onClickSubmit() {
        obtainEdit();
        if (TextUtils.isEmpty(nameStr)){
            T.show(this, R.string.name_error);
            return;
        }

        if (TextUtils.isEmpty(idNumStr)){
            T.show(this, R.string.identify_number_error);
            return;
        }

        if (!idNumStr.matches("^(\\d{14}|\\d{17})(\\d|[xX])$")){
            T.show(this, getString(R.string.please_check_identify));
            return;
        }

        if (!(skillArray[0] || skillArray[1] || skillArray[2] || skillArray[3])){
            T.show(this, R.string.skill_item_empty);
            return;
        }

        if (!isUploadIDImage){
            T.show(this, getString(R.string.requirement_id_photo));
            return;
        }

        if (TextUtils.isEmpty(bankNumStr)){
            T.show(this, R.string.bank_number_error);
            return;
        }

        if (!(bankNumStr.trim().length() >= 16 && BankUtil.checkBankCard(bankNumStr))){
            T.show(this, "请检查银行卡号");
            return;
        }

        String skillArray = "";
        for (int i = 0; i < 4; i++){
            if (this.skillArray[i]){
                skillArray += (i + 1) + ",";
            }
        }
        skillArray = skillArray.substring(0, skillArray.length() - 1);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", nameStr));
        params.add(new BasicNameValuePair("idNo", idNumStr));
        params.add(new BasicNameValuePair("skills", skillArray));
        params.add(new BasicNameValuePair("idPhoto", IDImageUrl));
        params.add(new BasicNameValuePair("bank", bankNameStr));
        params.add(new BasicNameValuePair("bankAddress", "china"));
        params.add(new BasicNameValuePair("bankCardNo", bankNumStr));

        Http.getInstance().postTaskToken(NetURL.COMMIT_CERTIFICATE, CommitCertificateEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){
                    T.show(getContext(), R.string.network_exception);
                    return;
                }
                if (entity instanceof CommitCertificateEntity ){
                    CommitCertificateEntity certificateEntity = (CommitCertificateEntity) entity;
                    if (certificateEntity.isResult()){
                        setResult(RESULT_OK);//携带结果返回
                        finish();
                    }else {
                        T.show(getContext(), R.string.operate_failed_agen);
                        return;
                    }
                }
            }
        }, (NameValuePair[]) params.toArray(new NameValuePair[params.size()]));
    }

    private void obtainEdit() {
        nameStr = name.getText().toString();
        idNumStr = identifyNumber.getText().toString();
        bankNameStr = bankSpinner.getSelectedItem().toString();
        bankNumStr = bankNumber.getText().toString();
    }

    private void onClickAuthorizeAgreement() {
        startActivity(AuthorizeAgreementActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode){
            case GatherImage.CAPTURE_REQUEST:
                crop();
                break;
            case GatherImage.CROP_REQUEST:
                uploadHeadImage();
                break;
            case GatherImage.CAPTURE_ID_REQUEST:
                idPhotoProcess(idPhotoUri);
                break;
            case GatherImage.GALLERY_REQUEST:
                if (isCrop){
                    if (data != null){
                        imageUri = data.getData();
                        crop();
                    }else {
                        T.show(getContext(), R.string.operate_failed_agen);
                        break;
                    }
                }else {
                    idPhotoProcess(data.getData());
                }
                break;
        }
    }

    private void idPhotoProcess(Uri uri){
        try {
            Bitmap bitmap = BitmapHelper.resizeImage(getContext(), uri, 0.5f);
            if (BitmapHelper.saveBitmap(this.idPhotoUri, bitmap)) {
                uploadIdPhoto();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
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

    private void crop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCorpUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", "jpeg");
        startActivityForResult(intent, GatherImage.CROP_REQUEST);
    }

    private void uploadHeadImage(){
        showDialog(getString(R.string.uploading_image));
        ImageLoaderCache.remove(NetURL.IP_PORT + getIntent().getStringExtra("headUrl"));
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
                    T.show(getContext(), getString(R.string.upload_image_failed));
                    return;
                }else {
                    AvatarEntity avatarEntity = JSON.parseObject(s, AvatarEntity.class);
                    if (avatarEntity.isResult()){
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageCorpUri));
                            headerImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else {
                        T.show(getContext(), getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(imageCorpUri.getPath(), NetURL.AVATAR);
    }

    private void uploadIdPhoto(){
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
                    isUploadIDImage = false;
                    T.show(getContext(), getString(R.string.upload_image_failed));
                    return;
                }else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isResult()){
                        isUploadIDImage = true;
                        IDImageUrl = idPhotoEntity.getData();
                        Bitmap bitmap = BitmapFactory.decodeFile(idPhotoUri.getPath());
                        identifyPhoto.setImageBitmap(bitmap);
                    }else {
                        T.show(getContext(), getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(idPhotoUri.getPath(), NetURL.ID_PHOTO);
    }

    /**
     * 选图对话框
     * @param type 操作类型
     */
    @Override
    public void onFragmentInteraction(int type) {
        switch (type){
            case GatherImage.CAPTURE:
                if (isCrop){
                    capture(GatherImage.CAPTURE_REQUEST, imageUri);
                }else {
                    capture(GatherImage.CAPTURE_ID_REQUEST, idPhotoUri);
                }
                break;
            case GatherImage.GALLERY:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GatherImage.GALLERY_REQUEST);
                break;
        }
    }
}
