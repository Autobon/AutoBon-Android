package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2016/3/10.
 */
public class OrderInfo_Data implements Parcelable {
    private int id;
    private String orderNum;
    private int orderType;
    private String photo;
    private long orderTime;
    private long addTime;
    private int creatorType;
    private int creatorId;
    private String creatorName;
    private String contactPhone;
    private String positionLon;
    private String positionLat;
    private String remark;
    private MyInfo_Data mainTech;
    private MyInfo_Data secondTech;
    private OrderInfo_Construction mainConstruct;
    private OrderInfo_Construction secondConstruct;
    private OrderInfo_Data_Comment comment;
    private OrderInfo_Cooperator cooperator;
    private String status;

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

    public MyInfo_Data getMainTech() {
        return mainTech;
    }

    public void setMainTech(MyInfo_Data mainTech) {
        this.mainTech = mainTech;
    }

    public MyInfo_Data getSecondTech() {
        return secondTech;
    }

    public void setSecondTech(MyInfo_Data secondTech) {
        this.secondTech = secondTech;
    }

    public OrderInfo_Construction getMainConstruct() {
        return mainConstruct;
    }

    public void setMainConstruct(OrderInfo_Construction mainConstruct) {
        this.mainConstruct = mainConstruct;
    }

    public OrderInfo_Construction getSecondConstruct() {
        return secondConstruct;
    }

    public void setSecondConstruct(OrderInfo_Construction secondConstruct) {
        this.secondConstruct = secondConstruct;
    }

    public OrderInfo_Data_Comment getComment() {
        return comment;
    }

    public void setComment(OrderInfo_Data_Comment comment) {
        this.comment = comment;
    }

    public OrderInfo_Cooperator getCooperator() {
        return cooperator;
    }

    public void setCooperator(OrderInfo_Cooperator cooperator) {
        this.cooperator = cooperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        dest.writeParcelable(this.mainTech, flags);
        dest.writeParcelable(this.secondTech, flags);
        dest.writeParcelable(this.mainConstruct, flags);
        dest.writeParcelable(this.secondConstruct, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.cooperator, flags);
        dest.writeString(this.status);
    }

    public OrderInfo_Data() {
    }

    protected OrderInfo_Data(Parcel in) {
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
        this.mainTech = in.readParcelable(MyInfo_Data.class.getClassLoader());
        this.secondTech = in.readParcelable(MyInfo_Data.class.getClassLoader());
        this.mainConstruct = in.readParcelable(OrderInfo_Construction.class.getClassLoader());
        this.secondConstruct = in.readParcelable(OrderInfo_Construction.class.getClassLoader());
        this.comment = in.readParcelable(OrderInfo_Data_Comment.class.getClassLoader());
        this.cooperator = in.readParcelable(OrderInfo_Cooperator.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Creator<OrderInfo_Data> CREATOR = new Creator<OrderInfo_Data>() {
        @Override
        public OrderInfo_Data createFromParcel(Parcel source) {
            return new OrderInfo_Data(source);
        }

        @Override
        public OrderInfo_Data[] newArray(int size) {
            return new OrderInfo_Data[size];
        }
    };
}
