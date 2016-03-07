package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/7.
 * 账单实体类(临时)
 */
public class BillEntity extends BaseEntity{
    private String year;
    private String month;
    private double pay;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
}
