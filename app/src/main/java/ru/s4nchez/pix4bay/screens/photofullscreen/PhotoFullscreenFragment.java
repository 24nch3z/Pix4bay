package ru.s4nchez.pix4bay.screens.photofullscreen;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.s4nchez.pix4bay.utils.Permissions;
import ru.s4nchez.pix4bay.model.PhotoItem;
import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.utils.MyToast;
import ru.s4nchez.pix4bay.utils.WebHelper;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by S4nchez on 17.04.2018.
 */

public class PhotoFullscreenFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_PHOTO_ITEM = "ARG_PHOTO_ITEM";
    private static final String SAVE_IS_DOWNLOADING = "SAVE_IS_DOWNLOADING";

    private PhotoItem mPhotoItem;
    private long mEnqueue;
    private DownloadManager mDownloadManager;
    private boolean mIsDownloading = false;

    public static PhotoFullscreenFragment newInstance(PhotoItem photoItem) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_ITEM, photoItem);
        PhotoFullscreenFragment fragment = new PhotoFullscreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        mPhotoItem = (PhotoItem) getArguments().getSerializable(ARG_PHOTO_ITEM);
        if (bundle != null) {
            mIsDownloading = bundle.getBoolean(SAVE_IS_DOWNLOADING, mIsDownloading);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_photo_fullsreen, container, false);
        initBroadcastReceiver();

        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frameWebView);
        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.frameWebView, WebViewFragment.newInstance(mPhotoItem))
                    .commit();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(SAVE_IS_DOWNLOADING, mIsDownloading);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                onClickDownload();
                return true;

            case R.id.share:
                onClickShare();
                return true;

            case R.id.information:
                showInformation();
                return true;

            case R.id.browser:
                openPageInBrowser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPageInBrowser() {
        String url = mPhotoItem.getPageURL();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(Intent.createChooser(intent, "Открыть в браузере"));
    }

    private void showInformation() {
        FragmentManager fm = getFragmentManager();
        InformationDialogFragment dialog = InformationDialogFragment.newInstance(mPhotoItem);
        dialog.show(fm, "INFORMATION_DIALOG");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE && Permissions.hasPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            downloadImage();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            return;
        }

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadImage();
                } else {
                    showPermissionSnackbar();
                }
                break;
        }
    }

    public void showPermissionSnackbar() {
        Snackbar.make(getActivity().findViewById(R.id.frame_main),
                "Для загрузки надо дать доступ к памяти приложению" , Snackbar.LENGTH_LONG)
                .setAction("НАСТРОЙКИ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
                    }
                })
                .show();
    }

    private void onClickShare() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mPhotoItem.getLargeImageURL());
        startActivity(Intent.createChooser(intent, "Поделиться"));
    }

    private void initBroadcastReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(mEnqueue);
                    Cursor c = mDownloadManager.query(query);

                    if (c.getCount() > 0) {
                        MyToast.get(getContext()).show("Файл успешно загружен");
                    }
                    mIsDownloading = false;
                }
            }
        };

        getContext().registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void onClickDownload() {
        if (!mIsDownloading) {
            if (Permissions.hasPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                 downloadImage();
            } else {
                requestPermission();
            }
        } else {
            MyToast.get(getContext()).show("Скачивание уже идёт");
        }
    }

    private void downloadImage() {
        if (!WebHelper.checkInternetConnected(getContext())) {
            MyToast.get(getContext()).show("Нет доступа к интернету");
            return;
        }

        mIsDownloading = true;
        MyToast.get(getContext()).show("Загрузка файла началась");

        mDownloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(mPhotoItem.getLargeImageURL()));
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                mPhotoItem.generateFileName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        mEnqueue = mDownloadManager.enqueue(request);
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions();

        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        Permissions.requestPermission(getActivity(), PERMISSION_REQUEST_CODE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}