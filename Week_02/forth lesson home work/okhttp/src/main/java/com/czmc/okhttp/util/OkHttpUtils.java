package com.czmc.okhttp.util;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    // 缓存客户端实例
    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = 
    		MediaType.get("application/json; charset=utf-8");
    private OkHttpUtils() {
		
	}
    
	   // GET 调用
    public static String getAsString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String postAsString(String url,String json) throws IOException {
    	RequestBody requestBody = RequestBody.create(JSON, json);
    	Request request = new Request.Builder().url(url).post(requestBody).build();
		try(Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
    }

    public static void main(String[] args) throws Exception {

        String url = "http://127.0.0.1:8801";
        String text = OkHttpUtils.getAsString(url);
        System.out.println("url: " + url + " ; response: \n" + text);

        // 清理资源
        OkHttpUtils.client = null;
    }
}
