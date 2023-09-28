package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.service.interfaces.URLService;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class URLServiceImpl implements URLService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public String validate(String url) {
        HttpURLConnection connection = null;
        int respCode = 200;
        if (url == null || url.isEmpty()) {
            System.out.println("URL is either not configured for anchor tag or it is empty");
        }
        try {
            connection = (HttpURLConnection) (new URL(url).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();
            respCode = connection.getResponseCode();

            if (respCode >= 400) {
                return QAConstant.NOTVALID + QAConstant.UNDERSCORE + respCode;
            } else {
                return QAConstant.VALID + QAConstant.UNDERSCORE + respCode;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QAConstant.NOTVALID;
    }

    @Override
    public HashMap<String,Integer> getRedirectedUrl(String url) {
        HttpURLConnection connection = null;
        HashMap<String,Integer> responseMap = new HashMap();
        int respCode = 200;
        if (url == null || url.isEmpty()) {
            System.out.println("URL is either not configured for anchor tag or it is empty");
            responseMap.put("",500);
            return responseMap;
        }
        try {
            connection = (HttpURLConnection) (new URL(url).openConnection());
            connection.setInstanceFollowRedirects( false );
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();

            respCode = connection.getResponseCode();


            String location = connection.getHeaderField( "Location" );
            responseMap.put(connection.getHeaderField( "Location" ),respCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            responseMap.put(url,500);
        } catch (IOException e) {
            e.printStackTrace();
            responseMap.put(url,500);
        }
        return responseMap;
    }

    @Override
    public MultiValueMap<String, String> getQueryParameters(String url){
        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();

        return queryParams;
    }

    @Override
    public HashMap<Integer, String> callGetAPI(String url){
        HashMap<Integer, String> responseMap = new HashMap();
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Response response = null;
        Request request = new Request.Builder()
                .addHeader("accept","application/json")
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            responseMap.put(response.code(), response.body().string());
            response.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return responseMap;
    }

    @Override
    public String callPostAPI(String input, String url) {

        String workfrontResponse =null;
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), input);
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        // body = RequestBody.create(input.getBytes(StandardCharsets.UTF_8));.getBytes(Charsets.US_ASCII)
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Encoding", "identity")
                .addHeader("Content-Type","application/json; charset=utf-8")
                .build();

        final Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            workfrontResponse =  response.body().string();
            response.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return workfrontResponse;
    }
}
