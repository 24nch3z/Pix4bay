package ru.s4nchez.pix4bay.model.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S4nchez on 04.05.2018.
 */

public abstract class FilterList {

    protected List<FilterItem> mList;

    public FilterList() {
        mList = new ArrayList<>();
    }

    public String[] getLabels() {
        List<String> labels = new ArrayList<>();
        for (FilterItem item : mList) {
            labels.add(item.getLabel());
        }
        return labels.toArray(new String[]{});
    }

    public String getValueByLabel(String label) {
        for (FilterItem item : mList) {
            if (item.getLabel().equalsIgnoreCase(label)) {
                return item.getValue();
            }
        }
        return null;
    }

    public int getPositionByValue(String value) {
        FilterItem filterItem = getFilterByValue(value);
        return mList.indexOf(filterItem);
    }

    private FilterItem getFilterByValue(String value) {
        for (FilterItem item : mList) {
            if (item.getValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    public class FilterItem {

        private String mLabel;
        private String mValue;

        public FilterItem(String label, String value) {
            mLabel = label;
            mValue = value;
        }

        public String getLabel() {
            return mLabel;
        }

        public String getValue() {
            return mValue;
        }
    }
}