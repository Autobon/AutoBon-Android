package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 二期 未完成订单
 * Created by yang on 2016/11/10.
 */
public class UnfinishOrder implements Parcelable {
    private int id;
    private String orderNum;
    private String photo;
    private int creatorType;
    private int techId;

    private int orderType;
    private long orderTime;
    private long addTime;
    private long finishTime;
    private long takenTime;
    private int creatorId;
    private int coopId;
    private String creatorName;
    private String contactPhone;
    private String positionLon;
    private String positionLat;
    private String remark;
    private int mainTechId;
    private int secondTechId;
    private String beforePhotos;
    private String afterPhotos;
    private long signTime;
    private long startTime;
    private long endTime;
    private String type;
    private String status;
    private long agreedStartTime;
    private long agreedEndTime;


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

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(long takenTime) {
        this.takenTime = takenTime;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
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

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAgreedStartTime() {
        return agreedStartTime;
    }

    public void setAgreedStartTime(long agreedStartTime) {
        this.agreedStartTime = agreedStartTime;
    }

    public long getAgreedEndTime() {
        return agreedEndTime;
    }

    public void setAgreedEndTime(long agreedEndTime) {
        this.agreedEndTime = agreedEndTime;
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
        dest.writeLong(this.finishTime);
        dest.writeLong(this.takenTime);
        dest.writeInt(this.creatorId);
        dest.writeInt(this.coopId);
        dest.writeString(this.creatorName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.positionLon);
        dest.writeString(this.positionLat);
        dest.writeString(this.remark);
        dest.writeInt(this.mainTechId);
        dest.writeInt(this.secondTechId);
        dest.writeString(this.beforePhotos);
        dest.writeString(this.afterPhotos);
        dest.writeLong(this.signTime);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.type);
        dest.writeString(this.status);
        dest.writeLong(this.agreedStartTime);
        dest.writeLong(this.agreedEndTime);
    }

    public UnfinishOrder() {
    }

    protected UnfinishOrder(Parcel in) {
        this.id = in.readInt();
        this.orderNum = in.readString();
        this.orderType = in.readInt();
        this.photo = in.readString();
        this.orderTime = in.readLong();
        this.addTime = in.readLong();
        this.finishTime = in.readLong();
        this.takenTime = in.readLong();
        this.creatorId = in.readInt();
        this.coopId = in.readInt();
        this.creatorName = in.readString();
        this.contactPhone = in.readString();
        this.positionLon = in.readString();
        this.positionLat = in.readString();
        this.remark = in.readString();
        this.mainTechId = in.readInt();
        this.secondTechId = in.readInt();
        this.beforePhotos = in.readString();
        this.afterPhotos = in.readString();
        this.signTime = in.readLong();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.type = in.readString();
        this.status = in.readString();
        this.agreedStartTime = in.readLong();
        this.agreedEndTime = in.readLong();
    }

    public static final Creator<UnfinishOrder> CREATOR = new Creator<UnfinishOrder>() {
        @Override
        public UnfinishOrder createFromParcel(Parcel source) {
            return new UnfinishOrder(source);
        }

        @Override
        public UnfinishOrder[] newArray(int size) {
            return new UnfinishOrder[size];
        }
    };
}
