package com.axiagroups.tvshowapplication.listeners;

import com.axiagroups.tvshowapplication.models.TVShow;

/**
 * Created by Ahsan Habib on 5/6/2024.
 * shehanuk.ahsan@gmail.com
 */
public interface WatchlistListener {
    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}
