package cn.com.incardata.getui;

import android.os.Parcel;
import android.os.Parcelable;

import cn.com.incardata.http.response.MyInfo_Data;

/**
 * 合作邀请
 * Created by wanghao on 16/3/17.
 */
public class InvitationMsg extends BaseMsg implements Parcelable {
    private int order;
    private MyInfo_Data owner;
    private MyInfo_Data partner;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
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
        super.writeToParcel(dest, flags);
        dest.writeInt(this.order);
        dest.writeParcelable(this.owner, flags);
        dest.writeParcelable(this.partner, flags);
    }

    public InvitationMsg() {
    }

    protected InvitationMsg(Parcel in) {
        super(in);
        this.order = in.readInt();
        this.owner = in.readParcelable(MyInfo_Data.class.getClassLoader());
        this.partner = in.readParcelable(MyInfo_Data.class.getClassLoader());
    }

    public static final Creator<InvitationMsg> CREATOR = new Creator<InvitationMsg>() {
        @Override
        public InvitationMsg createFromParcel(Parcel source) {
            return new InvitationMsg(source);
        }

        @Override
        public InvitationMsg[] newArray(int size) {
            return new InvitationMsg[size];
        }
    };
}
