package com.axiagroups.tvshowapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.axiagroups.tvshowapplication.R;
import com.axiagroups.tvshowapplication.databinding.ItemContainerSliderImageBinding;
import com.axiagroups.tvshowapplication.databinding.ItemContainerTvShowBinding;

/**
 * Created by Ahsan Habib on 5/4/2024.
 * shehanuk.ahsan@gmail.com
 */
public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    private String[] sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderImageBinding sliderImageBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_image, parent, false
        );
        return new ImageSliderViewHolder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerSliderImageBinding itemContainerSliderImageBinding;

        public ImageSliderViewHolder(ItemContainerSliderImageBinding itemContainerSliderImageBinding) {
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;

        }

        public void bindSliderImage(String imageURL) {
            itemContainerSliderImageBinding.setImageURL(imageURL);
        }
    }
}
