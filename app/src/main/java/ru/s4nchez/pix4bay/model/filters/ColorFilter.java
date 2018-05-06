package ru.s4nchez.pix4bay.model.filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class ColorFilter extends FilterList {

    public ColorFilter() {
        super();
        mList.add(new FilterItem("Все", Filters.EMPTY_VALUE));
        mList.add(new FilterItem("Прозрачный", "transparent"));
        mList.add(new FilterItem("Красный", "red"));
        mList.add(new FilterItem("Оранжевый", "orange"));
        mList.add(new FilterItem("Жёлтый", "yellow"));
        mList.add(new FilterItem("Зелёный", "green"));
        mList.add(new FilterItem("Бирюзовый", "turquoise"));
        mList.add(new FilterItem("Синий", "blue"));
        mList.add(new FilterItem("Сиреневый", "lilac"));
        mList.add(new FilterItem("Розовый", "pink"));
        mList.add(new FilterItem("Белый", "white"));
        mList.add(new FilterItem("Серый", "gray"));
        mList.add(new FilterItem("Чёрный", "black"));
        mList.add(new FilterItem("Коричневый", "brown"));
    }
}