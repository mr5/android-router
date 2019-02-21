## Android router

[![Build Status](https://travis-ci.org/mr5/android-router.svg)](https://travis-ci.org/mr5/android-router)
[![Coveralls](https://coveralls.io/repos/github/mr5/android-router/badge.svg?branch=master
)](https://coveralls.io/github/mr5/android-router)
## Getting started
添加依赖
```groovy
compile 'com.github.mr5:android-router:0.1.3'

```
## 初始化路由
```java
import com.github.mr5.androidrouter.Router;
import static com.github.mr5.androidrouter.Route.route;

public class Application extends android.app.Application {

    public void onCreate() {
        super.onCreate();
        // 在 Router 实例上调用 asShared() 方法，后续可以通过静态方法 getShared() 得到。
        Router router = new Router(getApplicationContext()).asShared();
	}
}
```

## 声明路由

声明无变量路由：

```java
Router.getShared().add(new Route("github.com/site/terms", SiteTermsActivity.class));
```

有变量时，使用 `{}` 包裹变量名：


```java
Router.getShared().add(new Route("github.com/{vendor}/{repository}", RespositoryActivity.class));

Router.getShared().add(new Route("{vendor}.github.io/", PagesActivity.class));

```

使用正则表达式可限定变量的内容格式：

```java
Route route = new Route("github.com/{vendor}/{repository}")
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+");
	
Router.getShared().add(route);

```

还可以声明 anchor（锚点）匹配：


```java
Route route = new Route("github.com/{vendor}/{repository}")
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+")
	.anchor("comments");
Router.getShared().add(route);
	
// 将匹配 github.com/mr5/android-router#comments	
```

链式调用
`route` 方法通过 `import static com.github.mr5.androidrouter.Route.route` 得到，类似 Builder 的概念，不过最后需要调用 addTo 来添加到指定 Router，`addTo` 方法不传参数则默认添加到 `Router.getShared()`

```java
route("github.com/{vendor}", VendorActivity.class)
	.bind("vendor", "[\\w-]+")
	.addTo(Router.getShared()); // equals to .addTo();
route("github.com/{vendor}/{repository}", RepositoryActivity.class)
	.bind("vendor", "[\\w-]+")
	.bind("repository", "[\\w-]+")
	.addTo(Router.getShared());
```
## 打开网址

```java
// `this` is current Context
Router.getShared().open("https://github.com", this);
// Open for result, like `startActivityForResult`,  `this` is current Context
Router.getShared().openForResult("https://github.com", this, YOUR_REQUEST_CODE);
// open in browser
Router.getShared().openExternal("https://github.com");

```

## 匹配优先级规则

### 匹配顺序：
* 优先匹配无变量路由，再匹配有变量路由；
* host + path 存在冲突时，优先解决冲突再匹配 scheme；
* 如待匹配 URL 无 anchor 则直接从 3 开始匹配；
* 带变量的路由匹配存在冲突时，先定义的先匹配；

匹配优先级

1. 不允许匹配 `SCHEME_ANY`， anchor 完全匹配; 
1. 允许匹配 `SCHEME_ANY`， anchor 完全匹配；
1. 不允许匹配 `SCHEME_ANY`，匹配不带 anchor 限定的路由；
1. 允许匹配 `SCHEME_ANY`， 匹配不带 anchor 限定的路由；


### 路由冲突情况

> 下面列表中前两个路由都存在变量，并且结构一致，在匹配时会产生冲突，匹配执行流第一次完整匹配后即结束流程，因此第二个路由规则会触及不到。

1. github.com/site/{site}
2. github.com/{vendor}/{repository}
3. github.com/site/privacy

匹配 `github.com/site/terms` 这个 URL 时，按照上述列表的顺序会优先匹配到 `github.com/site/{site}`，因为它声明得更早，因此此类路由需要由你控制它们的匹配优先级。而匹配 `github.com/site/privacy` 这个 URL 时，会匹配到路由规则 `github.com/site/privacy` ，因为它没有包含任何变量，优先级最高，与定义顺序无关，不过此处的顺序此处仅为演示优先级，实际编码时，建议仍然按照匹配优先级来定义，即把它放到最顶部，以使代码更加清晰可读。


## ROADMAP
- [ ] 优先匹配变量更多的路由；
- [ ] host 中的变量仅匹配单段；
- [ ] 优先匹配变量更多的路由，并且考虑变量分布在 host 和 path 段的情况；
- [ ] 支持 query 段匹配
 
## LICENSE
MIT
