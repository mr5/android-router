package com.github.mr5.androidrouter;

public interface RouterProxy {
    /**
     * @param request
     * @return
     */
    public RouterProxy proxy(Request request, String nextClassName);
}