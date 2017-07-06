package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bcsam on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private HomeTimelineFragment timelineFragment;
    private MentionsTimelineFragment mentionsTimelineFragment;

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return the total # of fragment
    @Override
    public int getCount(){
        return 2;
    }

    //return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {
       if(position==0){
           return getTimelineInstance();
       }else if(position==1){
           return getMentionsTimelineInstance();
       }else{
            return null;
       }
    }

    //return title

    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }

    private HomeTimelineFragment getTimelineInstance(){
        if (timelineFragment == null){
            timelineFragment = new HomeTimelineFragment();
        }
        return timelineFragment;
    }

    private MentionsTimelineFragment getMentionsTimelineInstance(){
        if (mentionsTimelineFragment == null){
            mentionsTimelineFragment = new MentionsTimelineFragment();
        }
        return mentionsTimelineFragment;
    }
}
