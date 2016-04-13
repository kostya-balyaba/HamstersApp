package com.hamstersapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Костя on 13.04.2016.
 */

@Table(name = "Hamsters")
public class HamsterModel extends Model implements Parcelable {

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String image;
    @Column(name = "pinned")
    private String pinned;

    public HamsterModel() {
    }

    protected HamsterModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
        pinned = in.readString();
    }

    public static final Creator<HamsterModel> CREATOR = new Creator<HamsterModel>() {
        @Override
        public HamsterModel createFromParcel(Parcel in) {
            return new HamsterModel(in);
        }

        @Override
        public HamsterModel[] newArray(int size) {
            return new HamsterModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(pinned);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public boolean getPinned() {
        return Boolean.valueOf(pinned);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HamsterModel that = (HamsterModel) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        return !(pinned != null ? !pinned.equals(that.pinned) : that.pinned != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (pinned != null ? pinned.hashCode() : 0);
        return result;
    }
}
