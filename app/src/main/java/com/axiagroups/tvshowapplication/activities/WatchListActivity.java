package com.axiagroups.tvshowapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.axiagroups.tvshowapplication.R;
import com.axiagroups.tvshowapplication.adapters.WatchlistAdapter;
import com.axiagroups.tvshowapplication.databinding.ActivityTvshowDetailsBinding;
import com.axiagroups.tvshowapplication.databinding.ActivityWatchListBinding;
import com.axiagroups.tvshowapplication.listeners.WatchlistListener;
import com.axiagroups.tvshowapplication.models.TVShow;
import com.axiagroups.tvshowapplication.viewModels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_watch_list);
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchlist = new ArrayList<>();
    }

    private void loadWatchlist() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    // Toast.makeText(getApplicationContext(), "Watchlist:" + tvShows.size(), Toast.LENGTH_SHORT).show();
                    if (watchlist.size() > 0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, this);
                    activityWatchListBinding.watchListRecyclerView.setAdapter(watchlistAdapter);
                    activityWatchListBinding.watchListRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();

                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);

    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(
                viewModel.removeTVShowFromWatchlist(tvShow)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            watchlist.remove(position);
                            watchlistAdapter.notifyItemRemoved(position);
                            watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                            compositeDisposableForDelete.dispose();
                        }));
    }
}