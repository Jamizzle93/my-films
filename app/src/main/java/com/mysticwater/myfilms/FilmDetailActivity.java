package com.mysticwater.myfilms;

import com.google.gson.Gson;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.mysticwater.myfilms.fragments.FilmDetailFragment;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;
import com.squareup.picasso.Picasso;

import static com.mysticwater.myfilms.fragments.FilmDetailFragment.FILM_ID;
import static com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider.Films.CONTENT_URI;

public class FilmDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collapsing_toolbar);

        // Setup toolbar
        mToolbar = (Toolbar) findViewById(R.id.film_detail_toolbar);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
//            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    finish();
//                }
//            });
        }

        if (getIntent().hasExtra(FILM_ID)) {
            int filmId = getIntent().getIntExtra(FILM_ID, 0);

            Bundle bundle = new Bundle();
            bundle.putInt(FILM_ID, filmId);

            Film film = null;
            String[] selectionArgs = new String[]{String.valueOf(filmId)};
            Cursor allFilms = getContentResolver().query(
                    CONTENT_URI,
                    null,
                    FilmColumns.ID + " = ?",
                    selectionArgs,
                    null
            );

            if (allFilms != null) {
                allFilms.moveToFirst();
                String filmJson = allFilms.getString(allFilms.getColumnIndex(FilmColumns.FILM));
                film = new Gson().fromJson(filmJson, Film.class);
            }

            if (film != null)
            {
                ImageView toolbarImage = (ImageView)findViewById(R.id.film_backdrop);
                String backdropPath = film.getBackdropPath();
                if (!TextUtils.isEmpty(backdropPath)) {
                    String imageUri = getString(R.string.moviedb_backdrop_w1280_url, backdropPath);

                    Picasso.Builder builder = new Picasso.Builder(this);
                    builder.indicatorsEnabled(true);
                    builder.build()
                            .load(imageUri)
                            .tag(this)
                            .into(toolbarImage);
                } else {
                    toolbarImage.setImageResource(0);
                }
            }

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment filmDetailFragment = new FilmDetailFragment();
            filmDetailFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, filmDetailFragment);
            fragmentTransaction.commit();
        }
    }

    public void updateToolbarTitle(String title)
    {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }



}
