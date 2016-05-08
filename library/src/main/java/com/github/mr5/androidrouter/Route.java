package com.github.mr5.androidrouter;

import android.app.Activity;
import android.app.Fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

public class Route {
    enum TYPE {
        DIRECTLY, PROXY
    }

    private String path = "/";

    private HashMap<String, String> requirements = new HashMap<>();

    private Class<Activity> activityClass;

    private Class proxyActivityClass;
    private String anchor;
    private LinkedList<String> passingFragmentClassNames;
    private Map<String, String> queries = new HashMap<>();
    private Pattern regex;
    private String constantUrl;

    public Route(String path, Class<Activity> activityClass) {
        this.path = path;
        this.activityClass = activityClass;
    }

    public String getAnchor() {
        return anchor;
    }

    public Class getProxyActivityClass() {
        return proxyActivityClass;
    }

    public Class<Activity> getActivityClass() {
        return activityClass;
    }

    public LinkedList<String> getPassingFragmentClassNames() {
        return passingFragmentClassNames;
    }

    public HashMap<String, String> getRequirements() {
        return requirements;
    }

    public String getPath() {
        return path;
    }


    /**
     * Repress var with given regex.
     *
     * @param varName Var name
     * @param regex   Regex pattern string
     * @return this
     */
    public Route bind(String varName, String regex) {
        requirements.put(varName, sanitizeRequirement(varName, regex));
        return this;
    }

    public <A extends Activity & RouterProxy, F extends Fragment & RouterProxy> Route(
            String path,
            Class<A> proxyActivityClass,
            Class<F>... fragmentClasses) {
        this.path = path;
        this.proxyActivityClass = proxyActivityClass;
        passingFragmentClassNames = new LinkedList<>();
        if (fragmentClasses != null && fragmentClasses.length > 0) {

            for (Class<F> fragmentClass : fragmentClasses) {
                passingFragmentClassNames.add(fragmentClass.getName());
            }
        }

    }


    public Map<String, String> getQueries() {
        return queries;
    }

    /**
     * Set an anchor determine.
     *
     * @param anchor Anchor determine.
     * @return Just for call chaining.
     */
    public Route setAnchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * Add a query determine.
     *
     * @param name  Query name.
     * @param value Query value.
     * @return Just for call chaining.
     */
    public Route addQuery(String name, String value) {
        queries.put(name, value);
        return this;
    }


    private String sanitizeRequirement(String key, String regex) {
        if (regex == null || "".equals(regex)) {
            throw new IllegalArgumentException(
                    String.format("Routing requirement for \"%s\" cannot be empty.", key)
            );
        }
        if (regex.startsWith("^")) {
            if (regex.length() > 1) {
                regex = regex.substring(1);
            } else {
                regex = "";
            }
        }
        if (regex.endsWith("$")) {
            if (regex.length() > 1) {
                regex = regex.substring(regex.length() - 1);
            } else {
                regex = "";
            }
        }

        if ("".equals(regex)) {
            throw new IllegalArgumentException(
                    String.format("Routing requirement for \"%s\" cannot be empty.", key)
            );
        }
        return regex;
    }
}