package com.codepath.apps.restclienttemplate.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

public class ComposeActivity extends AppCompatActivity {

    private EditText etBody;
    private TextView etCharCount;
    public static final int CHARS_IN_A_TWEET=140;
    private boolean isReply;

    private long in_reply_to_status_id;
    private String in_reply_to_screen_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etBody = (EditText) findViewById(R.id.etBody);
        etBody.addTextChangedListener(mTextEditorWatcher);

        Bundle bundle = getIntent().getExtras();
        in_reply_to_screen_name = bundle.getString("screenName");
        isReply = false;
        if(in_reply_to_screen_name != null) {
            isReply = true;
            in_reply_to_status_id = bundle.getLong("tweetId");
            etBody.setText("@" + in_reply_to_screen_name);
        }
    }


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            etCharCount = (TextView) findViewById(R.id.etCharCount);
            etCharCount.setText(String.valueOf((CHARS_IN_A_TWEET - s.length())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void cancel(View v){
        Intent intent = new Intent(this, TimelineActivity.class);
        this.startActivity(intent);
    }

    public void onSubmit(View v){
        EditText etName = (EditText) findViewById(R.id.etBody);
        // Prepare data intent
        // Pass relevant data back as a result
        final String message = etName.getText().toString();

        TwitterClient client = new TwitterClient(this);

        if(!isReply) {
            client.sendTweet(message, (new JsonHttpResponseHandler() {
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

            }), -1, "");
        }else{
            client.sendTweet(message, (new JsonHttpResponseHandler() {
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

            }), in_reply_to_status_id, in_reply_to_screen_name);
        }
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

