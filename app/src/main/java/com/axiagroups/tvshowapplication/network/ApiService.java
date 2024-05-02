package com.axiagroups.tvshowapplication.network;

import com.axiagroups.tvshowapplication.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);

}
