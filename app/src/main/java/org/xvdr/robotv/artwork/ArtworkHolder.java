package org.xvdr.robotv.artwork;

public class ArtworkHolder {

    static final String TAG = "ArtworkHolder";

    protected String mPosterUrl;
    protected String mBackgroundUrl;
    protected String mTitle;

    public ArtworkHolder(String posterUrl, String backgroundUrl) {
        mPosterUrl = posterUrl;
        mBackgroundUrl = backgroundUrl;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackgroundUrl() {
        return mBackgroundUrl;
    }

    public boolean hasPoster() {
        return !mPosterUrl.isEmpty();
    }

    public boolean hasBackground() {
        return !mBackgroundUrl.isEmpty();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
