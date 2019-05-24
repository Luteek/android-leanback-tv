package com.fxk.android_tv.hatatv.Network;

public class DataException {
    public Detail meta;

    public DataException(){
        meta=new Detail();
    }
    public final class Detail{
        public String function_name;
        public String error_detail;
    }
}
