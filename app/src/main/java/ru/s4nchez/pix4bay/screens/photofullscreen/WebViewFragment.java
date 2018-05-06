package ru.s4nchez.pix4bay.screens.photofullscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.model.PhotoItem;

/**
 * Created by S4nchez on 01.05.2018.
 */

public class WebViewFragment extends Fragment {

    private static final String ARG_PHOTO_ITEM = "ARG_PHOTO_ITEM";

    private PhotoItem mPhotoItem;

    public static WebViewFragment newInstance(PhotoItem photoItem) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_ITEM, photoItem);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        mPhotoItem = (PhotoItem) getArguments().getSerializable(ARG_PHOTO_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        WebView webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(50);
        webView.setPadding(0, 0, 0, 0);
        webView.loadUrl(mPhotoItem.getLargeImageURL());
        webView.setBackgroundColor(Color.BLACK);

        return view;
    }
}