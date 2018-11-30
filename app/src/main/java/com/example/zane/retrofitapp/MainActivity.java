package com.example.zane.retrofitapp;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zane.retrofitapp.model.Feed;
import com.example.zane.retrofitapp.model.children.Children;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.zane.retrofitapp.RedditAPI.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://www.reddit.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getDataBtn = (Button) findViewById(R.id.getDataBtn);
        final TextView dataTextView = (TextView) findViewById(R.id.dataTextView);
        //enable scrollable text view
        dataTextView.setMovementMethod(new ScrollingMovementMethod());

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RedditAPI redditAPI = retrofit.create(RedditAPI.class);
                Call<Feed> call = redditAPI.getData();

                call.enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {
                        Log.d(TAG, "onResponse: Server Response" + response.toString());
                        Log.d(TAG, "onResponse: Received Info" + response.body().toString());

                        ArrayList<Children> childrenList = response.body().getData().getChildren();

                        //Load info into our TextView
                        for(int i = 0; i < childrenList.size(); i++){
                            dataTextView.append("kind: " + childrenList.get(i).getKind()+ "\n");
                            dataTextView.append("contest_mode: " + childrenList.get(i).getData().getContest_mode() + "\n");
                            dataTextView.append("subreddit: " + childrenList.get(i).getData().getSubreddit() + "\n");
                            dataTextView.append("author: " + childrenList.get(i).getData().getAuthor() + "\n" +
                                    "-------------------------------------------------------------------------\n\n");

                        }
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        Log.e(TAG, "onFailure: FAIL" + t.getMessage());
                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
