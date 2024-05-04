package com.axiagroups.tvshowapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.axiagroups.tvshowapplication.repositories.TVShowDetailsRepository;
import com.axiagroups.tvshowapplication.responses.TVShowDetailsResponse;

/**
 * Created by Ahsan Habib on 5/4/2024.
 * shehanuk.ahsan@gmail.com
 */
public class TVShowDetailsViewModel extends ViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;

    public TVShowDetailsViewModel() {
        tvShowDetailsRepository = new TVShowDetailsRepository();
    }


    public LiveData<TVShowDetailsResponse> getTVShowDetail(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

}
