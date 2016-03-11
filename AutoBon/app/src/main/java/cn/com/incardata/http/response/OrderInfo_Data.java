package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/10.
 */
public class OrderInfo_Data {
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
    private String comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
