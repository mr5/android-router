package com.github.mr5.androidrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.mr5.androidrouter.activity.VendorActivity;
import com.github.mr5.androidrouter.matcher.MismatchedHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RouterTest {
    private Router router;
    private Context context;
    private boolean mismatched = false;

    //private
    @Before
    public void setUp() {
        context = mock(Context.class);
        router = new Router(context);
    }

    @Test
    public void testMismatched() throws InterruptedException {
        router.add(new Route("github.com/test", Activity.class));
        router.mismatched(new MismatchedHandler() {
            @Override
            public void run(Request request, Router router) {
                mismatched = true;
            }
        });
        router.open("https://github.com/test");
        assertFalse(mismatched);
        router.open("https://github.com/test?from=hello");
        assertFalse(mismatched);
        router.open("https://stackoverflow.com/test");
        assertTrue(mismatched);
    }

    @Test
    public void testShared() {
        assertNull(Router.getShared());
        Router _router = new Router(mock(Context.class));
        _router.asShared();
        assertEquals(_router, Router.getShared());
    }

    @Test
    public void testOpen() {
        //verify(context);
        router.add(new Route("github.com/{vendor}", VendorActivity.class));

        router.open("https://github.com/mr5");
    }

    @Test
    public void testGetIntent() {
        // get variable values and flags testing.
        router.add(new Route("github.com/{vendor}", VendorActivity.class));
        Bundle bundle = new Bundle();
        Intent intent = router.getIntent("https://github.com/mr5?q=haha", bundle, context);
        ;
        assertEquals(
                Intent.FLAG_ACTIVITY_NEW_TASK,
                intent.getFlags()
        );
        assertEquals("mr5", bundle.getString("vendor"));
        assertEquals("haha", bundle.getString("q"));
        assertEquals(bundle, intent.getExtras());

        // variables name conflicted testing.
        Bundle bundle4Variables = new Bundle();
        router.getIntent("https://github.com/mr5?q=haha&vendor=heihei", bundle4Variables, context);
        assertEquals("mr5", bundle4Variables.get("vendor"));

        assertEquals(
                0,
                router.getIntent(
                        "https://github.com/mr5?q=haha&vendor=heihei",
                        new Bundle(),
                        new Activity()).getFlags()
        );
    }
}
