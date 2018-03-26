package com.propelld.app.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ArrayList<Movies> moviesArrayList;
    MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.pop);
        setContentView(R.layout.activity_main);

        moviesArrayList = new ArrayList<Movies>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        moviesAdapter = new MoviesAdapter(moviesArrayList, MainActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Configuration.NO_OF_COLUMN_IN_GRID));
        recyclerView.setAdapter(moviesAdapter);

        try
        {
            MoviesTask moviesTask = new MoviesTask();
            moviesTask
                    .execute(Configuration.POPULAR_MOVIES_URL)
                    .get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    public  class  MoviesTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            StringBuilder stringBuilder = new StringBuilder();

            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return stringBuilder.toString() ;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            try
            {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                moviesArrayList.clear();

                for ( int i = 0; i <jsonArray.length(); i++)
                {
                    Movies movies = new Movies();

                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String poster = jsonObj.getString("poster_path");
                    String originalTitle = jsonObj.getString("original_title");
                    String overview = jsonObj.getString("overview");
                    String release_date = jsonObj.getString("release_date");
                    int vote_average = jsonObj.getInt("vote_average");

                    // setting movie
                    movies.setPoster_path(poster);
                    movies.setOriginal_title(originalTitle);
                    movies.setOverview(overview);
                    movies.setRelease_date(release_date);
                    movies.setVote_average(vote_average);

                    moviesArrayList.add(movies);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                moviesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater()
                .inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_popular:

                setTitle(R.string.pop);
                MoviesTask moviesTask = new MoviesTask();
                moviesTask.execute(Configuration.POPULAR_MOVIES_URL);
                return true;

            case R.id.menu_rated:

                setTitle(R.string.top);
                MoviesTask moviesTask1 = new MoviesTask();
                moviesTask1.execute(Configuration.TOP_RATED_MOVIES_URL);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}