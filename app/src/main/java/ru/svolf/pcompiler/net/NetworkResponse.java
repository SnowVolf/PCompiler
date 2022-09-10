package ru.svolf.pcompiler.net;

/**
 * Created by radiationx on 07.07.17.
 */

public class NetworkResponse {
    private int code = 0;
    private String message = "";
    private String url = "";
    private String redirect = url;
    private String body = "";

    public NetworkResponse(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NetworkResponse{ code: " + code + ", message: " + message + ", url: " + url + ", redirect: " + redirect + ", body length: " + body.length() + "}";
    }
}
