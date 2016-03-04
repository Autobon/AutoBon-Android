package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;

/**
 * 已认证的
 * @author wanghao
 */
public class MainAuthorizedActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_authorized);
        init();
    }

    private void init() {
        findViewById(R.id.personal).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personal:
                startActivity(MyInfoActivity.class);
                break;
        }
    }
}
