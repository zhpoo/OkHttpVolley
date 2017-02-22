## This is easy to request a JSON result from your server.
#### also you can use it to request many other http result by yourself , like upload files.

## Usage

1.First init OkVolley in your application's onCreate() (or other place before you need use it)
and set some default config.

    OkVolley.init(this);
    HttpHelper.setHostUrl(getMyHost());                 // like : http://www.kfw.net
    HttpHelper.setCookie(getMyCookie());                // your cookie. can be empty.
    HttpHelper.setUserAgent(getMyUserAgent());          // your user agent. can be empty.
    HttpHelper.setDefaultRetryPolicy(2000, 2, 0.5f);    // global retry policy.

2.That's all. now you can use it like this:

    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    params.put("password", password);
    OkVolley.post("/user/login", params, listener);

3.More detail please see demo [MainActivity](https://github.com/zozx/OkHttpVolley/blob/master/app/src/main/java/com/zozx/okvolley/demo/MainActivity.java).
