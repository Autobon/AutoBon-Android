package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.service.AutobonService;

/**
 * 未认证主页
 * @author wanghao
 */
public class MainUnauthorizedActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;

    private TextView mAuthorization;
    private boolean isVerifying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unauthorized);

        fragmentManager = getFragmentManager();
        isVerifying = getIntent().getExtras().getBoolean("isVerifying", false);
        init();
        startService(new Intent(this, AutobonService.class));
    }

    private void init() {
        mAuthorization = (TextView) findViewById(R.id.start_authorization);
        if (isVerifying){
            mAuthorization.setText(R.string.authorization_progress);
        }
        mAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInvoke();
            }
        });

        transaction = fragmentManager.beginTransaction();
        mFragment = IndentMapFragment.newInstance("2月25日 14:35", "下面比较脏车门下面脏下面比较脏下脏下面比较脏下面比较脏下面比较脏");
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
    }

    private void onClickInvoke(){
        if (isVerifying){
            startActivity(AuthorizationProgressActivity.class);
        }else {
            startActivity(AuthorizeActivity.class);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        {
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
