package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyInfoEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.ModifyBankCardNoDialog;

/**
 * Created by zhangming on 2016/3/2.
 * 余额信息
 */
public class RestInfoActivity extends BaseActivity{
    private Context context;
    private LinearLayout ll_modify_bank;
    private TextView tv_rest_money,tv_bank_category,tv_bank_number;
    private ImageView iv_back;
    private ModifyBankCardNoDialog dialog;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_money_activity);
        initView();
        initData();
    }

    private void initView(){
        context = this;
        ll_modify_bank = (LinearLayout) findViewById(R.id.ll_modify_bank);
        tv_rest_money = (TextView) findViewById(R.id.tv_rest_money);
        tv_bank_category = (TextView) findViewById(R.id.tv_bank_category);
        tv_bank_number = (TextView) findViewById(R.id.tv_bank_number);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        dialog = new ModifyBankCardNoDialog(context);


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
                Intent intent = new Intent(context,ModifyBankCardInfoActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            Log.i("test","修改了信息,重新从服务器上获取一次数据...");
            //注:只有用户修改了信息后才从服务器上拉取数据，否则直接使用我的信息中传递过来的数据显示，更新数据,减少流量消耗
            getDataFromServer();
        }
    }

    private void initData(){
        bundle = getIntent().getExtras();
        String rest_money = bundle.getString("rest_money",String.valueOf(0));
        String bank = bundle.getString("bank","");
        String bankCardNumber = bundle.getString("bankCardNumber","");
        if(bankCardNumber.length()>=8){
            bankCardNumber = replace(bankCardNumber,"*",3,bankCardNumber.length()-4);
        }
        tv_rest_money.setText(rest_money);
        tv_bank_category.setText(bank);
        tv_bank_number.setText(bankCardNumber);
    }


    private void getDataFromServer(){
        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().getTaskToken(NetURL.MY_INFO_URL, MyInfoEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.get_info_failed));
                        return;
                    }
                    MyInfoEntity myInfoEntity = (MyInfoEntity) entity;
                    MyInfo_Data data = myInfoEntity.getData();
                    String bank = data.getBank(); //银行字典
                    String bankCardNumber = data.getBankCardNo(); //银行卡号
                    tv_bank_category.setText(bank);
                    tv_bank_number.setText(bankCardNumber);
                }
            });
        }else{
            T.show(this,getString(R.string.no_network_tips));
        }
    }


    /**
     * @param str 原字符
     * @param reStr 替换后的字符
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    private String replace(String str,String reStr,int start,int end){
        StringBuffer sb = new StringBuffer();
        sb.append(str);

        StringBuilder builder = new StringBuilder();
        for(int i=0;i<end-start;i++){
            builder.append(reStr);
        }
        sb = sb.replace(start,end,builder.toString());
        return sb.toString();
    }
}
