package com.test.analysis;


import com.test.analysis.utils.RepositoryListGenerator;

import org.eclipse.egit.github.core.client.GitHubClient;

public class GithubAnalysis {

    public static void main(String[] args) {
        //test user :chinajames jameschinese
        GitHubClient client = Config.getGitHubClient();
        RepositoryListGenerator generator = new RepositoryListGenerator(client);
        generator.getFollowingByDefault();
        generator.getStarredBydefault();
        generator.getRepositoriesBydefault();
        //generator.getFollowingsRepositories();
    }


}
