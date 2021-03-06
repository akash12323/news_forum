package com.example.newsforum.ui.view

import android.app.NotificationChannel
import android.app.StatusBarManager
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsforum.*
import com.example.newsforum.data.api.Client
import com.example.newsforum.data.res.TopNewsArticlesItem
import com.example.newsforum.data.res.search.SearchArticlesItem
import com.example.newsforum.ui.adapter.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val list = arrayListOf<TopNewsArticlesItem>()
    val topnewsadapter = TopNewsAdapter(list)

    val searchedlist = arrayListOf<SearchArticlesItem>()
    val searchadapter = SearchAdapter(searchedlist)

    private lateinit var interstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawer.addDrawerListener(toogle)
        toogle.syncState()

        //adss
        MobileAds.initialize(this){}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        navView.setNavigationItemSelectedListener(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
            adapter = topnewsadapter
        }
        topnewsadapter.onItemClick = {
            Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
            val i = Intent(this, NewsDetailActivity::class.java)

            i.putExtra("title",it.title.toString())
            i.putExtra("author",it.author.toString())
            i.putExtra("urlToImage",it.urlToImage.toString())
            i.putExtra("desc",it.description.toString())
            i.putExtra("source",it.source?.name.toString())
            i.putExtra("url",it.url.toString())
            i.putExtra("publishedAt",it.publishedAt.toString())
            i.putExtra("content",it.content.toString())

            startActivity(i)
        }

        var connectivityManager = this.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = connectivityManager.getActiveNetworkInfo()

        if(networkInfo != null  && networkInfo.isConnected){
            loadHeadlines()
        }
        else if(networkInfo == null){
            ll3.visibility = View.GONE
            ll2.visibility = View.GONE
            error.visibility = View.VISIBLE
        }

        if(networkInfo != null  && networkInfo.isConnected){
            btn.setOnClickListener {
                loadHeadlines()
            }
        }

    }

    private fun loadHeadlines() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO){ Client.api.getTopNews("in") }

            if (response.isSuccessful){
                response.body()?.let {res->
                    res.articles?.let { list.clear()
                        list.addAll(it) }
                    runOnUiThread { topnewsadapter.notifyDataSetChanged()
                        ll3.visibility = View.GONE
                        ll2.visibility = View.VISIBLE
                        error.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2,menu)

        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setQueryHint("type here to search")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.length > 2){
                    LoadJson(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length>30){
                    LoadJson(newText.toString())
                }
                else if (newText!!.length<=0){
                    LoadJson(newText.toString())
                }
                return false
            }

        })

        item.icon.setVisible(false,false)
        return true
    }

    public fun LoadJson(keyword:String){
        if (keyword.length > 2){
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                adapter = searchadapter
            }
            searchadapter.onItemClick = {
                val i = Intent(this,NewsDetailActivity::class.java)

                i.putExtra("title",it.title.toString())
                i.putExtra("author",it.author.toString())
                i.putExtra("urlToImage",it.urlToImage.toString())
                i.putExtra("desc",it.description.toString())
                i.putExtra("source",it.source?.name.toString())
                i.putExtra("url",it.url.toString())
                i.putExtra("publishedAt",it.publishedAt.toString())
                i.putExtra("content",it.content.toString())

                startActivity(i)
            }
            GlobalScope.launch {
                val response = withContext(Dispatchers.IO){
                    Client.api.searchNews(keyword,"publishedAt","1c0fedf4d3414f9389a056a833ce10d2")
                }
                if (response.isSuccessful){
                    response.body()?.let { res->
                        res.articles?.let { searchedlist.clear()
                            searchedlist.addAll(it) }
                        runOnUiThread { searchadapter.notifyDataSetChanged() }
                    }
                }
            }
        }
        else{
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                adapter = topnewsadapter
            }
            GlobalScope.launch {
                val response = withContext(Dispatchers.IO){ Client.api.getTopNews("in") }

                if (response.isSuccessful){
                    response.body()?.let {res->
                        res.articles?.let { list.clear()
                            list.addAll(it) }
                        runOnUiThread { topnewsadapter.notifyDataSetChanged() }
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
//            R.id.topnews->{
//                Toast.makeText(this,"Top News Pressed", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,MainActivity::class.java))
//                toolbar.title = "Top Headlines"
//
//                interstitialAd = InterstitialAd(this)
//                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
//                interstitialAd.loadAd(AdRequest.Builder().build())
//                if (interstitialAd.isLoaded){
//                    interstitialAd.show()
//                }
//                else{
//                    Log.d("TAG", "The interstitial wasn't loaded yet.")
//                }
//
//                finish()
//            }
            R.id.sports->{
                Toast.makeText(this,"Sports Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    SportsActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()

            }
            R.id.entertainment->{
                Toast.makeText(this,"Entertainment Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    EntertainmentActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()

            }
            R.id.health->{
                Toast.makeText(this,"Health Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    HealthActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()
            }
            R.id.science->{
                Toast.makeText(this,"Science Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    ScienceActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()
            }
            R.id.technology->{
                Toast.makeText(this,"Technology Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    TechnologyActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()
            }
            R.id.business->{
                Toast.makeText(this,"Business Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,BusinessActivity::class.java))

                interstitialAd = InterstitialAd(this)
                interstitialAd.adUnitId = "ca-app-pub-2963035765518004/9649364662"
                interstitialAd.loadAd(AdRequest.Builder().build())
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
                else{
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }

                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
