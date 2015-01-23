package com.gocoin.api.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;

import java.util.Map;
import java.util.LinkedHashMap;

import com.gocoin.api.GoCoin;
import com.gocoin.api.resources.Token;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GCHttpRequestImpl implements GCHttpRequest {

    private static final Logger L = LoggerFactory.getLogger(GCHttpRequestImpl.class);

    private final Map<String, String> options;

    private final Map<String, String> parameters;

    private final Map<String, String> headers;

    private final Collection<Integer> validCodes;

    public GCHttpRequestImpl() {
        validCodes = Arrays.asList(new Integer[]{200, 201, 204});
        options = new LinkedHashMap<>();
        parameters = new LinkedHashMap<>();
        headers = new LinkedHashMap<>();
        //default options
        options.put(GCHttpRequest.KEY_OPTION_HOST, GCHttpRequest.DEFAULT_OPTION_HOST);
        options.put(GCHttpRequest.KEY_OPTION_DASHBOARD_HOST, GCHttpRequest.DEFAULT_OPTION_DASHBOARD_HOST);
        options.put(GCHttpRequest.KEY_OPTION_PORT, GCHttpRequest.DEFAULT_OPTION_PORT);
        options.put(GCHttpRequest.KEY_OPTION_API_PATH, GCHttpRequest.DEFAULT_OPTION_API_PATH);
        options.put(GCHttpRequest.KEY_OPTION_API_VERSION, GCHttpRequest.DEFAULT_OPTION_API_VERSION);
        options.put(GCHttpRequest.KEY_OPTION_PATH, GCHttpRequest.DEFAULT_OPTION_PATH);
        options.put(GCHttpRequest.KEY_OPTION_SECURE, GCHttpRequest.DEFAULT_OPTION_SECURE);
        options.put(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.DEFAULT_OPTION_METHOD);
        options.put(GCHttpRequest.KEY_OPTION_REQUEST_ID, GCHttpRequest.DEFAULT_OPTION_REQUEST_ID);
        options.put(GCHttpRequest.KEY_OPTION_REDIRECT_URI, GCHttpRequest.DEFAULT_OPTION_REDIRECT_URI);

        //default headers
        headers.put(GCHttpRequest.KEY_HEADER_CONTENT_TYPE, GCHttpRequest.DEFAULT_HEADER_CONTENT_TYPE);
    }

    /**
     * the oauth token
     */
    protected String token = null;

    /**
     * the error string
     */
    protected String error = null;

    /**
     * the response string
     */
    protected String response = null;

    /**
     * the response code
     */
    protected int responseCode = -1;

    /**
     * the response code
     */
    protected String responseMsg = null;

    /**
     * the request string
     */
    protected String request = null;

    @Override
    public void addAuthorizationHeader(Token t) {
        this.headers.put("Authorization", "Bearer " + t.getToken());
    }

    @Override
    public void setRequestHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public void setRequestHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    @Override
    public void setRequestOption(String name, String value) {
        this.options.put(name, value);
    }

    @Override
    public void setRequestOptions(Map<String, String> options) {
        this.options.putAll(options);
    }

    protected String getRequestOption(String key) {
        if (this.options != null && this.options.containsKey(key)) {
            String param = this.options.get(key);
            if (hasValue(param)) {
                return param;
            }
        }
        return null;
    }

    @Override
    public void setRequestParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    @Override
    public void setRequestParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public URL createURL(String type) {
        //get our options/parameters
        String host = getRequestOption(GCHttpRequest.KEY_OPTION_HOST);
        String dash_host = getRequestOption(GCHttpRequest.KEY_OPTION_DASHBOARD_HOST);
        String port = getRequestOption(GCHttpRequest.KEY_OPTION_PORT);
        String path = getRequestOption(GCHttpRequest.KEY_OPTION_PATH);
        String secure = getRequestOption(GCHttpRequest.KEY_OPTION_SECURE);
        String method = getRequestOption(GCHttpRequest.KEY_OPTION_METHOD);

        //secure url / https
        boolean https = "true".equalsIgnoreCase(secure);

        //our string builder
        StringBuilder sb = new StringBuilder();

        //if no host is set, get outta here
        if (!hasValue(host) && !hasValue(dash_host)) {
            return null;
        }

        //set a default port
        if (!hasValue(port)) {
            if (https) {
                port = "443";
            } else {
                port = "80";
            }
        }

        //support https or http
        if (https) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }

        //for dashboard types, use the dash host
        if (GCHttpRequest.URL_TYPE_DASH.equalsIgnoreCase(type)) {
            sb.append(dash_host).append(":").append(port);
        } //otherwise, use the given host
        else {
            sb.append(host).append(":").append(port);
        }

        //for api types, include the api path and version
        if (GCHttpRequest.URL_TYPE_API.equalsIgnoreCase(type)) {
            String api_path = getRequestOption(GCHttpRequest.KEY_OPTION_API_PATH);
            String api_version = getRequestOption(GCHttpRequest.KEY_OPTION_API_VERSION);
            //if api path and version aren't set, we have a problem
            if (!hasValue(api_path, api_version)) {
                return null;
            }
            sb.append(api_path).append(api_version);
        }

        //append the path
        if (hasValue(path)) {
            sb.append(path);
        }

        //append any parameters if its a GET request
        if (hasValue(this.parameters) && GCHttpRequest.METHOD_GET.equals(method)) {
            sb.append("?");
            for (Map.Entry<String, String> param : parameters.entrySet()) {
                try {
                    sb.append(param.getKey());
                    sb.append("=");
                    sb.append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    sb.append("&");
                } //this really shouldn't ever happen, but, get out if it does
                catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
            //delete the trailing &
            sb.deleteCharAt(sb.length() - 1);
        }

        try {
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            L.error(e.getMessage());
        }

        return null;
    }

    @Override
    public void setRequestBody(String body) {
        this.request = body;
    }

    @Override
    public String getRequestBody() {
        //get the request method
        String method = getRequestOption(GCHttpRequest.KEY_OPTION_METHOD);

        //sanity check, who's calling me via GET?
        if (GCHttpRequest.METHOD_GET.equals(method)) {
            return null;
        }

        if (hasValue(this.request)) {
            return this.request;
        } else {
            try {
                return JsonMarshaller.write(this.parameters);
            } catch (JsonProcessingException ex) {
                return ex.getMessage();
            }
        }
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public int getResponseCode() {
        return this.responseCode;
    }

    @Override
    public String getResponseMsg() {
        return this.responseMsg;
    }

    @Override
    public void get(URL u) throws Exception {
        request(u);
    }

    @Override
    public void post(URL u) throws Exception {
        request(u);
    }

    @Override
    public void put(URL u) throws Exception {
        request(u);
    }

    @Override
    public void delete(URL u) throws Exception {
        request(u);
    }

    protected void request(URL u) throws Exception {
        InputStream is = null;

        try {
            //get the request method
            String method = getRequestOption(GCHttpRequest.KEY_OPTION_METHOD);

            //debug
            if (GoCoin.DEBUG) {
                L.debug("[WARNING]: Sending %s request to URL [%s]...", method, u);
            }

            //open the URL connection
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //set the request method
            conn.setRequestMethod(method);

            //add request header
            conn.setRequestProperty("User-Agent", GCHttpRequest.USER_AGENT);

            //append any headers
            if (hasValue(this.headers)) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    if (GoCoin.VERBOSE) {
                        L.debug("[DEBUG]: Adding request header [%s]=>[%s]", header.getKey(), header.getValue());
                    }
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            //send post data if its not a get/delete/etc.
            if (!GCHttpRequest.METHOD_GET.equals(method)) {
                String body = getRequestBody();
                if (GoCoin.VERBOSE) {
                    L.debug("[DEBUG]: Request body: %n%s", body);
                }
                conn.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.writeBytes(body);
                    wr.flush();
                }
            }

            //get the response code
            this.responseCode = conn.getResponseCode();
            this.responseMsg = conn.getResponseMessage();

            if (GoCoin.DEBUG) {
                L.debug("[DEBUG]: Response Code: [%d]", responseCode);
            }
            if (GoCoin.DEBUG) {
                L.debug("[DEBUG]: Response Message: [%s]", responseMsg);
            }

            //get our response input stream
            if (responseCode >= 200 && responseCode <= 299) {
                is = conn.getInputStream();
            } //anything not in the 200 range is an error. so, use the error stream
            else {
                is = conn.getErrorStream();
            }

            if (is != null) {
                StringBuilder sb;
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
                    sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                }
                this.response = sb.toString();
            }

            //print result
            if (GoCoin.VERBOSE) {
                L.debug("[DEBUG]: Response Body:");
                L.debug(getResponse());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                //try to close the is and clean up after ourselves, do nothing if it fails
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void checkResponse() throws Exception {
        if (!validCodes.contains(this.getResponseCode())) {
            throw new Exception("[ERROR]: " + this.getResponseCode() + " - " + this.getResponseMsg());
        }
    }

    private boolean hasValue(Object... objects) {
        for (Object o : objects) {
            boolean has_value = true;
            if (o == null) {
                return false;
            } else if (o instanceof String) {
                has_value = !"".equals(o.toString().trim());
            } else if (o instanceof Collection<?>) {
                has_value = !((Collection<?>) o).isEmpty();
            } else if (o instanceof Map<?, ?>) {
                has_value = ((Map<?, ?>) o).size() > 0;
            }
            if (!has_value) {
                return false;
            }
        }
        return true;
    }
}
