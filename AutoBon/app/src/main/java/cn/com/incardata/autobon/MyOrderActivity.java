package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.incardata.fragment.MyOrderFragment;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener, MyOrderFragment.OnMyOrderFragmentListener{

    private TextView mainResponsible;
    private TextView secondResponsible;
    private View mainBaseline;
    private View secondBaseline;

    private MyOrderFragment mainFragment;//主责任人
    private MyOrderFragment secFragment;//次责任人

    private FragmentManager fragmentManager;
    public static String[] workItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();
    }

    private void initView() {
        mainResponsible = (TextView) findViewById(R.id.main_responsible);
        secondResponsible = (TextView) findViewById(R.id.second_responsible);
        mainBaseline = findViewById(R.id.main_baseline);
        secondBaseline = findViewById(R.id.second_baseline);

        findViewById(R.id.back).setOnClickListener(this);
        mainResponsible.setOnClickListener(this);
        secondResponsible.setOnClickListener(this);

        mainFragment = new MyOrderFragment();
        secFragment = new MyOrderFragment();
        fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.responsible_container, secFragment).commit();

        workItems = getResources().getStringArray(R.array.work_item);
    }

    /**
     * 切换主/次责任人状态
     * @param isMain 主责任人
     */
    private void showSelect(boolean isMain){
        if (isMain){
            mainResponsible.setTextColor(getResources().getColor(R.color.main_orange));
            mainBaseline.setVisibility(View.VISIBLE);
            secondResponsible.setTextColor(getResources().getColor(R.color.darkgray));
            secondBaseline.setVisibility(View.INVISIBLE);


        }else {
            secondResponsible.setTextColor(getResources().getColor(R.color.main_orange));
            secondBaseline.setVisibility(View.VISIBLE);
            mainResponsible.setTextColor(getResources().getColor(R.color.darkgray));
            mainBaseline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.main_responsible:
                showSelect(true);
                break;
            case R.id.second_responsible:
                showSelect(false);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
