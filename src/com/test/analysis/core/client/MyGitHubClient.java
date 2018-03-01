package com.test.analysis.core.client;

import com.test.analysis.utils.Log;

import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

/**
 * Created by Administrator on 2017/8/18.
 */

public class MyGitHubClient extends GitHubClient {
    private static final String TAG = "MyGitHubClient";
    private int bufferSize = 8192;
    private File tempFile = new File(System.getProperty("user.dir"), "temp");
    private boolean debug = true;

    public MyGitHubClient() {
        if (debug) tempFile.mkdirs();
    }

    public MyGitHubClient(String hostname) {
        super(hostname);
    }

    public MyGitHubClient(String hostname, int port, String scheme) {
        super(hostname, port, scheme);
    }

    @Override
    protected HttpURLConnection configureRequest(HttpURLConnection request) {
        //认证过程
        HttpURLConnection httpURLConnection = super.configureRequest(request);
        if (debug)
            Log.d(String.format("RequestProperties =  %s", httpURLConnection.getRequestProperties()));
        return httpURLConnection;
        //return super.configureRequest(request);
    }

    /*@Override
    protected String createUri(String path) {
        //return super.createUri(path);
        String baseCreateUri = super.createUri(path);
    }*/

    @Override
    public GitHubResponse get(GitHubRequest request) throws IOException {
        Map<String, String> params = request.getParams();
        if (params == null) {
            Map<String, String> addParams = new HashMap<>();
            //&sort=stars&order=desc type=source
            //addParams.put("type","source");
            addParams.put("sort", "stars");
            //addParams.put("order","desc");
            //addParams.put("language","java");
            request.setParams(addParams);
        }
        if (debug) {
            params = request.getParams();
            for (Map.Entry<String, String> param : params.entrySet())
                Log.d(String.format("get key = %s values = %s", param.getKey(), param.getValue()));
        }
        GitHubResponse gitHubResponse = super.get(request);
        Log.d(String.format("RequestLimit = %s RemainingRequests = %s", getRequestLimit(), getRemainingRequests()));
        return gitHubResponse;
        //return super.get(request);
    }

    @Override
    protected Object getBody(GitHubRequest request, InputStream stream) throws IOException {
        Log.d(TAG, String.format("getBody: Uri = %s", createUri(request.generateUri())));
        if (debug) {
            String generateUri = request.generateUri();
            Log.d(TAG, String.format("getBody: generateUri = %s", generateUri));
            String name_start = generateUri.substring(1, generateUri.indexOf('?')).replace('/', '_');
            String name_end = generateUri.substring(generateUri.lastIndexOf('&'), generateUri.length()).replace('=', '_');
            //Log.d(TAG, String.format("getBody:  name_start = %s name_end = %s", name_start, name_end));
            //File temp = new File(tempFile, request.hashCode() + ".json");
            File temp = new File(tempFile, name_start + name_end + ".json");
            if (temp.exists()) temp.delete();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream, CHARSET_UTF8), bufferSize);
            String line;
            while ((line = reader.readLine()) != null) {
                FileUtils.write(temp, line, "utf-8", true);
            }
            FileInputStream fileInputStream = new FileInputStream(temp);
            return super.getBody(request, fileInputStream);
        } else {
            return super.getBody(request, stream);
        }
        //return super.getBody(request, stream);
    }

    @Override
    protected <V> V parseJson(InputStream stream, Type type, Type listType) throws IOException {
        if (debug) Log.d(TAG, String.format("parseJson: type = %s ,listType = %s", type, listType));
        return super.parseJson(stream, type, listType);
    }
}
