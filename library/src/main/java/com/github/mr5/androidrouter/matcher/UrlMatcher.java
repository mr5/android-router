package com.github.mr5.androidrouter.matcher;

import com.github.mr5.androidrouter.CompiledRoute;
import com.github.mr5.androidrouter.Request;
import com.github.mr5.androidrouter.exception.URLNotMatchedException;

/**
 * Created by wscn on 16/4/29.
 */
public interface UrlMatcher {

    public void addCompiledRoute(CompiledRoute route);

    /**
     * Tries to match a URL path with a set of routes.
     * <p>
     * If the matcher can not find information, it must throw one of the exceptions documented
     * below.
     * </p>
     *
     * @return Compiled route.
     * @throws URLNotMatchedException If the url could not be matched.
     */
    public Request match(String url) throws URLNotMatchedException;
}