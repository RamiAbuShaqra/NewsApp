package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    /** Tag for log messages */
    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Private constructor
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createURL(String url) {
        URL queryUrl = null;

        if (url != null) {
            try {
                queryUrl = new URL(url);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error in creating query URL !!", e);
            }
        }
        return queryUrl;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpConnection(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if (url != null) {
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Unsuccessful connection: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in setting up the connection", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<News> parseJsonResponse(String returnedJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(returnedJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(returnedJson);
            JSONObject response = baseJson.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);

                String contentTitle = currentNews.getString("webTitle");
                String sectionName = currentNews.getString("sectionName");
                String webUrl = currentNews.getString("webUrl");

                String authorName;
                String publishedDate;

                if (currentNews.has("webPublicationDate")) {
                    publishedDate = currentNews.getString("webPublicationDate");
                } else {
                    publishedDate = "";
                }

                JSONArray tagsArray = currentNews.getJSONArray("tags");
                if (tagsArray.length() == 1) {
                    JSONObject currentTag = tagsArray.getJSONObject(0);
                    authorName = currentTag.getString("webTitle");
                } else {
                    authorName = "";
                }

                News newsObject = new News(contentTitle, sectionName, authorName, publishedDate, webUrl);

                news.add(newsObject);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing the JSON response", e);
        }

        return news;
    }

    /**
     * Query the GUARDIAN dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String queryLink) {
        if (queryLink != null){
            try {
                return parseJsonResponse(makeHttpConnection(createURL(queryLink)));
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in fetching the data", e);
            }
        }
        return null;
    }
}
