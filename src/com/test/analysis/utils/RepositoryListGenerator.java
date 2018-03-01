package com.test.analysis.utils;

import com.test.analysis.Config;
import com.test.analysis.markdown.MarkdownGenerator;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.StargazerService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RepositoryListGenerator {
    private static final String TAG = "RepositoryListGenerator";
    private GitHubClient client;
    private MarkdownGenerator markdownGenerator;
    private List<String> starsList = new ArrayList<String>();
    private List<String> forkList = new ArrayList<String>();
    private List<User> followings;

    public RepositoryListGenerator() {
        this(Config.getGitHubClient());
    }

    public RepositoryListGenerator(GitHubClient client) {
        this.client = client;
        markdownGenerator = new MarkdownGenerator();
    }

    public void getFollowingByDefault() {
        UserService service = new UserService(client);
        try {
            //List<User> followers = new ArrayList<>();
            List<User> followers = service.getFollowers();
            List<User> followings = service.getFollowing();
            this.followings = followings;
            Collections.sort(followings, new myUserComparator());
            markdownGenerator.setFile(markdownGenerator.getDefaultFile());
            markdownGenerator.writeFollowingUsers(followings);
            Log.d(TAG, String.format("getFollowers: followers = %s", followers.size()));
            Log.d(TAG, String.format("getFollowers: followings = %s", followings.size()));
            for (User follower : followers) {
                Log.d(TAG, String.format("follower login = %s url = %s type = %s", follower.getLogin(), follower.getHtmlUrl(), follower.getType()));
            }
            for (User following : followings) {
                Log.d(TAG, String.format("following login = %s url = %s id = %s", following.getLogin(), following.getHtmlUrl(), following.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStarredBydefault() {
        starsList.clear();
        StargazerService service = new StargazerService(client);
        markdownGenerator.setFile(markdownGenerator.getDefaultFile(), false);
        try {
            List<Repository> repositories = service.getStarred();
            markdownGenerator.writeTitle("stars", repositories.size());
            markdownGenerator.writeHead();
            markdownGenerator.writeRepository(repositories);
            Log.d(TAG, String.format("getStarred: size = %s", repositories.size()));
            for (Repository repo : repositories) {
                starsList.add(repo.getName());
                Log.d(TAG, String.format("getStarred: repo = %s", repo.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRepositoriesBydefault() {
        RepositoryService service = new RepositoryService(client);
        markdownGenerator.setFile(markdownGenerator.getDefaultFile(), false);
        try {
            List<Repository> repositories = service.getRepositories();
            Log.d(TAG, String.format("getRepositories: size = %s", repositories.size()));
            markdownGenerator.writeTitle("repositories", repositories.size());
            markdownGenerator.writeHead();
            //markdownGenerator.writeRepository(repositories);
            for (Repository repo : repositories) {
                if (!starsList.contains(repo.getName())) {
                    markdownGenerator.writeRepository(repo);
                    Log.d(TAG, String.format("getRepositories: name = %s", repo.getName()));
                } else {
                    Log.e(TAG, String.format("getRepositories: name = %s", repo.getName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFollowingsRepositories() {
        /*for (User user : followings) {
            getRepositories(user.getLogin());
            //getStarred(user.getLogin());
        }*/
        getRepositories(followings.get(0).getLogin());
        getStarred(followings.get(0).getLogin());
    }

    public void getFollowers(String user) {
        UserService service = new UserService(client);
        try {
            List<User> followers = new ArrayList<>();
            //List<User> followers = service.getFollowers(user);
            List<User> followings = service.getFollowing(user);
            Log.d(TAG, String.format("getFollowers: followers = %s", followers.size()));
            Log.d(TAG, String.format("getFollowers: followings = %s", followings.size()));
            for (User follower : followers) {
                Log.d(TAG, String.format("follower login = %s url = %s type = %s", follower.getLogin(), follower.getHtmlUrl(), follower.getType()));
            }
            for (User following : followings) {
                Log.d(TAG, String.format("following login = %s url = %s id = %s", following.getLogin(), following.getHtmlUrl(), following.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStarred(String user) {
        StargazerService service = new StargazerService(client);
        File userMarkdownFlie = markdownGenerator.createUserReadme(user);
        markdownGenerator.setFile(userMarkdownFlie, false);
        try {
            List<Repository> repositories = service.getStarred(user);
            markdownGenerator.writeTitle("stars", repositories.size());
            markdownGenerator.writeHead();
            //markdownGenerator.writeRepository(repositories);
            Log.d(TAG, String.format("getStarred: size = %s", repositories.size()));
            for (Repository repo : repositories) {
                if (!forkList.contains(repo.getName())) {
                    markdownGenerator.writeRepository(repo);
                    Log.d(TAG, String.format("getStarred: name = %s", repo.getName()));
                } else {
                    Log.e(TAG, String.format("getStarred: name = %s", repo.getName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRepositories(String user) {
        forkList.clear();
        RepositoryService service = new RepositoryService(client);
        File userMarkdownFlie = markdownGenerator.createUserReadme(user);
        markdownGenerator.setFile(userMarkdownFlie);
        try {
            List<Repository> repositories = service.getRepositories(user);
            Log.d(TAG, String.format("getRepositories: size = %s", repositories.size()));
            markdownGenerator.writeTitle("repositories", repositories.size());
            markdownGenerator.writeHead();
            markdownGenerator.writeRepository(repositories);
            for (Repository repo : repositories) {
                forkList.add(repo.getName());
                Log.d(TAG, String.format("getRepositories: name = %s", repo.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchRepositories(String query, String language) {
        RepositoryService service = new RepositoryService(client);
        try {
            //"topic:github-client", "java"
            List<SearchRepository> searchRepositories = service.searchRepositories(query, language);
            Log.d(TAG, String.format("searchRepositories:  = %s", searchRepositories.size()));
            for (SearchRepository repository : searchRepositories) {
                Log.d(TAG, String.format("searchRepositories: name = %s", repository.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getReadme(Repository repository) {
        ContentsService contentsService = new ContentsService(client);
        File readmeFile = markdownGenerator.createUserReadme(repository);
        try {
            RepositoryContents repositoryContents = contentsService.getReadme(repository);
            String readme = new String(EncodingUtils.fromBase64(repositoryContents.getContent()));
            Log.d(TAG, String.format("getReadme: readme = %s", readme.length()));
            markdownGenerator.setFile(readmeFile);
            markdownGenerator.write(readme, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReadme(String owner, String name) {
        RepositoryService repositoryService = new RepositoryService(client);
        ContentsService contentsService = new ContentsService(client);
        try {
            Repository repository = repositoryService.getRepository(owner, name);
            RepositoryContents repositoryContents = contentsService.getReadme(repository);
            String readme = new String(EncodingUtils.fromBase64(repositoryContents.getContent()));
            Log.d(TAG, String.format("getReadme: readme = %s", readme));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class myUserComparator implements Comparator<User> {
        @Override
        public int compare(User user, User t1) {
            return user.getId() - t1.getId();
        }
    }

    private class myRepositoryComparator implements Comparator<Repository> {
        @Override
        public int compare(Repository repository, Repository t1) {
            return 0;
        }
    }
}
