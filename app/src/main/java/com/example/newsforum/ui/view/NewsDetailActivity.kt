package com.example.newsforum.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsforum.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_detail.*


class NewsDetailActivity : AppCompatActivity() {

    lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorTransparent)
        }

        setSupportActionBar(toolbar)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val description = intent.getStringExtra("desc")
        val source = intent.getStringExtra("source")
        url = intent.getStringExtra("url")
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id == R.id.share){

            try{
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plan"
                i.putExtra(Intent.EXTRA_SUBJECT,Uri.parse("source"))

                i.putExtra(Intent.EXTRA_TEXT,url)
                startActivity(Intent.createChooser(i,"Share with: "))
            }
            catch (e:Exception){
                Toast.makeText(this,"cannot share",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
