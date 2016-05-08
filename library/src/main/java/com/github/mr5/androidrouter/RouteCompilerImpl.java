package com.github.mr5.androidrouter;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.mr5.androidrouter.matcher.UrlToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteCompilerImpl {

    public static CompiledRoute compile(Route route) {
        CompiledRoute compiledRoute = new CompiledRoute();

        List<String> variables = detectVariables(route);
        compiledRoute.setVariables(variables);
        compiledRoute.setQueryDetermines(route.getQueries());
        compiledRoute.setAnchor(route.getAnchor());

        if (variables == null || variables.size() < 1) {
            compiledRoute.setConstantUrl(route.getPath());
        } else {
            String regexString = compileRegex(route, variables);
            compiledRoute.setRegex(Pattern.compile(regexString));
        }
        if (route.getProxyActivityClass() != null) {
            if (route.getPassingFragmentClassNames() == null
                    || route.getPassingFragmentClassNames().size() < 1) {
                throw new IllegalArgumentException(String.format(
                        "Proxy activity must define passing fragments.(%s)",
                        route.getPath()
                ));
            }
            compiledRoute.setType(Route.TYPE.PROXY);
        }

        return compiledRoute;
    }


    private static List<String> detectVariables(Route route) {
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(route.getPath());
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }

    private static String compileRegex(Route route, List<String> variables) {
        HashMap<String, String> requirements = route.getRequirements();
        Iterator<String> variablesIterator = variables.iterator();
        String patternString = Pattern.quote(route.getPath());
        while (variablesIterator.hasNext()) {
            String varName = variablesIterator.next();
            String regex = "[^/]+";
            if (requirements.containsKey(varName)) {
                regex = requirements.get(varName);
            }
            patternString = patternString.replaceFirst(
                    String.format("\\{%s\\}", varName),
                    Matcher.quoteReplacement(String.format("(?<%s>%s)", varName, regex))
            );
        }

        // Remove literal pattern Strings (\Q and \E)
        patternString = patternString.substring(2, patternString.length() - 2);
        return patternString;
    }
}