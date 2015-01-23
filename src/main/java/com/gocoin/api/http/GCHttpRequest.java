package com.gocoin.api.http;

import java.util.Map;
import java.net.URL;

import com.gocoin.api.resources.Token;

public interface GCHttpRequest {

    static final String USER_AGENT = "Mozilla/5.0";

    //header keys
    static final String KEY_HEADER_CONTENT_TYPE = "Content-Type";

    //default header values
    static final String DEFAULT_HEADER_CONTENT_TYPE = "application/json";

    //option keys
    static final String KEY_OPTION_CLIENT_ID = "client_id";
    static final String KEY_OPTION_CLIENT_SECRET = "client_secret";
    static final String KEY_OPTION_HOST = "host";
    static final String KEY_OPTION_DASHBOARD_HOST = "dashboard_host";
    static final String KEY_OPTION_PORT = "port";
    static final String KEY_OPTION_API_PATH = "api_path";
    static final String KEY_OPTION_API_VERSION = "api_version";
    static final String KEY_OPTION_PATH = "path";
    static final String KEY_OPTION_SECURE = "secure";
    static final String KEY_OPTION_METHOD = "method";
    static final String KEY_OPTION_REQUEST_ID = "request_id";
    static final String KEY_OPTION_REDIRECT_URI = "redirect_uri";

    //default option values
    static final String DEFAULT_OPTION_HOST = "api.gocoin.com";
    static final String DEFAULT_OPTION_DASHBOARD_HOST = "dashboard.gocoin.com";
    static final String DEFAULT_OPTION_PORT = "443";
    static final String DEFAULT_OPTION_API_PATH = "/api";
    static final String DEFAULT_OPTION_API_VERSION = "/v1";
    static final String DEFAULT_OPTION_PATH = "";
    static final String DEFAULT_OPTION_SECURE = "true";
    static final String DEFAULT_OPTION_METHOD = GCHttpRequest.METHOD_GET;
    static final String DEFAULT_OPTION_REQUEST_ID = "";
    static final String DEFAULT_OPTION_REDIRECT_URI = "";

    //parameter keys
    //auth url keys
    static final String KEY_PARAM_RESPONSE_TYPE = "response_type";
    static final String KEY_PARAM_CLIENT_ID = "client_id";
    static final String KEY_PARAM_REDIRECT_URI = "redirect_uri";
    static final String KEY_PARAM_SCOPE = "scope";
    static final String KEY_PARAM_STATE = "state";
    //token request keys
    static final String KEY_PARAM_GRANT_TYPE = "grant_type";
    static final String KEY_PARAM_CODE = "code";
    static final String KEY_PARAM_CLIENT_SECRET = "client_secret";

    //url types
    static final String URL_TYPE_API = "api";
    static final String URL_TYPE_DASH = "dash";

    //method types
    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_PUT = "PUT";
    static final String METHOD_DELETE = "DELETE";

    void addAuthorizationHeader(Token t);

    void setRequestHeader(String name, String value);

    void setRequestHeaders(Map<String, String> headers);

    void setRequestOption(String name, String value);

    void setRequestOptions(Map<String, String> options);

    void setRequestParameter(String name, String value);

    void setRequestParameters(Map<String, String> parameters);

    URL createURL(String type);

    void setRequestBody(String body);

    String getRequestBody();

    void get(URL u) throws Exception;

    void post(URL u) throws Exception;

    void put(URL u) throws Exception;

    void delete(URL u) throws Exception;

    String getResponse();

    int getResponseCode();

    String getResponseMsg();

    void checkResponse() throws Exception;
}
