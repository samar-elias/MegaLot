package com.hudhud.megalot.Views.Registration.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.hudhud.megalot.AppUtils.Responses.Intro;
import com.hudhud.megalot.AppUtils.Urls;
import com.hudhud.megalot.R;
import com.hudhud.megalot.Views.Registration.RegistrationActivity;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private final ArrayList<Intro> contents;
    private final RegistrationActivity registrationActivity;

    public ViewPagerAdapter(ArrayList<Intro> contents, RegistrationActivity registrationActivity) {
        this.contents = contents;
        this.registrationActivity = registrationActivity;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup  container, int position) {

        LayoutInflater inflater = (LayoutInflater) registrationActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.intro_layout,container,false);
        container.addView(view);

        Intro introItem = contents.get(position);

        ImageView sliderImage = view.findViewById(R.id.slider_img);
        TextView sliderTitle = view.findViewById(R.id.slider_title);
        TextView sliderDescription = view.findViewById(R.id.slider_description);

        Glide.with(registrationActivity).load(introItem.getImage()).into(sliderImage);
        sliderTitle.setText(introItem.getTitle());
        sliderDescription.setText(introItem.getDescription());

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
