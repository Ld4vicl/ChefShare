package com.android.chefshare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnEditProfile: Button
    private lateinit var imgAvatar: CircleImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvUserId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        imgAvatar = findViewById(R.id.imgAvatar)
        tvFullName = findViewById(R.id.tvFullName)
        tvUserId = findViewById(R.id.tvUserId)

        // Lấy thông tin user từ UserManager
        val user = UserManager.currentUser

        if (user != null) {
            tvFullName.text = user.displayName ?: "Ẩn danh"
            tvUserId.text = "ID: ${user.uid ?: "Không rõ"}"

            val avatarUrl = user.photoUrl?.toString()
            if (!avatarUrl.isNullOrEmpty()) {
                Glide.with(this@ProfileActivity)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(imgAvatar)
            } else {
                imgAvatar.setImageResource(R.drawable.ic_person)
            }
        } else {
            Toast.makeText(this, "Không có thông tin người dùng!", Toast.LENGTH_SHORT).show()
        }

        // Nút quay lại
        btnBack.setOnClickListener {
            finish()
        }

        // Nút sửa thông tin
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
