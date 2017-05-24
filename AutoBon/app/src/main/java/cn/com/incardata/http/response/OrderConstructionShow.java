package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 二期 施工单
 * Created by yang on 2016/11/10.
 */
public class OrderConstructionShow implements Parcelable {
    private int techId;
    private String techName;
    private int isMainTech;
    private int payStatus;
    private int payment;
    private ProjectPosition[] projectPosition;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public int getIsMainTech() {
        return isMainTech;
    }

    public void setIsMainTech(int isMainTech) {
        this.isMainTech = isMainTech;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public ProjectPosition[] getProjectPosition() {
        return projectPosition;
    }

    public void setProjectPosition(ProjectPosition[] projectPosition) {
        this.projectPosition = projectPosition;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.techId);
        dest.writeString(this.techName);
        dest.writeInt(this.isMainTech);
        dest.writeInt(this.payStatus);
        dest.writeInt(this.payment);
        dest.writeTypedArray(this.projectPosition, flags);
    }

    public OrderConstructionShow() {
    }

    protected OrderConstructionShow(Parcel in) {
        this.techId = in.readInt();
        this.techName = in.readString();
        this.isMainTech = in.readInt();
        this.payStatus = in.readInt();
        this.payment = in.readInt();
        this.projectPosition = in.createTypedArray(ProjectPosition.CREATOR);
    }

    public static final Creator<OrderConstructionShow> CREATOR = new Creator<OrderConstructionShow>() {
        @Override
        public OrderConstructionShow createFromParcel(Parcel source) {
            return new OrderConstructionShow(source);
        }

        @Override
        public OrderConstructionShow[] newArray(int size) {
            return new OrderConstructionShow[size];
        }
    };
}
