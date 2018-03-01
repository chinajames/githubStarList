package com.test.analysis.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.analysis.utils.Log;

import org.eclipse.egit.github.core.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

/**
 * Created by Administrator on 2017/8/22.
 */

public class GsonTest {
    private static final String TAG = "GsonTest";
    int bufferSize = 1024;

    public void test() {
        File sourceFile = new File(System.getProperty("user.dir"), "1973379480.json");
        Log.d(TAG, String.format("exists = %s", sourceFile.exists()));
        try {
            FileInputStream stream = new FileInputStream(sourceFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream, CHARSET_UTF8), bufferSize);
           /*String read ;
            while ((read=reader.readLine())!=null){
                Log.d(TAG, String.format("%s",read ));
            }*/
            List<Repository> repositories = parseJsonArrayWithGson(reader, Repository.class);
            for (int i = 0; i < repositories.size(); i++) {
                Log.d(TAG, String.format("i = %s , repositories:  = %s", i, repositories));
            }
            Log.d(TAG, String.format("repoBeanList = %s", repositories));
           /* List<RepoBean> repoBeanList = parseJsonArrayWithGson(reader, RepoBean.class);
            Log.d(TAG, String.format("repoBeanList:  = %s", repoBeanList.size()));
            for (int i = 0; i < repoBeanList.size(); i++) {
                Log.d(TAG, String.format("i=%s , repoBean:  = %s", i, repoBeanList.get(i)));
            }*/
            //Log.d(TAG, String.format("repoBeanList:  = %s", repoBeanList));
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> parseJsonArrayWithGson(BufferedReader reader, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(reader, new TypeToken<List<T>>() {
        }.getType());
    }
}
