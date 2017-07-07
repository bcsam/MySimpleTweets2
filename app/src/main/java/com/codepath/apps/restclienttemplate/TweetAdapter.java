package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.ComposeActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bcsam on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;
    TwitterClient client;



    //define an interface required by the ViewHolder
    public interface TweetAdapterListener{
        public void onItemSelected(View view, int position);
    }

    public interface TweetSelectedListener{
        public void onTweetSelected(Tweet tweet);
    }
    //pass in Tweets array into constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    //for each row, inflate the layout and cache references into ViewHolder


    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        client = new TwitterClient(context);
        return viewHolder;
    }

    //bind the values based on the position of the element


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get data according to position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimeStamp.setText(tweet.timeStamp);
        holder.tvRTCount.setText(Integer.toString(tweet.retweetCount));
        holder.tvLikeCount.setText(Integer.toString(tweet.favoriteCount));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    public int getItemCount() {
        return mTweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public ImageView btReply;
        public ImageView ivRetweet;
        public ImageView ivLike;
        public TextView tvRTCount;
        public TextView tvLikeCount;
        private int RTCount;
        private int LikeCount;

        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            btReply = (ImageView) itemView.findViewById(R.id.btReply);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            tvRTCount = (TextView) itemView.findViewById(R.id.tvRTCount);
            //RTCount = Integer.parseInt(tvRTCount.getText().toString());
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
           // LikeCount = Integer.parseInt(tvLikeCount.getText().toString());
            //itemView.setOnClickListener(this);


            btReply.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btReply.setColorFilter(R.color.medium_red);
                    //gets item position
                    int position = getAdapterPosition();
                    //make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Tweet tweet = mTweets.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, ComposeActivity.class);
                        // serialize the movie using parceler, use its short name as a key
                        //intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                        intent.putExtra("screenName", tweet.user.screenName);
                        intent.putExtra("tweetId", tweet.uid);
                        intent.putExtra("reply", true);
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });

            // TODO: 7/6/17 scroll to the position if it brings you to the top
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mTweets.get(position);
                        client.sendRetweet(tweet.uid, new JsonHttpResponseHandler() {

                            private final int RESULT_OK = 20;

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Tweet newTweet = null;
                                ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                                ivRetweet.setColorFilter(R.color.inline_action_retweet);
                                try {
                                    newTweet = Tweet.fromJSON(response);
                                    tvRTCount.setText(Integer.toString(newTweet.retweetCount));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("retweet", errorResponse.toString());
                            }
                        });
                    }

                }});

            ivLike.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ivLike.setColorFilter(R.color.inline_action_like_pressed);
                        ivLike.setImageResource(R.drawable.ic_vector_heart);
                        Tweet tweet = mTweets.get(position);

                        //tvLikeCount.setText(Integer.toString(LikeCount));

                        client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                Tweet newTweet = null;
                                try {
                                    newTweet = Tweet.fromJSON(response);
                                    tvLikeCount.setText(Integer.toString(newTweet.favoriteCount));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                        //client.favoriteTweet
                        //params.put(id,)
                        //favorites/create.json
                        //create new tweet in onSuccess
                        //create new tweet
                        //set text of the tvfavorite to newTweet.favorites

                    }
                }});

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = mTweets.get(position);
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("screen_name", tweet.user.screenName);
                        context.startActivity(intent);
                    }
                }
            });

    }
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void add(Tweet tweet) {
        mTweets.add(tweet);
        notifyDataSetChanged();
    }
}
