package com.github.mr5.androidrouter;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RouteTest {


    @Test(expected = IllegalArgumentException.class)
    public void testBind() {
        HashMap<String, String> expectedMap = new HashMap<>();
        expectedMap.put("vendor", "\\w+");
        assertEquals(
                expectedMap,
                new Route("github.com/{vendor}", Activity.class).bind("vendor", "^\\w+$").getRequirements()
        );

        assertEquals(
                expectedMap,
                new Route("github.com/{vendor}", Activity.class).bind("vendor", "^\\w+").getRequirements()
        );

        assertEquals(
                expectedMap,
                new Route("github.com/{vendor}", Activity.class).bind("vendor", "\\w+$").getRequirements()
        );

        // expected IllegalArgumentException
        new Route("github.com/{vendor}", Activity.class).bind("vendor", "");
    }
}