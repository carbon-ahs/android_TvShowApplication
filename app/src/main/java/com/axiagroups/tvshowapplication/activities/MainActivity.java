package com.axiagroups.tvshowapplication.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.axiagroups.tvshowapplication.R;
import com.axiagroups.tvshowapplication.adapters.TVShowsAdapter;
import com.axiagroups.tvshowapplication.databinding.ActivityMainBinding;
import com.axiagroups.tvshowapplication.models.TVShow;
import com.axiagroups.tvshowapplication.viewModels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        getMostPopularTVShows();
    }

    private void getMostPopularTVShowsAsToast() {
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponce ->
                        Toast.makeText(
                                getApplicationContext(),
                                "Total Pages" + mostPopularTVShowsResponce.getPages(),
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void getMostPopularTVShows() {
        activityMainBinding.setIsLoading(true);
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponce -> {
                    activityMainBinding.setIsLoading(false);
            if (mostPopularTVShowsResponce != null) {
                if (mostPopularTVShowsResponce.getTvShows() != null) {
                    tvShows.addAll(mostPopularTVShowsResponce.getTvShows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }

        });
    }

}