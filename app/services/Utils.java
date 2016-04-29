package services;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class Utils {
    public static final int DEFAULT_CONNECT_TIMEOUT_IN_MILLISECONDS = 30000;
    public static final int DEFAULT_READ_TIMEOUT_IN_MILLISECONDS = 60000;

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static String toJson(Object classOfT) {
        return new Gson().toJson(classOfT);
    }

    public static String httpGet(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_MILLISECONDS);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MILLISECONDS);
        connection.setRequestMethod("GET");
        InputStream inStream = connection.getInputStream();
        InputStreamReader inReader = new InputStreamReader(inStream, "UTF-8");
        BufferedReader bufReader = new BufferedReader(inReader);
        String line = bufReader.readLine();
        StringBuffer buf = new StringBuffer();
        while (line != null) {
            buf.append(line);
            line = bufReader.readLine();
        }
        inStream.close();
        connection.disconnect();
        return buf.toString();
    }

    public static String readFileToString(String filePath, Class classs) {
        StringBuilder result = new StringBuilder("");
        URL pathURL = classs.getResource(filePath);
        if (pathURL == null) return null;
        java.nio.file.Path resPath = null;
        try {
            resPath = Paths.get(pathURL.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (resPath == null) return null;
        List<String> lines = new ArrayList<String>();
        try {
            lines.addAll(Files.readAllLines(resPath, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            result.append(line);
        }
        return result.toString();
    }
}
