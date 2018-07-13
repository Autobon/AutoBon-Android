package cn.com.incardata.autobon;

import android.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
import cn.com.incardata.http.response.ConsumeItem;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.MyMessage;
import cn.com.incardata.http.response.UploadAuthorizeMessage;
import cn.com.incardata.permission.PermissionUtil;
import cn.com.incardata.utils.BankUtil;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.LeverPopupWindow;
import cn.com.incardata.view.wheel.widget.WheelPopupWindow;

/**
 * 认证
 *
 * @author wanghao
 */
public class AuthorizeActivity extends BaseActivity implements View.OnClickListener, ImageChooseFragment.OnFragmentInteractionListener {

    private static final int GE = 1;
    private static final int YIN = 2;
    private static final int CHE = 3;
    private static final int MEI = 4;
    private ImageView headerImage;
    private EditText name;
    private EditText identifyNumber;
    private LinearLayout[] linearLayouts = new LinearLayout[5];
    //    private RatingBar[] ratingBars = new RatingBar[4];
    private TextView[] tv_workYears = new TextView[4];
    private ImageView identifyPhoto,img_hint;
    private Spinner bankSpinner;
    private EditText bankNumber;
    private Button submit;
    private TextView authorizeAgreement;
    private EditText et_presentation, bankAddress;
    private EditText referrer;

    private boolean[] skillArray = new boolean[4];
    private String nameStr; //姓名
    private String idNumStr; //身份证号
    private String bankNumStr; //银行卡号
    private String bankNameStr; //银行卡所属银行
    private String referenceStr;//推荐人号码
    private String resumeStr;//个人简介
    private String bankAddressStr;//开户行地址
    private boolean isUploadIDImage = false;//身份证照片是否已上传
    private String IDImageUrl;

    private Uri imageUri; //头像拍照
    private Uri imageCorpUri; //头像裁剪
    private Uri idPhotoUri; //身份证照片
    private int rat1 = 0;

    private boolean isAgain = false;//再次认证／修改认证信息
    private ImageChooseFragment mFragment;
    private LinearLayout ll_lever;
    /**
     * 是否需要裁剪
     */
    private boolean isCrop = false;
    private List<String> workYears;
    private int type;
    private int ge = 0;
    private int yin = 0;
    private int che = 0;
    private int mei = 0;
    private ImageView[] geimgs = new ImageView[5];
    private ImageView[] yinimgs = new ImageView[5];
    private ImageView[] cheimgs = new ImageView[5];
    private ImageView[] meimgs = new ImageView[5];
    private MyMessage myMessage;


    private boolean isInfo = false;

    private LeverPopupWindow leverPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_rank_default1);
        Log.d("bitmap", bitmap.getHeight() + "/" + bitmap.getWidth());

        checkPermission();
        initView();
        initSpinner();
        checkStatus();
    }

    /**
     * 申请存储权限
     *
     * @param commandCode 可选指令码，用以执行指定操作
     */
    private void checkPermission(final int... commandCode) {
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(getString(R.string.please_grant_file_location_phone_correation_authority), new PermissionUtil.PermissionListener() {
                    @Override
                    public void doAfterGrant(String... permission) {
                        Log.d("AuthorizeActivity",getString(R.string.authorization_success));
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        Log.d("AuthorizeActivity",getString(R.string.authorization_fail));
                    }
                }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 检查是否是再次认证
     */
    private void checkStatus() {
        if (isAgain) {
            submit.setText(R.string.again_authorization);

//            nameStr = getIntent().getStringExtra("name");
//            idNumStr = getIntent().getStringExtra("idNumber");
//            String headUrl = getIntent().getStringExtra("headUrl");
//            String skill = getIntent().getStringExtra("skillArray");
//            IDImageUrl = getIntent().getStringExtra("idUrl");
//            bankNumStr = getIntent().getStringExtra("bankNo");
//            bankNameStr = getIntent().getStringExtra("bankName");
//
//            name.setText(nameStr);
//            identifyNumber.setText(idNumStr);
//            bankNumber.setText(bankNumStr);
//            if (!TextUtils.isEmpty(IDImageUrl)) {
//                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + IDImageUrl, identifyPhoto, false);
//                isUploadIDImage = true;
//            }
//            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + headUrl, headerImage, false);
//
//            String[] bankArray = getResources().getStringArray(R.array.bank_array);
//            for (int i = 0; i < bankArray.length; i++) {
//                if (bankNameStr.equals(bankArray[i])) {
//                    bankSpinner.setSelection(i);
//                    break;
//                }
//            }
//
//            if (TextUtils.isEmpty(skill)) return;
//            String[] skillAr = skill.split(",");
//            for (String element : skillAr) {
//                try {
//                    int ski = Integer.parseInt(element);
//                    onClickSkillItem(ski - 1);
//                } catch (NumberFormatException e) {
//                    continue;
//                }
//            }
        }
// else if (isAgain == 2){
//            submit.setText(R.string.again_authorization);
//            myMessage = getIntent().getParcelableExtra("myMessage");
//
//
//
//            nameStr = myMessage.getName();
//            idNumStr = myMessage.getIdNo();
//            String headUrl = myMessage.getAvatar();
//            IDImageUrl = myMessage.getIdPhoto();
//            bankNumStr = myMessage.getBankCardNo();
//            bankNameStr = myMessage.getBank();
//            referenceStr = myMessage.getReference();
//            resumeStr = myMessage.getResume();
//            bankAddressStr = myMessage.getBankAddress();
//
//            name.setText(nameStr);
//            identifyNumber.setText(idNumStr);
//            if(!TextUtils.isEmpty(referenceStr)){
//                referrer.setText(referenceStr);
//            }else {
//                referrer.setText("");
//            }
//            ge = myMessage.getFilmLevel();
//            yin = myMessage.getCarCoverLevel();
//            che = myMessage.getColorModifyLevel();
//            mei = myMessage.getBeautyLevel();
//
//            for (int i = 0; i < 5; i++){
//                if (i < myMessage.getFilmLevel()){
//                    geimgs[i].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank1));
//                }else {
//                    geimgs[1].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
//                }
//
//
//            }
//
//            bankNumber.setText(bankNumStr);
//            if (!TextUtils.isEmpty(IDImageUrl)) {
//                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + IDImageUrl, identifyPhoto, false);
//                isUploadIDImage = true;
//            }
//            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + headUrl, headerImage, false);
//
//            String[] bankArray = getResources().getStringArray(R.array.bank_array);
//            for (int i = 0; i < bankArray.length; i++) {
//                if (bankNameStr.equals(bankArray[i])) {
//                    bankSpinner.setSelection(i);
//                    break;
//                }
//            }
//
////            if (TextUtils.isEmpty(skill)) return;
////            String[] skillAr = skill.split(",");
////            for (String element : skillAr) {
////                try {
////                    int ski = Integer.parseInt(element);
////                    onClickSkillItem(ski - 1);
////                } catch (NumberFormatException e) {
////                    continue;
////                }
////            }
//        }
    }

    private void initView() {
        headerImage = (ImageView) findViewById(R.id.header_image);
        name = (EditText) findViewById(R.id.name);
        identifyNumber = (EditText) findViewById(R.id.IDNo);
        tv_workYears[0] = (TextView) findViewById(R.id.ge_workYear);
        tv_workYears[1] = (TextView) findViewById(R.id.yin_workYear);
        tv_workYears[2] = (TextView) findViewById(R.id.che_workYear);
        tv_workYears[3] = (TextView) findViewById(R.id.mei_workYear);
        linearLayouts[0] = (LinearLayout) findViewById(R.id.ll_ge);
        linearLayouts[1] = (LinearLayout) findViewById(R.id.ll_yin);
        linearLayouts[2] = (LinearLayout) findViewById(R.id.ll_che);
        linearLayouts[3] = (LinearLayout) findViewById(R.id.ll_mei);
        linearLayouts[4] = (LinearLayout) findViewById(R.id.ll_lever);
        geimgs[0] = (ImageView) findViewById(R.id.ge_img1);
        geimgs[1] = (ImageView) findViewById(R.id.ge_img2);
        geimgs[2] = (ImageView) findViewById(R.id.ge_img3);
        geimgs[3] = (ImageView) findViewById(R.id.ge_img4);
        geimgs[4] = (ImageView) findViewById(R.id.ge_img5);
        yinimgs[0] = (ImageView) findViewById(R.id.yin_img1);
        yinimgs[1] = (ImageView) findViewById(R.id.yin_img2);
        yinimgs[2] = (ImageView) findViewById(R.id.yin_img3);
        yinimgs[3] = (ImageView) findViewById(R.id.yin_img4);
        yinimgs[4] = (ImageView) findViewById(R.id.yin_img5);
        cheimgs[0] = (ImageView) findViewById(R.id.che_img1);
        cheimgs[1] = (ImageView) findViewById(R.id.che_img2);
        cheimgs[2] = (ImageView) findViewById(R.id.che_img3);
        cheimgs[3] = (ImageView) findViewById(R.id.che_img4);
        cheimgs[4] = (ImageView) findViewById(R.id.che_img5);
        meimgs[0] = (ImageView) findViewById(R.id.mei_img1);
        meimgs[1] = (ImageView) findViewById(R.id.mei_img2);
        meimgs[2] = (ImageView) findViewById(R.id.mei_img3);
        meimgs[3] = (ImageView) findViewById(R.id.mei_img4);
        meimgs[4] = (ImageView) findViewById(R.id.mei_img5);
        ll_lever = (LinearLayout) findViewById(R.id.ll_levers);

        identifyPhoto = (ImageView) findViewById(R.id.identify_photo);
        img_hint = (ImageView) findViewById(R.id.img_hint);
        bankSpinner = (Spinner) findViewById(R.id.carInfo_brand);
        bankNumber = (EditText) findViewById(R.id.bank_number);
        submit = (Button) findViewById(R.id.submit);
        authorizeAgreement = (TextView) findViewById(R.id.authorize_agreement);
        et_presentation = (EditText) findViewById(R.id.et_presentation);
        bankAddress = (EditText) findViewById(R.id.bankAddress);
        referrer = (EditText) findViewById(R.id.referrer);

        findViewById(R.id.iv_back).setOnClickListener(this);
        headerImage.setOnClickListener(this);
        linearLayouts[0].setOnClickListener(this);
        linearLayouts[1].setOnClickListener(this);
        linearLayouts[2].setOnClickListener(this);
        linearLayouts[3].setOnClickListener(this);
        linearLayouts[4].setOnClickListener(this);
        geimgs[0].setOnClickListener(this);
        geimgs[1].setOnClickListener(this);
        geimgs[2].setOnClickListener(this);
        geimgs[3].setOnClickListener(this);
        geimgs[4].setOnClickListener(this);
        yinimgs[0].setOnClickListener(this);
        yinimgs[1].setOnClickListener(this);
        yinimgs[2].setOnClickListener(this);
        yinimgs[3].setOnClickListener(this);
        yinimgs[4].setOnClickListener(this);
        cheimgs[0].setOnClickListener(this);
        cheimgs[1].setOnClickListener(this);
        cheimgs[2].setOnClickListener(this);
        cheimgs[3].setOnClickListener(this);
        cheimgs[4].setOnClickListener(this);
        meimgs[0].setOnClickListener(this);
        meimgs[1].setOnClickListener(this);
        meimgs[2].setOnClickListener(this);
        meimgs[3].setOnClickListener(this);
        meimgs[4].setOnClickListener(this);
        linearLayouts[0].setTag(0);
        linearLayouts[1].setTag(0);
        linearLayouts[2].setTag(0);
        linearLayouts[3].setTag(0);
        identifyPhoto.setOnClickListener(this);
        submit.setOnClickListener(this);
        authorizeAgreement.setOnClickListener(this);
        ll_lever.setOnClickListener(this);

        isAgain = getIntent().getBooleanExtra("isAgain", false);
        isInfo = getIntent().getBooleanExtra("isInfo", false);
        workYears = new ArrayList<String>();
        for (int i = 0; i <= 10; i++) {
            String workYear = i + getString(R.string.year);
            workYears.add(workYear);
        }

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
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.header_image:
                isCrop = true;
                onClickHeaderImage();
                break;
            case R.id.ll_ge:
                type = GE;
                hideShow();
                showPopupWindow(workYears, (Integer) linearLayouts[0].getTag());
                break;
            case R.id.ll_yin:
                type = YIN;
                hideShow();
                showPopupWindow(workYears, (Integer) linearLayouts[1].getTag());
                break;
            case R.id.ll_che:
                type = CHE;
                hideShow();
                showPopupWindow(workYears, (Integer) linearLayouts[2].getTag());
                break;
            case R.id.ll_mei:
                type = MEI;
                hideShow();
                showPopupWindow(workYears, (Integer) linearLayouts[3].getTag());
                break;
            case R.id.ll_lever:
                hideShow();
                showpup();
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
            case R.id.ge_img1:
                geImgClick(0);
                break;
            case R.id.ge_img2:
                geImgClick(1);
                break;
            case R.id.ge_img3:
                geImgClick(2);
                break;
            case R.id.ge_img4:
                geImgClick(3);
                break;
            case R.id.ge_img5:
                geImgClick(4);
                break;
            case R.id.yin_img1:
                yinImgClick(0);
                break;
            case R.id.yin_img2:
                yinImgClick(1);
                break;
            case R.id.yin_img3:
                yinImgClick(2);
                break;
            case R.id.yin_img4:
                yinImgClick(3);
                break;
            case R.id.yin_img5:
                yinImgClick(4);
                break;
            case R.id.che_img1:
                cheImgClick(0);
                break;
            case R.id.che_img2:
                cheImgClick(1);
                break;
            case R.id.che_img3:
                cheImgClick(2);
                break;
            case R.id.che_img4:
                cheImgClick(3);
                break;
            case R.id.che_img5:
                cheImgClick(4);
                break;
            case R.id.mei_img1:
                meiImgClick(0);
                break;
            case R.id.mei_img2:
                meiImgClick(1);
                break;
            case R.id.mei_img3:
                meiImgClick(2);
                break;
            case R.id.mei_img4:
                meiImgClick(3);
                break;
            case R.id.mei_img5:
                meiImgClick(4);
                break;
            case R.id.ll_levers:
                hideShow();
                showpup();
//                Dialog dialog = new Dialog()
                break;
        }
    }

    private void onClickHeaderImage() {
        if (mFragment == null) {
            mFragment = new ImageChooseFragment();
        }

        if (!SDCardUtils.isExistSDCard()) {
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (imageUri == null) {
            File file1 = new File(SDCardUtils.getGatherDir() + File.separator + "image.jpeg");
            File file2 = new File(SDCardUtils.getGatherDir() + File.separator + "imageCorpUri.jpeg");

            if (file1.exists()) {
                file1.delete();
            }
            if (file2.exists()) {
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

        skillArray[item] = !skillArray[item];

    }

    private void onClickIdentifyPhoto() {
        if (mFragment == null) {
            mFragment = new ImageChooseFragment();
        }

        if (!SDCardUtils.isExistSDCard()) {
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (idPhotoUri == null) {
            idPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "idPhoto.jpeg"));
        }
        mFragment.show(getFragmentManager(), "Choose");
    }


    public void hideShow() {
        InputMethodManager manager = (InputMethodManager) getSystemService(getContext().INPUT_METHOD_SERVICE);
        boolean isOpen = manager.isActive();
        if (isOpen) {  //软键盘处于开状态
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
        }
    }

    private void onClickSubmit() {
        obtainEdit();
        if (TextUtils.isEmpty(nameStr)) {
            T.show(this, R.string.name_error);
            return;
        }

        if (TextUtils.isEmpty(idNumStr)) {
            T.show(this, R.string.identify_number_error);
            return;
        }

        if (!idNumStr.matches("^(\\d{14}|\\d{17})(\\d|[xX])$")) {
            T.show(this, getString(R.string.please_check_identify));
            return;
        }
        if (ge == 0 && (int) linearLayouts[0].getTag() == 0 && yin == 0 && (int) linearLayouts[1].getTag() == 0 &&
                che == 0 && (int) linearLayouts[2].getTag() == 0 && mei == 0 && (int) linearLayouts[3].getTag() == 0) {
            T.show(getContext(), R.string.skill_item_empty);
            return;
        }
        if (ge > 0 && (int) linearLayouts[0].getTag() == 0) {
            T.show(getContext(), getString(R.string.ge_work_year));
            return;
        }
        if (ge == 0 && (int) linearLayouts[0].getTag() > 0) {
            T.show(getContext(), getString(R.string.ge_level));
            return;
        }
        if (yin > 0 && (int) linearLayouts[1].getTag() == 0) {
            T.show(getContext(), getString(R.string.yin_work_year));
            return;
        }
        if (yin == 0 && (int) linearLayouts[1].getTag() > 0) {
            T.show(getContext(), getString(R.string.yin_levlel));
            return;
        }
        if (che > 0 && (int) linearLayouts[2].getTag() == 0) {
            T.show(getContext(), getString(R.string.che_work_year));
            return;
        }
        if (che == 0 && (int) linearLayouts[2].getTag() > 0) {
            T.show(getContext(), getString(R.string.che_level));
            return;
        }
        if (mei > 0 && (int) linearLayouts[3].getTag() == 0) {
            T.show(getContext(), getString(R.string.mei_work_year));
            return;
        }
        if (mei == 0 && (int) linearLayouts[3].getTag() > 0) {
            T.show(getContext(), getString(R.string.mei_level));
            return;
        }


        if (!isUploadIDImage) {
            T.show(this, getString(R.string.requirement_id_photo));
            return;
        }

        if (TextUtils.isEmpty(bankNumStr)) {
            T.show(this, R.string.bank_number_error);
            return;
        }

        if (!(bankNumStr.trim().length() >= 16 && BankUtil.checkBankCard(bankNumStr))) {
            T.show(this, getString(R.string.check_bank_number));
            return;
        }
        if (TextUtils.isEmpty(bankAddressStr)) {
            T.show(this, getString(R.string.bank_andress_not_null));
            return;
        }

        String skillArray = "";
        if (ge > 0) {
            skillArray += "1,";
        }
        if (yin > 0) {
            skillArray += "2,";
        }
        if (che > 0) {
            skillArray += "3,";
        }
        if (mei > 0) {
            skillArray += "4,";
        }

        skillArray = skillArray.substring(0, skillArray.length() - 1);
        UploadAuthorizeMessage uploadAuthorizeMessage = new UploadAuthorizeMessage();
        uploadAuthorizeMessage.setName(nameStr);
        uploadAuthorizeMessage.setIdNo(idNumStr);
        uploadAuthorizeMessage.setSkill(skillArray);
        uploadAuthorizeMessage.setIdPhoto(IDImageUrl);
        uploadAuthorizeMessage.setBank(bankNameStr);
        uploadAuthorizeMessage.setBankCardNo(bankNumStr);
        uploadAuthorizeMessage.setBankAddress(bankAddressStr);
        if (!TextUtils.isEmpty(referenceStr)) {
            uploadAuthorizeMessage.setReference(referenceStr);
        }
        uploadAuthorizeMessage.setFilmLevel(ge);
        uploadAuthorizeMessage.setFilmWorkingSeniority((int) linearLayouts[0].getTag());
        uploadAuthorizeMessage.setCarCoverLevel(yin);
        uploadAuthorizeMessage.setCarCoverWorkingSeniority((int) linearLayouts[1].getTag());
        uploadAuthorizeMessage.setColorModifyLevel(che);
        uploadAuthorizeMessage.setColorModifyWorkingSeniority((int) linearLayouts[2].getTag());
        uploadAuthorizeMessage.setBeautyLevel(mei);
        uploadAuthorizeMessage.setBeautyWorkingSeniority((int) linearLayouts[3].getTag());
        if (!TextUtils.isEmpty(resumeStr)) {
            uploadAuthorizeMessage.setResume(resumeStr);
        }

        Object json = JSON.toJSON(uploadAuthorizeMessage);


//
//        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("name", nameStr));
//        params.add(new BasicNameValuePair("idNo", idNumStr));
//        params.add(new BasicNameValuePair("skills", skillArray));
//        params.add(new BasicNameValuePair("idPhoto", IDImageUrl));
//        params.add(new BasicNameValuePair("bank", bankNameStr));
//        params.add(new BasicNameValuePair("bankCardNo", bankNumStr));
//        params.add(new BasicNameValuePair("bankAddress", bankAddressStr));
//        if (!TextUtils.isEmpty(referenceStr)) {
//            params.add(new BasicNameValuePair("reference", referenceStr));
//        }
//        params.add(new BasicNameValuePair("filmLevel",String.valueOf(ge)));
//        params.add(new BasicNameValuePair("filmWorkingSeniority", String.valueOf(linearLayouts[0].getTag())));
//        params.add(new BasicNameValuePair("carCoverLevel",String.valueOf(yin)));
//        params.add(new BasicNameValuePair("carCoverWorkingSeniority", String.valueOf(linearLayouts[1].getTag())));
//        params.add(new BasicNameValuePair("colorModifyLevel",String.valueOf(che)));
//        params.add(new BasicNameValuePair("colorModifyWorkingSeniority", String.valueOf(linearLayouts[2].getTag())));
//        params.add(new BasicNameValuePair("beautyLevel",String.valueOf(mei)));
//        params.add(new BasicNameValuePair("beautyWorkingSeniority", String.valueOf(linearLayouts[3].getTag())));
//        if (!TextUtils.isEmpty(resumeStr)) {
//            params.add(new BasicNameValuePair("resume", resumeStr));
//        }

        Http.getInstance().postTaskToken(NetURL.COMMIT_CERTIFICATEV2, json.toString(), CommitCertificateEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.network_exception);
                    return;
                }
                if (entity instanceof CommitCertificateEntity) {
                    CommitCertificateEntity certificateEntity = (CommitCertificateEntity) entity;
                    if (certificateEntity.isStatus()) {
                        if (isInfo) {
                            Intent intent = new Intent(AuthorizeActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            setResult(RESULT_OK);//携带结果返回
                            finish();
                        }
                    } else {
                        T.show(getContext(), certificateEntity.getMessage().toString());
                        return;
                    }
                }
            }
        });
    }

    public void showpup() {
        if (leverPopupWindow == null) {
            leverPopupWindow = new LeverPopupWindow(this);
            leverPopupWindow.init();
        }
        leverPopupWindow.showAtLocation(findViewById(R.id.lll), Gravity.CENTER, 0, 0);
    }

    private void obtainEdit() {
        nameStr = name.getText().toString();
        idNumStr = identifyNumber.getText().toString();
        referenceStr = referrer.getText().toString().trim();
        bankNameStr = bankSpinner.getSelectedItem().toString();
        bankNumStr = bankNumber.getText().toString();
        resumeStr = et_presentation.getText().toString();
        bankAddressStr = bankAddress.getText().toString();
    }

    private void onClickAuthorizeAgreement() {
        startActivity(AuthorizeAgreementActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
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
                if (isCrop) {
                    if (data != null) {
                        imageUri = data.getData();
                        crop();
                    } else {
                        T.show(getContext(), R.string.operate_failed_agen);
                        break;
                    }
                } else {
                    idPhotoProcess(data.getData());
                }
                break;
        }
    }

    private void idPhotoProcess(Uri uri) {
        try {
            Bitmap bitmap = BitmapHelper.resizeImage(getContext(), uri, 0.5f);
            if (BitmapHelper.saveBitmap(this.idPhotoUri, bitmap)) {
                uploadIdPhoto();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void capture(int requestCode, Uri imageUri) {
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

    private void uploadHeadImage() {
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
                } else {
                    AvatarEntity avatarEntity = JSON.parseObject(s, AvatarEntity.class);
                    if (avatarEntity.isStatus()) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageCorpUri));
                            headerImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.show(getContext(), getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(imageCorpUri.getPath(), NetURL.AVATARV2);
    }

    private void uploadIdPhoto() {
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
                } else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isStatus()) {
                        isUploadIDImage = true;
                        IDImageUrl = idPhotoEntity.getMessage();
                        Bitmap bitmap = BitmapFactory.decodeFile(idPhotoUri.getPath());
                        identifyPhoto.setImageBitmap(bitmap);
                        img_hint.setVisibility(View.GONE);
                    } else {
                        T.show(getContext(), getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(idPhotoUri.getPath(), NetURL.ID_PHOTOV2);
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
                if (isCrop) {
                    capture(GatherImage.CAPTURE_REQUEST, imageUri);
                } else {
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


    private WheelPopupWindow popup;

    private void showPopupWindow(List<String> list, int curIndex) {
        if (popup == null) {
            popup = new WheelPopupWindow(this);
            popup.init();
            popup.setListener(checkedListener);
        }
        popup.setData(list, curIndex);
        popup.showAtLocation(findViewById(R.id.scroll_auth), Gravity.BOTTOM, 0, 0);
    }

    private WheelPopupWindow.OnCheckedListener checkedListener = new WheelPopupWindow.OnCheckedListener() {
        @Override
        public void onChecked(int index) {
            ConsumeItem consumeItem = new ConsumeItem();
            switch (type) {
                case GE:
                    if (index == 0) {
                        tv_workYears[0].setText(R.string.work_year);
                        linearLayouts[0].setTag(index);
                    } else {
                        if (index == (Integer) linearLayouts[0].getTag()) return;
                        if (workYears != null && !workYears.isEmpty()) {
                            tv_workYears[0].setText(workYears.get(index));
                            linearLayouts[0].setTag(index);
                        }
                    }
                    break;
                case YIN:
                    if (index == 0) {
                        tv_workYears[1].setText(R.string.work_year);
                        linearLayouts[1].setTag(index);
                    } else {
                        if (index == (Integer) linearLayouts[1].getTag()) return;
                        if (workYears != null && !workYears.isEmpty()) {
                            tv_workYears[1].setText(workYears.get(index));
                            linearLayouts[1].setTag(index);
                        }
                    }
                    break;
                case CHE:
                    if (index == 0) {
                        tv_workYears[2].setText(R.string.work_year);
                        linearLayouts[2].setTag(index);
                    } else {
                        if (index == (Integer) linearLayouts[2].getTag()) return;
                        if (workYears != null && !workYears.isEmpty()) {
                            tv_workYears[2].setText(workYears.get(index));
                            linearLayouts[2].setTag(index);
                        }
                    }
                    break;
                case MEI:
                    if (index == 0) {
                        tv_workYears[3].setText(R.string.work_year);
                        linearLayouts[3].setTag(index);
                    } else {
                        if (index == (Integer) linearLayouts[3].getTag()) return;
                        if (workYears != null && !workYears.isEmpty()) {
                            tv_workYears[3].setText(workYears.get(index));
                            linearLayouts[3].setTag(index);
                        }
                    }
                    break;
            }
        }
    };

    public void geImgClick(int i) {
        if (ge == 1 && i == 0) {
            for (int j = 0; j < 5; j++) {
                geimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                ge = 0;
            }
        } else {
            ge = i + 1;
            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    geimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank1));
                } else {
                    geimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                }
            }
        }
    }

    public void yinImgClick(int i) {
        if (yin == 1 && i == 0) {
            for (int j = 0; j < 5; j++) {
                yinimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                yin = 0;
            }
        } else {
            yin = i + 1;
            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    yinimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank1));
                } else {
                    yinimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                }
            }
        }
    }

    public void cheImgClick(int i) {
        if (che == 1 && i == 0) {
            for (int j = 0; j < 5; j++) {
                cheimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                che = 0;
            }
        } else {
            che = i + 1;
            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    cheimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank1));
                } else {
                    cheimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                }
            }
        }
    }

    public void meiImgClick(int i) {
        if (mei == 1 && i == 0) {
            for (int j = 0; j < 5; j++) {
                meimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                mei = 0;
            }
        } else {
            mei = i + 1;
            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    meimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank1));
                } else {
                    meimgs[j].setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_rank_default1));
                }
            }
        }
    }
}
