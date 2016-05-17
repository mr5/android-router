package com.github.mr5.androidrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.mr5.androidrouter.matcher.MismatchedHandler;
import com.github.mr5.androidrouter.matcher.UrlMatcher;
import com.github.mr5.androidrouter.matcher.UrlMatcherImpl;

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

    public Router(UrlMatcher urlMatcher, RouteCompiler routeCompiler, Context context) {
        this.urlMatcher = urlMatcher;
        this.routeCompiler = routeCompiler;
    }

    public Router(Context context) {
        this.context = context;
        urlMatcher = new UrlMatcherImpl();
        routeCompiler = new RouteCompilerImpl();
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

    public Router add(String path, Class<? extends Activity> activityClass) {
        add(new Route(path, activityClass));

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

    public void openForResult(String url, int requestCode, Activity sourceActivity) {
        openForResult(url, requestCode, sourceActivity, new Bundle());
    }

    public void openForResult(String url, int requestCode, Activity sourceActivity, Bundle bundle) {
        Intent intent = getIntent(url, bundle, sourceActivity);
        if (intent != null) {
            sourceActivity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Open url in browser.
     *
     * @param url
     */
    public void openExternal(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void open(String url, Context context) {
        open(url, context, new Bundle());
    }

    public void open(String url, Bundle bundle) {
        open(url, context, bundle);
    }

    protected Intent getIntent(String url, Bundle bundle, Context context) {
        Request request = urlMatcher.match(url);
        if (request.getCompiledRoute() == null) {
            if (mismatchedHandler != null) {
                mismatchedHandler.run(request, this);
            }
            return null;
        }
        CompiledRoute compiledRoute = request.getCompiledRoute();

        String activityClassName = compiledRoute.getActivityClass();
        Class activityClass = null;
        try {
            activityClass = Class.forName(activityClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (activityClass == null) {
            return null;
        }
        Intent intent = new Intent(context, activityClass);
        if (bundle == null) {
            bundle = new Bundle();
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
        return intent;
    }

    public void open(String url, Context context, Bundle bundle) {
        if (context == null) {
            context = this.context;
        }
        Intent intent = getIntent(url, bundle, context);

        if (intent != null) {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    public Request getRequest(Bundle bundle) {
        return bundle.getParcelable(BUNDLE_KEY_REQUEST);
    }

    public void proxy(Bundle bundle, RouterProxy routerProxy) {
        proxy(getRequest(bundle), routerProxy);
    }

    public void proxy(Request request, RouterProxy routerProxy) {
        if (routerProxy == null) {
            return;
        }
        String nextClassName = request.getCompiledRoute().getNextFragment(routerProxy.getClass().getName());
        routerProxy.proxy(request, nextClassName);
    }
}