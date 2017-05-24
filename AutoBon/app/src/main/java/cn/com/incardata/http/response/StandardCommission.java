package cn.com.incardata.http.response;

/** 佣金标准
 * Created by yang on 2017/2/17.
 */
public class StandardCommission {
    private String workName;
    private String workMoney;

    public StandardCommission(String workName, String workMoney) {
        this.workName = workName;
        this.workMoney = workMoney;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkMoney() {
        return workMoney;
    }

    public void setWorkMoney(String workMoney) {
        this.workMoney = workMoney;
    }
}
