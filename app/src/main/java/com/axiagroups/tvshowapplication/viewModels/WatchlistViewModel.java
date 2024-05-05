package com.axiagroups.tvshowapplication.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.axiagroups.tvshowapplication.database.TVShowDatabase;
import com.axiagroups.tvshowapplication.models.TVShow;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ahsan Habib on 5/5/2024.
 * shehanuk.ahsan@gmail.com
 */
public class WatchlistViewModel extends AndroidViewModel {
    private TVShowDatabase tvShowDatabase;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchlist() {
        return tvShowDatabase.tvShowDao().getWatchlist();
    }
}
