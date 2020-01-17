package com.scaling_slider;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.scaling_slider.adapter.SliderPagerAdapter;
import com.scaling_slider.model.Product;
import com.scaling_slider.model.SliderResponse;
import com.scaling_slider.retrofit.ApiClient;
import com.scaling_slider.retrofit.ApiInterface;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "Main Activity";

    List<Product> images;
    SliderPagerAdapter sliderPagerAdapter;
    int page_position = 0;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    private void init() {


        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<SliderResponse> call = apiService.getSliderImages();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                images = response.body().getProducts();
                Log.d(TAG, "Number of images received: " + images.size());
                sliderPagerAdapter = new SliderPagerAdapter(MainActivity.this, images);
                vp_slider.setAdapter(sliderPagerAdapter);

                vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        addBottomDots(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                addBottomDots(0);

                final Handler handler = new Handler();

                final Runnable update = new Runnable() {
                    public void run() {
                        if (page_position == images.size()) {
                            page_position = 0;
                        } else {
                            page_position = page_position + 1;
                        }
                        Animation sgAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in);
                        vp_slider.startAnimation(sgAnimation);
                        vp_slider.setCurrentItem(page_position, true);
                    }
                };

                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 100, 2000);


            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[images.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
}

