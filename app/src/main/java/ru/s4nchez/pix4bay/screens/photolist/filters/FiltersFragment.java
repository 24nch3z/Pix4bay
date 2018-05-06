package ru.s4nchez.pix4bay.screens.photolist.filters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import ru.s4nchez.pix4bay.screens.photolist.PhotosListFragment;
import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.model.Engine;
import ru.s4nchez.pix4bay.model.filters.FilterList;
import ru.s4nchez.pix4bay.model.filters.Filters;

/**
 * Created by S4nchez on 04.05.2018.
 */

public class FiltersFragment extends Fragment {

    private Spinner mSpinnerOrder;
    private Spinner mSpinnerCategory;
    private Spinner mSpinnerColor;
    private Spinner mSpinnerOrientation;
    private CheckBox mCheckBoxSafeSearch;
    private Button mButtonConfirm;

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        mButtonConfirm = v.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Engine.get().setOrder(Filters.getOrder().getValueByLabel(
                        mSpinnerOrder.getSelectedItem().toString()));

                Engine.get().setCategory(Filters.getCategory().getValueByLabel(
                        mSpinnerCategory.getSelectedItem().toString()));

                Engine.get().setColor(Filters.getColor().getValueByLabel(
                        mSpinnerColor.getSelectedItem().toString()));

                Engine.get().setOrientation(Filters.getOrientation().getValueByLabel(
                        mSpinnerOrientation.getSelectedItem().toString()));

                Engine.get().setSafeSearch(mCheckBoxSafeSearch.isChecked());

                ((PhotosListFragment) getParentFragment()).confirmFilters();
            }
        });

        initSpinnerOrder(v);
        initSpinnerCategory(v);
        initSpinnerColor(v);
        initSpinnerOrientation(v);
        initCheckBoxSafeSearch(v);
    }

    private void initSpinnerOrder(View v) {
        mSpinnerOrder = v.findViewById(R.id.spinner_order);
        initSpinner(mSpinnerOrder, Filters.getOrder(), Engine.get().getOrder());
    }

    private void initSpinnerCategory(View v) {
        mSpinnerCategory = v.findViewById(R.id.spinner_category);
        initSpinner(mSpinnerCategory, Filters.getCategory(), Engine.get().getCategory());
    }

    private void initSpinnerColor(View v) {
        mSpinnerColor = v.findViewById(R.id.spinner_color);
        initSpinner(mSpinnerColor, Filters.getColor(), Engine.get().getColor());
    }

    private void initSpinnerOrientation(View v) {
        mSpinnerOrientation = v.findViewById(R.id.spinner_orientation);
        initSpinner(mSpinnerOrientation, Filters.getOrientation(), Engine.get().getOrientation());
    }

    private void initSpinner(Spinner spinner, FilterList filter, String value) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, filter.getLabels());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (value != null) {
            int selection = filter.getPositionByValue(value);
            spinner.setSelection(selection);
        }
    }

    private void initCheckBoxSafeSearch(View v) {
         mCheckBoxSafeSearch = v.findViewById(R.id.checkbox_safe_search);
         mCheckBoxSafeSearch.setChecked(Engine.get().isSafeSearch());
    }
}