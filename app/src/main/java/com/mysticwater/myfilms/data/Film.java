package com.mysticwater.myfilms.data;

import com.google.common.base.Objects;

import android.support.annotation.NonNull;

public final class Film {

    @NonNull
    private final String mId;

    @NonNull
    private final String mTitle;

    public Film(String id, String title) {
        this.mId = id;
        this.mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equal(mId, film.mId) &&
                Objects.equal(mTitle, film.mTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle);
    }

    @Override
    public String toString() {
        return "Film: " + mTitle;
    }

}