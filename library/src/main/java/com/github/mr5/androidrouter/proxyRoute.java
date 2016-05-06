package com.github.mr5.androidrouter;

import android.os.Bundle;

public interface proxyRoute {
    /**
     * @param bundle        Android bundle
     * @param compiledRoute CompiledRoute
     * @return Return false when mismatching
     */
    public boolean routeDispatch(Router router, Bundle bundle, CompiledRoute compiledRoute);
}
