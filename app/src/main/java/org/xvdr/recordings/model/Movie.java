package org.xvdr.recordings.model;

import org.xvdr.robotv.artwork.ArtworkHolder;
import org.xvdr.robotv.artwork.Event;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie implements Serializable {
    private String title;
    private String description;
    private String outline;
    private String category;
    private String cardImageUrl;
    private String backgroundImageUrl;
    private long timeStamp;
    private int duration;
    private String id;
    private int content;
    private String channelName;
    private int channelUid;
    private boolean isSeriesHeader = false;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        String parts[] = outline.split("/");

        if(parts.length == 0) {
            this.outline = "";
            return;
        }

        outline = parts[0];
        this.outline = outline.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getDurationMs() {
        return duration * 1000;
    }

    public long getDuration() {
        return duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public int getContent() {
        return content;
    }

    public int getGenreType() {
        return content & 0xF0;
    }

    public int getGenreSubType() {
        return content & 0x0F;
    }

    public boolean isSeries() {
        content = Event.guessGenreFromSubTitle(content, outline, duration);
        return (content == 0x15);
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelUid(int channelUid) {
        this.channelUid = channelUid;
    }

    public int getChannelUid() {
        return channelUid;
    }

    public String getDate() {
        try {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate).trim();
        }
        catch(Exception e) {
            return "";
        }
    }

    public String getDateTime() {
        try {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate).trim();
        }
        catch(Exception e) {
            return "";
        }
    }

    public Event getEvent() {
        if(content == 0 || (content & 0x50) == 0x50) {
            content = 0x10;
        }

        return new Event(content, title, outline, description, duration);
    }

    public void setSeriesHeader() {
        isSeriesHeader = true;
    }

    public boolean isSeriesHeader() {
        return isSeriesHeader;
    }

    public void setArtwork(ArtworkHolder artwork) {
        if(artwork == null) {
            return;
        }

        cardImageUrl = artwork.getPosterUrl();
        backgroundImageUrl = artwork.getBackgroundUrl();
    }

    public void setArtwork(Movie movie) {
        cardImageUrl = movie.getCardImageUrl();
        backgroundImageUrl = movie.getBackgroundImageUrl();
    }

    @Override
    public String toString() {
        return "Movie {" +
               "title=\'" + title + "\'" +
               ", description=\'" + description + "\'" +
               ", outline=\'" + outline + "\'" +
               ", category=\'" + category + "\'" +
               ", cardImageUrl=\'" + cardImageUrl + "\'" +
               ", backgroundImageUrl=\'" + backgroundImageUrl + "\'" +
               "}";
    }

    public void setStartTime(long startTime) {
        this.timeStamp = startTime * 1000;
    }

    public long getStartTime() {
        return timeStamp / 1000;
    }
}
