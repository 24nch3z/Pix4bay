package ru.s4nchez.pix4bay.model.filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class OrientationFilter extends FilterList {

    public OrientationFilter() {
        super();
        mList.add(new FilterItem("Все", Filters.EMPTY_VALUE));
        mList.add(new FilterItem("Альбомная", "horizontal"));
        mList.add(new FilterItem("Портретная", "vertical"));
    }
}