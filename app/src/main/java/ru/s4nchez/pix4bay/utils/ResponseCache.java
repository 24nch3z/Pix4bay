package ru.s4nchez.pix4bay.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by S4nchez on 25.04.2018.
 */

// Данные по запросу хранятся минуту,
// если прошло больше минуты, то данные из кэша удаляются и делается запрос по сети
public class ResponseCache {

    private static ResponseCache sResponseCache;

    private static final long LIMIT = 60000; // 1 минута

    private static ConcurrentMap<String, CacheItem> sResponses;

    private ResponseCache() {
        sResponses = new ConcurrentHashMap<>();
    }

    public static ResponseCache get() {
        if (sResponseCache == null) {
            sResponseCache = new ResponseCache();
        }
        return sResponseCache;
    }

    public void addResponse(String url, String response) {
        CacheItem cacheItem = new CacheItem(response);
        sResponses.put(url, cacheItem);
    }

    public String getResponse(String url) {
        return sResponses.get(url).getResponse();
    }

    public boolean checkTime(String url) {
        CacheItem cacheItem = sResponses.get(url);
        Date now = new Date();
        return cacheItem.getDate().getTime() + LIMIT > now.getTime();
    }

    // При каждом запросе перед выдачей результата проводится очистка кэша от старых записей
    public boolean canGetResponse(String url) {
        clearCache();
        return containsResponse(url);
    }

    private void clearCache() {
        Iterator<String> it = sResponses.keySet().iterator();
        while (it.hasNext()) {
            String url = it.next();
            if (!checkTime(url)) {
                it.remove();
            }
        }
    }

    public boolean containsResponse(String url) {
        return sResponses.containsKey(url);
    }

    public static class CacheItem {

        private Date mDate;
        private String mResponse;

        public CacheItem(String response) {
            mDate = new Date();
            mResponse = response;
        }

        public Date getDate() {
            return mDate;
        }

        public String getResponse() {
            return mResponse;
        }
    }
}