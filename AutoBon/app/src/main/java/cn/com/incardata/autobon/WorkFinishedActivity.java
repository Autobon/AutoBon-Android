package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.com.incardata.sharesdk.custom.OnClickSharePlatfornSelect;
import cn.com.incardata.sharesdk.custom.ShareConstant;
import cn.com.incardata.sharesdk.custom.SharePlatform;
import cn.com.incardata.sharesdk.custom.SharePopwindow;
import cn.com.incardata.sharesdk.custom.WShare;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;

/**
 * 工作已结束-分享页面
 * @author wanghao
 */
public class WorkFinishedActivity extends BaseActivity implements View.OnClickListener{
    private TextView today;
    private TextView finishedNote;

    private int orderId;
    private String orderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_finished);

        initView();
    }

    private void initView() {
        orderId = getIntent().getIntExtra(AutoCon.ORDER_ID, -1);
        orderNum = getIntent().getStringExtra("OrderNum");

        today = (TextView) findViewById(R.id.today);
        finishedNote = (TextView) findViewById(R.id.finished_note);

        today.setText(DateCompute.getWeekOfDate());
        finishedNote.setText(getResources().getString(R.string.order_finished_note, orderNum));

        findViewById(R.id.personal).setOnClickListener(this);
        findViewById(R.id.continue_immediate).setOnClickListener(this);
        findViewById(R.id.more).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.continue_immediate:
                finish();
                break;
            case R.id.more:
                showSharePopWindow();
                break;
        }
    }

    private SharePopwindow shareWindow;

    /**
     * 显示分享PopWindow
     */
    private void showSharePopWindow() {
        if (shareWindow == null) {
            shareWindow = new SharePopwindow(this);
            shareWindow.init();
        }
        shareWindow.setListener(new OnClickSharePlatfornSelect() {

            @Override
            public void OnClick(View v, SharePlatform paltforn) {
                share(paltforn);
            }
        });
        shareWindow.showAtLocation(this.findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
    }
    protected void share(SharePlatform paltforn) {

        String content = "车邻邦，技师的创业平台";

        WShare wShare = new WShare(this);
        switch (paltforn) {
            case QQ:
                if(!wShare.shareQQ(ShareConstant.TITLE, ShareConstant.URL, content, ShareConstant.IMAGE_URL)){
                    T.show(getContext(), "请先安装QQ客户端");
                }
                break;
            case QZONE:
                if(!wShare.shareQZone(ShareConstant.TITLE, ShareConstant.URL, content, ShareConstant.IMAGE_URL, ShareConstant.TITLE, ShareConstant.URL)){
                    T.show(getContext(), "请先安装QQ客户端");
                }
                break;
            case WECHAT:
                if(!wShare.shareWechat(ShareConstant.TITLE, ShareConstant.URL, content, ShareConstant.IMAGE_URL, ShareConstant.URL)){
                    T.show(getContext(), "请先安装微信客户端");
                }
                break;
            case WECHAT_MOMENT:
                if(!wShare.shareWechatMoment(content, ShareConstant.URL, content, ShareConstant.IMAGE_URL, ShareConstant.URL)){
                    T.show(getContext(), "请先安装微信客户端");
                }
                break;
            case SINA_WEIBO:

                break;
        }
    }
}
