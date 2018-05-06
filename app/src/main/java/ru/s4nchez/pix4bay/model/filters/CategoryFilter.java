package ru.s4nchez.pix4bay.model.filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class CategoryFilter extends FilterList {

    public CategoryFilter() {
        super();
        mList.add(new FilterItem("Все", Filters.EMPTY_VALUE));
        mList.add(new FilterItem("Мода", "fashion"));
        mList.add(new FilterItem("Природа", "nature"));
        mList.add(new FilterItem("Музыка", "music"));
        mList.add(new FilterItem("Бизнес", "business"));
        mList.add(new FilterItem("Сооружения", "buildings"));
        mList.add(new FilterItem("Путешествия", "travel"));
        mList.add(new FilterItem("Транспорт", "transportation"));
        mList.add(new FilterItem("Спорт", "sports"));
        mList.add(new FilterItem("Компьютеры", "computer"));
        mList.add(new FilterItem("Еда", "food"));
        mList.add(new FilterItem("Промышленность", "industry"));
        mList.add(new FilterItem("Животные", "animals"));
        mList.add(new FilterItem("Места", "places"));
        mList.add(new FilterItem("Здоровье", "health"));
        mList.add(new FilterItem("Религия", "religion"));
        mList.add(new FilterItem("Чувства", "feelings"));
        mList.add(new FilterItem("Люди", "people"));
        mList.add(new FilterItem("Обучение", "education"));
        mList.add(new FilterItem("Наука", "science"));
        mList.add(new FilterItem("Фоны", "backgrounds"));
    }
}