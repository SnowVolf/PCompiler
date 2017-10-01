package ru.SnowVolf.pcompiler.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RecoverySystem;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.TimeUnit;


import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.SnowVolf.girl.reactive.SimpleObservable;
import ru.SnowVolf.pcompiler.App;

/**
 * Created by Snow Volf on 21.09.2017, 0:09
 */

public class Client {
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
    //    private final static String userAgent = WebSettings.getDefaultUserAgent(App.getContext());
    private final static String LOG_TAG = Client.class.getSimpleName();
    private static Client INSTANCE = null;
    private static Map<String, Cookie> cookies;
    private static List<Cookie> listCookies;
    private String userAgent;
    private SimpleObservable networkObservables = new SimpleObservable();
    private ArrayList<String> privateHeaders = new ArrayList<>(Arrays.asList("pass_hash", "session_id", "auth_key", "password"));
    private final Cookie mobileCookie = Cookie.parse(HttpUrl.parse("https://4pda.ru/"), "ngx_mb=1;");

    //Class
    public Client() {
        try {
            userAgent = USER_AGENT;
        } catch (Exception ignore) {
            userAgent = "Linux; Android NaN; UNKNOWN";
        }
        cookies = new HashMap<>();
        listCookies = new ArrayList<>();
        cookies.put("ngx_mb", mobileCookie);
    }

    public static Client getInstance() {
        if (INSTANCE == null) INSTANCE = new Client();
        return INSTANCE;
    }

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    //Network
    public NetworkResponse get(String url) throws Exception {
        return request(new NetworkRequest.Builder().url(url).build());
    }

    private Request.Builder prepareRequest(NetworkRequest request, RecoverySystem.ProgressListener uploadProgressListener) {
        //Log.d("FORPDA_LOG", "request url " + request.getUrl());
        String url = request.getUrl();
        if (request.getUrl().substring(0, 2).equals("//")) {
            url = "https:".concat(request.getUrl());
        }
        Log.d(LOG_TAG, "Request url " + request.getUrl());
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4")
                .header("User-Agent", USER_AGENT);
        if (request.getHeaders() != null) {
            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                Log.d(LOG_TAG, "Header " + entry.getKey() + " : " + (privateHeaders.contains(entry.getKey()) ? "private" : entry.getValue()));
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        return requestBuilder;
    }

    public NetworkResponse request(NetworkRequest request, OkHttpClient client, RecoverySystem.ProgressListener uploadProgressListener) throws Exception {
        Request.Builder requestBuilder = prepareRequest(request, uploadProgressListener);
        NetworkResponse response = new NetworkResponse(request.getUrl());
        Response okHttpResponse = null;
        try {
            okHttpResponse = client.newCall(requestBuilder.build()).execute();
            if (!okHttpResponse.isSuccessful())
                Toast.makeText(App.getContext(), "Pizda rulyi", Toast.LENGTH_LONG).show();

            response.setCode(okHttpResponse.code());
            response.setMessage(okHttpResponse.message());
            response.setRedirect(okHttpResponse.request().url().toString());

            if (!request.isWithoutBody()) {
                response.setBody(okHttpResponse.body().string());
            }

            //Log.d("SUKA", "" + request.isWithoutBody() + " : " + response.toString());
            Log.d(LOG_TAG, "Response: " + response.toString());
        } /*catch (InterruptedIOException iioe){
            iioe.printStackTrace();
        }*/finally {
            if (okHttpResponse != null)
                okHttpResponse.close();
        }
        return response;
    }

    public NetworkResponse request(NetworkRequest request) throws Exception {
        return request(request, this.client, null);
    }

    public void removeNetworkObserver(Observer observer) {
        networkObservables.deleteObserver(observer);
    }

    public void addNetworkObserver(Observer observer) {
        networkObservables.addObserver(observer);
    }

    public void notifyNetworkObservers(Boolean b) {
        networkObservables.notifyObservers(b);
    }

    public boolean getNetworkState() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
