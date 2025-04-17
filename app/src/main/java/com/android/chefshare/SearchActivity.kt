package com.android.chefshare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_empty)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnFilter = findViewById<ImageView>(R.id.btnFilter)

        btnFilter.setOnClickListener {
            Toast.makeText(this, "Tính năng lọc đang phát triển", Toast.LENGTH_SHORT).show()
        }

        //sự kiện cho back
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val fab = findViewById<View>(R.id.fabContainer)
        val fabAddRecipe = fab.findViewById<FloatingActionButton>(R.id.fabAddRecipe)
        fabAddRecipe.setOnClickListener {
            val intent = Intent(this, UploadRecipeActivity::class.java)
            startActivity(intent)
        }

        // Bottom nav
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_storage
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("SELECTED_TAB", R.id.nav_home)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_ai -> {
                    Toast.makeText(this, "Tính năng AI đang phát triển", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_storage -> true
                else -> false
            }
        }
    }
}
