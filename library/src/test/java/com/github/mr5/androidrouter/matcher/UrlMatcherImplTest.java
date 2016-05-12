package com.github.mr5.androidrouter.matcher;

import com.github.mr5.androidrouter.BuildConfig;
import com.github.mr5.androidrouter.CompiledRoute;
import com.google.code.regexp.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UrlMatcherImplTest {
    @Test
    public void testMatch() {
        UrlMatcherImpl urlMatcher = new UrlMatcherImpl();

        CompiledRoute compiledRoute = new CompiledRoute()
                .setRegex(Pattern.compile("github\\.com/(?<vendor>[\\w\\d-]+)/(?<repository>[\\w\\d-]+)"))
                .setVariables(Arrays.asList("vendor", "repository"));
        urlMatcher.addCompiledRoute(compiledRoute);


        CompiledRoute compiledRoute1 = new CompiledRoute()
                .setConstantUrl("github.com/mr5");
        urlMatcher.addCompiledRoute(compiledRoute1);


        CompiledRoute compiledRoute3 = new CompiledRoute()
                .setRegex(Pattern.compile("github\\.com/(\\w+)"));
        urlMatcher.addCompiledRoute(compiledRoute3);


        CompiledRoute compiledRoute4 = new CompiledRoute()
                .setRegex(Pattern.compile("github\\.com/(\\w+)")).setAnchor("comments");
        urlMatcher.addCompiledRoute(compiledRoute4);

        CompiledRoute compiledRoute5 = new CompiledRoute();
        compiledRoute5.setConstantUrl("github.com/mr5");
        urlMatcher.addCompiledRoute(compiledRoute5);


        Request request = urlMatcher.match("https://github.com/mr5/android-router");
        assertEquals("-> assert specific route matched", compiledRoute, request.getCompiledRoute());
        assertNull("-> assert no query", request.getQueryVariables());
        assertNotNull("-> assert has path variables", request.getPathVariables());
        assertEquals("-> assert path variable `vendor`=mr5", "mr5", request.getPathVariables().get("vendor"));
        assertEquals("-> assert path variable `repository`=android-router", "android-router", request.getPathVariables().get("repository"));

        Request request1 = urlMatcher.match("https://github.com/mr5");
        assertNotSame("-> assert specific route overrided", compiledRoute1, request1.getCompiledRoute());
        assertNull("-> assert has no path variables", request1.getPathVariables());
        Request request2 = urlMatcher.match("https://github.com/mr5?username=xxxx&foo=bar");
        assertEquals("-> assert query `username`=xxxx", "xxxx", request2.getQueryVariables().get("username"));
        assertEquals("-> assert query `foo`=bar", "bar", request2.getQueryVariables().get("foo"));

        assertEquals("-> assert specific route with anchor matched", compiledRoute3, urlMatcher.match("https://github.com/hama").getCompiledRoute());
        assertEquals("-> assert specific route with anchor matched", compiledRoute4, urlMatcher.match("https://github.com/hama#comments").getCompiledRoute());
        assertEquals("-> assert specific route with anchor and query matched", compiledRoute4, urlMatcher.match("https://github.com/hama?foo=bar#comments").getCompiledRoute());

        CompiledRoute compiledRoute6 = new CompiledRoute()
                .setConstantUrl("github.com/mr5#wiki");
        urlMatcher.addCompiledRoute(compiledRoute6);
        assertEquals("-> test override", compiledRoute6, urlMatcher.match("https://github.com/mr5#wiki").getCompiledRoute());

        assertNull("-> test mismatched", urlMatcher.match("https://github.com/d/d/d").getCompiledRoute());
    }

    @Test
    public void testSplitQuery() {
        Map<String, String> query = UrlMatcherImpl.splitQuery("foo=bar");
        assertEquals("-> assert query string parser with 1 param", 1, query.size());
        assertEquals("-> assert query string parser with 1 param", "bar", query.get("foo"));

        Map<String, String> query1 = UrlMatcherImpl.splitQuery("foo=bar&foo2=bar2&foo3=bar3");
        assertEquals("-> assert query string parser with 3 params", 3, query1.size());
        assertEquals("-> assert query string parser with 3 param", "bar", query1.get("foo"));
        assertEquals("-> assert query string parser with 3 param", "bar2", query1.get("foo2"));
        assertEquals("-> assert query string parser with 3 param", "bar3", query1.get("foo3"));

        Map<String, String> query2 = UrlMatcherImpl.splitQuery("foo=bar&foo2=bar2&chinese=%e4%b8%ad%e6%96%87333%e6%b5%8b%e8%af%95");
        assertEquals("-> assert query string parser with 3 params", 3, query2.size());
        assertEquals("-> assert query string parser with 3 param", "bar", query2.get("foo"));
        assertEquals("-> assert query string parser with 3 param", "bar2", query2.get("foo2"));
        assertEquals("-> assert query string parser with 3 param", "中文333测试", query2.get("chinese"));
    }
}