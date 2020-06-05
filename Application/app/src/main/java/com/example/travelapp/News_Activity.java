package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.View;
import android.widget.Toast;

import com.example.travelapp.Api_News.ApiClient_News;
import com.example.travelapp.Api_News.ApiInterface_News;
import com.example.travelapp.News.Article;
import com.example.travelapp.News.News;
import com.example.travelapp.News.News_Adapter;
import com.example.travelapp.News.Utils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News_Activity extends AppCompatActivity {
    public static final String API_KEY="fe986d75e12d409db68cb9bec4bda83a";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private News_Adapter adapter;
    private String TAG = News_Activity.class.getSimpleName();
    SharedPreferences sharedPref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        sharedPref1 = getSharedPreferences("country", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(News_Activity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        LoadJSon();
        //recyclerView.setAdapter(adapter);
    }
    public void LoadJSon(){
        ApiInterface_News apiInterface_news = ApiClient_News.getApiClient_News().create(ApiInterface_News.class);
        //String country = Utils.getCountry();
        String country = sharedPref1.getString("country_name","");
        String language = Utils.getLanguage();
        Call<News> call;
        call = apiInterface_news.getNews(country,API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response)  {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    if(response.body().getArticles() == null){
                        System.out.println("Response Is null !");
                    }
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    adapter = new News_Adapter(articles,News_Activity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                }
                else{
                    Toast.makeText(News_Activity.this,"NO Results",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
            }
        });
    }
    private void initListener(){
        adapter.setOnItemClickListener(new News_Adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v , int position){
                Intent intent = new Intent(News_Activity.this,NewsDetailActivity.class);
                Article article = articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("author",article.getAuthor());
                startActivity(intent);
            }
        });
    }
}
