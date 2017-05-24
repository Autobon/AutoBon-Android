package cn.com.incardata.http.response;

/**
 * 发送验证码
 * Created by wanghao on 16/2/18.
 */
public class VerifySmsEntity extends BaseEntityTwo{
  private String message;

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
