package ru.s4nchez.pix4bay.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by S4nchez on 10.04.2018.
 */

/*
    Доки по API: https://pixabay.com/api/docs/#api_error_handling
 */
public class PhotoItem implements Serializable {

    private String mLargeImageURL;
    private int mWebformatHeight;
    private int mWebformatWidth;
    private int mLikes;
    private int mImageWidth;
    private int mId;
    private int mUserId;
    private int mViews;
    private int mComments;
    private String mPageURL;
    private int mImageHeight;
    private String mWebformatURL;
    private String mType;
    private int mPreviewHeight;
    private List<String> mTags;
    private int mDownloads;
    private String mUser;
    private int mFavorites;
    private int mImageSize;
    private int mPreviewWidth;
    private String mUserImageURL;
    private String mPreviewURL;

    public String getLargeImageURL() {
        return mLargeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        mLargeImageURL = largeImageURL;
    }

    public int getWebformatHeight() {
        return mWebformatHeight;
    }

    public void setWebformatHeight(int webformatHeight) {
        mWebformatHeight = webformatHeight;
    }

    public int getWebformatWidth() {
        return mWebformatWidth;
    }

    public void setWebformatWidth(int webformatWidth) {
        mWebformatWidth = webformatWidth;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getViews() {
        return mViews;
    }

    public void setViews(int views) {
        mViews = views;
    }

    public int getComments() {
        return mComments;
    }

    public void setComments(int comments) {
        mComments = comments;
    }

    public String getPageURL() {
        return mPageURL;
    }

    public void setPageURL(String pageURL) {
        mPageURL = pageURL;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        mImageHeight = imageHeight;
    }

    public String getWebformatURL() {
        return mWebformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        mWebformatURL = webformatURL;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        mPreviewHeight = previewHeight;
    }

    public List<String> getTags() {
        return mTags;
    }

    public String getTagsString() {
        return TextUtils.join(", ", getTags());
    }

    public void setTags(String tags) {
        mTags = Arrays.asList(tags.split(", "));
    }

    public int getDownloads() {
        return mDownloads;
    }

    public void setDownloads(int downloads) {
        mDownloads = downloads;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public int getFavorites() {
        return mFavorites;
    }

    public void setFavorites(int favorites) {
        mFavorites = favorites;
    }

    public int getImageSize() {
        return mImageSize;
    }

    public void setImageSize(int imageSize) {
        mImageSize = imageSize;
    }

    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        mPreviewWidth = previewWidth;
    }

    public String getUserImageURL() {
        return mUserImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        mUserImageURL = userImageURL;
    }

    public String getPreviewURL() {
        return mPreviewURL;
    }

    public void setPreviewURL(String previewURL) {
        mPreviewURL = previewURL;
    }

    public String generateFileName() {
        return "pixabay.com-" + "-" + getId() + ".jpg";
    }
}