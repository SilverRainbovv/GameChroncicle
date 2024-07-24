package com.didenko.gameservice.util;

import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.TwitchToken;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IgdbWrapperUtil {

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String ACCESS_TOKEN;
    private final Environment env;
    private IGDBWrapper wrapper;

    public IGDBWrapper getIGDBWrapper() {
        if (wrapper != null) {
            return wrapper;
        } else {
            if (CLIENT_ID == null || CLIENT_SECRET == null) {
                initCredentials();
            }
            wrapper = IGDBWrapper.INSTANCE;
            wrapper.setCredentials(CLIENT_ID, ACCESS_TOKEN);

            return wrapper;
        }
    }

    private void getTwitchToken() {
        if (CLIENT_ID == null || CLIENT_SECRET == null) {
            initCredentials();
        }
        TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
        TwitchToken token = tAuth.requestTwitchToken(CLIENT_ID, CLIENT_SECRET);
        ACCESS_TOKEN = token.getAccess_token();
    }

    private void initCredentials() {
        CLIENT_ID = env.getProperty("client-id");
        CLIENT_SECRET = env.getProperty("client-secret");

        getTwitchToken();
    }

}