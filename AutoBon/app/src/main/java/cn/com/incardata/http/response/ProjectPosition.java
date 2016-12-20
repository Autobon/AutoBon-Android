package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**二期 施工部位
 * Created by yang on 2016/11/10.
 */
public class ProjectPosition implements Parcelable {
    private String project;
    private String position;


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.project);
        dest.writeString(this.position);
    }

    public ProjectPosition() {
    }

    protected ProjectPosition(Parcel in) {
        this.project = in.readString();
        this.position = in.readString();
    }

    public static final Parcelable.Creator<ProjectPosition> CREATOR = new Parcelable.Creator<ProjectPosition>() {
        @Override
        public ProjectPosition createFromParcel(Parcel source) {
            return new ProjectPosition(source);
        }

        @Override
        public ProjectPosition[] newArray(int size) {
            return new ProjectPosition[size];
        }
    };
}
