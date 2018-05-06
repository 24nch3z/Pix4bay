package ru.s4nchez.pix4bay.model.filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class OrderFilter extends FilterList {

    public OrderFilter() {
        super();
        mList.add(new FilterItem("Новые", "latest"));
        mList.add(new FilterItem("По популярности", "popular"));
    }
}