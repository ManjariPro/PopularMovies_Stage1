package com.propelld.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity
{
    TextView title, overview, releaseDate, voteAvg;
    ImageView imageView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.detail);
        setContentView(R.layout.activity_movie_details);

        title = (TextView) findViewById(R.id.original_title);
        overview = (TextView) findViewById(R.id.overview);
        releaseDate = (TextView) findViewById(R.id.releasedate);
        voteAvg = (TextView) findViewById(R.id.voteAverage);
        imageView = (ImageView)findViewById(R.id.thumbnail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int vote = bundle.getInt("rate");
        String originalTitle = bundle.getString("title");
        String over = bundle.getString("over");
        String release = bundle.getString("release");
        String url = bundle.getString("url");

        Picasso.with(context)
                .load(url)
                .into(imageView);

        title.setText(originalTitle);
        overview.setText(over);
        releaseDate.setText(release);
        voteAvg.setText(vote+"/10");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(intent,0);
        return true;
    }
}