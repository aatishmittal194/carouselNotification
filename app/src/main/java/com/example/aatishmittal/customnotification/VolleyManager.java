package com.example.aatishmittal.customnotification;

import android.content.Context;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;

import javax.net.ssl.HttpsURLConnection;

public class VolleyManager
{
    private static final int DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024; // 5 mb


    private volatile static VolleyManager mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyManager(Context context)
    {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
        mImageLoader = new CustomImageLoader(mRequestQueue,
                VolleyL1Cache.getVolleyL1Cache(mCtx));
    }

    public static VolleyManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            synchronized (VolleyManager.class)
            {
                // Double check idiom
                if(mInstance == null)
                mInstance = new VolleyManager(context);
            }
        }
        return mInstance;
    }

    public static void destroy()
    {
        if (mInstance != null && mInstance.getRequestQueue() != null)
        {
            mInstance.getRequestQueue().cancelAll(mCtx);
        }
        mInstance = null;
    }

    private static RequestQueue newRequestQueue(Context context)
    {
        // define cache folder
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null)
        {
            rootCache = context.getCacheDir();
        }

        File cacheDir = rootCache;
        cacheDir.mkdirs();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
        RequestQueue queue = new RequestQueue(diskBasedCache, network);
        queue.start();

        return queue;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        if((req instanceof ImageRequest) && req.getRetryPolicy().getCurrentTimeout()==1000 ){
            req.setRetryPolicy(CustomImageLoader.getImageRetryPolicy());
        }
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }

    public void cancelAllRequestsForTag(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    static {
        HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3Factory());
    }

}
