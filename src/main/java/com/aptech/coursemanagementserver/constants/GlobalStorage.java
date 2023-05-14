package com.aptech.coursemanagementserver.constants;

public interface GlobalStorage {
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    // DOMAIN EMAIL
    String DOMAIN_EMAIL = "contact@cmproj.com";

    // DOMAIN API URL
    String DEV_DOMAIN_API = "http://localhost:8080";
    String PROD_DOMAIN_API = "https://cmapi.com";

    // DOMAIN CLIENT URL
    String DEV_DOMAIN_CLIENT = "http://localhost:3000";
    String PROD_DOMAIN_CLIENT = "http://cmclient.com";

    String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

}
