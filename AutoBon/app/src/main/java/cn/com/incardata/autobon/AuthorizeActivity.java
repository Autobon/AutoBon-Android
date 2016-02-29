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
    private TextView skillItem1, skillItem2, skillItem3, skillItem4;
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
    private boolean isUploadIDImage;//身份证照片是否已上传

    private Uri imageUri; //头像拍照
    private Uri imageCorpUri; //头像裁剪
    private Uri idPhotoUri; //身份证照片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        initView();
        initSpinner();
    }

    private void initView() {
        headerImage = (ImageView) findViewById(R.id.header_image);
        name = (EditText) findViewById(R.id.name);
        identifyNumber = (EditText) findViewById(R.id.IDNo);
        skillItem1 = (TextView) findViewById(R.id.skill_item_1);
        skillItem2 = (TextView) findViewById(R.id.skill_item_2);
        skillItem3 = (TextView) findViewById(R.id.skill_item_3);
        skillItem4 = (TextView) findViewById(R.id.skill_item_4);
        identifyPhoto = (ImageView) findViewById(R.id.identify_photo);
        bankSpinner = (Spinner) findViewById(R.id.carInfo_brand);
        bankNumber = (EditText) findViewById(R.id.bank_number);
        submit = (Button) findViewById(R.id.submit);
        authorizeAgreement = (TextView) findViewById(R.id.authorize_agreement);

        findViewById(R.id.iv_back).setOnClickListener(this);
        headerImage.setOnClickListener(this);
        skillItem1.setOnClickListener(this);
        skillItem2.setOnClickListener(this);
        skillItem3.setOnClickListener(this);
        skillItem4.setOnClickListener(this);
        identifyPhoto.setOnClickListener(this);
        submit.setOnClickListener(this);
        authorizeAgreement.setOnClickListener(this);
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
                onClickSkillItem(1);
                break;
            case R.id.skill_item_2:
                onClickSkillItem(2);
                break;
            case R.id.skill_item_3:
                onClickSkillItem(3);
                break;
            case R.id.skill_item_4:
                onClickSkillItem(4);
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
            imageUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "image.jpeg"));
            imageCorpUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "imageCorpUri.jpeg"));
        }

        capture(GatherImage.CAPTURE_REQUEST, imageUri);
    }

    private void onClickSkillItem(int item) {
        int paddingPixel = getResources().getDimensionPixelSize(R.dimen.dp5);

        switch (item){
            case 1:
                skillArray[0] = !skillArray[0];
                if (skillArray[0]) {
                    skillItem1.setBackgroundResource(R.drawable.skill_on);
                    skillItem1.setTextColor(Color.WHITE);
                }else {
                    skillItem1.setBackgroundResource(R.drawable.skill_off);
                    skillItem1.setTextColor(getResources().getColor(R.color.darkgray));
                }
                skillItem1.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                break;
            case 2:
                skillArray[1] = !skillArray[1];
                if (skillArray[1]) {
                    skillItem2.setBackgroundResource(R.drawable.skill_on);
                    skillItem2.setTextColor(Color.WHITE);
                } else {
                    skillItem2.setBackgroundResource(R.drawable.skill_off);
                    skillItem2.setTextColor(getResources().getColor(R.color.darkgray));
                }
                skillItem2.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                break;
            case 3:
                skillArray[2] = !skillArray[2];
                if (skillArray[2]) {
                    skillItem3.setBackgroundResource(R.drawable.skill_on);
                    skillItem3.setTextColor(Color.WHITE);
                }else {
                    skillItem3.setBackgroundResource(R.drawable.skill_off);
                    skillItem3.setTextColor(getResources().getColor(R.color.darkgray));
                }
                skillItem3.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                break;
            case 4:
                skillArray[3] = !skillArray[3];
                if (skillArray[3]) {
                    skillItem4.setBackgroundResource(R.drawable.skill_on);
                    skillItem4.setTextColor(Color.WHITE);
                }else {
                    skillItem4.setBackgroundResource(R.drawable.skill_off);
                    skillItem4.setTextColor(getResources().getColor(R.color.darkgray));
                }
                skillItem4.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                break;
        }
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

        if (TextUtils.isEmpty(bankNumStr)){
            T.show(this, R.string.bank_number_error);
            return;
        }

        if (!(skillArray[0] || skillArray[1] || skillArray[2] || skillArray[3])){
            T.show(this, R.string.skill_item_empty);
            return;
        }

        String skillArray = "{";
        for (int i = 0; i < 4; i++){
            if (this.skillArray[i]){
                skillArray += "\"" + (i + 1) + "\",";
            }
        }
        skillArray = skillArray.substring(0, skillArray.length() - 1) + "}";

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", nameStr));
        params.add(new BasicNameValuePair("idNo", idNumStr));
        params.add(new BasicNameValuePair("skillArray", skillArray));
//        params.add(new BasicNameValuePair("idPhoto", "photo/url"));//暂不要该字段
        params.add(new BasicNameValuePair("bank", "16"));
        params.add(new BasicNameValuePair("bankAddress", "china"));
        params.add(new BasicNameValuePair("bankCardNo", bankNumStr));

        Http.getInstance().postTaskToken(NetURL.COMMIT_CERTIFICATE, CommitCertificateEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){

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
            uploadImage();
            return;
        }
        if (requestCode == GatherImage.CAPTURE_ID_REQUEST){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(idPhotoUri));
                identifyPhoto.setImageBitmap(BitmapHelper.resizeImage(bitmap, 600, 360));
            } catch (FileNotFoundException e) {
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
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCorpUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", "jpeg");
        startActivityForResult(intent, GatherImage.CROP_REQUEST);
    }

    private void uploadImage(){
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
                    }
                }

            }
        }.execute(imageCorpUri.getPath(), NetURL.AVATAR);
    }
}
