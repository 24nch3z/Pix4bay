package ru.s4nchez.pix4bay.screens.photolist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.s4nchez.pix4bay.utils.BitmapCache;
import ru.s4nchez.pix4bay.utils.WebHelper;

/**
 * Created by S4nchez on 15.04.2018.
 */

public class ThumbnailDownloader<T, E> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private ConcurrentMap<T, Data> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T, E> mThumbnailDownloadListener;

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    handleRequest(target);
                }
            }
        };
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void handleRequest(final T target) {
        try {
            final Data data = mRequestMap.get(target);
            if (data == null) {
                return;
            }

            // Если изображение есть в кэше - отображаем сразу в элементе,
            // в противному случае делаем запрос на получение изображения
            Bitmap bitmap = null;
            if (BitmapCache.get().contains(data.getUrl())) {
                bitmap = BitmapCache.get().getBitmapByUrl(data.getUrl());
            } else {
                byte[] bitmapBytes = WebHelper.getUrlBytes(data.getUrl());
                bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                BitmapCache.get().add(bitmap, data.getUrl());
            }
            final Bitmap bufBitmap = bitmap.copy(bitmap.getConfig(), true);

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(target) != data || mHasQuit) {
                        return;
                    }

                    mRequestMap.remove(target);
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bufBitmap, (E) data.getData());
                }
            });
        } catch (IOException e) { }
    }

    public void queueThumbnail(T target, E extra, String url) {
        if (extra == null || url == null) {
            mRequestMap.remove(target);
        } else {
            Data data = new Data(extra, url);
            mRequestMap.put(target, data);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mResponseHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T, E> listener) {
        mThumbnailDownloadListener = listener;
    }

    public interface ThumbnailDownloadListener<T, E> {
        void onThumbnailDownloaded(T target, Bitmap image, E data);
    }

    private static class Data<E> {

        private E mData;
        private String mUrl;

        public Data(E data, String url) {
            mData = data;
            mUrl = url;
        }

        public E getData() {
            return mData;
        }

        public String getUrl() {
            return mUrl;
        }
    }
}