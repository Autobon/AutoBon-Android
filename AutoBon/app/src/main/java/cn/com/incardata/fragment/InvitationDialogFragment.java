package cn.com.incardata.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.InvitationActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.getui.InvitationMsg;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.view.CircleImageView;

/**
 * 邀请dialog
 * @author wanghao
 */
public class InvitationDialogFragment extends DialogFragment implements View.OnClickListener{
    private OnClickListener mListener;
    private View rootView;
    private CircleImageView header;
    private TextView userName;
    private RatingBar ratingBar;
    private TextView rate;
    private TextView orderNum;
    private TextView goodRate;
    private TextView orderInfo;
    private Button previewOrder;

    private OrderInfo_Data orderInfo_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_invitation_dialog, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        initViews();
        return rootView;
    }

    private void initViews() {
        header = (CircleImageView) rootView.findViewById(R.id.header_image);
        userName = (TextView) rootView.findViewById(R.id.username);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingbar);
        rate = (TextView) rootView.findViewById(R.id.rate);
        orderNum = (TextView) rootView.findViewById(R.id.order_num);
        goodRate = (TextView) rootView.findViewById(R.id.tv_good_rate);
        orderInfo = (TextView) rootView.findViewById(R.id.order_info);
        previewOrder = (Button) rootView.findViewById(R.id.preview_order);

        rootView.findViewById(R.id.delete).setOnClickListener(this);
        previewOrder.setOnClickListener(this);
        updateView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof OnClickListener) {
//            mListener = (OnClickListener) activity;
//        } else {
//            throw new RuntimeException(activity.toString()
//                    + " must implement OnForceListener");
//        }
    }

    private InvitationMsg invitation;
    public void update(InvitationMsg invitation){
        this.invitation = invitation;
       updateView();
    }

    private void updateView(){
        if (header == null) return;
        if (invitation != null){
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + invitation.getOwner().getAvatar(), header);
            userName.setText(invitation.getOwner().getName());
            orderNum.setText(invitation.getOwner().getTotalOrders());
            loadOrderInfo(invitation.getOrder());
            try {
                float starNum = Float.parseFloat(invitation.getOwner().getStarRate());
                ratingBar.setRating((int)Math.floor(starNum));  //取整设置星级
                rate.setText(String.valueOf(DecimalUtil.FloatDecimal1(starNum)));  //设置保留一位小数
            }catch (Exception e){
                ratingBar.setRating(0);
                rate.setText("0");
            }
        }
    }

    private void loadOrderInfo(final int orderId){
        Http.getInstance().getTaskToken(NetURL.getOrderInfo(orderId), "", OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity != null && entity instanceof OrderInfoEntity && ((OrderInfoEntity) entity).isResult()){
                    orderInfo.setText("邀请您参与" + MyApplication.getInstance().getSkill(((OrderInfoEntity) entity).getData().getOrderType()) + "的订单，订单编号" + ((OrderInfoEntity) entity).getData().getOrderNum());
                    orderInfo_data = ((OrderInfoEntity) entity).getData();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                dismiss();
                break;
            case R.id.preview_order:
                Intent intent = new Intent(getActivity(), InvitationActivity.class);
                intent.putExtra(AutoCon.ORDER_INFO, orderInfo_data);
                startActivity(intent);
                dismiss();
                break;
        }
    }

    public interface OnClickListener {
        // TODO: Update argument type and name
        void onClick(View v);
    }
}
