package ru.s4nchez.pix4bay.screens.photolist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.s4nchez.pix4bay.R;
import ru.s4nchez.pix4bay.screens.photofullscreen.PhotoFullscreenActivity;
import ru.s4nchez.pix4bay.utils.ApiHelper;
import ru.s4nchez.pix4bay.OnBackPressedListener;
import ru.s4nchez.pix4bay.screens.photolist.filters.FiltersFragment;
import ru.s4nchez.pix4bay.model.PhotoItem;
import ru.s4nchez.pix4bay.model.Engine;
import ru.s4nchez.pix4bay.model.ResponseItem;
import ru.s4nchez.pix4bay.utils.MyToast;
import ru.s4nchez.pix4bay.utils.WebHelper;

import static ru.s4nchez.pix4bay.model.Engine.PAGE_SIZE;

/**
 * Created by S4nchez on 10.04.2018.
 */

public class PhotosListFragment extends Fragment implements OnBackPressedListener {

    private static final int SPAN_COUNT = 3;

    private PhotoAdapter mAdapter;
    private ThumbnailDownloader<PhotoHolder, PhotoItem> mThumbnailDownloader;
    private FetchItemsTask mFetchItemsTask;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTextViewNothingFound;
    private DrawerLayout mDrawerLayout;

    public static PhotosListFragment newInstance() {
        return new PhotosListFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        Handler handler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(handler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder, PhotoItem>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder target, Bitmap image, PhotoItem photoItem) {
//                        // Пришедшую картинку отрисовываем в элементе и добавляем в кэш
//                        BitmapCache.get().add(image, photoItem.getPreviewURL());
//                        Drawable drawable = new BitmapDrawable(getResources(), image);
//                        target.bind(photoItem, drawable);

                        Drawable drawable = new BitmapDrawable(getResources(), image);
                        target.bind(photoItem, drawable);
                    }
                });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_photos_list, container, false);
        initViews(view);
        initRecyclerView(view);
        updateItems(true);

        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.filters);
        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.filters, FiltersFragment.newInstance())
                    .commit();
        }

        return view;
    }

    private void initRecyclerView(View v) {
        mRecyclerView.addItemDecoration(new ItemDecoration(SPAN_COUNT, getResources()
                        .getDimensionPixelSize(R.dimen.recyclerViewSpacing)));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        setupAdapter();
    }

    private void initViews(View v) {
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mTextViewNothingFound = v.findViewById(R.id.textViewNothingFound);
        mProgressBar = v.findViewById(R.id.progressBar);
        mDrawerLayout = v.findViewById(R.id.drawerLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Engine.get().setSearch(s);
                updateItems(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = Engine.get().getSearch() != null ? Engine.get().getSearch() : "";
                searchView.setQuery(search, false);
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Если мы перестали вводить что-то в строку поиска и перед этим удалили строку,
                // выдавать результат без строки поиска
                if (!b && ((SearchView) view).getQuery().length() == 0 &&
                        Engine.get().getSearch() != null && Engine.get().getSearch().length() != 0) {
                    Engine.get().setSearch(null);
                    updateItems(true);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_filters:
                toggleFilters();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleFilters() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Реализация ленивой загрузки
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager())
                    .findFirstVisibleItemPosition();

            if (!Engine.get().isLoading() && !Engine.get().isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    Engine.get().incrementCurrentPage();
                    updateItems(false);
                }
            }
        }
    };

    private void setupAdapter() {
        if (isAdded()) {
            if (mAdapter == null) {
                mAdapter = new PhotoAdapter(Engine.get().getPhotoItems());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                // Обновление адаптера (чтобы обновлять только новые картинки)
                // если обновлять все, то загрузка всех изображений пойдёт заново
                int[] range = Engine.get().getRangeOfLastPage();
                int lastCountOfPhotoItems = Engine.get().getLastCountOfPhotoItems();
                Engine.get().saveLastCountOfPhotoItems();

                // Если новый список короче старого, то нужно оповестить адаптер о том,
                // что были удалены элементы, иначе этот гад выплюнет exception
                if (lastCountOfPhotoItems > Engine.get().getPhotoItems().size()) {
                    mAdapter.notifyItemRangeRemoved(0, lastCountOfPhotoItems);
                }
                mAdapter.notifyItemRangeChanged(range[0], range[1]);
            }

            if (Engine.get().isNothingFound()) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mTextViewNothingFound.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mTextViewNothingFound.setVisibility(View.INVISIBLE);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateItems(boolean isNewQuery) {
        if (!WebHelper.checkInternetConnected(getContext())) {
            MyToast.get(getContext()).show("Нет доступа к интернету");
            return;
        }

        if (isNewQuery) {
            // Если новый запрос (поставлен новый фильтр или поиск)
            // подготавливаем объект Engine для нового запроса и убиваем очередь
            // загружаемых изображений
            Engine.get().prepareBeforeNewQuery();
            mRecyclerView.setVisibility(View.INVISIBLE);
            mThumbnailDownloader.clearQueue();
        }

        String url = ApiHelper.getUrl(Engine.get());
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewNothingFound.setVisibility(View.INVISIBLE);

        if (mFetchItemsTask != null) {
            mFetchItemsTask.cancel(true);
        }
        mFetchItemsTask = new FetchItemsTask(url, isNewQuery);
        mFetchItemsTask.execute();
    }

    public void confirmFilters() {
        updateItems(true);
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        getActivity().finish();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PhotoItem mPhotoItem;
        private ImageView mImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(PhotoItem photoItem, Drawable drawable) {
            if (photoItem != null) {
                mPhotoItem = photoItem;
            }
            mImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View view) {
            Intent intent = PhotoFullscreenActivity.newInstance(getContext(), mPhotoItem);
            startActivity(intent);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<PhotoItem> mItems;

        public PhotoAdapter(List<PhotoItem> items) {
            mItems = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_photo, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            PhotoItem photoItem = mItems.get(position);
            Drawable drawable = getResources().getDrawable(R.drawable.empty);
            holder.bind(photoItem, drawable);
            mThumbnailDownloader.queueThumbnail(holder, photoItem, photoItem.getPreviewURL());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ResponseItem> {

        private String mQuery;
        private boolean mIsNewQuery;

        public FetchItemsTask(String query, boolean isNewQuery) {
            Engine.get().setLoading(true);
            mQuery = query;
            mIsNewQuery = isNewQuery;
        }

        @Override
        protected ResponseItem doInBackground(Void... voids) {
            return ApiHelper.fetchPhotos(mQuery);
        }

        @Override
        protected void onPostExecute(ResponseItem response) {
            Engine.get().setLoading(false);
            Engine.get().setData(mIsNewQuery, response);
            setupAdapter();
        }
    }
}