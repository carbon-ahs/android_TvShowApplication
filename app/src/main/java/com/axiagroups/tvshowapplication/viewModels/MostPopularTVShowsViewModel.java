package com.axiagroups.tvshowapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.axiagroups.tvshowapplication.repositories.MostPopularTVShowsRepository;
import com.axiagroups.tvshowapplication.responses.TVShowsResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();
    }

    public LiveData<TVShowsResponse> getMostPopularTVShows(int page) {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}
