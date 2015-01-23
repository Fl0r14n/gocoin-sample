package com.gocoin.api;

/**
 * Oauth scope enum
 */
public enum Scope {

    USER_READ, //Access to read users data.
    USER_READ_WRITE, //Access to read and write user data. Cannot modify password.
    USER_PASSWORD_WRITE, //Can update user password. Must be combined with user_read_write to access other attributes.
    MERCHANTS_READ, //Can read all merchant info associated with a user.
    MERCHANT_READ_WRITE, //Can read and write to all merchants associated with a user.
    INVOICE_READ, //Can read all invoice info.
    INVOICE_READ_WRITE, //Can read and write to invoices.
    OAUTH_READ, //Read applications belonging to a specified user.
    OAUTH_READ_WRITE //Can read and write to user's applications.
    ;

    @Override
    public String toString() {
        //lower case the string
        String s = super.toString();
        return s.toLowerCase();
    }

    static public String getScope(Scope... scopes) {
        StringBuilder sb = new StringBuilder();
        for (Scope s : scopes) {
            sb.append(s.toString());
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    static public String getAllScopes() {
        return getScope(
                //Scope.values()     //Using all scopes doesn't work, the service doesn't like it
                Scope.USER_READ_WRITE, Scope.USER_PASSWORD_WRITE,
                Scope.MERCHANT_READ_WRITE, Scope.INVOICE_READ_WRITE,
                Scope.OAUTH_READ_WRITE
        );
    }

}
