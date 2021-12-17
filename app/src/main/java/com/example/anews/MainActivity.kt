package com.example.anews

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.anews.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter:NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.recycleView.layoutManager=LinearLayoutManager(this)
         fetchData()

        mAdapter = NewsListAdapter(this)
        binding.recycleView.adapter = mAdapter

    }
    private fun fetchData() {

        val url =
            "https://saurav.tech/NewsAPI/everything/cnn.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                    val newsJsonArray= it.getJSONArray("articles")
                    val newsArray=ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)

                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }
        )
      MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder();
       val  customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));

    }
}