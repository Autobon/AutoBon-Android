package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 学习园地列表数据
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class StudyGardenData implements Parcelable {
    private int id;
    private int type;
    private String fileName;
    private long fileLength;
    private String path;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.fileName);
        dest.writeLong(this.fileLength);
        dest.writeString(this.path);
        dest.writeString(this.remark);
    }

    public StudyGardenData() {
    }

    protected StudyGardenData(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.fileName = in.readString();
        this.fileLength = in.readLong();
        this.path = in.readString();
        this.remark = in.readString();
    }

    public static final Parcelable.Creator<StudyGardenData> CREATOR = new Parcelable.Creator<StudyGardenData>() {
        @Override
        public StudyGardenData createFromParcel(Parcel source) {
            return new StudyGardenData(source);
        }

        @Override
        public StudyGardenData[] newArray(int size) {
            return new StudyGardenData[size];
        }
    };
}
