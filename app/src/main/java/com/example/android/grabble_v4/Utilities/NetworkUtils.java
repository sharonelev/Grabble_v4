package com.example.android.grabble_v4.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by user on 24/09/2017.
 */



public class NetworkUtils {

    final static String BASE_URL_CHK_WORD="http://www.wordgamedictionary.com/api/v1/references/scrabble/";
    final static String KEY = "key";
    final static String API_CHK_WORD = "2.6319805885208397e29";
    final static String BASE_URL_OXFORD ="https://od-api.oxforddictionaries.com/api/v1";

        //http://www.wordgamedictionary.com/api/v1/references/scrabble/test?key=2.6319805885208397e29

    public static URL buildUrlCheckWord(String checkWord) {
        Uri builtUri =  Uri.parse(BASE_URL_CHK_WORD).buildUpon().appendPath(checkWord).appendQueryParameter(KEY,API_CHK_WORD).build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
