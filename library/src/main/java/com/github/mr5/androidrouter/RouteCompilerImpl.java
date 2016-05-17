package com.github.mr5.androidrouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.code.regexp.Pattern;
import com.google.code.regexp.Matcher;

public class RouteCompilerImpl implements RouteCompiler {

    public CompiledRoute compile(Route route) {
        CompiledRoute compiledRoute = new CompiledRoute();

        List<String> variables = detectVariables(route);
        compiledRoute.setVariables(variables);
        compiledRoute.setAnchor(route.getAnchor());
        compiledRoute.setType(Route.TYPE_DIRECTLY);
        compiledRoute.setActivityClass(route.getActivityClass().getName());
        if (variables == null || variables.size() < 1) {
            String constantUrl = route.getPath();
            if (route.getAnchor() != null) {
                constantUrl += "#" + route.getAnchor();
            }
            compiledRoute.setConstantUrl(constantUrl);
        } else {
            String regexString = compileRegex(route, variables);
            compiledRoute.setRegex(Pattern.compile(regexString));
        }
        if (route.getProxyDestIdentify() != null) {
            //compiledRoute
            compiledRoute.setType(Route.TYPE_PROXY);
        }
        if (route.getMiddlewares() != null
                && route.getMiddlewares().size() > 0) {
            ArrayList<String> fragmentClassNames = new ArrayList<>();
            for (Class clazz : route.getMiddlewares()) {
                fragmentClassNames.add(clazz.getName());
            }
            compiledRoute.setMiddlewares(fragmentClassNames);
            compiledRoute.setType(Route.TYPE_PROXY);
        }
        return compiledRoute;
    }


    public static List<String> detectVariables(Route route) {
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(route.getPath());
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }

    public static String compileRegex(Route route, List<String> variables) {
        HashMap<String, String> requirements = route.getRequirements();
        Iterator<String> variablesIterator = variables.iterator();
        String patternString = quote(route.getPath());
        while (variablesIterator.hasNext()) {
            String varName = variablesIterator.next();
            String regex = "[^/]+";
            if (requirements.containsKey(varName)) {
                regex = requirements.get(varName);
            }
            patternString = patternString.replaceFirst(
                    String.format("\\\\\\{%s\\\\\\}", varName),
                    java.util.regex.Matcher.quoteReplacement(String.format("(?<%s>%s)", varName, regex))
            );
        }

        // Remove literal pattern Strings (\Q and \E)
        //patternString = patternString.substring(2, patternString.length() - 2);
        return patternString;
    }

    public static String quote(String str) {
        return str.replaceAll("[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]", "\\\\$0");
    }
}