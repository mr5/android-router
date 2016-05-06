package com.github.mr5.androidrouter.matcher;

/**
 * Created by wscn on 16/4/29.
 */
public class UrlToken {
    public enum TYPE {
        TEXT, VARIABLE
    }

    private UrlToken.TYPE type;

    private String value;

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
