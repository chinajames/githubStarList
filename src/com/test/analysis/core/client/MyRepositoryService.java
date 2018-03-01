package com.test.analysis.core.client;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyRepositoryService extends RepositoryService {
    public MyRepositoryService() {
    }

    public MyRepositoryService(GitHubClient client) {
        super(client);
    }



}
