package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhangming on 2016/3/2.
 * 余额信息
 */
public class RestInfoActivity extends BaseActivity{
    private LinearLayout ll_modify_bank;
    private TextView tv_rest_money,tv_bank_category,tv_bank_number;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_money_activity);
        initView();
        initData();
    }

    private void initView(){
        ll_modify_bank = (LinearLayout) findViewById(R.id.ll_modify_bank);
        tv_rest_money = (TextView) findViewById(R.id.tv_rest_money);
        tv_bank_category = (TextView) findViewById(R.id.tv_bank_category);
        tv_bank_number = (TextView) findViewById(R.id.tv_bank_number);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        /**
        ll_modify_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 弹出对话框
            }
        });**/

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
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
