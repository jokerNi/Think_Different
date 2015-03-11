package com.think_different.network;

/**
 * Created by oceancx on 15/3/3.
 */
public class WeiBoAPI {
    public static String APP_KEY="3375079460";
    public static String APP_SECRET="65efddcdec4c4377908b734120d2120e";


    public static String REDIRECT_URL="https://api.weibo.com/oauth2/default.html";
    public static final String BASE_URL="https://api.weibo.com/2/";
    public static final String BASE_OAUTH2="https://api.weibo.com/oauth2/";


    public static final String OAUTH2_AUTHORIZE="https://api.weibo.com/oauth2/authorize?";
    // Login
    public static final String OAUTH2_ACCESS_TOKEN = BASE_URL + "oauth2/access_token";

    public static final String GET_HOME_TIMELINE="https://api.weibo.com/2/statuses/home_timeline.json";

    public static final String URL_USERS_SHOW="users/show.json";
    public static final String URL_ACCOUNT_GET_UID="account/get_uid";
    public static final String URL_ACCOUNT_END_SESSION ="account/end_session.json";

    public static final String URL_REVOKEOAUTH2 = "revokeoauth2";

}
