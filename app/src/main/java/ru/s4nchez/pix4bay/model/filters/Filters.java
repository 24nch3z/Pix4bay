package ru.s4nchez.pix4bay.model.filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class Filters {

    public static final String EMPTY_VALUE = "EMPTY_VALUE";

    private static FilterList sOrder;
    private static FilterList sCategory;
    private static FilterList sColor;
    private static FilterList sOrientation;

    public static void init() {
        sOrder = new OrderFilter();
        sCategory = new CategoryFilter();
        sColor = new ColorFilter();
        sOrientation = new OrientationFilter();
    }

    public static FilterList getOrder() {
        return sOrder;
    }

    public static FilterList getCategory() {
        return sCategory;
    }

    public static FilterList getColor() {
        return sColor;
    }

    public static FilterList getOrientation() {
        return sOrientation;
    }
}