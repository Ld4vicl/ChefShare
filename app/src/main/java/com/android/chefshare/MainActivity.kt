package com.android.chefshare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val popularIngredients = listOf(
        "Trứng", "Thịt bò", "Hành lá", "Tỏi", "Cà chua", "Rau thơm"
    )

    private val newIngredients = listOf(
        "Ớt chuông", "Măng tây", "Nấm đông cô", "Dưa leo", "Sả", "Khoai lang"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val avatar = findViewById<View>(R.id.tvAvatar)

        // mở menu khi bấm avatar
        avatar.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        //Xử lý chọn menu trong NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> showToast("Bếp Cá Nhân")
                R.id.nav_friends -> showToast("Các Bạn Bếp")
                R.id.nav_stats -> showToast("Thống Kê Bếp")
                R.id.nav_recent -> showToast("Món đã xem gần đây")
                R.id.nav_premium -> showToast("Premium")
                R.id.nav_challenges -> showToast("Thử Thách")
                R.id.nav_settings -> showToast("Cài Đặt")
                R.id.nav_faq -> showToast("Câu hỏi thường gặp")
                R.id.nav_feedback -> showToast("Gửi phản hồi")
            }
            drawerLayout.closeDrawer(navigationView)
            true
        }

        //Hiển thị nguyên liệu phổ biến (Grid)
        val glIngredients = findViewById<GridLayout>(R.id.glIngredients)
        displayIngredientSquares(glIngredients, popularIngredients)

        //Hiển thị nguyên liệu mới lên sóng (horizoncal )
        val llRecentIngredients = findViewById<LinearLayout>(R.id.llRecentIngredients)
        displayIngredientHorizontally(llRecentIngredients, newIngredients)
    }

    private fun displayIngredientSquares(gridLayout: GridLayout, items: List<String>) {
        val inflater = LayoutInflater.from(this)
        gridLayout.removeAllViews()

        for (item in items) {
            val itemView = inflater.inflate(R.layout.item_ingredient_square, gridLayout, false)
            val tvName = itemView.findViewById<TextView>(R.id.tvIngredientName)
            tvName.text = item
            gridLayout.addView(itemView)
        }
    }

    private fun displayIngredientHorizontally(container: LinearLayout, items: List<String>) {
        val inflater = LayoutInflater.from(this)
        container.removeAllViews()

        for (item in items) {
            val itemView = inflater.inflate(R.layout.item_ingredient_chip, container, false)
            val tvName = itemView.findViewById<TextView>(R.id.tvIngredientName)
            tvName.text = item
            container.addView(itemView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
