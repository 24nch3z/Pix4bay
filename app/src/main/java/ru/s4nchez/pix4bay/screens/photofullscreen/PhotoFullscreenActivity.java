package ru.s4nchez.pix4bay.screens.photofullscreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.model.PhotoItem;
import ru.s4nchez.pix4bay.SimpleFragmentActivity;

/**
 * Created by S4nchez on 15.04.2018.
 */

public class PhotoFullscreenActivity extends SimpleFragmentActivity {

    private static final String EXTRA_PHOTO_ITEM = "EXTRA_PHOTO_ITEM";

    @Override
    protected Fragment getFragment() {
        PhotoItem photoItem = (PhotoItem) getIntent().getSerializableExtra(EXTRA_PHOTO_ITEM);
        return PhotoFullscreenFragment.newInstance(photoItem);
    }

    public static Intent newInstance(Context context, PhotoItem photoItem) {
        Intent intent = new Intent(context, PhotoFullscreenActivity.class);
        intent.putExtra(EXTRA_PHOTO_ITEM, photoItem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Обработка нажатия стрелки в toolbar'e
        // т.к. при дефолтном поведении перезагружается первая активность (список фотографий)
        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
