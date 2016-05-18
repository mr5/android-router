package com.github.mr5.androidrouter;

public interface RouterProxy {

    public RouterProxy proxy(Request request, String nextClassName);
}