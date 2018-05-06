package ru.s4nchez.pix4bay.screens.photofullscreen;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import ru.s4nchez.pix4bay.model.PhotoItem;

/**
 * Created by S4nchez on 02.05.2018.
 */

public class InformationDialogFragment extends DialogFragment {

    private static final String ARG_PHOTO_ITEM = "ARG_PHOTO_ITEM";

    public static InformationDialogFragment newInstance(PhotoItem photoItem) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_ITEM, photoItem);

        InformationDialogFragment fragment = new InformationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> message = new ArrayList<>();
        final PhotoItem photoItem = (PhotoItem) getArguments().getSerializable(ARG_PHOTO_ITEM);
        message.add("Выложил: " + photoItem.getUser());
        message.add("Теги: " + photoItem.getTagsString());
        message.add("Количество просмотров: " + photoItem.getViews());
        message.add("Количество лайков: " + photoItem.getLikes());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Информация о фотографии")
                .setMessage(TextUtils.join("\n", message))
                .setPositiveButton("Закрыть", null)
                .create();
    }
}