package com.hamstersapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Костя on 13.04.2016.
 */

public class HamsterModel implements Parcelable {

    private String title;
    private String description;
    private String image;
    private String pinned;

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
}
