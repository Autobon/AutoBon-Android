package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 实体类基类
 * Created by yang on 2016/11/10.
 */
public class BaseEntityTwo implements Parcelable{
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }

    public BaseEntityTwo() {
    }

    protected BaseEntityTwo(Parcel in) {
        this.status = in.readByte() != 0;
    }

    public static final Creator<BaseEntityTwo> CREATOR = new Creator<BaseEntityTwo>() {
        @Override
        public BaseEntityTwo createFromParcel(Parcel source) {
            return new BaseEntityTwo(source);
        }

        @Override
        public BaseEntityTwo[] newArray(int size) {
            return new BaseEntityTwo[size];
        }
    };
}
