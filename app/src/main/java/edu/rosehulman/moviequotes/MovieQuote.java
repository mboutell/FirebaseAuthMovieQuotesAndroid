package edu.rosehulman.moviequotes;

import java.util.HashMap;
import java.util.Map;

/**
 * The model for a moviequote
 * <p/>
 * Created by rockwotj on 4/22/2015.
 */
public class MovieQuote {

    private String mKey;
    private String mMovie;
    private String mQuote;
    private String mUid;

    public MovieQuote(String key, String movie, String quote, String uid) {
        mKey = key;
        mMovie = movie;
        mQuote = quote;
        mUid = uid;
    }

    public MovieQuote() {
    }

    public MovieQuote(String movie, String quote, String uid) {
        mMovie = movie;
        mQuote = quote;
        mUid = uid;
    }

    public MovieQuote(String key, Map<String, Object> map) {
        mKey = key;
        mMovie = (String) map.get("movie");
        mQuote = (String) map.get("quote");
        mUid = (String) map.get("uid");
//        mMovie = movie;
//        mQuote = quote;
    }

    public String getKey() {
        return mKey;
    }

    public String getUid() {
        return mUid;
    }

    public String getMovie() {
        return mMovie;
    }

    public void setMovie(String movie) {
        mMovie = movie;
    }

    public String getQuote() {
        return mQuote;
    }

    public void setQuote(String quote) {
        mQuote = quote;
    }

    public void setValues(Map<String, Object> values) {
        mMovie = (String) values.get("movie");
        mQuote = (String) values.get("quote");
        mUid = (String) values.get("uid");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("movie", mMovie);
        map.put("quote", mQuote);
        map.put("uid", mUid);
        return map;
    }


}