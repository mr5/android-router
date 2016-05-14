package com.github.mr5.androidrouter.matcher;

import android.util.Log;

import com.github.mr5.androidrouter.CompiledRoute;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mr5.androidrouter.Route;
import com.google.code.regexp.Matcher;

public class UrlMatcherImpl implements UrlMatcher {
    protected List<CompiledRoute> compiledRoutes = new ArrayList<>();
    protected Map<String, CompiledRoute> constantsRoutes = new HashMap<>();


    @Override
    public void addCompiledRoute(CompiledRoute route) {
        if (route.getConstantUrl() != null) {
            if (route.getSchemes() == null) {
                constantsRoutes.put(Route.SCHEMA_ANY + "://" + route.getConstantUrl(), route);
                return;
            }

            for (String scheme : route.getSchemes()) {
                constantsRoutes.put(scheme + "://" + route.getConstantUrl(), route);
            }

        } else {
            compiledRoutes.add(route);
        }
    }


    public Request match(String urlStr) {
        URL url;
        Map<String, String> queryMap;
        Request request = new Request();
        String host;
        String urlStr4match = urlStr;
        try {
            url = new URL(urlStr4match);
            host = url.getPort() < 0 || url.getPort() == 80 || url.getPort() == 443
                    ? url.getHost() : url.getAuthority();
            urlStr4match = host + url.getPath();
            queryMap = splitQuery(url.getQuery());
        } catch (MalformedURLException e) {
            Log.e("router", e.getLocalizedMessage());
            return request;
        }
        request.setUrl(urlStr)
                .setHost(url.getHost())
                .setScheme(url.getProtocol())
                .setAnchor(url.getRef())
                .setHost(host)
                .setQueryVariables(queryMap);

        searchCertainRoute(url, urlStr4match, request);
        if (request.getCompiledRoute() != null) {
            return request;
        }
        searchRegexRoute(url, urlStr4match, request);
        if (request.getCompiledRoute() != null) {
            return request;
        }

        return request;
    }

    protected Map<String, String> parsePathVariables(CompiledRoute compiledRoute, Matcher matchedMatcher) {
        Map<String, String> pathVariables = null;
        if (compiledRoute.getVariables() != null) {
            pathVariables = new HashMap<>();
            for (String var : compiledRoute.getVariables()) {
                pathVariables.put(var, matchedMatcher.group(var));
            }
        }

        return pathVariables;
    }

    protected void searchRegexRoute(URL url, String urlStr4match, Request request) {
        String scheme = url.getProtocol();
        String anchor = url.getRef();
        List<CompiledRoute> preMatchedRouteList = new ArrayList<>();
        // Certain scheme and certain anchor matching.
        for (CompiledRoute compiledRoute : compiledRoutes) {
            Matcher matcher = compiledRoute.getRegex().matcher(urlStr4match);

            if (!matcher.matches()) {
                continue;
            }

            List<String> routeSchemes = compiledRoute.getSchemes();

            if (anchor != null) {
                if (routeSchemes != null && routeSchemes.contains(scheme)
                        && compiledRoute.getAnchor() != null
                        && !compiledRoute.getAnchor().equals(anchor)) {
                    request.setCompiledRoute(compiledRoute)
                            .setPathVariables(parsePathVariables(compiledRoute, matcher));
                    return;
                }
            } else {
                if (routeSchemes != null && routeSchemes.contains(scheme)
                        && compiledRoute.getAnchor() == null) {
                    request.setCompiledRoute(compiledRoute)
                            .setPathVariables(parsePathVariables(compiledRoute, matcher));
                    return;
                }
            }


            preMatchedRouteList.add(compiledRoute);

        }

        if (anchor != null) {
            // Certain anchor and wildcard scheme matching
            for (CompiledRoute compiledRoute : preMatchedRouteList) {
                Matcher matcher = compiledRoute.getRegex().matcher(urlStr4match);

                if (!matcher.matches()) {
                    continue;
                }
                List<String> routeSchemes = compiledRoute.getSchemes();
                if (routeSchemes == null) {
                    routeSchemes = Arrays.asList(Route.SCHEMA_ANY);
                }
                // Filter no anchor restricted route.
                if (compiledRoute.getAnchor() == null) {
                    continue;
                }
                // Certain anchor and matched
                if (anchor.equals(compiledRoute.getAnchor())
                        && routeSchemes.contains(scheme) || routeSchemes.contains(Route.SCHEMA_ANY)) {
                    request.setCompiledRoute(compiledRoute)
                            .setPathVariables(parsePathVariables(compiledRoute, matcher));
                    return;
                }
            }
        }

        // Discard anchor and certain scheme matching
        for (CompiledRoute compiledRoute : preMatchedRouteList) {
            Matcher matcher = compiledRoute.getRegex().matcher(urlStr4match);

            if (!matcher.matches()) {
                continue;
            }
            List<String> routeSchemes = compiledRoute.getSchemes();
            if (routeSchemes == null) {
                routeSchemes = Arrays.asList(Route.SCHEMA_ANY);
            }
            // Filter anchor restricted route.
            if (compiledRoute.getAnchor() != null) {
                continue;
            }


            if (routeSchemes.contains(scheme)) {
                request.setCompiledRoute(compiledRoute)
                        .setPathVariables(parsePathVariables(compiledRoute, matcher));
                return;
            }
        }

        // Discard anchor and wildcard scheme matching
        for (CompiledRoute compiledRoute : preMatchedRouteList) {
            Matcher matcher = compiledRoute.getRegex().matcher(urlStr4match);

            if (!matcher.matches()) {
                continue;
            }
            List<String> routeSchemes = compiledRoute.getSchemes();
            if (routeSchemes == null) {
                routeSchemes = Arrays.asList(Route.SCHEMA_ANY);
            }
            // Filter anchor restricted route.
            if (compiledRoute.getAnchor() != null) {
                continue;
            }


            if (routeSchemes.contains(scheme) || routeSchemes.contains(Route.SCHEMA_ANY)) {
                request.setCompiledRoute(compiledRoute)
                        .setPathVariables(parsePathVariables(compiledRoute, matcher));
                return;
            }
        }
    }

    protected void searchCertainRoute(URL url, String urlStr4match, Request request) {
        String anchor = url.getRef();
        String searchKey;
        if (anchor != null) {
            searchKey = url.getProtocol() + "://" + urlStr4match + "#" + anchor;
            if (constantsRoutes.containsKey(searchKey)) {
                request.setCompiledRoute(constantsRoutes.get(searchKey));
                return;
            }
            searchKey = Route.SCHEMA_ANY + "://" + urlStr4match + "#" + anchor;
            if (constantsRoutes.containsKey(searchKey)) {
                request.setCompiledRoute(constantsRoutes.get(searchKey));
                return;
            }
        }
        searchKey = url.getProtocol() + "://" + urlStr4match;
        if (constantsRoutes.containsKey(searchKey)) {
            request.setCompiledRoute(constantsRoutes.get(searchKey));
            return;
        }
        searchKey = Route.SCHEMA_ANY + "://" + urlStr4match;
        if (constantsRoutes.containsKey(searchKey)) {
            request.setCompiledRoute(constantsRoutes.get(searchKey));
            return;
        }

    }

    public static Map<String, String> splitQuery(String query) {
        if (query == null) {
            return null;
        }
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                value = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            map.put(name, value);
        }
        return map;
    }
}
