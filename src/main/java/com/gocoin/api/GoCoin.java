package com.gocoin.api;

import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.http.GCHttpRequestImpl;
import java.net.URL;

import com.gocoin.api.resources.AuthCode;
import com.gocoin.api.resources.ExchangeRates;
import com.gocoin.api.resources.Token;
import com.gocoin.api.services.AccountService;
import com.gocoin.api.services.DepositAddressService;
import com.gocoin.api.services.InvoiceService;
import com.gocoin.api.services.MerchantService;
import com.gocoin.api.services.MerchantUserService;
import com.gocoin.api.services.UserService;
import com.gocoin.api.services.impl.HTTPUserService;
import com.gocoin.api.services.impl.HTTPDepositAddressService;
import com.gocoin.api.services.impl.HTTPInvoiceService;
import com.gocoin.api.services.impl.HTTPMerchantService;
import com.gocoin.api.services.impl.HTTPMerchantUserService;
import com.gocoin.api.services.impl.HTTPAccountService;
import com.gocoin.api.http.JsonMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GoCoin {

    private static final Logger L = LoggerFactory.getLogger(GoCoin.class);

    private static final UserService userService;
    private static final AccountService accountService;
    private static final InvoiceService invoiceService;
    private static final MerchantService merchantService;
    private static final MerchantUserService merchantUserService;
    private static final DepositAddressService depositAddressService;

    static {
        userService = new HTTPUserService();
        accountService = new HTTPAccountService();
        invoiceService = new HTTPInvoiceService();
        merchantService = new HTTPMerchantService();
        merchantUserService = new HTTPMerchantUserService();
        depositAddressService = new HTTPDepositAddressService();
    }

    static public GCHttpRequest getHTTPClient() {
        return new GCHttpRequestImpl();
    }

    static public UserService getUserService() {
        return userService;
    }

    static public AccountService getAccountService() {
        return accountService;
    }

    static public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    static public MerchantService getMerchantService() {
        return merchantService;
    }

    static public MerchantUserService getMerchantUserService() {
        return merchantUserService;
    }

    static public DepositAddressService getDepositAddressService() {
        return depositAddressService;
    }

    //the exchange service url to use
    public static final String EXCHANGE_HOST = "x.g0cn.com";
    public static final String EXCHANGE_PATH = "/prices";

    public static boolean DEBUG = false;
    public static boolean VERBOSE = false;

    static public URL getAuthURL(String clientId, String redirectUri, String scope, String state) {
        //get a new http client and set the options
        GCHttpRequest client = getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/auth");
        //set parameters
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_RESPONSE_TYPE, "code");
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_CLIENT_ID, clientId);
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_REDIRECT_URI, redirectUri);
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_SCOPE, scope);
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_STATE, state);
        //create the url
        return client.createURL(GCHttpRequest.URL_TYPE_DASH);
    }

    static public Token getOAuthToken(AuthCode auth) {
        //get a new http client and set the options
        //NOTE: since its a post request, the parameters get converted into JSON
        GCHttpRequest client = getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, "/oauth/token");
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_POST);
        //set parameters
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_GRANT_TYPE, auth.getGrantType());
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_CODE, auth.getCode());
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_CLIENT_ID, auth.getClientId());
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_CLIENT_SECRET, auth.getClientSecret());
        client.setRequestParameter(GCHttpRequest.KEY_PARAM_REDIRECT_URI, auth.getRedirectUri());
        try {
            //make the POST request
            client.post(client.createURL(GCHttpRequest.URL_TYPE_API));
            return JsonMarshaller.read(client.getResponse(), Token.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }

    static public ExchangeRates getExchangeRates() {
        GCHttpRequest client = getHTTPClient();
        client.setRequestOption(GCHttpRequest.KEY_OPTION_HOST, EXCHANGE_HOST);
        client.setRequestOption(GCHttpRequest.KEY_OPTION_PATH, EXCHANGE_PATH);
        client.setRequestOption(GCHttpRequest.KEY_OPTION_METHOD, GCHttpRequest.METHOD_GET);
        try {
            //make the GET request
            client.get(client.createURL(null));
            return JsonMarshaller.read(client.getResponse(), ExchangeRates.class);
        } catch (Exception e) {
            L.error(e.getMessage());
            return null;
        }
    }
}
