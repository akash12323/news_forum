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
import com.example.newsforum.data.res.search.SearchArticlesItem
import com.example.newsforum.data.res.technology.TechnologyArticlesItem
import com.example.newsforum.ui.adapter.SearchAdapter
import com.example.newsforum.ui.adapter.TechnologyAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TechnologyActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val list8 = arrayListOf<TechnologyArticlesItem>()
    val technologyadapter = TechnologyAdapter(list8)

    val searchedlist = arrayListOf<SearchArticlesItem>()
    val searchadapter = SearchAdapter(searchedlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technology)

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

        toolbar.title = "Technology"

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TechnologyActivity,
                RecyclerView.VERTICAL,false)
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
                    res.articles?.let { list8.clear()
                        list8.addAll(it) }
                    runOnUiThread { technologyadapter.notifyDataSetChanged() }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.topnews ->{
                Toast.makeText(this,"Top News Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                toolbar.title = "Top Headlines"
            }
            R.id.sports ->{
                Toast.makeText(this,"Sports Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    SportsActivity::class.java))

            }
            R.id.entertainment ->{
                Toast.makeText(this,"Entertainment Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    EntertainmentActivity::class.java))

            }
            R.id.health ->{
                Toast.makeText(this,"Health Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    HealthActivity::class.java))

            }
            R.id.science ->{
                Toast.makeText(this,"Science Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    ScienceActivity::class.java))

            }
            R.id.technology ->{
                Toast.makeText(this,"Technology Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,
                    TechnologyActivity::class.java))

            }
            R.id.business ->{
                Toast.makeText(this,"Business Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,BusinessActivity::class.java))
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
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
                layoutManager = LinearLayoutManager(this@TechnologyActivity,RecyclerView.VERTICAL,false)
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
                layoutManager = LinearLayoutManager(this@TechnologyActivity,RecyclerView.VERTICAL,false)
                adapter = technologyadapter
            }

            GlobalScope.launch {
                val response = withContext(Dispatchers.IO){ Client.api.getTechnologyNews("in",
                    "technology") }

                if (response.isSuccessful){
                    response.body()?.let {res->
                        res.articles?.let { searchedlist.clear()
                            list8.clear()
                            list8.addAll(it) }
                        runOnUiThread { technologyadapter.notifyDataSetChanged() }
                    }
                }
            }
        }
    }

}
