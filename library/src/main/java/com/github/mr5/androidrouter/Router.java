package com.github.mr5.androidrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.mr5.androidrouter.matcher.UrlNotMatchedHandler;

import java.util.HashMap;
import java.util.Map;

public class Router {
    protected static Router sharedInstance;
    protected Map<String, Route> routeMap = new HashMap<>();
    protected UrlNotMatchedHandler urlNotMatchedHandler;

    public void setUrlNotMatchedHandler(UrlNotMatchedHandler urlNotMatchedHandler) {
        this.urlNotMatchedHandler = urlNotMatchedHandler;
    }

    public void register(String urlPattern, Activity activity) {

    }

    public static Router getShared() {
        if (sharedInstance == null) {
            sharedInstance = new Router();
        }
        return sharedInstance;
    }

    public void open(String url) {

    }

    public void open(String url, Context context) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("_router_referer_class", context.getClass().getName());
        bundle.putString("_router_referer_url", context.getClass().getName());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void openInWebView(String url) {

    }

    public void openExternal(String url) {

    }

    /**
     * Add route with activity as agent, it is useful for route to fragments.
     *
     * @param url           URL string.
     * @param activityClass Agent activity class.
     * @param fragments     All passing fragments, and last fragment is the destination.
     * @param <A>           Activity with `proxyRoute` implementation.
     */
    public <A extends Activity & proxyRoute> void add(String url, Class<A> activityClass, Fragment... fragments) {
    }

    public void openFromFragment(
            String url,
            Activity fromActivity,
            Fragment fromFragment,
            int requestCode
    ) {
        openFromFragment(url, fromActivity, fromFragment, requestCode);
    }

    public void openFromFragment(
            String url,
            Activity fromActivity,
            Fragment fromFragment,
            int requestCode,
            Bundle options
    ) {

    }
}
