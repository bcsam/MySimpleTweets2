package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TweetAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
//import org.parceler.Parcel;

/**
 * Created by bcsam on 6/26/17.
 */
@Parcel
public class Tweet {

    //list of all attributes
    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public String timeStamp;

    public Tweet(){}

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract vals from json
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.timeStamp = TweetAdapter.getRelativeTimeAgo(tweet.createdAt);
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;

    }
}
