package com.example.aatishmittal.customnotification;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;


public class CustomImageLoader extends ImageLoader {

    private static final int MAX_RETRY = 1; //Total Attempts is 2.
    private static final int INITIAL_TIMEOUT = 5000; //5secs.
    private static final float BACK_OFF_MULTIPLIER = 2.0F;// 5s, 15s

    public CustomImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, String cacheKey) {
        Request<Bitmap> imageRequest = super.makeImageRequest(requestUrl, maxWidth, maxHeight, scaleType, cacheKey);
        imageRequest.setRetryPolicy(getImageRetryPolicy());
        return imageRequest;
    }

    public static RetryPolicy getImageRetryPolicy(){
        return new DefaultRetryPolicy(INITIAL_TIMEOUT, MAX_RETRY, BACK_OFF_MULTIPLIER);
    }
}
