package com.github.mr5.androidrouter.matcher;


import com.github.mr5.androidrouter.Request;
import com.github.mr5.androidrouter.Router;

public interface MismatchedHandler {
    public void run(Request request, Router router);
}