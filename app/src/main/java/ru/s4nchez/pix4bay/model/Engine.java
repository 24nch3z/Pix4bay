package ru.s4nchez.pix4bay.model;

import java.util.ArrayList;
import java.util.List;

import ru.s4nchez.pix4bay.model.filters.Filters;

/**
 * Created by S4nchez on 17.04.2018.
 */

public class Engine {

    private static Engine mEngine;

    public static final int PAGE_SIZE = 90;

    private List<PhotoItem> mItems = new ArrayList<>();
    private int mTotalHits;
    private int mTotal;

    private String mSearch;
    private String mOrder;
    private String mCategory;
    private String mColor;
    private String mOrientation;
    private boolean mIsSafeSearch = true;

    private boolean mIsLastPage = false;
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private int mLastCountOfPhotoItems = 0;

    private Engine() {
        Filters.init();
    }

    public static Engine get() {
        if (mEngine == null) {
            mEngine = new Engine();
        }
        return mEngine;
    }

    public static void clear() {
        mEngine = new Engine();
    }

    public void setData(boolean isNewQuery, ResponseItem response) {
        if (response != null) {
            setTotal(response.getTotal());
            setTotalHits(response.getTotalHits());
            if (isNewQuery) {
                setPhotoItems(response.getItems());
            } else {
                appendItems(response.getItems());
            }
            initLastPageByData();
            return;
        }

        setTotal(0);
        setTotalHits(0);
        mItems.clear();
        setLastPage(true);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void incrementCurrentPage() {
        mCurrentPage++;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }

    public List<PhotoItem> getPhotoItems() {
        return mItems;
    }

    private void setPhotoItems(List<PhotoItem> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    private void appendItems(List<PhotoItem> items) {
        mItems.addAll(items);
    }

    public int[] getRangeOfLastPage() {
        return new int[] { (mCurrentPage - 1) * PAGE_SIZE, mItems.size() };
    }

    public int getTotalHits() {
        return mTotalHits;
    }

    public int getTotal() {
        return mTotal;
    }

    private void setTotalHits(int totalHits) {
        mTotalHits = totalHits;
    }

    private void setTotal(int total) {
        mTotal = total;
    }

    public String getSearch() {
        return mSearch;
    }

    public void setSearch(String search) {
        mSearch = search;
    }

    public boolean isLastPage() {
        return mIsLastPage;
    }

    public void setLastPage(boolean lastPage) {
        mIsLastPage = lastPage;
    }

    private void initLastPageByData() {
        mIsLastPage = mItems.size() >= mTotalHits;
    }

    public void prepareBeforeNewQuery() {
        mItems.clear();
        mCurrentPage = 1;
    }

    public boolean isNothingFound() {
        return !isLoading() && mItems.size() == 0 && getTotalHits() == 0;
    }

    public int getLastCountOfPhotoItems() {
        return mLastCountOfPhotoItems;
    }

    public void saveLastCountOfPhotoItems() {
        mLastCountOfPhotoItems = getPhotoItems().size();
    }

    public String getOrder() {
        return mOrder;
    }

    public void setOrder(String order) {
        mOrder = order;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getOrientation() {
        return mOrientation;
    }

    public void setOrientation(String orientation) {
        mOrientation = orientation;
    }

    public boolean isSafeSearch() {
        return mIsSafeSearch;
    }

    public void setSafeSearch(boolean safeSearch) {
        mIsSafeSearch = safeSearch;
    }
}