package ru.s4nchez.pix4bay.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.s4nchez.pix4bay.model.Engine;
import ru.s4nchez.pix4bay.model.PhotoItem;
import ru.s4nchez.pix4bay.model.ResponseItem;

import static ru.s4nchez.pix4bay.Keys.API_KEY;
import static ru.s4nchez.pix4bay.model.filters.Filters.EMPTY_VALUE;

/**
 * Created by S4nchez on 10.04.2018.
 */

public class ApiHelper {

    public static ResponseItem fetchPhotos(String url) {
        return downloadItems(url);
    }

    public static String getUrl(Engine engine) {
        Uri.Builder uriBuilder = Uri.parse("https://pixabay.com/api/")
                .buildUpon()
                .appendQueryParameter("key", API_KEY);

        uriBuilder.appendQueryParameter("page", String.valueOf(engine.getCurrentPage()));
        uriBuilder.appendQueryParameter("per_page", String.valueOf(Engine.PAGE_SIZE));
        appendParamIfExist(uriBuilder, "order", engine.getOrder(), "latest");
        appendParamIfExist(uriBuilder, "category", engine.getCategory(), null);
        appendParamIfExist(uriBuilder, "colors", engine.getColor(), null);
        appendParamIfExist(uriBuilder, "orientation", engine.getOrientation(), "all");
        appendParamIfExist(uriBuilder, "safesearch", engine.isSafeSearch() ? "true" : "false", null);
        appendParamIfExist(uriBuilder, "q", engine.getSearch(), null);

        Log.l(uriBuilder.build().toString());
        return uriBuilder.build().toString();
    }

    // Добавление необязательных парамов к запросу
    private static void appendParamIfExist(Uri.Builder uriBuilder, String name,
                                    String value, String defaultValue) {
        if (value != null && !value.equalsIgnoreCase(EMPTY_VALUE)) {
            uriBuilder.appendQueryParameter(name, value);
        } else if (defaultValue != null) {
            uriBuilder.appendQueryParameter(name, defaultValue);
        }
    }

    private static ResponseItem downloadItems(String url) {
        ResponseItem response = null;
        try {
            String jsonString;
            if (ResponseCache.get().canGetResponse(url)) {
                jsonString = ResponseCache.get().getResponse(url);
            } else {
                jsonString = WebHelper.getUrlString(url);
                ResponseCache.get().addResponse(url, jsonString);
            }
            response = parseResponse(jsonString);
        } catch (IOException ioe) {
            Log.l(ioe);
        } catch (JSONException e) {
            Log.l(e);
        } finally {
            return response;
        }
    }

    private static ResponseItem parseResponse(String jsonString) throws JSONException, IOException {
        ResponseItem responseItem = new ResponseItem();
        JSONObject jsonBody = new JSONObject(jsonString);
        responseItem.setTotalHits(jsonBody.getInt("totalHits"));
        responseItem.setTotal(jsonBody.getInt("total"));
        responseItem.setItems(parseItems(jsonBody.getJSONArray("hits")));
        return responseItem;
    }

    private static List<PhotoItem> parseItems(JSONArray photoJsonArray)
            throws IOException, JSONException {
        List<PhotoItem> items = new ArrayList<>();
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject jsonObj = photoJsonArray.getJSONObject(i);
            items.add(getPhotoItem(jsonObj));
        }
        return items;
    }

    private static PhotoItem getPhotoItem(JSONObject jsonObj) throws JSONException {
        PhotoItem photoItem = new PhotoItem();
        photoItem.setId(jsonObj.getInt("id"));
        photoItem.setLargeImageURL(jsonObj.getString("largeImageURL"));
        photoItem.setLikes(jsonObj.getInt("likes"));
        photoItem.setViews(jsonObj.getInt("views"));
        photoItem.setPageURL(jsonObj.getString("pageURL"));
        photoItem.setWebformatURL(jsonObj.getString("webformatURL"));
        photoItem.setTags(jsonObj.getString("tags"));
        photoItem.setDownloads(jsonObj.getInt("downloads"));
        photoItem.setUser(jsonObj.getString("user"));
        photoItem.setUserImageURL(jsonObj.getString("userImageURL"));
        photoItem.setPreviewURL(jsonObj.getString("previewURL"));
        return photoItem;
    }
}
