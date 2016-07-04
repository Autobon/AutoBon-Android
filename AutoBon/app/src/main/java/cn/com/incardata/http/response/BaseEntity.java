package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 16/2/17.
 */
public class BaseEntity implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.result ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeString(this.error);
    }

    public BaseEntity() {
    }

    protected BaseEntity(Parcel in) {
        this.result = in.readByte() != 0;
        this.message = in.readString();
        this.error = in.readString();
    }

}
