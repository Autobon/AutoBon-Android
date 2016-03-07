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
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AvatarEntity;
import cn.com.incardata.http.response.CommitCertificateEntity;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.MyInfoEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * 认证
 * @author wanghao
 */
public class AuthorizeActivity extends BaseActivity implements View.OnClickListener{

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

    private Uri imageUri; //头像拍照
    private Uri imageCorpUri; //头像裁剪
    private Uri idPhotoUri; //身份证照片

    private boolean isAgain = false;//再次认证／修改认证信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        initView();
        checkStatus();
        initSpinner();
    }

    /**
     * 检查是否是再次认证
     */
    private void checkStatus() {
        if (isAgain){
            Http.getInstance().getTaskToken(NetURL.MY_INFO_URL, MyInfoEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(getContext(), R.string.get_info_failed);
                        return;
                    }
                    if (entity instanceof MyInfoEntity) {
                        MyInfoEntity apEntity = (MyInfoEntity) entity;
                        MyInfo_Data apData = apEntity.getData();
                        if (apData != null) {
                            nameStr = apData.getName();
                            idNumStr = apData.getIdNo();
                            bankNumStr = apData.getBankCardNo();
                            bankNameStr = apData.getBank();




                            name.setText(nameStr);
                            identifyNumber.setText(idNumStr);
                            bankNumber.setText(bankNumStr);

                            String skill = apData.getSkill();
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
                }
            });
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

        capture(GatherImage.CAPTURE_REQUEST, imageUri);
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
//        switch (item){
//            case 1:
//                skillArray[0] = !skillArray[0];
//                if (skillArray[0]) {
//                    skillItem1.setBackgroundResource(R.drawable.skill_on);
//                    skillItem1.setTextColor(Color.WHITE);
//                }else {
//                    skillItem1.setBackgroundResource(R.drawable.skill_off);
//                    skillItem1.setTextColor(getResources().getColor(R.color.darkgray));
//                }
//                skillItem1.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
//                break;
//            case 2:
//                skillArray[1] = !skillArray[1];
//                if (skillArray[1]) {
//                    skillItem2.setBackgroundResource(R.drawable.skill_on);
//                    skillItem2.setTextColor(Color.WHITE);
//                } else {
//                    skillItem2.setBackgroundResource(R.drawable.skill_off);
//                    skillItem2.setTextColor(getResources().getColor(R.color.darkgray));
//                }
//                skillItem2.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
//                break;
//            case 3:
//                skillArray[2] = !skillArray[2];
//                if (skillArray[2]) {
//                    skillItem3.setBackgroundResource(R.drawable.skill_on);
//                    skillItem3.setTextColor(Color.WHITE);
//                }else {
//                    skillItem3.setBackgroundResource(R.drawable.skill_off);
//                    skillItem3.setTextColor(getResources().getColor(R.color.darkgray));
//                }
//                skillItem3.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
//                break;
//            case 4:
//                skillArray[3] = !skillArray[3];
//                if (skillArray[3]) {
//                    skillItem4.setBackgroundResource(R.drawable.skill_on);
//                    skillItem4.setTextColor(Color.WHITE);
//                }else {
//                    skillItem4.setBackgroundResource(R.drawable.skill_off);
//                    skillItem4.setTextColor(getResources().getColor(R.color.darkgray));
//                }
//                skillItem4.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
//                break;
//        }
    }

    private void onClickIdentifyPhoto() {
        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (idPhotoUri == null) {
            idPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "idPhoto.jpeg"));
        }
        capture(GatherImage.CAPTURE_ID_REQUEST, idPhotoUri);
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

        if (TextUtils.isEmpty(bankNumStr)){
            T.show(this, R.string.bank_number_error);
            return;
        }

        if (!isUploadIDImage){
            T.show(this, getString(R.string.requirement_id_photo));
            return;
        }

        if (!(skillArray[0] || skillArray[1] || skillArray[2] || skillArray[3])){
            T.show(this, R.string.skill_item_empty);
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
//        params.add(new BasicNameValuePair("idPhoto", "photo/url"));//暂不要该字段
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
        if (requestCode == GatherImage.CAPTURE_REQUEST){
            crop();
            return;
        }
        if (requestCode == GatherImage.CROP_REQUEST){
            uploadHeadImage();
            return;
        }
        if (requestCode == GatherImage.CAPTURE_ID_REQUEST){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(idPhotoUri));
                bitmap = BitmapHelper.resizeImage(bitmap, 0.5f);
                BitmapHelper.saveBitmap(idPhotoUri, bitmap, true);
                uploadIdPhoto();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
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
                    isUploadIDImage = false;
                    T.show(getContext(), getString(R.string.upload_image_failed));
                    return;
                }else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isResult()){
                        isUploadIDImage = true;
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
}
