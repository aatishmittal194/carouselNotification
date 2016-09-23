package com.example.aatishmittal.customnotification;

import android.util.Log;

import java.io.File;


public class DiskBasedCache extends com.android.volley.toolbox.DiskBasedCache {

    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);
    }

    @Override
    public synchronized Entry get(String key) {
        try {
            Entry entry = super.get(key);
            return entry;
        } catch (NegativeArraySizeException e) {
            Log.d("DiskBasedCache","Error in getting cached response"+e);
            remove(key);
            return null;
        }

    }

}
