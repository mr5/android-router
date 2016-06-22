package com.github.mr5.androidrouter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.code.regexp.Pattern;

import java.util.List;

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
    protected int weight;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getActivityClass() {
        return activityClass;
    }

    public CompiledRoute setActivityClass(String activityClass) {
        this.activityClass = activityClass;

        return this;
    }

    public String getProxyDestIdentify() {
        return proxyDestIdentify;
    }

    public CompiledRoute setProxyDestIdentify(String proxyDestIdentify) {
        this.proxyDestIdentify = proxyDestIdentify;

        return this;
    }

    public List<String> getMiddlewares() {
        return middlewares;
    }

    public CompiledRoute setMiddlewares(List<String> middlewares) {
        this.middlewares = middlewares;

        return this;
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

    public String getNextMiddleware(String fragmentName) {
        int index = middlewares.indexOf(fragmentName);
        if (index >= 0 && index < middlewares.size() - 1) {
            return middlewares.get(index + 1);
        }

        return null;
    }

    public String toString() {
        String str = getConstantUrl() == null ? getRegex().toString() : getConstantUrl();
        str += getAnchor() != null ? "#" + getAnchor() : "";
        return str;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private boolean objectsCompare(Object object1, Object object2) {
        return object1 == object2 ||
                (
                        object1 != null && object2 != null && object1.equals(object2)
                );
    }

    public boolean equals(CompiledRoute compiledRoute) {
        return objectsCompare(variables, compiledRoute.getVariables()) &&
                objectsCompare(regex, compiledRoute.getRegex()) &&
                objectsCompare(constantUrl, compiledRoute.getConstantUrl()) &&
                objectsCompare(type, compiledRoute.getType()) &&
                objectsCompare(weight, compiledRoute.getWeight()) &&
                objectsCompare(activityClass, compiledRoute.getActivityClass()) &&
                objectsCompare(proxyDestIdentify, compiledRoute.getProxyDestIdentify()) &&
                objectsCompare(middlewares, compiledRoute.getMiddlewares()) &&
                objectsCompare(anchor, compiledRoute.getAnchor()) &&
                objectsCompare(schemes, compiledRoute.getSchemes());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(variables);
        parcel.writeString(regex == null ? null : regex.toString());
        parcel.writeString(constantUrl);
        parcel.writeInt(type);
        parcel.writeInt(weight);
        parcel.writeString(activityClass);
        parcel.writeString(proxyDestIdentify);
        parcel.writeStringList(middlewares);
        parcel.writeString(anchor);
        parcel.writeStringList(schemes);
    }

    protected CompiledRoute(Parcel in) {
        variables = in.createStringArrayList();
        String regexString = in.readString();
        regex = regexString == null ? null : Pattern.compile(regexString);
        constantUrl = in.readString();
        type = in.readInt();
        weight = in.readInt();
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