package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/6.
 */
public class StartWork_Data {
    private int id;
    private int orderId;
    private int technicianId;
    private String positionLon;
    private String positionLat;
    private long startTime;
    private String signinTime;
    private String endTime;
    private String beforePhotos;
    private String afterPhotos;
    private String payment;
    private String workItems;
    private int carSeat;

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

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(String signinTime) {
        this.signinTime = signinTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getWorkItems() {
        return workItems;
    }

    public void setWorkItems(String workItems) {
        this.workItems = workItems;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
    }
}
