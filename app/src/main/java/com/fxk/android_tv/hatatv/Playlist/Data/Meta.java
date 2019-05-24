package com.fxk.android_tv.hatatv.Playlist.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/**
 *  Наследуется от  Parcelable для передачи объектов между активностями
 * */
public class Meta implements Parcelable {
    @SerializedName("lcn")
    public String lcn;
    @SerializedName("group")
    public String group;
    @SerializedName("logo")
    public String logo;
    @SerializedName("site")
    public String site;
    @SerializedName("description")
    public String description;
    @SerializedName("status")
    public String status;
    @SerializedName("bitrate")
    public String bitrate;

    protected Meta(Parcel in) {
        lcn = in.readString();
        group = in.readString();
        logo = in.readString();
        site = in.readString();
        description = in.readString();
        status = in.readString();
        bitrate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lcn);
        parcel.writeString(group);
        parcel.writeString(logo);
        parcel.writeString(site);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeString(bitrate);
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        @Override
        public Meta createFromParcel(Parcel parcel) {
            return new Meta(parcel);
        }

        @Override
        public Meta[] newArray(int i) {
            return new Meta[i];
        }
    };
}
