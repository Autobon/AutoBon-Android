package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论
 * Created by wanghao on 16/3/22.
 */
public class OrderInfo_Data_Comment implements Parcelable {
    private String advice;//其他建议
    private boolean arriveOnTime;//准时到达
    private boolean carProtect;//车辆保护
    private boolean completeOnTime;//准时完成
    private long createAt;//评论时间
    private boolean dressNeatly;//着装整洁
    private boolean goodAttitude;//态度好
    private int id;
    private int orderId;
    private boolean professional;//专业技能
    private int star;
    private int techId;

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public boolean isArriveOnTime() {
        return arriveOnTime;
    }

    public void setArriveOnTime(boolean arriveOnTime) {
        this.arriveOnTime = arriveOnTime;
    }

    public boolean isCarProtect() {
        return carProtect;
    }

    public void setCarProtect(boolean carProtect) {
        this.carProtect = carProtect;
    }

    public boolean isCompleteOnTime() {
        return completeOnTime;
    }

    public void setCompleteOnTime(boolean completeOnTime) {
        this.completeOnTime = completeOnTime;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public boolean isDressNeatly() {
        return dressNeatly;
    }

    public void setDressNeatly(boolean dressNeatly) {
        this.dressNeatly = dressNeatly;
    }

    public boolean isGoodAttitude() {
        return goodAttitude;
    }

    public void setGoodAttitude(boolean goodAttitude) {
        this.goodAttitude = goodAttitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isProfessional() {
        return professional;
    }

    public void setProfessional(boolean professional) {
        this.professional = professional;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advice);
        dest.writeByte(arriveOnTime ? (byte) 1 : (byte) 0);
        dest.writeByte(carProtect ? (byte) 1 : (byte) 0);
        dest.writeByte(completeOnTime ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createAt);
        dest.writeByte(dressNeatly ? (byte) 1 : (byte) 0);
        dest.writeByte(goodAttitude ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeInt(this.orderId);
        dest.writeByte(professional ? (byte) 1 : (byte) 0);
        dest.writeInt(this.star);
        dest.writeInt(this.techId);
    }

    public OrderInfo_Data_Comment() {
    }

    protected OrderInfo_Data_Comment(Parcel in) {
        this.advice = in.readString();
        this.arriveOnTime = in.readByte() != 0;
        this.carProtect = in.readByte() != 0;
        this.completeOnTime = in.readByte() != 0;
        this.createAt = in.readLong();
        this.dressNeatly = in.readByte() != 0;
        this.goodAttitude = in.readByte() != 0;
        this.id = in.readInt();
        this.orderId = in.readInt();
        this.professional = in.readByte() != 0;
        this.star = in.readInt();
        this.techId = in.readInt();
    }

    public static final Parcelable.Creator<OrderInfo_Data_Comment> CREATOR = new Parcelable.Creator<OrderInfo_Data_Comment>() {
        public OrderInfo_Data_Comment createFromParcel(Parcel source) {
            return new OrderInfo_Data_Comment(source);
        }

        public OrderInfo_Data_Comment[] newArray(int size) {
            return new OrderInfo_Data_Comment[size];
        }
    };
}
