package com.scaling_slider.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.scaling_slider.MCrypt;
import com.scaling_slider.R;
import com.scaling_slider.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    Activity activity;
    List<Product> image_arraylist;
    private LayoutInflater layoutInflater;
    MCrypt mcrypt;

    public SliderPagerAdapter(Activity activity, List<Product> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        mcrypt = new MCrypt();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        String imgUrl= null;
        try {
            imgUrl = mcrypt.Decrypt(image_arraylist.get(position).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Picasso.with(activity.getApplicationContext())
                .load("https://unnatiagro.in/stores/image/" + imgUrl)
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(im_slider);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}