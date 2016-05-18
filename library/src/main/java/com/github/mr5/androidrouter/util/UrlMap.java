package com.github.mr5.androidrouter.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class UrlMap {
    private Map<String, String> mapping;
    private static UrlMap shared;

    public UrlMap(Map<String, String> urlPrefixMapping) {
        this.mapping = urlPrefixMapping;
    }

    public UrlMap asShared() {
        shared = this;
        return this;
    }

    public static UrlMap getShared() {
        return shared;
    }

    /**
     * Resolve URL
     *
     * @param group URL group name, such as `main`/`api`
     * @param uri   URI without scheme and host, `group` param will be discard when scheme and host given.
     * @return Absolutely URL
     */
    public String resolve(String group, String uri) {
        return resolve(group, uri, "");
    }

    /**
     * Resolve URL
     *
     * @param group URL group name, such as `main`/`api`
     * @param uri   URI without scheme and host, `group` param will be discard when scheme and host given.
     * @param query Query map append to URL.
     * @return Absolutely URL
     */
    public String resolve(String group, String uri, Map<String, String> query) {

        String queryString = "";
        for (String key : query.keySet()) {
            try {
                queryString += key += "=" + URLEncoder.encode(query.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resolve(group, uri, queryString);
    }

    /**
     * Resolve URL
     *
     * @param group URL group name, such as `main`/`api`
     * @param uri   URI without scheme and host, `group` param will be discard when scheme and host given.
     * @param query Query string append to URL, like `param1=value1&amp;pamra2=value2`;
     * @return Absolutely URL
     */
    public String resolve(String group, String uri, String query) {
        String uriLowerCase = uri.toLowerCase();
        if (uriLowerCase.startsWith("http://") || uriLowerCase.startsWith("https://")) {
            return uri;
        }
        if (!mapping.containsKey(group) || mapping.get(group) == null) {
            return uri;
        }
        uri = rightTrim(mapping.get(group), "/") + "/" + leftTrim(uri, "/");
        if (query != null && query.length() > 0) {
            uri += uri.contains("?") ? "&" : "?";
            uri += query;
        }

        return uri;
    }

    protected String leftTrim(String s, String charsetList) {
        int i = 0;
        while (i < s.length() && charsetList.indexOf(s.charAt(i)) > -1) {
            i++;
        }
        return i >= s.length() ? "" : s.substring(i);
    }

    protected String trim(String s, String charsetList) {
        return rightTrim(leftTrim(s, charsetList), charsetList);
    }

    protected String rightTrim(String s, String charsetList) {
        int i = s.length() - 1;
        while (i > 0 && charsetList.indexOf(s.charAt(i)) > -1) {
            i--;
        }
        return i <= 0 ? "" : s.substring(0, i + 1);
    }
}
