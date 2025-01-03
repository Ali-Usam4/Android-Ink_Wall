package com.example.inkwall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.inkwall.R;
import com.example.inkwall.model.WallpaperModel;

import java.util.List;
import java.util.Random;

//import com.google.firebase.database.core.Context;

//import com.google.firebase.database.core.Context;

public class SwiperAdapter extends RecyclerView.Adapter<SwiperAdapter.SwiperHolder> {

    public Context context;
    private List<WallpaperModel> list;

    public static OnDataPass dataPass;

    public SwiperAdapter(Context context, List<WallpaperModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_item,parent,false);
        return new SwiperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwiperHolder holder, int position) {

        Random random = new Random();
        int color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));

        holder.constraintLayout.setBackgroundColor(color);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImage())
                .timeout(6500)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }
                }).into(holder.imageView);
        // For Saving Image
        holder.clickListener(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SwiperHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        ImageButton saveBtn;
        Button setBtn;

        public SwiperHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.consLayout);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            setBtn = itemView.findViewById(R.id.setBtn);

        }


        // Button click listeners

        private void clickListener(int position){
            // Save Button Click Listner
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Convertion image to Bitmap
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    dataPass.onImageSave(position,bitmap);
                }
            });
            // Wallpaper apply Button
            setBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    dataPass.onImageSet(position,bitmap);
                }
            });
        }
    }
    // For savind Image
    public interface  OnDataPass{
        void onImageSave(int position, Bitmap bitmap);
        void onImageSet(int position, Bitmap bitmap);
    }
    public void OnDataPass(OnDataPass dataPass){
        SwiperAdapter.dataPass = dataPass;
    }
}
