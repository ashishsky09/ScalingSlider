package com.scaling_slider.retrofit;

import com.scaling_slider.model.SliderResponse;

import retrofit2.Call;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("index.php?route=mpos/dashboard/slider")
    Call<SliderResponse> getSliderImages();


}
