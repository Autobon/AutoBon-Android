package cn.com.incardata.http.response;

import java.util.Date;

/**
 * 提现参数实体类
 * <p>Created by wangyang on 2017/10/24.</p>
 */
public class TakeCashJson {
    private int techId;
    private double applyMoney;
    private Date applyDate;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public double getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(double applyMoney) {
        this.applyMoney = applyMoney;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
}
