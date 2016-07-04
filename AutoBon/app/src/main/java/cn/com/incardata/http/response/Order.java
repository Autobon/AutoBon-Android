package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单
 * Created by wanghao on 16/3/9.
 */
public class Order implements Parcelable {
    private int id;
    private String orderNum;
    private int orderType;
    private String photo;//订单图片
    private long orderTime;//返回时间为毫秒
    private long addTime;
    private int creatorType;
    private int creatorId;
    private String creatorName;
    private String contactPhone;
    private String positionLon;//经度"35.123521",
    private String positionLat;//纬度"20.214411",
    private String remark;//备注
    private int mainTechId;
    private int secondTechId;
    private String status;
    private OrderInfo_Cooperator cooperator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMainTechId() {
        return mainTechId;
    }

    public void setMainTechId(int mainTechId) {
        this.mainTechId = mainTechId;
    }

    public int getSecondTechId() {
        return secondTechId;
    }

    public void setSecondTechId(int secondTechId) {
        this.secondTechId = secondTechId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderInfo_Cooperator getCooperator() {
        return cooperator;
    }

    public void setCooperator(OrderInfo_Cooperator cooperator) {
        this.cooperator = cooperator;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.orderNum);
        dest.writeInt(this.orderType);
        dest.writeString(this.photo);
        dest.writeLong(this.orderTime);
        dest.writeLong(this.addTime);
        dest.writeInt(this.creatorType);
        dest.writeInt(this.creatorId);
        dest.writeString(this.creatorName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.positionLon);
        dest.writeString(this.positionLat);
        dest.writeString(this.remark);
        dest.writeInt(this.mainTechId);
        dest.writeInt(this.secondTechId);
        dest.writeString(this.status);
        dest.writeParcelable(this.cooperator, flags);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.id = in.readInt();
        this.orderNum = in.readString();
        this.orderType = in.readInt();
        this.photo = in.readString();
        this.orderTime = in.readLong();
        this.addTime = in.readLong();
        this.creatorType = in.readInt();
        this.creatorId = in.readInt();
        this.creatorName = in.readString();
        this.contactPhone = in.readString();
        this.positionLon = in.readString();
        this.positionLat = in.readString();
        this.remark = in.readString();
        this.mainTechId = in.readInt();
        this.secondTechId = in.readInt();
        this.status = in.readString();
        this.cooperator = in.readParcelable(OrderInfo_Cooperator.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
