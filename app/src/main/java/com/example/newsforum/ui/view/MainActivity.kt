package com.example.newsforum.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsforum.R
import com.example.newsforum.data.api.Client
import com.example.newsforum.data.res.TopNewsArticlesItem
import com.example.newsforum.data.res.business.BusinessArticlesItem
import com.example.newsforum.data.res.entertainment.EntertainmentArticlesItem
import com.example.newsforum.data.res.health.HealthArticlesItem
import com.example.newsforum.data.res.politics.PoliticsArticlesItem
import com.example.newsforum.data.res.science.ScienceArticlesItem
import com.example.newsforum.data.res.search.SearchArticlesItem
import com.example.newsforum.data.res.sports.SportsArticlesItem
import com.example.newsforum.data.res.technology.TechnologyArticlesItem
import com.example.newsforum.ui.adapter.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val list = arrayListOf<TopNewsArticlesItem>()
    val topnewsadapter = TopNewsAdapter(list)

    val list2 = arrayListOf<SportsArticlesItem>()
    val sportsadapter = SportsAdapter(list2)

    val list3 = arrayListOf<PoliticsArticlesItem>()
    val politicsadapter = PoliticsAdapter(list3)

    val list4 = arrayListOf<HealthArticlesItem>()
    val healthadapter = HealthAdapter(list4)

    val list5 = arrayListOf<ScienceArticlesItem>()
    val scienceadapter = ScienceAdapter(list5)

    val list6 = arrayListOf<EntertainmentArticlesItem>()
    val entertainmentadapter = EntertainmentAdapter(list6)

    val list7 = arrayListOf<BusinessArticlesItem>()
    val businessadapter = BusinessAdapter(list7)

    val list8 = arrayListOf<TechnologyArticlesItem>()
    val technologyadapter = TechnologyAdapter(list8)

    val searchedlist = arrayListOf<SearchArticlesItem>()
    val searchadapter = SearchAdapter(searchedlist)

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

        navView.setNavigationItemSelectedListener(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
            adapter = topnewsadapter
        }
        topnewsadapter.onItemClick = {
            Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
            val i = Intent(this,
                NewsDetailActivity::class.java)

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
            val response = withContext(Dispatchers.IO){ Client.api.getTopNews("in") }

            if (response.isSuccessful){
                response.body()?.let {res->
                    res.articles?.let { list.addAll(it) }
                    runOnUiThread { topnewsadapter.notifyDataSetChanged() }
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
                LoadJson(newText.toString())
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
            if (toolbar.title == "Top Headlines"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = topnewsadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getTopNews("in") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list.addAll(it) }
                            runOnUiThread { topnewsadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Sports"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = sportsadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getSportsNews("in","sports") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list2.addAll(it) }
                            runOnUiThread { sportsadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Politics"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = politicsadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getPoliticsNews("in","politics") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list3.addAll(it) }
                            runOnUiThread { politicsadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Entertainment"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = entertainmentadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getEntertainmentNews("in",
                        "entertainment") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list6.addAll(it) }
                            runOnUiThread { entertainmentadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Health"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = healthadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getHealthNews("in",
                        "health") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list4.addAll(it) }
                            runOnUiThread { healthadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Science"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = scienceadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getScienceNews("in",
                        "science") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list5.addAll(it) }
                            runOnUiThread { scienceadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Technology"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = technologyadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getTechnologyNews("in",
                        "technology") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list8.addAll(it) }
                            runOnUiThread { technologyadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
            else if (toolbar.title == "Business"){
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = businessadapter
                }

                GlobalScope.launch {
                    val response = withContext(Dispatchers.IO){ Client.api.getBusinessNews("in",
                        "business") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { searchedlist.clear()
                                list7.addAll(it) }
                            runOnUiThread { businessadapter.notifyDataSetChanged() }
                        }
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.topnews->{
                Toast.makeText(this,"Top News Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Top Headlines"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = topnewsadapter
                }
                topnewsadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getTopNews("in") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list.addAll(it) }
                            runOnUiThread { topnewsadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener {  recyclerView.apply {
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
                    refresh1.isRefreshing = false
                }
            }
            R.id.sports->{
                Toast.makeText(this,"Sports Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Sports"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = sportsadapter
                }
                sportsadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getSportsNews("in","sports") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list2.addAll(it) }
                            runOnUiThread { sportsadapter.notifyDataSetChanged() }
                        }
                    }
                }

                refresh1.setOnRefreshListener {
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                        adapter = sportsadapter
                    }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getSportsNews("in","sports") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list2.clear()
                                    list2.addAll(it) }
                                runOnUiThread { sportsadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.politics->{
                Toast.makeText(this,"Politics Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Politics"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = politicsadapter
                }
                politicsadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getPoliticsNews("in",
                        "politics") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list3.addAll(it) }
                            runOnUiThread { politicsadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener {  recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = politicsadapter
                }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getPoliticsNews("in",
                            "politics") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list3.clear()
                                    list3.addAll(it) }
                                runOnUiThread { politicsadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.entertainment->{
                Toast.makeText(this,"Entertainment Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Entertainment"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = entertainmentadapter
                }
                entertainmentadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getEntertainmentNews("in",
                        "entertainment") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list6.addAll(it) }
                            runOnUiThread { entertainmentadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener {
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                        adapter = entertainmentadapter
                    }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getEntertainmentNews("in",
                            "entertainment") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list6.clear()
                                    list6.addAll(it) }
                                runOnUiThread { entertainmentadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.health->{
                Toast.makeText(this,"Health Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Health"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = healthadapter
                }
                healthadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getHealthNews("in",
                        "health") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list4.addAll(it) }
                            runOnUiThread { healthadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener {  recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = healthadapter
                }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getHealthNews("in",
                            "health") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list4.clear()
                                    list4.addAll(it) }
                                runOnUiThread { healthadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.science->{
                Toast.makeText(this,"Science Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Science"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = scienceadapter
                }
                scienceadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getScienceNews("in",
                        "science") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list5.addAll(it) }
                            runOnUiThread { scienceadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener { recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = scienceadapter
                }
                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getScienceNews("in",
                            "science") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list5.clear()
                                    list5.addAll(it) }
                                runOnUiThread { scienceadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.technology->{
                Toast.makeText(this,"Technology Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Technology"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = technologyadapter
                }
                technologyadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getTechnologyNews("in",
                        "technology") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list8.addAll(it) }
                            runOnUiThread { technologyadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener { recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = technologyadapter
                }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getTechnologyNews("in",
                            "technology") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list8.clear()
                                    list8.addAll(it) }
                                runOnUiThread { technologyadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
            R.id.business->{
                Toast.makeText(this,"Business Pressed", Toast.LENGTH_SHORT).show()
                toolbar.title = "Business"

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = businessadapter
                }
                businessadapter.onItemClick = {
                    val i = Intent(this,
                        NewsDetailActivity::class.java)
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
                    val response = withContext(Dispatchers.IO){ Client.api.getBusinessNews("in",
                        "business") }

                    if (response.isSuccessful){
                        response.body()?.let {res->
                            res.articles?.let { list7.addAll(it) }
                            runOnUiThread { businessadapter.notifyDataSetChanged() }
                        }
                    }
                }
                refresh1.setOnRefreshListener {  recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = businessadapter
                }

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO){ Client.api.getBusinessNews("in",
                            "business") }

                        if (response.isSuccessful){
                            response.body()?.let {res->
                                res.articles?.let { list7.clear()
                                    list7.addAll(it) }
                                runOnUiThread { businessadapter.notifyDataSetChanged() }
                            }
                        }
                    }
                    refresh1.isRefreshing = false
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
