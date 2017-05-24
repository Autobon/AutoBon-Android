package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.adapter.BankNameAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ModifyBankCardEntity;
import cn.com.incardata.utils.BankUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/3.
 * 修改银行卡信息
 */
public class ModifyBankCardInfoActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private ImageView iv_back;
    private TextView tv_name;
    private EditText bank_number,bank_address;
    private Spinner sp_bank_catrgory;
    private String[] bankArray;
    private Button submit_bank_card_info_btn;

    private String bankName;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_bank_card_info_activity);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        context = this;
        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_name = (TextView) findViewById(R.id.tv_name);
        sp_bank_catrgory = (Spinner) findViewById(R.id.sp_bank_catrgory);
        bank_number = (EditText) findViewById(R.id.bank_number);
        bank_address = (EditText) findViewById(R.id.bank_address);
        submit_bank_card_info_btn = (Button) findViewById(R.id.submit_bank_card_info_btn);
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String bankCardNumber = bundle.getString("bankCardNumber");
        String bankAddress = bundle.getString("bankAddress");
        tv_name.setText(name);
        bank_number.setText(bankCardNumber);
        bank_address.setText(bankAddress);

        bankArray = getResources().getStringArray(R.array.bank_array);
        BankNameAdapter bankNameAdapter = new BankNameAdapter(this, bankArray);
        sp_bank_catrgory.setAdapter(bankNameAdapter);
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
        submit_bank_card_info_btn.setOnClickListener(this);

        sp_bank_catrgory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(isFirst){
                    initBankName();
                    isFirst = false;
                }else{
                    bankName = bankArray[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initBankName(){
        Bundle bundle = getIntent().getExtras();
        String bank = bundle.getString("bank");

        for(int i=0;i<bankArray.length;i++){
            if(bank.equals(bankArray[i])){
                sp_bank_catrgory.setSelection(i);
                break;
            }
        }
    }

    private void modifyBankCardInfo(){
        if(NetWorkHelper.isNetworkAvailable(context)){
            String bankNo = bank_number.getText().toString().trim();
            String bankAddress = bank_address.getText().toString().trim();

            if (TextUtils.isEmpty(bankNo)) {
                T.show(this, R.string.bank_number_error);
                return;
            }

            if (!(bankNo.trim().length() >= 16 && BankUtil.checkBankCard(bankNo))) {
                T.show(this, R.string.check_bank_number);
                return;
            }

            if (TextUtils.isEmpty(bankAddress)) {
                T.show(this, R.string.bank_andress_not_null);
                return;
            }
            //TODO 银行卡号只能根据数字位数来判断,诸如16位和19位
            BasicNameValuePair bv_one = new BasicNameValuePair("bank",bankName);
            BasicNameValuePair bv_two = new BasicNameValuePair("bankCardNo",bankNo);
            BasicNameValuePair bv_three = new BasicNameValuePair("bankAddress",bankAddress);
            String param = "?bank=" + bankName + "&bankCardNo=" + bankNo + "&bankAddress=" + bankAddress;

            Http.getInstance().putTaskToken(NetURL.MODIFY_BANK_CARD_INFO_URLV2 + param,"", ModifyBankCardEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if(entity == null){
                        T.show(context,context.getString(R.string.modify_bankcard_info_tips));
                        return;
                    }
                    ModifyBankCardEntity modifyBankCardEntity = (ModifyBankCardEntity)entity;
                    if(modifyBankCardEntity.isStatus()){
                        T.show(context,context.getString(R.string.modify_bankcard_info_success));
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        T.show(context,modifyBankCardEntity.getMessage());
                        return;
                    }
                }
            });
        }else{
            T.show(context,context.getString(R.string.no_network_tips));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                this.setResult(RESULT_OK);
                finish();
                break;
            case R.id.submit_bank_card_info_btn:
                modifyBankCardInfo();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            //点击空白位置 隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
