package com.github.mr5.androidrouter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.code.regexp.Pattern;

public class CompiledRoute implements Parcelable {
    private List<String> variables;
    private Pattern regex;
    private String constantUrl;
    private int type;
    private String activityClass;
    private String proxyDestIdentify;
    private List<String> middlewares;
    private String anchor;
    private List<String> schemes;

    public CompiledRoute() {
    }


    public List<String> getSchemes() {
        return schemes;
    }

    public CompiledRoute setSchemes(List<String> schemes) {
        this.schemes = schemes;

        return this;
    }

    public int getType() {
        return type;
    }

    public CompiledRoute setType(int type) {
        this.type = type;

        return this;
    }

    public String getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(String activityClass) {
        this.activityClass = activityClass;
    }

    public String getProxyDestIdentify() {
        return proxyDestIdentify;
    }

    public void setProxyDestIdentify(String proxyDestIdentify) {
        this.proxyDestIdentify = proxyDestIdentify;
    }

    public List<String> getMiddlewares() {
        return middlewares;
    }

    public void setMiddlewares(List<String> middlewares) {
        this.middlewares = middlewares;
    }

    public String getAnchor() {
        return anchor;
    }

    public CompiledRoute setAnchor(String anchor) {
        this.anchor = anchor;

        return this;
    }

    public List<String> getVariables() {
        return variables;
    }

    public CompiledRoute setVariables(List<String> variables) {
        this.variables = variables;

        return this;
    }

    public Pattern getRegex() {
        return regex;
    }

    public CompiledRoute setRegex(Pattern regex) {
        this.regex = regex;

        return this;
    }

    public String getConstantUrl() {
        return constantUrl;
    }

    public CompiledRoute setConstantUrl(String constantUrl) {
        this.constantUrl = constantUrl;
        return this;
    }

    public String getNextFragment(String fragmentName) {
        int index = middlewares.indexOf(fragmentName);
        if (index >= 0 && index < middlewares.size()) {
            return middlewares.get(index + 1);
        }

        return null;
    }

    public String toString() {
        return getConstantUrl() == null ?
                (
                        getAnchor() != null ?
                                getRegex().toString() + "#" + getAnchor()
                                : getRegex().toString()
                )
                : getConstantUrl();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(variables);
        parcel.writeString(regex.toString());
        parcel.writeString(constantUrl);
        parcel.writeInt(type);
        parcel.writeString(activityClass);
        parcel.writeString(proxyDestIdentify);
        parcel.writeList(middlewares);
        parcel.writeString(anchor);
        parcel.writeList(schemes);
    }

    protected CompiledRoute(Parcel in) {
        variables = in.createStringArrayList();
        regex = Pattern.compile(in.readString());
        constantUrl = in.readString();
        type = in.readInt();
        activityClass = in.readString();
        proxyDestIdentify = in.readString();
        middlewares = in.createStringArrayList();
        anchor = in.readString();
        schemes = in.createStringArrayList();
    }

    public static final Creator<CompiledRoute> CREATOR = new Creator<CompiledRoute>() {
        @Override
        public CompiledRoute createFromParcel(Parcel in) {
            return new CompiledRoute(in);
        }

        @Override
        public CompiledRoute[] newArray(int size) {
            return new CompiledRoute[size];
        }
    };
}