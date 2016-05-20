## Android router

[![Build Status](https://travis-ci.org/mr5/android-router.svg)](https://travis-ci.org/mr5/android-router)
[![Coveralls](https://coveralls.io/repos/github/mr5/android-router/badge.svg?branch=master
)](https://coveralls.io/github/mr5/android-router)

[中文文档](README_zh.md)
## Getting started
To add a dependency using Gradle:
```groovy
compile 'com.github.mr5:android-router:0.1.1-SNAPSHOT'

```
## Initialization
```java
import com.github.mr5.androidrouter.Router;
import static com.github.mr5.androidrouter.Route.route;

public class Application extends android.app.Application {

    public void onCreate() {
        super.onCreate();
        // You can get the shared instance of router via static method `getShared`, after `asShared` method called on a instance of `Router`.
        Router router = new Router(getApplicationContext()).asShared();
	}
}
```

## Route definition.

Route without variables：

```java
Router.getShared().add(new Route("github.com/site/terms", SiteTermsActivity.class));
```

Use `{}` for variables：

```java
Router.getShared().add(new Route("github.com/{vendor}/{repository}", RespositoryActivity.class));

Router.getShared().add(new Route("{vendor}.github.io/", PagesActivity.class));

// You can get variables from `Bundle` that in intent.

```

Assert variable pattern with regex:

```java
Route route = new Route("github.com/{vendor}/{repository}")
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+");
	
Router.getShared().add(route);

```

Anchor matching:

```java
Route route = new Route("github.com/{vendor}/{repository}")
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+")
	.anchor("comments");
Router.getShared().add(route);
	
// `github.com/mr5/android-router#comments` will be matched.
```

 Invocation chaining:
 

```java
import static com.github.mr5.androidrouter.Route.route;

...

route("github.com/{vendor}", VendorActivity.class)
	.bind("vendor", "[\\w-]+")
	.addTo(Router.getShared()); // Same as .addTo();
	
route("github.com/{vendor}/{repository}", RepositoryActivity.class)
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+")
	.addTo(Router.getShared());
```
## Open specific url:

```java
// `this` is current Context
Router.getShared().open("https://github.com", this);
// Open for result, like `startActivityForResult`,  `this` is current Context
Router.getShared().openForResult("https://github.com", this, YOUR_REQUEST_CODE);
// open in browser
Router.getShared().openExternal("https://github.com");
```


## Priority of matching

### Matching sequence：

* Routes without variables;
* Routes with variables;
* Matching schemes;
* Start from `3` when url has no anchor.
* Fist added first matching when conflicts produced;

priority:

1. `SCHEME_ANY` matching is deny， certainly anchor matching ;
1. `SCHEME_ANY` matching is allow，certainly anchor matching;
1. `SCHEME_ANY` matching is deny，matching routes that no anchor asserted；
1. `SCHEME_ANY` matching is allow，matching routes that no anchor asserted；


### Conflicts

> The top 2 routes in following list include variables , and have same structure, they will produce conflicts. Matching program will be finished when the first route matched,  so the second route will never be matched.

1. github.com/site/{site}
2. github.com/{vendor}/{repository}
3. github.com/site/privacy

`github.com/site/{site}` will be matched when a url like `https://github.com/site/terms` given, because of it's added earlier than the second one. So priority of conflicting routes must be controlled by yourself.
`github.com/site/privacy` will be matched to route `github.com/site/privacy` firstly, because of it's a certainly route that without variables. 
 
## LICENSE
MIT