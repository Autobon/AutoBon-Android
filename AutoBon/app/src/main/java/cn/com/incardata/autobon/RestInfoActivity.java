package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CommitCertificateEntity;
import cn.com.incardata.http.response.MyInfoEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.MyMessage;
import cn.com.incardata.http.response.MyMessageData;
import cn.com.incardata.http.response.MyMessageEntity;
import cn.com.incardata.http.response.RegisterEntity;
import cn.com.incardata.http.response.TakeCashEntity;
import cn.com.incardata.http.response.TakeCashJson;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.ModifyBankCardNoDialog;

/**
 * Created by zhangming on 2016/3/2.
 * 余额信息
 */
public class RestInfoActivity extends BaseActivity {
    private Context context;
    private LinearLayout ll_modify_bank;
    private TextView tv_rest_money, tv_bank_category, tv_bank_number;
    private ImageView iv_back;
    private ModifyBankCardNoDialog dialog;
    private Bundle bundle;


    private int id;             //技师ID
    private String rest_money;
    private String bank;
    private String bankCardNumber;
    private String bankCardNumber1;
    private String bankAddress;

    private Button btn_take_cash;       //提现
    private EditText ed_money;          //提现金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_money_activity);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        ll_modify_bank = (LinearLayout) findViewById(R.id.ll_modify_bank);
        tv_rest_money = (TextView) findViewById(R.id.tv_rest_money);
        tv_bank_category = (TextView) findViewById(R.id.tv_bank_category);
        tv_bank_number = (TextView) findViewById(R.id.tv_bank_number);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        dialog = new ModifyBankCardNoDialog(context);

        btn_take_cash = (Button) findViewById(R.id.btn_take_cash);
        ed_money = (EditText) findViewById(R.id.ed_money);


        ll_modify_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 弹出对话框
                dialog.show();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog.setDialogAttribute(new ModifyBankCardNoDialog.OnDialogBaseAttribute() {
            @Override
            public void setOnDialogBaseAttribute() {
                dialog.setCanceledOnTouchOutside(true); //设置点击空白处关闭对话框

                Window modifyWindow = dialog.getWindow();
                WindowManager manager = getWindowManager();
                Display d = manager.getDefaultDisplay();

                //重新设置对话框的宽高属性
                WindowManager.LayoutParams params = modifyWindow.getAttributes(); // 获取对话框当前的参数值
                params.width = (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.75
                //params.height = (int) (d.getHeight() * 0.75); // 高度设置为屏幕的0.75
                modifyWindow.setAttributes(params);
            }
        });

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, ModifyBankCardInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bank",bank);
                bundle.putString("bankCardNumber",bankCardNumber1);
                bundle.putString("bankAddress",bankAddress);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        btn_take_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = ed_money.getText().toString().trim();
                if (TextUtils.isEmpty(money)){
                    T.show(context,"请输入提现金额");
                    return;
                }
                if (Double.parseDouble(money) > Double.parseDouble(rest_money)){
                    T.show(context,"余额不足");
                    return;
                }
                takeCash(money);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.i("test", "修改了信息,重新从服务器上获取一次数据...");
            //注:只有用户修改了信息后才从服务器上拉取数据，否则直接使用我的信息中传递过来的数据显示，更新数据,减少流量消耗
            getDataFromServer();
        }
    }



    private void initData() {
        bundle = getIntent().getExtras();
        id = bundle.getInt("id",0);
        rest_money = bundle.getString("rest_money", String.valueOf(0));
        bank = bundle.getString("bank", "");
        bankCardNumber = bundle.getString("bankCardNumber", "");
        bankCardNumber1 = bundle.getString("bankCardNumber", "");
        bankAddress = bundle.getString("bankAddress","");
        if (bankCardNumber.length() >= 8) {
            bankCardNumber = replace(bankCardNumber, "*", 3, bankCardNumber.length() - 4);
        }
        tv_rest_money.setText(rest_money);
        tv_bank_category.setText(bank);
        tv_bank_number.setText(bankCardNumber);

        double balance = Double.parseDouble(rest_money);
        if (balance <= 0){
            btn_take_cash.setAlpha(0.5f);
            btn_take_cash.setEnabled(false);
            ed_money.setEnabled(false);
            ed_money.setFocusable(false);
        }
    }

    private void getDataFromServer() {
        if (NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().getTaskToken(NetURL.MY_INFO_URLV2, MyMessageEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.get_info_failed));
                        return;
                    }
                    MyMessageEntity myInfoEntity = (MyMessageEntity) entity;

                    if (myInfoEntity.isStatus()) {
                        MyMessageData myMessageData = JSON.parseObject(myInfoEntity.getMessage().toString(), MyMessageData.class);
                        MyMessage myMessage = myMessageData.getTechnician();
                        bank = myMessage.getBank();
                        bankCardNumber = myMessage.getBankCardNo();
                        bankCardNumber1 = myMessage.getBankCardNo();
                        bankAddress = myMessage.getBankAddress();
                        if (bankCardNumber.length() >= 8) {
                            bankCardNumber = replace(bankCardNumber, "*", 3, bankCardNumber.length() - 4);
                        }
                        tv_bank_category.setText(bank);
                        tv_bank_number.setText(bankCardNumber);
                    }
                }
            });
        } else {
            T.show(this, getString(R.string.no_network_tips));
        }
    }

    /**
     * 提现接口
     */
    private void takeCash(String money){
        btn_take_cash.setEnabled(false);
        TakeCashJson takeCashJson = new TakeCashJson();
        takeCashJson.setTechId(id);
        takeCashJson.setApplyMoney(Double.parseDouble(money));
//        takeCashJson.setApplyMoney(108);
        takeCashJson.setApplyDate(new Date());

        Object json = JSON.toJSON(takeCashJson);

        showDialog();
        if(NetWorkHelper.isNetworkAvailable(context)) {

            Http.getInstance().postTaskToken(NetURL.TAKECASH, json.toString(), TakeCashEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    cancelDialog();
                    btn_take_cash.setEnabled(true);
                    if (entity == null) {
                        T.show(getContext(), R.string.request_failed);
                        return;
                    }
                    if (entity instanceof TakeCashEntity) {
                        TakeCashEntity takeCashEntity = (TakeCashEntity) entity;
                        if (takeCashEntity.isStatus()) {
                            T.show(getContext(),"提交提现申请成功");
                            ed_money.setText("");
                        } else {
                            T.show(getContext(), takeCashEntity.getMessage().toString());
                            return;
                        }
                    }
                }
            });
//            Http.getInstance().postTaskToken(NetURL.TAKECASH, TakeCashEntity.class, new OnResult() {
//                @Override
//                public void onResult(Object entity) {
//                    if (entity == null) {
//                        T.show(context, "提现申请失败");
//                        return;
//                    }
//                    TakeCashEntity takeCashEntity = (TakeCashEntity) entity;
//                    if(takeCashEntity.isStatus()){  //成功
//
//                    }else{  //失败
////                        T.show(context,takeCashEntity.getMessage().toString());
////                        return;
//                    }
//                }
//            },(BasicNameValuePair[]) mList.toArray(new BasicNameValuePair[mList.size()]));
        }else{
            T.show(context,getString(R.string.no_network_tips));
            return;
        }
    }

//
//    private void getDataFromServer(){
//        if(NetWorkHelper.isNetworkAvailable(context)) {
//            Http.getInstance().getTaskToken(NetURL.MY_INFO_URL, MyInfoEntity.class, new OnResult() {
//                @Override
//                public void onResult(Object entity) {
//                    if (entity == null) {
//                        T.show(context, context.getString(R.string.get_info_failed));
//                        return;
//                    }
//                    MyInfoEntity myInfoEntity = (MyInfoEntity) entity;
//                    MyInfo_Data data = myInfoEntity.getData();
//                    String bank = data.getBank(); //银行字典
//                    String bankCardNumber = data.getBankCardNo(); //银行卡号
//                    if(bankCardNumber.length()>=8){
//                        bankCardNumber = replace(bankCardNumber,"*",3,bankCardNumber.length()-4);
//                    }
//                    tv_bank_category.setText(bank);
//                    tv_bank_number.setText(bankCardNumber);
//                }
//            });
//        }else{
//            T.show(this,getString(R.string.no_network_tips));
//        }
//    }


    /**
     * @param str   原字符
     * @param reStr 替换后的字符
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    private String replace(String str, String reStr, int start, int end) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < end - start; i++) {
            builder.append(reStr);
        }
        sb = sb.replace(start, end, builder.toString());
        return sb.toString();
    }
}
