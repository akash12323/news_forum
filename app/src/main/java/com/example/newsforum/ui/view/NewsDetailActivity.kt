package com.example.newsforum.ui.view

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsforum.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_detail.*


class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val description = intent.getStringExtra("desc")
        val source = intent.getStringExtra("source")
        val url = intent.getStringExtra("url")
        val publishedAt = intent.getStringExtra("publishedAt")
        val content = intent.getStringExtra("content")

        Picasso.get().load(intent.getStringExtra("urlToImage")).into(backdrop)
        subtitle_on_appbar.setText(url)
        title_on_appbar.setText(source)
        titleOfNews.setText(title)
        date.setText(publishedAt)

        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }
}
