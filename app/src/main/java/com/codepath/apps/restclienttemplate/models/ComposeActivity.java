package com.codepath.apps.restclienttemplate.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

public class ComposeActivity extends AppCompatActivity {

    //private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    /*
    public boolean onTweetItemSelected(MenuItem item){
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.btTweet:
                TwitterClient t = new TwitterClient(this);
                onSubmit();

                return true;
            case R.id.btCancel:


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
 */
    public void onSubmit(View v){
        EditText etName = (EditText) findViewById(R.id.etBody);
        // Prepare data intent
        // Pass relevant data back as a result
        final String message = etName.getText().toString();
        ////data.putExtra("tweetBody", message);

        TwitterClient client = new TwitterClient(this);

        client.sendTweet(message, (new JsonHttpResponseHandler(){
           private final int RESULT_OK = 20;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Intent data = new Intent();
                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response);
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }



        }));
    }
/*
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

    }


*/
    //timeline intent to compose
    //compose onSubmit stores the getText of the edit text and sends to the api
    //onSuccess has a finish method which sends the message back to the timeline activity
}

