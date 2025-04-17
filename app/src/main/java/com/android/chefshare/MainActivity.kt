package com.android.chefshare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val popularIngredients = listOf(
        "Trứng", "Thịt bò", "Hành lá", "Tỏi", "Cà chua", "Rau thơm"
    )

    private val newIngredients = listOf(
        "Ớt chuông", "Măng tây", "Nấm đông cô", "Dưa leo", "Sả", "Khoai lang"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        // Gán thông tin user vào header view của Navigation Drawer
        val headerView = navigationView.getHeaderView(0)
        bindUserHeader(headerView, drawerLayout, navigationView)

        // Gán thông tin user vào header trên cùng của màn home
        bindTopHeader()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_friends -> showToast("Các Bạn Bếp")
                R.id.nav_stats -> showToast("Thống Kê Bếp")
                R.id.nav_recent -> {
                    val intent = Intent(this, RecentDishesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_premium -> showToast("Premium")
                R.id.nav_challenges -> showToast("Thử Thách")
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_faq -> showToast("Câu hỏi thường gặp")
                R.id.nav_feedback -> showToast("Gửi phản hồi")
            }
            drawerLayout.closeDrawer(navigationView)
            true
        }

        val glIngredients = findViewById<GridLayout>(R.id.glIngredients)
        displayIngredientSquares(glIngredients, popularIngredients)

        val llRecentIngredients = findViewById<LinearLayout>(R.id.llRecentIngredients)
        displayIngredientHorizontally(llRecentIngredients, newIngredients)

        val fab = findViewById<View>(R.id.fabContainer)
        val fabAddRecipe = fab.findViewById<FloatingActionButton>(R.id.fabAddRecipe)
        fabAddRecipe.setOnClickListener {
            val intent = Intent(this, UploadRecipeActivity::class.java)
            startActivity(intent)
        }

        val etSearchFake = findViewById<View>(R.id.etSearchFake)
        etSearchFake.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val searchBarContainer = findViewById<View>(R.id.searchBarContainer)
        searchBarContainer.setOnClickListener {
            Log.d("MainActivity", "Search bar clicked")
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val btnSubscribe = findViewById<Button>(R.id.btnSubscribe)
        btnSubscribe.setOnClickListener {
            val intent = Intent(this, PremiumActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_ai -> {
                    val intent = Intent(this, AIRecipeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.getIntExtra("SELECTED_TAB", R.id.nav_home)?.let { selectedTab ->
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigation.selectedItemId = selectedTab
        }
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

    private fun bindUserHeader(header: View, drawerLayout: DrawerLayout, navigationView: NavigationView) {
        val imgAvatar = header.findViewById<ImageView>(R.id.imgAvatar)
        val tvUsername = header.findViewById<TextView>(R.id.tvUsername)
        val tvHandle = header.findViewById<TextView>(R.id.tvHandle)
        val tvUserId = header.findViewById<TextView>(R.id.tvUserId)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val context = this

        val name = currentUser?.displayName
        val email = currentUser?.email
        val uid = currentUser?.uid
        val photoUrl = currentUser?.photoUrl?.toString()

        tvUsername.text = name ?: "Ẩn danh"
        tvHandle.text = email ?: "Không rõ"
        tvUserId.text = "ID: ${uid ?: "Không rõ"}"

        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(photoUrl)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(imgAvatar)
        } else {
            imgAvatar.setImageResource(R.drawable.ic_person)
        }
    }

    private fun bindTopHeader() {
        val imgHeaderAvatar = findViewById<ImageView>(R.id.imgHeaderAvatar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        val user = UserManager.currentUser
        val photoUrl = user?.photoUrl?.toString()

        imgHeaderAvatar.visibility = View.VISIBLE

        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(imgHeaderAvatar)
        } else {
            imgHeaderAvatar.setImageResource(R.drawable.ic_person) // 👉 hiển thị icon mặc định
        }

        // Thêm sự kiện click để mở Drawer
        imgHeaderAvatar.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

    }
}