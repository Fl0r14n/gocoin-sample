package com.gocoin.api.services.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import com.gocoin.api.GoCoin;
import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Application;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.User;

import com.gocoin.api.services.UserService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPUserService implements UserService {

    private static final Logger L = LoggerFactory.getLogger(HTTPUserService.class);

    @Override
    public User getResourceOwner(Token t) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/user");
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), User.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public User getUser(Token t, String userId) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/users/" + userId);
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), User.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<User> listUsers(Token t, Map<String, String> parameters) {
        return null;
    }

    @Override
    public User createUser(Token t, User u, Map<String, String> parameters) {
        return null;
    }

    @Override
    public User updateUser(Token t, User u, Map<String, String> parameters) {
        //get a new http client and set the options
        //NOTE: since its a post request, the parameters get converted into JSON
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/users/" + u.getId());
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_PUT);
        client.addAuthorizationHeader(t);
        try {
            client.setRequestBody(JsonMarshaller.write(u));
            client.put(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.read(client.getResponse(), User.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteUser(Token t, User u, Map<String, String> parameters) {
        return false;
    }

    @Override
    public boolean updatePassword(Token t, User u, Map<String, String> parameters) {
        if (!parameters.containsKey("current_password") || !parameters.containsKey("password")) {
            L.error("Invalid parameters for resetPasswordWithToken!");
            return false;
        }

//TODO: pull out map keys as constants
        String path = "/users/" + u.getId() + "/password";

        //get a new http client and set the options
        //NOTE: since its a post request, the parameters get converted into JSON
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, path);
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_PUT);
        client.addAuthorizationHeader(t);
        //create the json map
        Map<String, String> body = new LinkedHashMap<>();
        body.put("current_password", parameters.get("current_password"));
        body.put("currentpassword", parameters.get("current_password"));
        body.put("password", parameters.get("password"));
        body.put("newpassword", parameters.get("password"));
        try {
            client.setRequestBody(JsonMarshaller.write(body));
            client.put(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean resetPassword(Token t, User u) {
        //get a new http client and set the options
        //NOTE: since its a post request, the parameters get converted into JSON
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/users/request_password_reset");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        //create the json map
        Map<String, String> body = new LinkedHashMap<>();
        body.put("email", u.getEmail());
        try {
            client.setRequestBody(JsonMarshaller.write(body));
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean resetPasswordWithToken(Token t, User u, Map<String, String> parameters) {
        if (!parameters.containsKey("password") || !parameters.containsKey("reset_token")) {
            L.error("Invalid parameters for resetPasswordWithToken!");
            return false;
        }
        //TODO: pull out map keys as constants
        String path = "/users/" + u.getId() + "/reset_password/" + parameters.get("reset_token");

        //get a new http client and set the options
        //NOTE: since its a post request, the parameters get converted into JSON
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, path);
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_PUT);
        client.addAuthorizationHeader(t);
        //create the json map
        Map<String, String> body = new LinkedHashMap<>();
        body.put("password", parameters.get("password"));
        body.put("password_confirmation", parameters.get("password"));
        try {
            client.setRequestBody(JsonMarshaller.write(body));
            client.put(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean requestConfirmationEmail(Token t, User u) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/users/request_new_confirmation_email");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        //create the json map
        Map<String, String> body = new LinkedHashMap<>();
        body.put("email", u.getEmail());
        try {
            client.setRequestBody(JsonMarshaller.write(body));
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean confirmUser(Token t, User u, Map<String, String> parameters) {
        if (!parameters.containsKey("confirmation_token")) {
            L.error("Invalid parameters for confirmUser!");
            return false;
        }
        //TODO: pull out map keys as constants
        String path = "/users/" + u.getId() + "/confirm_account/" + parameters.get("confirmation_token");

        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, path);
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return true;
        } catch (Exception e) {
            L.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Collection<Application> getUserApplications(Token t, User u) {
        GCHttpRequest client = GoCoin.getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/users/" + u.getId() + "/applications");
        client.addAuthorizationHeader(t);
        try {
            client.get(client.createURL(GCHttpRequest.URL_TYPE_API));
            client.checkResponse();
            return JsonMarshaller.readArray(client.getResponse(), Application.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }
}
