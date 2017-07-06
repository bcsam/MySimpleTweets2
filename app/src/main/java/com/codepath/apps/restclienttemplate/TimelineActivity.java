package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.ComposeActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {


    //private SwipeRefreshLayout swipeContainer;
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 20;
    TweetsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //set the adapter for the paper
        adapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapter);
        //setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        /*

        // Only ever call `setContentView` once right at the top
        //setContentView(R.layout.activity_timeline); //check on this
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                swipeContainer.setRefreshing(false);
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //launches the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                launchComposeView();
                //composeMessage();
                return true;
            case R.id.miProfile:
                //showProfileView();
                //launchProfileView(); //Todo
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    // ActivityOne.java
    public void launchComposeView(MenuItem menuItem) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        startActivityForResult(i, REQUEST_CODE);
    }

    /*
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.getHomeTimeline(new JsonHttpResponseHandler() { ////Is this the right client?
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                //tweetAdapter.addAll(temp);
                // Now we call setRefreshing(false) to signal refresh has finished
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                //ArrayList<Tweet> temp = new ArrayList<Tweet>();
                for (int i = 0; i < response.length(); i++) {
                    //convert each object to a Tweet model

                    //add the Tweet model to our data source
                    //notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }
*
*
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK) {
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            HomeTimelineFragment fragment =  (HomeTimelineFragment) adapter.getItem(0);
            fragment.addTweet(tweet);
        }
    }
}
