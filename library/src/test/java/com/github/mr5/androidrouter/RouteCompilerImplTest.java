package com.github.mr5.androidrouter;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.code.regexp.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RouteCompilerImplTest {
    @Test
    public void testCompileRegex() {
        Route route = new Route("github.com/mr5/{repository}", Activity.class);
        List<String> variables = RouteCompilerImpl.detectVariables(route);
        String regex = RouteCompilerImpl.compileRegex(route, variables);
        assertEquals("-> assert regex equals", "github\\.com/mr5/(?<repository>[^/]+)", regex);
        route.bind("repository", "\\w+");
        regex = RouteCompilerImpl.compileRegex(route, variables);
        assertEquals("-> assert regex equals", "github\\.com/mr5/(?<repository>\\w+)", regex);
        route = new Route("github.com/{vendor}/{repository}", Activity.class);
        route.bind("vendor", "\\w+");
        route.bind("repository", "\\w+");
        variables = RouteCompilerImpl.detectVariables(route);
        regex = RouteCompilerImpl.compileRegex(route, variables);
        assertEquals("-> assert regex equals", "github\\.com/(?<vendor>\\w+)/(?<repository>\\w+)", regex);
    }

    @Test
    public void testDetectVariables() {
        assertEquals(
                "-> assert none variables",
                RouteCompilerImpl.detectVariables(
                        new Route("github.com/mr5/android-router", Activity.class)
                ).size(),
                0
        );
        assertEquals(
                "-> assert 1 variable",
                RouteCompilerImpl.detectVariables(
                        new Route("github.com/mr5/{repository}", Activity.class)
                ).size(),
                1
        );
        assertEquals(
                "-> assert 2 variables",
                RouteCompilerImpl.detectVariables(
                        new Route("github.com/{vendor}/{repository}", Activity.class)
                ).size(),
                2
        );
        assertEquals(
                "-> assert variable name detecting correctly.",
                RouteCompilerImpl.detectVariables(
                        new Route("github.com/{vendor}/{repository}", Activity.class)
                ),
                Arrays.asList("vendor", "repository")
        );
        assertEquals(
                "-> assert sub domain detecting correctly.",
                RouteCompilerImpl.detectVariables(
                        new Route("{vendor}.github.com/{page}", Activity.class)
                ),
                Arrays.asList("vendor", "page")
        );
        assertEquals(
                "-> assert route with format correctly.",
                RouteCompilerImpl.detectVariables(
                        new Route("{vendor}.github.com/{page}.{format}", Activity.class)
                ),
                Arrays.asList("vendor", "page", "format")
        );
    }


    @Test
    public void testParcel() {
        Parcel parcel = Parcel.obtain();

        CompiledRoute compiledRoute = new CompiledRoute();
        compiledRoute.setVariables(Arrays.asList("vendor", "repository"))
                .setRegex(Pattern.compile("hello"))
                .setConstantUrl("constantUrl")
                .setType(Route.TYPE_DIRECTLY)
                .setActivityClass(Activity.class.getName())
                .setProxyDestIdentify("ha")
                .setMiddlewares(Arrays.asList("a", "b"))
                .setAnchor("comments")
                .setSchemes(Arrays.asList("vendor", "repository"));
        compiledRoute.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        CompiledRoute compiledRouteFromParcel = new CompiledRoute(parcel);

        assertTrue("-> test parcelable", compiledRoute.equals(compiledRouteFromParcel));

        assertEquals("b", compiledRoute.getNextMiddleware("a"));
        assertNull(compiledRoute.getNextMiddleware("b"));
        assertNull(compiledRoute.getNextMiddleware("c"));
    }
}