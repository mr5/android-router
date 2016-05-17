package com.github.mr5.androidrouter;

import android.app.Fragment;
import android.os.Bundle;

import com.github.mr5.androidrouter.matcher.Request;

public interface RouterProxy {
    /**
     * @param request
     * @return
     */
    public RouterProxy proxy(Request request, String nextClassName);
}