package com.test.analysis;

import com.test.analysis.core.client.MyGitHubClient;

import org.eclipse.egit.github.core.client.GitHubClient;

public class Config {
    //private static MyGitHubClient client;
    private static GitHubClient client;
    private final static String user = "";//github user
    private final static String password = "";//github password
    private final static String token = "";//OAuth2 token authentication

    public static GitHubClient getGitHubClient() {
        if (client == null) {
            //client = new MyGitHubClient();//自定义GitHubClient，方便调试
            client = new GitHubClient();
            if (user.length() > 0 && password.length() > 0)
                client.setCredentials(user, password);
            else if (token.length() > 0)
                client.setOAuth2Token(token);
        }
        return client;
    }
}
