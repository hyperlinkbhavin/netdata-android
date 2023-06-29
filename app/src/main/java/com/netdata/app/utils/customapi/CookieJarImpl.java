package com.netdata.app.utils.customapi;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CookieJarImpl implements CookieJar {

    private HashMap<String, String> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            this.cookies.put(cookie.name(), cookie.value());
        }
    }

    public HashMap<String, String> getStoreCookies(){
        Log.e("setCookie",cookies.toString());
        return cookies;
    }

    public void clearStoreCookies(){
        Log.e("clear","clear");
        cookies.clear();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        for (String name : this.cookies.keySet()) {
            Cookie cookie = new Cookie.Builder()
                    .name(name)
                    .value(this.cookies.get(name))
                    .domain(url.host())
                    .build();
            cookies.add(cookie);
        }
        return cookies;
    }
}