package com.android.chefshare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AIRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_recipe_generator)

        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val tvRecipeResult = findViewById<TextView>(R.id.tvRecipeResult)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        //button back
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // quay lại màn hình trước
        }

        btnGenerate.setOnClickListener {
            val sampleRecipe = """
                🍲 Gợi ý công thức:
                - Ức gà chiên nước mắm
                - Nguyên liệu: ức gà, nước mắm, tỏi, đường
                - Cách làm: Ướp ức gà với gia vị, chiên vàng giòn, rưới nước mắm tỏi...
            """.trimIndent()

            tvRecipeResult.text = sampleRecipe
            tvRecipeResult.visibility = View.VISIBLE

            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_ai
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("SELECTED_TAB", R.id.nav_home)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_ai -> true
                R.id.nav_storage -> {
                    val intent = Intent(this, MyRecipeStorageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
