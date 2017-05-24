package cn.com.incardata.http.response;

/** 我的信息实体类
 * Created by yang on 2016/12/7.
 */
public class MyMessageData {
    private MyMessage technician;
    private String starRate;
    private String balance;
    private String unpaidOrders;
    private String totalOrders;


    public MyMessage getTechnician() {
        return technician;
    }

    public void setTechnician(MyMessage technician) {
        this.technician = technician;
    }

    public String getStarRate() {
        return starRate;
    }

    public void setStarRate(String starRate) {
        this.starRate = starRate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(String unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }
}
