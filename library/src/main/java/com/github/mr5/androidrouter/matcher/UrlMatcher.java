package com.github.mr5.androidrouter.matcher;

import com.github.mr5.androidrouter.CompiledRoute;
import com.github.mr5.androidrouter.Request;
import com.github.mr5.androidrouter.exception.URLNotMatchedException;

/**
 * Created by wscn on 16/4/29.
 */
public interface UrlMatcher {

    public void addCompiledRoute(CompiledRoute route);

    public Request match(String url);
}