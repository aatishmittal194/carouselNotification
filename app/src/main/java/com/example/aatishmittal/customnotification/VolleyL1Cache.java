package com.example.aatishmittal.customnotification;


import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


public class VolleyL1Cache implements ImageLoader.ImageCache
{
    private static final String TAG = "VolleyL1Cache";

    private LruCache<String, Bitmap> mCache;

    private static VolleyL1Cache volleyCache = null;

    private VolleyL1Cache(Context context)
    {
        mCache = new LruCache<String, Bitmap>(calculateMemoryCacheSize(context))
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                int value = bitmap.getByteCount() / 1024 == 0 ? 1 : bitmap.getByteCount() / 1024;
                Log.v(TAG+"_count",String.valueOf(value));
                return value;
            }
        };
    }

    public static synchronized VolleyL1Cache getVolleyL1Cache(Context context) {
        if(volleyCache == null) {
            volleyCache = new VolleyL1Cache(context);
        }
        return volleyCache;
    }

    @Override
    public Bitmap getBitmap(String s)
    {
        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap)
    {
        mCache.put(s, bitmap);
    }

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = getService(context, Context.ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
        }
        // Target ~15% of the available heap.
        return ((1024 * memoryClass) / 7);
    }

    static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static class ActivityManagerHoneycomb {
        static int getLargeMemoryClass(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }

    }
}