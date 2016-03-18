package cn.com.incardata.getui;

import android.os.Parcel;
import android.os.Parcelable;

import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.Order;

/**
 * 合作邀请
 * Created by wanghao on 16/3/17.
 */
public class InvitationMsg extends BaseMsg implements Parcelable {
    private Order order;
    private MyInfo_Data owner;
    private MyInfo_Data partner;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MyInfo_Data getOwner() {
        return owner;
    }

    public void setOwner(MyInfo_Data owner) {
        this.owner = owner;
    }

    public MyInfo_Data getPartner() {
        return partner;
    }

    public void setPartner(MyInfo_Data partner) {
        this.partner = partner;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.order, flags);
        dest.writeParcelable(this.owner, 0);
        dest.writeParcelable(this.partner, 0);
    }

    public InvitationMsg() {
    }

    protected InvitationMsg(Parcel in) {
        this.order = in.readParcelable(Order.class.getClassLoader());
        this.owner = in.readParcelable(MyInfo_Data.class.getClassLoader());
        this.partner = in.readParcelable(MyInfo_Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<InvitationMsg> CREATOR = new Parcelable.Creator<InvitationMsg>() {
        public InvitationMsg createFromParcel(Parcel source) {
            return new InvitationMsg(source);
        }

        public InvitationMsg[] newArray(int size) {
            return new InvitationMsg[size];
        }
    };
}
