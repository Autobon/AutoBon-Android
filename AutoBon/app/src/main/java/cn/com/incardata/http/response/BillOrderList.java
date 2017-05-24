package cn.com.incardata.http.response;

/** 账单订单列表实体类
 * Created by yang on 2016/12/12.
 */
public class BillOrderList {

    private String orderNum;
    private Integer payStatus;
    private Float payment;
    private long createDate;
    private Integer project1;
    private Integer project2;
    private Integer project3;
    private Integer project4;
    private int source;  // 账单来源


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public Integer getProject1() {
        return project1;
    }

    public void setProject1(Integer project1) {
        this.project1 = project1;
    }

    public Integer getProject2() {
        return project2;
    }

    public void setProject2(Integer project2) {
        this.project2 = project2;
    }

    public Integer getProject3() {
        return project3;
    }

    public void setProject3(Integer project3) {
        this.project3 = project3;
    }

    public Integer getProject4() {
        return project4;
    }

    public void setProject4(Integer project4) {
        this.project4 = project4;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
