package com.test.analysis.test;

import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */

public class markdownTest {

    public String excelHead = "| name | owner | language | updatedAt | star | fork | description |";
    public String excelFormat = "|:--:|:--:|:--:|:--:|:--:|:--:|:--:|";

    public void run() {
        markdownTest markdownTest = new markdownTest();
        markdownTest.sample();
        markdownTest.test();
    }

    public void sample() {
        StringBuilder builder = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        stringList.add("| 左对齐 | 居中  | 右对齐 |");
        stringList.add("| :------------ |:---------------:| -----:|");
        stringList.add("| col 3 is      | some wordy text | $1600 |");
        stringList.add("| col 2 is      | centered        |   $12 |");
        stringList.add("| zebra stripes | are neat        |    $1 |");

        File readmeFile = new File(System.getProperty("user.dir"), "readme-sample.md");
        try {
            FileUtils.writeLines(readmeFile, "utf-8", stringList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        File readmeFile = new File(System.getProperty("user.dir"), "readme.md");
        log(readmeFile.getAbsolutePath());
        List<String> stringList = new ArrayList<>();
        stringList.add(excelHead);
        stringList.add(excelFormat);
        Repository repository = new Repository();
        repository.setName("FastHub");
        repository.setDescription("FastHub the ultimate GitHub client for Android.");
        repository.setUrl("https://github.com/k0shk0sh/FastHub");
        stringList.add(genaratorContent(repository));
        stringList.add(genaratorContent(repository));
        stringList.add(genaratorContent(repository));
        try {
            FileUtils.writeLines(readmeFile, "utf-8", stringList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public String genaratorContent(Repository repository) {
        StringBuilder builder = new StringBuilder();
        builder.append("| ");
        //builder.append(repository.getName());
        //builder.append("[名称](https://github.com/k0shk0sh/FastHub \"悬停显示\")");
        builder.append(genaratorName(repository));
        builder.append("| ");
        builder.append(repository.getName());
        builder.append("| ");
        builder.append(repository.getName());
        builder.append("| ");
        builder.append(repository.getName());
        builder.append("| ");
        builder.append(repository.getName());
        builder.append("| ");
        builder.append(repository.getName());
        builder.append("| ");
        builder.append(repository.getDescription());
        builder.append("|");
        return builder.toString();
    }

    public String genaratorName(Repository repository) {
        StringBuilder builder = new StringBuilder();
        //builder.append("[名称](https://github.com/k0shk0sh/FastHub \"悬停显示\")");
        builder.append("[");
        builder.append(repository.getName());//名称
        builder.append("](");
        builder.append(repository.getUrl());
        builder.append(" \"");
        builder.append(repository.getName());//悬停显示
        builder.append("\")");
        return builder.toString();
    }

}
