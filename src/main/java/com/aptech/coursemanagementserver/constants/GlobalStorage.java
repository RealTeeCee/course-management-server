package com.aptech.coursemanagementserver.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface GlobalStorage {
    // import static com.aptech.coursemanagementserver.constants.GlobalStorage.*;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    // PATH ROOT
    Path COURSE_PATH = Paths.get("assets", "images", "course");

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

    String GLOBAL_EXCEPTION = "Something wrong. Please try again";
    String FETCHING_FAILED = "Fetch data failed!";
    String BAD_REQUEST_EXCEPTION = "Failed! Please check your infomation and try again.";
    String INVALID_TOKEN_EXCEPTION = "Failed! Token is not valid.";
}
