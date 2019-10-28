package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.com.incardata.fragment.MyOrderFragment;
import cn.com.incardata.http.response.SerializableMap;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener, MyOrderFragment.OnMyOrderFragmentListener {

    private TextView mainResponsible;
    private TextView secondResponsible;
    private View mainBaseline;
    private View secondBaseline;

    private TextView tv_query;                      //查询

    private MyOrderFragment mainFragment;//主责任人
    private MyOrderFragment secFragment;//次责任人

    private FragmentManager fragmentManager;
    public static String[] workItems;

    private boolean isMainFragment = true;

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

        tv_query = (TextView) findViewById(R.id.tv_query);


        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.tv_query).setOnClickListener(this);
        mainResponsible.setOnClickListener(this);
        secondResponsible.setOnClickListener(this);

        mainFragment = MyOrderFragment.newInstance(true);
        secFragment = MyOrderFragment.newInstance(false);
        fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.responsible_container, secFragment).add(R.id.responsible_container, mainFragment).commit();

        workItems = getResources().getStringArray(R.array.work_item);
    }

    /**
     * 切换主/次责任人状态
     *
     * @param isMain 主责任人
     */
    private void showSelect(boolean isMain) {
        if (isMain) {
            mainResponsible.setTextColor(getResources().getColor(R.color.main_orange));
            mainBaseline.setVisibility(View.VISIBLE);
            secondResponsible.setTextColor(getResources().getColor(R.color.darkgray));
            secondBaseline.setVisibility(View.INVISIBLE);

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.hide(secFragment).show(mainFragment).commit();
        } else {
            secondResponsible.setTextColor(getResources().getColor(R.color.main_orange));
            secondBaseline.setVisibility(View.VISIBLE);
            mainResponsible.setTextColor(getResources().getColor(R.color.darkgray));
            mainBaseline.setVisibility(View.INVISIBLE);

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.hide(mainFragment).show(secFragment).commit();
        }

        isMainFragment = isMain;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.main_responsible:
                showSelect(true);
                break;
            case R.id.second_responsible:
                showSelect(false);
                break;
            case R.id.tv_query:
                Intent intent = new Intent(getContext(), OrderQueryActivity.class);
                startActivityForResult(intent, 0x00);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 0x00:
                    Map<String, String> params = new HashMap<>();
                    params.clear();
                    Bundle bundle = data.getExtras();
                    SerializableMap map = (SerializableMap) bundle.get("map");
                    params.putAll(map.getMap());

                    if (isMainFragment) {
                        if (mainFragment != null){
                            ((MyOrderFragment)mainFragment).queryMainData(params);
                        }
                    } else {
                        if (secFragment != null){
                            ((MyOrderFragment)secFragment).queryCiData(params);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
