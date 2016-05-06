package com.github.mr5.androidrouter;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.MainThread;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Route<P extends Activity & proxyRoute, F extends Fragment & proxyRoute> {
    enum TYPE {
        DIRECTLY, PROXY
    }

    private String path = "/";

    private String host = "";
    private String[] schemes;

    private String[] methods;

    private Map<String, String> defaults = new HashMap<>();

    private Map<String, String> requirements = new HashMap<>();

    private String[] options;

    private String compiled;
    private String[] condition;
    private Class<Activity> activityClass;

    private Class<P> proxyActivityClass;

    private LinkedList<Class<F>> passingFragments;


    public Route(String path, Class<Activity> activityClass) {
        this.path = path;
        this.defaults = defaults;

        this.activityClass = activityClass;
        try {
            URL url = new URL("http://qq.com");
            System.out.println(url.getProtocol());
            System.out.println(url.getHost());
            System.out.println(url.getPath());
            System.out.println(url.getQuery());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.bind("id", "\\d+").bind("xxx", "\\d+")
                .bind("xxx", "xxx")
                .bind("xxx", "xxxxddd").defaultValue("xxx", "1");
    }

    /**
     * Repress var with given regex.
     *
     * @param varName Var name
     * @param regex   Regex pattern string
     * @return this
     */
    public Route bind(String varName, String regex) {
        if (requirements.containsKey(varName)) {
            throw new IllegalStateException(
                    String.format("Key %s has been bound before.", varName)
            );
        }
        requirements.put(varName, regex);
        return this;
    }

    public Route defaultValue(String varName, String defaultValue) {
        defaults.put(varName, defaultValue);
        return this;
    }

    public Route(String path, Class<P> proxyActivityClass, Class<F>... fragmentClasses) {
        this.path = path;
        this.proxyActivityClass = proxyActivityClass;
        passingFragments = new LinkedList<>();
        passingFragments.addAll(Arrays.asList(fragmentClasses));
    }

    public Class<F> getNext(Class<F> relativeFragment) {
        if (passingFragments == null) {
            return null;
        }
        int relativeFragmentPosition = passingFragments.indexOf(relativeFragment);
        if (relativeFragmentPosition < 0) {
            return null;
        }
        return passingFragments.get(relativeFragmentPosition + 1);
    }

    public String getHost() {
        return host;
    }
}