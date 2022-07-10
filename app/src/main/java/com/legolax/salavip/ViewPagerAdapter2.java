package com.legolax.salavip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter2 extends RecyclerView.Adapter<ViewPagerAdapter2.PagerViewHolder> {

    private ArrayList<String> imagenItems;
    private ViewPager2 viewPager2;

    ViewPagerAdapter2(ArrayList<String> imagenItems, ViewPager2 viewPager2) {
        this.imagenItems = imagenItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_images,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
        holder.setImage (imagenItems.get(position));
    }

    @Override
    public int getItemCount() {
        return imagenItems.size();
    }

    class PagerViewHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;

             PagerViewHolder(@NonNull View itemView) {
                super(itemView);
                this.imageView = itemView.findViewById(R.id.imageView);
            }

            void setImage(String sliderItem){
                imageView.setImageURI(Uri.parse(sliderItem));
            }
        }
}
