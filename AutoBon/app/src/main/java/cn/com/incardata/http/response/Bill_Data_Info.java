package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/18.
 * 每一条账单信息
 */
public class Bill_Data_Info {
    private int id;
    private int techId;
    private long billMonth;
    private int count;
    private double sum;
    private boolean paid;
    private String payAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public long getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(long billMonth) {
        this.billMonth = billMonth;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getPayAt() {
        return payAt;
    }

    public void setPayAt(String payAt) {
        this.payAt = payAt;
    }
}
