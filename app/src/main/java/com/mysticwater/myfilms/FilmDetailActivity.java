package com.mysticwater.myfilms;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mysticwater.myfilms.fragments.FilmDetailFragment;

import static com.mysticwater.myfilms.fragments.FilmDetailFragment.FILM_ID;

public class FilmDetailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frame);

        //TODO - Build the bundle
        if (getIntent().hasExtra(FILM_ID)) {
            int filmId = getIntent().getIntExtra(FILM_ID, 0);

            Bundle bundle = new Bundle();
            bundle.putInt(FILM_ID, filmId);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment filmDetailFragment = new FilmDetailFragment();
            filmDetailFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, filmDetailFragment);
            fragmentTransaction.commit();
        }
    }

}
