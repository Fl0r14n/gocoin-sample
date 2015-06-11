package com.gocoin.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.net.URL;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

import com.gocoin.api.http.GCHttpRequest;
import com.gocoin.api.resources.Account;
import com.gocoin.api.resources.Application;
import com.gocoin.api.resources.ExchangeRates;
import com.gocoin.api.resources.Invoice;
import com.gocoin.api.resources.InvoiceList;
import com.gocoin.api.resources.Merchant;
import com.gocoin.api.http.SearchQueryBuilder;
import com.gocoin.api.resources.Token;
import com.gocoin.api.resources.User;
import com.gocoin.api.http.JsonMarshaller;
import com.gocoin.api.resources.AuthCode;

public class GoCoinApiTestCase {

    private final String MERCHANT_ID = "";
    private final String ACCOUNT_ID = "";
    private final String INVOICE_ID = "";
    private final String USER_ID = "";
    private final String CLIENT_ID = "";
    private final String CLIENT_SECRET = "";
    private final String CODE = "";
    private final String REDIRECT_URI = "";

    private final String TOKEN_USER_READ = "";
    private final String TOKEN_USER_READ_WRITE = "";
    private final String TOKEN_USER_PASSWORD_WRITE = "";
    private final String TOKEN_ALL_SCOPES = "";

    private final String TOKEN_CONFIRM_USER = "";
    private final String TOKEN_RESET_PASSWORD = "";

    Token t = new Token("5578fe0fe4362a2e908343d4438b2b6b6a7a35f375cba6dfde876c35d4dcc8b7", Scope.getScope(Scope.INVOICE_READ_WRITE, Scope.USER_READ));
    
    //@Test
    public void testExchangeRate() {
        ExchangeRates rates = GoCoin.getExchangeRates();
        System.out.printf("[DEBUG]: EXCHANGE %s%n", rates);
        System.out.printf("[DEBUG]: EXCHANGE RATE %s%n", rates.getExchangeRate("BTC", "USD"));
        //System.out.printf("[DEBUG]: EXCHANGE JSON %s%n",rates.toJSON());
    }

    //@Test
    public void testAccountService() {
        Collection<Account> accounts = GoCoin.getAccountService().getAccounts(t, MERCHANT_ID);
        System.out.println("[DEBUG]: accounts\n" + accounts);
    }

    //@Test
    public void testInvoiceService() {
        GoCoin.DEBUG = true;
        GoCoin.VERBOSE = true;
        //Token t = new Token(TOKEN_ALL_SCOPES, "", "");
        //perform a search        
        InvoiceList result = GoCoin.getInvoiceService().searchInvoices(t, new SearchQueryBuilder(MERCHANT_ID));
        System.out.println("[DEBUG]: invoice search result: " + result);
        Invoice invoice = null;
        for (Invoice inv : result.getInvoices()) {
            invoice = inv;
            break;
        }
        //get the invoice by id
        if (invoice != null) {
            Invoice i = GoCoin.getInvoiceService().getInvoice(t, invoice.getId());
            System.out.println("[DEBUG]: invoice: " + i);
        }
        boolean TEST_NEW_INVOICE = false;
        if (TEST_NEW_INVOICE) {
            //create a new invoice
            Invoice newInvoice = new Invoice();
            {
                newInvoice.setPriceCurrency("BTC");
                newInvoice.setBasePrice("124.00");
                newInvoice.setBasePriceCurrency("USD");
            }
            newInvoice = GoCoin.getInvoiceService().createInvoice(t, MERCHANT_ID, newInvoice);
            System.out.println("[DEBUG]: created invoice: " + newInvoice);
            System.out.println(newInvoice.getGatewayURL());
        }
    }

    //@Test
    public void testMerchantService() {
        //get the merchant
        Merchant m = GoCoin.getMerchantService().getMerchant(t, MERCHANT_ID);
        System.out.println("[DEBUG]: merchant: " + m);
        m.setCity("Utica");
        m.setRegion("NY");
        m.setContactName("Contact");
        //update the merchant
        m = GoCoin.getMerchantService().updateMerchant(t, m);
        System.out.println("[DEBUG]: updated merchant: " + m);
    }

    //@Test
    public void testMerchantUserService() {
        Collection<User> users = GoCoin.getMerchantUserService().getMerchantUsers(t, MERCHANT_ID);
        System.out.println("[DEBUG]: merchant users\n" + users);
    }

    //@Test
    public void testDepositAddressService() {
        boolean generated = GoCoin.getDepositAddressService().generateDepositAddress(t, ACCOUNT_ID);
        System.out.println("[DEBUG]: generated:" + generated);
    }

    //@Test
    public void testUserService() throws JsonProcessingException {
        //Token t = new Token(TOKEN_USER_READ, "", "");
        //test resource owner
        User u = GoCoin.getUserService().getResourceOwner(t);
        System.out.println("[DEBUG]: Resource owner: " + u);
        boolean TEST_GET_USER = false;
        //test get user
        if (TEST_GET_USER) {
            u = GoCoin.getUserService().getUser(t, USER_ID);
            System.out.println("[DEBUG]: Found: " + u);
            System.out.println("[DEBUG]: \n" + JsonMarshaller.write(u));
        }
        boolean TEST_UPDATE = false;
        if (TEST_UPDATE) {
            u.setLastName("LaBella");
            //test update user
            u = GoCoin.getUserService().updateUser(t, u, null);
            System.out.println("[DEBUG]: " + u);
        }
        boolean TEST_APPS = false;
        //test the user applications
        if (TEST_APPS) {
            Collection<Application> apps = GoCoin.getUserService().getUserApplications(t, u);
            System.out.println("[DEBUG]: applications\n" + apps);
        }
        boolean TEST_RESET_PW = false;
        if (TEST_RESET_PW) {
            boolean reset = GoCoin.getUserService().resetPassword(t, u);
            System.out.println("[DEBUG]: Reset password result: [" + reset + "]");
        }
        boolean TEST_RESET_PW_WITH_TOKEN = false;
        if (TEST_RESET_PW_WITH_TOKEN) {
            Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put("reset_token", TOKEN_RESET_PASSWORD);
            parameters.put("password", "passw0rd1");
            boolean reset = GoCoin.getUserService().resetPasswordWithToken(t, u, parameters);
            System.out.println("[DEBUG]: Reset password with TOKEN result: [" + reset + "]");
        }
        boolean TEST_CONFIRMATION_EMAIL = false;
        if (TEST_CONFIRMATION_EMAIL) {
            boolean email = GoCoin.getUserService().requestConfirmationEmail(t, u);
            System.out.println("[DEBUG]: Confirmation email result: [" + email + "]");
        }
        boolean TEST_CONFIRM_USER = false;
        if (TEST_CONFIRM_USER) {
            Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put("confirmation_token", TOKEN_CONFIRM_USER);
            boolean confirmed = GoCoin.getUserService().confirmUser(t, u, parameters);
            System.out.println("[DEBUG]: Confirm user result: [" + confirmed + "]");
        }
    }

    //@Test
    public void testApiCodeAndToken() throws Exception {
        System.out.println("[DEBUG]: running testApiCodeAndToken test case...");
        try {
            GCHttpRequest client = GoCoin.getHTTPClient();
            //test api url
            URL api_url = client.createURL(GCHttpRequest.URL_TYPE_API);
            //test dash url
            URL dash_url = client.createURL(GCHttpRequest.URL_TYPE_DASH);
            //test an auth url
            URL auth_url = GoCoin.getAuthURL(
                    CLIENT_ID, //client/appplication id
                    REDIRECT_URI, //redirect uri
                    Scope.getAllScopes(), //scope, ie: Scope.getAllScopes()
                    //Scope.getScope(Scope.USER_READ_WRITE, Scope.USER_PASSWORD_WRITE),  //scope, ie: Scope.getAllScopes()
                    "optional" //state
            );

            System.out.println("[DEBUG]: using auth URL [" + auth_url + "]");
            System.out.println("[DEBUG]: using api URL  [" + api_url + "]");
            System.out.println("[DEBUG]: using dash URL [" + dash_url + "]");

            boolean TEST_TOKEN = false;

            if (TEST_TOKEN) {
                //get a new oauth token
                Token token = GoCoin.getOAuthToken(new AuthCode(
                        CODE, //code
                        CLIENT_ID, //client/appplication id
                        CLIENT_SECRET, //client secret
                        REDIRECT_URI //redirect uri
                ));

                System.out.println("[WARNING]: auth token [" + token + "]");
            }
            //TODO: add assertions
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
