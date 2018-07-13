package cn.com.incardata.autobon;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tencent.smtt.sdk.TbsReaderView;

import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * 学习资料查看界面
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class StudyDataCheckActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    private FrameLayout frameLayout;
    private String filePath;
    private TbsReaderView tbsReaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_data_check);
//        GrantExternalRW.isGrantExternalRW(this);//6.0以后动态权限的判断
//        fullScreen(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        if (getIntent() != null){
            filePath = getIntent().getStringExtra("filePath");
            if (TextUtils.isEmpty(filePath)){
                T.show(getContext(),"数据加载错误，请返回重新加载");
                return;
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        tbsReaderView = new TbsReaderView(this, this);
        frameLayout.addView(tbsReaderView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        displayFile(filePath);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //   | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
                //    window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;//设置全屏
                attributes.flags |= flagTranslucentNavigation;//设置是否显示标题栏
                window.setAttributes(attributes);
            }
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
    }

    private void displayFile(String fileName) {
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, fileName);
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, SDCardUtils.getGatherDir());
        boolean b = tbsReaderView.preOpen(parseFormat(fileName), false);//第一个参数代表问的的格式

        if (b) {
            tbsReaderView.openFile(bundle);//打开文档
        }
    }

    /**
     * 获取文件类型
     * @param fileName
     * @return
     */
    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tbsReaderView.onStop();//重要的是释放
    }


}
