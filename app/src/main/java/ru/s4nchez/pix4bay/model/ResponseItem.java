package ru.s4nchez.pix4bay.model;

import java.util.List;

/**
 * Created by S4nchez on 22.04.2018.
 */

public class ResponseItem {

    private int mTotalHits;
    private int mTotal;
    private List<PhotoItem> mItems;

    public int getTotalHits() {
        return mTotalHits;
    }

    public void setTotalHits(int totalHits) {
        mTotalHits = totalHits;
    }

    public List<PhotoItem> getItems() {
        return mItems;
    }

    public void setItems(List<PhotoItem> items) {
        mItems = items;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }
}
