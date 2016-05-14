package com.github.mr5.androidrouter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.code.regexp.Pattern;
import com.google.code.regexp.Matcher;

public class CompiledRoute implements Cloneable, Parcelable {
    private List<String> variables;
    private Pattern regex;
    private String constantUrl;
    private int type;
    private Class<? extends Activity> activityClass;
    private LinkedList<Class<Fragment>> fragmentClasses;
    private String anchor;
    private List<String> schemes;

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

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public CompiledRoute setActivityClass(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;

        return this;
    }

    public LinkedList<Class<Fragment>> getFragmentClasses() {
        return fragmentClasses;
    }

    public CompiledRoute setFragmentClasses(LinkedList<Class<Fragment>> fragmentClasses) {
        this.fragmentClasses = fragmentClasses;

        return this;
    }

    public Class<Fragment> getNextFragment(Class<Fragment> relativeFragment) {
        if (fragmentClasses == null) {
            return null;
        }
        int relativeFragmentPosition = fragmentClasses.indexOf(relativeFragment);
        if (relativeFragmentPosition < 0) {
            return null;
        }
        return fragmentClasses.get(relativeFragmentPosition + 1);
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
//        private List<String> variables;
//        private Pattern regex;
//        private String constantUrl;
//        private Route.TYPE type;
//        private Class<Activity> activityClass;
//        private LinkedList<Class<Fragment>> fragmentClasses;
//        private String anchor;
//        private List<String> schemes;
        parcel.writeList(getVariables());
        parcel.writeString(getRegex().toString());
        parcel.writeString(getConstantUrl());
        parcel.writeInt(getType());
        parcel.writeString(getActivityClass().toString());
        parcel.writeList(getFragmentClasses());
        parcel.writeString(getAnchor());
        parcel.writeList(getSchemes());
    }

    private void readFromParcel(Parcel in) {
//        mObjList = (MyClass[]) in.readParcelableArray(
//                com.myApp.MyClass.class.getClassLoader()));
        ClassLoader classLoader = CompiledRoute.class.getClassLoader();
//        setVariables();
        List<String> variables = new ArrayList<>();
        in.readList(variables, ArrayList.class.getClassLoader());
        setRegex(Pattern.compile(in.readString()));
        setConstantUrl(in.readString());
        setType(in.readInt());
//        try {
//            setActivityClass(Class.forName(in.readString()));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}