package ru.svolf.pcompiler.net;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by radiationx on 02.05.17.
 */

public class NetworkRequest {
    private String url = "";
    private LinkedHashMap<String, String> headers, formHeaders;
    private Set<String> encodedFormHeaders;
    private boolean isMultipartForm = false;
    //true - get, false - post
    private boolean method = true;
    private boolean withoutBody = false;

    public NetworkRequest(Builder builder) {
        this.url = builder.url;
        this.headers = builder.headers;
        this.formHeaders = builder.formHeaders;
        this.encodedFormHeaders = builder.encodedFormHeaders;
        this.isMultipartForm = builder.isMultipartForm;
        this.method = builder.method;
        this.withoutBody = builder.withoutBody;
    }

    public String getUrl() {
        return url;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getFormHeaders() {
        return formHeaders;
    }

    public Set<String> getEncodedFormHeaders() {
        return encodedFormHeaders;
    }

    public boolean isMultipartForm() {
        return isMultipartForm;
    }


    public boolean getMethod() {
        return method;
    }

    public boolean isWithoutBody() {
        return withoutBody;
    }

    public static class Builder {
        private String url = "";
        private LinkedHashMap<String, String> headers, formHeaders;
        private Set<String> encodedFormHeaders;
        private boolean isMultipartForm = false;
        private boolean method = true;
        private boolean withoutBody = false;

        public NetworkRequest.Builder url(String url) {
            this.url = url;
            return this;
        }

        public NetworkRequest.Builder addHeaders(LinkedHashMap<String, String> headers) {
            if (this.headers == null)
                this.headers = new LinkedHashMap<>();
            this.headers.putAll(headers);
            return this;
        }

        public NetworkRequest.Builder addHeader(String name, String value) {
            if (this.headers == null)
                this.headers = new LinkedHashMap<>();
            this.headers.put(name, value);
            return this;
        }

        public NetworkRequest.Builder xhrHeader() {
            addHeader("X-Requested-With", "XMLHttpRequest");
            return this;
        }

        public NetworkRequest.Builder formHeaders(Map<String, String> formHeaders) {
            return formHeaders(formHeaders, false);
        }

        public NetworkRequest.Builder formHeaders(Map<String, String> formHeaders, boolean encoded) {
            if (this.formHeaders == null)
                this.formHeaders = new LinkedHashMap<>();
            this.formHeaders.putAll(formHeaders);
            if (encoded) {
                if (this.encodedFormHeaders == null) {
                    encodedFormHeaders = new HashSet<>();
                }
                encodedFormHeaders.addAll(this.formHeaders.keySet());
            }
            method = false;
            return this;
        }

        public NetworkRequest.Builder formHeader(String name, String value) {
            return formHeader(name, value, false);
        }

        public NetworkRequest.Builder formHeader(String name, String value, boolean encoded) {
            if (this.formHeaders == null)
                this.formHeaders = new LinkedHashMap<>();
            this.formHeaders.put(name, value);
            if (encoded) {
                if (this.encodedFormHeaders == null) {
                    encodedFormHeaders = new HashSet<>();
                }
                encodedFormHeaders.add(name);
            }
            method = false;
            return this;
        }

        public NetworkRequest.Builder multipart() {
            isMultipartForm = true;
            return this;
        }

        public NetworkRequest.Builder withoutBody() {
            withoutBody = true;
            return this;
        }

        public NetworkRequest build() {
            return new NetworkRequest(this);
        }
    }
}
