package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangming on 2016/3/10.
 */
public class OrderInfo_Construction implements Parcelable {
    private int id;
    private int orderId;
    private int techId;
    private String positionLon;
    private String positionLat;
    private Long startTime;
    private Long signinTime;
    private long endTime;
    private String beforePhotos;
    private String afterPhotos;
    private int payStatus;//支付状态(0-未出账，1-已出账，未支付，2-已支付)
    private int payment;//支付金额
    private String workItems;
    private double workPercent;
    private int carSeat;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getPositionLon() {
        return positionLon;
    }

    public void setPositionLon(String positionLon) {
        this.positionLon = positionLon;
    }

    public String getPositionLat() {
        return positionLat;
    }

    public void setPositionLat(String positionLat) {
        this.positionLat = positionLat;
    }

    public Long getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(long signinTime) {
        this.signinTime = signinTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getBeforePhotos() {
        return beforePhotos;
    }

    public void setBeforePhotos(String beforePhotos) {
        this.beforePhotos = beforePhotos;
    }

    public String getAfterPhotos() {
        return afterPhotos;
    }

    public void setAfterPhotos(String afterPhotos) {
        this.afterPhotos = afterPhotos;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getWorkItems() {
        return workItems;
    }

    public void setWorkItems(String workItems) {
        this.workItems = workItems;
    }

    public double getWorkPercent() {
        return workPercent;
    }

    public void setWorkPercent(double workPercent) {
        this.workPercent = workPercent;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.orderId);
        dest.writeInt(this.techId);
        dest.writeString(this.positionLon);
        dest.writeString(this.positionLat);
        dest.writeLong(this.startTime == null ? -1 : this.startTime);
        dest.writeLong(this.signinTime == null ? -1 : this.signinTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.beforePhotos);
        dest.writeString(this.afterPhotos);
        dest.writeInt(this.payStatus);
        dest.writeInt(this.payment);
        dest.writeString(this.workItems);
        dest.writeDouble(this.workPercent);
        dest.writeInt(this.carSeat);
    }

    public OrderInfo_Construction() {
    }

    protected OrderInfo_Construction(Parcel in) {
        this.id = in.readInt();
        this.orderId = in.readInt();
        this.techId = in.readInt();
        this.positionLon = in.readString();
        this.positionLat = in.readString();
        this.startTime = in.readLong();
        this.signinTime = in.readLong();
        this.endTime = in.readLong();
        this.beforePhotos = in.readString();
        this.afterPhotos = in.readString();
        this.payStatus = in.readInt();
        this.payment = in.readInt();
        this.workItems = in.readString();
        this.workPercent = in.readDouble();
        this.carSeat = in.readInt();
    }

    public static final Parcelable.Creator<OrderInfo_Construction> CREATOR = new Parcelable.Creator<OrderInfo_Construction>() {
        public OrderInfo_Construction createFromParcel(Parcel source) {
            return new OrderInfo_Construction(source);
        }

        public OrderInfo_Construction[] newArray(int size) {
            return new OrderInfo_Construction[size];
        }
    };
}
