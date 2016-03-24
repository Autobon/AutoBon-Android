package cn.com.incardata.http.response;

/**
 * Created by wanghao on 16/2/17.
 */
public class BaseEntity {
    private boolean result;
    private String message;
    private String error;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
