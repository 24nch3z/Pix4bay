package ru.s4nchez.pix4bay.utils;

import android.graphics.Bitmap;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by S4nchez on 05.05.2018.
 */

/*
    Простой способ кэширования картинок
    В памяти хранится не больше 50 картинок (OutOfMemory не вылетит, т.к. картинки мало весят)
 */
public class BitmapCache {

    private static BitmapCache sBitmapCache;

    private static final int CAPACITY = 50;

    private int mOrderIndex = 0;

    private static ConcurrentMap<String, BitmapCacheItem> mItems;

    private BitmapCache() {
        mItems = new ConcurrentHashMap<>();
    }

    public static BitmapCache get() {
        if (sBitmapCache == null) {
            sBitmapCache = new BitmapCache();
        }
        return sBitmapCache;
    }

    public static void clear() {
        sBitmapCache = new BitmapCache();
    }

    public void add(Bitmap image, String url) {
        mItems.put(url, new BitmapCacheItem(url, image, getOrder()));
        if (mItems.size() > CAPACITY) {
            removeFirstImage();
        }
    }

    private void removeFirstImage() {
        Iterator<BitmapCacheItem> it = mItems.values().iterator();
        int minOrder = Integer.MAX_VALUE;
        String minOrderKey = null;

        while (it.hasNext()) {
            BitmapCacheItem item = it.next();
            if (item.getOrder() < minOrder) {
                minOrder = item.getOrder();
                minOrderKey = item.getUrl();
            }
        }

        if (minOrderKey != null) {
            mItems.remove(minOrderKey);
        }
    }

    private synchronized int getOrder() {
        return ++mOrderIndex;
    }

    public boolean contains(String url) {
        return mItems.containsKey(url);
    }

    public Bitmap getBitmapByUrl(String url) {
        return mItems.get(url).getImage();
    }

    private static class BitmapCacheItem {

        private String mUrl;
        private Bitmap mImage;
        private int mOrder;

        public BitmapCacheItem(String url, Bitmap image, int order) {
            mUrl = url;
            mImage = image;
            mOrder = order;
        }

        public String getUrl() {
            return mUrl;
        }

        public Bitmap getImage() {
            return mImage;
        }

        public int getOrder() {
            return mOrder;
        }
    }
}
