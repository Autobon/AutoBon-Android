package cn.com.incardata.http.response;

/**
 * 发送验证码
 * Created by wanghao on 16/2/18.
 */
public class VerifySmsEntity extends BaseEntity{
   private Object data;

   public Object getData() {
      return data;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
