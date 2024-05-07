package com.axiagroups.tvshowapplication.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.axiagroups.tvshowapplication.database.TVShowDatabase;
import com.axiagroups.tvshowapplication.models.TVShow;
import com.axiagroups.tvshowapplication.repositories.TVShowDetailsRepository;
import com.axiagroups.tvshowapplication.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Ahsan Habib on 5/4/2024.
 * shehanuk.ahsan@gmail.com
 */
public class TVShowDetailsViewModel extends AndroidViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }


    public LiveData<TVShowDetailsResponse> getTVShowDetail(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().addToWatchlist(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchList(String tvShowID) {
        return tvShowDatabase.tvShowDao().getTVShowFromWatchlist(tvShowID);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().removeFromWatchlist(tvShow);
    }

}
