package com.github.mr5.samples.androidrouter;

import android.app.Activity;

import com.github.mr5.androidrouter.Router;

import static com.github.mr5.androidrouter.Route.route;

public class Application extends android.app.Application {
    private static final String vendorPattern = "[\\w-]+";
    private static final String repositoryPattern = "[\\w-]+";

    public void onCreate() {
        super.onCreate();
        Router router = new Router(getApplicationContext()).asShared();

        router.add("github.com/", MainActivity.class);

        route("github.com/{vendor}", VendorActivity.class).bind("vendor", vendorPattern).addTo(router);
        route("github.com/{vendor}/{repository}", RepositoryActivity.class)
                .bind("vendor", vendorPattern).bind("repository", repositoryPattern).addTo(router);
    }
}