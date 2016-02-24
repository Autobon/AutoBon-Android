package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import cn.com.incardata.fragment.IndentMapFragment;

/**
 * 未认证主页
 * @author wanghao
 */
public class MainUnauthorizedActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unauthorized);

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, IndentMapFragment.newInstance("2月25日 14:35", "下面比较脏车门下面脏下面比较脏下脏下面比较脏下面比较脏下面比较脏"));
        transaction.commit();
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
