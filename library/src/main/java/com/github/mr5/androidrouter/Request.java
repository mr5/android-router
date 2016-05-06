package com.github.mr5.androidrouter;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Map;

public class Request implements Parcelable {
    private String refererUrl;
    private String refererClass;
    private String url;
    private String scheme;
    private String host;
    private Map<String, String> query;
    private int requestCode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}