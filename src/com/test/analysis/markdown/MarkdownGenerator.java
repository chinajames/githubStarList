package com.test.analysis.markdown;


import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MarkdownGenerator {
    private static final String TAG = "MarkdownGenerator";
    private String excelHead = "| name | description | language | update | star | fork |";
    private String excelFormat = "|:--:|:--:|:--:|:--:|:--:|:--:|";
    private File markdownFile;
    private final String repository = "repository";
    private final String readme = "readme";
    private File repositoryFlie = new File(System.getProperty("user.dir"), repository);
    private File readmeFlie = new File(System.getProperty("user.dir"), readme);
    private File defaultFile = new File(System.getProperty("user.dir"), "README.md");

    public MarkdownGenerator() {
        this(new File(System.getProperty("user.dir"), "README.md"));
    }

    public MarkdownGenerator(File markdownFile) {
        setFile(markdownFile);
        if (!repositoryFlie.exists()) {
            repositoryFlie.mkdirs();
        }
        if (!readmeFlie.exists()) {
            readmeFlie.mkdirs();
        }
    }

    public File getDefaultFile(){
        return defaultFile;
    }
    public void setFile(File markdownFile) {
        setFile(markdownFile, true);
    }

    public void setFile(File markdownFile, boolean delete) {
        this.markdownFile = markdownFile;
        if (markdownFile.exists() && delete)
            markdownFile.delete();
    }

    public void writeFollowingUsers(List<User> followings) {
        List<String> stringList = new ArrayList<>();
        stringList.add("#### following user\n");
        stringList.add("| user | user | user | user | user | user |");
        stringList.add("|:--:|:--:|:--:|:--:|:--:|:--:|");
        StringBuilder builder = new StringBuilder();
        builder.append("| ");
        for (int i = 0; i < followings.size(); i++) {
            builder.append(generateUser(followings.get(i)));
            builder.append("| ");
            if (i % 6 == 5) {
                stringList.add(builder.toString());
                builder = new StringBuilder();
                builder.append("| ");
            }
        }
        try {
            FileUtils.writeLines(markdownFile, "utf-8", stringList, true);//追加模式
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateUser(User user) {
        StringBuilder builder = new StringBuilder();
        //[名称](/example/profile.md)
        builder.append("[");
        builder.append(user.getLogin());//名称
        builder.append("](/");
        builder.append(repository);
        builder.append("/");
        builder.append(user.getLogin());
        builder.append(".md)");
        return builder.toString();
    }

    public File createUserReadme(Repository repository) {
        File userFlie = new File(readmeFlie, repository.getOwner().getLogin());
        if (!userFlie.exists()) userFlie.mkdirs();
        File userReadmeFlie = new File(userFlie, repository.getName() + ".md");
        return userReadmeFlie;
    }

    public File createUserReadme(String user) {
        File userMarkdownFlie = new File(repositoryFlie, user + ".md");
        return userMarkdownFlie;
    }

    public void writeTitle(String title, int size) {
        //### 三级标题
        StringBuilder builder = new StringBuilder();
        builder.append("#### ");
        builder.append(title);
        builder.append(" (");
        builder.append(size);
        builder.append(")\n");
        try {
            FileUtils.write(markdownFile, builder.toString(), "utf-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String data, boolean append) {
        try {
            FileUtils.write(markdownFile, data, "utf-8", append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHead() {
        List<String> stringList = new ArrayList<>();
        stringList.add(excelHead);
        stringList.add(excelFormat);
        try {
            FileUtils.writeLines(markdownFile, "utf-8", stringList, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRepository(List<Repository> repositorys) {
        List<String> stringList = new ArrayList<>();
        for (Repository repository : repositorys) {
            stringList.add(generateContent(repository));
        }
        try {
            FileUtils.writeLines(markdownFile, "utf-8", stringList, true);//追加模式
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRepository(Repository repository) {
        String content = generateContent(repository) + "\n";
        try {
            FileUtils.write(markdownFile, content, "utf-8", true);//追加模式
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateContent(Repository repository) {
        //| name | owner | language | update | star | fork | description |
        StringBuilder builder = new StringBuilder();
        builder.append("| ");
        //builder.append(repository.getName());
        //builder.append("[名称](https://github.com/k0shk0sh/FastHub \"悬停显示\")");
        builder.append(generateName(repository));
       /* builder.append("| ");
        builder.append(repository.getName());*/
        builder.append("| ");
        builder.append(repository.getDescription());
        builder.append("| ");
        builder.append(repository.getLanguage());
        builder.append("| ");
        //builder.append(repository.getCreatedAt().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
        String dateString = formatter.format(repository.getUpdatedAt());
        builder.append(dateString);
        builder.append("| ");
        builder.append(repository.getStargazersCount());
        builder.append("| ");
        builder.append(repository.getForks());
        builder.append("|");
        return builder.toString();
    }

    private String generateName(Repository repository) {
        StringBuilder builder = new StringBuilder();
        //builder.append("[名称](https://github.com/k0shk0sh/FastHub \"悬停显示\")");
        builder.append("[");
        builder.append(repository.getName());//名称
        builder.append("](");
        builder.append(repository.getHtmlUrl());
        builder.append(" \"");
        builder.append(repository.getName());//悬停显示
        builder.append("\")");
        return builder.toString();
    }
}
