package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Comment;

/** 二期 订单详情
 * Created by yang on 2016/11/10.
 */
public class OrderInfo implements Parcelable {

    private int id;
    private String orderNum;
    private String photo;
    private long agreedStartTime;
    private long agreedEndTime;
    private int creatorType;
    private int techId;
    private String techName;
    private String techAvatar;
    private String techPhone;
    private String beforePhotos;
    private String afterPhotos;
    private long startTime;
    private long endTime;
    private long signTime;
    private long takenTime;
    private long createTime;
    private String type;
    private int coopId;
    private String coopName;
    private String address;
    private String longitude;
    private String latitude;
    private int creatorId;
    private String creatorName;
    private String contactPhone;
    private String remark;
    private int evaluateStatus;
    private int orderCount;
    private Float evaluate;
    private int cancelCount;
    private int productStatus;
    private int reassignmentStatus;
    private Float payment;
    private Integer payStatus;
    private String techLongitude;
    private String techLatitude;
    private OrderConstructionShow[] orderConstructionShow;
    private ConstructionWasteShow[] constructionWasteShows;
    private OrderInfo_Data_Comment comment;
    private String status;
    private String license;
    private String vin;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechAvatar() {
        return techAvatar;
    }

    public void setTechAvatar(String techAvatar) {
        this.techAvatar = techAvatar;
    }

    public String getTechPhone() {
        return techPhone;
    }

    public void setTechPhone(String techPhone) {
        this.techPhone = techPhone;
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

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public long getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(long takenTime) {
        this.takenTime = takenTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
    }

    public String getCoopName() {
        return coopName;
    }

    public void setCoopName(String coopName) {
        this.coopName = coopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(int evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public Float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Float evaluate) {
        this.evaluate = evaluate;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public int getReassignmentStatus() {
        return reassignmentStatus;
    }

    public void setReassignmentStatus(int reassignmentStatus) {
        this.reassignmentStatus = reassignmentStatus;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getTechLongitude() {
        return techLongitude;
    }

    public void setTechLongitude(String techLongitude) {
        this.techLongitude = techLongitude;
    }

    public String getTechLatitude() {
        return techLatitude;
    }

    public void setTechLatitude(String techLatitude) {
        this.techLatitude = techLatitude;
    }

    public OrderConstructionShow[] getOrderConstructionShow() {
        return orderConstructionShow;
    }

    public void setOrderConstructionShow(OrderConstructionShow[] orderConstructionShow) {
        this.orderConstructionShow = orderConstructionShow;
    }

    public ConstructionWasteShow[] getConstructionWasteShows() {
        return constructionWasteShows;
    }

    public void setConstructionWasteShows(ConstructionWasteShow[] constructionWasteShows) {
        this.constructionWasteShows = constructionWasteShows;
    }

    public OrderInfo_Data_Comment getComment() {
        return comment;
    }

    public void setComment(OrderInfo_Data_Comment comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.orderNum);
        dest.writeString(this.photo);
        dest.writeLong(this.agreedStartTime);
        dest.writeLong(this.agreedEndTime);
        dest.writeInt(this.creatorType);
        dest.writeInt(this.techId);
        dest.writeString(this.techName);
        dest.writeString(this.techAvatar);
        dest.writeString(this.techPhone);
        dest.writeString(this.beforePhotos);
        dest.writeString(this.afterPhotos);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeLong(this.signTime);
        dest.writeLong(this.takenTime);
        dest.writeLong(this.createTime);
        dest.writeString(this.type);
        dest.writeInt(this.coopId);
        dest.writeString(this.coopName);
        dest.writeString(this.address);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeInt(this.creatorId);
        dest.writeString(this.creatorName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.remark);
        dest.writeInt(this.evaluateStatus);
        dest.writeInt(this.orderCount);
        dest.writeValue(this.evaluate);
        dest.writeInt(this.cancelCount);
        dest.writeInt(this.productStatus);
        dest.writeInt(this.reassignmentStatus);
        dest.writeValue(this.payment);
        dest.writeValue(this.payStatus);
        dest.writeString(this.techLongitude);
        dest.writeString(this.techLatitude);
        dest.writeTypedArray(this.orderConstructionShow, flags);
        dest.writeTypedArray(this.constructionWasteShows, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeString(this.status);
        dest.writeString(this.license);
        dest.writeString(this.vin);
    }

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        this.id = in.readInt();
        this.orderNum = in.readString();
        this.photo = in.readString();
        this.agreedStartTime = in.readLong();
        this.agreedEndTime = in.readLong();
        this.creatorType = in.readInt();
        this.techId = in.readInt();
        this.techName = in.readString();
        this.techAvatar = in.readString();
        this.techPhone = in.readString();
        this.beforePhotos = in.readString();
        this.afterPhotos = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.signTime = in.readLong();
        this.takenTime = in.readLong();
        this.createTime = in.readLong();
        this.type = in.readString();
        this.coopId = in.readInt();
        this.coopName = in.readString();
        this.address = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.creatorId = in.readInt();
        this.creatorName = in.readString();
        this.contactPhone = in.readString();
        this.remark = in.readString();
        this.evaluateStatus = in.readInt();
        this.orderCount = in.readInt();
        this.evaluate = (Float) in.readValue(Float.class.getClassLoader());
        this.cancelCount = in.readInt();
        this.productStatus = in.readInt();
        this.reassignmentStatus = in.readInt();
        this.payment = (Float) in.readValue(Float.class.getClassLoader());
        this.payStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.techLongitude = in.readString();
        this.techLatitude = in.readString();
        this.orderConstructionShow = in.createTypedArray(OrderConstructionShow.CREATOR);
        this.constructionWasteShows = in.createTypedArray(ConstructionWasteShow.CREATOR);
        this.comment = in.readParcelable(OrderInfo_Data_Comment.class.getClassLoader());
        this.status = in.readString();
        this.license = in.readString();
        this.vin = in.readString();
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
