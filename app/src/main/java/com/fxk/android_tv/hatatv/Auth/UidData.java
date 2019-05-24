package com.fxk.android_tv.hatatv.Auth;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class UidData {
    private String android_uid;
    private String android_os;
    private String android_model;
    private String HASH_UID;

    public RequestDataUID uid ;

    public UidData(Context context){
        // получает уникальный индетефикатор android устройства
        setAndroid_uid(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        // получает версию ОС
        setAndroid_os(Build.VERSION.RELEASE);
        // получает модель устройства (конкретного имени не найдем)
        setAndroid_model(Build.MODEL);
        setHASH_UID(getHashUID());
        uid = new RequestDataUID();
    }

    public String getAndroid_uid() {
        return android_uid;
    }

    public void setAndroid_uid(String android_uid) {
        this.android_uid = android_uid;
    }

    public String getAndroid_model() {
        return android_model;
    }

    public void setAndroid_model(String android_model) {
        this.android_model = android_model;
    }

    public String getAndroid_os() {
        return android_os;
    }

    public void setAndroid_os(String android_os) {
        this.android_os = android_os;
    }

    public String getHASH_UID() {
        return HASH_UID;
    }

    public void setHASH_UID(String HASH_UID) {
        this.HASH_UID = HASH_UID;
    }

    private String getHashUID(){
        String data = getAndroid_uid() + getAndroid_model();
        try{
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(data.getBytes());
            byte dataDigest[] = digest.digest();
            StringBuffer hexData = new StringBuffer();
            for(int i =0; i < dataDigest.length; i++)
                hexData.append(Integer.toHexString(0xff & dataDigest[i]));
            return hexData.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public class RequestDataUID{
        public String hashid;
        public String uid;
        public String version;

        public RequestDataUID(){
            hashid = getHASH_UID();
            uid = getAndroid_uid();
            version = getAndroid_os();
        }
    }
}
