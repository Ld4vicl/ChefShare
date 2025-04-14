package com.android.chefshare.view

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.android.chefshare.databinding.ActivityHomeBinding
import com.android.chefshare.databinding.ItemIngredientChipBinding
import com.android.chefshare.databinding.ItemIngredientSquareBinding
import com.google.android.material.navigation.NavigationView
import com.android.chefshare.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val popularIngredients = listOf(
        "Trứng", "Thịt bò", "Hành lá", "Tỏi", "Cà chua", "Rau thơm"
    )

    private val newIngredients = listOf(
        "Ớt chuông", "Măng tây", "Nấm đông cô", "Dưa leo", "Sả", "Khoai lang"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mở menu khi bấm avatar
        binding.tvAvatar.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Xử lý chọn menu Navigation
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
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
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Hiển thị nguyên liệu phổ biến (Grid)
        displayIngredientSquares()

        // Hiển thị nguyên liệu mới lên sóng (Horizontal)
        displayIngredientHorizontally()
    }

    private fun displayIngredientSquares() {
        val inflater = LayoutInflater.from(this)
        binding.glIngredients.removeAllViews()

        for (item in popularIngredients) {
            val itemView = ItemIngredientSquareBinding.inflate(inflater, binding.glIngredients, false)
            itemView.tvIngredientName.text = item
            binding.glIngredients.addView(itemView.root)
        }
    }

    private fun displayIngredientHorizontally() {
        val inflater = LayoutInflater.from(this)
        binding.llRecentIngredients.removeAllViews()

        for (item in newIngredients) {
            val itemView = ItemIngredientChipBinding.inflate(inflater, binding.llRecentIngredients, false)
            itemView.tvIngredientName.text = item
            binding.llRecentIngredients.addView(itemView.root)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
