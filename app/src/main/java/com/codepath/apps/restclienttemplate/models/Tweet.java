package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bcsam on 6/26/17.
 */

public class Tweet {

    //list of all attributes
    public String body;
    public long uid;
    public User user;
    public String createdAt;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract vals from json
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;

    }
}
