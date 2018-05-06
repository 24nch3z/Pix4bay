package ru.s4nchez.pix4bay.screens.photolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.SimpleFragmentActivity;
import ru.s4nchez.pix4bay.model.Engine;
import ru.s4nchez.pix4bay.utils.BitmapCache;

/**
 * Created by S4nchez on 10.04.2018.
 */

public class PhotosListActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return PhotosListFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Engine.clear();
        BitmapCache.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
