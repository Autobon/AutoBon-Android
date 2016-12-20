package cn.com.incardata.http.response;

/** 修改银行卡实体类
 * Created by zhangming on 2016/3/3.
 */
public class ModifyBankCardEntity extends BaseEntityTwo{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
