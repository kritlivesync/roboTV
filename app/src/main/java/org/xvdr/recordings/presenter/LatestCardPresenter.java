package org.xvdr.recordings.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;

import org.xvdr.recordings.model.Movie;
import org.xvdr.robotv.R;

public class LatestCardPresenter extends CardPresenter {
    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        ViewHolder vh = (ViewHolder) viewHolder;
        ImageCardView cardView = vh.getCardView();
        Context context = cardView.getContext();

        String contextText = movie.getCategory() + (!movie.getDate().isEmpty() ? " / " + movie.getDate() : "");

        cardView.setTitleText(movie.getTitle());
        cardView.setContentText(contextText);
        cardView.setMainImageDimensions(266, 400);
        cardView.setInfoAreaBackgroundColor(context.getColor(R.color.recordings_fastlane_background));
        vh.updateCardViewImage(context, movie.getCardImageUrl());
    }

}
