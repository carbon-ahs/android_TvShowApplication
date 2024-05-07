package com.axiagroups.tvshowapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.axiagroups.tvshowapplication.R;
import com.axiagroups.tvshowapplication.adapters.EpisodesAdapter;
import com.axiagroups.tvshowapplication.adapters.ImageSliderAdapter;
import com.axiagroups.tvshowapplication.databinding.ActivityTvshowDetailsBinding;
import com.axiagroups.tvshowapplication.databinding.LayoutEpisodesBottomSheetBinding;
import com.axiagroups.tvshowapplication.models.TVShow;
import com.axiagroups.tvshowapplication.utilities.TempDataHolder;
import com.axiagroups.tvshowapplication.viewModels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowInWatchlist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_tvshow_details);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        doInitialization();

    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchList(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowInWatchlist = true;
                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();
                })
        );
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetail(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTvshowDetailsBinding.setIsLoading(false);

            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                activityTvshowDetailsBinding.setTvShowImageURL(
                        tvShowDetailsResponse.getTvShowDetails().getImagePath()
                );
                activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.setDescription(
                        String.valueOf(
                                HtmlCompat.fromHtml(
                                        tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                        )
                );
                activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textReadMore.setOnClickListener(v -> {
                    if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                        activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                        activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less);
                    } else {
                        activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                        activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more);
                    }
                });
                activityTvshowDetailsBinding.setRating(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                        )
                );
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    activityTvshowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    activityTvshowDetailsBinding.setGenre("N/A");
                }
                activityTvshowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min.");
                activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.layoutMise.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                activityTvshowDetailsBinding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                        startActivity(intent);
                    }
                });
                activityTvshowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (episodeBottomSheetDialog == null) {
                            episodeBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                            layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                    LayoutInflater.from(TVShowDetailsActivity.this),
                                    R.layout.layout_episodes_bottom_sheet,
                                    findViewById(R.id.episodesContainer),
                                    false
                            );
                            episodeBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                            layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                    new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                            );
                            layoutEpisodesBottomSheetBinding.textTitle.setText(
                                    String.format("Episodes | %s", tvShow.getName())
                            );
                            layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    episodeBottomSheetDialog.dismiss();
                                }
                            });
                        }
                        episodeBottomSheetDialog.show();
                    }
                });
                activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.imageWatchlist.setOnClickListener(v ->
                {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTVShowInWatchlist) {
                        compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTVShowInWatchlist = false;
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                    Toast.makeText(getApplicationContext(), "Removed from watchlist.", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                })
                        );
                    } else {
                        compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                                    Toast.makeText(getApplicationContext(), "Added to watchlist.", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                })
                        );
                    }
                });
                activityTvshowDetailsBinding.imageWatchlist.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 0, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indecator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indecator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indecator_inactive));

            }
        }

    }

    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork()
                        + " (" + tvShow.getCountry() + ")"
        );
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailsBinding.setStartDate(tvShow.getStartDate());

        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStarted.setVisibility(View.VISIBLE);

    }
}