package com.github.mr5.androidrouter;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wscn on 16/4/28.
 */
public class CompiledRoute implements Serializable {
    private String[] variables;
    private String[] tokens;
    private Pattern regex;
    private String[] pathVariables;
    private String[] hostVariables;
    private Pattern hostRegex;
    private String[] hostTokens;

    /**
     * @param regex         The regular expression to use to match this route
     * @param tokens        An array of tokens to use to generate URL for this route
     * @param pathVariables An array of path variables
     * @param hostRegex     Host regex
     * @param hostTokens    Host tokens
     * @param hostVariables An array of host variables
     * @param variables     An array of variables (variables defined in the path and in the host patterns)
     */
    public CompiledRoute(
            Pattern regex, String[] tokens, String[] pathVariables, Pattern hostRegex,
            String[] hostTokens, String[] hostVariables, String[] variables
    ) {
        this.regex = regex;
        this.tokens = tokens;
        this.pathVariables = pathVariables;
        this.hostRegex = hostRegex;
        this.hostTokens = hostTokens;
        this.hostVariables = hostVariables;
        this.variables = variables;
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    public String[] getVariables() {
        return variables;
    }

    public String[] getTokens() {
        return tokens;
    }

    public Pattern getRegex() {
        return regex;
    }

    public String[] getPathVariables() {
        return pathVariables;
    }

    public String[] getHostVariables() {
        return hostVariables;
    }

    public Pattern getHostRegex() {
        return hostRegex;
    }

    public String[] getHostTokens() {
        return hostTokens;
    }
}