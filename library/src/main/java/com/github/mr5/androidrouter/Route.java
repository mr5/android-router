package com.github.mr5.androidrouter;

import android.app.Activity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
    public static final int TYPE_DIRECTLY = 1;
    public static final int TYPE_PROXY = 2;
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String SCHEME_TEL = "tel";
    public static final String SCHEME_MAILTO = "mailto";
    public static final String SCHEME_ANY = "*";

    protected String path = "/";

    protected HashMap<String, String> requirements = new HashMap<>();

    protected Class<? extends Activity> activityClass;

    protected String anchor;
    protected List<Class<? extends RouterProxy>> middlewares;
    protected Map<String, String> queries = new HashMap<>();
    protected List<String> schemes;
    protected String proxyDestIdentify;
    protected int weight;

    public Route(String path, Class<? extends Activity> activityClass, String[] schemes) {
        this.path = path;
        this.activityClass = activityClass;
        this.schemes = Arrays.asList(schemes);
    }

    public Route(String path, Class<? extends Activity> activityClass) {
        this(path, activityClass, new String[]{SCHEME_ANY});
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public Route setSchemes(List<String> schemes) {
        this.schemes = schemes;

        return this;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setActivityClass(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
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

    public Route bindWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public Route(
            String path,
            Class<? extends Activity> proxyActivityClass,
            String destIdentify
    ) {
        this.path = path;
        this.activityClass = proxyActivityClass;
        this.proxyDestIdentify = destIdentify;
    }

    public Route(
            String path,
            Class<? extends Activity> proxyActivityClass,
            String proxyDestIdentify,
            Class<? extends RouterProxy>... middlewares) {
        this.path = path;
        this.activityClass = proxyActivityClass;
        this.middlewares = Arrays.asList(middlewares);
        this.proxyDestIdentify = proxyDestIdentify;
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
    public Route anchor(String anchor) {
        this.anchor = anchor;
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
                regex = regex.substring(0, regex.length() - 1);
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

    public static Route route(String path, Class<? extends Activity> activityClass) {
        return new Route(path, activityClass);
    }

    public void addTo(Router router) {
        router.add(this);
    }

    public void addTo() {
        Router.getShared().add(this);
    }

    public List<Class<? extends RouterProxy>> getMiddlewares() {
        return middlewares;
    }

    public void setMiddlewares(List<Class<? extends RouterProxy>> middlewares) {
        this.middlewares = middlewares;
    }

    public String getProxyDestIdentify() {
        return proxyDestIdentify;
    }

    public void setProxyDestIdentify(String proxyDestIdentify) {
        this.proxyDestIdentify = proxyDestIdentify;
    }

    public String toString() {
        return getPath();
    }
}