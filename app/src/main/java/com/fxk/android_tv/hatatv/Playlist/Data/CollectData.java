package com.fxk.android_tv.hatatv.Playlist.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *  Наследуется от  Parcelable для передачи объектов между активностями
 *  Данная реализация пока не включает в себя обработку телепрограммы EPG_ONAIR
 * */
public class CollectData implements Parcelable{
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("service_uri")
    public String service_uri;
    @SerializedName("meta")
    public Meta meta;
    @SerializedName("epg_onair")
    public ArrayList<Epg_onair> epg_onair;

    protected CollectData(Parcel in) {
        id = in.readString();
        name = in.readString();
        service_uri = in.readString();
        meta = in.readParcelable(Meta.class.getClassLoader());
    }

    public static final Creator<CollectData> CREATOR = new Creator<CollectData>() {
        @Override
        public CollectData createFromParcel(Parcel in) {
            return new CollectData(in);
        }

        @Override
        public CollectData[] newArray(int size) {
            return new CollectData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(service_uri);
        parcel.writeParcelable(meta, 0);
    }


    public class Epg_onair{
        @SerializedName("title")
        public String title;
        @SerializedName("descriprion")
        public String description;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("start")
        private String start;
        @SerializedName("finish")
        private String finish;

        public Date getStart(){
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                return data.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public Date getFinish(){
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                return data.parse(finish);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String reformDateToString(Date str_time){
            SimpleDateFormat out = new SimpleDateFormat("HH:mm");
            return out.format(str_time);
        }
    }

}
