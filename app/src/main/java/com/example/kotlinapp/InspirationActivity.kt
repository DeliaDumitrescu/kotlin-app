package com.example.kotlinapp
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class InspirationActivity : AppCompatActivity() {
    private lateinit var dataAdapter: DataAdapter
    private var data = ArrayList<String>()
    private var images = HashMap<String, Int>()

    private fun initData() {
        data.add("Travel")
        images.put("Travel", R.drawable.travel)

        data.add("Friends")
        images.put("Friends", R.drawable.friends)

        data.add("Beach")
        images.put("Beach", R.drawable.beach)

        data.add("Food")
        images.put("Food", R.drawable.food)

        data.add("Dance")
        images.put("Dance", R.drawable.dance)

        data.add("Sun")
        images.put("Sun", R.drawable.sun)

        data.add("Fun")
        images.put("Fun", R.drawable.having_fun)

        data.add("Relaxing")
        images.put("Relaxing", R.drawable.relaxing)

        data.add("Chill")
        images.put("Chill", R.drawable.chill)

        data.add("Party")
        images.put("Party", R.drawable.party)

        data.add("Cat")
        images.put("Cat", R.drawable.cat)

        data.add("Cocktail")
        images.put("Cocktail", R.drawable.cocktail)

        data.add("Music")
        images.put("Music", R.drawable.music)

        data.add("Be happy")
        images.put("Be happy", R.drawable.behappy)

        data.add("Rain")
        images.put("Rain", R.drawable.rain)

        data.add("Dog")
        images.put("Dog", R.drawable.dog)

        dataAdapter.notifyDataSetChanged()
    }

    private fun filter(text: String) {
        val filteredData: ArrayList<String> = ArrayList()

        for (item in data) {
            if (item.contains(text, ignoreCase = true)) {
                filteredData.add(item)
            }
        }

        dataAdapter.filterList(filteredData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspiration)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(applicationContext)
        dataAdapter = DataAdapter(data, images)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = dataAdapter
        initData()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.inspiration
        
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.inspiration -> true
                R.id.photo -> {
                    startActivity(Intent(applicationContext, TakePhotoActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.getActionView() as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(inputText: String?): Boolean {
                filter(inputText!!)
                return false
            }
        })
        return true
    }
}