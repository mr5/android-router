package com.github.mr5.androidrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.mr5.androidrouter.matcher.Request;
import com.github.mr5.androidrouter.matcher.MismatchedHandler;
import com.github.mr5.androidrouter.matcher.UrlMatcher;

import java.util.HashMap;
import java.util.Map;


public class Router {
    protected static Router sharedInstance;
    protected Map<String, Route> routeMap = new HashMap<>();
    protected RouteCompiler routeCompiler;
    protected UrlMatcher urlMatcher;
    protected MismatchedHandler mismatchedHandler;
    protected Context context;
    public static final String BUNDLE_KEY_REQUEST = "_router_request";

    public Router(UrlMatcher urlMatcher, RouteCompiler routeCompiler) {
        this.urlMatcher = urlMatcher;
        this.routeCompiler = routeCompiler;
    }

    public Router setContext(Context context) {
        this.context = context;

        return this;
    }

    public Context getContext() {
        return context;
    }

    public Router mismatched(MismatchedHandler mismatchedHandler) {
        this.mismatchedHandler = mismatchedHandler;
        return this;
    }

    public Router add(Route route) {
        urlMatcher.addCompiledRoute(routeCompiler.compile(route));
        return this;
    }

    public Router asShared() {
        sharedInstance = this;
        return this;
    }

    public static Router getShared() {
        return sharedInstance;
    }

    public void open(String url) {
        open(url, new Bundle());
    }

    public void open(String url, Context context) {
        open(url, context, new Bundle());
    }

    public void open(String url, Bundle bundle) {
        open(url, context, bundle);
    }

    public void open(String url, Context context, Bundle bundle) {
        Request request = urlMatcher.match(url);
        if (request.getCompiledRoute() == null) {
            if (mismatchedHandler != null) {
                mismatchedHandler.run(request, this);
            }
            return;
        }
        Intent intent = new Intent(context, request.getCompiledRoute().getActivityClass());
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (context == null) {
            context = this.context;
        }
        if (request.getQueryVariables() != null) {
            for (String queryKey : request.getQueryVariables().keySet()) {
                bundle.putString(queryKey, request.getQueryVariables().get(queryKey));
            }
        }
        if (request.getPathVariables() != null) {
            for (String queryKey : request.getPathVariables().keySet()) {
                bundle.putString(queryKey, request.getPathVariables().get(queryKey));
            }
        }
        request.setRefererClass(context.getClass().getName());
        bundle.putParcelable(BUNDLE_KEY_REQUEST, request);
        intent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }
}
