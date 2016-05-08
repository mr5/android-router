package com.github.mr5.androidrouter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicInteger;

public class CompiledRoute {
    private List<String> variables;
    private Pattern regex;
    private String constantUrl;
    private Route.TYPE type;
    private Class<Activity> activityClass;
    private LinkedList<Class<Fragment>> fragmentClasses;
    private Map<String, String> queryDetermines;
    private String anchor;

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Map<String, String> getQueryDetermines() {
        return queryDetermines;
    }

    public void setQueryDetermines(Map<String, String> queryDetermines) {
        this.queryDetermines = queryDetermines;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public Pattern getRegex() {
        return regex;
    }

    public void setRegex(Pattern regex) {
        this.regex = regex;
    }

    public String getConstantUrl() {
        return constantUrl;
    }

    public void setConstantUrl(String constantUrl) {
        this.constantUrl = constantUrl;
    }

    public Route.TYPE getType() {
        return type;
    }

    public void setType(Route.TYPE type) {
        this.type = type;
    }

    public Class<Activity> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public LinkedList<Class<Fragment>> getFragmentClasses() {
        return fragmentClasses;
    }

    public void setFragmentClasses(LinkedList<Class<Fragment>> fragmentClasses) {
        this.fragmentClasses = fragmentClasses;
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
}