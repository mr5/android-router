package com.github.mr5.androidrouter;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.provider.Settings;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
        assertEquals("hello", true, false);
        System.out.println("hello");
    }
}