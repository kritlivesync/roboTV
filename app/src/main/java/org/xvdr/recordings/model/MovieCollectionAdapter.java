package org.xvdr.recordings.model;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.util.ArrayMap;

import org.xvdr.recordings.presenter.CardPresenter;
import org.xvdr.recordings.presenter.LatestCardPresenter;

import java.util.Comparator;

public class MovieCollectionAdapter extends ArrayObjectAdapter {

    private CardPresenter mCardPresenter;
    private LatestCardPresenter mLatestCardPresenter;
    private ArrayMap<String, ListRow> mSeriesMap;
    private ArrayObjectAdapter mLatest;
    private ArrayObjectAdapter mTvShows;

    static public Comparator<Movie> compareTimestamps = new Comparator<Movie>() {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return lhs.getTimeStamp() > rhs.getTimeStamp() ? -1 : 1;
        }
    };


    public MovieCollectionAdapter() {
        super(new ListRowPresenter());
        mCardPresenter = new CardPresenter();
        mLatestCardPresenter = new LatestCardPresenter();

        mSeriesMap = new ArrayMap<>();

        mLatest = getCategory("Latest", true, mLatestCardPresenter); // 0
        mTvShows = getCategory("TV Shows", true, mCardPresenter); // 1
    }

    private Movie movieExists(ArrayObjectAdapter adapter, Movie movie) {
        for(int i = 0; i < adapter.size(); i++) {
            Movie item = (Movie) adapter.get(i);

            if(item.getId().equals(movie.getId())) {
                return item;
            }
        }

        return null;
    }

    public ArrayObjectAdapter getCategory(Movie movie) {
        return getCategory(movie.getCategory());
    }

    private ArrayObjectAdapter getCategory(String category) {
        return getCategory(category, false);
    }

    private ArrayObjectAdapter getCategory(String category, boolean addNew) {
        return getCategory(category, addNew, mCardPresenter);
    }

    private ArrayObjectAdapter getCategory(String category, boolean addNew, Presenter presenter) {
        ListRow listrow;

        for(int i = 0; i < size(); i++) {
            listrow = (ListRow)get(i);

            if(listrow.getHeaderItem().getName().equalsIgnoreCase(category)) {
                return (ArrayObjectAdapter) listrow.getAdapter();
            }
        }

        if(!addNew) {
            return null;
        }

        HeaderItem header = new HeaderItem(size() - 1, category);
        ArrayObjectAdapter listRowAdapter = new SortedArrayObjectAdapter(compareTimestamps, presenter);

        listrow = new ListRow(header, listRowAdapter);
        add(listrow);

        return listRowAdapter;
    }

    public ObjectAdapter getSeries(String title) {
        return getSeries(title, "", false);
    }

    private ObjectAdapter getSeries(String title, String url, boolean addNew) {
        ArrayObjectAdapter row = getCategory("TV Shows", true);

        if(row == null) {
            return null;
        }

        // get adapter of series
        ListRow seriesRow = mSeriesMap.get(title);

        if(seriesRow != null) {
            return seriesRow.getAdapter();
        }

        if(!addNew) {
            return null;
        }

        // create a new one for this series
        Movie series = new Movie();
        series.setTitle(title);
        series.setContent(0x15);
        series.setCardImageUrl(url);
        series.setSeriesHeader();

        row.add(series);

        // create a new adapter for this series
        ArrayObjectAdapter seriesRowAdapter = new SortedArrayObjectAdapter(compareTimestamps, mCardPresenter);
        ListRow listRow = new ListRow(new HeaderItem("Show: " + title), seriesRowAdapter);

        if(mSeriesMap.isEmpty()) {
            add(2, listRow);
        }

        mSeriesMap.put(title, listRow);

        return seriesRowAdapter;
    }

    public ArrayObjectAdapter add(Movie movie) {
        // add into "latest" category
        Movie item = movieExists(mLatest, movie);

        if(item != null) {
            item.setArtwork(movie);
        }
        else {
            mLatest.add(movie);
        }

        if(movie.isSeries()) {
            return addSeriesEpisode(movie);
        }

        return addMovie(movie);
    }

    protected ArrayObjectAdapter addMovie(Movie movie) {
        ArrayObjectAdapter row = getCategory(movie.getCategory(), true);

        if(row == null) {
            return null;
        }

        Movie item = movieExists(row, movie);

        if(item != null) {
            item.setArtwork(movie);
        }
        else {
            row.add(movie);
        }

        return row;
    }

    protected ArrayObjectAdapter addSeriesEpisode(Movie episode) {
        ArrayObjectAdapter seriesRow = (ArrayObjectAdapter) getSeries(episode.getTitle(), episode.getCardImageUrl(), true);

        if(seriesRow == null) {
            return null;
        }

        Movie item = movieExists(seriesRow, episode);

        if(item != null) {
            item.setArtwork(episode);
        }
        else {
            seriesRow.add(episode);
        }

        return seriesRow;
    }

    public void remove(Movie movie) {
        ArrayObjectAdapter row = getCategory(movie.getCategory(), true);

        if(row == null) {
            return;
        }

        row.remove(movie);
    }

    public void setSeriesRow(String title) {
        ListRow row = mSeriesMap.get(title);
        replace(2, row);
        notifyArrayItemRangeChanged(2, 1);
    }

    public void cleanup() {
        if(mTvShows.size() == 0) {
            removeItems(1, 1);
        }
    }
}
