package com.mysticwater.myfilms.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysticwater.myfilms.FilmDetailActivity;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static com.mysticwater.myfilms.fragments.FilmDetailFragment.FILM_ID;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmHolder> {

    private RecyclerView mRecyclerView;
    private LayoutInflater inflater;
    private Context mContext;
    private List<Film> filmsList = Collections.emptyList();

    public FilmAdapter(Context context, List<Film> films) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        filmsList = films;
    }

    @Override
    public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_upcoming_film, parent, false);
        v.setOnClickListener(filmOnClickListener);
        FilmHolder filmHolder = new FilmHolder(v);

        return filmHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(FilmHolder holder, int position) {
        Film film = filmsList.get(position);

        String posterPath = film.getPosterPath();
        if (!TextUtils.isEmpty(posterPath)) {
            String imageUri = mContext.getString(R.string.moviedb_image_w500_url, posterPath);

            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.indicatorsEnabled(true);

            builder.build()
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .tag(mContext)
                    .placeholder(R.drawable.posterunavailable)
                    .into(holder.poster);
        }

        String title = film.getTitle();
        if (!TextUtils.isEmpty(title)) {
            holder.title.setText(title);
        }

        String releaseDate = film.getReleaseDate();
        if (!TextUtils.isEmpty(releaseDate)) {
            String localDate = CalendarUtils.convertDateToLocale(mContext,
                    releaseDate);
            holder.releaseDate.setText(localDate);
        }
    }

    @Override
    public int getItemCount() {
        return filmsList.size();
    }

    public void updateData(List<Film> data) {
        filmsList.clear();
        filmsList.addAll(data);
        notifyDataSetChanged();
    }

    private void openFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(FILM_ID, id);

        mContext.startActivity(intent);
    }

    public View.OnClickListener filmOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(v);
            Film selectedFilm = filmsList.get(itemPosition);
            if (selectedFilm != null) {
                int id = selectedFilm.getId();
                openFilmDetail(id);
            }
        }
    };

    class FilmHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView releaseDate;

        public FilmHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.row_upcoming_poster_image);
            title = (TextView) itemView.findViewById(R.id.row_upcoming_title_text);
            releaseDate = (TextView) itemView.findViewById(R.id.row_upcoming_release_text);
        }


    }

}
