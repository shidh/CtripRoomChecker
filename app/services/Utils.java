package services;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
}
