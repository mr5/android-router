package com.github.mr5.androidrouter;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.mr5.androidrouter.matcher.UrlToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteCompilerImpl {
    public static final String REGEX_DELIMITER = "#";
    /**
     * This string defines the characters that are automatically considered separators in front of
     * optional placeholders (with default and no static text following). Such a single separator
     * can be left out together with the optional placeholder from matching and generating URLs.
     */
    public static String SEPARATORS = "/,;.:-_~+*=@|";

    public static CompiledRoute compile(Route route) {
        String[] hostVariables = new String[]{};
        String[] variables = new String[]{};
        String[] hostTokens = new String[]{};
        String host = route.getHost();
        if (!"".equals(host)) {

        }

        return null;
    }

    public static void compilePattern(Route route, String patternString, boolean isHost) {
        Pattern pattern = Pattern.compile("\\{\\w+\\}");
        Matcher matcher = pattern.matcher(patternString);
        Pattern integerPattern = Pattern.compile("^\\d+");
        String defaultSeparator = isHost ? "." : "/";
        ArrayList<String> variables = new ArrayList<>();
        ArrayList<UrlToken> tokens = new ArrayList<>();
        while (matcher.find()) {
            // println(matcher.group());
            String varName = matcher.group(1);
            String precedingChar = patternString.substring(matcher.start() - 1, matcher.start());
            boolean isSeparator = !"".equals(precedingChar) && SEPARATORS.contains(precedingChar);
            if (integerPattern.matcher(varName).matches()) {
                throw new IllegalArgumentException(
                        String.format(
                                "Variable name \"%s\" cannot be numeric in route pattern \"%s\"" +
                                        ". Please use a different name.",
                                varName,
                                pattern
                        )
                );
            }

            if (variables.contains(varName)) {
                throw new IllegalArgumentException(
                        String.format(
                                "Route pattern \"%s\" cannot reference variable name \"%s\" more" +
                                        " than once.",
                                pattern,
                                varName
                        )
                );

            }

            if (isSeparator && matcher.start() > 1) {
                UrlToken token = new UrlToken();
                token.setType(UrlToken.TYPE.TEXT);
                token.setValue(patternString.substring(0, matcher.start() - 2));
                tokens.add(token);
            } else if (!isSeparator && matcher.start() > 0) {
                UrlToken token = new UrlToken();
                token.setType(UrlToken.TYPE.TEXT);
                token.setValue(patternString.substring(0, matcher.start() - 1));
                tokens.add(token);
            }
        }
    }

    public static String pregQuote(String str) {
        int a = "".getBytes().length;

        return str.replaceAll("[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]", "\\\\$0");
    }
}